package pl.edu.ug.neuromapa.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import pl.edu.ug.neuromapa.screens.add.data.SelectionItem
import pl.edu.ug.neuromapa.ui.icons.*

@Composable
fun getCategories() = remember {
    listOf(
        SelectionItem(
            name = CategoriesLight.categoryLightChildren.iconName,
            description = CategoriesLight.categoryLightChildren.iconDescription,
            iconLight = CategoriesLight.categoryLightChildren.icon,
            iconDark = CategoriesDark.categoryDarkChildren.icon
        ),
        SelectionItem(
            name = CategoriesLight.categoryLightCulture.iconName,
            description = CategoriesLight.categoryLightCulture.iconDescription,
            iconLight = CategoriesLight.categoryLightCulture.icon,
            iconDark = CategoriesDark.categoryDarkCulture.icon
        ),
        SelectionItem(
            name = CategoriesLight.categoryLightFood.iconName,
            description = CategoriesLight.categoryLightFood.iconDescription,
            iconLight = CategoriesLight.categoryLightFood.icon,
            iconDark = CategoriesDark.categoryDarkFood.icon
        ),
        SelectionItem(
            name = CategoriesLight.categoryLightRelax.iconName,
            description = CategoriesLight.categoryLightRelax.iconDescription,
            iconLight = CategoriesLight.categoryLightRelax.icon,
            iconDark = CategoriesDark.categoryDarkRelax.icon
        ),
        SelectionItem(
            name = CategoriesLight.categoryLightServices.iconName,
            description = CategoriesLight.categoryLightServices.iconDescription,
            iconLight = CategoriesLight.categoryLightServices.icon,
            iconDark = CategoriesDark.categoryDarkServices.icon
        ),
        SelectionItem(
            name = CategoriesLight.categoryLightSupport.iconName,
            description = CategoriesLight.categoryLightSupport.iconDescription,
            iconLight = CategoriesLight.categoryLightSupport.icon,
            iconDark = CategoriesDark.categoryDarkSupport.icon
        ),
        SelectionItem(
            name = CategoriesLight.categoryLightWork.iconName,
            description = CategoriesLight.categoryLightWork.iconDescription,
            iconLight = CategoriesLight.categoryLightWork.icon,
            iconDark = CategoriesDark.categoryDarkWork.icon
        )
    )
}

@Composable
fun getSensoryProperties() = remember {
    listOf(
        SelectionItem(
            name = SensoryPropertiesLight.sensoryPropertyLightAccess.iconName,
            description = SensoryPropertiesLight.sensoryPropertyLightAccess.iconDescription,
            iconLight = SensoryPropertiesLight.sensoryPropertyLightAccess.icon,
            iconDark = SensoryPropertiesDark.sensoryPropertyDarkAccess.icon
        ),
        SelectionItem(
            name = SensoryPropertiesLight.sensoryPropertyLightDimmed.iconName,
            description = SensoryPropertiesLight.sensoryPropertyLightDimmed.iconDescription,
            iconLight = SensoryPropertiesLight.sensoryPropertyLightDimmed.icon,
            iconDark = SensoryPropertiesDark.sensoryPropertyDarkDimmed.icon
        ),
        SelectionItem(
            name = SensoryPropertiesLight.sensoryPropertyLightScents.iconName,
            description = SensoryPropertiesLight.sensoryPropertyLightScents.iconDescription,
            iconLight = SensoryPropertiesLight.sensoryPropertyLightScents.icon,
            iconDark = SensoryPropertiesDark.sensoryPropertyDarkScents.icon
        ),
        SelectionItem(
            name = SensoryPropertiesLight.sensoryPropertyLightQuiet.iconName,
            description = SensoryPropertiesLight.sensoryPropertyLightQuiet.iconDescription,
            iconLight = SensoryPropertiesLight.sensoryPropertyLightQuiet.icon,
            iconDark = SensoryPropertiesDark.sensoryPropertyDarkQuiet.icon
        ),
        SelectionItem(
            name = SensoryPropertiesLight.sensoryPropertyLightRelax.iconName,
            description = SensoryPropertiesLight.sensoryPropertyLightRelax.iconDescription,
            iconLight = SensoryPropertiesLight.sensoryPropertyLightRelax.icon,
            iconDark = SensoryPropertiesDark.sensoryPropertyDarkRelax.icon
        ),
        SelectionItem(
            name = SensoryPropertiesLight.sensoryPropertyLightInformation.iconName,
            description = SensoryPropertiesLight.sensoryPropertyLightInformation.iconDescription,
            iconLight = SensoryPropertiesLight.sensoryPropertyLightInformation.icon,
            iconDark = SensoryPropertiesDark.sensoryPropertyDarkInformation.icon
        ),
        SelectionItem(
            name = SensoryPropertiesLight.sensoryPropertyLightOrganized.iconName,
            description = SensoryPropertiesLight.sensoryPropertyLightOrganized.iconDescription,
            iconLight = SensoryPropertiesLight.sensoryPropertyLightOrganized.icon,
            iconDark = SensoryPropertiesDark.sensoryPropertyDarkOrganized.icon
        ),
        SelectionItem(
            name = SensoryPropertiesLight.sensoryPropertyLightStaff.iconName,
            description = SensoryPropertiesLight.sensoryPropertyLightStaff.iconDescription,
            iconLight = SensoryPropertiesLight.sensoryPropertyLightStaff.icon,
            iconDark = SensoryPropertiesDark.sensoryPropertyDarkStaff.icon
        ),
    )
}

@Composable
fun getExcellenceMarks() = remember {
    listOf(
        SelectionItem(
            name = ExcellencesLight.excellenceLightHeart.iconName,
            description = ExcellencesLight.excellenceLightHeart.iconDescription,
            iconLight = ExcellencesLight.excellenceLightHeart.icon,
            iconDark = ExcellencesDark.excellenceDarkHeart.icon
        ),
        SelectionItem(
            name = ExcellencesLight.excellenceLightMedal.iconName,
            description = ExcellencesLight.excellenceLightMedal.iconDescription,
            iconLight = ExcellencesLight.excellenceLightMedal.icon,
            iconDark = ExcellencesDark.excellenceDarkMedal.icon
        )
    )
}