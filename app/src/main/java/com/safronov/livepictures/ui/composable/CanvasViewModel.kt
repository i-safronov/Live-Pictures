package com.safronov.livepictures.ui.composable

import androidx.compose.runtime.mutableStateListOf
import com.safronov.livepictures.udf.EffectorScope
import com.safronov.livepictures.udf.ExecutorScope
import com.safronov.livepictures.udf.UDFViewModel
import com.safronov.livepictures.ui.composable.CanvasContract.*
import com.safronov.livepictures.ui.composable.CanvasContract.State.UserAction.*
import com.safronov.livepictures.ui.theme.ColorValue
import com.safronov.livepictures.ui.theme.Colors

class CanvasViewModel : UDFViewModel<State, Executor, Effect, Event>(
    initState = State()
) {
    override suspend fun ExecutorScope<Effect>.execute(ex: Executor): State =
        when (ex) {
            Executor.OnAddFrame -> {
                val disablePaths = mutableStateListOf<PathData>()
                val activePaths = mutableStateListOf<PathData>()
                disablePaths.addAll(
                    state.activePaths + state.disablePaths
                )
                state.copy(
                    disablePaths = disablePaths,
                    activePaths = activePaths,
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
                    val activePaths = mutableStateListOf<PathData>()
                    val disablePaths = mutableStateListOf<PathData>()
                    val prevFrame = state.currentFrameId - 1
                    state.activePaths.clear()
                    activePaths.addAll(state.disablePaths.filter { it.frameId == prevFrame })
                    disablePaths.addAll(state.disablePaths.filter { it.frameId != prevFrame })

                    state.copy(
                        activePaths = activePaths,
                        disablePaths = disablePaths,
                        deleteFrameValue = if (prevFrame < 1) ColorValue(enabled = false) else state.deleteFrameValue,
                        currentFrameId = prevFrame
                    )
                }
            }

            is Executor.AddPath -> {
                if (state.userAction == PEN) {
                    state.activePaths.add(
                        PathData(
                            path = ex.path,
                            color = ex.color,
                            frameId = state.currentFrameId,
                        )
                    )
                } else {
                    state.erasesPaths.add(
                        PathData(
                            path = ex.path,
                            color = Colors.White,
                            frameId = state.currentFrameId,
                        )
                    )
                }
                state.copy()
            }

            is Executor.ChangeUserAction -> {
                state.copy(
                    userAction = ex.userAction
                )
            }
        }

    override suspend fun EffectorScope<Executor>.affect(ef: Effect) {
        //TODO
    }

}