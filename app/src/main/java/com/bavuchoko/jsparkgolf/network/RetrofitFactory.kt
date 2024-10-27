package com.bavuchoko.jsparkgolf.network

import android.content.Context
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.service.UserApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object RetrofitFactory {

    //    private const val BASE_URL = "https://todoro.co.kr"
    private const val BASE_URL = "https://todoro.co.kr/api/"

    fun create(context: Context): Retrofit {

        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createUserApiService(context: Context): UserApiService {
        return create(context).create(UserApiService::class.java)
    }


//    fun createGameApiService(context: Context): GameApiService {
//        return create(context).create(GameApiService::class.java)
//    }
}