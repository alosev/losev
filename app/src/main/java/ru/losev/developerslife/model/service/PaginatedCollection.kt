package ru.losev.developerslife.model.service

data class PaginatedCollection<T>(val collection: List<T>, val totalCount: Int)