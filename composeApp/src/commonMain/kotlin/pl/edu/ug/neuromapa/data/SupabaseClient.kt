package pl.edu.ug.neuromapa.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

private const val SUPABASE_URL = "https://mvcxlvcjcvcrjftbcvxp.supabase.co"
private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im12Y3hsdmNqY3ZjcmpmdGJjdnhwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzU3NDg4MTIsImV4cCI6MjA5MTMyNDgxMn0.HE-4oiBGnQR_kymusg9mhDWSzMWSm_lMd3JT0eL0Wc8"

@Serializable
data class AuthRequest(
    val email: String,
    val password: String,
    val data: Map<String, String>? = null
)

@Serializable
data class AuthUser(
    val id: String,
    val email: String? = null,
    @SerialName("user_metadata") val userMetadata: JsonObject? = null
) {
    val firstName: String get() = userMetadata?.get("first_name")?.jsonPrimitive?.content ?: ""
    val lastName: String get() = userMetadata?.get("last_name")?.jsonPrimitive?.content ?: ""
    val displayName: String get() = "$firstName $lastName".trim()
}

@Serializable
data class AuthResponse(
    @SerialName("access_token") val accessToken: String? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    val user: AuthUser? = null,
    // top-level user fields when email confirmation is required
    val id: String? = null,
    val email: String? = null,
    @SerialName("user_metadata") val userMetadata: JsonObject? = null,
    // error fields
    val error: String? = null,
    @SerialName("error_code") val errorCode: String? = null,
    @SerialName("error_description") val errorDescription: String? = null,
    @SerialName("msg") val message: String? = null,
    val code: Int? = null
)

val supabaseHttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    expectSuccess = false
}

object SupabaseAuth {

    suspend fun signIn(email: String, password: String): AuthResponse {
        return supabaseHttpClient.post("$SUPABASE_URL/auth/v1/token?grant_type=password") {
            contentType(ContentType.Application.Json)
            header("apikey", SUPABASE_ANON_KEY)
            setBody(AuthRequest(email, password))
        }.body()
    }

    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): AuthResponse {
        val data = buildMap<String, String> {
            if (firstName.isNotBlank()) put("first_name", firstName)
            if (lastName.isNotBlank()) put("last_name", lastName)
        }.ifEmpty { null }

        return supabaseHttpClient.post("$SUPABASE_URL/auth/v1/signup") {
            contentType(ContentType.Application.Json)
            header("apikey", SUPABASE_ANON_KEY)
            setBody(AuthRequest(email, password, data))
        }.body()
    }

    suspend fun signOut(accessToken: String) {
        supabaseHttpClient.post("$SUPABASE_URL/auth/v1/logout") {
            header("apikey", SUPABASE_ANON_KEY)
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }
    }
}
