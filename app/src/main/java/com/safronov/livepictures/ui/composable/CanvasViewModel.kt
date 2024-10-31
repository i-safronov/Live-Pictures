package com.safronov.livepictures.ui.composable

import androidx.compose.runtime.mutableStateListOf
import com.safronov.livepictures.udf.EffectorScope
import com.safronov.livepictures.udf.ExecutorScope
import com.safronov.livepictures.udf.UDFViewModel
import com.safronov.livepictures.ui.composable.CanvasContract.*
import com.safronov.livepictures.ui.composable.CanvasContract.State.UserInputType.*
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
                    val cachedActivePaths = mutableStateListOf<PathData>()
                    val disablePaths = mutableStateListOf<PathData>()
                    val prevFrame = state.currentFrameId - 1
                    state.activePaths.clear()

                    activePaths.addAll(state.disablePaths.filter { it.frameId == prevFrame })
                    disablePaths.addAll(state.disablePaths.filter { it.frameId != prevFrame })
                    cachedActivePaths.addAll(activePaths)

                    state.copy(
                        activePaths = activePaths,
                        disablePaths = disablePaths,
                        deleteFrameValue = if (prevFrame < 1) ColorValue(enabled = false) else state.deleteFrameValue,
                        currentFrameId = prevFrame,
                        cachedActivePaths = cachedActivePaths
                    )
                }
            }

            is Executor.AddPath -> {
                val element = PathData(
                    path = ex.path,
                    color = if (state.userInputType == PEN) ex.color else Colors.White,
                    frameId = state.currentFrameId,
                )
                state.activePaths.add(element)
                state.cachedActivePaths.add(element)
                state.copy(
                    prevActionValue = ColorValue(enabled = true)
                )
            }

            is Executor.ChangeUserAction -> {
                state.copy(
                    userInputType = ex.userInputType
                )
            }

            Executor.NextAction -> {
                val enableNextAction = state.activePaths.size < state.cachedActivePaths.size
                if (state.nextActionValue.enabled && enableNextAction) {
                    state.activePaths.add(state.cachedActivePaths[state.activePaths.size])
                    state.copy(
                        nextActionValue = ColorValue(
                            enabled = state.activePaths.size < state.cachedActivePaths.size
                        ),
                        prevActionValue = ColorValue(
                            enabled = true
                        )
                    )
                } else {
                    state
                }
            }

            Executor.PrevAction -> {
                if (state.activePaths.size >= 1) {
                    state.activePaths.removeLastOrNull()
                    state.copy(
                        nextActionValue = ColorValue(
                            enabled = true
                        ),
                        prevActionValue = ColorValue(
                            enabled = state.activePaths.size >= 1
                        )
                    )
                } else {
                    state.copy(
                        prevActionValue = ColorValue(
                            enabled = state.activePaths.size >= 1
                        )
                    )
                }
            }
        }

    override suspend fun EffectorScope<Executor>.affect(ef: Effect) {
        //TODO
    }

}