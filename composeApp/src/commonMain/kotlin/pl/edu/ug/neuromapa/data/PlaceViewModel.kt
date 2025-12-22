package pl.edu.ug.neuromapa.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

sealed class PlaceDataState {
    object Loading : PlaceDataState()
    data class Success(val mapPoints: List<MapPoint>) : PlaceDataState()
    data class Error(val message: String) : PlaceDataState()
}

class PlaceViewModel : ViewModel() {

    private val api = NeuroMapApi()

    private val _dataState = MutableStateFlow<PlaceDataState>(PlaceDataState.Loading)
    val dataState: StateFlow<PlaceDataState> = _dataState

    var selectedCategories = mutableStateOf<Set<String>>(emptySet())
    var selectedProperties = mutableStateOf<Set<String>>(emptySet())
    var selectedExcellences = mutableStateOf<Set<String>>(emptySet())

    var searchQuery = mutableStateOf("")

    init {
        fetchPlaces()
    }

    private fun fetchPlaces() {
        viewModelScope.launch {
            try {
                val fetchedPlaces = api.getPlaces()

                val mapPoints = fetchedPlaces.mapNotNull { place ->
                    // Pobieramy koordynaty bezpiecznie
                    val lat = place.acfFields?.location?.getLatDouble()
                    val lng = place.acfFields?.location?.getLngDouble()

                    // Sprawdzamy, czy koordynaty istnieją
                    if (lat != null && lng != null) {
                        MapPoint(
                            id = place.id,
                            name = place.title.rendered,
                            latitude = lat,
                            longitude = lng,
                            category = place.acfFields.category ?: "Bez kategorii",
                            sensoryFeatures = place.acfFields.sensoryFeatures,
                            hasMedal = place.acfFields.hasMedal,
                            hasHeart = place.acfFields.hasHeart,
                            description = place.acfFields.description ?: "Bez opisu",
                            address = place.acfFields.address ?: "Bez adresu",
                            photoUrl = place.acfFields.photoUrl ?: "Bez zdjęcia",
                            website = place.acfFields.website ?: "Bez strony www",
                            facebook = place.acfFields.facebook ?: "Bez profilu na Facebook",
                            instagram = place.acfFields.instagram ?: "Bez profilu na Instagram",
                        )
                    } else {
                        null
                    }
                }

                _dataState.value = PlaceDataState.Success(mapPoints)
                println("Pobrano punktów: ${mapPoints.size}")

            } catch (e: Exception) {
                e.printStackTrace()
                _dataState.value = PlaceDataState.Error("Błąd pobierania danych: ${e.message}")
            }
        }
    }
}