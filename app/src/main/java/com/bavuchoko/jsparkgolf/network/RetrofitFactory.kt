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
    private const val BASE_URL = "http://10.0.2.2:8443"

    fun create(context: Context): Retrofit {
        val cf = CertificateFactory.getInstance("X.509")
        val caInputStream: InputStream = context.resources.openRawResource(R.raw.parkgolf)
        val ca = cf.generateCertificate(caInputStream)
        caInputStream.close()

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(keyStore)

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, SecureRandom())

        val client = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, tmf.trustManagers[0] as X509TrustManager)
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