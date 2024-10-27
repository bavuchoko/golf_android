package com.bavuchoko.jsparkgolf.service

import com.bavuchoko.jsparkgolf.dto.request.UserRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("auth/login")
    suspend fun loginUser(@Body userRequestDto: UserRequestDto): Response<String>
}