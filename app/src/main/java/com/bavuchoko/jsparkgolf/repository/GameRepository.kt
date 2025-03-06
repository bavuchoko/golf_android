package com.bavuchoko.jsparkgolf.repository

import com.bavuchoko.jsparkgolf.dto.request.QuickGameRequestDto
import com.bavuchoko.jsparkgolf.service.GameApiService
import com.bavuchoko.jsparkgolf.vo.GameVo
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class GameRepository(private val apiService: GameApiService) {

    suspend fun getList(page: Int, size: Int, status: String, player: Boolean, city: String?): Result<List<GameVo>> {
        return try {
            val response = apiService.getGameList(page, size, status, player, city)
            if (response.isSuccessful) {
                val contentList = response.body()?.content ?: emptyList()
                Result.success(contentList)
            } else {
                Result.failure(Exception("조회 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun quickStartGame(request: QuickGameRequestDto) : Result<GameVo> {
        return try {
            val response = apiService.quickStartGame(request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("시작 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}