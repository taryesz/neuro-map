package pl.edu.ug.neuromapa.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.about_project_description
import neuromapa.composeapp.generated.resources.about_project_title
import neuromapa.composeapp.generated.resources.faru_gummed_pg_ug_logo_blue
import neuromapa.composeapp.generated.resources.key_operations_description
import neuromapa.composeapp.generated.resources.key_operations_title
import neuromapa.composeapp.generated.resources.neuromap_logo_dark
import neuromapa.composeapp.generated.resources.project_goals_description
import neuromapa.composeapp.generated.resources.project_goals_title
import neuromapa.composeapp.generated.resources.team_members_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.screens.home.components.HomeHeader
import pl.edu.ug.neuromapa.screens.home.models.affiliationsData
import pl.edu.ug.neuromapa.screens.home.models.teamMembersData
import pl.edu.ug.neuromapa.screens.home.settings.bodyMediumPadding
import pl.edu.ug.neuromapa.screens.home.settings.bodyNarrowPadding
import pl.edu.ug.neuromapa.screens.home.settings.bodyWidePadding
import pl.edu.ug.neuromapa.screens.home.settings.bodyWideSpacing
import pl.edu.ug.neuromapa.screens.home.settings.bulletPointHorizontalSpacing
import pl.edu.ug.neuromapa.screens.home.settings.superTextFontSize
import pl.edu.ug.neuromapa.screens.home.settings.superTextLineHeight
import pl.edu.ug.neuromapa.screens.home.settings.superTextNumberWidth
import pl.edu.ug.neuromapa.screens.home.settings.superTextOpacity
import pl.edu.ug.neuromapa.ui.getAppTypography

@Composable
fun HomeScreen(
    userFirstName: String,
    userProfileImage: DrawableResource,
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
            HomeHeader(
                title = "Witaj, $userFirstName!",
                motto = "Znajdź miejsca przyjazne Twoim potrzebom.",
                buttonText = "Potrzebuję spokoju",
                userProfileImage = userProfileImage,
                showProfile = true,
                onProfileClick = { println("Profile clicked") }
            )

            // Body (Content)
            Column(
                modifier = Modifier.fillMaxSize().padding(bodyWidePadding),
                verticalArrangement = Arrangement.spacedBy(bodyWideSpacing)
            ) {

                // TODO: Think of a way to avoid repetition?

                // "O projekcie" section
                Column(
                    modifier = Modifier.fillMaxWidth()
                )
                {

                    // Title
                    Text(
                        text = stringResource(Res.string.about_project_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )

                    // Description
                    Text(
                        modifier = Modifier.padding(top = bodyMediumPadding),
                        text = stringResource(Res.string.about_project_description),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().bodySmall,
                    )

                }

                // "Cele projektu" section
                Column(
                    modifier = Modifier.fillMaxWidth()
                )
                {

                    // Title
                    Text(
                        text = stringResource(Res.string.project_goals_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )

                    // Read the description from strings.xml and divide it into separate facts
                    val rawGoals = stringResource(Res.string.project_goals_description)
                    val goalsList = remember(rawGoals) {
                        rawGoals.split("\n").filter { it.isNotBlank() }
                    }

                    // Description
                    Column {
                        goalsList.forEach { goalText ->

                            // Individual fact
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = bodyMediumPadding),
                                verticalAlignment = Alignment.Top
                            ) {

                                // Bullet
                                Text(
                                    text = "•",
                                    style = getAppTypography().bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(end = bulletPointHorizontalSpacing)
                                )

                                // Fact
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = goalText.trim(),
                                    style = getAppTypography().bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                            }

                        }
                    }

                }

                // "Kluczowe działania" section
                Column(
                    modifier = Modifier.fillMaxWidth()
                )
                {

                    // Title
                    Text(
                        text = stringResource(Res.string.key_operations_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )

                    // Read the description and divide it into separate facts
                    val rawGoals = stringResource(Res.string.key_operations_description)
                    val goalsList = remember(rawGoals) {
                        rawGoals.split("\n").filter { it.isNotBlank() }
                    }

                    // Description
                    Column {
                        goalsList.forEach { goalText ->

                            // Individual fact
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = bodyMediumPadding),
                                verticalAlignment = Alignment.Top
                            ) {

                                // Bullet
                                Text(
                                    text = "•",
                                    style = getAppTypography().bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(end = bulletPointHorizontalSpacing)
                                )

                                // Fact
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = goalText.trim(),
                                    style = getAppTypography().bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                            }

                        }
                    }

                }

                // "Uczestnicy projektu" section
                Column(
                    modifier = Modifier.fillMaxWidth()
                )
                {

                    // Title
                    Text(
                        text = stringResource(Res.string.team_members_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )

                    // Description (members & institutions)

                    // Members
                    teamMembersData.forEach { member ->

                        val nameText = stringResource(member.nameRes)

                        // Append super text to a name
                        val styledText = buildAnnotatedString {
                            append("$nameText ")
                            withStyle(
                                style = SpanStyle(
                                    fontSize = superTextFontSize
                                )
                            ) {
                                val refsString = member.affiliationIds.joinToString(",")
                                append("($refsString)")
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = bodyMediumPadding),
                            verticalAlignment = Alignment.Top,
                        ) {

                            // Bullet
                            Text(
                                text = "•",
                                style = getAppTypography().bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(end = bulletPointHorizontalSpacing)
                            )

                            // Name
                            Text(
                                text = styledText,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = getAppTypography().bodySmall,
                                modifier = Modifier.weight(1f)
                            )

                        }
                    }

                    Spacer(modifier = Modifier.height(bodyWideSpacing))

                    // Institutions
                    affiliationsData.forEach { (id, textRes) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = bodyNarrowPadding),
                            verticalAlignment = Alignment.Top
                        ) {

                            // Small number in front of an institution name
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(
                                        baselineShift = BaselineShift.Superscript,
                                        fontSize = superTextFontSize
                                    )) {
                                        append("$id ")
                                    }
                                },
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = superTextOpacity),
                                style = getAppTypography().bodySmall,
                                modifier = Modifier.width(superTextNumberWidth)
                            )

                            // Institution name
                            Text(
                                text = stringResource(textRes),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = superTextOpacity),
                                fontSize = superTextFontSize,
                                lineHeight = superTextLineHeight,
                                style = getAppTypography().bodySmall,
                                modifier = Modifier.weight(1f)
                            )

                        }
                    }

                }

                // Logos section
                Column(
                    modifier = Modifier.fillMaxWidth()
                )
                {

                    val logos = listOf(
                        Res.drawable.neuromap_logo_dark to "Logotyp NeuroMapy.",
                        Res.drawable.faru_gummed_pg_ug_logo_blue to "Logotyp FarU, GumMed, PG oraz UG.",
                    )

                    logos.forEach { (logo, contentDescription) ->
                        Image(
                            painter = painterResource(logo),
                            contentDescription = contentDescription,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }

            }

        }

    }

}