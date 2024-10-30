package com.safronov.livepictures.ui.composable

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.safronov.livepictures.udf.UDF
import com.safronov.livepictures.ui.theme.ColorValue

class CanvasContract {

    data class State(
        val prevActionValue: ColorValue = ColorValue(enabled = false),
        val nextActionValue: ColorValue = ColorValue(enabled = false),
        val deleteFrameValue: ColorValue = ColorValue(enabled = false),
        val addFrameValue: ColorValue = ColorValue(enabled = true),
        val listOfFramesValue: ColorValue = ColorValue(enabled = false),
        val stopAnimationValue: ColorValue = ColorValue(enabled = false),
        val startAnimationValue: ColorValue = ColorValue(enabled = true),
        val penValue: ColorValue = ColorValue(enabled = true, isActive = true),
        val brushValue: ColorValue = ColorValue(enabled = false),
        val eraseValue: ColorValue = ColorValue(enabled = true),
        val instrumentsValue: ColorValue = ColorValue(enabled = false),
        val activePaths: SnapshotStateList<PathData> = SnapshotStateList(),
        val disablePaths: SnapshotStateList<PathData> = SnapshotStateList(),
        val erasesPaths: SnapshotStateList<PathData> = SnapshotStateList(),
        val isShowingColorPalette: Boolean = false,
        val currentFrameId: Int = 0,
        val userAction: UserAction = UserAction.PEN,
    ): UDF.State {
        @Stable
        enum class UserAction {
            PEN, ERASE
        }
    }

    sealed interface Executor: UDF.Executor {
        data object OnAddFrame: Executor
        data object OnDeleteFrame: Executor
        data object OnChangeColorPaletteState: Executor
        data class AddPath(
            val path: Path,
            val color: Color,
        ): Executor
        data class ChangeUserAction(
            val userAction: State.UserAction
        ): Executor
    }

    sealed interface Event: UDF.Event

    sealed interface Effect: UDF.Effect

}