package pl.edu.ug.neuromapa.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
// import io.ktor.client.request.delete
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import io.ktor.client.request.post // DODANE
import io.ktor.client.request.setBody // DODANE
import io.ktor.client.request.header // DODANE
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType // DODANE
import io.ktor.http.contentType // DODANE
import io.ktor.http.isSuccess // DODANE
import kotlinx.serialization.Serializable
import pl.edu.ug.neuromapa.screens.add.data.NominatimResponse
import pl.edu.ug.neuromapa.screens.add.data.WpPlaceRequest

@Serializable
private data class WpStatusUpdateRequest(
    val status: String
)

data class WordPressActionResult(
    val isSuccess: Boolean,
    val errorMessage: String? = null
)

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
    suspend fun getRawPlacesByStatus(authHeader: String, status: String): List<JsonElement> {
        val json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
            coerceInputValues = true
        }

        val allPlaces = mutableListOf<JsonElement>()
        var page = 1

        while (true) {
            val response = client.get("$baseUrl/miejsce") {
                parameter("per_page", 100)
                parameter("page", page)
                parameter("status", status)
                parameter("context", "edit")
                header("Authorization", authHeader)
            }

            if (!response.status.isSuccess()) {
                throw IllegalStateException(
                    "WordPress API error ${response.status}: ${response.bodyAsText()}"
                )
            }

            val responseBody = response.bodyAsText()
            val places = json.parseToJsonElement(responseBody) as? JsonArray
                ?: throw IllegalStateException("WordPress API did not return a JSON array: $responseBody")

            if (places.isEmpty()) {
                break
            }

            allPlaces.addAll(places)

            val totalPages = response.headers["X-WP-TotalPages"]?.toIntOrNull()
            if (totalPages != null && page >= totalPages) {
                break
            }

            if (totalPages == null && places.size < 100) {
                break
            }

            page += 1
        }

        return allPlaces
    }

    suspend fun postPlace(request: WpPlaceRequest, authHeader: String): Boolean {
        return try {
            val response = client.post("$baseUrl/miejsce") {
                contentType(ContentType.Application.Json)
                header("Authorization", authHeader)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                return true
            } else {
                val errorBody = response.bodyAsText()
                println("API POST Error Body: $errorBody")
                return false
            }

        } catch (e: Exception) {
            println("API POST Error: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    suspend fun publishPlaceDraft(placeId: String, authHeader: String): Boolean {
        return try {
            val response = client.post("$baseUrl/miejsce/$placeId") {
                contentType(ContentType.Application.Json)
                header("Authorization", authHeader)
                setBody(WpStatusUpdateRequest(status = "publish"))
            }

            if (response.status.isSuccess()) {
                true
            } else {
                println("API PUBLISH DRAFT Error Body: ${response.bodyAsText()}")
                false
            }
        } catch (e: Exception) {
            println("API PUBLISH DRAFT Error: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    suspend fun getCoordinates(address: String): Pair<Double, Double>? {
        return try {
            val formattedAddress = address.replace(" ", "+")
            val url = "https://nominatim.openstreetmap.org/search?q=$formattedAddress&format=json&limit=1"

            val response: List<NominatimResponse> = client.get(url) {
                header("User-Agent", "NeuroMapa-App")
            }.body()

            if (response.isNotEmpty()) {
                Pair(response[0].lat.toDouble(), response[0].lon.toDouble())
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updatePlace(placeId: String, payload: WpPlaceRequest, authHeader: String): Boolean {
        return try {
            val response = client.post("$baseUrl/miejsce/$placeId") {
                contentType(ContentType.Application.Json)
                header("Authorization", authHeader)
                setBody(payload)
            }

            if (response.status.isSuccess()) {
                true
            } else {
                val errorBody = response.bodyAsText()
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}
