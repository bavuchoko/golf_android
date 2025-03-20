package com.bavuchoko.jsparkgolf.service

import com.bavuchoko.jsparkgolf.vo.ScoreVo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ScoreService {


    @POST("score/{id}")
    suspend fun saveScores(
        @Path("id") gameId: Long,
        @Body scores: List<ScoreVo>
    ) : Response<List<ScoreVo>>
}