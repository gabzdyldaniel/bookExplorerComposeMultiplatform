package cz.gabzdyldaniel

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cz.gabzdyldaniel.ui.home.HomeScreen
import cz.gabzdyldaniel.ui.home.HomeScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        HomeScreen(viewModel = HomeScreenViewModel())
    }
}