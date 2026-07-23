package com.example.skeleton.View.Examples.lat_long_pred

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation3.runtime.NavKey
import androidx.navigationevent.NavigationEventDispatcher
import com.example.skeleton.View.Examples.navigation.Predict
import com.example.skeleton.ViewModel.MyViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatLongScreen(viewModel: MyViewModel, rootNav: MutableList<NavKey>) {
    val context = LocalContext.current
    var locationError by remember { mutableStateOf<String?>(null) }


    val hasFineLocation = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val hasCoarseLocation = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
        if (granted) {
            getCurrentLocation(context, viewModel, rootNav) { error ->
                locationError = error
            }
        } else {
            locationError = "Location permission denied"
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Location Access",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Current Location")
            Text("Latitude: ${viewModel.latitude}")
            Text("Longitude: ${viewModel.longitude}")

            if (locationError != null) {
                Text("Error: $locationError", color = androidx.compose.ui.graphics.Color.Red)
            }

            Button(
                onClick = {
                    locationError = null


                    if (hasFineLocation || hasCoarseLocation) {
                        getCurrentLocation(context, viewModel, rootNav) { error ->
                            locationError = error
                        }
                    } else {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Get Current Lat and Long")
            }

            //Setting tthe domain
            //Set protocol
            OutlinedTextField(
                value = viewModel.protocol,
                onValueChange = { viewModel.updateProtocol(it) },
                label = { Text("http or https") })

            //Set host
            OutlinedTextField(
                value = viewModel.host,
                onValueChange = { viewModel.updateHost(it) },
                label = { Text("10.0.2.2") })

            //Set port
            OutlinedTextField(
                value = viewModel.port,
                onValueChange = { viewModel.updatePort(it) },
                label = { Text("8000") })


            if (locationError != null) {
                Text("Error: $locationError", color = androidx.compose.ui.graphics.Color.Red)
            }

            Button(
                onClick = { viewModel.setDomain() },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Set the domain name")
            }

            Button(
                onClick = {
//                    rootNav.removeLastOrNull()
                    rootNav.add(Predict)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                enabled = viewModel.isDomainSet && viewModel.isCoordinatesObtained
            ) {
                Icon(Icons.Default.Analytics, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Predict AQI")
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    context: Context,
    viewModel: MyViewModel,
    rootNav: MutableList<NavKey>,
    onError: (String) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        CancellationTokenSource().token
    ).addOnSuccessListener { location ->
        if (location != null) {
            //For maps
            viewModel.updateUserLocation(LatLng(location.latitude, location.longitude))
        } else {
            onError("Location is null. Try turning on GPS.")
        }
    }.addOnFailureListener { exception ->
        onError(exception.message ?: "Failed to get location")
    }
}
