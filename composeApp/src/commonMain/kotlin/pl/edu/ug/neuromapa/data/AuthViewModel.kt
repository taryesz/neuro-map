package pl.edu.ug.neuromapa.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Checking : AuthState()
    object SignedOut : AuthState()
    data class SignedIn(val email: String, val userId: String, val accessToken: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.SignedOut)
    val authState: StateFlow<AuthState> = _authState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Checking
            try {
                val response = SupabaseAuth.signIn(email, password)
                val token = response.accessToken
                val user = response.user
                if (token != null && user != null) {
                    _authState.value = AuthState.SignedIn(
                        email = user.email ?: email,
                        userId = user.id,
                        accessToken = token
                    )
                } else {
                    _authState.value = AuthState.Error(translateSignInError(response))
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Błąd logowania. Sprawdź połączenie z internetem.")
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Checking
            try {
                val response = SupabaseAuth.signUp(email, password)
                // Case 1: logged in immediately (email confirmation disabled)
                val token = response.accessToken
                val user = response.user ?: if (response.id != null) AuthUser(response.id, response.email) else null
                if (user != null && token != null) {
                    _authState.value = AuthState.SignedIn(
                        email = user.email ?: email,
                        userId = user.id,
                        accessToken = token
                    )
                } else if (user != null) {
                    // Case 2: registered but needs email confirmation
                    _authState.value = AuthState.Error("Sprawdź skrzynkę email i potwierdź rejestrację")
                } else {
                    _authState.value = AuthState.Error(translateSignUpError(response))
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Błąd rejestracji. Sprawdź połączenie z internetem.")
            }
        }
    }

    private fun translateSignInError(response: AuthResponse): String {
        val code = response.errorCode ?: response.error ?: ""
        val msg = (response.errorDescription ?: response.message ?: "").lowercase()
        return when {
            "invalid" in msg && ("credential" in msg || "password" in msg || "login" in msg) ->
                "Nieprawidłowy email lub hasło"
            "rate" in msg || "limit" in msg || response.code == 429 ->
                "Zbyt wiele prób. Spróbuj za chwilę."
            "not found" in msg || "user" in msg && "exist" in msg ->
                "Nie znaleziono użytkownika. Zarejestruj się najpierw."
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
            "invalid" in msg && "email" in msg ->
                "Nieprawidłowy format email"
            else -> "Błąd rejestracji. Spróbuj ponownie."
        }
    }

    fun signOut() {
        viewModelScope.launch {
            val current = _authState.value
            if (current is AuthState.SignedIn) {
                try { SupabaseAuth.signOut(current.accessToken) } catch (_: Exception) {}
            }
            _authState.value = AuthState.SignedOut
        }
    }

    fun clearError() {
        _authState.value = AuthState.SignedOut
    }
}
