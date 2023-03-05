package com.example.composerecorder

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun RecordingButton(modifier: Modifier = Modifier) {

    val path = remember { Path() }
    val fillPath = remember { Path() }

    val bgColor = MaterialTheme.colorScheme.background

    val fillColors = remember {
        listOf(Color.Black, Color.Gray, bgColor)
    }

    var sizeWidth by remember {
        mutableStateOf(0f)
    }
    var y1 by remember {
        mutableStateOf(0f)
    }

    val y1Animated by animateFloatAsState(targetValue = y1)

    LaunchedEffect(key1 = true, block = {
        while (true) {
            delay(300)
            y1 = sizeWidth / (2..5).random()
        }
    })
    Box(modifier = modifier
        .drawBehind {
            for (i in 0..360 step 40) {
                rotate(i.toFloat()) {
                    translate(top = -size.height) {
                        sizeWidth = size.width
                        // (x0, y0) is initial coordinate where path is moved with path.moveTo(x0,y0)
                        val x0 = size.width / 1.5f
                        val y0 = size.width

                        /*
                Adds a quadratic bezier segment that curves from the current point(x0,y0) to the
                given point (x2, y2), using the control point (x1, y1).
             */
                        val x1 = size.width / 2
                        val x2 = size.width / 3
                        val y2 = size.width
                        path.reset()
                        path.moveTo(x0, y0)
                        path.quadraticBezierTo(x1 = x1, y1 = y1Animated, x2 = x2, y2 = y2)

                        fillPath.reset()
                        fillPath.addPath(path)
                        fillPath.lineTo(x1, y1)
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
        .background(Color.Blue)
    )

}