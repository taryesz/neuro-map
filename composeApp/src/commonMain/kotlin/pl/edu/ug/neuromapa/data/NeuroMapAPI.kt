package pl.edu.ug.neuromapa.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class NeuroMapApi {

    private val baseUrl = "https://neuromapa.ug.edu.pl/wp-json/wp/v2"

    private val client = HttpClient {

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
                coerceInputValues = true
            })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 60000
            connectTimeoutMillis = 60000
            socketTimeoutMillis = 60000
        }

    }

    suspend fun getPlaces(): List<PlaceResponse> {
        return try {
            val response: List<PlaceResponse> = client.get("$baseUrl/miejsce?per_page=100").body()
            println("API Success: ${response.size}")
            response
        } catch (e: Exception) {
            println("API Error: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

}
