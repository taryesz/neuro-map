package pl.edu.ug.neuromapa.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import pl.edu.ug.neuromapa.ui.Background
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding
import pl.edu.ug.neuromapa.ui.Primary
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumPadding
import pl.edu.ug.neuromapa.ui.Surface
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.neuromap_logo_vertical_dark_no_text
import neuromapa.composeapp.generated.resources.*
import pl.edu.ug.neuromapa.enums.Screen
import pl.edu.ug.neuromapa.ui.settings.globalComponentCornerRadius
import pl.edu.ug.neuromapa.ui.settings.navigationBarLabelSpacing

@Composable
fun NavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(
                topStart=globalComponentCornerRadius,
                topEnd=globalComponentCornerRadius)
            ),
        color = Background
    ) {
        NavigationBar(
            containerColor = Transparent,
            modifier = Modifier
                .padding(
                    top = globalComponentMediumPadding,
                    start = globalComponentWidePadding,
                    end = globalComponentWidePadding
                ),
            tonalElevation = 0.dp,
        ) {

            Screen.entries
                .filter { it.label != "Lokalizacja" }
                .forEach { screen ->

                    // Check if the the screen is selected
                    val isSelected = currentScreen == screen
                    val iconPainter = getIconPainter(screen)

                    // Check if the current screen is the Map screen
                    val isMapScreen = screen == Screen.Map

                    // Navigation Button
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onScreenSelected(screen) },
                        icon = {

                            // Define sizes of the container and the icon
                            val containerSize = if (isMapScreen) 70.dp else 50.dp
                            val iconSize = if (isMapScreen) 50.dp else 40.dp

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {

                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.size(containerSize)
                                        .requiredSize(containerSize)    // Make sure the size is the one we want
                                        .aspectRatio(1f)         // Lock the ratio to be a square
                                ) {

                                    // If a button is clicked, add background, round corners
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(containerSize)
                                                .clip(RoundedCornerShape(15.dp))
                                                .background(Surface)
                                        )
                                    }

                                    Icon(
                                        painter = iconPainter,
                                        contentDescription = screen.label,
                                        modifier = Modifier.size(iconSize)
                                    )

                                }

                                // Show the button label for all buttons except for the Map one
                                if (!isMapScreen) {
                                    Text(
                                        text = screen.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(top = navigationBarLabelSpacing)
                                    )
                                }
                            }

                        },

                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = Primary,
                            unselectedTextColor = Primary,
                            selectedIconColor = Primary,
                            selectedTextColor = Primary,
                            indicatorColor = Color.Transparent
                        )

                    )
                }
        }
    }
}

@Composable
fun getIconPainter(screen: Screen): Painter {
    return when (screen) {
        Screen.Home -> painterResource(Res.drawable.home)
        Screen.Add -> painterResource(Res.drawable.add_circle)
        Screen.Map -> painterResource(Res.drawable.neuromap_logo_vertical_dark_no_text)
        Screen.Favorites -> painterResource(Res.drawable.favorite)
        Screen.Survey -> painterResource(Res.drawable.comment)
        else -> painterResource(Res.drawable.home)
    }
}
