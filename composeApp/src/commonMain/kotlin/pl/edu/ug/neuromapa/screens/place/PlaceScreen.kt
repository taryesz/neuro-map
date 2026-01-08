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
import pl.edu.ug.neuromapa.screens.place.models.Link
import pl.edu.ug.neuromapa.screens.place.models.LinkType
import pl.edu.ug.neuromapa.screens.place.settings.*
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.screens.place.helpers.*

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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
        {

            PlaceHeader(
                name = mapPoint.name,
                image = Res.drawable.no_photo, 
                imageDescription = "Zdjęcie miejsca ${mapPoint.name}",
                categoryIcon = getCategoryIconHelper(mapPoint.category),
                categoryIconDescription = mapPoint.category,
                isFavorite = false,
                onFavoriteButtonClick = { }, 
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(widePadding),
                verticalArrangement = Arrangement.spacedBy(wideSpacing)
            )
            {

                if (allFeatures.isNotEmpty()) {
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

                    Column(
                        modifier = Modifier
                            .padding(top = mediumPadding)
                            .clickable { 
                                mapNavigator.navigateTo(mapPoint.latitude, mapPoint.longitude, mapPoint.name)
                            }
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(cornerRadius))
                            .background(MaterialTheme.colorScheme.primary)
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

                val links = mutableListOf<Link>()
                if (mapPoint.website.isNotBlank()) {
                    links.add(Link(LinkType.Website, mapPoint.website, "Strona internetowa"))
                }
                if (mapPoint.facebook.isNotBlank()) {
                    links.add(Link(LinkType.Facebook, mapPoint.facebook, "Facebook"))
                }
                if (mapPoint.instagram.isNotBlank()) {
                    links.add(Link(LinkType.Instagram, mapPoint.instagram, "Instagram"))
                }
                if (links.isNotEmpty()) {
                    Column {
                        Text(
                            text = stringResource(Res.string.useful_link_title),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = getAppTypography().titleMedium,
                            modifier = Modifier.padding(bottom = mediumPadding)
                        )
                        links.forEach { link ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = narrowPadding),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(link.type.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(linkIconSize),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.width(mediumSpacing))
                                val uriHandler = LocalUriHandler.current
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