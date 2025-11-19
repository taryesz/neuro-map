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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.screens.home.components.HomeHeader
import pl.edu.ug.neuromapa.screens.home.data.models.affiliationsData
import pl.edu.ug.neuromapa.screens.home.data.models.teamMembersData
import pl.edu.ug.neuromapa.ui.theme.BulletPointHorizontalSpacing
import pl.edu.ug.neuromapa.ui.theme.MainPadding
import pl.edu.ug.neuromapa.ui.theme.MainSpacing
import pl.edu.ug.neuromapa.ui.theme.SecondaryPadding
import pl.edu.ug.neuromapa.ui.theme.SuperTextFontSize
import pl.edu.ug.neuromapa.ui.theme.SuperTextLineHeight
import pl.edu.ug.neuromapa.ui.theme.SuperTextNumberWidth
import pl.edu.ug.neuromapa.ui.theme.SuperTextOpacity
import pl.edu.ug.neuromapa.ui.theme.TertiaryPadding
import pl.edu.ug.neuromapa.ui.theme.getAppTypography

@Composable
fun HomeScreen() {
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
                title = "Witaj, Ryszard!",
                motto = "Znajdź miejsca przyjazne Twoim potrzebom.",
                buttonText = "Potrzebuję spokoju",
                showProfile = true,
                onProfileClick = { println("Profile clicked") }
            )

            // Body (Content)
            Column(
                modifier = Modifier.fillMaxSize().padding(MainPadding),
                verticalArrangement = Arrangement.spacedBy(MainSpacing)
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
                        modifier = Modifier.padding(top = SecondaryPadding),
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
                    Column() {
                        goalsList.forEach { goalText ->

                            // Individual fact
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = SecondaryPadding),
                                verticalAlignment = Alignment.Top
                            ) {

                                // Bullet
                                Text(
                                    text = "•",
                                    style = getAppTypography().bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(end = BulletPointHorizontalSpacing)
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

                // "Kluczowe dzialania" section
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
                    Column() {
                        goalsList.forEach { goalText ->

                            // Individual fact
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = SecondaryPadding),
                                verticalAlignment = Alignment.Top
                            ) {

                                // Bullet
                                Text(
                                    text = "•",
                                    style = getAppTypography().bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(end = BulletPointHorizontalSpacing)
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

                        // Append supertext to a name
                        val styledText = buildAnnotatedString {
                            append("$nameText ")
                            withStyle(
                                style = SpanStyle(
                                    fontSize = SuperTextFontSize
                                )
                            ) {
                                val refsString = member.affiliationIds.joinToString(",")
                                append("($refsString)")
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = SecondaryPadding),
                            verticalAlignment = Alignment.Top,
                        ) {

                            // Bullet
                            Text(
                                text = "•",
                                style = getAppTypography().bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(end = BulletPointHorizontalSpacing)
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

                    Spacer(modifier = Modifier.height(MainSpacing))

                    // Institutions
                    affiliationsData.forEach { (id, textRes) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = TertiaryPadding),
                            verticalAlignment = Alignment.Top
                        ) {

                            // Small number in front of an institution name
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(
                                        baselineShift = BaselineShift.Superscript,
                                        fontSize = SuperTextFontSize
                                    )) {
                                        append("$id ")
                                    }
                                },
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = SuperTextOpacity),
                                style = getAppTypography().bodySmall,
                                modifier = Modifier.width(SuperTextNumberWidth)
                            )

                            // Institution name
                            Text(
                                text = stringResource(textRes),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = SuperTextOpacity),
                                fontSize = SuperTextFontSize,
                                lineHeight = SuperTextLineHeight,
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
                        Res.drawable.faru_gummed_pg_ug_logo_blue to "Logotyp FarU, GumMedu, PG oraz UG.",
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