package com.streetrage.fighter

data class Chapter(
    val id: Int,
    val title: String,
    val enemyName: String,
    val enemyMaxHealth: Int,
    val isBoss: Boolean
)

object ChapterManager {
    val chapters = listOf(
        Chapter(1, "Bölüm 1: Arka Sokaklar", "Mafya Tetikçisi", 60, false),
        Chapter(2, "Bölüm 2: Liman", "Liman Muhafızı", 80, false),
        Chapter(3, "Bölüm 3: Kaçakçılık Deposu", "Depo Bekçisi", 100, false),
        Chapter(4, "Bölüm 4: Sanayi Bölgesi", "Kiralık Katil", 120, false),
        Chapter(5, "Bölüm 5: Yeraltı Dövüş Arenası", "Arena Şampiyonu (BOSS)", 200, true),
        Chapter(6, "Bölüm 6: Tren İstasyonu", "Sabotajcı", 140, false),
        Chapter(7, "Bölüm 7: Yağmurlu Şehir", "Sokak Çetesi Lideri", 160, false),
        Chapter(8, "Bölüm 8: İnşaat Alanı", "Ağır Siklet Koruma (Mini Boss)", 220, true),
        Chapter(9, "Bölüm 9: Mafyanın Konağı", "Özel Muhafız", 200, false),
        Chapter(10, "Bölüm 10: Mafyanın Merkezi", "MAFYA LİDERİ (FİNAL)", 300, true)
    )

    var currentChapterIndex = 0

    fun getCurrentChapter(): Chapter {
        return chapters[currentChapterIndex]
    }

    fun nextChapter(): Boolean {
        if (currentChapterIndex < chapters.size - 1) {
            currentChapterIndex++
            return true
        }
        return false
    }

    fun reset() {
        currentChapterIndex = 0
    }
}
