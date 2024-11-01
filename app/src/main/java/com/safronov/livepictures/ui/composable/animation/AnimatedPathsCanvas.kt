package com.safronov.livepictures.ui.composable.animation

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.Region
import android.view.animation.LinearInterpolator
import androidx.compose.ui.graphics.asAndroidPath

class PathAnimator(
    private val paths: List<Path>,
    private val duration: Long = 2000L // Длительность анимации в миллисекундах
) {
    private var currentProgress: Float = 0f
    private val boundsList = mutableListOf<Rect>()
    private var _paths: List<Path> = paths.map { it }

    init {
        paths.map { it }.forEach { path ->
            val region = Region()
            region.setPath(path, Region(0, 0, Int.MAX_VALUE, Int.MAX_VALUE))
            boundsList.add(region.bounds)
        }
    }

    // Функция для запуска анимации
    fun startAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            this.duration = duration
            interpolator = LinearInterpolator()
            addUpdateListener {
                currentProgress = it.animatedValue as Float
            }
        }
        animator.start()
    }

    fun draw(canvas: Canvas, paint: Paint) {
        paths.forEachIndexed { index, path ->
            val bounds = boundsList[index]
            canvas.save()

            canvas.scale(currentProgress, currentProgress, bounds.centerX().toFloat(), bounds.centerY().toFloat())

            canvas.drawPath(path, paint)
            canvas.restore()
        }
    }
}


