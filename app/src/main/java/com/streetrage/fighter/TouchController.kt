package com.streetrage.fighter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent

class TouchController {
    // Sanal buton koordinat alanları
    val btnLeft = RectF(50f, 320f, 150f, 420f)
    val btnRight = RectF(180f, 320f, 280f, 420f)
    val btnJump = RectF(115f, 200f, 215f, 300f)

    val btnPunch = RectF(720f, 320f, 820f, 420f)
    val btnKick = RectF(850f, 320f, 950f, 420f)

    var isLeftPressed = false
    var isRightPressed = false
    var isJumpPressed = false
    var isPunchPressed = false
    var isKickPressed = false

    fun handleTouchEvent(event: MotionEvent, screenWidth: Float, screenHeight: Float) {
        val action = event.actionMasked
        
        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_MOVE -> {
                // Tüm parmak dokunuşlarını tara
                isLeftPressed = false
                isRightPressed = false
                isJumpPressed = false
                isPunchPressed = false
                isKickPressed = false

                for (i in 0 until event.pointerCount) {
                    val x = event.getX(i)
                    val y = event.getY(i)
                    checkButtonPress(x, y, true)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                isLeftPressed = false
                isRightPressed = false
                isJumpPressed = false
                isPunchPressed = false
                isKickPressed = false

                for (i in 0 until event.pointerCount) {
                    // Aktif kalan parmakları kontrol et
                    val x = event.getX(i)
                    val y = event.getY(i)
                    checkButtonPress(x, y, true)
                }
            }
        }
    }

    private fun checkButtonPress(x: Float, y: Float, isPressed: Boolean) {
        if (btnLeft.contains(x, y)) isLeftPressed = isPressed
        if (btnRight.contains(x, y)) isRightPressed = isPressed
        if (btnJump.contains(x, y)) isJumpPressed = isPressed
        if (btnPunch.contains(x, y)) isPunchPressed = isPressed
        if (btnKick.contains(x, y)) isKickPressed = isPressed
    }

    fun draw(canvas: Canvas, paint: Paint) {
        paint.style = Paint.Style.FILL
        paint.textSize = 24f

        // Sol D-Pad Butonları
        paint.color = if (isLeftPressed) Color.argb(150, 220, 220, 220) else Color.argb(70, 100, 100, 100)
        canvas.drawRect(btnLeft, paint)
        paint.color = Color.WHITE
        canvas.drawText("SOL", btnLeft.left + 28f, btnLeft.centerY() + 8f, paint)

        paint.color = if (isRightPressed) Color.argb(150, 220, 220, 220) else Color.argb(70, 100, 100, 100)
        canvas.drawRect(btnRight, paint)
        paint.color = Color.WHITE
        canvas.drawText("SAĞ", btnRight.left + 25f, btnRight.centerY() + 8f, paint)

        paint.color = if (isJumpPressed) Color.argb(150, 220, 220, 220) else Color.argb(70, 100, 100, 100)
        canvas.drawRect(btnJump, paint)
        paint.color = Color.WHITE
        canvas.drawText("ZIPLA", btnJump.left + 22f, btnJump.centerY() + 8f, paint)

        // Sağ Eylem Butonları
        paint.color = if (isPunchPressed) Color.argb(150, 250, 100, 100) else Color.argb(70, 150, 40, 40)
        canvas.drawRect(btnPunch, paint)
        paint.color = Color.WHITE
        canvas.drawText("YUMRUK", btnPunch.left + 10f, btnPunch.centerY() + 8f, paint)

        paint.color = if (isKickPressed) Color.argb(150, 100, 100, 250) else Color.argb(70, 40, 40, 150)
        canvas.drawRect(btnKick, paint)
        paint.color = Color.WHITE
        canvas.drawText("TEKME", btnKick.left + 15f, btnKick.centerY() + 8f, paint)
    }
}
