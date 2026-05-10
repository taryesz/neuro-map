package pl.edu.ug.neuromapa.screens.account.dashboard

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
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
    displayName: String,
    onSignOut: () -> Unit,
    currentName: String,
    onNameChange: (String) -> Unit,
    onSaveName: () -> Unit,
    currentBirthDate: String,
    onBirthDateChange: (String) -> Unit,
    onSaveBirthDate: () -> Unit,
    onPickProfilePhoto: () -> Unit,
    profilePhotoUrl: String?,
    saveStatusMessage: String?,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
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

            Column(modifier = Modifier.padding(widePadding)) {
                Text("Ustawienia profilu", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = currentName,
                    onValueChange = onNameChange,
                    label = { Text("Twoje imię") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Ciemny motyw")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(checked = isDarkTheme, onCheckedChange = onThemeChange)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onSaveName,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Zapisz imię")
                }

                if (!saveStatusMessage.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = saveStatusMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = currentBirthDate,
                    onValueChange = onBirthDateChange,
                    label = { Text("Data urodzenia (RRRR-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onSaveBirthDate,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Zapisz datę urodzenia")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onPickProfilePhoto,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Wybierz zdjęcie profilowe")
                }

                if (!profilePhotoUrl.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Zdjęcie zapisane",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(widePadding),
                verticalArrangement = Arrangement.spacedBy(wideSpacing)
            ) {
                Text(
                    text = "Cześć${if (displayName.isNotBlank()) " $displayName" else ""}!",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "E-mail: $userEmail",
                    style = MaterialTheme.typography.bodyMedium
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
            }
        }
    }
}
