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
    mode: AuthFormType,  // TODO: Use this to determine if we need to log the user in or register them
    submitButtonText: String,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
//    TODO: use these two variables if the third field (repeat password) is required
//    confirmPassword: String? = null,
//    onConfirmPasswordChange: ((String) -> Unit)? = null,
) {

    Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(globalComponentWidePadding),
    ) {

        // Let the user INPUT their email
        FormSection(
            placeholder = "Email",
            value = email,
            onValueChange = onEmailChange
        )

        // Let the user INPUT their password
        FormSection(
            placeholder = "Hasło",
            value = password,
            onValueChange = onPasswordChange
        )

//        TODO: Decide if this field is needed. Does Supabase demand it?
//        // Let the user INPUT their password AGAIN
//        if (confirmPassword != null && onConfirmPasswordChange != null) {
//            FormSection(
//                placeholder = "Powtórz hasło",
//                value = confirmPassword,
//                onValueChange = onConfirmPasswordChange
//            )
//        }

        // "Sign in" or "Sign up" button
        Box(
            modifier = Modifier
                .bounceClick {
                    // TODO: send the information to Supabase... log in (or sign up) the user
                }
        ) {
            FormButton(
                text = submitButtonText,
                isPrimary = true
            )
        }

    }

}