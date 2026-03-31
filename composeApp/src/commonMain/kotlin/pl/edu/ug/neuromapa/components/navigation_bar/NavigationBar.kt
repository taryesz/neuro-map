package pl.edu.ug.neuromapa.components.navigation_bar

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
import neuromapa.composeapp.generated.resources.logo_neuromap_no_text
import neuromapa.composeapp.generated.resources.*
import pl.edu.ug.neuromapa.enums.Screen
import pl.edu.ug.neuromapa.ui.animations.animatedSelectionBackground
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.settings.globalComponentCornerRadius

@Composable
fun NavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {

    // This function find the according icon for each navigation button type
    @Composable
    fun getIconPainter(screen: Screen): Painter {
        return when (screen) {
            Screen.Home -> painterResource(Res.drawable.navigation_bar_home)
            Screen.Add -> painterResource(Res.drawable.navigation_bar_add)
            Screen.Map -> painterResource(Res.drawable.logo_neuromap_no_text)
            Screen.Favorites -> painterResource(Res.drawable.navigation_bar_favorites)
            Screen.Survey -> painterResource(Res.drawable.navigation_bar_survey)
            else -> painterResource(Res.drawable.navigation_bar_home)
        }
    }

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
                .filter { it.label != "Lokalizacja" && it.label != "Logowanie" }    // Exclude these screens from
                                                                                    // the navigation bar
                .forEach { screen ->

                    // Check if the screen is selected and select an icon accordingly
                    val isSelected = currentScreen == screen
                    val iconPainter = getIconPainter(screen)

                    // Check if the current screen is the Map screen
                    val isMapScreen = screen == Screen.Map

                    // Define sizes of the container and the icon
                    val containerSize =
                        if (isMapScreen) navigationBarMapItemContainerSize
                        else navigationBarItemContainerSize

                    val iconSize =
                        if (isMapScreen) navigationBarMapItemIconSize
                        else navigationBarItemIconSize

                    // This makes the buttons have equal spacing between them (except for with the NeuroMap button:
                    // it's a little bigger and so the second and forth buttons have a little less space from it)
                    val buttonWeight = if (isMapScreen) navigationBarMapItemWeight else navigationBarItemWeight

                    // Button wrapper
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(buttonWeight)
                            .clip(RoundedCornerShape(globalComponentCornerRadius))
                            .bounceClick(onClick = { onScreenSelected(screen) })
                    )
                    {

                        // One more wrapper...
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {

                            // Icon wrapper
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
                                            .matchParentSize()
                                            .clip(RoundedCornerShape(globalComponentCornerRadius))
                                            .animatedSelectionBackground(
                                                isSelected = isSelected,
                                                backgroundColor = Surface,
                                            )
                                    )
                                }

                                Icon(
                                    painter = iconPainter,
                                    contentDescription = screen.label,
                                    modifier = Modifier.size(iconSize),
                                    tint = Primary
                                )

                            }

                            // Icon label
                            // Show the button label for all buttons except for the Map one
                            if (!isMapScreen) {
                                Text(
                                    text = screen.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(top = navigationBarItemLabelSpacing),
                                    color = Primary,
                                )
                            }

                        }

                    }

                }
        }
    }
}
