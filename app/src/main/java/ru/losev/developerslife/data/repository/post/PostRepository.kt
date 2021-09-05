package ru.losev.developerslife.data.repository.post

import ru.losev.developerslife.model.entity.Post
import ru.losev.developerslife.model.service.PaginatedCollection

interface PostRepository {
    suspend fun getPostsByCategory(category: String, page: Int, pageSize: Int): Result<PaginatedCollection<Post>>
}