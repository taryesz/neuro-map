package pl.edu.ug.neuromapa.screens.account.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.profile_screen_edit_motive_subscreen_title
import neuromapa.composeapp.generated.resources.profile_screen_motive_field_title
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.components.form.FormSection
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumSpacing
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding

@Composable
fun MotiveSettings(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Header(
                title = stringResource(Res.string.profile_screen_edit_motive_subscreen_title),
                showProfileTopRightCorner = false,
                roundBottomCorners = true
            )

            // Main body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(globalComponentWidePadding),
                verticalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing)
            )
            {

                FormSection(
                    title = stringResource(Res.string.profile_screen_motive_field_title),
                    customContent = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isDarkTheme) "Włączony" else "Wyłączony",
                                style = getAppTypography().bodySmall,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = null,
                                modifier = Modifier.bounceClick {
                                    onThemeChange(!isDarkTheme)
                                },
                                colors = SwitchDefaults.colors(

                                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                    checkedTrackColor = MaterialTheme.colorScheme.primary,

                                    uncheckedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                    uncheckedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),

                                    checkedBorderColor = Color.Transparent,
                                    uncheckedBorderColor = Color.Transparent

                                )
                            )
                        }
                    }
                )

            }

        }
    }
}