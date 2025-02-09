package com.bavuchoko.jsparkgolf.service

import com.bavuchoko.jsparkgolf.dto.request.RegionRequestDto
import com.bavuchoko.jsparkgolf.dto.request.UserRequestDto
import com.bavuchoko.jsparkgolf.vo.GameVo
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {

    @POST("auth/login")
    suspend fun loginUser(@Body userRequestDto: UserRequestDto): Response<String>


    @GET("user/region")
    suspend fun getUserRegion(): Response<String>


    @POST("user/region")
    suspend fun saveUserRegion(@Body region: RegionRequestDto): Response<String>


    @POST("auth/reissue")
    suspend fun refreshToken(): Response<ResponseBody>


}