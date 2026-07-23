package com.example.skeleton.View.Examples.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.skeleton.View.Examples.analytics.AnalyticsScreen
import com.example.skeleton.View.Examples.health_advisory.results.ResultsScreen
import com.example.skeleton.View.Examples.lat_long_pred.LatLongScreen
import com.example.skeleton.View.Examples.ml.PredictScreen
import com.example.skeleton.ViewModel.MyViewModel

@Composable
fun RootNavigation(viewModel: MyViewModel){
    val rootBackStack = rememberNavBackStack(LatLong)
    val predictBackStack = rememberNavBackStack(Aqi)

    NavDisplay(
        backStack = rootBackStack,
        onBack = {
            rootBackStack.removeLastOrNull()
        }
    ) { key ->
        when (key) {
            LatLong -> NavEntry(key) {
                LatLongScreen(viewModel,rootBackStack)
            }

            Predict -> NavEntry(key) {
                PredictScreen(viewModel,rootBackStack,predictBackStack)
            }

            Result -> NavEntry(key) {
                ResultsScreen(viewModel,rootBackStack)
            }

            Analytics -> NavEntry(key) {
                AnalyticsScreen(viewModel,rootBackStack)
            }

            else -> error("Unknown key: $key")
        }
    }
}