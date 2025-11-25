package pl.edu.ug.neuromapa.screens.add

import SelectionItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.excellence_description
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.screens.add.components.FormButton
import pl.edu.ug.neuromapa.screens.add.components.FormSection
import pl.edu.ug.neuromapa.screens.add.components.FormSelection
import pl.edu.ug.neuromapa.screens.map.settings.bodyWidePadding
import pl.edu.ug.neuromapa.screens.place.settings.bodyNarrowSpacing
import pl.edu.ug.neuromapa.ui.Background
import pl.edu.ug.neuromapa.ui.icons.CategoriesDark
import pl.edu.ug.neuromapa.ui.icons.CategoriesLight
import pl.edu.ug.neuromapa.ui.icons.ExcellencesDark
import pl.edu.ug.neuromapa.ui.icons.ExcellencesLight
import pl.edu.ug.neuromapa.ui.icons.SensoryPropertiesDark
import pl.edu.ug.neuromapa.ui.icons.SensoryPropertiesLight

@Composable
fun AddScreen(
    userProfileImage: DrawableResource,
) {

    // Form states
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var instagram by remember { mutableStateOf("") }
    var facebook by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }
    var selectedProperties by remember { mutableStateOf(setOf<String>()) }
    var selectedExcellences by remember { mutableStateOf(setOf<String>()) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // Base Header
            Header(
                title = "Zgłoś miejsce",
                userProfileImage = userProfileImage,
                showProfile = true,
                roundBottomCorners = true,
                onProfileClick = { println("Profile clicked") },
            )

            // Body (Content)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background)
                    .padding(bodyWidePadding),
                verticalArrangement = Arrangement.spacedBy(bodyNarrowSpacing)
            )
            {

                FormSection(
                    title = "Nazwa",
                    placeholder = "Jak nazywa się to wyjątkowe miejsce?",
                    value = name,
                    onValueChange = { name = it }
                )

                val categories = remember {
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

                FormSelection(
                    title = "Kategoria",
                    items = categories,
                    selectedItems = selectedCategories,
                    onSelectionChange = { selectedCategories = it }
                )

                FormSection(
                    title = "Opis",
                    placeholder = "Co sprawia, że czujesz się tu dobrze?",
                    value = description,
                    onValueChange = { description = it }
                )

                FormSection(
                    title = "Adres",
                    placeholder = "Gdzie znajdziemy ten bezpieczny zakątek?",
                    value = address,
                    onValueChange = { address = it }
                )

                val sensoryProperties = remember {
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

                FormSelection(
                    title = "Cechy sensoryczne",
                    items = sensoryProperties,
                    selectedItems = selectedProperties,
                    onSelectionChange = { selectedProperties = it }
                )

                val excellenceMarks = remember {
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
                ) }

                FormSelection(
                    title = "Wyróżnienia",
                    items = excellenceMarks,
                    selectedItems = selectedExcellences,
                    onSelectionChange = { selectedExcellences = it },
                    showDescription = true,
                    description = stringResource(Res.string.excellence_description),
                )

                FormSection(
                    title = "Instagram",
                    placeholder = "Gdzie dzielą się pięknymi chwilami?",
                    value = instagram,
                    onValueChange = { instagram = it }
                )

                FormSection(
                    title = "Facebook",
                    placeholder = "Gdzie budują swoją społeczność?",
                    value = facebook,
                    onValueChange = { facebook = it }
                )

                FormSection(
                    title = "Strona internetowa",
                    placeholder = "Gdzie w sieci możemy poczytać o nich więcej?",
                    value = website,
                    onValueChange = { website = it }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                ) {

                    FormButton(
                        text = "Wyczyść",
                        modifier = Modifier.weight(1f),
                        onClick = {                     // Clear the form
                            name = ""
                            description = ""
                            address = ""
                            instagram = ""
                            facebook = ""
                            website = ""
                            selectedCategories = emptySet()
                            selectedProperties = emptySet()
                            selectedExcellences = emptySet()
                        },
                        isPrimary = false
                    )

                    FormButton(
                        text = "Wyślij",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            println("Kliknięto Wyślij")     // TODO: zip the data to a json for API??
                        },
                        isPrimary = true
                    )

                }

            }

        }

    }

}