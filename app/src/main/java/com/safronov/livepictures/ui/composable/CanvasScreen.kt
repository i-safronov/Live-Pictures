package com.safronov.livepictures.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.safronov.livepictures.R
import com.safronov.livepictures.ui.theme.ColorValue
import com.safronov.livepictures.ui.theme.Colors

@Composable
fun CanvasScreen(
    modifier: Modifier = Modifier,
    prevActionValue: ColorValue,
    nextActionValue: ColorValue,
    onPrevAction: () -> Unit,
    onNextAction: () -> Unit,
    deleteFrameValue: ColorValue,
    addFrameValue: ColorValue,
    listOfFramesValue: ColorValue,
    onDeleteFrame: () -> Unit,
    onAddFrame: () -> Unit,
    onListOfFrames: () -> Unit,
    stopAnimationValue: ColorValue,
    startAnimationValue: ColorValue,
    onStopAnimationValue: () -> Unit,
    onStartAnimationValue: () -> Unit,
    penValue: ColorValue,
    onPen: () -> Unit,
    brushValue: ColorValue,
    onBrush: () -> Unit = {},
    eraseValue: ColorValue,
    onErase: () -> Unit = {},
    instrumentsValue: ColorValue,
    onInstruments: () -> Unit = {},
    colorValue: ColorValue,
    onColor: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier
            .background(Colors.Background)
            .fillMaxSize(),
        topBar = {
            TopBar(
                prevActionValue = prevActionValue,
                onPrevAction = onPrevAction,
                nextActionValue = nextActionValue,
                onNextAction = onNextAction,
                deleteFrameValue = deleteFrameValue,
                onDeleteFrame = onDeleteFrame,
                addFrameValue = addFrameValue,
                onAddFrame = onAddFrame,
                listOfFramesValue = listOfFramesValue,
                onListOfFrames = onListOfFrames,
                stopAnimationValue = stopAnimationValue,
                onStopAnimationValue = onStopAnimationValue,
                startAnimationValue = startAnimationValue,
                onStartAnimationValue = onStartAnimationValue
            )
        },
        bottomBar = {
            BottomBar(
                penValue,
                onPen,
                brushValue,
                onBrush,
                eraseValue,
                onErase,
                instrumentsValue,
                onInstruments,
                colorValue,
                onColor
            )
        }
    ) { innerPadding ->
        val image = ImageBitmap.imageResource(id = R.drawable.ic_canvas)

        Box(
            modifier = Modifier
                .background(Colors.Background)
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    end = 16.dp,
                    start = 16.dp,
                    top = 32.dp,
                    bottom = 32.dp
                )
                .clip(RoundedCornerShape(size = 20.dp))
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = image,
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop,
            )

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                //TODO implement
            }
        }
    }
}

@Composable
private fun BottomBar(
    penValue: ColorValue,
    onPen: () -> Unit,
    brushValue: ColorValue,
    onBrush: () -> Unit,
    eraseValue: ColorValue,
    onErase: () -> Unit,
    instrumentsValue: ColorValue,
    onInstruments: () -> Unit,
    colorValue: ColorValue,
    onColor: () -> Unit
) {
    Row(
        modifier = Modifier
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
                tint = penValue.colorByState(),
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
                tint = eraseValue.colorByState(),
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
            enabled = colorValue.enabled,
            onClick = onColor,
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(percent = 100))
                    .size(28.dp)
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
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
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
                onClick = onAddFrame,
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
            prevActionValue = ColorValue(enabled = false),
            nextActionValue = ColorValue(enabled = false),
            onPrevAction = {},
            onNextAction = {},
            deleteFrameValue = ColorValue(enabled = true),
            addFrameValue = ColorValue(enabled = true),
            listOfFramesValue = ColorValue(enabled = true),
            onDeleteFrame = {},
            onAddFrame = {},
            onListOfFrames = {},
            stopAnimationValue = ColorValue(enabled = false),
            startAnimationValue = ColorValue(enabled = false),
            onStartAnimationValue = {},
            onStopAnimationValue = {},
            penValue = ColorValue(enabled = true),
            brushValue = ColorValue(enabled = true),
            eraseValue = ColorValue(enabled = true),
            colorValue = ColorValue(enabled = true, enableColor = Colors.Blue),
            onPen = {},
            onBrush = {},
            onErase = {},
            onInstruments = {},
            onColor = {},
            instrumentsValue = ColorValue(enabled = true)
        )
    }
}