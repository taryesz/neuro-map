package pl.edu.ug.neuromapa.ui.icons

import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.excellence_dark_heart
import neuromapa.composeapp.generated.resources.excellence_dark_medal
import neuromapa.composeapp.generated.resources.excellence_light_heart
import neuromapa.composeapp.generated.resources.excellence_light_medal
import org.jetbrains.compose.resources.DrawableResource

data class ExcellenceIcon(
    val icon: DrawableResource,
    val iconName: String,
    val iconDescription: String,
)

object ExcellencesLight {

    val excellenceLightHeart = CategoryIcon(
        icon = Res.drawable.excellence_light_heart,
        iconName = "Serduszko",
        iconDescription = "Ikona wyróżnienia 'Serduszko' w wersji jasnej."
    )

    val excellenceLightMedal = CategoryIcon(
        icon = Res.drawable.excellence_light_medal,
        iconName = "Medal",
        iconDescription = "Ikona wyróżnienia 'Medal' w wersji jasnej."
    )

}

object ExcellencesDark {

    val excellenceDarkHeart = CategoryIcon(
        icon = Res.drawable.excellence_dark_heart,
        iconName = "Serduszko",
        iconDescription = "Ikona wyróżnienia 'Serduszko' w wersji ciemnej."
    )

    val excellenceDarkMedal = CategoryIcon(
        icon = Res.drawable.excellence_dark_medal,
        iconName = "Medal",
        iconDescription = "Ikona wyróżnienia 'Medal' w wersji ciemnej."
    )

}
