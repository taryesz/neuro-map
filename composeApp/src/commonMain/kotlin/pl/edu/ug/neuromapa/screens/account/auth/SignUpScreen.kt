package pl.edu.ug.neuromapa.screens.account.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.sign_up_screen_alternative_sign_up_methods_title
import neuromapa.composeapp.generated.resources.sign_up_screen_offer_to_sign_in_link_text
import neuromapa.composeapp.generated.resources.sign_up_screen_offer_to_sign_in_text
import neuromapa.composeapp.generated.resources.sign_up_screen_submit_button_text
import neuromapa.composeapp.generated.resources.sign_up_screen_title
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.screens.account.auth.components.AlternativeAuthForm
import pl.edu.ug.neuromapa.screens.account.auth.components.AuthSwitch
import pl.edu.ug.neuromapa.screens.account.auth.components.AuthForm
import pl.edu.ug.neuromapa.screens.account.auth.components.AuthFormHeader
import pl.edu.ug.neuromapa.screens.account.auth.enum.AuthFormType
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding

@Composable
fun SignUpScreen(
    onNavigateToSignIn: () -> Unit
) {

    val focusManager = LocalFocusManager.current

    // Wrapper of the whole screen
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

//        TODO: use this variable if the third field (repeat password) is required
//        var confirmPassword by remember { mutableStateOf("") }

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
                modifier = Modifier.fillMaxSize().weight(1f).padding(globalComponentWidePadding),
            ) {

                AuthFormHeader(title = stringResource(Res.string.sign_up_screen_title))

                Spacer(modifier = Modifier.weight(1f))

                // Sign up form
                AuthForm(
                    mode = AuthFormType.SignUp,
                    submitButtonText = stringResource(Res.string.sign_up_screen_submit_button_text),
                    email = email,
                    onEmailChange = { newEmail -> email = newEmail },
                    password = password,
                    onPasswordChange = { newPassword -> password = newPassword },
//                    TODO: use these two variables if the third field is required
//                    confirmPassword = confirmPassword,
//                    onConfirmPasswordChange = { newPassword -> confirmPassword = newPassword }
                )

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(globalComponentWidePadding)
                ) {

                    // Alternative sign up methods
                    AlternativeAuthForm(
                        formTitle = stringResource(Res.string.sign_up_screen_alternative_sign_up_methods_title),
                    )

                    // Offer to sign in at the bottom of the screen
                    AuthSwitch(
                        text = stringResource(Res.string.sign_up_screen_offer_to_sign_in_text),
                        linkText = stringResource(Res.string.sign_up_screen_offer_to_sign_in_link_text),
                        onLinkClick = onNavigateToSignIn
                    )

                }

            }

        }

    }


}