package pl.edu.ug.neuromapa.data.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.auth_system_alert_dialog_no_internet
import org.jetbrains.compose.resources.getString

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Checking)
    val authState: StateFlow<AuthState> = _authState

    private var lastSelectedProvider: String? = null

    init {
        OAuthResultHandler.handle = { accessToken, error ->
            handleOAuthCallback(accessToken, error, lastSelectedProvider ?: "unknown")
        }
    }

    fun restoreSession() {

        val stored = SessionStorage.load?.invoke()

        _authState.value = if (stored != null) {
            AuthState.SignedIn(
                email = stored.email,
                userId = stored.userId,
                accessToken = stored.token,
            )
        }
        else {
            AuthState.SignedOut
        }

    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {

            _authState.value = AuthState.Checking

            // Try to sign in an existing user
            try {

                val response = SupabaseAuth.signIn(email, password)
                val token = response.accessToken
                val user = response.user

                // If the signing in was successful, change the AuthState to SignedIn
                if (token != null && user != null) {

                    SessionStorage.save?.invoke(token, user.email ?: email, user.id)
                    _authState.value = AuthState.SignedIn(
                        email = user.email ?: email,
                        userId = user.id,
                        accessToken = token,
                    )

                }

                // In case some unexpected error took place, show the error (translating "raw" error into more "human" way)
                else {
                    _authState.value = AuthState.Error(translateAuthError(response))
                }

            }

            // Show the error regarding the absence of Internet
            catch (_: Exception) {
                _authState.value = AuthState.Error(getString(Res.string.auth_system_alert_dialog_no_internet))
            }

        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {

            _authState.value = AuthState.Checking

            // Try to sign up a new user
            try {

                val response = SupabaseAuth.signUp(email, password)
                val token = response.accessToken

                val user = response.user ?: if (response.id != null) {
                    AuthUser(
                        id = response.id,
                        email = response.email,
                        userMetadata = response.userMetadata
                    )
                }
                else null

                // If the signing up was successful, change the AuthState to SignedIn
                if (user != null && token != null) {

                    SessionStorage.save?.invoke(token, user.email ?: email, user.id)
                    _authState.value = AuthState.SignedIn(
                        email = user.email ?: email,
                        userId = user.id,
                        accessToken = token,
                    )

                }

                // In case some unexpected error took place, show the error (translating "raw" error into more "human" way)
                else {
                    _authState.value = AuthState.Error(translateAuthError(response))
                }

            }

            // Show the error regarding the absence of Internet
            catch (_: Exception) {
                _authState.value = AuthState.Error(getString(Res.string.auth_system_alert_dialog_no_internet))
            }

        }
    }

    fun signOut() {
        viewModelScope.launch {

            val current = _authState.value

            if (current is AuthState.SignedIn) {
                try {
                    SupabaseAuth.signOut(current.accessToken)   // trying to inform Supabase of signing out
                }
                catch (e: Exception) {
                    println(e.message)  // information for us programmers :(
                }
            }

            // still sign the user out even if Supabase returned an error
            SessionStorage.clear?.invoke()

            // change the state to SignedOut
            _authState.value = AuthState.SignedOut

        }
    }

    // Auth using Google / Apple
    fun signInWithOAuth(provider: String) {
        lastSelectedProvider = provider // lastly used provider for auth
        OAuthLauncher.launch?.invoke(provider)
    }

    fun handleOAuthCallback(accessToken: String?, externalError: String?, provider: String) {
        viewModelScope.launch {

            _authState.value = AuthState.Checking

            if (accessToken == null) {
                val errorMsg = translateOAuthError(externalError ?: "unknown", provider)
                _authState.value = AuthState.Error(errorMsg)
                return@launch
            }

            try {

                val user = SupabaseAuth.getUser(accessToken)

                if (user != null) {
                    SessionStorage.save?.invoke(accessToken, user.email ?: "", user.id)
                    _authState.value = AuthState.SignedIn(
                        email = user.email ?: "",
                        userId = user.id,
                        accessToken = accessToken
                    )
                }
                else {
                    _authState.value = AuthState.Error(translateOAuthError("invalid_session", provider))
                }

            }
            catch (_: Exception) {
                _authState.value = AuthState.Error(getString(Res.string.auth_system_alert_dialog_no_internet))
            }

        }
    }

    fun clearError() {
        // If the popup was dismissed, return to the SignedOut state
        _authState.value = AuthState.SignedOut
    }

    private fun translateAuthError(response: AuthResponse): String {

        val code = response.errorCode ?: response.error ?: ""
        val msg = (response.errorDescription ?: response.message ?: "").lowercase()

        return when {


            // COMMON ERRORS
            "validation_failed" in code || "missing email" in msg || response.code == 422 ->
                "Proszę wprowadzić adres email oraz hasło."

            "rate" in msg || "limit" in msg || response.code == 429 ->
                "Zbyt wiele prób. Spróbuj za chwilę."


            // SIGNING IN ERRORS
            "invalid" in msg && ("credential" in msg || "password" in msg) ->
                "Nieprawidłowy email lub hasło."

            "not found" in msg ->
                "Nie znaleziono użytkownika z takim emailem."


            // SIGNING UP ERRORS
            "already" in msg || "registered" in msg || code == "user_already_exists" ->
                "Ten email jest już zarejestrowany."

            "password" in msg && ("short" in msg || "weak" in msg || "length" in msg) ->
                "Hasło musi mieć co najmniej 6 znaków."

            "invalid" in msg && "email" in msg ->
                "Nieprawidłowy format adresu email."


            // UNSERVED ERRORS (CUSTOM BUILD)
            else -> buildString {
                append("Wystąpił błąd: ")
                if (response.errorCode != null) append("[${response.errorCode}] ")
                if (response.message != null) append("${response.message} ")
                if (response.errorDescription != null) append("/ ${response.errorDescription}")
                if (response.code != null) append(" (${response.code})")
                if (length <= "Wystąpił błąd: ".length) append("Spróbuj ponownie później.")
            }.trim()

        }
    }

    private fun translateOAuthError(error: String, provider: String? = null): String {

        val msg = error.lowercase()

        val providerName = when(provider) {
            "google" -> "Google"
            "apple" -> "Apple"
            else -> "zewnętrznego dostawcy"
        }

        return when {
            "access_denied" in msg || "cancelled" in msg || "canceled" in msg ->
                "Logowanie przez $providerName zostało anulowane."

            "identity_provider_error" in msg ->
                "Wystąpił problem z konfiguracją konta $providerName."

            "invalid_session" in msg ->
                "Sesja wygasła lub jest nieprawidłowa. Spróbuj ponownie."

            else -> "Nie udało się zalogować przez $providerName. ($error)"
        }
    }

}
