package pl.edu.ug.neuromapa.screens.add.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pl.edu.ug.neuromapa.screens.add.settings.categoryItemIconSize
import pl.edu.ug.neuromapa.screens.add.settings.categoryItemVerticalPadding
import pl.edu.ug.neuromapa.screens.add.settings.mediumPadding
import pl.edu.ug.neuromapa.screens.add.settings.cornerRadius

@Composable
fun CategoryItem(
    icon: DrawableResource,
    iconDescription: String,
    name: String,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(cornerRadius))
            .clickable { onClick() }
            .padding(horizontal = mediumPadding),
        verticalArrangement = Arrangement.spacedBy(categoryItemVerticalPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        Image(
            painter = painterResource(icon),
            contentDescription = iconDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(categoryItemIconSize)
                .clip(CircleShape)
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = name,
            style = MaterialTheme.typography.titleSmall.copy(
                hyphens = Hyphens.Auto,         // Automatically add hyphens when a word doesn't fit
                lineBreak = LineBreak.Heading   // The app will rather move the whole word to a new line than
                                                // only one letter
            ),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,   // If the text is too long, cut it with "..."
        )

    }

}