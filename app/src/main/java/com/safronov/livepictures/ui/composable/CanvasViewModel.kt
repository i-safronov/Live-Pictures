package com.safronov.livepictures.ui.composable

import androidx.compose.runtime.mutableStateListOf
import com.safronov.livepictures.udf.EffectorScope
import com.safronov.livepictures.udf.ExecutorScope
import com.safronov.livepictures.udf.UDFViewModel
import com.safronov.livepictures.ui.composable.CanvasContract.*
import com.safronov.livepictures.ui.theme.ColorValue

class CanvasViewModel : UDFViewModel<State, Executor, Effect, Event>(
    initState = State()
) {
    override suspend fun ExecutorScope<Effect>.execute(ex: Executor): State =
        when (ex) {
            Executor.OnAddFrame -> {
                val newPaths = mutableStateListOf<PathData>()
                newPaths.addAll(
                    state.paths.map { it: PathData ->
                        it.copy(
                            color = it.color.copy(alpha = 30f),
                        )
                    }
                )
                state.copy(
                    paths = newPaths,
                    currentFrame = state.currentFrame + 1
                )
            }

            Executor.OnChangeColorPaletteState -> {
                state.copy(
                    isShowingColorPalette = !state.isShowingColorPalette
                )
            }

            Executor.OnDeleteFrame -> {
                if (state.currentFrame < 1) {
                    state.copy(
                        deleteFrameValue = ColorValue(enabled = false)
                    )
                } else {
                    val newPaths = mutableStateListOf<PathData>()
                    val prevFrame = state.currentFrame - 1
                    newPaths.addAll(
                        state.paths.filter {
                            it.pathId <= prevFrame
                        }
                    )
                    state.copy(
                        paths = newPaths,
                        deleteFrameValue = if (prevFrame < 1) ColorValue(enabled = false) else state.deleteFrameValue,
                        currentFrame = prevFrame
                    )
                }
            }
        }

    override suspend fun EffectorScope<Executor>.affect(ef: Effect) {
        //TODO
    }

}