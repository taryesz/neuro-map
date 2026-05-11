package pl.edu.ug.neuromapa.data.auth

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import pl.edu.ug.neuromapa.BuildConfig

private const val SUPABASE_URL = "https://mvcxlvcjcvcrjftbcvxp.supabase.co"
private const val PROFILE_PHOTO_BUCKET = "photo"
@Serializable
data class AuthRequest(
    val email: String,
    val password: String
)

@Serializable
data class RefreshTokenRequest(
    @SerialName("refresh_token") val refreshToken: String
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

@Serializable
data class AuthUserMetadataUpdateRequest(
    @SerialName("data") val data: Map<String, String>
)

@Serializable
data class UserDataResponse(
    @SerialName("id") val id: String,
    @SerialName("year") val year: String? = null,
    @SerialName("photo") val photo: String? = null
)

@Serializable
data class UserDataUpsertRequest(
    @SerialName("id") val id: String,
    @SerialName("year") val year: String? = null,
    @SerialName("photo") val photo: String? = null
)

@Serializable
data class UserDataBirthDatePatchRequest(
    @SerialName("year") val year: String
)

@Serializable
data class UserDataPhotoPatchRequest(
    @SerialName("photo") val photo: String
)

data class UserDataProfile(
    val birthDate: String?,
    val photoUrl: String?
)

@Serializable
data class FavoriteRequestResponse(
    @SerialName("user_id") val userId: String? = null,
    @SerialName("place_id") val placeId: Int
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

    suspend fun refreshSession(refreshToken: String): AuthResponse {
        return supabaseHttpClient.post("$SUPABASE_URL/auth/v1/token?grant_type=refresh_token") {
            contentType(ContentType.Application.Json)
            header("apikey", SUPABASE_ANON_KEY)
            setBody(RefreshTokenRequest(refreshToken = refreshToken))
        }.body()
    }

    suspend fun getUser(accessToken: String): AuthUser? {
        return supabaseHttpClient.get("$SUPABASE_URL/auth/v1/user") {
            header("apikey", SUPABASE_ANON_KEY)
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }
}


object SupabaseDatabase {
    private val SUPABASE_ANON_KEY = BuildConfig.SUPABASE_API_KEY
    private const val USER_SCHEMA = "user_information"

    suspend fun addFavoritePlace(userId: String, placeId: Int, accessToken: String): Boolean {
        val response = SupabaseAuth.supabaseHttpClient.post("$SUPABASE_URL/rest/v1/favorite_places") {
            header("apikey", SUPABASE_ANON_KEY)
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            header("Content-Type", "application/json")

            header("Content-Profile", USER_SCHEMA)
            header("Accept-Profile", USER_SCHEMA)

            setBody(FavoriteRequestResponse(userId, placeId))
        }
        if (!response.status.isSuccess()) {
            println("DB ERROR BODY: ${response.bodyAsText()}")
        }
        return response.status.isSuccess()
    }

    suspend fun getName(accessToken: String, userId: String): String? {
        val user = SupabaseAuth.getUser(accessToken) ?: return null
        val metadata = user.userMetadata ?: return null

        val valueFromMetadata = metadata["display_name"]?.jsonPrimitive?.contentOrNull
            ?: metadata["full_name"]?.jsonPrimitive?.contentOrNull
            ?: metadata["name"]?.jsonPrimitive?.contentOrNull

        return valueFromMetadata?.trim().orEmpty().ifBlank { null }
    }

    suspend fun updateName(accessToken: String, userId: String, name: String) {
        val response = SupabaseAuth.supabaseHttpClient.put("$SUPABASE_URL/auth/v1/user") {
            contentType(ContentType.Application.Json)
            header("apikey", SUPABASE_ANON_KEY)
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            setBody(
                AuthUserMetadataUpdateRequest(
                    data = mapOf(
                        "display_name" to name,
                        "full_name" to name,
                        "name" to name
                    )
                )
            )
        }

        if (!response.status.isSuccess()) {
            throw IllegalStateException("Failed to save name: ${response.status} ${response.bodyAsText()}")
        }
    }

    suspend fun getUserDataProfile(accessToken: String, userId: String): UserDataProfile {
        val response = SupabaseAuth.supabaseHttpClient.get("$SUPABASE_URL/rest/v1/user_data") {
            header("apikey", SUPABASE_ANON_KEY)
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            header("Accept-Profile", "user_information")
            parameter("select", "id,year,photo")
            parameter("id", "eq.$userId")
            header("Accept", "application/json")
        }

        if (!response.status.isSuccess()) {
            return UserDataProfile(birthDate = null, photoUrl = null)
        }

        val rows = response.body<List<UserDataResponse>>()
        val first = rows.firstOrNull()

        return UserDataProfile(
            birthDate = first?.year?.trim().orEmpty().ifBlank { null },
            photoUrl = first?.photo?.trim().orEmpty().ifBlank { null }
        )
    }

    suspend fun updateBirthDate(accessToken: String, userId: String, birthDate: String) {
        val patchResponse = SupabaseAuth.supabaseHttpClient.patch("$SUPABASE_URL/rest/v1/user_data") {
            contentType(ContentType.Application.Json)
            header("apikey", SUPABASE_ANON_KEY)
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            header("Content-Profile", "user_information")
            header("Prefer", "return=representation")
            parameter("id", "eq.$userId")
            setBody(UserDataBirthDatePatchRequest(year = birthDate))
        }

        if (!patchResponse.status.isSuccess()) {
            throw IllegalStateException("Failed to update birth date: ${patchResponse.status} ${patchResponse.bodyAsText()}")
        }

        val patchedRows = patchResponse.body<List<UserDataResponse>>()
        if (patchedRows.isNotEmpty()) {
            return
        }

        val insertResponse = SupabaseAuth.supabaseHttpClient.post("$SUPABASE_URL/rest/v1/user_data") {
            contentType(ContentType.Application.Json)
            header("apikey", SUPABASE_ANON_KEY)
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            header("Content-Profile", "user_information")
            header("Prefer", "return=representation")
            setBody(
                listOf(
                    UserDataUpsertRequest(
                        id = userId,
                        year = birthDate
                    )
                )
            )
        }

        if (!insertResponse.status.isSuccess()) {
            throw IllegalStateException("Failed to insert birth date: ${insertResponse.status} ${insertResponse.bodyAsText()}")
        }
    }

    suspend fun uploadProfilePhoto(accessToken: String, userId: String, imageBytes: ByteArray): String {
        @OptIn(kotlin.time.ExperimentalTime::class)
        val timestamp = kotlin.time.Clock.System.now().toEpochMilliseconds()
        val objectPath = "$userId.jpg"

        val uploadResponse = SupabaseAuth.supabaseHttpClient.post("$SUPABASE_URL/storage/v1/object/$PROFILE_PHOTO_BUCKET/$objectPath") {
            header("apikey", SUPABASE_ANON_KEY)
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            header("x-upsert", "true")
            contentType(ContentType.Image.JPEG)
            setBody(imageBytes)
        }

        if (!uploadResponse.status.isSuccess()) {
            throw IllegalStateException("Failed to upload photo: ${uploadResponse.status} ${uploadResponse.bodyAsText()}")
        }

        val publicUrl = "$SUPABASE_URL/storage/v1/object/public/$PROFILE_PHOTO_BUCKET/$objectPath?t=$timestamp"
        
        val patchResponse = SupabaseAuth.supabaseHttpClient.patch("$SUPABASE_URL/rest/v1/user_data") {
            contentType(ContentType.Application.Json)
            header("apikey", SUPABASE_ANON_KEY)
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            header("Content-Profile", "user_information")
            header("Prefer", "return=representation")
            parameter("id", "eq.$userId")
            setBody(UserDataPhotoPatchRequest(photo = publicUrl))
        }

        if (!patchResponse.status.isSuccess()) {
            throw IllegalStateException("Failed to update photo URL: ${patchResponse.status} ${patchResponse.bodyAsText()}")
        }

        val patchedRows = patchResponse.body<List<UserDataResponse>>()
        if (patchedRows.isEmpty()) {
            val insertResponse = SupabaseAuth.supabaseHttpClient.post("$SUPABASE_URL/rest/v1/user_data") {
                contentType(ContentType.Application.Json)
                header("apikey", SUPABASE_ANON_KEY)
                header(HttpHeaders.Authorization, "Bearer $accessToken")
                header("Content-Profile", "user_information")
                header("Prefer", "return=representation")
                setBody(
                    listOf(
                        UserDataUpsertRequest(
                            id = userId,
                            photo = publicUrl
                        )
                    )
                )
            }

            if (!insertResponse.status.isSuccess()) {
                throw IllegalStateException("Failed to insert photo URL: ${insertResponse.status} ${insertResponse.bodyAsText()}")
            }
        }

        return publicUrl
    }

    suspend fun removeFavoritePlace(userId: String, placeId: Int, accessToken: String): Boolean {
        val response = SupabaseAuth.supabaseHttpClient.delete("$SUPABASE_URL/rest/v1/favorite_places") {
            header("apikey", SUPABASE_ANON_KEY)
            header(HttpHeaders.Authorization, "Bearer $accessToken")

            header("Accept-Profile", USER_SCHEMA)
            header("Content-Profile", USER_SCHEMA)

            parameter("user_id", "eq.$userId")
            parameter("place_id", "eq.$placeId")
        }
        return response.status.isSuccess()
    }

    suspend fun fetchFavoritePlaces(userId: String, accessToken: String): List<Int> {
        val response = SupabaseAuth.supabaseHttpClient.get("$SUPABASE_URL/rest/v1/favorite_places") {
            header("apikey", SUPABASE_ANON_KEY)

            header(HttpHeaders.Authorization, "Bearer $accessToken")
            header("Accept-Profile", "user_information")

            header("Content-Profile", USER_SCHEMA)
            parameter("select", "place_id")
            parameter("user_id", "eq.$userId")
        }
        println("RAW RESPONSE: ${response.bodyAsText()}")

        return if (response.status.isSuccess()) {
            val favorites: List<FavoriteRequestResponse> = response.body()
            favorites.map { it.placeId }
        } else {
            emptyList()
        }
    }
}
