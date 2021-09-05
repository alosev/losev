package ru.losev.developerslife.model.entity

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id")
    val id: Int,

    @SerializedName("description")
    val description: String,

    @SerializedName("previewURL")
    val previewUrl: String,

    @SerializedName("gifURL")
    val gifUrl: String
)