package pl.edu.ug.neuromapa.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import pl.edu.ug.neuromapa.BuildConfig
import pl.edu.ug.neuromapa.screens.add.data.WpLocation
import pl.edu.ug.neuromapa.screens.add.data.WpPlaceFields
import pl.edu.ug.neuromapa.screens.add.data.WpPlaceRequest
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

sealed class PlaceDataState {
    object Loading : PlaceDataState()
    data class Success(val mapPoints: List<MapPoint>) : PlaceDataState()
    data class Error(val message: String) : PlaceDataState()
}

sealed class AdminDraftPlacesState {
    object Idle : AdminDraftPlacesState()
    object Loading : AdminDraftPlacesState()
    data class Success(val drafts: List<AdminDraftPlace>) : AdminDraftPlacesState()
    data class Error(val message: String) : AdminDraftPlacesState()
}

data class AdminDraftPlace(
    val id: String,
    val title: String,
    val status: String,
    val category: String,
    val description: String,
    val address: String,
    val sensoryFeatures: List<String>,
    val hasMedal: Boolean,
    val hasHeart: Boolean,
    val website: String,
    val facebook: String,
    val instagram: String,
    val latitude: Double?,
    val longitude: Double?,
    val parameters: List<AdminPlaceParameter>,
    val rawJson: String
)

data class AdminPlaceParameter(
    val name: String,
    val value: String
)

class PlaceViewModel : ViewModel() {

    private val api = NeuroMapApi()
    private val prettyJson = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
        coerceInputValues = true
    }

    private val _dataState = MutableStateFlow<PlaceDataState>(PlaceDataState.Loading)
    val dataState: StateFlow<PlaceDataState> = _dataState

    private val _adminDraftPlacesState = MutableStateFlow<AdminDraftPlacesState>(AdminDraftPlacesState.Idle)
    val adminDraftPlacesState: StateFlow<AdminDraftPlacesState> = _adminDraftPlacesState

    var selectedCategories = mutableStateOf<Set<String>>(emptySet())
    var selectedProperties = mutableStateOf<Set<String>>(emptySet())
    var selectedExcellences = mutableStateOf<Set<String>>(emptySet())

    var searchQuery = mutableStateOf("")

    val isSubmitting = mutableStateOf(false)
    val isPublishingDraft = mutableStateOf(false)
//    val isRejectingDraft = mutableStateOf(false)

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

//    fun debugLogAllApiPlaces() {
//        viewModelScope.launch {
//            val authHeader = createWordPressAuthHeader()
//            if (authHeader == null) {
//                println("NEUROMAPA PLACES API DEBUG ERROR: missing WP_USERNAME or WP_APPLICATION_PASSWORD")
//                return@launch
//            }
//
//            api.debugLogAllPlacesRaw(authHeader = authHeader)
//        }
//    }

    fun loadAdminDraftPlaces() {
        viewModelScope.launch {
            val authHeader = createWordPressAuthHeader()
            if (authHeader == null) {
                _adminDraftPlacesState.value = AdminDraftPlacesState.Error(
                    "Brakuje WP_USERNAME albo WP_APPLICATION_PASSWORD w local.properties."
                )
                return@launch
            }

            _adminDraftPlacesState.value = AdminDraftPlacesState.Loading

            try {
                val rawDrafts = api.getRawPlacesByStatus(
                    authHeader = authHeader,
                    status = "draft"
                )
                val draftPlaces = rawDrafts.mapIndexed { index, draft ->
                    draft.toAdminDraftPlace(index)
                }
                _adminDraftPlacesState.value = AdminDraftPlacesState.Success(draftPlaces)
            } catch (e: Exception) {
                _adminDraftPlacesState.value = AdminDraftPlacesState.Error(
                    e.message ?: "Nie udało się pobrać szkiców z WordPress API."
                )
            }
        }
    }

    fun publishAdminDraftPlace(
        draft: AdminDraftPlace,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (isPublishingDraft.value) return

        viewModelScope.launch {
            val authHeader = createWordPressAuthHeader()
            if (authHeader == null) {
                onError("Brakuje WP_USERNAME albo WP_APPLICATION_PASSWORD w local.properties.")
                return@launch
            }

            isPublishingDraft.value = true

            try {
                val isSuccess = api.publishPlaceDraft(
                    placeId = draft.id,
                    authHeader = authHeader
                )

                if (isSuccess) {
                    onSuccess()
                    loadAdminDraftPlaces()
                } else {
                    onError("Nie udało się opublikować szkicu. WordPress odrzucił żądanie.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Nie udało się opublikować szkicu.")
            } finally {
                isPublishingDraft.value = false
            }
        }
    }

//    fun rejectAdminDraftPlace(
//        draft: AdminDraftPlace,
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit
//    ) {
//        if (isRejectingDraft.value) return
//
//        viewModelScope.launch {
//            val authHeader = createWordPressAuthHeader()
//            if (authHeader == null) {
//                onError("Brakuje WP_USERNAME albo WP_APPLICATION_PASSWORD w local.properties.")
//                return@launch
//            }
//
//            isRejectingDraft.value = true
//
//            try {
//                val result = api.deletePlaceDraft(
//                    placeId = draft.id,
//                    authHeader = authHeader
//                )
//
//                if (result.isSuccess) {
//                    onSuccess()
//                    loadAdminDraftPlaces()
//                } else {
//                    onError(
//                        result.errorMessage
//                            ?: "Nie udało się odrzucić szkicu. WordPress odrzucił żądanie."
//                    )
//                }
//            } catch (e: Exception) {
//                onError(e.message ?: "Nie udało się odrzucić szkicu.")
//            } finally {
//                isRejectingDraft.value = false
//            }
//        }
//    }

    private fun JsonElement.toAdminDraftPlace(index: Int): AdminDraftPlace {
        val objectValue = this as? JsonObject
        val acf = objectValue?.get("acf") as? JsonObject
        val location = acf?.get("lokalizacja") as? JsonObject
        val id = objectValue?.stringField("id") ?: (index + 1).toString()
        val status = objectValue?.stringField("status") ?: "draft"
        val titleObject = objectValue?.get("title") as? JsonObject
        val title = titleObject?.stringField("raw")
            ?: titleObject?.stringField("rendered")
            ?: objectValue?.stringField("slug")
            ?: "Szkic $id"
        val sensoryFeatures = acf?.arrayField("cechy_sensoryczne") ?: emptyList()
        val hasMedal = acf?.booleanField("wyroznienie_medal") ?: false
        val hasHeart = acf?.booleanField("wyroznienie_serduszko") ?: false

        return AdminDraftPlace(
            id = id,
            title = title.ifBlank { "Szkic $id" },
            status = status,
            category = acf?.stringField("kategoria_miejsca").orEmpty(),
            description = acf?.stringField("opis_miejsca").orEmpty().stripHtml(),
            address = acf?.stringField("adres_miejsca").orEmpty(),
            sensoryFeatures = sensoryFeatures,
            hasMedal = hasMedal,
            hasHeart = hasHeart,
            website = acf?.stringField("www").orEmpty(),
            facebook = acf?.stringField("facebook_url").orEmpty(),
            instagram = acf?.stringField("instagram_url").orEmpty(),
            latitude = location?.doubleField("lat"),
            longitude = location?.doubleField("lng"),
            parameters = this.flattenParameters(),
            rawJson = prettyJson.encodeToString(JsonElement.serializer(), this)
        )
    }

    private fun JsonObject.stringField(key: String): String? {
        return this[key]?.jsonPrimitive?.contentOrNull
    }

    private fun JsonObject.booleanField(key: String): Boolean? {
        val value = this[key]?.jsonPrimitive ?: return null
        return value.booleanOrNull ?: value.contentOrNull.equals("true", ignoreCase = true)
    }

    private fun JsonObject.doubleField(key: String): Double? {
        val value = this[key]?.jsonPrimitive ?: return null
        return value.doubleOrNull ?: value.contentOrNull?.toDoubleOrNull()
    }

    private fun JsonObject.arrayField(key: String): List<String>? {
        return (this[key] as? JsonArray)?.mapNotNull { element ->
            element.jsonPrimitive.contentOrNull
        }
    }

    private fun JsonElement.flattenParameters(prefix: String = ""): List<AdminPlaceParameter> {
        return when (this) {
            is JsonObject -> entries.flatMap { (key, value) ->
                val name = if (prefix.isBlank()) key else "$prefix.$key"
                value.flattenParameters(name)
            }
            is JsonArray -> {
                if (isEmpty()) {
                    listOf(AdminPlaceParameter(prefix, "[]"))
                } else {
                    mapIndexed { index, value ->
                        value.flattenParameters("$prefix[$index]")
                    }.flatten()
                }
            }
            is JsonPrimitive -> listOf(AdminPlaceParameter(prefix, displayValue()))
        }
    }

    private fun JsonPrimitive.displayValue(): String {
        return contentOrNull ?: toString()
    }

    private fun String.stripHtml(): String {
        return replace(Regex("<[^>]*>"), " ")
            .replace("&nbsp;", " ")
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&#8211;", "-")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun createWordPressAuthHeader(): String? {
        val wpUsername = BuildConfig.WP_USERNAME
        val wpAppPassword = BuildConfig.WP_APPLICATION_PASSWORD

        if (wpUsername.isBlank() || wpAppPassword.isBlank()) {
            return null
        }

        val credentials = "$wpUsername:$wpAppPassword"
        val base64Credentials = Base64.encode(credentials.encodeToByteArray())
        return "Basic $base64Credentials"
    }

    fun submitPlace(
        name: String, description: String, address: String,
        category: String, properties: List<String>,
        hasMedal: Boolean, hasHeart: Boolean,
        facebook: String, instagram: String, website: String,
        authHeader: String,
        submitterName: String,
        submitterEmail: String,
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

            val displayName = if (submitterName.isNotBlank()) submitterName else "Anonim"

            val fullContent = """
                <p><strong>Zgłoszenie z aplikacji NeuroMapa:</strong><br>
                Użytkownik: $displayName<br>
                E-mail: <a href="mailto:$submitterEmail">$submitterEmail</a></p>
            """.trimIndent()

            val payload = WpPlaceRequest(
                title = name,
                content = fullContent,
                acf = WpPlaceFields(
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

            val isSuccess = api.postPlace(payload, authHeader)

            isSubmitting.value = false

            if (isSuccess) {
                onSuccess()
            } else {
                onError("Nie udało się wysłać zgłoszenia. Serwer odrzucił żądanie. Upewnij się, że masz połączenie z Internetem i dane są poprawne.")
            }

        }
    }

}
