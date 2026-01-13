package pl.edu.ug.neuromapa.helpers

import kotlin.math.* // <--- TO JEST KLUCZOWE

// Zakładam, że masz tu jakąś funkcję do obliczania dystansu, np. Haversine
fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val r = 6371 // Promień Ziemi w km
    val dLat = (lat2 - lat1) * (PI / 180) // Zamiast Math.PI używamy PI
    val dLon = (lon2 - lon1) * (PI / 180)
    val a = sin(dLat / 2) * sin(dLat / 2) + // Zamiast Math.sin używamy sin
            cos(lat1 * (PI / 180)) * cos(lat2 * (PI / 180)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a)) // Zamiast Math.atan2 itd.
    return r * c
}