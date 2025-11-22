package pl.edu.ug.neuromapa

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.edu.ug.neuromapa.enums.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview
import pl.edu.ug.neuromapa.components.NavigationBar

import pl.edu.ug.neuromapa.screens.home.HomeScreen
import pl.edu.ug.neuromapa.screens.map.MapScreen
import pl.edu.ug.neuromapa.ui.NeuroMapaTheme

@Composable
@Preview
fun App() {
    NeuroMapaTheme {

        var currentScreen by remember { mutableStateOf(Screen.Home) }

        Scaffold(
            bottomBar = {
                NavigationBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { newScreen ->
                        currentScreen = newScreen
                    }
                )
            },
            contentWindowInsets = WindowInsets(0.dp)    // Ignore system status bar
        ) { paddingValues ->

            Box(modifier = Modifier.padding(paddingValues)) {
                when (currentScreen) {
                    Screen.Home -> HomeScreen()
                    Screen.Add -> Text("Ekran Dodaj")           // TODO
                    Screen.Map -> MapScreen()
                    Screen.Favorites -> Text("Ekran Ulubione")  // TODO
                    Screen.Survey -> Text("Ekran Ankiety")      // TODO
                }
            }

        }
    }
}