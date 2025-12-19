package pl.edu.ug.neuromapa.screens.add

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
import pl.edu.ug.neuromapa.data.getCategories
import pl.edu.ug.neuromapa.data.getExcellenceMarks
import pl.edu.ug.neuromapa.data.getSensoryProperties
import pl.edu.ug.neuromapa.screens.add.components.FormButton
import pl.edu.ug.neuromapa.screens.add.components.FormSection
import pl.edu.ug.neuromapa.screens.add.components.FormSelection
import pl.edu.ug.neuromapa.screens.add.settings.widePadding
import pl.edu.ug.neuromapa.screens.add.settings.mediumSpacing
import pl.edu.ug.neuromapa.ui.Background

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
                    .padding(widePadding),
                verticalArrangement = Arrangement.spacedBy(mediumSpacing)
            )
            {

                FormSection(
                    title = "Nazwa",
                    placeholder = "Jak nazywa się to wyjątkowe miejsce?",
                    value = name,
                    onValueChange = { name = it }
                )

                FormSelection(
                    title = "Kategoria",
                    items = getCategories(),
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

                FormSelection(
                    title = "Cechy sensoryczne",
                    items = getSensoryProperties(),
                    selectedItems = selectedProperties,
                    onSelectionChange = { selectedProperties = it }
                )

                FormSelection(
                    title = "Wyróżnienia",
                    items = getExcellenceMarks(),
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