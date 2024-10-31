package com.safronov.livepictures.ui.composable.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.safronov.livepictures.ui.composable.PathData

@Composable
fun AnimatedPathsCanvas(
    animation: List<PathData>,
    modifier: Modifier = Modifier,
    color: Color = com.safronov.livepictures.ui.theme.Colors.Blue,
    strokeWidth: Float = 4f,
    durationMillis: Int = 1000
) {
    val transition = rememberInfiniteTransition(label = "")

    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Canvas(modifier = modifier) {
        animation.forEach { path ->
            drawPath(
                path = path.path,
                color = color,
                style = Stroke(width = strokeWidth),
                alpha = progress
            )
        }
    }
}