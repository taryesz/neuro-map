package pl.edu.ug.neuromapa.screens.place.helpers

import androidx.compose.runtime.Composable
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.category_light_children
import neuromapa.composeapp.generated.resources.category_light_culture
import neuromapa.composeapp.generated.resources.category_light_food
import neuromapa.composeapp.generated.resources.category_light_relax
import neuromapa.composeapp.generated.resources.category_light_services
import neuromapa.composeapp.generated.resources.category_light_support
import neuromapa.composeapp.generated.resources.category_light_work
import neuromapa.composeapp.generated.resources.excellence_dark_heart
import neuromapa.composeapp.generated.resources.excellence_dark_medal
import neuromapa.composeapp.generated.resources.sensory_property_dark_access
import neuromapa.composeapp.generated.resources.sensory_property_dark_dimmed
import neuromapa.composeapp.generated.resources.sensory_property_dark_information
import neuromapa.composeapp.generated.resources.sensory_property_dark_organized
import neuromapa.composeapp.generated.resources.sensory_property_dark_quiet
import neuromapa.composeapp.generated.resources.sensory_property_dark_relax
import neuromapa.composeapp.generated.resources.sensory_property_dark_scents
import neuromapa.composeapp.generated.resources.sensory_property_dark_staff
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun getCategoryIconHelper(category: String): DrawableResource {
    return when (category.lowercase()) {
        "jedzenie", "food" -> Res.drawable.category_light_food
        "relaks", "relax" -> Res.drawable.category_light_relax
        "dzieci", "children" -> Res.drawable.category_light_children
        "kultura", "culture" -> Res.drawable.category_light_culture
        "usługi", "services" -> Res.drawable.category_light_services
        "wsparcie", "support" -> Res.drawable.category_light_support
        "praca", "work" -> Res.drawable.category_light_work
        else -> Res.drawable.category_light_food // TODO
    }
}

@Composable
fun getFeatureIconHelper(feature: String): DrawableResource {
    return when (feature.lowercase()) {
        "ciche", "cisza" -> Res.drawable.sensory_property_dark_quiet
        "pomocny_personel" -> Res.drawable.sensory_property_dark_staff
        "przygaszone_swiatlo" -> Res.drawable.sensory_property_dark_dimmed
        "organizacja_przestrzeni" -> Res.drawable.sensory_property_dark_organized
        "dostepnosc_informacyjna" -> Res.drawable.sensory_property_dark_information
        "strefa_relaksu" -> Res.drawable.sensory_property_dark_relax
        "latwy_dojazd" -> Res.drawable.sensory_property_dark_access
        "brak_zapachow", "brak_intensywnych_zapachow" -> Res.drawable.sensory_property_dark_scents
        "hasmedal" -> Res.drawable.excellence_dark_medal
        "hasheart" -> Res.drawable.excellence_dark_heart
        else -> Res.drawable.sensory_property_dark_access // TODO
    }
}

@Composable
fun getFeatureNameHelper(rawName: String): String {
    return when (rawName.lowercase()) {
        "ciche", "cisza" -> "Ciche miejsce"
        "pomocny_personel" -> "Pomocny personel"
        "przygaszone_swiatlo" -> "Przygaszone światło"
        "organizacja_przestrzeni" -> "Organizacja przestrzeni"
        "dostepnosc_informacyjna" -> "Dostępność informacyjna"
        "strefa_relaksu" -> "Strefa relaksu"
        "latwy_dojazd" -> "Łatwy dojazd"
        "brak_zapachow", "brak_intensywnych_zapachow" -> "Brak zapachów"
        "hasmedal" -> "Medal"
        "hasheart" -> "Serduszko"
        else -> rawName
            .replace("_", " ")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}