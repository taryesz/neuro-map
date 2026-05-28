package pl.edu.ug.neuromapa.screens.account.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.components.form.FormButton
import pl.edu.ug.neuromapa.components.form.FormSection
import pl.edu.ug.neuromapa.data.AdminDraftPlace
import pl.edu.ug.neuromapa.data.AdminDraftPlacesState
import pl.edu.ug.neuromapa.data.AdminPlaceParameter
import pl.edu.ug.neuromapa.data.PlaceViewModel
import pl.edu.ug.neuromapa.platform.SystemAlertDialog
import pl.edu.ug.neuromapa.screens.place.components.PlaceFeature
import pl.edu.ug.neuromapa.screens.place.helpers.getCategoryIconHelper
import pl.edu.ug.neuromapa.screens.place.helpers.getFeatureIconHelper
import pl.edu.ug.neuromapa.screens.place.helpers.getFeatureNameHelper
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumPadding
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumSpacing
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding

@Composable
fun AdminPanel(placeViewModel: PlaceViewModel) {

    val draftPlacesState by placeViewModel.adminDraftPlacesState.collectAsState()
    val isPublishingDraft by placeViewModel.isPublishingDraft
//    val isRejectingDraft by placeViewModel.isRejectingDraft
    var selectedDraft by remember { mutableStateOf<AdminDraftPlace?>(null) }
    var draftPendingPublish by remember { mutableStateOf<AdminDraftPlace?>(null) }
//    var draftPendingReject by remember { mutableStateOf<AdminDraftPlace?>(null) }
    var actionStatusMessage by remember { mutableStateOf<String?>(null) }

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
        ) {

            Header(
                title = "Panel admina",
                showProfileTopRightCorner = false,
                roundBottomCorners = true
            )

            when (val state = draftPlacesState) {
                AdminDraftPlacesState.Idle,
                AdminDraftPlacesState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is AdminDraftPlacesState.Error -> {
                    AdminMessage(message = state.message, isError = true)
                }

                is AdminDraftPlacesState.Success -> {
                    val currentDraft = selectedDraft
                    if (currentDraft == null) {
                        DraftPlacesList(
                            drafts = state.drafts,
                            onDraftSelected = { draft -> selectedDraft = draft }
                        )
                    } else {
                        DraftPlaceDetails(
                            draft = currentDraft,
                            isPublishing = isPublishingDraft,
                            onBack = { selectedDraft = null },
                            onPublishClick = { draftPendingPublish = currentDraft }
//                            onRejectClick = { draftPendingReject = currentDraft }
                        )
                    }
                }
            }
        }

        val confirmationDraft = draftPendingPublish
        if (confirmationDraft != null) {
            PublishConfirmationDialog(
                draft = confirmationDraft,
                isPublishing = isPublishingDraft,
                onConfirm = {
                    draftPendingPublish = null
                    placeViewModel.publishAdminDraftPlace(
                        draft = confirmationDraft,
                        onSuccess = {
                            selectedDraft = null
                            actionStatusMessage = "Miejsce zostało opublikowane."
                        },
                        onError = { message ->
                            actionStatusMessage = message
                        }
                    )
                },
                onDismiss = {
                    if (!isPublishingDraft) {
                        draftPendingPublish = null
                    }
                }
            )
        }

//        val rejectionDraft = draftPendingReject
//        if (rejectionDraft != null) {
//            RejectConfirmationDialog(
//                draft = rejectionDraft,
//                isRejecting = isRejectingDraft,
//                onConfirm = {
//                    draftPendingReject = null
//                    placeViewModel.rejectAdminDraftPlace(
//                        draft = rejectionDraft,
//                        onSuccess = {
//                            selectedDraft = null
//                            actionStatusMessage = "Szkic został odrzucony i usunięty."
//                        },
//                        onError = { message ->
//                            actionStatusMessage = message
//                        }
//                    )
//                },
//                onDismiss = {
//                    if (!isRejectingDraft) {
//                        draftPendingReject = null
//                    }
//                }
//            )
//        }

        if (!actionStatusMessage.isNullOrBlank()) {
            SystemAlertDialog(
                title = "Panel admina",
                message = actionStatusMessage.orEmpty(),
                onDismiss = { actionStatusMessage = null }
            )
        }
    }
}

@Composable
private fun AdminMessage(message: String, isError: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(globalComponentWidePadding),
        verticalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing)
    ) {
        FormSection(
            title = "Szkice miejsc",
            customContent = {
                Text(
                    text = message,
                    style = getAppTypography().bodySmall,
                    color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
                )
            }
        )
    }
}

@Composable
private fun DraftPlacesList(
    drafts: List<AdminDraftPlace>,
    onDraftSelected: (AdminDraftPlace) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(globalComponentWidePadding),
        verticalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing)
    ) {
        item {
            FormSection(
                title = "Szkice miejsc",
                customContent = {
                    Text(
                        text = "Znaleziono szkicow: ${drafts.size}",
                        style = getAppTypography().bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        }

        if (drafts.isEmpty()) {
            item {
                Text(
                    text = "Brak szkicow do wyswietlenia.",
                    style = getAppTypography().bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        itemsIndexed(drafts) { index, draft ->
            DraftPlaceCard(
                index = index,
                draft = draft,
                onClick = { onDraftSelected(draft) }
            )
        }
    }
}

@Composable
private fun DraftPlaceCard(
    index: Int,
    draft: AdminDraftPlace,
    onClick: () -> Unit
) {
    val features = buildList {
        addAll(draft.sensoryFeatures)
        if (draft.hasMedal) add("hasMedal")
        if (draft.hasHeart) add("hasHeart")
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(isAnimated = false) { onClick() },
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            DraftPlaceCardHeader(index = index, draft = draft)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(globalComponentWidePadding),
                verticalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing)
            ) {
                if (features.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing)
                    ) {
                        items(features) { feature ->
                            PlaceFeature(
                                icon = getFeatureIconHelper(feature),
                                iconContentDescription = getFeatureNameHelper(feature),
                                name = getFeatureNameHelper(feature)
                            )
                        }
                    }
                }

                DraftInfoSection(
                    title = "Opis",
                    value = draft.description.ifBlank { "Brak opisu" }
                )

                DraftInfoSection(
                    title = "Adres",
                    value = draft.address.ifBlank { "Brak adresu" }
                )

                DraftLinksSection(draft = draft)
            }
        }
    }
}

@Composable
private fun DraftPlaceDetails(
    draft: AdminDraftPlace,
    isPublishing: Boolean,
    onBack: () -> Unit,
    onPublishClick: () -> Unit,
//    onRejectClick: () -> Unit
) {
    val features = buildList {
        addAll(draft.sensoryFeatures)
        if (draft.hasMedal) add("hasMedal")
        if (draft.hasHeart) add("hasHeart")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(globalComponentWidePadding),
        verticalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing)
    ) {
        item {
            DraftDetailsActions(
                isPublishing = isPublishing,
                onBack = onBack,
                onPublishClick = onPublishClick
//                onRejectClick = onRejectClick
            )
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DraftPlaceCardHeader(index = 0, draft = draft, showIndex = false)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(globalComponentWidePadding),
                        verticalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing)
                    ) {
                        if (features.isNotEmpty()) {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing)
                            ) {
                                items(features) { feature ->
                                    PlaceFeature(
                                        icon = getFeatureIconHelper(feature),
                                        iconContentDescription = getFeatureNameHelper(feature),
                                        name = getFeatureNameHelper(feature)
                                    )
                                }
                            }
                        }

                        DraftInfoSection(
                            title = "Opis",
                            value = draft.description.ifBlank { "Brak opisu" }
                        )

                        DraftInfoSection(
                            title = "Adres",
                            value = draft.address.ifBlank { "Brak adresu" }
                        )

                        DraftLinksSection(draft = draft)

                        DraftParametersSection(parameters = draft.parameters)
                    }
                }
            }
        }

        item {
            PublishDraftButton(
                isPublishing = isPublishing,
                onPublishClick = onPublishClick
//                onRejectClick = onRejectClick
            )
        }
    }
}

@Composable
private fun DraftDetailsActions(
    isPublishing: Boolean,
    onBack: () -> Unit,
    onPublishClick: () -> Unit,
//    onRejectClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing)
    ) {
        Box(
            modifier = Modifier.bounceClick { onBack() }
        ) {
            FormButton(
                text = "Wroc do listy",
                isPrimary = false
            )
        }

        PublishDraftButton(
            isPublishing = isPublishing,
            onPublishClick = onPublishClick
        )
//        DraftDecisionButtons(
//            isPublishing = isPublishing,
//            isRejecting = isRejecting,
//            onPublishClick = onPublishClick,
//            onRejectClick = onRejectClick
//        )
    }
}

//@Composable
//private fun DraftDecisionButtons(
//    isPublishing: Boolean,
//    isRejecting: Boolean,
//    onPublishClick: () -> Unit,
//    onRejectClick: () -> Unit
//) {
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing)
//    ) {
//        PublishDraftButton(
//            isPublishing = isPublishing,
//            isRejecting = isRejecting,
//            onPublishClick = onPublishClick
//        )
//
//        RejectDraftButton(
//            isPublishing = isPublishing,
//            isRejecting = isRejecting,
//            onRejectClick = onRejectClick
//        )
//    }
//}

@Composable
private fun PublishDraftButton(
    isPublishing: Boolean,
    onPublishClick: () -> Unit
) {
    Box(
        modifier = Modifier.bounceClick {
            if (!isPublishing) {
                onPublishClick()
            }
        }
    ) {
        FormButton(
            text = if (isPublishing) "Publikowanie..." else "Opublikuj miejsce",
            isPrimary = true,
            containerColor = if (isPublishing) MaterialTheme.colorScheme.surfaceBright else MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

//@Composable
//private fun RejectDraftButton(
//    isPublishing: Boolean,
//    isRejecting: Boolean,
//    onRejectClick: () -> Unit
//) {
//    Box(
//        modifier = Modifier.bounceClick {
//            if (!isPublishing && !isRejecting) {
//                onRejectClick()
//            }
//        }
//    ) {
//        FormButton(
//            text = if (isRejecting) "Odrzucanie..." else "Odrzuć szkic",
//            isPrimary = true,
//            containerColor = if (isRejecting) MaterialTheme.colorScheme.surfaceBright else MaterialTheme.colorScheme.surfaceTint
//        )
//    }
//}

@Composable
private fun PublishConfirmationDialog(
    draft: AdminDraftPlace,
    isPublishing: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Opublikować miejsce?")
        },
        text = {
            Text(
                text = "Czy na pewno chcesz dodać \"${draft.title}\" ze szkiców do opublikowanych?"
            )
        },
        confirmButton = {
            TextButton(
                enabled = !isPublishing,
                onClick = onConfirm
            ) {
                Text(text = if (isPublishing) "Publikowanie..." else "Opublikuj")
            }
        },
        dismissButton = {
            TextButton(
                enabled = !isPublishing,
                onClick = onDismiss
            ) {
                Text(text = "Anuluj")
            }
        }
    )
}

//@Composable
//private fun RejectConfirmationDialog(
//    draft: AdminDraftPlace,
//    isRejecting: Boolean,
//    onConfirm: () -> Unit,
//    onDismiss: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = {
//            Text(text = "Odrzucić szkic?")
//        },
//        text = {
//            Text(
//                text = "Czy na pewno chcesz odrzucić i usunąć \"${draft.title}\"? Tej operacji nie da się cofnąć."
//            )
//        },
//        confirmButton = {
//            TextButton(
//                enabled = !isRejecting,
//                onClick = onConfirm
//            ) {
//                Text(text = if (isRejecting) "Odrzucanie..." else "Odrzuć")
//            }
//        },
//        dismissButton = {
//            TextButton(
//                enabled = !isRejecting,
//                onClick = onDismiss
//            ) {
//                Text(text = "Anuluj")
//            }
//        }
//    )
//}

@Composable
private fun DraftPlaceCardHeader(
    index: Int,
    draft: AdminDraftPlace,
    showIndex: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(globalComponentWidePadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(getCategoryIconHelper(draft.category)),
                    contentDescription = draft.category.ifBlank { "Kategoria" },
                    modifier = Modifier.size(42.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = if (showIndex) "${index + 1}. ${draft.title}" else draft.title,
                    style = getAppTypography().titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = draft.category.ifBlank { "Brak kategorii" },
                    style = getAppTypography().bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row {
                    DraftStatusPill(text = "ID ${draft.id}")
                    Spacer(modifier = Modifier.width(8.dp))
                    DraftStatusPill(text = draft.status)
                }
            }
        }
    }
}

@Composable
private fun DraftStatusPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.16f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            style = getAppTypography().bodySmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun DraftInfoSection(title: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = getAppTypography().titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        SelectionContainer {
            Text(
                text = value,
                style = getAppTypography().bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun DraftLinksSection(draft: AdminDraftPlace) {
    val links = listOf(
        "WWW" to draft.website,
        "Facebook" to draft.facebook,
        "Instagram" to draft.instagram
    ).filter { (_, value) -> value.isNotBlank() }

    if (links.isEmpty()) return

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Linki",
            style = getAppTypography().titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        links.forEach { (label, value) ->
            ParameterRow(
                parameter = AdminPlaceParameter(label, value),
                compact = true
            )
        }
    }
}

@Composable
private fun DraftParametersSection(parameters: List<AdminPlaceParameter>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Parametry wpisu",
            style = getAppTypography().titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        parameters.forEachIndexed { index, parameter ->
            ParameterRow(parameter = parameter)
            if (index != parameters.lastIndex) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)
                )
            }
        }
    }
}

@Composable
private fun ParameterRow(
    parameter: AdminPlaceParameter,
    compact: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (compact) 2.dp else globalComponentMediumPadding / 2)
    ) {
        Text(
            text = parameter.name,
            style = getAppTypography().bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onBackground
        )
        SelectionContainer {
            Text(
                text = parameter.value.ifBlank { "-" },
                style = getAppTypography().bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.82f)
            )
        }
    }
}
