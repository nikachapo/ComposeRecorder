package com.example.composerecorder

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

@Composable
fun VoiceHertz(
    values: List<MutableState<Float>>,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 10f,
    cornerRadius: CornerRadius = CornerRadius(12f, 12f)
) {

    val animatedValues = remember {
        mutableListOf<State<Float>>()
    }

    for (i in values.indices) {
        if (animatedValues.size < values.size) {
            animatedValues.add(
                animateFloatAsState(
                    targetValue = values[i].value,
                    animationSpec = tween(durationMillis = 500, easing = LinearEasing)
                )
            )
        } else {
            animatedValues[i] = animateFloatAsState(
                targetValue = values[i].value,
                animationSpec = tween(durationMillis = 500, easing = LinearEasing)
            )
        }
    }


    Canvas(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth()
    ) {
        var lastX = -strokeWidth * 3
        values.forEach { value ->
            drawRoundRect(
                color = Color.Gray,
                topLeft = Offset(
                    x = (lastX + (size.width / values.size.toFloat())).also { lastX = it },
                    y = (size.height + value.value * size.height / 100) / 2
                ),
                size = Size(strokeWidth, -value.value * size.height / 100),
                cornerRadius = cornerRadius
            )
        }


//        val path = Path().apply {
//            val radius = 150f
//            moveTo(center.x + radius, center.y)
//            val controlX1 = center.x - radius
//            val controlY1 = center.y - radius
//            val controlX2 = center.x + radius
//            val controlY2 = center.y + radius
//            cubicTo(controlX1, controlY1, controlX2, controlY2, center.x, center.y - radius)
//        }
//
//        drawPath(path, Color.Blue, style = Stroke(2f))

    }
}