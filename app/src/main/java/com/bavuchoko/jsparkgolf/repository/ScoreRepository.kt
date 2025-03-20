package com.bavuchoko.jsparkgolf.repository

import com.bavuchoko.jsparkgolf.service.ScoreService
import com.bavuchoko.jsparkgolf.vo.ScoreVo

class ScoreRepository(private val scoreService: ScoreService) {

    suspend fun saveScores(gameId: Long, scores: List<ScoreVo>) : Result<List<ScoreVo>> {
        return try {
            val response = scoreService.saveScores(gameId, scores)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("등록 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}