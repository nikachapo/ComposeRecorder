package com.example.composerecorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composerecorder.ui.theme.ComposeRecorderTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRecorderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val values = remember {
                        mutableListOf<MutableState<Float>>().apply {
                            addAll((1..9).map { mutableStateOf(it.toFloat()) })
                        }
                    }

//                    LaunchedEffect(key1 = Unit) {
//                        while (true) {
//                            delay(90)
//                            values.forEachIndexed { index, value ->
//                                if (index == values.lastIndex) {
//                                    value.value = (2..8).random().toFloat()
//                                } else {
//                                    value.value = values[index + 1].value
//                                }
//                            }
//                        }
//                    }

//                    Box(
//                        Modifier
//                            .height(10.dp)
//                            .fillMaxWidth(), contentAlignment = Alignment.Center) {
//                        VoiceHertz(values = values)
//                    }

                    Box(contentAlignment = Alignment.Center) {
                        RecordingButton(modifier = Modifier.size(70.dp))
                    }
                }
            }
        }
    }
}
