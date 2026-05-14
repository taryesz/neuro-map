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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumPadding
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.logo_neuromap_no_text
import neuromapa.composeapp.generated.resources.*
import pl.edu.ug.neuromapa.components.navigation_bar.settings.navigationBarItemContainerSize
import pl.edu.ug.neuromapa.components.navigation_bar.settings.navigationBarItemIconSize
import pl.edu.ug.neuromapa.components.navigation_bar.settings.navigationBarItemLabelSpacing
import pl.edu.ug.neuromapa.components.navigation_bar.settings.navigationBarItemWeight
import pl.edu.ug.neuromapa.components.navigation_bar.settings.navigationBarMapItemContainerSize
import pl.edu.ug.neuromapa.components.navigation_bar.settings.navigationBarMapItemIconSize
import pl.edu.ug.neuromapa.components.navigation_bar.settings.navigationBarMapItemWeight
import pl.edu.ug.neuromapa.enums.Screen
import pl.edu.ug.neuromapa.ui.animations.animatedSelectionBackground
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.settings.globalComponentCornerRadius

@Composable
fun NavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {

    @Composable
    fun getIconPainter(screen: Screen): Painter {
        return when (screen) {
            Screen.Home -> painterResource(Res.drawable.navigation_bar_home)
            Screen.Add -> painterResource(Res.drawable.navigation_bar_add)
            Screen.Map -> painterResource(Res.drawable.logo_neuromap_no_text)
            Screen.Favorites -> painterResource(Res.drawable.navigation_bar_favorites)
            Screen.Profile -> painterResource(Res.drawable.navigation_bar_profile)
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
        color = MaterialTheme.colorScheme.background
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
                .filter { it.label != "Lokalizacja" && it.label != "Logowanie" && it.label != "Rejestracja" }
                .forEach { screen ->

                    val isSelected = when (screen) {
                        Screen.Profile -> {
                            currentScreen == Screen.Profile || currentScreen == Screen.SignIn ||
                                    currentScreen == Screen.SignUp
                        }
                        Screen.Map -> {
                            currentScreen == Screen.Map || currentScreen == Screen.Place
                        }
                        else -> {
                            currentScreen == screen
                        }
                    }

                    val iconPainter = getIconPainter(screen)
                    val isMapScreen = screen == Screen.Map

                    val containerSize = if (isMapScreen) navigationBarMapItemContainerSize else navigationBarItemContainerSize
                    val iconSize = if (isMapScreen) navigationBarMapItemIconSize else navigationBarItemIconSize
                    val buttonWeight = if (isMapScreen) navigationBarMapItemWeight else navigationBarItemWeight

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(buttonWeight)
                            .clip(RoundedCornerShape(globalComponentCornerRadius))
                            .bounceClick(
                                onClick = { onScreenSelected(screen) },
                                hapticType = HapticFeedbackType.LongPress
                            )
                    )
                    {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            // Icon wrapper
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(containerSize)
                                    .requiredSize(containerSize)
                                    .aspectRatio(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clip(RoundedCornerShape(globalComponentCornerRadius))
                                        .animatedSelectionBackground(
                                            isSelected = isSelected,
                                            backgroundColor = MaterialTheme.colorScheme.surface,
                                        )
                                )

                                Icon(
                                    painter = iconPainter,
                                    contentDescription = screen.label,
                                    modifier = Modifier.size(iconSize),
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                                )
                            }

                            // Icon label
                            if (!isMapScreen) {
                                Text(
                                    text = screen.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(top = navigationBarItemLabelSpacing),
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                    }
                }
        }
    }
}