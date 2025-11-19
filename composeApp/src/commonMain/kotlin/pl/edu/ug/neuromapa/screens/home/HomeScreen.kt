package pl.edu.ug.neuromapa.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.edu.ug.neuromapa.components.Header

@Composable
fun HomeScreen() {
    Scaffold(
        bottomBar = {
            // ...
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()) // Żeby można było przewijać tekst
        ) {

            Header(
                title = "Witaj, Ryszard!",
                onProfileClick = { println("Kliknięto profil") }
            )

        }

    }

}