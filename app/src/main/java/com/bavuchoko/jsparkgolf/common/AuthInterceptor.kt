package com.bavuchoko.jsparkgolf.common

import android.content.Context
import com.bavuchoko.jsparkgolf.service.UserApiService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context,
    private val userApiService: UserApiService,) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = CommonMethod.getToken(context)
        val request = chain.request()
        val urlPath = request.url().encodedPath()

        // 인증이 필요한 요청은 /api/auth/login이 아닌 모든 요청으로 설정
        val requiresAuth = !urlPath.contains("/api/auth/login")

        val newRequest = if (requiresAuth) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        var response = chain.proceed(newRequest)

        if (!response.isSuccessful && response.code() == 401) {
            // 토큰 갱신 시도
            val newToken = runBlocking {
                try {
                    val refreshResponse = userApiService.refreshToken() // 토큰 갱신 요청
                    if (refreshResponse.isSuccessful) {
                        refreshResponse.body()
                    } else {
                        null // 갱신 실패
                    }
                } catch (e: Exception) {
                    null // 예외 발생 시 null 반환
                }
            }
            if (newToken != null) {
                // 갱신된 토큰으로 다시 요청 시도
                val requestWithNewAuth = request.newBuilder()
                    .addHeader("Authorization", "Bearer $newToken")
                    .build()

                response.close() // 이전 응답 닫기
                response = chain.proceed(requestWithNewAuth)
            } else {
                // 갱신 실패 시 로그아웃
                CommonMethod.clearValue(context,"accessToken");
            }
        }

        return response
    }
}


