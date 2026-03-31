package pl.edu.ug.neuromapa.screens.place

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.data.MapPoint
import pl.edu.ug.neuromapa.platform.rememberMapNavigator
import pl.edu.ug.neuromapa.screens.home.settings.cornerRadius
import pl.edu.ug.neuromapa.screens.place.components.PlaceHeader
import pl.edu.ug.neuromapa.screens.place.components.PlaceFeature
import pl.edu.ug.neuromapa.screens.place.data.Link
import pl.edu.ug.neuromapa.screens.place.enums.LinkType
import pl.edu.ug.neuromapa.screens.place.settings.*
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.screens.place.helpers.*
import pl.edu.ug.neuromapa.ui.SurfaceVariant
import pl.edu.ug.neuromapa.ui.animations.bounceClick

@Composable
fun PlaceScreen(
    mapPoint: MapPoint,
) {

    val mapNavigator = rememberMapNavigator()

    val allFeatures = remember(mapPoint) {
        val features = mapPoint.sensoryFeatures.toMutableList()
        if (mapPoint.hasMedal) features.add("hasMedal")
        if (mapPoint.hasHeart) features.add("hasHeart")
        features
    }

    // Wrapper of the whole screen
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    )
    {

        // One more wrapper...
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
        {

            // Custom Header (it has a snapshot of a map instead of the turquoise title)
            PlaceHeader(
                name = mapPoint.name,
                categoryIcon = getCategoryIconHelper(mapPoint.category),
                categoryIconDescription = mapPoint.category,
                isFavorite = false,
                onFavoriteButtonClick = { },
                latitude = mapPoint.latitude,
                longitude = mapPoint.longitude,
            )

            // Place description section (Wrapper)
            Column(
                modifier = Modifier.fillMaxSize().padding(widePadding),
                verticalArrangement = Arrangement.spacedBy(wideSpacing)
            )
            {

                if (allFeatures.isNotEmpty()) {

                    // The horizontal scrollable list of all features the places has
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(mediumSpacing)
                    ) {
                        items(allFeatures) { featureName ->
                            PlaceFeature(
                                icon = getFeatureIconHelper(featureName),
                                iconContentDescription = getFeatureNameHelper(featureName),
                                name = getFeatureNameHelper(featureName)
                            )
                        }
                    }
                }

                // The rest of the description of the place is here:

                // "About"
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(Res.string.about_place_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )
                    Text(
                        modifier = Modifier.padding(top = mediumPadding),
                        text = mapPoint.description,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().bodySmall,
                    )
                }

                // "Localization"
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(Res.string.localization_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )
                    Text(
                        modifier = Modifier.padding(top = mediumPadding),
                        text = mapPoint.address,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().bodySmall,
                    )

                    // The "Navigate" button
                    Column(
                        modifier = Modifier
                            .padding(top = mediumPadding)
                            .bounceClick(
                                onClick = {
                                    mapNavigator.navigateTo(mapPoint.latitude, mapPoint.longitude, mapPoint.name)
                                },
                                hapticType = HapticFeedbackType.LongPress
                            )
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(cornerRadius))
                            .background(SurfaceVariant)
                            .padding(horizontal = widePadding, vertical = mediumPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(Res.string.navigate_button),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = getAppTypography().titleSmall,
                        )
                    }
                }

                // All the place-related links
                val links = mutableListOf<Link>()

                if (mapPoint.website.isNotBlank()) {
                    links.add(
                        Link(
                            LinkType.Website,
                            mapPoint.website,
                            stringResource(
                                Res.string.place_screen_place__description_instagram_link_label
                            )
                        )
                    )
                }
                if (mapPoint.facebook.isNotBlank()) {
                    links.add(
                        Link(
                            LinkType.Facebook,
                            mapPoint.facebook,
                            stringResource(
                                Res.string.place_screen_place__description_facebook_link_label
                            )
                        )
                    )
                }
                if (mapPoint.instagram.isNotBlank()) {
                    links.add(
                        Link(
                            LinkType.Instagram,
                            mapPoint.instagram,
                            stringResource(
                                Res.string.place_screen_place__description_website_link_label
                            )
                        )
                    )
                }

                if (links.isNotEmpty()) {

                    // Show one more section with links (if any)
                    Column {
                        Text(
                            text = stringResource(Res.string.useful_link_title),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = getAppTypography().titleMedium,
                            modifier = Modifier.padding(bottom = mediumPadding)
                        )

                        links.forEach { link ->

                            // Link wrapper
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = narrowPadding),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                // An icon describing the link
                                Icon(
                                    painter = painterResource(link.type.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(linkIconSize),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )

                                Spacer(modifier = Modifier.width(mediumSpacing))

                                val uriHandler = LocalUriHandler.current

                                // The link itself
                                Text(
                                    modifier = Modifier.clickable { uriHandler.openUri(link.url) },
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
}