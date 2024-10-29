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
                            color = it.color,
                            alpha = .5f
                        )
                    }
                )
                state.copy(
                    paths = newPaths,
                    currentFrameId = state.currentFrameId + 1,
                    deleteFrameValue = ColorValue(enabled = true)
                )
            }

            Executor.OnChangeColorPaletteState -> {
                state.copy(
                    isShowingColorPalette = !state.isShowingColorPalette
                )
            }

            Executor.OnDeleteFrame -> {
                if (state.currentFrameId < 1) {
                    state.copy(
                        deleteFrameValue = ColorValue(enabled = false)
                    )
                } else {
                    val newPaths = mutableStateListOf<PathData>()
                    val prevFrame = state.currentFrameId - 1
                    newPaths.addAll(
                        state.paths.filter {
                            it.frameId <= prevFrame
                        }.map {
                            it.copy(
                                color = it.color,
                                alpha = 1f
                            )
                        }
                    )
                    state.copy(
                        paths = newPaths,
                        deleteFrameValue = if (prevFrame < 1) ColorValue(enabled = false) else state.deleteFrameValue,
                        currentFrameId = prevFrame
                    )
                }
            }

            is Executor.AddPath -> {
                state.paths.add(
                    PathData(
                        path = ex.path,
                        color = ex.color,
                        frameId = state.currentFrameId
                    )
                )
                state.copy()
            }
        }

    override suspend fun EffectorScope<Executor>.affect(ef: Effect) {
        //TODO
    }

}