package com.safronov.livepictures.ui.composable.animation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.safronov.livepictures.ui.composable.PathData
import com.safronov.livepictures.ui.theme.Colors
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationDialog(
    modifier: Modifier = Modifier,
    text: String = "Made with care from Yandex ❤\uFE0F",
    loadingDescription: String = "Please wait, we are preparing an interesting video for you",
    animation: List<PathData>,
    isLoading: Boolean = false,
    properties: DialogProperties = DialogProperties(
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = false,
        dismissOnBackPress = true
    ),
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        modifier = modifier
            .fillMaxSize()
            .background(Colors.TransparentGray)
            .padding(
                top = 72.dp,
                bottom = 72.dp,
                end = 24.dp,
                start = 24.dp,
            )
            .clip(RoundedCornerShape(size = 20.dp)),
        onDismissRequest = onDismiss,
        properties = properties,
        content = {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 32.dp),
                verticalArrangement = Arrangement
                    .spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = text,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Colors.LightTextColor
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(80.dp),
                        color = Colors.Blue
                    )

                    Text(
                        text = loadingDescription,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Colors.LightTextGray
                    )
                } else {
                    animation.forEach {
                        AnimatedPath(
                            strokeWidth = 6.dp,
                            duration = 2000,
                            pathStr = it.path,
                            color = it.color
                        )
                    }
                }
            }
        }
    )
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

            LaunchedEffect(pathStr) {
                path = pathStr
            }

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