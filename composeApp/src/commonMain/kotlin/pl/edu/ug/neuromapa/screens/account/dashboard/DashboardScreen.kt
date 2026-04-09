package pl.edu.ug.neuromapa.screens.account.dashboard

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.screens.home.settings.widePadding
import pl.edu.ug.neuromapa.screens.home.settings.wideSpacing

@Composable
fun DashboardScreen(
    headerTitle: String,
    userProfileImage: DrawableResource,
    userEmail: String,
    userId: String,
    onSignOut: () -> Unit,
    scrollState: ScrollState = rememberScrollState()
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            Header(
                title = headerTitle,
                userProfileImage = userProfileImage,
                showProfileTopRightCorner = false,
                roundBottomCorners = true,
                modifier = Modifier,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(widePadding),
                verticalArrangement = Arrangement.spacedBy(wideSpacing)
            ) {

                // --- TEST DATA (Vlad's task: verify auth works) ---

                Text(
                    text = "Email: $userEmail",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "ID: $userId",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    onClick = onSignOut,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Wyloguj się")
                }

                // --- END TEST DATA ---
                // TODO (Taras): add fancy profile UI here

            }

        }

    }

}
