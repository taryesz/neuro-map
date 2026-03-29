package pl.edu.ug.neuromapa.screens.place.data

import pl.edu.ug.neuromapa.screens.place.enums.LinkType

data class Link(
    val type: LinkType,
    val url: String,
    val label: String
)
