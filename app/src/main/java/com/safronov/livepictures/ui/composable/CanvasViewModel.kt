package com.safronov.livepictures.ui.composable

import androidx.compose.runtime.mutableStateListOf
import com.safronov.livepictures.udf.EffectorScope
import com.safronov.livepictures.udf.ExecutorScope
import com.safronov.livepictures.udf.UDFViewModel
import com.safronov.livepictures.ui.composable.CanvasContract.*
import com.safronov.livepictures.ui.theme.ColorValue
import com.safronov.livepictures.ui.theme.Colors
import java.util.Stack

class CanvasViewModel : UDFViewModel<State, Executor, Effect, Event>(
    initState = State()
) {
    override suspend fun ExecutorScope<Effect>.execute(ex: Executor): State =
        when (ex) {
            Executor.OnAddFrame -> {
                val disablePaths = Stack<PathData>().apply {
                    addAll(state.activePaths + state.disablePaths)
                }
                val activePaths = mutableStateListOf<PathData>()

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
                    val cachedActivePaths = Stack<PathData>()
                    val disablePaths = Stack<PathData>()
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
                if (state.userInputType == State.UserInputType.ERASE && state.activePaths.isEmpty()) {
                    state
                } else {
                    val element = PathData(
                        path = ex.path,
                        color = if (state.userInputType == State.UserInputType.PEN) ex.color else Colors.White,
                        frameId = state.currentFrameId
                    )

                    state.activePaths.add(element)
                    state.cachedActivePaths.add(index = state.activePaths.size - 1, element)

                    state.copy(
                        prevActionValue = ColorValue(enabled = true)
                    )
                }
            }

            is Executor.ChangeUserAction -> {
                state.copy(
                    userInputType = ex.userInputType
                )
            }

            Executor.NextAction -> {
                if (state.redoStack.isNotEmpty()) {
                    val pathToRedo = state.redoStack.pop()
                    state.activePaths.add(pathToRedo)

                    state.undoStack.push(pathToRedo)

                    state.copy(
                        nextActionValue = ColorValue(
                            enabled = state.redoStack.isNotEmpty()
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
                if (state.activePaths.isNotEmpty()) {
                    val pathToUndo = state.activePaths.removeLastOrNull()
                    if (pathToUndo != null) {
                        state.redoStack.push(pathToUndo)
                    }

                    state.copy(
                        nextActionValue = ColorValue(
                            enabled = state.redoStack.isNotEmpty()
                        ),
                        prevActionValue = ColorValue(
                            enabled = state.activePaths.isNotEmpty()
                        )
                    )
                } else {
                    state.copy(
                        prevActionValue = ColorValue(enabled = false)
                    )
                }
            }

        }

    override suspend fun EffectorScope<Executor>.affect(ef: Effect) {
        //TODO
    }

}