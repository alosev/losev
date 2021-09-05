package ru.losev.developerslife.store.post.category

import ru.losev.developerslife.store.core.Effect

sealed class PostCategoryEffect : Effect {
    data class Error(val message: String) : PostCategoryEffect()
}