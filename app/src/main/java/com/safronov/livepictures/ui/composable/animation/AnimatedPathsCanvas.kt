package com.safronov.livepictures.ui.composable.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import com.safronov.livepictures.ui.composable.PathData
import kotlinx.coroutines.delay

@Composable
fun SmoothAnimatedPathsCanvas(
    paths: List<PathData>, // Assuming PathData holds a Path
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    strokeWidth: Float = 4f,
    durationPerPathMillis: Int = 200
) {
    var currentPathIndex by remember { mutableStateOf(0) }
    val animatedProgress = remember { Animatable(0f) }

    // Restart animation for each path segment with the smooth handwriting effect
    LaunchedEffect(currentPathIndex) {
        if (currentPathIndex < paths.size) {
            animatedProgress.snapTo(0f)
            animatedProgress.animateTo(
                targetValue = 100f,
                animationSpec = tween(durationMillis = durationPerPathMillis, easing = LinearOutSlowInEasing)
            )
            delay(300)
            currentPathIndex++
        }
    }

    Canvas(modifier = modifier) {
        // Draw all previous paths fully since they are already completed
        paths.take(currentPathIndex).forEach { pathData ->
            drawPath(
                path = pathData.path,
                color = color,
                style = Stroke(width = strokeWidth)
            )
        }

        // Draw the current path with animated progress for a smooth handwriting effect
        if (currentPathIndex < paths.size) {
            val currentPath = paths[currentPathIndex].path
            val pathMeasure = PathMeasure()
            pathMeasure.setPath(currentPath, false)

            val animatedPath = Path().apply {
                pathMeasure.getSegment(
                    startDistance = 0f,
                    stopDistance = animatedProgress.value * pathMeasure.length,
                    destination = this,
                    startWithMoveTo = true
                )
            }

            drawPath(
                path = animatedPath,
                color = color,
                style = Stroke(width = strokeWidth)
            )
        }
    }
}
