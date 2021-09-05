package ru.losev.developerslife.data.source.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.losev.developerslife.data.source.network.model.entity.ApiResult
import ru.losev.developerslife.model.entity.Post
import ru.losev.developerslife.model.setting.api.ApiSetting

interface ApiService {
    // json=true захаркодил, т.к. полный формат нам не подойдет для обработки
    @GET("/{category}/{page}?json=true")
    suspend fun getPostsByCategory(
        @Path("category") category: String,
        @Path("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): ApiResult<List<Post>>

    companion object {
        @JvmStatic
        fun getInstance(apiSetting: ApiSetting, client: OkHttpClient): ApiService {
            val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(apiSetting.basePath)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}