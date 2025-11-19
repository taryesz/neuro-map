package pl.edu.ug.neuromapa

import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview

import pl.edu.ug.neuromapa.screens.home.HomeScreen
import pl.edu.ug.neuromapa.ui.theme.NeuroMapaTheme

@Composable
@Preview
fun App() {
    NeuroMapaTheme {
        HomeScreen()
    }

}