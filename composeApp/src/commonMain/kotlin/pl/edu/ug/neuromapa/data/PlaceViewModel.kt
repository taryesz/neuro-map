package pl.edu.ug.neuromapa.data
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



sealed class PlaceDataState {
    object Loading : PlaceDataState()
    data class Success(val mapPoints: List<MapPoint>) : PlaceDataState()
    data class Error(val message: String) : PlaceDataState()
}

class PlaceViewModel : ViewModel() {

    private val api = NeuroMapApi()

    private val _dataState = MutableStateFlow<PlaceDataState>(PlaceDataState.Loading)
    val dataState: StateFlow<PlaceDataState> = _dataState

    init {
        fetchPlaces()
    }

    private fun fetchPlaces() {
        viewModelScope.launch {
            try {
                val fetchedPlaces = api.getPlaces()

                val mapPoints = fetchedPlaces.mapNotNull { place ->
                    val lat = place.acfFields?.location?.getLatDouble()
                    val lng = place.acfFields?.location?.getLngDouble()

                    if (lat != null && lng != null) {
                        MapPoint(
                            id = place.id,
                            name = place.title.rendered,
                            latitude = lat,
                            longitude = lng,
                            category = place.acfFields.category ?: "Without categories",
                            sensoryFeatures = place.acfFields.sensoryFeatures,
                            hasMedal = place.acfFields.hasMedal,
                            hasHeart = place.acfFields.hasHeart
                        )
                    } else {
                        null
                    }
                }

                _dataState.value = PlaceDataState.Success(mapPoints)
                println("MapPoint: ${mapPoints.size}")

            } catch (e: Exception) {
                _dataState.value = PlaceDataState.Error("Error: ")
            }
        }
    }
}
