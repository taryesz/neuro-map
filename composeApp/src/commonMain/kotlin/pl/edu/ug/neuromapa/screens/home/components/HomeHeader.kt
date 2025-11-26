package pl.edu.ug.neuromapa.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.screens.home.settings.buttonCornerRadius
import pl.edu.ug.neuromapa.screens.home.settings.buttonTextFontSize
import pl.edu.ug.neuromapa.screens.home.settings.buttonWidePadding
import pl.edu.ug.neuromapa.screens.map.settings.bodyMediumPadding
import pl.edu.ug.neuromapa.ui.SurfaceVariant
import pl.edu.ug.neuromapa.ui.getAppTypography

@Composable
fun HomeHeader(
    title: String,
    motto: String,
    buttonText: String,
    userProfileImage: DrawableResource,
    showProfile: Boolean,
    onProfileClick: () -> Unit
) {

    // Build Home Screen's Header basing on the original Header, adding own content to it
    Header(
        title = title,
        userProfileImage = userProfileImage,
        showProfile = showProfile,
        roundBottomCorners = true,
        onProfileClick = onProfileClick,
        modifier = Modifier,
        additionalContent = {

            // Motto panel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = bodyMediumPadding),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = motto,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = getAppTypography().titleSmall,
                )
            }

            // Button wrapper
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = bodyMediumPadding),
                verticalAlignment = Alignment.Top,
            ) {

                // Button panel
                Box(
                    modifier = Modifier
                        .clickable { println("Kliknięto przycisk Potrzebuję spokoju") } // TODO
                        .fillMaxWidth()
                        .background(
                            color = SurfaceVariant,     // Light green color
                            shape = RoundedCornerShape(buttonCornerRadius)
                        )
                        .padding(buttonWidePadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = buttonText,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = buttonTextFontSize,
                        style = getAppTypography().titleLarge,
                    )
                }
            }

        }
    )
}