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

    // Boyutlar
    private var screenWidth = 1.0f
    private var screenHeight = 1.0f

    // Boyama araçları
    private val paint = Paint()

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
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        gameLoop.stopLoop()
    }

    fun update() {
        when (currentState) {
            GameState.MAIN_MENU -> {
                // Menü mantıksal güncellemeleri
            }
            GameState.PLAYING -> {
                // Oyun içi mantıksal güncellemeler (Karakter, Düşman vb.)
            }
            else -> {}
        }
    }

    fun drawGame(canvas: Canvas) {
        // Ekranı temizle (Koyu arka plan - Gece/Sokak temasına hazırlık)
        canvas.drawColor(Color.rgb(20, 20, 30))

        when (currentState) {
            GameState.MAIN_MENU -> drawMainMenu(canvas)
            GameState.PLAYING -> drawGameplay(canvas)
            else -> drawMainMenu(canvas)
        }
    }

    private fun drawMainMenu(canvas: Canvas) {
        // Çevresel Atmosfer / Başlık Çizimi
        paint.color = Color.rgb(240, 200, 50)
        paint.textSize = 64f
        paint.isAntiAlias = false // Piksel hissini korumak için
        canvas.drawText("STREET RAGE: FIGHTER", 100f, 150f, paint)

        // Menü Butonları Tasarımı
        paint.textSize = 36f
        paint.color = Color.WHITE
        canvas.drawText("1. HİKÂYE / BAŞLA", 100f, 280f, paint)
        canvas.drawText("2. AYARLAR", 100f, 360f, paint)
        canvas.drawText("3. ÇIKIŞ", 100f, 440f, paint)

        paint.textSize = 24f
        paint.color = Color.GRAY
        canvas.drawText("Ekran dokunarak oyunu başlatın.", 100f, screenHeight - 60f, paint)
    }

    private fun drawGameplay(canvas: Canvas) {
        paint.color = Color.WHITE
        paint.textSize = 40f
        canvas.drawText("Bölüm 1: Arka Sokaklar (Yükleniyor...)", 100f, 200f, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (currentState == GameState.MAIN_MENU) {
                // Menü dokunmasıyla oyunu başlatalım
                currentState = GameState.PLAYING
            } else if (currentState == GameState.PLAYING) {
                // İleride buraya dokunmatik kontroller eklenecek
            }
        }
        return true
    }
}
