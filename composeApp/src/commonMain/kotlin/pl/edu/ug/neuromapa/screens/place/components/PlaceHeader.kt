package pl.edu.ug.neuromapa.screens.place.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.add_circle
import neuromapa.composeapp.generated.resources.favorite
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.DrawableResource
import pl.edu.ug.neuromapa.screens.place.settings.cornerRadius
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.screens.place.settings.widePadding
import pl.edu.ug.neuromapa.screens.place.settings.wideSpacing
import pl.edu.ug.neuromapa.screens.place.settings.headerHeight
import pl.edu.ug.neuromapa.screens.place.settings.headerScrimOffset
import pl.edu.ug.neuromapa.screens.place.settings.headerScrimOpacity
import pl.edu.ug.neuromapa.screens.place.settings.headerCategoryIconSize

@Composable
fun PlaceHeader(
    name: String,
    image: DrawableResource,
    imageDescription: String,
    categoryIcon: DrawableResource,
    categoryIconDescription: String,
    isFavorite: Boolean,
    onFavoriteButtonClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .height(headerHeight)
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius))
    )
    {

        // Set the background image
        Image(
            painter = painterResource(image),
            contentDescription = imageDescription,
            contentScale = ContentScale.Crop, // "Object-fit: cover" - fill the whole Header with the image
            modifier = Modifier.fillMaxSize()
        )

        // Scrim for better title readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = headerScrimOpacity)),
                        startY = headerScrimOffset
                    )
                )
        )

        // "Save to favorites" button panel
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)    // "position: absolute" in the top right corner of the box
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = widePadding, end = widePadding)
                .clip(CircleShape)
                .clickable{ onFavoriteButtonClick() },
            contentAlignment = Alignment.Center,
        )
        {
            Image(
                modifier = Modifier.size(40.dp),
                // TODO: change the icons!!!
                painter = painterResource(if (!isFavorite) Res.drawable.favorite else Res.drawable.add_circle),
                contentDescription = if (isFavorite) "Usuń z ulubionych." else "Dodaj do ulubionych.",
            )
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = widePadding, vertical = widePadding),
            verticalArrangement = Arrangement.Bottom    // Stick content to bottom
        ) {

            // Category icon & title wrapper
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(wideSpacing),
                verticalAlignment = Alignment.Top,
            ) {

                // Category icon panel
                Box(
                    modifier = Modifier.clip(CircleShape),
                    contentAlignment = Alignment.Center
                )
                {
                    Image(
                        modifier = Modifier.size(headerCategoryIconSize),
                        painter = painterResource(categoryIcon),
                        contentDescription = categoryIconDescription,
                    )
                }

                // Place name panel
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
                {

                    // Place name wrapper
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .fillMaxWidth()
                    )
                    {
                        Text(
                            text = name,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = getAppTypography().titleLarge
                        )
                    }

                }
            }

        }

    }

}