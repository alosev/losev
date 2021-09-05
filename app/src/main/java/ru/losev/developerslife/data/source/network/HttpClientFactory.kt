package ru.losev.developerslife.data.source.network

import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import ru.losev.developerslife.BuildConfig
import java.util.concurrent.TimeUnit

abstract class HttpClientFactory {
    companion object {
        @JvmStatic
        fun getInstance(): OkHttpClient {
            val userAgent = "Android ver. ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

            val client = OkHttpClient.Builder()
                .protocols(listOf(Protocol.HTTP_2, Protocol.QUIC, Protocol.HTTP_1_1))
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor(HttpUserAgentInterceptor(userAgent))

            return client.build()
        }
    }
}