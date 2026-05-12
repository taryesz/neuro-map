package pl.edu.ug.neuromapa.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import pl.edu.ug.neuromapa.screens.add.data.WpLocation
import pl.edu.ug.neuromapa.screens.add.data.WpPlaceFields
import pl.edu.ug.neuromapa.screens.add.data.WpPlaceRequest

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

    val isSubmitting = mutableStateOf(false)

    init {
        fetchPlaces()
    }

    private fun fetchPlaces() {
        viewModelScope.launch {
            try {

                // Get all the places data from "https://neuromapa.ug.edu.pl/"
                val fetchedPlaces = api.getPlaces()

                // Compose a list of places - for each place...
                val mapPoints = fetchedPlaces.mapNotNull { place ->

                    // ... get its coordinates
                    val lat = place.acfFields?.location?.getLatDouble()
                    val lng = place.acfFields?.location?.getLngDouble()

                    // ... check if the coordinates are valid
                    // If so, create an object with all the necessary information about the place
                    if (lat != null && lng != null) {

                        // This text will be shown if there is a piece of information missing about the place
                        val noInformation = "Datum not available"

                        MapPoint(
                            id = place.id,
                            name = place.title.rendered,
                            latitude = lat,
                            longitude = lng,
                            category = place.acfFields.category ?: noInformation,
                            sensoryFeatures = place.acfFields.sensoryFeatures,
                            hasMedal = place.acfFields.hasMedal,
                            hasHeart = place.acfFields.hasHeart,
                            description = place.acfFields.description ?: noInformation,
                            address = place.acfFields.address ?: noInformation,
                            photoUrl = place.acfFields.photoUrl ?: noInformation,
                            website = place.acfFields.website ?: noInformation,
                            facebook = place.acfFields.facebook ?: noInformation,
                            instagram = place.acfFields.instagram ?: noInformation,
                        )

                    } else {
                        null
                    }
                }

                // Update the state to Success (at this point, all the data should be available in mapPoints)
                _dataState.value = PlaceDataState.Success(mapPoints)

            } catch (e: Exception) {
                // TODO: add a UI response to when there is a problem with data fetching
                e.printStackTrace()
                _dataState.value = PlaceDataState.Error("Błąd pobierania danych: ${e.message}")
            }
        }
    }

    fun submitPlace(
        name: String, description: String, address: String,
        category: String, properties: List<String>,
        hasMedal: Boolean, hasHeart: Boolean,
        facebook: String, instagram: String, website: String,
        onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        if (name.isBlank() || address.isBlank() || category.isBlank()) {
            onError("Nazwa, adres oraz kategoria są wymagane.")
            return
        }

        isSubmitting.value = true

        viewModelScope.launch {

            val coords = api.getCoordinates(address)
            if (coords == null) {
                isSubmitting.value = false
                onError("Nie udało się zlokalizować tego adresu.")
                return@launch
            }

            val payload = WpPlaceRequest(
                title = name,
                content = description,
                fields = WpPlaceFields(
                    kategoria_miejsca = category,
                    opis_miejsca = description,
                    adres_miejsca = address,
                    lokalizacja = WpLocation(lat = coords.first, lng = coords.second),
                    cechy_sensoryczne = properties,
                    wyroznienie_medal = hasMedal,
                    wyroznienie_serduszko = hasHeart,
                    facebook_url = facebook,
                    instagram_url = instagram,
                    www = website
                )
            )

            // TODO: UG IT KEY
            val authHeader = ""
            val isSuccess = api.postPlace(payload, authHeader)

            isSubmitting.value = false

            if (isSuccess) {
                onSuccess()
            } else {
                onError("Nie udało się wysłać zgłoszenia. Serwer odrzucił żądanie.")
            }

        }
    }

}