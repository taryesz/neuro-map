package pl.edu.ug.neuromapa.screens.account.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.sign_in_apple
import neuromapa.composeapp.generated.resources.sign_in_google_light
import pl.edu.ug.neuromapa.components.form.FormButton
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumSpacing
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding

@Composable
fun AlternativeAuthForm(
    formTitle: String,
    onGoogleClick: () -> Unit = {},
    onAppleClick: () -> Unit = {},
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(globalComponentWidePadding),
    ) {

        // Section title
        Text(
            text = formTitle,
            color = MaterialTheme.colorScheme.onBackground,
            style = getAppTypography().bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // Alternative sign in methods (Google + Apple buttons wrapper)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing),
        ) {

            // Sign in with Google
            Box(
                modifier = Modifier
                    .weight(1f)
                    .bounceClick { onGoogleClick() }
            ) {
                FormButton(
                    icon = Res.drawable.sign_in_google_light,
                    isPrimary = false,
                )
            }

//            // Sign in with Apple
//            Box(
//                modifier = Modifier
//                    .weight(1f)
//                    .bounceClick { onAppleClick() }
//            ) {
//                FormButton(
//                    icon = Res.drawable.sign_in_apple,
//                    isPrimary = false,
//                )
//            }

        }

    }

}