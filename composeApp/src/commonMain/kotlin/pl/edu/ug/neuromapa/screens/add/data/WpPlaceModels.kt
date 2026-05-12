package pl.edu.ug.neuromapa.screens.add.data

import kotlinx.serialization.Serializable

@Serializable
data class NominatimResponse(
    val lat: String,
    val lon: String
)

@Serializable
data class WpPlaceRequest(
    val title: String,
    val content: String,
    val status: String = "pending",
    val type: String = "miejsce",
    val fields: WpPlaceFields
)

@Serializable
data class WpPlaceFields(
    val kategoria_miejsca: String,
    val opis_miejsca: String,
    val adres_miejsca: String,
    val lokalizacja: WpLocation,
    val cechy_sensoryczne: List<String>,
    val organizacja_przestrzeni: String = "",
    val dostepnosc_informacyjna: String = "",
    val transport: String = "",
    val wyroznienie_medal: Boolean,
    val wyroznienie_serduszko: Boolean,
    val facebook_url: String,
    val instagram_url: String,
    val zdjecie_miejsca: String = "",
    val www: String
)

@Serializable
data class WpLocation(
    val lat: Double,
    val lng: Double
)