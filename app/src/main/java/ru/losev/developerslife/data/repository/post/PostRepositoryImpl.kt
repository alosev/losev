package ru.losev.developerslife.data.repository.post

import retrofit2.HttpException
import ru.losev.developerslife.data.source.network.ApiService
import ru.losev.developerslife.model.entity.Post
import ru.losev.developerslife.model.service.PaginatedCollection

class PostRepositoryImpl(
    private val apiService: ApiService
) : PostRepository {
    override suspend fun getPostsByCategory(category: String, page: Int, pageSize: Int): Result<PaginatedCollection<Post>> {
        return try {
            val result = apiService.getPostsByCategory(category = category, page = page, pageSize = pageSize)
            Result.success(value = PaginatedCollection(collection = result.result, totalCount = result.totalCount))
        } catch (ex: HttpException) {
            Result.failure(exception = ex)
        } catch (ex: Exception) {
            Result.failure(exception = ex)
        }
    }
}