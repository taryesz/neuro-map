package pl.edu.ug.neuromapa.screens.place.models

import org.jetbrains.compose.resources.DrawableResource

data class Place(
    val name: String,
    val photo: DrawableResource,
    val photoDescription: String,
    val categoryIcon: DrawableResource,
    val categoryIconDescription: String,
    val description: String,
    val address: String,
    val properties: List<SensoryProperty>,
    val links: List<Link>
)
