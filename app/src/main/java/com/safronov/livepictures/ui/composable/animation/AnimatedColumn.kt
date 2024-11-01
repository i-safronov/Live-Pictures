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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.safronov.livepictures.ui.composable.PathData

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
        var currentPathData by remember { mutableStateOf(animation.getOrNull(index)) }
        val strokeWidthPx = with(LocalDensity.current) { 8.dp.toPx() }

        val completedPaths = remember { mutableStateListOf<PathData>() }

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val pathMeasure = remember { PathMeasure() }
            val animatedProgress = remember { Animatable(0f) }

            if (currentPathData != null) {
                LaunchedEffect(currentPathData) {
                    animatedProgress.snapTo(0f)

                    pathMeasure.setPath(currentPathData!!.path, false)

                    animatedProgress.animateTo(
                        targetValue = pathMeasure.length,
                        animationSpec = tween(duration)
                    )

                    completedPaths.add(currentPathData!!)
                    if (index < animation.lastIndex) {
                        index += 1
                        currentPathData = animation.getOrNull(index)
                    } else {
                        currentPathData = null
                    }
                }
            }

            val animatedPath = remember(currentPathData) {
                derivedStateOf {
                    val destination = Path()
                    if (currentPathData != null) {
                        pathMeasure.getSegment(0f, animatedProgress.value, destination)
                    }
                    destination
                }
            }

            Canvas(modifier = Modifier.fillMaxWidth()) {
                if (currentPathData != null && animatedProgress.value > 0f) {
                    drawPath(
                        animatedPath.value,
                        currentPathData!!.color,
                        style = Stroke(width = strokeWidthPx)
                    )
                }
                completedPaths.forEach { pathData ->
                    drawPath(pathData.path, pathData.color, style = Stroke(width = strokeWidthPx))
                }
            }
        }
    }
}




