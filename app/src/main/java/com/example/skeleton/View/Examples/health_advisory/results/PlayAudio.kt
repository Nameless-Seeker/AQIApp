package com.example.skeleton.View.Examples.health_advisory.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.skeleton.ViewModel.MyViewModel

@Composable
fun PlayAudio(viewModel: MyViewModel) {

    val context = LocalContext.current



    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(viewModel.englishAudioUrl.value))
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {

        // Play / Resume
        Button(onClick = {
            exoPlayer.play()
        }) {
            Text("▶")
        }

// Pause
        Button(onClick = {
            exoPlayer.pause()
        }) {
            Text("⏸")
        }

// Stop and go to beginning
        Button(onClick = {
            exoPlayer.pause()
            exoPlayer.seekTo(0)
        }) {
            Text("⏹")
        }
    }
}