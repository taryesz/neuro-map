package pl.edu.ug.neuromapa.ui.icons

import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.sensory_property_dark_access
import neuromapa.composeapp.generated.resources.sensory_property_dark_dimmed
import neuromapa.composeapp.generated.resources.sensory_property_dark_information
import neuromapa.composeapp.generated.resources.sensory_property_dark_organized
import neuromapa.composeapp.generated.resources.sensory_property_dark_quiet
import neuromapa.composeapp.generated.resources.sensory_property_dark_relax
import neuromapa.composeapp.generated.resources.sensory_property_dark_scents
import neuromapa.composeapp.generated.resources.sensory_property_dark_staff
import neuromapa.composeapp.generated.resources.sensory_property_light_access
import neuromapa.composeapp.generated.resources.sensory_property_light_dimmed
import neuromapa.composeapp.generated.resources.sensory_property_light_information
import neuromapa.composeapp.generated.resources.sensory_property_light_organized
import neuromapa.composeapp.generated.resources.sensory_property_light_quiet
import neuromapa.composeapp.generated.resources.sensory_property_light_relax
import neuromapa.composeapp.generated.resources.sensory_property_light_scents
import neuromapa.composeapp.generated.resources.sensory_property_light_staff
import org.jetbrains.compose.resources.DrawableResource

data class SensoryPropertyIcon(
    val icon: DrawableResource,
    val iconName: String,
    val iconDescription: String,
)

object SensoryPropertiesLight {
    val sensoryPropertyLightQuiet = CategoryIcon(
        icon = Res.drawable.sensory_property_light_quiet,
        iconName = "Cisza",
        iconDescription = "Ikona cechy sensorycznej 'Cisza' w wersji jasnej."
    )
    val sensoryPropertyLightAccess = CategoryIcon(
        icon = Res.drawable.sensory_property_light_access,
        iconName = "Łatwy dojazd",
        iconDescription = "Ikona cechy sensorycznej 'Łatwy dojazd' w wersji jasnej."
    )
    val sensoryPropertyLightDimmed = CategoryIcon(
        icon = Res.drawable.sensory_property_light_dimmed,
        iconName = "Przygaszone światło",
        iconDescription = "Ikona cechy sensorycznej 'Przygaszone światło' w wersji jasnej."
    )
    val sensoryPropertyLightInformation = CategoryIcon(
        icon = Res.drawable.sensory_property_light_information,
        iconName = "Jasna informacja",
        iconDescription = "Ikona cechy sensorycznej 'Jasna informacja' w wersji jasnej."
    )
    val sensoryPropertyLightOrganized = CategoryIcon(
        icon = Res.drawable.sensory_property_light_organized,
        iconName = "Organizacja przestrzeni",
        iconDescription = "Ikona cechy sensorycznej 'Organizacja przestrzeni' w wersji jasnej."
    )
    val sensoryPropertyLightRelax = CategoryIcon(
        icon = Res.drawable.sensory_property_light_relax,
        iconName = "Strefa relaksu",
        iconDescription = "Ikona cechy sensorycznej 'Strefa relaksu' w wersji jasnej."
    )
    val sensoryPropertyLightScents = CategoryIcon(
        icon = Res.drawable.sensory_property_light_scents,
        iconName = "Brak intensywnych zapachów",
        iconDescription = "Ikona cechy sensorycznej 'Brak intensywnych zapachów' w wersji jasnej."
    )
    val sensoryPropertyLightStaff = CategoryIcon(
        icon = Res.drawable.sensory_property_light_staff,
        iconName = "Pomocny personel",
        iconDescription = "Ikona cechy sensorycznej 'Pomocny personel' w wersji jasnej."
    )
}

object SensoryPropertiesDark {
    val sensoryPropertyDarkQuiet = CategoryIcon(
        icon = Res.drawable.sensory_property_dark_quiet,
        iconName = "Cisza",
        iconDescription = "Ikona cechy sensorycznej 'Cisza' w wersji ciemnej."
    )
    val sensoryPropertyDarkAccess = CategoryIcon(
        icon = Res.drawable.sensory_property_dark_access,
        iconName = "Łatwy dojazd",
        iconDescription = "Ikona cechy sensorycznej 'Łatwy dojazd' w wersji ciemnej."
    )
    val sensoryPropertyDarkDimmed = CategoryIcon(
        icon = Res.drawable.sensory_property_dark_dimmed,
        iconName = "Przygaszone światło",
        iconDescription = "Ikona cechy sensorycznej 'Przygaszone światło' w wersji ciemnej."
    )
    val sensoryPropertyDarkInformation = CategoryIcon(
        icon = Res.drawable.sensory_property_dark_information,
        iconName = "Jasna informacja",
        iconDescription = "Ikona cechy sensorycznej 'Jasna informacja' w wersji ciemnej."
    )
    val sensoryPropertyDarkOrganized = CategoryIcon(
        icon = Res.drawable.sensory_property_dark_organized,
        iconName = "Organizacja przestrzeni",
        iconDescription = "Ikona cechy sensorycznej 'Organizacja przestrzeni' w wersji ciemnej."
    )
    val sensoryPropertyDarkRelax = CategoryIcon(
        icon = Res.drawable.sensory_property_dark_relax,
        iconName = "Strefa relaksu",
        iconDescription = "Ikona cechy sensorycznej 'Strefa relaksu' w wersji ciemnej."
    )
    val sensoryPropertyDarkScents = CategoryIcon(
        icon = Res.drawable.sensory_property_dark_scents,
        iconName = "Brak intensywnych zapachów",
        iconDescription = "Ikona cechy sensorycznej 'Brak intensywnych zapachów' w wersji ciemnej."
    )
    val sensoryPropertyDarkStaff = CategoryIcon(
        icon = Res.drawable.sensory_property_dark_staff,
        iconName = "Pomocny personel",
        iconDescription = "Ikona cechy sensorycznej 'Pomocny personel' w wersji ciemnej."
    )
}
