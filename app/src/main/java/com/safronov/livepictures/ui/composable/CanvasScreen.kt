package com.safronov.livepictures.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.safronov.livepictures.R
import com.safronov.livepictures.ui.theme.ColorValue

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
    onStartAnimationValue: () -> Unit
) {
    Scaffold(
        modifier = modifier,
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

        }
    ) { innerPadding ->
        Canvas(
            modifier = Modifier
                .padding(innerPadding)
        ) {

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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
            horizontalArrangement = Arrangement.spacedBy(16.dp)
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
            horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                    painter = painterResource(R.drawable.ic_stop_animation),
                    contentDescription = "Start an animation",
                    tint = startAnimationValue.colorByState(),
                )
            }
        }
    }
}