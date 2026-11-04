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

    private val scale = 6f
    private val idleBitmap: Bitmap = PixelSpriteGenerator.createHeroIdleBitmap()
    
    val hurtBox = RectF()
    val attackBox = RectF()

    private var actionTimer = 0

    init {
        updateBounds()
    }

    fun update() {
        if (currentState == PlayerState.PUNCH || currentState == PlayerState.KICK) {
            actionTimer--
            if (actionTimer <= 0) {
                currentState = PlayerState.IDLE
            }
        }
        updateBounds()
    }

    private fun updateBounds() {
        val width = idleBitmap.width * scale
        val height = idleBitmap.height * scale
        hurtBox.set(x, y, x + width, y + height)

        if (currentState == PlayerState.PUNCH || currentState == PlayerState.KICK) {
            if (isFacingRight) {
                attackBox.set(x + width, y + height * 0.3f, x + width + 40f, y + height * 0.7f)
            } else {
                attackBox.set(x - 40f, y + height * 0.3f, x, y + height * 0.7f)
            }
        } else {
            attackBox.set(0f, 0f, 0f, 0f)
        }
    }

    fun punch() {
        if (currentState != PlayerState.PUNCH && currentState != PlayerState.KICK) {
            currentState = PlayerState.PUNCH
            actionTimer = 15
        }
    }

    fun kick() {
        if (currentState != PlayerState.PUNCH && currentState != PlayerState.KICK) {
            currentState = PlayerState.KICK
            actionTimer = 20
        }
    }

    fun draw(canvas: Canvas, paint: Paint) {
        paint.isAntiAlias = false
        paint.isFilterBitmap = false

        val destWidth = idleBitmap.width * scale
        val destHeight = idleBitmap.height * scale

        canvas.save()
        if (!isFacingRight) {
            canvas.scale(-1f, 1f, x + destWidth / 2, y + destHeight / 2)
        }

        val destRect = RectF(x, y, x + destWidth, y + destHeight)
        canvas.drawBitmap(idleBitmap, null, destRect, paint)
        canvas.restore()
    }

    fun move(dx: Float, screenWidth: Float) {
        if (currentState == PlayerState.PUNCH || currentState == PlayerState.KICK) return
        
        x += dx
        if (x < 0f) x = 0f
        if (x > screenWidth - (idleBitmap.width * scale)) {
            x = screenWidth - (idleBitmap.width * scale)
        }
        currentState = if (dx != 0f) PlayerState.WALKING else PlayerState.IDLE
    }
}
