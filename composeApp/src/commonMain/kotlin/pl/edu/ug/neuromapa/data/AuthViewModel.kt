package pl.edu.ug.neuromapa.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Checking : AuthState()
    object SignedOut : AuthState()
    data class SignedIn(
        val email: String,
        val userId: String,
        val accessToken: String,
        val displayName: String = ""
    ) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Checking)
    val authState: StateFlow<AuthState> = _authState

    init {
        restoreSession()
    }

    private fun restoreSession() {
        val stored = SessionStorage.load?.invoke()
        _authState.value = if (stored != null) {
            AuthState.SignedIn(
                email = stored.email,
                userId = stored.userId,
                accessToken = stored.token,
                displayName = stored.displayName
            )
        } else {
            AuthState.SignedOut
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Checking
            try {
                val response = SupabaseAuth.signIn(email, password)
                val token = response.accessToken
                val user = response.user
                if (token != null && user != null) {
                    val displayName = user.displayName
                    SessionStorage.save?.invoke(token, user.email ?: email, user.id, displayName)
                    _authState.value = AuthState.SignedIn(
                        email = user.email ?: email,
                        userId = user.id,
                        accessToken = token,
                        displayName = displayName
                    )
                } else {
                    _authState.value = AuthState.Error(translateSignInError(response))
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Błąd logowania. Sprawdź połączenie z internetem.")
            }
        }
    }

    fun signUp(email: String, password: String, firstName: String = "", lastName: String = "") {
        viewModelScope.launch {
            _authState.value = AuthState.Checking
            try {
                val response = SupabaseAuth.signUp(email, password, firstName, lastName)
                val token = response.accessToken
                val user = response.user ?: if (response.id != null) {
                    AuthUser(
                        id = response.id,
                        email = response.email,
                        userMetadata = response.userMetadata
                    )
                } else null

                if (user != null && token != null) {
                    val displayName = "$firstName $lastName".trim()
                    SessionStorage.save?.invoke(token, user.email ?: email, user.id, displayName)
                    _authState.value = AuthState.SignedIn(
                        email = user.email ?: email,
                        userId = user.id,
                        accessToken = token,
                        displayName = displayName
                    )
                } else if (user != null) {
                    _authState.value = AuthState.Error("Sprawdź skrzynkę email i potwierdź rejestrację")
                } else {
                    _authState.value = AuthState.Error(translateSignUpError(response))
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Błąd rejestracji. Sprawdź połączenie z internetem.")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            val current = _authState.value
            if (current is AuthState.SignedIn) {
                try { SupabaseAuth.signOut(current.accessToken) } catch (_: Exception) {}
            }
            SessionStorage.clear?.invoke()
            _authState.value = AuthState.SignedOut
        }
    }

    fun clearError() {
        _authState.value = AuthState.SignedOut
    }

    private fun translateSignInError(response: AuthResponse): String {
        val code = response.errorCode ?: response.error ?: ""
        val msg = (response.errorDescription ?: response.message ?: "").lowercase()
        return when {
            "invalid" in msg && ("credential" in msg || "password" in msg) -> "Nieprawidłowy email lub hasło"
            "rate" in msg || "limit" in msg || response.code == 429 -> "Zbyt wiele prób. Spróbuj za chwilę."
            "not found" in msg -> "Nie znaleziono użytkownika. Zarejestruj się najpierw."
            code.isNotEmpty() -> code
            else -> "Nieprawidłowy email lub hasło"
        }
    }

    private fun translateSignUpError(response: AuthResponse): String {
        val code = response.errorCode ?: response.error ?: ""
        val msg = (response.errorDescription ?: response.message ?: "").lowercase()
        return when {
            "already" in msg || "registered" in msg || code == "user_already_exists" ->
                "Ten email jest już zarejestrowany. Zaloguj się."
            "rate" in msg || "limit" in msg || response.code == 429 ->
                "Zbyt wiele prób. Spróbuj za chwilę."
            "password" in msg && ("short" in msg || "weak" in msg || "length" in msg) ->
                "Hasło musi mieć co najmniej 6 znaków"
            "invalid" in msg && "email" in msg -> "Nieprawidłowy format email"
            else -> "Błąd rejestracji. Spróbuj ponownie."
        }
    }
}
