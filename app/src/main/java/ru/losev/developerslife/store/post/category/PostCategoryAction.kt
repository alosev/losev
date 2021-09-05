package ru.losev.developerslife.store.post.category

import ru.losev.developerslife.store.core.Action

sealed class PostCategoryAction : Action {
    data class Init(val category: String) : PostCategoryAction()
    data class Empty(val message: String) : PostCategoryAction()
    data class Error(val message: String) : PostCategoryAction()
    object Refresh : PostCategoryAction()
    object Data : PostCategoryAction()
    object NextPost : PostCategoryAction()
    object PrevPost : PostCategoryAction()
}