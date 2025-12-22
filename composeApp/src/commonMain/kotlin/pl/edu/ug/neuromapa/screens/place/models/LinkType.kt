package pl.edu.ug.neuromapa.screens.place.models

import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.link_website
import neuromapa.composeapp.generated.resources.link_facebook
import neuromapa.composeapp.generated.resources.link_instagram
import org.jetbrains.compose.resources.DrawableResource

enum class LinkType(val icon: DrawableResource) {
    Website(Res.drawable.link_website),
    Instagram(Res.drawable.link_instagram),
    Facebook(Res.drawable.link_facebook),
}
