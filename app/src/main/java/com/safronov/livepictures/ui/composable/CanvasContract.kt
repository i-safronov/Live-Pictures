package com.safronov.livepictures.ui.composable

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.safronov.livepictures.udf.UDF
import com.safronov.livepictures.ui.theme.ColorValue

class CanvasContract {

    data class State(
        val prevActionValue: ColorValue = ColorValue(enabled = true),
        val nextActionValue: ColorValue = ColorValue(enabled = true),
        val deleteFrameValue: ColorValue = ColorValue(enabled = false),
        val addFrameValue: ColorValue = ColorValue(enabled = true),
        val listOfFramesValue: ColorValue = ColorValue(enabled = true),
        val stopAnimationValue: ColorValue = ColorValue(enabled = true),
        val startAnimationValue: ColorValue = ColorValue(enabled = true),
        val penValue: ColorValue = ColorValue(enabled = true),
        val brushValue: ColorValue = ColorValue(enabled = true),
        val eraseValue: ColorValue = ColorValue(enabled = true),
        val instrumentsValue: ColorValue = ColorValue(enabled = true),
        val paths: SnapshotStateList<PathData> = SnapshotStateList(),
        val isShowingColorPalette: Boolean = false,
        val currentFrame: Int = 0
    ): UDF.State

    sealed interface Executor: UDF.Executor {
        data object OnAddFrame: Executor
        data object OnDeleteFrame: Executor
        data object OnChangeColorPaletteState: Executor
    }

    sealed interface Event: UDF.Event

    sealed interface Effect: UDF.Effect

}