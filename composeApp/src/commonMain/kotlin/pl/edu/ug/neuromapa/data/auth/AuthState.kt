package pl.edu.ug.neuromapa.data.auth

// This class defines all possible states of authentication
sealed class AuthState {

    object Checking : AuthState()   // doesn't carry any additional information, so using "object"

    object SignedOut : AuthState()

    data class SignedIn(            // carries additional information, so using "data class"
        val email: String,
        val userId: String,
        val accessToken: String,
        val refreshToken: String? = null,
        val name: String? = null,
        val birthDate: String? = null,
        val photoUrl: String? = null,
        val theme: String = "light",
    ) : AuthState()

    data class Error(val message: String) : AuthState()

}
