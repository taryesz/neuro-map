package pl.edu.ug.neuromapa.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.logo_neuromap_dark
import neuromapa.composeapp.generated.resources.sign_in_apple
import neuromapa.composeapp.generated.resources.sign_in_google_light
import org.jetbrains.compose.resources.painterResource
import pl.edu.ug.neuromapa.components.form.FormButton
import pl.edu.ug.neuromapa.components.form.FormSection
import pl.edu.ug.neuromapa.screens.home.settings.logoHeight
import pl.edu.ug.neuromapa.screens.login.settings.widePadding
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.onBackground

@Composable
fun LoginScreen() {

    val focusManager = LocalFocusManager.current

    // Wrapper of the whole screen
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        // One more wrapper...
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                }
        ) {

            // Body (main content)
            Column(
                modifier = Modifier.fillMaxSize().weight(1f).padding(widePadding),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {

                // Logo at the top of the scren
                Image(
                    painter = painterResource(Res.drawable.logo_neuromap_dark),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(logoHeight)
                        .fillMaxWidth()
                )

                // Sign in form
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(widePadding),
                ) {

                    // Section title ("Sign in")
                    Text(
                        text = "Zaloguj się",
                        style = getAppTypography().titleMedium,
                    )

                    // Let the user INPUT their email
                    FormSection(
                        placeholder = "Email",
                        value = email,
                        onValueChange = { email = it }
                    )

                    // Let the user INPUT their password
                    FormSection(
                        placeholder = "Hasło",
                        value = password,
                        onValueChange = { password = it }
                    )

                    // "Sign in" button
                    Box(
                        modifier = Modifier
                            .bounceClick {
                                // TODO: send the information to Supabase... log in the user
                            }
                    ) {
                        FormButton(
                            text = "Zaloguj się",
                            isPrimary = true
                        )
                    }

                }

                // Alternative sign in methods (Wrapper)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(widePadding),
                ) {

                    // Section title
                    Text(
                        text = "- Lub zaloguj się za pomocą -",
                        color = onBackground,
                        style = getAppTypography().bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Alternative sign in methods (Google + Apple buttons wrapper)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                    ) {

                        // Sign in with Google
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .bounceClick {
                                    // TODO: send the information to Supabase... log in the user WITH GOOGLE
                                }
                        ) {
                            FormButton(
                                icon = Res.drawable.sign_in_google_light,
                                isPrimary = false,
                            )
                        }

                        // Sign in with Apple
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .bounceClick {
                                    // TODO: send the information to Supabase... log in the user WITH APPLE
                                }
                        ) {
                            FormButton(
                                icon = Res.drawable.sign_in_apple,
                                isPrimary = false,
                            )
                        }

                    }

                }

                // Sign up
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Nie masz konta? ",
                        color = onBackground,
                        style = getAppTypography().bodySmall,
                    )
                    Text(
                        text = "Zarejestruj się",
                        color = onBackground,
                        style = getAppTypography().bodySmall,
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    // TODO: switch to another screen where the user can sign up
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                            ),
                        textDecoration = TextDecoration.Underline
                    )
                }

            }

        }

    }


}