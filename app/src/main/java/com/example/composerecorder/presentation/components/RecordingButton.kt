package com.example.composerecorder.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.composerecorder.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

@JvmInline
value class WavesCount(val value: Int) {
    init {
        if (360 % value != 0) throw IllegalArgumentException("WavesCount must be divider of 360")
    }
}

@Composable
fun RecordingButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    wavesCount: WavesCount = WavesCount(4),
    animationDurationMillis: Int = 300,
    onPlayChanged: (isPlaying: Boolean) -> Unit = {}
) {

    val animatedValues = remember(wavesCount) {
        mutableStateListOf<Animatable<Float, AnimationVector1D>>().apply {
            (1..wavesCount.value).map { add(Animatable(0f)) }
        }
    }

    LaunchedEffect(key1 = isPlaying) {
        if (!isPlaying) {
            for (i in 0..animatedValues.lastIndex) {
                launch {
                    if (animatedValues[i].value != 0f) {
                        animatedValues[i].animateTo(
                            targetValue = 0f,
                            animationSpec = tween(animationDurationMillis, easing = LinearEasing)
                        )
                    }
                }
            }
        } else {
            while (true) {
                delay(animationDurationMillis.toLong())
                for (i in 0..animatedValues.lastIndex) {
                    if (!isActive) break
                    launch {
                        animatedValues[i].animateTo(
                            targetValue = Random.nextFloat() * 2,
                            animationSpec = tween(animationDurationMillis, easing = LinearEasing)
                        )
                    }
                }
            }
        }
    }

    val path = remember { Path() }
    val fillPath = remember { Path() }

    val onBgColor = MaterialTheme.colorScheme.onBackground

    val fillColors = remember {
        listOf(onBgColor, Color.Gray, Color.Transparent)
    }

    val animatedButtonColor by animateColorAsState(
        targetValue = if (isPlaying) Color.Red else Color.Blue,
        animationSpec = tween(animationDurationMillis)
    )

    Box(modifier = modifier
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            onPlayChanged(!isPlaying)
        }
        .drawBehind {
            var rotate = 0f
            animatedValues.forEach {
                rotate(rotate) {
                    rotate += 360 / wavesCount.value
                    translate(top = -size.height) {
                        val x0 = size.width / 1.2f
                        val y0 = size.width
                        val x1 = size.width / 2
                        val x2 = size.width / 5
                        val y2 = size.width
                        path.reset()
                        path.moveTo(x0, y0)
                        path.quadraticBezierTo(
                            x1 = x1,
                            y1 = size.width / it.value,
                            x2 = x2,
                            y2 = y2
                        )

                        fillPath.reset()
                        fillPath.addPath(path)
                        fillPath.lineTo(x1, size.width / it.value)
                        fillPath.lineTo(x2, y2)
                        fillPath.close()

                        drawPath(
                            color = Color.Transparent,
                            path = path,
                            style = Stroke(
                                width = 2.dp.toPx()
                            )
                        )

                        drawPath(
                            brush = Brush.verticalGradient(colors = fillColors),
                            path = fillPath
                        )
                    }
                }
            }
        }
        .clip(CircleShape)
        .background(animatedButtonColor),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = isPlaying,
            enter = slideInVertically(),
            exit = fadeOut()
        ) {
            Image(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.baseline_stop_24),
                contentDescription = "Stop recording"
            )
        }

        AnimatedVisibility(
            visible = !isPlaying,
            enter = slideInVertically(),
            exit = fadeOut()
        ) {
            Image(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.baseline_mic_24),
                contentDescription = "Start Recording"
            )
        }
    }

}