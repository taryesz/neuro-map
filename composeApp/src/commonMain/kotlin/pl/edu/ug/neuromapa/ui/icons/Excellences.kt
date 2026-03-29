package pl.edu.ug.neuromapa.ui.icons

import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.excellence_dark_heart
import neuromapa.composeapp.generated.resources.excellence_dark_medal
import neuromapa.composeapp.generated.resources.excellence_light_heart
import neuromapa.composeapp.generated.resources.excellence_light_medal
import pl.edu.ug.neuromapa.ui.icons.data.Icon

object ExcellencesLight {

    val excellenceLightHeart = Icon(
        icon = Res.drawable.excellence_light_heart,
        iconName = "Serduszko",
        iconDescription = "Ikona wyróżnienia 'Serduszko' w wersji jasnej."
    )

    val excellenceLightMedal = Icon(
        icon = Res.drawable.excellence_light_medal,
        iconName = "Medal",
        iconDescription = "Ikona wyróżnienia 'Medal' w wersji jasnej."
    )

}

object ExcellencesDark {

    val excellenceDarkHeart = Icon(
        icon = Res.drawable.excellence_dark_heart,
        iconName = "Serduszko",
        iconDescription = "Ikona wyróżnienia 'Serduszko' w wersji ciemnej."
    )

    val excellenceDarkMedal = Icon(
        icon = Res.drawable.excellence_dark_medal,
        iconName = "Medal",
        iconDescription = "Ikona wyróżnienia 'Medal' w wersji ciemnej."
    )

}
