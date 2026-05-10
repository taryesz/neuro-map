package pl.edu.ug.neuromapa.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pl.edu.ug.neuromapa.screens.map.settings.userProfileIconSize
import pl.edu.ug.neuromapa.ui.ProfileIcon
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.settings.globalComponentCornerRadius
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding

@Composable
fun Header(
    title: String,
    userProfileImage: DrawableResource? = null,
    profilePhotoUrl: String? = null,
    showProfileTopRightCorner: Boolean = true,
    roundBottomCorners: Boolean = true,
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    additionalContent: @Composable () -> Unit = {}
) {

    val haptic = LocalHapticFeedback.current

    // Header panel
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = if (roundBottomCorners)
                    RoundedCornerShape(bottomStart = globalComponentCornerRadius, bottomEnd = globalComponentCornerRadius)
                else RoundedCornerShape(0.dp)
            )
            .windowInsetsPadding(WindowInsets.statusBars)
            .wrapContentHeight()
            .padding(horizontal = globalComponentWidePadding, vertical = globalComponentWidePadding)
    ) {

        // Greeting & profile picture panels' wrapper
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,   // Space the two panels evenly
            verticalAlignment = Alignment.Top                   // Keep the content of the two panels at the top
                                                                // in case the text is longer and doesn't fit
                                                                // in the row
        ) {

            // Greeting panel : contains the "Witaj, User!" text
            Box(
                // Make the Greeting panel not push the Profile picture panel out and keep it in the Wrapper
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {

                // Greeting text wrapper
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)  // Stick the text to the top of its wrapper
                        .fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = getAppTypography().titleLarge
                    )
                }
            }

            // Profile picture panel : contains user photo. Showed optionally
            if (showProfileTopRightCorner) {
                val baseModifier = Modifier
                    .size(userProfileIconSize)
                    .clip(CircleShape)
                    .background(ProfileIcon)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onProfileClick()
                    }

                when {
                    !profilePhotoUrl.isNullOrBlank() -> {
                        KamelImage(
                            resource = asyncPainterResource(profilePhotoUrl),
                            contentDescription = "Zdjęcie profilowe użytkownika",
                            contentScale = ContentScale.Crop,
                            modifier = baseModifier,
                            onLoading = { Box(modifier = baseModifier) },
                            onFailure = { Box(modifier = baseModifier) }
                        )
                    }
                    userProfileImage != null -> {
                        Image(
                            painter = painterResource(userProfileImage),
                            contentDescription = "Zdjęcie profilowe użytkownika",
                            contentScale = ContentScale.Crop,
                            modifier = baseModifier
                        )
                    }
                    else -> Box(modifier = baseModifier)
                }
            }
        }

        // Add more content depending on what Screen the user is on
        additionalContent()

    }
}
