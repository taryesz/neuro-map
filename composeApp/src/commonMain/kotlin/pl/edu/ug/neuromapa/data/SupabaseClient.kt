package pl.edu.ug.neuromapa.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val SUPABASE_URL = "https://mvcxlvcjcvcrjftbcvxp.supabase.co"
private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im12Y3hsdmNqY3ZjcmpmdGJjdnhwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzU3NDg4MTIsImV4cCI6MjA5MTMyNDgxMn0.HE-4oiBGnQR_kymusg9mhDWSzMWSm_lMd3JT0eL0Wc8"

@Serializable
data class AuthRequest(val email: String, val password: String)

@Serializable
data class AuthUser(
    val id: String,
    val email: String? = null
)

@Serializable
data class AuthResponse(
    @SerialName("access_token") val accessToken: String? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    val user: AuthUser? = null,
    // top-level user fields (email confirmation required case)
    val id: String? = null,
    val email: String? = null,
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
    expectSuccess = false  // don't throw on 4xx — we read the error body ourselves
}

object SupabaseAuth {

    suspend fun signIn(email: String, password: String): AuthResponse {
        return supabaseHttpClient.post("$SUPABASE_URL/auth/v1/token?grant_type=password") {
            contentType(ContentType.Application.Json)
            header("apikey", SUPABASE_ANON_KEY)
            setBody(AuthRequest(email, password))
        }.body()
    }

    suspend fun signUp(email: String, password: String): AuthResponse {
        return supabaseHttpClient.post("$SUPABASE_URL/auth/v1/signup") {
            contentType(ContentType.Application.Json)
            header("apikey", SUPABASE_ANON_KEY)
            setBody(AuthRequest(email, password))
        }.body()
    }

    suspend fun signOut(accessToken: String) {
        supabaseHttpClient.post("$SUPABASE_URL/auth/v1/logout") {
            header("apikey", SUPABASE_ANON_KEY)
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }
    }
}
