package com.streetrage.fighter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class Enemy(initialX: Float, initialY: Float, val enemyType: String = "thug") {

    var x = initialX
    var y = initialY
    var health = 60
    var maxHealth = 60
    var currentState = EnemyState.IDLE
    var isFacingRight = false

    private val scale = 6f
    private val spriteBitmap: Bitmap = PixelSpriteGenerator.createEnemyBitmap()
    
    val hurtBox = RectF()
    val attackBox = RectF()

    init {
        updateBounds()
    }

    fun update(playerX: Float, screenWidth: Float) {
        if (health <= 0) {
            currentState = EnemyState.DEAD
            return
        }

        val distanceToPlayer = playerX - x

        if (kotlin.math.abs(distanceToPlayer) > 45f) {
            currentState = EnemyState.CHASING
            isFacingRight = distanceToPlayer > 0
            val speed = if (isFacingRight) 2.2f else -2.2f
            x += speed
        } else {
            currentState = EnemyState.ATTACKING
        }

        // Ekran sınırlarında tutma
        if (x < 0f) x = 0f
        if (x > screenWidth - (spriteBitmap.width * scale)) {
            x = screenWidth - (spriteBitmap.width * scale)
        }

        updateBounds()
    }

    private fun updateBounds() {
        val width = spriteBitmap.width * scale
        val height = spriteBitmap.height * scale
        hurtBox.set(x, y, x + width, y + height)

        if (currentState == EnemyState.ATTACKING) {
            if (isFacingRight) {
                attackBox.set(x + width, y + height * 0.3f, x + width + 35f, y + height * 0.7f)
            } else {
                attackBox.set(x - 35f, y + height * 0.3f, x, y + height * 0.7f)
            }
        } else {
            attackBox.set(0f, 0f, 0f, 0f)
        }
    }

    fun draw(canvas: Canvas, paint: Paint) {
        if (currentState == EnemyState.DEAD) return

        paint.isAntiAlias = false
        paint.isFilterBitmap = false

        val destWidth = spriteBitmap.width * scale
        val destHeight = spriteBitmap.height * scale

        canvas.save()
        if (!isFacingRight) {
            canvas.scale(-1f, 1f, x + destWidth / 2, y + destHeight / 2)
        }

        val destRect = RectF(x, y, x + destWidth, y + destHeight)
        canvas.drawBitmap(spriteBitmap, null, destRect, paint)
        canvas.restore()
    }
}
