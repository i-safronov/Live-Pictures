package com.safronov.livepictures.ui.composable

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.safronov.livepictures.udf.UDF
import com.safronov.livepictures.ui.theme.ColorValue
import java.util.Stack

class CanvasContract {

    data class State(
        val prevActionValue: ColorValue = ColorValue(enabled = false),
        val nextActionValue: ColorValue = ColorValue(enabled = false),
        val deleteFrameValue: ColorValue = ColorValue(enabled = false),
        val addFrameValue: ColorValue = ColorValue(enabled = true),
        val listOfFramesValue: ColorValue = ColorValue(enabled = false),
        val stopAnimationValue: ColorValue = ColorValue(enabled = false),
        val startAnimationValue: ColorValue = ColorValue(enabled = false),
        val penValue: ColorValue = ColorValue(enabled = true, isActive = true),
        val brushValue: ColorValue = ColorValue(enabled = false),
        val eraseValue: ColorValue = ColorValue(enabled = true),
        val instrumentsValue: ColorValue = ColorValue(enabled = false),
        val colorValue: ColorValue = ColorValue(enabled = true),
        val activePaths: SnapshotStateList<PathData> = SnapshotStateList(),
        val cachedActivePaths: Stack<PathData> = Stack(),
        val disablePaths: Stack<PathData> = Stack(),
        val undoStack: Stack<PathData> = Stack(),
        val redoStack: Stack<PathData> = Stack(),
        val isShowingColorPalette: Boolean = false,
        val currentFrameId: Int = 0,
        val userInputType: UserInputType = UserInputType.PEN,
        val isLoadingAnimation: Boolean = false,
        val isAnimating: Boolean = false,
        val animation: List<PathData> = emptyList()
    ) : UDF.State {
        @Stable
        enum class UserInputType {
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
            val userInputType: State.UserInputType
        ): Executor
        data object PrevAction: Executor
        data object NextAction: Executor
        data object MakeAnimation: Executor
        data class Animate(
            val animation: List<PathData>
        ): Executor
        data object DismissAnimation: Executor
    }

    sealed interface Event: UDF.Event

    sealed interface Effect: UDF.Effect {
        data class PrepareToAnimate(
            val activePaths: List<PathData>,
            val disablePaths: List<PathData>,
            val lastFrameId: Int
        ): Effect
    }

}