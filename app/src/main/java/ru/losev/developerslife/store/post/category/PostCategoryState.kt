package ru.losev.developerslife.store.post.category

import ru.losev.developerslife.model.entity.Post
import ru.losev.developerslife.store.core.State

sealed class PostCategoryState : State {
    object Undefined : PostCategoryState()
    object Loading : PostCategoryState()
    data class Error(val message: String) : PostCategoryState()
    data class Empty(val message: String) : PostCategoryState()
    data class Data(val stateData: PostCategoryData) : PostCategoryState()
}

data class PostCategoryData(
    val post: Post?,
    val loading: Boolean,
    val prevPostExist: Boolean,
    val nextPostExist: Boolean
)