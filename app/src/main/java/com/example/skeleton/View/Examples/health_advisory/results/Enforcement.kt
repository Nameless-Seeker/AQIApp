package com.example.skeleton.View.Examples.health_advisory.results

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.skeleton.Model.KTor.Requests_And_Responses.EnforcementRequest
import com.example.skeleton.Model.KTor.Requests_And_Responses.EnforcementResponse
import com.example.skeleton.Model.Repository.NetworkResult
import com.example.skeleton.ViewModel.MyViewModel

@Composable
fun Enforcement(viewModel: MyViewModel) {
    val enforcementState by viewModel.enforcementState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Gavel, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Enforcement Protocol", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Actionable insights for local authorities", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Button(
            enabled = viewModel.predResultObtained,
            onClick = {
                viewModel.getEnforcement(
                    EnforcementRequest(
                        latitude = viewModel.latitude ,
                        longitude = viewModel.longitude,
                        currentAqi = viewModel.current_aqi.toInt(),
                        forecastAqi = viewModel.forecast_aqi.toInt(),
                        trafficPct = viewModel.traffic_pct,
                        industrialPct = viewModel.industrial_pct,
                        biomassPct = viewModel.biomass_pct
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Default.Security, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generate Enforcement Strategy")
        }

        EnforcementResult(enforcementState)
    }
}

@Composable
private fun EnforcementResult(state: NetworkResult<EnforcementResponse>) {
    when (state) {
        is NetworkResult.Idle -> {
            Text("Awaiting input data...", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline, modifier = Modifier.padding(16.dp))
        }
        NetworkResult.Loading -> {
            Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is NetworkResult.Success -> {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Hotspot Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Hotspot Analysis", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            AqiStat("Current", state.response.hotspot.currentAqi.toString())
                            AqiStat("Forecast", state.response.hotspot.forecastAqi.toString())
                            RiskBadge(state.response.hotspot.riskLevel)
                        }
                    }
                }

                // Sources Card
                SectionCard(Icons.Default.Factory, "Primary Sources") {
                    state.response.topSources?.forEach { source ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(source.name, style = MaterialTheme.typography.bodyMedium)
                            Text("${source.distanceKm} km", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                        }
                    } ?: Text("No primary sources identified", style = MaterialTheme.typography.bodySmall)
                }

                // Recommendations Card
                SectionCard(Icons.Default.TipsAndUpdates, "Recommended Actions") {
                    state.response.recommendations?.forEach { rec ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(rec, style = MaterialTheme.typography.bodySmall)
                        }
                    } ?: Text("No specific recommendations", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        is NetworkResult.Error -> {
            Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun AqiStat(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun RiskBadge(level: String) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = level,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun SectionCard(icon: ImageVector, title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
