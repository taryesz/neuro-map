package pl.edu.ug.neuromapa.screens.place.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pl.edu.ug.neuromapa.screens.place.settings.cornerRadius
import pl.edu.ug.neuromapa.screens.place.settings.mediumPadding
import pl.edu.ug.neuromapa.screens.place.settings.mediumSpacing
import pl.edu.ug.neuromapa.screens.place.settings.sensoryPropertyIconSize

@Composable
fun PlaceFeature(
    icon: DrawableResource,
    iconContentDescription: String,
    name: String,
) {

    // Wrapper
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surface)
            .padding(mediumPadding),
        horizontalArrangement = Arrangement.spacedBy(mediumSpacing),
        verticalAlignment = Alignment.CenterVertically
    )
    {

        // Icon
        Image(
            painter = painterResource(icon),
            contentDescription = iconContentDescription,
            modifier = Modifier.size(sensoryPropertyIconSize),
        )

        // Property name
        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall,
        )

    }

}