package com.bavuchoko.jsparkgolf.repository

import com.bavuchoko.jsparkgolf.dto.UserRequestDto
import com.bavuchoko.jsparkgolf.service.UserApiService

class UserRepository (private val userApiService: UserApiService){

    suspend fun login(username: String, password: String): String? {
        val userRequest = UserRequestDto(username,password)
        val response = userApiService.loginUser(userRequest)

        return if(response.isSuccessful){
            response.body()
        }else{
            null
        }

    }
}