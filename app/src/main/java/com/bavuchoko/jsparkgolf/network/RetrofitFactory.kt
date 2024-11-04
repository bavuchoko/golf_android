package com.bavuchoko.jsparkgolf.network

import android.content.Context
import com.bavuchoko.jsparkgolf.common.AuthInterceptor
import com.bavuchoko.jsparkgolf.service.UserApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitFactory {

    //    private const val BASE_URL = "https://todoro.co.kr"
    private const val BASE_URL = "https://todoro.co.kr/api/"


    fun create(context: Context): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val userApiService = retrofit.create(UserApiService::class.java)

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context, userApiService))
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}