package com.safronov.livepictures.ui.composable

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.safronov.livepictures.R
import com.safronov.livepictures.ui.composable.CanvasContract.Executor
import com.safronov.livepictures.ui.composable.CanvasContract.State
import com.safronov.livepictures.ui.composable.CanvasContract.State.UserAction
import com.safronov.livepictures.ui.theme.ColorValue
import com.safronov.livepictures.ui.theme.Colors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class PathData(
    val path: Path = Path(),
    val color: Color,
    val frameId: Int,
    val alpha: Float = 1f
)

val mainColors = listOf(
    Colors.Green,
    Colors.Orange,
    Colors.Yellow,
    Colors.Blue
)

@Composable
fun CanvasScreen(
    modifier: Modifier = Modifier,
    state: State,
    dispatch: (Executor) -> Unit
) {
    var pathColor by remember { mutableStateOf(Colors.Blue) }
    var isShowingColorPalette by remember { mutableStateOf(false) }
    var bottomBarSize by remember { mutableStateOf(IntSize.Zero) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .background(Colors.Background)
            .padding(
                top = 20.dp,
                bottom = 16.dp
            )
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    prevActionValue = state.prevActionValue,
                    onPrevAction = {
                        //TODO
                    },
                    nextActionValue = state.nextActionValue,
                    onNextAction = {
                        //TODO
                    },
                    deleteFrameValue = state.deleteFrameValue,
                    onDeleteFrame = {
                        dispatch(Executor.OnDeleteFrame)
                    },
                    addFrameValue = state.addFrameValue,
                    onAddFrame = {
                        dispatch(Executor.OnAddFrame)
                    },
                    listOfFramesValue = state.listOfFramesValue,
                    onListOfFrames = {
                        //TODO
                    },
                    stopAnimationValue = state.stopAnimationValue,
                    onStopAnimationValue = {
                        //TODO
                    },
                    startAnimationValue = state.startAnimationValue,
                    onStartAnimationValue = {
                        //TODO
                    }
                )
            },
            bottomBar = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    BottomBar(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                            .onGloballyPositioned { layoutCoordinates ->
                                bottomBarSize = layoutCoordinates.size
                            },
                        penValue = state.penValue,
                        onPen = {
                            dispatch(Executor.ChangeUserAction(UserAction.PEN))
                        },
                        brushValue = state.brushValue,
                        onBrush = {
                            //TODO
                        },
                        eraseValue = state.eraseValue,
                        onErase = {
                            dispatch(Executor.ChangeUserAction(UserAction.ERASE))
                        },
                        instrumentsValue = state.instrumentsValue,
                        onInstruments = {
                            //TODO
                        },
                        colorValue = ColorValue(
                            enabled = true,
                            enableColor = pathColor,
                            disableColor = pathColor
                        ),
                        isShowingColorPalette = isShowingColorPalette,
                        userAction = state.userAction,
                        onColor = {
                            isShowingColorPalette = !isShowingColorPalette
                        }
                    )
                }
            }
        ) { innerPadding ->
            val image = ImageBitmap.imageResource(id = R.drawable.ic_canvas)
            var tempPath = Path()
            var path by remember { mutableStateOf(Path()) }

            Column(
                modifier = Modifier.background(Colors.Background)
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .clip(RoundedCornerShape(size = 20.dp))
            ) {
                Box(
                    modifier = Modifier
                        .background(Colors.White)
                ) {
                    val activePaths = state.activePaths
                    val disablePaths = state.disablePaths
                    val erasesPaths = state.erasesPaths

                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(alpha = .3f)
                    ) {
                        disablePaths.forEach { pathData ->
                            drawPath(
                                path = pathData.path,
                                color = pathData.color,
                                style = Stroke(10f),
                            )
                        }
                    }

                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        tempPath = Path().apply {
                                            moveTo(offset.x, offset.y)
                                        }
                                        path = tempPath
                                    },
                                    onDragEnd = {
                                        dispatch(
                                            Executor.AddPath(
                                                path = tempPath,
                                                color = pathColor,
                                            )
                                        )

                                        scope.launch {
                                            delay(100)
                                            path = Path()
                                            tempPath = path
                                        }
                                    },
                                    onDrag = { change, dragAmount ->
                                        tempPath.moveTo(
                                            x = change.position.x - dragAmount.x,
                                            y = change.position.y - dragAmount.y
                                        )

                                        tempPath.lineTo(
                                            x = change.position.x,
                                            y = change.position.y
                                        )

                                        path = Path().apply {
                                            addPath(tempPath)
                                        }
                                    }
                                )
                            }
                    ) {
                        activePaths.forEach { pathData ->
                            drawPath(
                                path = pathData.path,
                                color = pathData.color,
                                style = Stroke(10f),
                            )
                        }

                        erasesPaths.forEach { pathData ->
                            drawPath(
                                path = pathData.path,
                                color = pathData.color,
                                style = Stroke(12f),
                            )
                        }

                        drawPath(
                            path = path,
                            color = if (state.userAction == UserAction.PEN) {
                                pathColor
                            } else {
                                Colors.White
                            },
                            style = Stroke(if (state.userAction == UserAction.PEN) {
                                10f
                            } else {
                                12f
                            })
                        )
                    }
                }
            }
        }

        if (isShowingColorPalette) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .animateContentSize()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 74.dp)
                        .clip(RoundedCornerShape(size = 4.dp))
                        .border(
                            width = 1.dp,
                            color = Colors.BorderColor,
                            shape = RoundedCornerShape(size = 4.dp)
                        )
                        .background(Colors.TransparentWhite)
                        .padding(
                            16.dp
                        ),
                ) {
                    IconButton(
                        onClick = {
                            //TODO implement
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(32.dp),
                            painter = painterResource(R.drawable.ic_color_palette),
                            contentDescription = "A color palette",
                            tint = Colors.White,
                        )
                    }

                    mainColors.forEach { color ->
                        IconButton(
                            onClick = {
                                pathColor = color
                                isShowingColorPalette = false
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(percent = 100))
                                    .size(28.dp)
                                    .background(
                                        color = color,
                                        shape = RoundedCornerShape(percent = 100)
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    penValue: ColorValue,
    onPen: () -> Unit,
    brushValue: ColorValue,
    onBrush: () -> Unit,
    eraseValue: ColorValue,
    onErase: () -> Unit,
    instrumentsValue: ColorValue,
    onInstruments: () -> Unit,
    colorValue: ColorValue,
    isShowingColorPalette: Boolean,
    userAction: UserAction,
    onColor: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Colors.Background)
            .padding(
                bottom = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        IconButton(
            enabled = penValue.enabled,
            onClick = onPen,
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp),
                painter = painterResource(R.drawable.ic_pen),
                contentDescription = "A pen",
                tint = penValue.copy(isActive = userAction == UserAction.PEN).colorByState(),
            )
        }

        IconButton(
            enabled = brushValue.enabled,
            onClick = onBrush,
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp),
                painter = painterResource(R.drawable.ic_brush),
                contentDescription = "A brush",
                tint = brushValue.colorByState(),
            )
        }

        IconButton(
            enabled = eraseValue.enabled,
            onClick = onErase,
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp),
                painter = painterResource(R.drawable.ic_erase),
                contentDescription = "Erase",
                tint = penValue.copy(isActive = userAction == UserAction.ERASE).colorByState(),
            )
        }

        IconButton(
            enabled = instrumentsValue.enabled,
            onClick = onInstruments,
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp),
                painter = painterResource(R.drawable.ic_instruments),
                contentDescription = "The instruments",
                tint = instrumentsValue.colorByState(),
            )
        }

        IconButton(
            modifier = Modifier,
            enabled = colorValue.enabled,
            onClick = onColor,
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(percent = 100))
                    .size(28.dp)
                    .run {
                        if (isShowingColorPalette) {
                            border(
                                width = 1.5.dp,
                                color = Colors.Active,
                                shape = RoundedCornerShape(percent = 100)
                            )
                        } else {
                            this
                        }
                    }
                    .background(
                        color = colorValue.colorByState(),
                        shape = RoundedCornerShape(percent = 100)
                    )
            )
        }
    }
}

@Composable
private fun TopBar(
    prevActionValue: ColorValue,
    onPrevAction: () -> Unit,
    nextActionValue: ColorValue,
    onNextAction: () -> Unit,
    deleteFrameValue: ColorValue,
    onDeleteFrame: () -> Unit,
    addFrameValue: ColorValue,
    onAddFrame: () -> Unit,
    listOfFramesValue: ColorValue,
    onListOfFrames: () -> Unit,
    stopAnimationValue: ColorValue,
    onStopAnimationValue: () -> Unit,
    startAnimationValue: ColorValue,
    onStartAnimationValue: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 12.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                enabled = prevActionValue.enabled,
                onClick = onPrevAction,
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_prev_action),
                    contentDescription = "Previous action",
                    tint = prevActionValue.colorByState(),
                )
            }

            IconButton(
                enabled = nextActionValue.enabled,
                onClick = onNextAction,
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_next_action),
                    contentDescription = "Next action",
                    tint = nextActionValue.colorByState(),
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                enabled = deleteFrameValue.enabled,
                onClick = onDeleteFrame,
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_delete_frame),
                    contentDescription = "Delete this frame",
                    tint = deleteFrameValue.colorByState(),
                )
            }

            IconButton(
                enabled = addFrameValue.enabled,
                onClick = {
                    onAddFrame()
                },
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_add_frame),
                    contentDescription = "Add a frame",
                    tint = addFrameValue.colorByState(),
                )
            }

            IconButton(
                enabled = listOfFramesValue.enabled,
                onClick = onListOfFrames,
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_list_of_frames),
                    contentDescription = "A list of frames",
                    tint = listOfFramesValue.colorByState(),
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                enabled = stopAnimationValue.enabled,
                onClick = onStopAnimationValue,
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_stop_animation),
                    contentDescription = "Stop this animation",
                    tint = stopAnimationValue.colorByState(),
                )
            }

            IconButton(
                enabled = startAnimationValue.enabled,
                onClick = onStartAnimationValue,
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_start_animation),
                    contentDescription = "Start an animation",
                    tint = startAnimationValue.colorByState(),
                )
            }
        }
    }
}

@Composable
@Preview
fun CanvasScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
    ) {
        CanvasScreen(
            state = State(
                prevActionValue = ColorValue(enabled = false),
                nextActionValue = ColorValue(enabled = false),
                deleteFrameValue = ColorValue(enabled = true),
                addFrameValue = ColorValue(enabled = true),
                listOfFramesValue = ColorValue(enabled = true),
                stopAnimationValue = ColorValue(enabled = false),
                startAnimationValue = ColorValue(enabled = false),
                penValue = ColorValue(enabled = true),
                brushValue = ColorValue(enabled = true),
                eraseValue = ColorValue(enabled = true),
                instrumentsValue = ColorValue(enabled = true)
            ),
            dispatch = {

            }
        )
    }
}