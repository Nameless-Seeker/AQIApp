package com.example.skeleton.View.Examples.health_advisory.results

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skeleton.Model.KTor.Requests_And_Responses.HealthAdvisoryRequest
import com.example.skeleton.Model.KTor.Requests_And_Responses.HealthAdvisoryResponse
import com.example.skeleton.Model.Repository.NetworkResult
import com.example.skeleton.ViewModel.MyViewModel

@Composable
fun HealthAdvisory(viewModel: MyViewModel) {
    val healthAdvisoryState by viewModel.healthAdvisoryState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Medical Advisory", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Personalized safety guidance", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Button(
            enabled = viewModel.predResultObtained,
            onClick = {
                viewModel.getHealthAdvisory(
                    HealthAdvisoryRequest(
                        latitude = viewModel.latitude,
                        longitude = viewModel.longitude,
                        forecastAqi = viewModel.forecast_aqi.toInt(),
                        primaryPollutantSource = viewModel.primaryPollutantSource
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Default.VolunteerActivism, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generate Health Advisory")
        }

        HealthAdvisoryResult(viewModel, healthAdvisoryState)


    }
}

@Composable
private fun HealthAdvisoryResult(viewModel: MyViewModel,state: NetworkResult<HealthAdvisoryResponse>) {
    when (state) {
        is NetworkResult.Idle -> {
            Text("Awaiting data analysis...", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline, modifier = Modifier.padding(16.dp))
        }
        NetworkResult.Loading -> {
            Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is NetworkResult.Success -> {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Overview Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Forecast AQI", style = MaterialTheme.typography.labelSmall)
                            Text(state.response.forecastAqi.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Vulnerable Sites", style = MaterialTheme.typography.labelSmall)
                            Text(state.response.vulnerableLocationsFound.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                        }
                    }
                }

                // Advisory Cards (Tabbed or List)
                AdvisoryLanguageCard("English", state.response.advisory.english, Icons.Default.Translate)
//                AdvisoryLanguageCard("Hindi (हिंदी)", state.response.advisory.hindi, Icons.Default.Translate)
//                AdvisoryLanguageCard("Kannada (ಕನ್ನಡ)", state.response.advisory.kannada, Icons.Default.Translate)
            }

            PlayAudio(viewModel)
        }
        is NetworkResult.Error -> {
            Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun AdvisoryLanguageCard(lang: String, text: String, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(lang, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
