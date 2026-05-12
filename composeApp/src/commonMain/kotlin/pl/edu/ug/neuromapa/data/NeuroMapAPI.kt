package pl.edu.ug.neuromapa.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.request.post // DODANE
import io.ktor.client.request.setBody // DODANE
import io.ktor.client.request.header // DODANE
import io.ktor.http.ContentType // DODANE
import io.ktor.http.contentType // DODANE
import io.ktor.http.isSuccess // DODANE
import pl.edu.ug.neuromapa.screens.add.data.NominatimResponse
import pl.edu.ug.neuromapa.screens.add.data.WpPlaceRequest

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

    suspend fun postPlace(request: WpPlaceRequest, authHeader: String): Boolean {
        return try {
            val response = client.post("$baseUrl/miejsce") {
                contentType(ContentType.Application.Json)
                header("Authorization", authHeader)     // TODO: Auth data (contact UG IT department)
                setBody(request)                        // JSON (body)
            }

            println("API POST Status: ${response.status}")
            response.status.isSuccess()
        } catch (e: Exception) {
            println("API POST Error: ${e.message}")
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

}
