package pl.edu.ug.neuromapa.screens.account.dashboard

import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.favorites_screen_go_to_map_button_text
import neuromapa.composeapp.generated.resources.profile_screen_birthday_field_title
import neuromapa.composeapp.generated.resources.profile_screen_birthday_playholder
import neuromapa.composeapp.generated.resources.profile_screen_log_out_button_text
import neuromapa.composeapp.generated.resources.profile_screen_name_field_title
import neuromapa.composeapp.generated.resources.profile_screen_name_placeholder
import neuromapa.composeapp.generated.resources.profile_screen_pick_profile_picture_button_text
import neuromapa.composeapp.generated.resources.profile_screen_save_changes_button_text
import neuromapa.composeapp.generated.resources.profile_screen_system_alert_dialog_title
import neuromapa.composeapp.generated.resources.profile_screen_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.components.form.FormButton
import pl.edu.ug.neuromapa.components.form.FormSection
import pl.edu.ug.neuromapa.platform.SystemAlertDialog
import pl.edu.ug.neuromapa.screens.home.settings.widePadding
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumPadding

@Composable
fun DashboardScreen(
    userProfileImage: DrawableResource,
    onSignOut: () -> Unit,
    currentName: String,
    onNameChange: (String) -> Unit,
    onSaveName: () -> Unit,
    currentBirthDate: String,
    onBirthDateChange: (String) -> Unit,
    onSaveBirthDate: () -> Unit,
    onPickProfilePhoto: () -> Unit,
    profilePhotoUrl: String?,
    saveStatusMessage: String?,
    onClearStatusMessage: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    scrollState: ScrollState = rememberScrollState()
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            Header(
                title = stringResource(Res.string.profile_screen_title),
                userProfileImage = userProfileImage,
                profilePhotoUrl = profilePhotoUrl,
                showProfileTopRightCorner = true,
                roundBottomCorners = true,
                modifier = Modifier,
            )

            Column(
                modifier = Modifier.padding(widePadding),
                verticalArrangement = Arrangement.spacedBy(globalComponentMediumPadding)
            )
            {

                // Dark mode switch
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Ciemny motyw",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().bodySmall,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(checked = isDarkTheme, onCheckedChange = onThemeChange)
                }

                // Name field
                FormSection(
                    title = stringResource(Res.string.profile_screen_name_field_title),
                    placeholder = stringResource(Res.string.profile_screen_name_placeholder),
                    value = currentName,
                    onValueChange = { onNameChange(it) }
                )

                // Birthday field
                FormSection(
                    title = stringResource(Res.string.profile_screen_birthday_field_title),
                    placeholder = stringResource(Res.string.profile_screen_birthday_playholder),
                    value = currentBirthDate,
                    onValueChange = { onBirthDateChange(it) }
                )

                // PFP picker
                Box(
                    modifier = Modifier.bounceClick {
                        onPickProfilePhoto()
                    }
                ) {
                    FormButton(
                        text = stringResource(Res.string.profile_screen_pick_profile_picture_button_text),
                        isPrimary = false
                    )
                }

                // Show error
                if (!saveStatusMessage.isNullOrBlank()) {
                    SystemAlertDialog(
                        title = stringResource(Res.string.profile_screen_system_alert_dialog_title),
                        message = saveStatusMessage,
                        onDismiss = onClearStatusMessage
                    )
                }

                // Save button
                Box(
                    modifier = Modifier.bounceClick {
                        onSaveName()
                        onSaveBirthDate()
                    }
                ) {
                    FormButton(
                        text = stringResource(Res.string.profile_screen_save_changes_button_text),
                        isPrimary = true
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Log out button
                Box(
                    modifier = Modifier.bounceClick {
                        onSignOut()
                    }
                ) {
                    FormButton(
                        text = stringResource(Res.string.profile_screen_log_out_button_text),
                        isPrimary = true,
                        containerColor = MaterialTheme.colorScheme.error
                    )
                }

            }

        }
    }
}