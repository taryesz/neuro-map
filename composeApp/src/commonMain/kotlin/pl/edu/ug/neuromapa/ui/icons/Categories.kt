package pl.edu.ug.neuromapa.ui.icons

import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.category_dark_children
import neuromapa.composeapp.generated.resources.category_dark_culture
import neuromapa.composeapp.generated.resources.category_dark_food
import neuromapa.composeapp.generated.resources.category_dark_relax
import neuromapa.composeapp.generated.resources.category_dark_services
import neuromapa.composeapp.generated.resources.category_dark_support
import neuromapa.composeapp.generated.resources.category_dark_work
import neuromapa.composeapp.generated.resources.category_light_children
import neuromapa.composeapp.generated.resources.category_light_culture
import neuromapa.composeapp.generated.resources.category_light_food
import neuromapa.composeapp.generated.resources.category_light_relax
import neuromapa.composeapp.generated.resources.category_light_services
import neuromapa.composeapp.generated.resources.category_light_support
import neuromapa.composeapp.generated.resources.category_light_work
import org.jetbrains.compose.resources.DrawableResource

data class CategoryIcon(
    val icon: DrawableResource,
    val iconName: String,
    val iconDescription: String,
)

object CategoriesLight {

    val categoryLightChildren = CategoryIcon(
        icon = Res.drawable.category_light_children,
        iconName = "Dzieci",
        iconDescription = "Ikona kategorii 'Dzieci' w wersji światłej."
    )

    val categoryLightCulture = CategoryIcon(
        icon = Res.drawable.category_light_culture,
        iconName = "Kultura",
        iconDescription = "Ikona kategorii 'Kultura' w wersji światłej."
    )

    val categoryLightFood = CategoryIcon(
        icon = Res.drawable.category_light_food,
        iconName = "Jedzenie",
        iconDescription = "Ikona kategorii 'Jedzenie' w wersji światłej."
    )

    val categoryLightRelax = CategoryIcon(
        icon = Res.drawable.category_light_relax,
        iconName = "Relaks",
        iconDescription = "Ikona kategorii 'Relaks' w wersji światłej."
    )

    val categoryLightServices = CategoryIcon(
        icon = Res.drawable.category_light_services,
        iconName = "Usługi",
        iconDescription = "Ikona kategorii 'Usługi' w wersji światłej."
    )

    val categoryLightSupport = CategoryIcon(
        icon = Res.drawable.category_light_support,
        iconName = "Wsparcie",
        iconDescription = "Ikona kategorii 'Wsparcie' w wersji światłej."
    )

    val categoryLightWork = CategoryIcon(
        icon = Res.drawable.category_light_work,
        iconName = "Praca",
        iconDescription = "Ikona kategorii 'Praca' w wersji światłej."
    )

}

object CategoriesDark {

    val categoryDarkChildren = CategoryIcon(
        icon = Res.drawable.category_dark_children,
        iconName = "Dzieci",
        iconDescription = "Ikona kategorii 'Dzieci' w wersji ciemnej."
    )

    val categoryDarkCulture = CategoryIcon(
        icon = Res.drawable.category_dark_culture,
        iconName = "Kultura",
        iconDescription = "Ikona kategorii 'Kultura' w wersji ciemnej."
    )

    val categoryDarkFood = CategoryIcon(
        icon = Res.drawable.category_dark_food,
        iconName = "Jedzenie",
        iconDescription = "Ikona kategorii 'Jedzenie' w wersji ciemnej."
    )

    val categoryDarkRelax = CategoryIcon(
        icon = Res.drawable.category_dark_relax,
        iconName = "Relaks",
        iconDescription = "Ikona kategorii 'Relaks' w wersji ciemnej."
    )

    val categoryDarkServices = CategoryIcon(
        icon = Res.drawable.category_dark_services,
        iconName = "Usługi",
        iconDescription = "Ikona kategorii 'Usługi' w wersji ciemnej."
    )

    val categoryDarkSupport = CategoryIcon(
        icon = Res.drawable.category_dark_support,
        iconName = "Wsparcie",
        iconDescription = "Ikona kategorii 'Wsparcie' w wersji ciemnej."
    )

    val categoryDarkWork = CategoryIcon(
        icon = Res.drawable.category_dark_work,
        iconName = "Praca",
        iconDescription = "Ikona kategorii 'Praca' w wersji ciemnej."
    )

}
