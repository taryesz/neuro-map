package pl.edu.ug.neuromapa.screens.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import pl.edu.ug.neuromapa.ui.getAppTypography
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.map_screen_filter_overlay_filter_button_alttext
import neuromapa.composeapp.generated.resources.search_bar_filter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.screens.map.settings.mediumPadding
import pl.edu.ug.neuromapa.screens.map.settings.widePadding
import pl.edu.ug.neuromapa.screens.map.settings.userProfileIconSize
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.settings.globalComponentCornerRadius

@Composable
fun MapHeader(
    searchBarPlaceHolder: String,
    onFilterClick: () -> Unit,
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {

    // Search Bar (Wrapper)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = globalComponentCornerRadius,
                    bottomEnd = globalComponentCornerRadius
                )
            )
            .background(MaterialTheme.colorScheme.background)
            .padding(
                vertical = mediumPadding,
                horizontal = widePadding
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // Text field for user input
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        )
        {

            if (searchText.isEmpty()) {
                Text(
                    text = searchBarPlaceHolder,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = getAppTypography().bodySmall
                )
            }

            BasicTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                textStyle = getAppTypography().bodySmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

        }

        // Filter button (Wrapper)
        Box(
            modifier = Modifier
                .size(userProfileIconSize)
                .bounceClick(
                    onClick = onFilterClick,
                ),
            contentAlignment = Alignment.Center
        )
        {
            Icon(
                painter =
                    painterResource(Res.drawable.search_bar_filter),
                contentDescription =
                    stringResource(Res.string.map_screen_filter_overlay_filter_button_alttext),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(userProfileIconSize),
            )
        }

    }

}