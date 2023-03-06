package com.example.composerecorder.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

@Composable
fun RecordingButton(
    values: List<MutableState<Float>>,
    modifier: Modifier = Modifier,
) {

    val animatedValues = remember {
        mutableListOf<State<Float>>()
    }

    for (i in values.indices) {
        if (animatedValues.size < values.size) {
            animatedValues.add(
                animateFloatAsState(
                    targetValue = values[i].value,
                    animationSpec = tween(durationMillis = 700, easing = LinearEasing)
                )
            )
        } else {
            animatedValues[i] = animateFloatAsState(
                targetValue = values[i].value,
                animationSpec = tween(durationMillis = 700, easing = LinearEasing)
            )
        }
    }

    val path = remember { Path() }
    val fillPath = remember { Path() }

    val onBgColor = MaterialTheme.colorScheme.onBackground

    val fillColors = remember {
        listOf(onBgColor, Color.Gray, Color.Transparent)
    }

    Box(modifier = modifier
        .drawBehind {
            var rotate = 0f
            animatedValues.forEach {
                rotate(rotate) {
                    rotate += 360 / values.size
                    translate(top = -size.height) {
                        // (x0, y0) is initial coordinate where path is moved with path.moveTo(x0,y0)
                        val x0 = size.width / 1.2f
                        val y0 = size.width

                        /*
                Adds a quadratic bezier segment that curves from the current point(x0,y0) to the
                given point (x2, y2), using the control point (x1, y1).
             */
                        val x1 = size.width / 2
                        val x2 = size.width / 5
                        val y2 = size.width
                        path.reset()
                        path.moveTo(x0, y0)
                        path.quadraticBezierTo(x1 = x1, y1 = size.width / it.value, x2 = x2, y2 = y2)

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
        .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {

        Image(modifier = Modifier.size(32.dp), painter = painterResource(id = R.drawable.baseline_mic_24), contentDescription = "")

    }

}