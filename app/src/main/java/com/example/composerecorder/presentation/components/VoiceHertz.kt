package com.example.composerecorder.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun VoiceHertz(
    modifier: Modifier = Modifier,
    wavesCount: Int = 40,
    strokeWidth: Float = 10f,
    cornerRadius: CornerRadius = CornerRadius(12f, 12f),
    centerLineWidth: Float = 2f,
    maxValue: Float = 100f,
    newHertz: Float,
    isPlaying: Boolean,
) {

    val animatedValues = remember(wavesCount) {
        mutableStateListOf<Animatable<Float, AnimationVector1D>>().apply {
            (1..wavesCount).map { add(Animatable(0f)) }
        }
    }

    LaunchedEffect(key1 = newHertz, key2 = isPlaying) {
        animatedValues.forEachIndexed { index, value ->
            launch {
                if (!isPlaying) {
                    value.animateTo(0f)
                } else {
                    if (index == animatedValues.lastIndex) {
                        value.animateTo(newHertz)
                    } else {
                        value.animateTo(animatedValues[index + 1].value)
                    }
                }
            }
        }
    }

    val onBgColor = MaterialTheme.colorScheme.onBackground
    Canvas(modifier = Modifier.fillMaxWidth()) {
        drawLine(
            onBgColor,
            start = Offset(x = 0f, y = size.height / 2),
            end = Offset(x = size.width, y = size.height / 2)
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    Color.Red,
                    topLeft = Offset(x = size.width / 2, y = 0f),
                    size = Size(width = centerLineWidth, height = size.height),
                    style = Stroke(width = centerLineWidth, cap = StrokeCap.Round)
                )
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .height(50.dp)
                .weight(1f)
        ) {
            var lastX = -strokeWidth
            animatedValues.forEach { value ->
                drawRoundRect(
                    color = onBgColor,
                    topLeft = Offset(
                        x = (lastX + (size.width / animatedValues.size.toFloat())).also {
                            lastX = it
                        },
                        y = (size.height + value.value * size.height / maxValue) / 2
                    ),
                    size = Size(strokeWidth, -value.value * size.height / maxValue),
                    cornerRadius = cornerRadius
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }

}