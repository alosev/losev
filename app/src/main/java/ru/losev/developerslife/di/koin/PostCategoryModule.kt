package ru.losev.developerslife.di.koin

import org.koin.dsl.module
import ru.losev.developerslife.data.repository.post.PostRepository
import ru.losev.developerslife.data.repository.post.PostRepositoryImpl
import ru.losev.developerslife.store.post.category.PostCategoryStore

val postCategoryModule = module {
    single<PostRepository> { PostRepositoryImpl(get()) }
    factory { PostCategoryStore(get()) }
}