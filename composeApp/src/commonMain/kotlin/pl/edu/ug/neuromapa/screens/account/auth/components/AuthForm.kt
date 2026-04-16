package pl.edu.ug.neuromapa.screens.account.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.edu.ug.neuromapa.components.form.FormButton
import pl.edu.ug.neuromapa.components.form.FormSection
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding

@Composable
fun AuthForm(
    submitButtonText: String,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onSubmit: (email: String, password: String) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(globalComponentWidePadding),
    ) {

        FormSection(
            placeholder = "Email",
            value = email,
            onValueChange = onEmailChange,
            isEmail = true,
        )

        FormSection(
            placeholder = "Hasło",
            value = password,
            onValueChange = onPasswordChange,
            isPassword = true,
        )

        Box(
            modifier = Modifier.bounceClick {
                onSubmit(email, password)
            }
        ) {
            FormButton(
                text = submitButtonText,
                isPrimary = true
            )
        }
    }
}
