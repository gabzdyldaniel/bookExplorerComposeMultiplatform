package cz.gabzdyldaniel.data.repository

import cz.gabzdyldaniel.data.network.ApiService

class BookRepository {

    private val apiService = ApiService()

    suspend fun fetchBooks(author: String) = apiService.getBooks(author)
}