package pl.edu.ug.neuromapa.screens.account.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.profile_screen_birthday_field_title
import neuromapa.composeapp.generated.resources.profile_screen_birthday_playholder
import neuromapa.composeapp.generated.resources.profile_screen_edit_profile_subscreen_edit_pfp_button_description
import neuromapa.composeapp.generated.resources.profile_screen_edit_profile_subscreen_title
import neuromapa.composeapp.generated.resources.profile_screen_name_field_title
import neuromapa.composeapp.generated.resources.profile_screen_name_placeholder
import neuromapa.composeapp.generated.resources.profile_screen_save_changes_button_text
import neuromapa.composeapp.generated.resources.profile_screen_system_alert_dialog_title
import neuromapa.composeapp.generated.resources.profile_screen_user_profile_picture_content_description
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.components.form.FormButton
import pl.edu.ug.neuromapa.components.form.FormSection
import pl.edu.ug.neuromapa.platform.SystemAlertDialog
import pl.edu.ug.neuromapa.screens.account.settings.iconSizeSettingRepresentation
import pl.edu.ug.neuromapa.screens.account.settings.iconSmallSizeSettingRepresentation
import pl.edu.ug.neuromapa.screens.account.settings.imageBigSizeProfilePicture
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumSpacing
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding
import pl.edu.ug.neuromapa.ui.settings.globalComponentWideSpacing

@Composable
fun ProfileSettings(
    userProfileImage: DrawableResource,
    profilePhotoUrl: String?,
    currentName: String,
    onNameChange: (String) -> Unit,
    currentBirthDate: String,
    onBirthDateChange: (String) -> Unit,
    onSaveProfile: () -> Unit,
    onPickProfilePhoto: () -> Unit,
    saveStatusMessage: String?,
    onClearStatusMessage: () -> Unit,
) {

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
                title = stringResource(Res.string.profile_screen_edit_profile_subscreen_title),
                showProfileTopRightCorner = false,
                roundBottomCorners = true
            )
            {

                Spacer(modifier = Modifier.height(globalComponentWideSpacing))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    // PFP + edit button
                    Box(contentAlignment = Alignment.BottomEnd) {
                        val pfpModifier = Modifier
                            .size(imageBigSizeProfilePicture)
                            .clip(CircleShape)
                            .background(Color.White)

                        if (!profilePhotoUrl.isNullOrBlank()) {
                            io.kamel.image.KamelImage(
                                resource = io.kamel.image.asyncPainterResource(profilePhotoUrl),
                                contentDescription = stringResource(Res.string.profile_screen_user_profile_picture_content_description),
                                contentScale = ContentScale.Crop,
                                modifier = pfpModifier,
                                onLoading = { Box(modifier = pfpModifier) },
                                onFailure = { Box(modifier = pfpModifier) }
                            )
                        } else {
                            Image(
                                painter = painterResource(userProfileImage),
                                contentDescription = stringResource(Res.string.profile_screen_user_profile_picture_content_description),
                                contentScale = ContentScale.Crop,
                                modifier = pfpModifier
                            )
                        }

                        // Edit PFP button (a pencil icon)
                        Box(
                            modifier = Modifier
                                .size(iconSizeSettingRepresentation)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onPrimary)
                                .bounceClick { onPickProfilePhoto() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = stringResource(Res.string.profile_screen_edit_profile_subscreen_edit_pfp_button_description),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(iconSmallSizeSettingRepresentation)
                            )
                        }
                    }
                }
            }

            // Main body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(globalComponentWidePadding),
                verticalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing)
            )
            {

                // Let the user INPUT their name
                FormSection(
                    title = stringResource(Res.string.profile_screen_name_field_title),
                    placeholder = stringResource(Res.string.profile_screen_name_placeholder),
                    value = currentName,
                    onValueChange = onNameChange
                )

                // Let the user INPUT their birthdate
                FormSection(
                    title = stringResource(Res.string.profile_screen_birthday_field_title),
                    placeholder = stringResource(Res.string.profile_screen_birthday_playholder),
                    value = currentBirthDate,
                    onValueChange = onBirthDateChange
                )

                Spacer(modifier = Modifier.weight(1f))

                // Save button
                Box(
                    modifier = Modifier
                        .padding(top = globalComponentWidePadding)
                        .bounceClick {
                            onSaveProfile()
                        }
                ) {
                    FormButton(
                        text = stringResource(Res.string.profile_screen_save_changes_button_text),
                        isPrimary = true,
                        containerColor = MaterialTheme.colorScheme.primary,
                    )
                }

            }

            // Error UI response
            if (!saveStatusMessage.isNullOrBlank()) {
                SystemAlertDialog(
                    title = stringResource(Res.string.profile_screen_system_alert_dialog_title),
                    message = saveStatusMessage,
                    onDismiss = onClearStatusMessage
                )
            }

        }
    }
}