package com.example.skeleton.View.Examples.agents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.skeleton.Model.KTor.Requests_And_Responses.ChatRequest
import com.example.skeleton.Model.KTor.Requests_And_Responses.ChatResponse
import com.example.skeleton.Model.Repository.NetworkResult
import com.example.skeleton.ViewModel.MyViewModel

@Composable
fun Chat(viewModel: MyViewModel) {
    val chatState by viewModel.chatState.collectAsState()
    var message by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("28.6304") }
    var longitude by remember { mutableStateOf("77.2177") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Chat")
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Message") },
            modifier = Modifier.fillMaxWidth()
        )
        NumberField("Latitude", latitude) { latitude = it }
        NumberField("Longitude", longitude) { longitude = it }

        Button(
            onClick = {
                viewModel.chat(
                    ChatRequest(
                        message = message,
                        latitude = latitude.toDoubleOrNull() ?: 28.6304,
                        longitude = longitude.toDoubleOrNull() ?: 77.2177
                    )
                )
            }
        ) {
            Text("Send")
        }

        ChatResult(chatState)
    }
}

@Composable
private fun NumberField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ChatResult(state: NetworkResult<ChatResponse>) {
    when (state) {
        is NetworkResult.Idle -> {}
        NetworkResult.Loading -> CircularProgressIndicator()
        is NetworkResult.Success -> Text(state.response.reply)
        is NetworkResult.Error -> Text("${state.type}: ${state.message}")
    }
}
