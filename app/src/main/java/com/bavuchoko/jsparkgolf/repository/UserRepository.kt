package com.bavuchoko.jsparkgolf.repository

import com.bavuchoko.jsparkgolf.dto.request.UserRequestDto
import com.bavuchoko.jsparkgolf.service.UserApiService

class UserRepository (private val userApiService: UserApiService){

    suspend fun login(username: String, password: String): String? {
        val userRequest = UserRequestDto(username,password)
        return  try {
            val response = userApiService.loginUser(userRequest)
            if(response.isSuccessful){
                response.body()
            }else{
                null
            }
        }catch (e: Exception){
            e.printStackTrace()
            null
        }


    }
}