package pl.edu.ug.neuromapa.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.edu.ug.neuromapa.ui.theme.CornersRadius
import pl.edu.ug.neuromapa.ui.theme.MainPadding
import pl.edu.ug.neuromapa.ui.theme.getAppTypography
import pl.edu.ug.neuromapa.ui.theme.ProfileIcon

@Composable
fun Header(
    title: String,
    showProfile: Boolean = true,
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    // Header itself
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(bottomStart = CornersRadius, bottomEnd = CornersRadius)
            )
            .windowInsetsPadding(WindowInsets.statusBars)
            .wrapContentHeight()
            .padding(horizontal = MainPadding, vertical = MainPadding)
    ) {

        // Header's content
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {

            // Title box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)     // Fill full width, but leave space for the second box (Profile picture)
            ) {

                // Title content
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onPrimary,
                        lineHeight = 40.sp,
                        style = getAppTypography().titleLarge
                    )
                }
            }

            // Profile picture box
            if (showProfile) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ProfileIcon)
                        .clickable { onProfileClick() }
                )
            }
        }
    }
}