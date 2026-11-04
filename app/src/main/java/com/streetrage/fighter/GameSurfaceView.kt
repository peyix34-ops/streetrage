package com.streetrage.fighter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameSurfaceView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private val gameLoop: GameLoop
    private var currentState = GameState.MAIN_MENU

    private var screenWidth = 1.0f
    private var screenHeight = 1.0f

    private val paint = Paint()

    // Oyun Bileşenleri
    private lateinit var player: Player
    private val touchController = TouchController()

    init {
        holder.addCallback(this)
        gameLoop = GameLoop(holder, this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (!gameLoop.isAlive) {
            gameLoop.startLoop()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        screenWidth = width.toFloat()
        screenHeight = height.toFloat()

        // Oyuncuyu zemin seviyesine konumlandır
        player = Player(100f, screenHeight * 0.48f)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        gameLoop.stopLoop()
    }

    fun update() {
        when (currentState) {
            GameState.MAIN_MENU -> {}
            GameState.PLAYING -> {
                var dx = 0f
                if (touchController.isLeftPressed) {
                    dx = -6f
                    player.isFacingRight = false
                }
                if (touchController.isRightPressed) {
                    dx = 6f
                    player.isFacingRight = true
                }
                player.move(dx, screenWidth)
                player.update()
            }
            else -> {}
        }
    }

    fun drawGame(canvas: Canvas) {
        canvas.drawColor(Color.rgb(20, 20, 30))

        when (currentState) {
            GameState.MAIN_MENU -> drawMainMenu(canvas)
            GameState.PLAYING -> drawGameplay(canvas)
            else -> drawMainMenu(canvas)
        }
    }

    private fun drawMainMenu(canvas: Canvas) {
        paint.color = Color.rgb(240, 200, 50)
        paint.textSize = 56f
        paint.isAntiAlias = false
        canvas.drawText("STREET RAGE: FIGHTER", 80f, 130f, paint)

        paint.textSize = 32f
        paint.color = Color.WHITE
        canvas.drawText("1. HİKÂYE / BAŞLA", 80f, 240f, paint)
        canvas.drawText("2. AYARLAR", 80f, 310f, paint)
        canvas.drawText("3. ÇIKIŞ", 80f, 380f, paint)

        paint.textSize = 20f
        paint.color = Color.GRAY
        canvas.drawText("Oyuna başlamak için ekrana dokunun.", 80f, screenHeight - 40f, paint)
    }

    private fun drawGameplay(canvas: Canvas) {
        // Arka plan atmosferi
        BackgroundRenderer.drawBackAlleysBackground(canvas, screenWidth, screenHeight, paint)

        // Oyuncu karakteri
        player.draw(canvas, paint)

        // Dokunmatik Kontroller (HUD)
        touchController.draw(canvas, paint)

        // Oyuncu Sağlık Göstergesi (Can Barı)
        paint.color = Color.RED
        canvas.drawRect(40f, 30f, 240f, 55f, paint)
        paint.color = Color.GREEN
        val currentHealthWidth = 200f * (player.health.toFloat() / player.maxHealth)
        canvas.drawRect(40f, 30f, 40f + currentHealthWidth, 55f, paint)

        paint.color = Color.WHITE
        paint.textSize = 18f
        canvas.drawText("BÖLÜM 1: ARKA SOKAKLAR", 40f, 22f, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (currentState == GameState.MAIN_MENU) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentState = GameState.PLAYING
            }
        } else if (currentState == GameState.PLAYING) {
            touchController.handleTouchEvent(event, screenWidth, screenHeight)
        }
        return true
    }
}
