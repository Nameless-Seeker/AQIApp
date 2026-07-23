package com.example.Aqi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.compose.SkeletonTheme
import com.example.skeleton.View.Examples.agents.Chat
import com.example.skeleton.View.Examples.health_advisory.results.Enforcement
import com.example.skeleton.View.Examples.analytics.CityComparison
import com.example.skeleton.View.Examples.analytics.ComplianceEfficacy
import com.example.skeleton.View.Examples.health_advisory.results.HealthAdvisory
import com.example.skeleton.View.Examples.navigation.RootNavigation
import com.example.skeleton.ViewModel.MyViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    val viewModel: MyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SkeletonTheme() {
                RootNavigation(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AQIApp(viewModel: MyViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "AQI Dashboard",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HealthAdvisory(viewModel = viewModel)
            Enforcement(viewModel = viewModel)
            Chat(viewModel = viewModel)
            CityComparison(viewModel = viewModel)
            ComplianceEfficacy(viewModel = viewModel)
        }
    }
}
