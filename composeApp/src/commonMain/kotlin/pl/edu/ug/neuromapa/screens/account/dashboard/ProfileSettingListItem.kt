package pl.edu.ug.neuromapa.screens.account.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pl.edu.ug.neuromapa.screens.account.settings.iconSizeSettingRepresentation
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumPadding
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumSpacing
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding

@Composable
fun ProfileSettingListItem(item: SettingItemData) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .bounceClick { item.onClick() }
                .padding(vertical = globalComponentMediumPadding, horizontal = globalComponentWidePadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Icon representation of the Setting
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(iconSizeSettingRepresentation).align(Alignment.Top)
            )

            Spacer(modifier = Modifier.width(globalComponentMediumSpacing))

            // Title and description of a Setting
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = getAppTypography().titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = item.subtitle,
                    style = getAppTypography().bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // An arrow icon showing that the element is interactive
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )

        }

    }
}