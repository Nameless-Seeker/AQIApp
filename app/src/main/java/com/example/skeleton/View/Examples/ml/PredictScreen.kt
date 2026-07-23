package com.example.skeleton.View.Examples.ml

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Source
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.example.skeleton.Model.KTor.Requests_And_Responses.PredictRequest
import com.example.skeleton.Model.KTor.Requests_And_Responses.PredictResponse
import com.example.skeleton.Model.Repository.NetworkResult
import com.example.skeleton.View.Examples.analytics.AnalyticsScreen
import com.example.skeleton.View.Examples.health_advisory.results.ResultsScreen
import com.example.skeleton.View.Examples.lat_long_pred.LatLongScreen
import com.example.skeleton.View.Examples.navigation.Analytics
import com.example.skeleton.View.Examples.navigation.Aqi
import com.example.skeleton.View.Examples.navigation.LatLong
import com.example.skeleton.View.Examples.navigation.Maps
import com.example.skeleton.View.Examples.navigation.Predict
import com.example.skeleton.View.Examples.navigation.Result
import com.example.skeleton.ViewModel.MyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictScreen(viewModel: MyViewModel, rootNav: MutableList<NavKey>,predictNav: MutableList<NavKey>) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (predictNav.lastOrNull() != Maps) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Analyze & Predict AQI",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = predictNav,
            onBack = {
                predictNav.removeLastOrNull()
            }
        ) { key ->
            when (key) {
                Aqi -> NavEntry(key) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .verticalScroll(rememberScrollState())
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Predict(viewModel, predictNav, rootNav)
                    }
                }

                Maps -> NavEntry(key) {
                    MapsScreen(viewModel)
                }

                else -> error("Unknown key: $key")
            }
        }
    }
}
