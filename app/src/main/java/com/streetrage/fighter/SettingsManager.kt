package com.streetrage.fighter

enum class Difficulty(val label: String, val damageMultiplier: Float) {
    EASY("Kolay", 0.7f),
    NORMAL("Normal", 1.0f),
    HARD("Zor", 1.5f)
}

object SettingsManager {
    var currentDifficulty = Difficulty.NORMAL
    var soundEnabled = true
    var musicEnabled = true

    fun cycleDifficulty() {
        currentDifficulty = when (currentDifficulty) {
            Difficulty.EASY -> Difficulty.NORMAL
            Difficulty.NORMAL -> Difficulty.HARD
            Difficulty.HARD -> Difficulty.EASY
        }
    }

    fun toggleSound() {
        soundEnabled = !soundEnabled
    }

    fun toggleMusic() {
        musicEnabled = !musicEnabled
    }
}
