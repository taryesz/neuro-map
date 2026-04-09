package pl.edu.ug.neuromapa.screens.account.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.edu.ug.neuromapa.components.form.FormButton
import pl.edu.ug.neuromapa.components.form.FormSection
import pl.edu.ug.neuromapa.screens.account.auth.enum.AuthFormType
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding

@Composable
fun AuthForm(
    mode: AuthFormType,
    submitButtonText: String,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    firstName: String = "",
    onFirstNameChange: (String) -> Unit = {},
    lastName: String = "",
    onLastNameChange: (String) -> Unit = {},
    onSubmit: (email: String, password: String, firstName: String, lastName: String) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(globalComponentWidePadding),
    ) {

        if (mode == AuthFormType.SignUp) {
            FormSection(
                placeholder = "Imię",
                value = firstName,
                onValueChange = onFirstNameChange,
            )
            FormSection(
                placeholder = "Nazwisko",
                value = lastName,
                onValueChange = onLastNameChange,
            )
        }

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
                onSubmit(email, password, firstName, lastName)
            }
        ) {
            FormButton(
                text = submitButtonText,
                isPrimary = true
            )
        }
    }
}
