package ru.losev.developerslife.model.service

enum class PostCategoryTab(val id: String, val title: String) {
    LATEST(id = "latest", title = "Последние"),
    HOT(id = "hot", title = "Горячие"),
    TOP(id = "top", title = "Лучшие")
}