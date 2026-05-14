package pl.edu.ug.neuromapa.screens.favorites
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pl.edu.ug.neuromapa.data.auth.AuthState
import pl.edu.ug.neuromapa.data.auth.SupabaseDatabase

class FavoritesViewModel : ViewModel() {
    val showLoginFavoriteAlert = MutableStateFlow(false)
    private val _favoritePlaceIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoritePlaceIds: StateFlow<Set<Int>> = _favoritePlaceIds
    fun toggleFavorite(placeId: Int, authState: AuthState) {
        if (authState !is AuthState.SignedIn) {
            showLoginFavoriteAlert.value = true
            return
        }

        if (authState is AuthState.SignedIn) {
            val isCurrentlyFavorite = _favoritePlaceIds.value.contains(placeId)
            println("Is currently favorite: $isCurrentlyFavorite")

            viewModelScope.launch {
                try {
                    val success = if (isCurrentlyFavorite) {
                        SupabaseDatabase.removeFavoritePlace(authState.userId, placeId, authState.accessToken)
                    } else {
                        SupabaseDatabase.addFavoritePlace(authState.userId, placeId, authState.accessToken)
                    }

                    if (success) {
                        _favoritePlaceIds.value = if (isCurrentlyFavorite) {
                            _favoritePlaceIds.value - placeId
                        } else {
                            _favoritePlaceIds.value + placeId
                        }
                        println("SUCCESS: Favorite status changed in DB")
                    } else {
                        println("ERROR: DB returned non-success status")
                    }
                } catch (e: Exception) {
                    println("EXCEPTION: ${e.stackTraceToString()}")
                }
            }
        }
    }

    fun dismissLoginAlert() {
        showLoginFavoriteAlert.value = false
    }

    // Function to load the list (call it after logging in to your account)
    fun loadFavorites(authState: AuthState) {
        if (authState is AuthState.SignedIn) {
            viewModelScope.launch {
                try {
                    val ids = SupabaseDatabase.fetchFavoritePlaces(
                        userId = authState.userId,
                        accessToken = authState.accessToken
                    )

                    _favoritePlaceIds.value = ids.toSet()
                    println("Loaded ${ids.size} favorite places")

                } catch (e: Exception) {
                    println("Error toggling favorites UI: ${e.message}")
                }
            }
        }
    }

    fun clearFavorites() {
        _favoritePlaceIds.value = emptySet()
        println("Favorites cleared after sign out")
    }
}