package pl.edu.ug.neuromapa.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import org.jetbrains.compose.resources.DrawableResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.screens.home.settings.buttonTextFontSize
import pl.edu.ug.neuromapa.screens.home.settings.cornerRadius
import pl.edu.ug.neuromapa.screens.home.settings.widePadding
import pl.edu.ug.neuromapa.screens.map.settings.mediumPadding
import pl.edu.ug.neuromapa.ui.SurfaceVariant
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.getAppTypography

@Composable
fun HomeHeader(
    title: String,
    motto: String,
    buttonText: String,
    userProfileImage: DrawableResource,
    profilePhotoUrl: String? = null,
    showProfile: Boolean,
    onProfileClick: () -> Unit,
    onButtonClick: () -> Unit
) {

    // Build Home Screen's Header basing on the original Header, adding own content to it
    Header(
        title = title,
        userProfileImage = userProfileImage,
        showProfileTopRightCorner = showProfile,
        profilePhotoUrl = profilePhotoUrl,
        roundBottomCorners = true,
        onProfileClick = onProfileClick,
        modifier = Modifier,
        additionalContent = {

            // Motto panel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = mediumPadding),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = motto,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = getAppTypography().titleSmall,
                )
            }

            // "Need quiet" button wrapper
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = mediumPadding),
                verticalAlignment = Alignment.Top,
            ) {

                // Button panel
                Box(
                    modifier = Modifier
                        .bounceClick(
                            onClick = onButtonClick,
                            hapticType = HapticFeedbackType.LongPress,
                        )
                        .fillMaxWidth()
                        .background(
                            color = SurfaceVariant,     // Light green color
                            shape = RoundedCornerShape(cornerRadius)
                        )
                        .padding(widePadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = buttonText,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = buttonTextFontSize,
                        style = getAppTypography().titleMedium,
                    )
                }
            }

        }
    )
}
