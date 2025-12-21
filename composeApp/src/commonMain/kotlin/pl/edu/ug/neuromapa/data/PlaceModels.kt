package pl.edu.ug.neuromapa.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
data class PlaceResponse(
    val id: Int,
    val title: RenderedText,
    @SerialName("acf")
    val acfFields: PlaceFields? = null
)

@Serializable
data class RenderedText(
    val rendered: String
)

@Serializable
data class PlaceFields(

    @SerialName("kategoria_miejsca") val category: String? = null,
    @SerialName("adres_miejsca") val address: String? = null,
    @SerialName("opis_miejsca") val description: String? = null,
    @SerialName("transport") val transport: String? = null,


    @SerialName("zdjecie_miejsca") val photoUrl: String? = null,
    @SerialName("www") val website: String? = null,
    @SerialName("facebook_url") val facebook: String? = null,
    @SerialName("instagram_url") val instagram: String? = null,

    @SerialName("cechy_sensoryczne") val sensoryFeatures: List<String> = emptyList(),

    @SerialName("wyroznienie_medal") val hasMedal: Boolean = false,
    @SerialName("wyroznienie_serduszko") val hasHeart: Boolean = false,

    @SerialName("lokalizacja") val location: LocationData? = null
)

@Serializable
data class LocationData(
    val lat: JsonElement? = null,
    val lng: JsonElement? = null
) {

    fun getLatDouble(): Double? {
        if (lat == null) return null
        return try {
            lat.jsonPrimitive.doubleOrNull ?: lat.jsonPrimitive.contentOrNull?.toDoubleOrNull()
        } catch (e: Exception) { null }
    }

    fun getLngDouble(): Double? {
        if (lng == null) return null
        return try {
            lng.jsonPrimitive.doubleOrNull ?: lng.jsonPrimitive.contentOrNull?.toDoubleOrNull()
        } catch (e: Exception) { null }
    }
}

data class MapPoint(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val category: String,
    val sensoryFeatures: List<String>,
    val hasMedal: Boolean,
    val hasHeart: Boolean
)
