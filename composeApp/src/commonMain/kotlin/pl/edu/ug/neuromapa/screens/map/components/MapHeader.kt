package pl.edu.ug.neuromapa.screens.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pl.edu.ug.neuromapa.ui.getAppTypography
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.filter
import org.jetbrains.compose.resources.painterResource
import pl.edu.ug.neuromapa.screens.map.settings.mediumPadding
import pl.edu.ug.neuromapa.screens.map.settings.widePadding
import pl.edu.ug.neuromapa.screens.map.settings.userProfileIconSize
import pl.edu.ug.neuromapa.ui.Background

@Composable
fun MapHeader(
    searchBarPlaceHolder: String,
) {

    var searchText by remember { mutableStateOf("") }

    // Search Bar
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background)
            .padding(
                vertical = mediumPadding,
                horizontal = widePadding
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // Text Field
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
                onValueChange = { searchText = it },
                textStyle = getAppTypography().bodySmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

        }

        // Settings / Filter Button
        Box(
            modifier = Modifier
                .size(userProfileIconSize)
                .clickable {
                    // TODO
                },
            contentAlignment = Alignment.Center
        )
        {
            Icon(
                painter = painterResource(Res.drawable.filter),
                contentDescription = "Filtruj wyniki",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(userProfileIconSize),
            )
        }

    }

}
