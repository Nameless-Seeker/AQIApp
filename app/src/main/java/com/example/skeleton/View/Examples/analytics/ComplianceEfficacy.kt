package com.example.skeleton.View.Examples.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.skeleton.Model.KTor.Requests_And_Responses.ComplianceEfficacyResponse
import com.example.skeleton.Model.KTor.Requests_And_Responses.Intervention
import com.example.skeleton.Model.Repository.NetworkResult
import com.example.skeleton.ViewModel.MyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplianceEfficacy(viewModel: MyViewModel) {
    val complianceEfficacyState by viewModel.complianceEfficacyState.collectAsState()

    Button(
        onClick = { viewModel.getComplianceEfficacy() },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Icon(Icons.Default.Insights, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Analyze Intervention Efficacy")
    }

    ComplianceEfficacyResult(complianceEfficacyState)
}


@Composable
private fun ComplianceEfficacyResult(state: NetworkResult<ComplianceEfficacyResponse>) {
    when (state) {
        is NetworkResult.Idle -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Ready to analyze environmental impact",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        NetworkResult.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is NetworkResult.Success -> {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SummaryCard(state.response)

                Text(
                    "Recent Interventions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                state.response.recentInterventions?.forEach { intervention ->
                    InterventionCard(intervention)
                }
            }
        }

        is NetworkResult.Error -> {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "${state.type}: ${state.message}",
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(response: ComplianceEfficacyResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Assessment, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Efficacy Score", style = MaterialTheme.typography.titleMedium)
            }
            Text(
                text = "${response.overallEfficacyScore}%",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
            )

            MetricRow("Avg AQI Drop", response.metrics.averageAqiDropPostIntervention)
            MetricRow("Most Effective", response.metrics.mostEffectiveTarget)
            MetricRow("Least Effective", response.metrics.leastEffectiveTarget)
        }
    }
}

@Composable
private fun MetricRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun InterventionCard(intervention: Intervention) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    intervention.target,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                StatusBadge(intervention.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AqiBox(
                    "Before",
                    intervention.aqiBefore.toString(),
                    MaterialTheme.colorScheme.errorContainer
                )
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(16.dp)
                )
                AqiBox(
                    "After",
                    intervention.aqiAfter.toString(),
                    MaterialTheme.colorScheme.tertiaryContainer
                )
            }
        }
    }
}

@Composable
private fun AqiBox(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Surface(
        color = color.copy(alpha = 0.3f),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.width(80.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val isSuccess = status.contains("Success", ignoreCase = true)
    Surface(
        color = (if (isSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error).copy(
            alpha = 0.1f
        ),
        contentColor = if (isSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}
