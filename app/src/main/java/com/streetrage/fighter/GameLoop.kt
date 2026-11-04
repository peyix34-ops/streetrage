package com.streetrage.fighter

import android.graphics.Canvas
import android.view.SurfaceHolder

class GameLoop(
    private val surfaceHolder: SurfaceHolder,
    private val gameSurfaceView: GameSurfaceView
) : Thread() {

    private var isRunning = false
    private val targetFPS = 60.0
    private val timePerFrame = 1000.0 / targetFPS

    fun startLoop() {
        isRunning = true
        start()
    }

    fun stopLoop() {
        var retry = true
        isRunning = false
        while (retry) {
            try {
                join()
                retry = false
            } catch (e: InterruptedException) {
                // Tekrar dene
            }
        }
    }

    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        var totalTime = 0L
        var frameCount = 0L
        val targetTime = timePerFrame.toLong()

        while (isRunning) {
            startTime = System.nanoTime()
            var canvas: Canvas? = null

            try {
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    if (canvas != null) {
                        gameSurfaceView.update()
                        gameSurfaceView.drawGame(canvas)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis

            if (waitTime > 0) {
                try {
                    sleep(waitTime)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            totalTime += System.nanoTime() - startTime
            frameCount++
            if (frameCount == targetFPS.toLong()) {
                // Ortalama FPS loglanabilir veya performans takibi yapılabilir
                frameCount = 0
                totalTime = 0
            }
        }
    }
}
