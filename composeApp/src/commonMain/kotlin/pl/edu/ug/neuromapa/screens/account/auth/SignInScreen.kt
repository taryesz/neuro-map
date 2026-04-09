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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.sign_in_screen_submit_button_text
import neuromapa.composeapp.generated.resources.sign_in_screen_title
import neuromapa.composeapp.generated.resources.sign_in_screen_alternative_sign_in_methods_title
import neuromapa.composeapp.generated.resources.sign_in_screen_offer_to_sign_up_link_text
import neuromapa.composeapp.generated.resources.sign_in_screen_offer_to_sign_up_text
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.data.AuthState
import pl.edu.ug.neuromapa.data.AuthViewModel
import pl.edu.ug.neuromapa.screens.account.auth.components.AlternativeAuthForm
import pl.edu.ug.neuromapa.screens.account.auth.components.AuthSwitch
import pl.edu.ug.neuromapa.screens.account.auth.components.AuthForm
import pl.edu.ug.neuromapa.screens.account.auth.components.AuthFormHeader
import pl.edu.ug.neuromapa.screens.account.auth.enum.AuthFormType
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding

@Composable
fun SignInScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    val authState by authViewModel.authState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

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

            Column(
                modifier = Modifier.fillMaxSize().weight(1f).padding(globalComponentWidePadding),
            ) {

                AuthFormHeader(title = stringResource(Res.string.sign_in_screen_title))

                Spacer(modifier = Modifier.weight(1f))

                when (authState) {
                    is AuthState.Checking -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    is AuthState.Error -> {
                        Text(
                            text = (authState as AuthState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth()
                        )
                        AuthForm(
                            mode = AuthFormType.SignIn,
                            submitButtonText = stringResource(Res.string.sign_in_screen_submit_button_text),
                            email = email,
                            onEmailChange = { email = it },
                            password = password,
                            onPasswordChange = { password = it },
                            onSubmit = { e, p, _, _ -> authViewModel.signIn(e, p) }
                        )
                    }
                    else -> {
                        AuthForm(
                            mode = AuthFormType.SignIn,
                            submitButtonText = stringResource(Res.string.sign_in_screen_submit_button_text),
                            email = email,
                            onEmailChange = { email = it },
                            password = password,
                            onPasswordChange = { password = it },
                            onSubmit = { e, p, _, _ -> authViewModel.signIn(e, p) }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(globalComponentWidePadding)
                ) {

                    AlternativeAuthForm(
                        formTitle = stringResource(Res.string.sign_in_screen_alternative_sign_in_methods_title),
                    )

                    AuthSwitch(
                        text = stringResource(Res.string.sign_in_screen_offer_to_sign_up_text),
                        linkText = stringResource(Res.string.sign_in_screen_offer_to_sign_up_link_text),
                        onLinkClick = onNavigateToSignUp
                    )

                }

            }

        }

    }

}
