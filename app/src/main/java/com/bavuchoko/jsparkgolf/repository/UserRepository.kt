package com.bavuchoko.jsparkgolf.repository

import android.content.Context
import android.util.Log
import com.bavuchoko.jsparkgolf.common.CommonMethod
import com.bavuchoko.jsparkgolf.dto.request.RegionRequestDto
import com.bavuchoko.jsparkgolf.dto.request.UserRequestDto
import com.bavuchoko.jsparkgolf.service.UserApiService

class UserRepository (private val userApiService: UserApiService, private val context: Context){

    suspend fun login(username: String, password: String): Pair<String?, String?> {
        val response = userApiService.loginUser(UserRequestDto(username, password))
        return if (response.isSuccessful) {
            val accessToken = response.body()
            val refreshToken = response.headers()["Set-Cookie"]?.let { extractRefreshToken(it) }

            accessToken?.let { saveAccessToken(it) }
            refreshToken?.let { saveValue("refreshToken", it) }
            val region = accessToken?.let { fetchUserRegion(it) }
            region?.let { saveValue("region", it) }
            Pair(accessToken, region)
        } else {
            Pair(null, null)
        }
    }

    suspend fun updateUserRegion(sido: String): String? {
        return try {
            val response = userApiService.saveUserRegion(RegionRequestDto(sido))
            if (response.isSuccessful) {
                val region = response.body()
                region?.let {
                    saveValue("region", it)
                }
                region
            } else {
                Log.e("API", "Failed to save region: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("API", "Exception: ${e.message}")
            null
        }
    }


    private fun saveAccessToken(token: String) {
        CommonMethod.saveAccessToken(context, token)
    }

    private fun saveValue(key:String, value: String) {
        CommonMethod.saveValue(context, key, value)
    }

    private suspend fun fetchUserRegion(accessToken: String): String? {
        return try {
            val response = userApiService.getUserRegion()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun extractRefreshToken(cookies: String?): String? {
        cookies?.split(";")?.forEach {
            if (it.trim().startsWith("refreshToken=")) {
                return it.split("=")[1]
            }
        }
        return null
    }
}