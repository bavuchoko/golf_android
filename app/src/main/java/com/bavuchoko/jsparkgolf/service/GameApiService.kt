package com.bavuchoko.jsparkgolf.service

import com.bavuchoko.jsparkgolf.dto.request.QuickGameRequestDto
import com.bavuchoko.jsparkgolf.response.GameResponse
import com.bavuchoko.jsparkgolf.vo.GameVo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GameApiService {
    @GET("game")
    suspend fun getGameList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("status") status: String? = null,
        @Query("player") player: Boolean = false,
        @Query("city") city: String? = null
    ) : Response<GameResponse>


    @POST("game/quick")
    suspend fun quickStartGame(
        @Body request: QuickGameRequestDto
    ) : Response<GameVo>

}