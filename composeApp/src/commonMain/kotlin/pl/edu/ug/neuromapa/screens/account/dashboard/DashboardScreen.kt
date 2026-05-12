package pl.edu.ug.neuromapa.screens.account.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.profile_screen_account_setting_description
import neuromapa.composeapp.generated.resources.profile_screen_account_setting_title
import neuromapa.composeapp.generated.resources.profile_screen_log_out_button_text
import neuromapa.composeapp.generated.resources.profile_screen_motive_setting_description
import neuromapa.composeapp.generated.resources.profile_screen_motive_setting_title
import neuromapa.composeapp.generated.resources.profile_screen_system_alert_dialog_title
import neuromapa.composeapp.generated.resources.profile_screen_title
import neuromapa.composeapp.generated.resources.profile_screen_user_profile_picture_content_description
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.components.form.FormButton
import pl.edu.ug.neuromapa.platform.SystemAlertDialog
import pl.edu.ug.neuromapa.screens.account.settings.imageSizeProfilePicture
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.settings.globalComponentExtraNarrowSpacing
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumPadding
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumSpacing
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding
import pl.edu.ug.neuromapa.ui.settings.globalComponentWideSpacing

@Composable
fun DashboardScreen(
    userProfileImage: DrawableResource,
    onSignOut: () -> Unit,
    currentName: String,
    profilePhotoUrl: String?,
    saveStatusMessage: String?,
    onClearStatusMessage: () -> Unit,
    onNavigateToProfileSettings: () -> Unit,
    onNavigateToMotiveSettings: () -> Unit,
    scrollState: ScrollState = rememberScrollState(),
    currentEmail: String,
) {

    // Available settings list
    val settingsList = listOf(

        SettingItemData(
            icon = Icons.Outlined.AccountCircle,
            title = stringResource(Res.string.profile_screen_account_setting_title),
            subtitle = stringResource(Res.string.profile_screen_account_setting_description),
            onClick = {
                onNavigateToProfileSettings()
            }
        ),

        SettingItemData(
            icon = Icons.Outlined.Palette,
            title = stringResource(Res.string.profile_screen_motive_setting_title),
            subtitle = stringResource(Res.string.profile_screen_motive_setting_description),
            onClick = {
                onNavigateToMotiveSettings()
            }
        ),

    )

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
                title = stringResource(Res.string.profile_screen_title),
                showProfileTopRightCorner = false,
                roundBottomCorners = true
            )
            {

                Spacer(modifier = Modifier.height(globalComponentWideSpacing))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val pfpModifier = Modifier
                        .size(imageSizeProfilePicture)
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

                    Spacer(modifier = Modifier.width(globalComponentWideSpacing))

                    // Dane uzytkownika (Imię + email)
                    Column {
                        Text(
                            text = if (currentName.isNotBlank()) currentName else "Gość",
                            style = getAppTypography().titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        Spacer(modifier = Modifier.height(globalComponentExtraNarrowSpacing))

                        Text(
                            text = currentEmail,
                            style = getAppTypography().bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            // Main body
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(top = globalComponentMediumPadding)
                ) {

                    // Show all available settings
                    settingsList.forEach { settingItem ->

                        // The Setting
                        ProfileSettingListItem(item = settingItem)

                        // Setting items divider
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = globalComponentMediumSpacing),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                        )

                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Logout button
                    Box(
                        modifier = Modifier
                            .padding(globalComponentWidePadding)
                            .bounceClick {
                                onSignOut()
                            }
                    ) {
                        FormButton(
                            text = stringResource(Res.string.profile_screen_log_out_button_text),
                            isPrimary = true,
                            containerColor = MaterialTheme.colorScheme.surfaceTint,
                        )
                    }

                }
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