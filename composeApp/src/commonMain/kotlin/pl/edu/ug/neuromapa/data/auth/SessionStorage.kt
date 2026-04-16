package pl.edu.ug.neuromapa.data.auth

object SessionStorage {
    var save: ((token: String, email: String, userId: String) -> Unit)? = null
    var load: (() -> StoredSession?)? = null
    var clear: (() -> Unit)? = null
}

data class StoredSession(
    val token: String,
    val email: String,
    val userId: String,
)
