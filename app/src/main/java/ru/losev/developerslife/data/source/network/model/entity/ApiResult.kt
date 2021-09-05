package ru.losev.developerslife.data.source.network.model.entity

import com.google.gson.annotations.SerializedName

data class ApiResult<T>(
    @SerializedName("result")
    val result: T,

    @SerializedName("totalCount")
    val totalCount: Int
)