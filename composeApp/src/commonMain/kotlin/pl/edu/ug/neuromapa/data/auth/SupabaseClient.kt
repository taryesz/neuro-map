package pl.edu.ug.neuromapa.data.auth

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.get
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import pl.edu.ug.neuromapa.BuildConfig

private const val SUPABASE_URL = "https://mvcxlvcjcvcrjftbcvxp.supabase.co"
@Serializable
data class AuthRequest(
    val email: String,
    val password: String
)

@Serializable
data class AuthUser(
    val id: String,
    val email: String? = null,
    @SerialName("user_metadata") val userMetadata: JsonObject? = null
)

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

object SupabaseAuth {

    private val SUPABASE_ANON_KEY = BuildConfig.SUPABASE_API_KEY

    val supabaseHttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        expectSuccess = false
    }

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

    suspend fun getUser(accessToken: String): AuthUser? {
        return supabaseHttpClient.get("$SUPABASE_URL/auth/v1/user") {
                header("apikey", SUPABASE_ANON_KEY)
                header(HttpHeaders.Authorization, "Bearer $accessToken")
            }.body()
    }

}
