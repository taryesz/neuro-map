package pl.edu.ug.neuromapa.screens.account.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.profile_screen_admin_panel_subscreen_content
import neuromapa.composeapp.generated.resources.profile_screen_admin_panel_subscreen_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.components.form.FormButton
import pl.edu.ug.neuromapa.components.form.FormSection
import pl.edu.ug.neuromapa.components.form.FormSelection
import pl.edu.ug.neuromapa.data.AdminDraftPlace
import pl.edu.ug.neuromapa.data.AdminDraftPlacesState
import pl.edu.ug.neuromapa.data.MapPoint
import pl.edu.ug.neuromapa.data.PlaceViewModel
import pl.edu.ug.neuromapa.data.getCategories
import pl.edu.ug.neuromapa.data.getExcellenceMarks
import pl.edu.ug.neuromapa.data.getSensoryProperties
import pl.edu.ug.neuromapa.platform.SystemAlertDialog
import pl.edu.ug.neuromapa.screens.favorites.components.FavoritePlaceCard
import pl.edu.ug.neuromapa.screens.home.settings.cornerRadius
import pl.edu.ug.neuromapa.screens.home.settings.widePadding
import pl.edu.ug.neuromapa.screens.home.settings.wideSpacing
import pl.edu.ug.neuromapa.screens.place.settings.mediumPadding
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.settings.globalComponentCornerRadius
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumPadding
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumSpacing
import pl.edu.ug.neuromapa.ui.settings.globalComponentNarrowSpacing

@Composable
fun AdminPanel(
    userProfileImage: DrawableResource,
    profilePhotoUrl: String? = null,
    placeViewModel: PlaceViewModel
)
{

    val draftPlacesState by placeViewModel.adminDraftPlacesState.collectAsState()
    val isPublishingDraft by placeViewModel.isPublishingDraft
    var selectedDraft by remember { mutableStateOf<AdminDraftPlace?>(null) }
    var actionStatusMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    LaunchedEffect(placeViewModel) {
        placeViewModel.loadAdminDraftPlaces()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {

            Header(
                title = stringResource(Res.string.profile_screen_admin_panel_subscreen_title),
                userProfileImage = userProfileImage,
                profilePhotoUrl = profilePhotoUrl,
                showProfileTopRightCorner = true,
                roundBottomCorners = true
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(widePadding),
                verticalArrangement = Arrangement.spacedBy(wideSpacing)
            ) {

                when (val state = draftPlacesState) {
                    is AdminDraftPlacesState.Idle, is AdminDraftPlacesState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is AdminDraftPlacesState.Error -> {
                        SystemAlertDialog(
                            title = "Błąd",
                            message = "Wystąpił błąd: ${state.message}",
                            onDismiss = { }
                        )
                    }

                    is AdminDraftPlacesState.Success -> {

                        if (selectedDraft == null) {

                            Text(
                                text = stringResource(Res.string.profile_screen_admin_panel_subscreen_content) + "${state.drafts.size}",
                                style = getAppTypography().bodySmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            state.drafts.forEach { draft ->

                                val mockMapPoint = remember(draft) {
                                    MapPoint(
                                        id = draft.id.toIntOrNull() ?: 0,
                                        name = draft.title,
                                        category = draft.category,
                                        latitude = 0.0,
                                        longitude = 0.0,
                                        sensoryFeatures = draft.sensoryFeatures,
                                        hasMedal = draft.hasMedal,
                                        hasHeart = draft.hasHeart,
                                        description = draft.description,
                                        address = draft.address,
                                        photoUrl = "",
                                        website = draft.website,
                                        facebook = draft.facebook,
                                        instagram = draft.instagram
                                    )
                                }

                                FavoritePlaceCard(
                                    mapPoint = mockMapPoint,
                                    isFavorite = false,
                                    onFavoriteClick = { },
                                    onDelete = { },
                                    onNavigate = { },
                                    onClick = { selectedDraft = draft },
                                    isSwipeable = false
                                )

                            }

                        } else {
                            EditDraftForm(
                                draft = selectedDraft!!,
                                isPublishing = isPublishingDraft,
                                onSaveClick = { updatedDraft ->
                                    placeViewModel.updateAdminDraftPlace(
                                        draft = updatedDraft,
                                        onSuccess = {
                                            selectedDraft = updatedDraft
                                            actionStatusMessage = "Zmiany zostały pomyślnie zapisane."
                                        },
                                        onError = { message ->
                                            actionStatusMessage = message
                                        }
                                    )
                                },
                                onPublishClick = { updatedDraft ->
                                    placeViewModel.publishAdminDraftPlace(
                                        draft = updatedDraft,
                                        onSuccess = {
                                            selectedDraft = null
                                            actionStatusMessage = "Miejsce zostało pomyślnie opublikowane."
                                        },
                                        onError = { message ->
                                            actionStatusMessage = message
                                        }
                                    )
                                },
                                onBackClick = { selectedDraft = null }
                            )
                        }
                    }
                }
            }
        }

        if (!actionStatusMessage.isNullOrBlank()) {
            SystemAlertDialog(
                title = "Sukces",
                message = actionStatusMessage.orEmpty(),
                onDismiss = { actionStatusMessage = null }
            )
        }
    }
}

@Composable
private fun EditDraftForm(
    draft: AdminDraftPlace,
    isPublishing: Boolean,
    onSaveClick: (AdminDraftPlace) -> Unit,
    onPublishClick: (AdminDraftPlace) -> Unit,
    onBackClick: () -> Unit
)
{

    val focusManager = LocalFocusManager.current

    var name by remember { mutableStateOf(draft.title) }
    var description by remember { mutableStateOf(draft.description) }
    var address by remember { mutableStateOf(draft.address) }
    var instagram by remember { mutableStateOf(draft.instagram) }
    var facebook by remember { mutableStateOf(draft.facebook) }
    var website by remember { mutableStateOf(draft.website) }

    val allExcellences = getExcellenceMarks()
    val allCategories = getCategories()
    val allProperties = getSensoryProperties()

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

    var selectedCategories by remember {
        mutableStateOf<Set<String>>(
            if (draft.category.isNotBlank()) {
                allCategories
                    .filter { it.name.toWpSlug() == draft.category || it.name.contains(draft.category, ignoreCase = true) }
                    .map { it.name }
                    .toSet()
            } else emptySet()
        )
    }

    var selectedProperties by remember {
        mutableStateOf<Set<String>>(
            allProperties
                .filter { prop ->
                    // Zamieniamy nazwę wyświetlaną (UI) na sluga i dopiero wtedy porównujemy z cechą (slugiem) z bazy
                    draft.sensoryFeatures.any { feature -> prop.name.toWpSlug() == feature || prop.name.contains(feature, ignoreCase = true) }
                }
                .map { it.name }
                .toSet()
        )
    }

    var selectedExcellences by remember(draft, allExcellences) {
        mutableStateOf<Set<String>>(
            allExcellences
                .filter { item ->
                    (draft.hasMedal && item.name.contains("medal", ignoreCase = true)) ||
                            (draft.hasHeart && item.name.contains("serduszko", ignoreCase = true))
                }
                .map { it.name }
                .toSet()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        FormSection(
            title = "Nazwa miejsca",
            placeholder = "Wpisz nazwę",
            value = name,
            onValueChange = { name = it }
        )

        FormSelection(
            title = "Kategoria",
            items = getCategories(),
            selectedItems = selectedCategories,
            onSelectionChange = { selectedCategories = it },
            isSingleSelection = true
        )

        FormSection(
            title = "Opis",
            placeholder = "Opis miejsca",
            value = description,
            onValueChange = { description = it }
        )

        FormSection(
            title = "Adres",
            placeholder = "Adres miejsca",
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
            onSelectionChange = { selectedExcellences = it }
        )

        FormSection(
            title = "Instagram",
            placeholder = "Link do profilu na Instagramie",
            value = instagram,
            onValueChange = { instagram = it }
        )

        FormSection(
            title = "Facebook",
            placeholder = "Link do profilu na Facebooku",
            value = facebook,
            onValueChange = { facebook = it }
        )

        FormSection(
            title = "Strona WWW",
            placeholder = "Link do strony",
            value = website,
            onValueChange = { website = it }
        )

        // Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = globalComponentMediumPadding),
            horizontalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing)
        ) {

            // Go back
            Box(
                modifier = Modifier
                    .weight(1f)
                    .bounceClick { onBackClick() }
            ) {
                FormButton(
                    text = "Wróć",
                    isPrimary = false
                )
            }

            // Save
            Box(
                modifier = Modifier
                    .weight(1f)
                    .bounceClick {
                        if (!isPublishing) {
                            focusManager.clearFocus()

                            val updatedDraft = draft.copy(
                                title = name,
                                description = description,
                                address = address,
                                category = selectedCategories.firstOrNull() ?: "",
                                sensoryFeatures = selectedProperties.toList(),
                                hasMedal = selectedExcellences.any { it.contains("medal", ignoreCase = true) },
                                hasHeart = selectedExcellences.any { it.contains("serduszko", ignoreCase = true) },
                                instagram = instagram,
                                facebook = facebook,
                                website = website
                            )
                            onSaveClick(updatedDraft)
                        }
                    }
            ) {
                FormButton(
                    text = "Zapisz",
                    isPrimary = false
                )
            }

        }

        // Publish
        Column(
            modifier = Modifier
                .padding(top = mediumPadding)
                .bounceClick(
                    onClick = {
                        if (!isPublishing) {
                            focusManager.clearFocus()

                            val updatedDraft = draft.copy(
                                title = name,
                                description = description,
                                address = address,
                                category = selectedCategories.firstOrNull() ?: "",
                                sensoryFeatures = selectedProperties.toList(),
                                hasMedal = selectedExcellences.any { it.contains("medal", ignoreCase = true) },
                                hasHeart = selectedExcellences.any { it.contains("serduszko", ignoreCase = true) },
                                instagram = instagram,
                                facebook = facebook,
                                website = website
                            )
                            onPublishClick(updatedDraft)
                        }
                    },
                    hapticType = HapticFeedbackType.LongPress
                )
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadius))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = pl.edu.ug.neuromapa.screens.place.settings.widePadding, vertical = mediumPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Opublikuj miejsce",
                color = MaterialTheme.colorScheme.onPrimary,
                style = getAppTypography().bodyLarge,
            )
        }

    }
}