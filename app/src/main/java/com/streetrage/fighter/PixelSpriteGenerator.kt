package com.streetrage.fighter

import android.graphics.Bitmap
import android.graphics.Color

object PixelSpriteGenerator {

    fun createHeroIdleBitmap(): Bitmap {
        val width = 16
        val height = 24
        val pixels = IntArray(width * height) { Color.TRANSPARENT }

        val skin = Color.rgb(224, 172, 105)
        val hair = Color.rgb(40, 40, 40)
        val jacket = Color.rgb(180, 40, 40)
        val pants = Color.rgb(40, 50, 120)
        val boots = Color.rgb(60, 60, 60)
        val eye = Color.rgb(20, 20, 20)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = y * width + x
                when {
                    y in 2..5 && x in 4..11 -> pixels[index] = hair
                    y in 6..9 && x in 5..10 -> {
                        if ((y == 7) && (x == 6 || x == 9)) pixels[index] = eye
                        else pixels[index] = skin
                    }
                    y in 10..15 && x in 3..12 -> {
                        if (x == 3 || x == 12) pixels[index] = Color.rgb(120, 20, 20)
                        else pixels[index] = jacket
                    }
                    y in 16..19 && x in 4..11 -> {
                        if (x == 7 || x == 8) pixels[index] = Color.rgb(20, 30, 80)
                        else pixels[index] = pants
                    }
                    y in 20..22 && (x in 3..6 || x in 9..12) -> pixels[index] = boots
                }
            }
        }

        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
    }

    fun createEnemyBitmap(): Bitmap {
        val width = 16
        val height = 24
        val pixels = IntArray(width * height) { Color.TRANSPARENT }

        val skin = Color.rgb(200, 150, 100)
        val mafiaHair = Color.rgb(20, 20, 20)
        val suit = Color.rgb(50, 50, 60)
        val tie = Color.rgb(200, 30, 30)
        val shoes = Color.rgb(20, 20, 20)
        val eye = Color.rgb(230, 40, 40)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = y * width + x
                when {
                    y in 2..5 && x in 4..11 -> pixels[index] = mafiaHair
                    y in 6..9 && x in 5..10 -> {
                        if ((y == 7) && (x == 6 || x == 9)) pixels[index] = eye
                        else pixels[index] = skin
                    }
                    y in 10..15 && x in 3..12 -> {
                        if (x == 7 || x == 8) pixels[index] = tie
                        else pixels[index] = suit
                    }
                    y in 16..19 && x in 4..11 -> pixels[index] = suit
                    y in 20..22 && (x in 3..6 || x in 9..12) -> pixels[index] = shoes
                }
            }
        }

        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
    }
}
