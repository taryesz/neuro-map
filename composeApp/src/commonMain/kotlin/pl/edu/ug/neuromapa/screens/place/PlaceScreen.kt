package pl.edu.ug.neuromapa.screens.place

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.about_place_title
import neuromapa.composeapp.generated.resources.localization_title
import neuromapa.composeapp.generated.resources.navigate_button
import neuromapa.composeapp.generated.resources.useful_link_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.screens.place.components.PlaceHeader
import pl.edu.ug.neuromapa.screens.place.components.SensoryProperty
import pl.edu.ug.neuromapa.screens.place.data.mockPlaceKotkaCafe
import pl.edu.ug.neuromapa.screens.place.models.Place
import pl.edu.ug.neuromapa.screens.place.settings.bodyMediumPadding
import pl.edu.ug.neuromapa.screens.place.settings.bodyNarrowPadding
import pl.edu.ug.neuromapa.screens.place.settings.bodyNarrowSpacing
import pl.edu.ug.neuromapa.screens.place.settings.bodyWidePadding
import pl.edu.ug.neuromapa.screens.place.settings.bodyWideSpacing
import pl.edu.ug.neuromapa.screens.place.settings.linkIconSize
import pl.edu.ug.neuromapa.screens.place.settings.navigationButtonCornerRadius
import pl.edu.ug.neuromapa.screens.place.settings.navigationButtonHorizontalPadding
import pl.edu.ug.neuromapa.screens.place.settings.navigationButtonVerticalPadding
import pl.edu.ug.neuromapa.ui.SurfaceVariant
import pl.edu.ug.neuromapa.ui.getAppTypography

@Composable
fun PlaceScreen(
    place: Place = mockPlaceKotkaCafe,
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // Header
            // TODO: mockPlaceKotkaCafe will need to be changed to an object received from API
            PlaceHeader(
                name = mockPlaceKotkaCafe.name,
                image = mockPlaceKotkaCafe.photo,
                imageDescription = mockPlaceKotkaCafe.photoDescription,
                categoryIcon = mockPlaceKotkaCafe.categoryIcon,
                categoryIconDescription = mockPlaceKotkaCafe.categoryIconDescription,
            )

            // Body (Content)
            Column(
                modifier = Modifier.fillMaxSize().padding(bodyWidePadding),
                verticalArrangement = Arrangement.spacedBy(bodyWideSpacing)
            ) {

                // Sensory Properties horizontal "Carousel"
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(bodyNarrowSpacing)
                ) {
                    items(place.properties) { property ->
                        SensoryProperty(
                            icon = property.icon,
                            iconContentDescription = "Ikona cechy sensorycznej '" + property.name + "'.",
                            name = property.name
                        )
                    }
                }

                // "Opis" section
                Column(
                    modifier = Modifier.fillMaxWidth()
                )
                {

                    // Title
                    Text(
                        text = stringResource(Res.string.about_place_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )

                    // Description
                    Text(
                        modifier = Modifier.padding(top = bodyMediumPadding),
                        text = place.description,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().bodySmall,
                    )

                }

                // "Lokalizacja" section
                Column(
                    modifier = Modifier.fillMaxWidth()
                )
                {

                    // Title
                    Text(
                        text = stringResource(Res.string.localization_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )

                    // Description
                    Text(
                        modifier = Modifier.padding(top = bodyMediumPadding),
                        text = place.address,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().bodySmall,
                    )

                    // Navigation button
                    Column(
                        modifier = Modifier
                            .padding(top = bodyMediumPadding)
                            .clickable { println("Kliknięto przycisk Nawiguj") }   // TODO: open Google Maps and route the user to this place from the user's actual position
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(navigationButtonCornerRadius))
                            .background(SurfaceVariant)
                            .padding(
                                horizontal = navigationButtonHorizontalPadding,
                                vertical = navigationButtonVerticalPadding
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    )
                    {
                        Text(
                            text = stringResource(Res.string.navigate_button),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = getAppTypography().titleSmall,
                        )
                    }

                }

                // "Przydatne linki" section
                Column {
                    Text(
                        text = stringResource(Res.string.useful_link_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium,
                        modifier = Modifier.padding(bottom = bodyMediumPadding)
                    )

                    // Show each relevant link
                    place.links.forEach { link ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = bodyNarrowPadding),
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {

                            // Icon shown depends on the link type
                            Icon(
                                painter = painterResource(link.type.icon),
                                contentDescription = link.type.iconDescription,
                                modifier = Modifier.size(linkIconSize),
                                tint = MaterialTheme.colorScheme.onBackground
                            )

                            Spacer(modifier = Modifier.width(bodyNarrowSpacing))

                            // Needed to open link in default browser of the system
                            val uriHandler = LocalUriHandler.current

                            // Link text
                            Text(
                                modifier = Modifier.clickable {
                                    uriHandler.openUri(link.url)
                                },
                                text = link.label,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = getAppTypography().bodySmall,
                            )

                        }
                    }
                }

            }

        }
    }
}