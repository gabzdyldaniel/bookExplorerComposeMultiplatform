package cz.gabzdyldaniel.data.network

import cz.gabzdyldaniel.data.models.VolumesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.*

class ApiService {

    private val baseUrl = "www.googleapis.com/books/v1/volumes"

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
                isLenient = true
                coerceInputValues = true
            })
        }

        install(Logging) {
            level = LogLevel.ALL
        }
    }

    suspend fun getBooks(author: String): VolumesResponse {
        try {
            val response = httpClient.get("https://$baseUrl") {
                parameter("q", "inauthor:$author")
                parameter("langRestrict", "cs")
            }

            println("HTTP status: ${response.status}")

            return response.body<VolumesResponse>()
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            throw e
        }
    }
}