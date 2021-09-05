package ru.losev.developerslife.store.post.category

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.losev.developerslife.data.repository.post.PostRepository
import ru.losev.developerslife.model.entity.Post
import ru.losev.developerslife.store.core.Store

class PostCategoryStore(
    private val postRepository: PostRepository
) : Store<PostCategoryAction, PostCategoryEffect, PostCategoryState>, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow<PostCategoryState>(PostCategoryState.Undefined)
    override fun observeState(): StateFlow<PostCategoryState> = state

    private val sideEffect = MutableSharedFlow<PostCategoryEffect>()
    override fun observeSideEffect(): Flow<PostCategoryEffect> = sideEffect

    private lateinit var category: String
    private val categoryPosts: MutableList<Post> = mutableListOf()
    private var page: Int = 0
    private var position: Int = 0
    private var moreLoading: Boolean = false

    override fun dispatch(action: PostCategoryAction) {
        val oldState = state.value

        val newState = when (action) {
            is PostCategoryAction.Init -> {
                when (oldState) {
                    is PostCategoryState.Undefined -> {
                        category = action.category
                        launch { loadPosts() }
                        PostCategoryState.Loading
                    }
                    else -> oldState
                }
            }
            is PostCategoryAction.Error -> {
                when (oldState) {
                    is PostCategoryState.Loading -> {
                        PostCategoryState.Error(message = action.message)
                    }
                    is PostCategoryState.Data -> {
                        launch { sideEffect.emit(value = PostCategoryEffect.Error(message = action.message)) }
                        PostCategoryState.Data(stateData = oldState.stateData.copy(loading = false))
                    }
                    else -> {
                        launch { sideEffect.emit(value = PostCategoryEffect.Error(message = action.message)) }
                        oldState
                    }
                }
            }
            is PostCategoryAction.Empty -> {
                when (oldState) {
                    is PostCategoryState.Loading -> {
                        PostCategoryState.Empty(message = action.message)
                    }
                    else -> {
                        oldState
                    }
                }
            }
            PostCategoryAction.Refresh -> {
                when (oldState) {
                    is PostCategoryState.Error, is PostCategoryState.Empty -> {
                        launch { loadPosts() }
                        PostCategoryState.Loading
                    }
                    else -> oldState
                }
            }
            PostCategoryAction.Data -> {
                when (oldState) {
                    is PostCategoryState.Loading -> {
                        val postCategoryData = getPostCategoryData()
                        PostCategoryState.Data(stateData = postCategoryData)
                    }
                    is PostCategoryState.Data -> {
                        val postCategoryData = getNextPost()
                        PostCategoryState.Data(stateData = postCategoryData)
                    }
                    else -> oldState
                }
            }
            PostCategoryAction.NextPost -> {
                when {
                    oldState is PostCategoryState.Data && !oldState.stateData.loading && !moreLoading -> {
                        val postCategoryData = getNextPost()
                        PostCategoryState.Data(stateData = postCategoryData)
                    }
                    oldState is PostCategoryState.Data && !oldState.stateData.loading -> {
                        if (checkCategoryPosts()) {
                            val postCategoryData = getNextPost()
                            PostCategoryState.Data(stateData = postCategoryData)
                        } else {
                            launch { loadPosts() }
                            PostCategoryState.Data(stateData = oldState.stateData.copy(loading = true))
                        }
                    }
                    else -> oldState
                }
            }
            PostCategoryAction.PrevPost -> {
                when (oldState) {
                    is PostCategoryState.Data -> {
                        val postCategoryData = getPrevPost()
                        PostCategoryState.Data(stateData = postCategoryData)
                    }
                    else -> oldState
                }
            }
        }

        if (newState != oldState) {
            state.value = newState
        }
    }

    private suspend fun loadPosts() {
        postRepository.getPostsByCategory(category = category, page = page, pageSize = PAGE_SIZE)
            .onFailure { dispatch(PostCategoryAction.Error(message = "Произошла ошибка при загрузке данных")) }
            .onSuccess { paginatedCollection ->
                categoryPosts.addAll(paginatedCollection.collection)
                if (categoryPosts.isEmpty()) {
                    dispatch(PostCategoryAction.Empty(message = "К сожалению, на данный момент в категории нет постов"))
                } else {
                    page++
                    moreLoading = categoryPosts.size < paginatedCollection.totalCount
                    dispatch(PostCategoryAction.Data)
                }
            }
    }

    private fun checkCategoryPosts(): Boolean {
        return categoryPosts.size > position + RESERVE_SIZE
    }

    private fun getPrevPost(): PostCategoryData {
        position--
        return getPostCategoryData()
    }

    private fun getNextPost(): PostCategoryData {
        position++
        return getPostCategoryData()
    }

    private fun getPostCategoryData(): PostCategoryData {
        val post = categoryPosts[position]
        val prevPostExist = position > 0
        val nextPostExist = (categoryPosts.size > position + 1) || moreLoading
        return PostCategoryData(post = post, loading = false, prevPostExist = prevPostExist, nextPostExist = nextPostExist)
    }

    companion object {
        private const val PAGE_SIZE: Int = 5
        private const val RESERVE_SIZE: Int = 1
    }
}