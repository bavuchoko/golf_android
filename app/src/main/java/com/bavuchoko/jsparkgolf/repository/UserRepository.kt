package com.bavuchoko.jsparkgolf.repository

import com.bavuchoko.jsparkgolf.common.CommonMethod
import com.bavuchoko.jsparkgolf.dto.request.UserRequestDto
import com.bavuchoko.jsparkgolf.service.UserApiService

class UserRepository (private val userApiService: UserApiService){

    suspend fun login(username: String, password: String): Pair<String?, String?> {
        val userRequest = UserRequestDto(username,password)
        return  try {
            val response = userApiService.loginUser(userRequest)
            if (response.isSuccessful) {
                val cookies = response.headers()["Set-Cookie"]
                val refreshToken = extractRefreshToken(cookies)
                Pair(response.body(), refreshToken)
            } else {
                Pair(null, null)
            }
        }catch (e: Exception){
            e.printStackTrace()
            Pair(null, null)
        }
    }

    private fun extractRefreshToken(cookies: String?): String? {
        // 예: "refresh_token=your_token_value; Path=/; HttpOnly"
        cookies?.split(";")?.forEach {
            if (it.trim().startsWith("refreshToken=")) {
                return it.split("=")[1]
            }
        }
        return null
    }
}