package pl.edu.ug.neuromapa.screens.add.models

import org.jetbrains.compose.resources.DrawableResource

data class SelectionItem(
    val name: String,
    val description: String,
    val iconLight: DrawableResource,
    val iconDark: DrawableResource
)
