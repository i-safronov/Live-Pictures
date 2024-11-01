package com.safronov.livepictures.ui.composable

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.safronov.livepictures.udf.DispatchersList
import com.safronov.livepictures.udf.EffectorScope
import com.safronov.livepictures.udf.ExecutorScope
import com.safronov.livepictures.udf.UDFViewModel
import com.safronov.livepictures.ui.composable.CanvasContract.*
import com.safronov.livepictures.ui.theme.ColorValue
import com.safronov.livepictures.ui.theme.Colors
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Stack

class CanvasViewModel : UDFViewModel<State, Executor, Effect, Event>(
    initState = State()
) {
    var animationJob: Job? = null

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
                    deleteFrameValue = ColorValue(enabled = true),
                    prevActionValue = ColorValue(enabled = false),
                    nextActionValue = ColorValue(enabled = false),
                    startAnimationValue = ColorValue(enabled = state.activePaths.isNotEmpty() || state.disablePaths.isNotEmpty())
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

                    val t = element.path
                    Log.d("ANIM_PATH", "PATHVM: ${t.getBounds()}")
                    state.activePaths.add(element)
                    state.cachedActivePaths.add(index = state.activePaths.size - 1, element)

                    state.copy(
                        prevActionValue = ColorValue(enabled = true),
                        startAnimationValue = ColorValue(enabled = true)
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
                        ),
                        startAnimationValue = ColorValue(enabled = true)
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
                        ),
                        startAnimationValue = ColorValue(
                            enabled = state.activePaths.isNotEmpty() || state.disablePaths.isNotEmpty()
                        )
                    )
                } else {
                    state.copy(
                        prevActionValue = ColorValue(enabled = false),
                        startAnimationValue = ColorValue(enabled = false)
                    )
                }
            }

            Executor.MakeAnimation -> {
                sendEffect(
                    Effect.PrepareToAnimate(
                        activePaths = state.activePaths,
                        disablePaths = state.disablePaths,
                        lastFrameId = state.currentFrameId
                    )
                )
                sendEvent(
                    Event.Animate
                )
                state.copy(
                    isLoadingAnimation = true,
                    startAnimationValue = ColorValue(enabled = false),
                    stopAnimationValue = ColorValue(enabled = true)
                )
            }

            is Executor.Animate -> {
                sendEvent(
                    Event.Animate
                )
                state.copy(
                    isLoadingAnimation = false,
                    animation = ex.animation,
                    startAnimationValue = ColorValue(enabled = false),
                    stopAnimationValue = ColorValue(enabled = false)
                )
            }

            Executor.DismissAnimation -> {
                sendEvent(Event.DismissAnimation)
                animationJob?.cancel()
                state.copy(
                    isLoadingAnimation = false,
                    animation = emptyList(),
                    startAnimationValue = ColorValue(enabled = true),
                    stopAnimationValue = ColorValue(enabled = false)
                )
            }
        }

    override suspend fun EffectorScope<Executor>.affect(ef: Effect) = when (ef) {
        is Effect.PrepareToAnimate -> {
            animationJob = viewModelScope.launch(DispatchersList.Base().io()) {
                val reversedPaths: List<PathData> =
                    ef.activePaths + ef.disablePaths
                val groupByFrame = reversedPaths.groupBy { it.frameId }
                val animation = mutableListOf<PathData>()

                for (i in 0..ef.lastFrameId) {
                    animation.addAll(
                        groupByFrame.getOrElse(key = i, defaultValue = {
                            emptyList()
                        })
                    )
                }

                dispatch(
                    Executor.Animate(
                        animation = animation
                    )
                )
            }
        }
    }

}