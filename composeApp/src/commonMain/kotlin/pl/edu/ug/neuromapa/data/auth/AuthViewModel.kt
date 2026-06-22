package pl.edu.ug.neuromapa.data.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.auth_system_alert_dialog_no_internet
import org.jetbrains.compose.resources.getString
import kotlin.time.Clock

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Checking)
    val authState: StateFlow<AuthState> = _authState

    private var lastSelectedProvider: String? = null
//    private var hasLoggedUserDatabaseDebug = false

    init {
        OAuthResultHandler.handle = { accessToken, refreshToken, error ->
            handleOAuthCallback(accessToken, refreshToken, error, lastSelectedProvider ?: "unknown")
        }
    }

    fun restoreSession() {

        val stored = SessionStorage.load?.invoke()

        _authState.value = if (stored != null) {
            AuthState.SignedIn(
                email = stored.email,
                userId = stored.userId,
                accessToken = stored.token,
                refreshToken = stored.refreshToken,
                name = stored.name,
            )
        }
        else {
            AuthState.SignedOut
        }

        loadProfileForCurrentUser()
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {

            _authState.value = AuthState.Checking

            // Try to sign in an existing user
            try {

                val response = SupabaseAuth.signIn(email, password)
                val token = response.accessToken
                val refreshToken = response.refreshToken
                val user = response.user

                // If the signing in was successful, change the AuthState to SignedIn
                if (token != null && user != null) {

                    SessionStorage.save?.invoke(token, refreshToken, user.email ?: email, user.id, null, null, null)
                    _authState.value = AuthState.SignedIn(
                        email = user.email ?: email,
                        userId = user.id,
                        accessToken = token,
                        refreshToken = refreshToken,
                    )
                    loadProfileForCurrentUser()

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
                val refreshToken = response.refreshToken

                val user = response.user ?: if (response.id != null) {
                    AuthUser(
                        id = response.id,
                        email = response.email,
                        appMetadata = response.appMetadata,
                        userMetadata = response.userMetadata
                    )
                }
                else null

                // If the signing up was successful, change the AuthState to SignedIn
                if (user != null && token != null) {

                    SessionStorage.save?.invoke(token, refreshToken, user.email ?: email, user.id, null, null, null)
                    _authState.value = AuthState.SignedIn(
                        email = user.email ?: email,
                        userId = user.id,
                        accessToken = token,
                        refreshToken = refreshToken,
                    )
                    loadProfileForCurrentUser()

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
//            hasLoggedUserDatabaseDebug = false

            // change the state to SignedOut
            _authState.value = AuthState.SignedOut

        }
    }

    // Auth using Google / Apple
    fun signInWithOAuth(provider: String) {
        lastSelectedProvider = provider // lastly used provider for auth
        OAuthLauncher.launch?.invoke(provider)
    }

    fun handleOAuthCallback(accessToken: String?, refreshToken: String?, externalError: String?, provider: String) {
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
                    SessionStorage.save?.invoke(accessToken, refreshToken, user.email ?: "", user.id, null, null, null)
                    _authState.value = AuthState.SignedIn(
                        email = user.email ?: "",
                        userId = user.id,
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                    )
                    loadProfileForCurrentUser()
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

    fun updateTheme(theme: String) {
        val current = _authState.value as? AuthState.SignedIn ?: return
        viewModelScope.launch {
            try {
                val validSession = getValidSession(current) ?: return@launch
                SupabaseDatabase.updateTheme(
                    accessToken = validSession.accessToken,
                    userId = validSession.userId,
                    theme = theme
                )
                _authState.value = validSession.copy(theme = theme)
            } catch (_: Exception) { }
        }
    }

    fun clearError() {
        // If the popup was dismissed, return to the SignedOut state
        _authState.value = AuthState.SignedOut
    }

    fun deleteAccount(
        onError: ((String) -> Unit)? = null,
        onSuccess: (() -> Unit)? = null
    ) {
        val current = _authState.value as? AuthState.SignedIn ?: return

        viewModelScope.launch {
            try {
                val validSession = getValidSession(current) ?: throw IllegalStateException("Sesja wygasła. Zaloguj się ponownie.")
                SupabaseDatabase.deleteUserAccount(
                    accessToken = validSession.accessToken
                )
                signOut()
                onSuccess?.invoke()
            } catch (e: Exception) {
                val details = e.message?.take(220).orEmpty()
                onError?.invoke(
                    if (details.isNotBlank()) "Nie udało się usunąć konta: $details"
                    else "Wystąpił błąd podczas usuwania konta. Spróbuj ponownie później."
                )
            }
        }
    }

    fun updateProfileData(
        newName: String,
        newBirthDate: String,
        onError: ((String) -> Unit)? = null,
        onSuccess: (() -> Unit)? = null
    ) {
        val normalizedName = newName.trim()
        val normalizedBirthDate = newBirthDate.trim()

        if (normalizedName.isEmpty()) {
            onError?.invoke("Imię nie może być puste.")
            return
        }

        if (normalizedBirthDate.isNotEmpty() && !isValidIsoDate(normalizedBirthDate)) {
            onError?.invoke("Data urodzenia musi mieć format RRRR-MM-DD.")
            return
        }

        val current = _authState.value as? AuthState.SignedIn ?: return

        viewModelScope.launch {
            try {

                val validSession = getValidSession(current) ?: throw IllegalStateException("Sesja wygasła. Zaloguj się ponownie.")

                val updatedState = validSession.copy(
                    name = normalizedName,
                    birthDate = normalizedBirthDate
                )

                _authState.value = updatedState

                SupabaseDatabase.updateName(
                    accessToken = validSession.accessToken,
                    userId = validSession.userId,
                    name = normalizedName
                )

                if (normalizedBirthDate.isNotEmpty()) {
                    SupabaseDatabase.updateBirthDate(
                        accessToken = validSession.accessToken,
                        userId = validSession.userId,
                        birthDate = normalizedBirthDate
                    )
                }

                SessionStorage.save?.invoke(
                    updatedState.accessToken,
                    updatedState.refreshToken,
                    updatedState.email,
                    updatedState.userId,
                    updatedState.name,
                    updatedState.birthDate,
                    updatedState.photoUrl
                )

                onSuccess?.invoke()

            } catch (e: Exception) {
                val details = e.message?.take(220).orEmpty()
                onError?.invoke(
                    if (details.isNotBlank()) "Nie udało się zapisać zmian: $details"
                    else "Nie udało się zapisać zmian. Spróbuj ponownie."
                )
            }
        }
    }

    private fun isValidIsoDate(date: String): Boolean {
        val parts = date.split("-")
        if (parts.size != 3) return false

        val year = parts[0].toIntOrNull() ?: return false
        val month = parts[1].toIntOrNull() ?: return false
        val day = parts[2].toIntOrNull() ?: return false

        if (year < 1900 || year > 2100) return false
        if (month !in 1..12) return false
        if (day !in 1..31) return false

        val maxDay = when (month) {
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> 31
        }
        return day <= maxDay
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    fun uploadProfilePhoto(
        imageBytes: ByteArray,
        onError: ((String) -> Unit)? = null,
        onSuccess: (() -> Unit)? = null
    ) {
        val current = _authState.value as? AuthState.SignedIn ?: return

        viewModelScope.launch {
            try {
                val validSession = getValidSession(current) ?: throw IllegalStateException("Sesja wygasła. Zaloguj się ponownie.")
                val photoUrl = SupabaseDatabase.uploadProfilePhoto(
                    accessToken = validSession.accessToken,
                    userId = validSession.userId,
                    imageBytes = imageBytes
                )
                _authState.value = validSession.copy(photoUrl = photoUrl)
                onSuccess?.invoke()
            } catch (e: Exception) {
                println("BŁĄD UPLOADU ZDJĘCIA: ${e.message}")
                onError?.invoke("Nie udało się zapisać zdjęcia profilowego.")
            }
        }
    }

    private fun loadProfileForCurrentUser() {
        val current = _authState.value as? AuthState.SignedIn ?: return

        viewModelScope.launch {
            try {
                val validSession = getValidSession(current) ?: throw IllegalStateException("Session is invalid")
                val name = SupabaseDatabase.getName(
                    accessToken = validSession.accessToken,
                    userId = validSession.userId
                )
                val profile = SupabaseDatabase.getUserDataProfile(
                    accessToken = validSession.accessToken,
                    userId = validSession.userId
                )
                val isAdmin = SupabaseDatabase.isCurrentUserAdmin(
                    accessToken = validSession.accessToken
                )
                _authState.value = validSession.copy(
                    name = name,
                    birthDate = profile.birthDate,
                    photoUrl = profile.photoUrl,
                    theme = profile.theme,
                    isAdmin = isAdmin
                )
                val updated = _authState.value as? AuthState.SignedIn
                if (updated != null) {
                    SessionStorage.save?.invoke(
                        updated.accessToken,
                        updated.refreshToken,
                        updated.email,
                        updated.userId,
                        updated.name,
                        updated.birthDate,
                        updated.photoUrl
                    )

//                    if (!hasLoggedUserDatabaseDebug) {
//                        hasLoggedUserDatabaseDebug = true
//                        SupabaseDatabase.debugLogCurrentUserDatabase(
//                            accessToken = updated.accessToken,
//                            userId = updated.userId
//                        )
//                    }
                }
            } catch (_: Exception) {
                // keep user signed in even if profile endpoint fails
            }
        }
    }

    private suspend fun getValidSession(current: AuthState.SignedIn): AuthState.SignedIn? {
        return if (isTokenExpired(current.accessToken)) {
            refreshSession(current)
        } else {
            current
        }
    }

    @OptIn(kotlin.io.encoding.ExperimentalEncodingApi::class, kotlin.time.ExperimentalTime::class)
    private fun isTokenExpired(token: String): Boolean {
        return try {
            val payload = token.split(".").getOrNull(1) ?: return true
            val decoded = kotlin.io.encoding.Base64.UrlSafe.decode(payload).decodeToString()
            val exp = Regex("\"exp\":(\\d+)").find(decoded)
                ?.groupValues?.get(1)?.toLong() ?: return true
            val nowSeconds = Clock.System.now().toEpochMilliseconds() / 1000
            exp < nowSeconds + 60 
        } catch (_: Exception) {
            true
        }
    }

    private suspend fun refreshSession(current: AuthState.SignedIn): AuthState.SignedIn? {
        val refreshToken = current.refreshToken ?: return null
        val refreshResponse = SupabaseAuth.refreshSession(refreshToken)
        val newAccessToken = refreshResponse.accessToken ?: return null
        val newRefreshToken = refreshResponse.refreshToken ?: refreshToken
        val user = refreshResponse.user ?: SupabaseAuth.getUser(newAccessToken) ?: return null

        val updated = current.copy(
            email = user.email ?: current.email,
            userId = user.id,
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )

        SessionStorage.save?.invoke(
            newAccessToken,
            newRefreshToken,
            updated.email,
            updated.userId,
            updated.name,
            updated.birthDate,
            updated.photoUrl
        )
        _authState.value = updated
        return updated
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
