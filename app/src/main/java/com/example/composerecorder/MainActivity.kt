package com.example.composerecorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composerecorder.presentation.components.RecordingButton
import com.example.composerecorder.presentation.components.VoiceHertz
import com.example.composerecorder.presentation.theme.AppTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val values = remember {
                        mutableListOf<MutableState<Float>>().apply {
                            addAll((1..6).map { mutableStateOf(0f) })
                        }
                    }

                    LaunchedEffect(key1 = Unit) {
                        while (true) {
                            delay(15)
                            values[(0..values.lastIndex).random()].value =
                                (0..3).random() + Random.nextFloat()
                        }
                    }
                    val values1 = remember {
                        mutableListOf<MutableState<Float>>().apply {
                            addAll((0..80).map { mutableStateOf(0f) })
                        }
                    }

                    LaunchedEffect(key1 = Unit) {
                        while (true) {
                            delay(90)
                            values1.forEachIndexed { index, value ->
                                if (index == values1.lastIndex) {
                                    value.value = (0..100).random().toFloat()
                                } else {
                                    value.value = values1[index + 1].value
                                }
                            }
                        }
                    }

                    Box(
                        Modifier
                            .padding(top = 200.dp)
                            .height(10.dp)
                            .fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        VoiceHertz(values = values1)
                    }

                    Box(contentAlignment = Alignment.Center) {
                        RecordingButton(values = values,modifier = Modifier.size(70.dp))
                    }
                }
            }
        }
    }
}
