package com.streetrage.fighter

import android.graphics.Bitmap
import android.graphics.Color

object PixelSpriteGenerator {

    // Piksel sanat karakterimiz için 16x24 piksel matris tabanlı örnek bir sprite üretici (Nearest Neighbor uyumlu)
    fun createHeroIdleBitmap(): Bitmap {
        val width = 16
        val height = 24
        val pixels = IntArray(width * height) { Color.TRANSPARENT }

        // Renk paleti
        val skin = Color.rgb(224, 172, 105)
        val hair = Color.rgb(40, 40, 40)
        val jacket = Color.rgb(180, 40, 40)
        val pants = Color.rgb(40, 50, 120)
        val boots = Color.rgb(60, 60, 60)
        val eye = Color.rgb(20, 20, 20)

        // Basit örnek piksel çizim matrisi (Idle duruşu)
        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = y * width + x
                when {
                    // Saç (y: 2-5, x: 4-11)
                    y in 2..5 && x in 4..11 -> pixels[index] = hair
                    // Yüz (y: 6-9, x: 5-10)
                    y in 6..9 && x in 5..10 -> {
                        if ((y == 7) && (x == 6 || x == 9)) pixels[index] = eye
                        else pixels[index] = skin
                    }
                    // Ceket / Gövde (y: 10-15, x: 3-12)
                    y in 10..15 && x in 3..12 -> {
                        if (x == 3 || x == 12) pixels[index] = Color.rgb(120, 20, 20) // Ceket kenarı
                        else pixels[index] = jacket
                    }
                    // Pantolon (y: 16-19, x: 4-11)
                    y in 16..19 && x in 4..11 -> {
                        if (x == 7 || x == 8) pixels[index] = Color.rgb(20, 30, 80) // İç çizgi
                        else pixels[index] = pants
                    }
                    // Botlar (y: 20-22, x: 3-12)
                    y in 20..22 && (x in 3..6 || x in 9..12) -> pixels[index] = boots
                }
            }
        }

        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
    }
}
