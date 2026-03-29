package pl.edu.ug.neuromapa.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.screens.login.settings.widePadding
import pl.edu.ug.neuromapa.screens.login.settings.wideSpacing

@Composable
fun LoginScreen() {

    // Wrapper of the whole screen
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) {

        // One more wrapper...
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Header (turquoise panel at the very top)
            Header(
                userProfileImage = null,
                title = "Zaloguj się",
                showProfile = false,
            )

            // Body (main content)
            Column(
                modifier = Modifier.fillMaxSize().weight(1f).padding(widePadding),
                verticalArrangement = Arrangement.spacedBy(wideSpacing)
            ) {

                // TODO: Here goes the rest of the UI...

            }

        }

    }


}