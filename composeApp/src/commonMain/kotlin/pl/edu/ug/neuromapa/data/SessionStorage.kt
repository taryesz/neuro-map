package pl.edu.ug.neuromapa.data

/**
 * Platform-agnostic session storage. Lambdas are wired from MainActivity (Android)
 * before App() is rendered, so AuthViewModel can persist/restore sessions.
 */
object SessionStorage {
    var save: ((token: String, email: String, userId: String, displayName: String) -> Unit)? = null
    var load: (() -> StoredSession?)? = null
    var clear: (() -> Unit)? = null
}

data class StoredSession(
    val token: String,
    val email: String,
    val userId: String,
    val displayName: String
)
