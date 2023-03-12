package com.example.composerecorder.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composerecorder.presentation.components.RecordingButton
import com.example.composerecorder.presentation.components.VoiceHertz
import kotlinx.coroutines.delay

@Composable
fun RecorderScreen() {


    var isPlaying by remember {
        mutableStateOf(false)
    }

    var hertz by remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(key1 = isPlaying, block = {
        if (isPlaying) {
            while (true) {
                delay(90)
                hertz = (0..100).random().toFloat()
            }
        } else {
            hertz = 0f
        }
    })

    Box(
        Modifier
            .padding(top = 200.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        VoiceHertz(newHertz = hertz, isPlaying = isPlaying)
    }

    Box(contentAlignment = Alignment.Center) {
        RecordingButton(
            modifier = Modifier.size(70.dp),
            isPlaying = isPlaying,
            onPlayChanged = { isPlaying = it })
    }
}