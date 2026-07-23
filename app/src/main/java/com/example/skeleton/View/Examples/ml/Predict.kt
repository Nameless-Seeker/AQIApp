package com.example.skeleton.View.Examples.ml

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.example.skeleton.Model.KTor.Requests_And_Responses.PredictRequest
import com.example.skeleton.Model.KTor.Requests_And_Responses.PredictResponse
import com.example.skeleton.Model.Repository.NetworkResult
import com.example.skeleton.View.Examples.navigation.Maps
import com.example.skeleton.View.Examples.navigation.Result
import com.example.skeleton.ViewModel.MyViewModel

@Composable
fun Predict(viewModel: MyViewModel, predictNav: MutableList<NavKey>, rootNav: MutableList<NavKey>) {
    val predictState by viewModel.predictState.collectAsState()

    Column() {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.3f
                )
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Target Location",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Latitude: ${String.format("%.2f",viewModel.latitude)}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Longitude: ${String.format("%.2f",viewModel.longitude)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Button(
            onClick = {
                viewModel.predict(
                    PredictRequest(
                        latitude = viewModel.latitude,
                        longitude = viewModel.longitude
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Default.Analytics, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Analyze & Predict AQI")
        }

        PredictResult(predictState)

        if (viewModel.predResultObtained) {
            Button(
                onClick = { rootNav.add(Result) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors()
            ) {
                Text("View Detailed Report")
            }

            Button(
                onClick = { predictNav.add(Maps) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors()
            ) {
                Text("Go to maps")
            }
        }
    }
}


@Composable
private fun PredictResult(state: NetworkResult<PredictResponse>) {
    when (state) {
        NetworkResult.Idle -> {}
        NetworkResult.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Fetching forecast data...", style = MaterialTheme.typography.bodySmall)
            }
        }

        is NetworkResult.Success -> {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Main AQI Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Forecast AQI", style = MaterialTheme.typography.labelLarge)
                                Text(
                                    text = state.response.dispersionPlume.properties.forecastAqi.toInt()
                                        .toString(),
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    text = state.response.dispersionPlume.properties.riskLevel,
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    ),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "As of: ${state.response.timestamp}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // Pollutant details
                Text(
                    "Pollutant Breakdown (PM2.5)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PollutantCard(
                        Modifier.weight(1f),
                        "1 Hour",
                        "%.2f".format(state.response.forecastPm25.oneHour)
                    )
                    PollutantCard(
                        Modifier.weight(1f),
                        "24 Hours",
                        "%.2f".format(state.response.forecastPm25.twentyFourHours)
                    )
                    PollutantCard(
                        Modifier.weight(1f),
                        "48 Hours",
                        "%.2f".format(state.response.forecastPm25.fortyEightHours)
                    )
                }

                // Attribution Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Source,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Source Attribution",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        AttributionRow("Traffic", (state.response.attribution.trafficPct*100).toInt())
                        AttributionRow("Industrial", (state.response.attribution.industrialPct*100).toInt())
                        AttributionRow("Biomass", (state.response.attribution.biomassPct*100).toInt())
                        AttributionRow("Solvent VOC", (state.response.attribution.solventVocPct*100).toInt())

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            "Confidence Level: ${(state.response.attribution.confidence * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }

        is NetworkResult.Error -> {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${state.type}: ${state.message}",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun PollutantCard(modifier: Modifier, label: String, value: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                alpha = 0.4f
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                "µg/m³",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AttributionRow(label: String, percentage: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(
            "${percentage}%",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
