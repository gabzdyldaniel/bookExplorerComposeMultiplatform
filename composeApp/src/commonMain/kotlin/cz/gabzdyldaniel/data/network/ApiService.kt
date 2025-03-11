package cz.gabzdyldaniel.data.network

import cz.gabzdyldaniel.data.models.VolumesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiService {

    private val baseUrl = "www.googleapis.com/books/v1/volumes?q="

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
                isLenient = true
                coerceInputValues = true
            })
        }
    }

    suspend fun getBooks(author: String): VolumesResponse {
        return httpClient.get("$baseUrl&inauthor:\"${author.replace(" ", "+")}\"langRestrict=cs").body()
    }
}