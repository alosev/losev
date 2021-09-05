package ru.losev.developerslife.data.source.network

import okhttp3.Interceptor
import okhttp3.Response

class HttpUserAgentInterceptor(
    private val userAgent: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val requestWithUserAgent = originRequest.newBuilder()
            .header("User-Agent", userAgent)
            .build()

        return chain.proceed(requestWithUserAgent)
    }
}