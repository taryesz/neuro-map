package pl.edu.ug.neuromapa.data.auth

object SessionStorage {
    var save: ((
        token: String,
        refreshToken: String?,
        email: String,
        userId: String,
        name: String?,
        birthDate: String?,
        photoUrl: String?
    ) -> Unit)? = null
    var load: (() -> StoredSession?)? = null
    var clear: (() -> Unit)? = null
}

data class StoredSession(
    val token: String,
    val refreshToken: String? = null,
    val email: String,
    val userId: String,
    val name: String? = null,
    val birthDate: String? = null,
    val photoUrl: String? = null,
)
