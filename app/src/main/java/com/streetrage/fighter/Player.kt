package com.streetrage.fighter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class Player(initialX: Float, initialY: Float) {

    var x = initialX
    var y = initialY
    var health = 100
    var maxHealth = 100
    var currentState = PlayerState.IDLE
    var isFacingRight = true

    // Çizim ölçeği (Piksel sanatını ekranda büyütmek için)
    private val scale = 6f
    private val idleBitmap: Bitmap = PixelSpriteGenerator.createHeroIdleBitmap()
    
    // Çarpışma kutuları (Hitbox & Hurtbox)
    val hurtBox = RectF()

    init {
        updateBounds()
    }

    fun update() {
        updateBounds()
    }

    private fun updateBounds() {
        val width = idleBitmap.width * scale
        val height = idleBitmap.height * scale
        hurtBox.set(x, y, x + width, y + height)
    }

    fun draw(canvas: Canvas, paint: Paint) {
        // Nearest Neighbor için filter'ı kapatıyoruz (keskin piksel görünümü için)
        paint.isAntiAlias = false
        paint.isFilterBitmap = false

        val destWidth = idleBitmap.width * scale
        val destHeight = idleBitmap.height * scale

        canvas.save()
        if (!isFacingRight) {
            // Yön sola bakıyorsa sprite'ı yatayda ters çevir
            canvas.scale(-1f, 1f, x + destWidth / 2, y + destHeight / 2)
        }

        val destRect = RectF(x, y, x + destWidth, y + destHeight)
        canvas.drawBitmap(idleBitmap, null, destRect, paint)
        canvas.restore()
    }

    fun move(dx: Float, screenWidth: Float) {
        x += dx
        // Ekran sınırlarında kalmasını sağla
        if (x < 0f) x = 0f
        if (x > screenWidth - (idleBitmap.width * scale)) {
            x = screenWidth - (idleBitmap.width * scale)
        }
        currentState = if (dx != 0f) PlayerState.WALKING else PlayerState.IDLE
    }
}
