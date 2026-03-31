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
import neuromapa.composeapp.generated.resources.add_screen_excellence_description
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.platform.LocalFocusManager
import neuromapa.composeapp.generated.resources.add_screen_form_erase_button
import neuromapa.composeapp.generated.resources.add_screen_form_field_place_address
import neuromapa.composeapp.generated.resources.add_screen_form_field_place_category
import neuromapa.composeapp.generated.resources.add_screen_form_field_place_description
import neuromapa.composeapp.generated.resources.add_screen_form_field_place_excellences
import neuromapa.composeapp.generated.resources.add_screen_form_field_place_facebook
import neuromapa.composeapp.generated.resources.add_screen_form_field_place_instagram
import neuromapa.composeapp.generated.resources.add_screen_form_field_place_name
import neuromapa.composeapp.generated.resources.add_screen_form_field_place_sensory_properties
import neuromapa.composeapp.generated.resources.add_screen_form_field_place_website
import neuromapa.composeapp.generated.resources.add_screen_form_field_placeholder_place_address
import neuromapa.composeapp.generated.resources.add_screen_form_field_placeholder_place_description
import neuromapa.composeapp.generated.resources.add_screen_form_field_placeholder_place_facebook
import neuromapa.composeapp.generated.resources.add_screen_form_field_placeholder_place_instagram
import neuromapa.composeapp.generated.resources.add_screen_form_field_placeholder_place_name
import neuromapa.composeapp.generated.resources.add_screen_form_field_placeholder_place_website
import neuromapa.composeapp.generated.resources.add_screen_form_submit_button
import neuromapa.composeapp.generated.resources.add_screen_header_title
import pl.edu.ug.neuromapa.ui.animations.bounceClick

@Composable
fun AddScreen(
    userProfileImage: DrawableResource,
    onProfileClick: () -> Unit,
) {

    // This is used to hide the keyboard whenever the user clicks somewhere NOT in the form field
    val focusManager = LocalFocusManager.current

    // Each of these variables is a state of a specific form field
    // By default, they are empty, but later can store some user input
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var instagram by remember { mutableStateOf("") }
    var facebook by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }
    var selectedProperties by remember { mutableStateOf(setOf<String>()) }
    var selectedExcellences by remember { mutableStateOf(setOf<String>()) }

    // Wrapper of the whole screen
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) {

        // One more wrapper...
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },    // This allows tracking the clicks
                    indication = null                                               // This makes the clicks non-visible
                ) {
                    focusManager.clearFocus()   // If the user clicked somewhere in the screen but not a form field,
                                                // the keyboard hides ("focus is lost")
                }
                .verticalScroll(rememberScrollState())  // Make the screen scrollable
        ) {

            // Header (turquoise panel at the very top)
            Header(
                title = stringResource(Res.string.add_screen_header_title),
                userProfileImage = userProfileImage,
                showProfile = true,
                roundBottomCorners = true,
                onProfileClick = onProfileClick,
            )

            // Body (main content)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background)
                    .padding(widePadding),
                verticalArrangement = Arrangement.spacedBy(mediumSpacing)
            )
            {

                // Let the user INPUT a place name
                FormSection(
                    title = stringResource(Res.string.add_screen_form_field_place_name),
                    placeholder = stringResource(Res.string.add_screen_form_field_placeholder_place_name),
                    value = name,
                    onValueChange = { name = it }
                )

                // Let the user CHOOSE a place category
                FormSelection(
                    title = stringResource(Res.string.add_screen_form_field_place_category),
                    items = getCategories(),
                    selectedItems = selectedCategories,
                    onSelectionChange = { selectedCategories = it },
                    isSingleSelection = true
                )

                // Let the user INPUT a place description
                FormSection(
                    title = stringResource(Res.string.add_screen_form_field_place_description),
                    placeholder = stringResource(Res.string.add_screen_form_field_placeholder_place_description),
                    value = description,
                    onValueChange = { description = it }
                )

                // Let the user INPUT a place address
                FormSection(
                    title = stringResource(Res.string.add_screen_form_field_place_address),
                    placeholder = stringResource(Res.string.add_screen_form_field_placeholder_place_address),
                    value = address,
                    onValueChange = { address = it }
                )

                // Let the user CHOOSE a place properties
                FormSelection(
                    title = stringResource(Res.string.add_screen_form_field_place_sensory_properties),
                    items = getSensoryProperties(),
                    selectedItems = selectedProperties,
                    onSelectionChange = { selectedProperties = it }
                )

                // Let the user CHOOSE a place excellences
                FormSelection(
                    title = stringResource(Res.string.add_screen_form_field_place_excellences),
                    items = getExcellenceMarks(),
                    selectedItems = selectedExcellences,
                    onSelectionChange = { selectedExcellences = it },
                    showDescription = true,
                    description = stringResource(Res.string.add_screen_excellence_description),
                )

                // Let the user INPUT a place Instagram profile
                FormSection(
                    title = stringResource(Res.string.add_screen_form_field_place_instagram),
                    placeholder = stringResource(Res.string.add_screen_form_field_placeholder_place_instagram),
                    value = instagram,
                    onValueChange = { instagram = it }
                )

                // Let the user INPUT a place Facebook profile
                FormSection(
                    title = stringResource(Res.string.add_screen_form_field_place_facebook),
                    placeholder = stringResource(Res.string.add_screen_form_field_placeholder_place_facebook),
                    value = facebook,
                    onValueChange = { facebook = it }
                )

                // Let the user INPUT a place website
                FormSection(
                    title = stringResource(Res.string.add_screen_form_field_place_website),
                    placeholder = stringResource(Res.string.add_screen_form_field_placeholder_place_website),
                    value = website,
                    onValueChange = { website = it }
                )

                // Form button wrapper at the very bottom of the screen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                ) {

                    // Wrapper for ERASE BUTTON (needed for the animation to work)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .bounceClick {
                                name = ""
                                description = ""
                                address = ""
                                instagram = ""
                                facebook = ""
                                website = ""
                                selectedCategories = emptySet()
                                selectedProperties = emptySet()
                                selectedExcellences = emptySet()
                            }
                    ) {
                        FormButton(
                            text = stringResource(Res.string.add_screen_form_erase_button),
                            isPrimary = false
                        )
                    }

                    // Wrapper for SUBMIT BUTTON (needed for the animation to work)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .bounceClick {
                                // TODO: send the information to the admin panel
                            }
                    ) {
                        FormButton(
                            text = stringResource(Res.string.add_screen_form_submit_button),
                            isPrimary = true
                        )
                    }

                }

            }

        }

    }

}