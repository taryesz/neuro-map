package pl.edu.ug.neuromapa.screens.place.models

import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.website
import org.jetbrains.compose.resources.DrawableResource

enum class LinkType(val icon: DrawableResource, val iconDescription: String) {
    Website(Res.drawable.website, "Ikona linku do strony internetowej"),
    // TODO: Instagram(Res.drawable.instagram),
    // TODO: Facebook(Res.drawable.facebook)
}
