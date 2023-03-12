package com.example.composerecorder.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.composerecorder.presentation.components.RecordingButton
import com.example.composerecorder.presentation.components.VoiceHertz
import kotlinx.coroutines.delay

@Composable
fun RecorderScreen() {

    var hertz by remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(key1 = true, block = {
        while (true) {
            delay(90)
            hertz = (0..100).random().toFloat()
        }
    })

    Box(
        Modifier
            .padding(top = 200.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        VoiceHertz(newHertz = hertz)
    }


    var isPlaying by remember {
        mutableStateOf(false)
    }

    Box(contentAlignment = Alignment.Center) {
        RecordingButton(
            modifier = Modifier.size(70.dp),
            isPlaying = isPlaying,
            onPlayChanged = { isPlaying = it })
    }
}