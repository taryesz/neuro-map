package pl.edu.ug.neuromapa.screens.add

import androidx.compose.foundation.ScrollState
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
import pl.edu.ug.neuromapa.components.form.FormButton
import pl.edu.ug.neuromapa.components.form.FormSection
import pl.edu.ug.neuromapa.components.form.FormSelection
import pl.edu.ug.neuromapa.screens.add.settings.widePadding
import pl.edu.ug.neuromapa.screens.add.settings.mediumSpacing
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
import pl.edu.ug.neuromapa.data.PlaceViewModel
import pl.edu.ug.neuromapa.platform.SystemAlertDialog
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import pl.edu.ug.neuromapa.BuildConfig

@Composable
fun AddScreen(
    userProfileImage: DrawableResource,
    onProfileClick: () -> Unit,
    profilePhotoUrl: String? = null,
    scrollState: ScrollState = rememberScrollState(),
    placeViewModel: PlaceViewModel,
    currentName: String,
    currentEmail: String
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
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf<String?>(null) }

    fun String.toWpSlug(): String {
        val polishChars = mapOf(
            'ą' to 'a', 'ć' to 'c', 'ę' to 'e', 'ł' to 'l', 'ń' to 'n',
            'ó' to 'o', 'ś' to 's', 'ź' to 'z', 'ż' to 'z'
        )
        val slug = this.lowercase()
            .map { polishChars[it] ?: it }
            .joinToString("")
            .replace(" ", "_")
            .filter { it.isLetterOrDigit() || it == '_' }
        return when (slug) {
            "cisza" -> "ciche"
            "brak_intensywnych_zapachow" -> "brak_zapachow"
            "jasna_informacja" -> "dostepnosc_informacyjna"
            else -> slug
        }
    }

    // Wrapper of the whole screen
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->

        // One more wrapper...
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },    // This allows tracking the clicks
                    indication = null                                               // This makes the clicks non-visible
                ) {
                    focusManager.clearFocus()   // If the user clicked somewhere in the screen but not a form field,
                    // the keyboard hides ("focus is lost")
                }
        ) {

            // Header (turquoise panel at the very top)
            Header(
                title = stringResource(Res.string.add_screen_header_title),
                userProfileImage = userProfileImage,
                profilePhotoUrl = profilePhotoUrl,
                showProfileTopRightCorner = true,
                roundBottomCorners = true,
                onProfileClick = onProfileClick,
            )

            // Body (main content)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .background(MaterialTheme.colorScheme.background)
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

                                if (placeViewModel.isSubmitting.value) return@bounceClick

                                focusManager.clearFocus()

                                val hasMedal = selectedExcellences.any { it.contains("medal", true) }
                                val hasHeart = selectedExcellences.any { it.contains("serduszko", true) }
                                val wpPropertiesSlugs = selectedProperties.map { it.toWpSlug() }
                                val category = selectedCategories.firstOrNull()?.toWpSlug() ?: ""

                                val wpUsername = BuildConfig.WP_USERNAME
                                val wpAppPassword = BuildConfig.WP_APPLICATION_PASSWORD

                                val credentials = "$wpUsername:$wpAppPassword"

                                @OptIn(ExperimentalEncodingApi::class)
                                val base64Credentials = Base64.encode(credentials.encodeToByteArray())

                                val authHeader = "Basic $base64Credentials"

                                placeViewModel.submitPlace(
                                    name = name,
                                    description = description,
                                    address = address,
                                    category = category,
                                    properties = wpPropertiesSlugs,
                                    hasMedal = hasMedal,
                                    hasHeart = hasHeart,
                                    facebook = facebook,
                                    instagram = instagram,
                                    website = website,
                                    authHeader = authHeader,
                                    submitterName = currentName,
                                    submitterEmail = currentEmail,
                                    onSuccess = {
                                        name = ""; description = ""; address = ""
                                        instagram = ""; facebook = ""; website = ""
                                        selectedCategories = emptySet()
                                        selectedProperties = emptySet()
                                        selectedExcellences = emptySet()

                                        showSuccessDialog = true

                                    },
                                    onError = { errorMessage ->
                                        showErrorDialog = errorMessage
                                    }
                                )
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

        if (showSuccessDialog) {
            SystemAlertDialog(
                title = "Powiodło się!",
                message = "Pomyślnie wysłano formularz zgłoszeniowy.",
                onDismiss = {
                    showSuccessDialog = false
                }
            )
        }

        if (showErrorDialog != null) {
            SystemAlertDialog(
                title = "Błąd",
                message = showErrorDialog!!,
                onDismiss = {
                    showErrorDialog = null
                }
            )
        }

    }

}