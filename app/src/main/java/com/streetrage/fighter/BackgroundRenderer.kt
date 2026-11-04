package com.streetrage.fighter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

object BackgroundRenderer {

    // Bölüm 1: Arka Sokaklar Atmosferi
    fun drawBackAlleysBackground(canvas: Canvas, width: Float, height: Float, paint: Paint) {
        paint.style = Paint.Style.FILL

        // Akşam / Mafya Gökyüzü Teması
        paint.color = Color.rgb(35, 20, 45)
        canvas.drawRect(0f, 0f, width, height * 0.65f, paint)

        // Uzak Binalar / Siluetler
        paint.color = Color.rgb(20, 12, 25)
        canvas.drawRect(40f, height * 0.30f, 220f, height * 0.65f, paint)
        canvas.drawRect(250f, height * 0.20f, 480f, height * 0.65f, paint)
        canvas.drawRect(520f, height * 0.35f, 720f, height * 0.65f, paint)
        canvas.drawRect(750f, height * 0.25f, width - 40f, height * 0.65f, paint)

        // Zemin (Asfalt / Sokak)
        paint.color = Color.rgb(45, 45, 55)
        canvas.drawRect(0f, height * 0.65f, width, height, paint)

        // Yol Çizgileri / Kaldırım Kenarı Detayı
        paint.color = Color.rgb(90, 90, 105)
        canvas.drawRect(0f, height * 0.65f, width, height * 0.68f, paint)
    }
}
