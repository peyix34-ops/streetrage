package com.streetrage.fighter

object DialogueManager {
    fun getIntroDialogue(chapterId: Int): Pair<String, String> {
        return when (chapterId) {
            1 -> Pair("Mafya Tetikçisi", "Eşini asla geri alamayacaksın, buraya kadar!")
            5 -> Pair("Arena Şampiyonu", "Arenamda hayatta kalabileceğini mi sandın?")
            8 -> Pair("Koruma", "Burası son durak, yabancı.")
            10 -> Pair("Mafya Lideri", "Demek karımı kurtarmaya geldin... Seni ve cesaretini yok edeceğim!")
            else -> Pair("Mafya Üyesi", "Hatasız bir pusu, buradan çıkışın yok!")
        }
    }
}
