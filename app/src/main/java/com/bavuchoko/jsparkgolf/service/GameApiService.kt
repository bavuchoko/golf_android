package com.bavuchoko.jsparkgolf.service

import com.bavuchoko.jsparkgolf.response.GameResponse
import retrofit2.Response
import retrofit2.http.GET
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
}