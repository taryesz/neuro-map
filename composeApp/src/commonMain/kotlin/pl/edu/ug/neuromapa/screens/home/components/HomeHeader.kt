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
import androidx.compose.ui.unit.sp
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.ui.theme.CornersRadius
import pl.edu.ug.neuromapa.ui.theme.MainPadding
import pl.edu.ug.neuromapa.ui.theme.SecondaryPadding
import pl.edu.ug.neuromapa.ui.theme.SurfaceVariant
import pl.edu.ug.neuromapa.ui.theme.getAppTypography

@Composable
fun HomeHeader(
    title: String,
    motto: String,
    buttonText: String,
    showProfile: Boolean,
    onProfileClick: () -> Unit
) {

    // Build Home Screen's Header basing on the original Header, adding own content to it
    Header(
        title = title,
        showProfile = showProfile,
        roundBottomCorners = true,
        onProfileClick = onProfileClick,
        modifier = Modifier,
        additionalContent = {

            // Motto panel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = SecondaryPadding, bottom = SecondaryPadding),
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
                    .padding(top = SecondaryPadding),
                verticalAlignment = Alignment.Top,
            ) {

                // Button panel
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = SurfaceVariant,     // Light green color
                            shape = RoundedCornerShape(CornersRadius)
                        )
                        .padding(MainPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = buttonText,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 24.sp,   // Custom font size
                        style = getAppTypography().titleLarge,
                    )
                }
            }

        }
    )
}