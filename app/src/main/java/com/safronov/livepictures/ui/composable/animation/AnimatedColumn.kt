package com.safronov.livepictures.ui.composable.animation

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.safronov.livepictures.ui.composable.PathData
import com.safronov.livepictures.ui.theme.Colors
import kotlin.math.min

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AnimatedColumn(
    modifier: Modifier = Modifier,
    duration: Int = 2000,
    animation: List<PathData>,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        var index by remember { mutableStateOf(0) }
        var currentPath by remember { mutableStateOf(animation.getOrNull(index)) }
        val strokeWidthPx = with(LocalDensity.current) { 8.dp.toPx() }

        val completedPaths = remember { mutableStateListOf<Pair<Path, Color>>() }

        if (currentPath != null) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                val pathMeasure = remember { PathMeasure() }
                pathMeasure.setPath(currentPath!!.path, false)

                val animatedProgress = remember { Animatable(0f) }

                LaunchedEffect(currentPath) {
                    animatedProgress.snapTo(0f)
                    animatedProgress.animateTo(
                        targetValue = pathMeasure.length,
                        animationSpec = tween(duration)
                    )

                    completedPaths.add(currentPath!!.path to currentPath!!.color)

                    if (index < animation.lastIndex) {
                        index += 1
                        currentPath = animation.getOrNull(index)
                    }
                }

                val animatedPath = remember {
                    derivedStateOf {
                        val destination = Path()
                        pathMeasure.getSegment(0f, animatedProgress.value, destination)
                        destination
                    }
                }

                Canvas(modifier = Modifier.fillMaxWidth()) {
                    completedPaths.forEach { (path, color) ->
                        drawPath(path, color, style = Stroke(width = strokeWidthPx))
                    }

                    drawPath(animatedPath.value, currentPath!!.color, style = Stroke(width = strokeWidthPx))
                }
            }
        }
    }
}



@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AnimatedPath(
    modifier: Modifier = Modifier,
    pathStr: Path,
    strokeWidth: Dp,
    color: Color = Colors.Blue,
    duration: Int,
) {
    with(LocalDensity.current) {
        BoxWithConstraints(modifier) {
            var path by remember { mutableStateOf(Path()) }
            val strokeWidthPx = strokeWidth.toPx()

            val pathMeasure = remember { PathMeasure() }
            pathMeasure.setPath(path, false)

            val animatedProgress = remember { Animatable(0f) }

            LaunchedEffect(pathMeasure.length) {
                animatedProgress.animateTo(
                    targetValue = pathMeasure.length,
                    animationSpec = tween(duration)
                )
            }

            val animatedPath = remember {
                derivedStateOf {
                    val destination = Path()
                    pathMeasure.getSegment(0f, animatedProgress.value, destination)
                    destination
                }
            }

            Canvas(modifier = Modifier.fillMaxWidth()) {
                drawPath(animatedPath.value, color, style = Stroke(width = strokeWidthPx))
            }
        }
    }
}


fun Path.fillBounds(strokeWidthPx: Float, maxWidth: Int, maxHeight: Int) {
    val pathSize = getBounds()
    val matrix = Matrix()

    val horizontalOffset = pathSize.left - strokeWidthPx / 2
    val verticalOffset = pathSize.top - strokeWidthPx / 2
    val scaleWidth = maxWidth / (pathSize.width + strokeWidthPx)
    val scaleHeight = maxHeight / (pathSize.height + strokeWidthPx)
    val scale = min(scaleHeight, scaleWidth)

    matrix.scale(scale, scale)
    matrix.translate(-horizontalOffset, -verticalOffset)

    transform(matrix)
}