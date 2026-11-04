package com.streetrage.fighter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameSurfaceView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private val gameLoop: GameLoop
    private var currentState = GameState.MAIN_MENU

    private var screenWidth = 1.0f
    private var screenHeight = 1.0f

    private val paint = Paint()

    private lateinit var player: Player
    private lateinit var enemy: Enemy
    private val touchController = TouchController()

    init {
        holder.addCallback(this)
        gameLoop = GameLoop(holder, this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (!gameLoop.isAlive) {
            gameLoop.startLoop()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        screenWidth = width.toFloat()
        screenHeight = height.toFloat()

        player = Player(100f, screenHeight * 0.48f)
        initNewChapter()
    }

    private fun initNewChapter() {
        val chapter = ChapterManager.getCurrentChapter()
        enemy = Enemy(screenWidth - 300f, screenHeight * 0.48f)
        enemy.applyDifficultyScaling(chapter.enemyMaxHealth)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        gameLoop.stopLoop()
    }

    fun update() {
        when (currentState) {
            GameState.MAIN_MENU, GameState.SETTINGS -> {}
            GameState.PLAYING -> {
                if (touchController.isPunchPressed) {
                    player.punch()
                }
                if (touchController.isKickPressed) {
                    player.kick()
                }

                var dx = 0f
                if (touchController.isLeftPressed) {
                    dx = -6f
                    player.isFacingRight = false
                }
                if (touchController.isRightPressed) {
                    dx = 6f
                    player.isFacingRight = true
                }

                player.move(dx, screenWidth)
                player.update()

                enemy.update(player.x, screenWidth)

                if ((player.currentState == PlayerState.PUNCH || player.currentState == PlayerState.KICK) &&
                    player.attackBox.intersect(enemy.hurtBox)) {
                    if (enemy.health > 0) {
                        enemy.health -= if (player.currentState == PlayerState.PUNCH) 5 else 8
                        if (enemy.health <= 0) {
                            enemy.health = 0
                            if (ChapterManager.currentChapterIndex == ChapterManager.chapters.size - 1) {
                                currentState = GameState.VICTORY
                            } else {
                                if (ChapterManager.nextChapter()) {
                                    initNewChapter()
                                } else {
                                    currentState = GameState.VICTORY
                                }
                            }
                        }
                    }
                }

                if (enemy.currentState == EnemyState.ATTACKING && enemy.attackBox.intersect(player.hurtBox)) {
                    val damage = when(SettingsManager.currentDifficulty) {
                        Difficulty.EASY -> 1
                        Difficulty.NORMAL -> 1
                        Difficulty.HARD -> 2
                    }
                    player.health -= damage
                    if (player.health < 0) {
                        player.health = 0
                        currentState = GameState.GAME_OVER
                    }
                }
            }
            GameState.GAME_OVER, GameState.VICTORY -> {}
        }
    }

    fun drawGame(canvas: Canvas) {
        canvas.drawColor(Color.rgb(20, 20, 30))

        when (currentState) {
            GameState.MAIN_MENU -> drawMainMenu(canvas)
            GameState.SETTINGS -> drawSettingsMenu(canvas)
            GameState.PLAYING -> drawGameplay(canvas)
            GameState.GAME_OVER -> drawGameOver(canvas)
            GameState.VICTORY -> drawVictory(canvas)
        }
    }

    private fun drawMainMenu(canvas: Canvas) {
        paint.color = Color.rgb(240, 200, 50)
        paint.textSize = 56f
        paint.isAntiAlias = false
        canvas.drawText("STREET RAGE: FIGHTER", 80f, 130f, paint)

        paint.textSize = 32f
        paint.color = Color.WHITE
        canvas.drawText("1. HİKÂYE / BAŞLA", 80f, 240f, paint)
        canvas.drawText("2. AYARLAR", 80f, 310f, paint)
        canvas.drawText("3. ÇIKIŞ", 80f, 380f, paint)

        paint.textSize = 20f
        paint.color = Color.GRAY
        canvas.drawText("Seçenekler için ilgili yazıya dokunun.", 80f, screenHeight - 40f, paint)
    }

    private fun drawSettingsMenu(canvas: Canvas) {
        paint.color = Color.rgb(240, 200, 50)
        paint.textSize = 48f
        paint.isAntiAlias = false
        canvas.drawText("OYUN AYARLARI", 80f, 100f, paint)

        paint.textSize = 28f
        paint.color = Color.WHITE
        canvas.drawText("Zorluk Seviyesi: [ ${SettingsManager.currentDifficulty.label} ] (Değiştirmek için dokun)", 80f, 200f, paint)
        canvas.drawText("Ses Efektleri: [ ${if (SettingsManager.soundEnabled) "AÇIK" else "KAPALI"} ]", 80f, 260f, paint)
        canvas.drawText("Müzik: [ ${if (SettingsManager.musicEnabled) "AÇIK" else "KAPALI"} ]", 80f, 320f, paint)

        paint.color = Color.rgb(180, 50, 50)
        canvas.drawText("< ANA MENÜYE DÖN", 80f, 420f, paint)
    }

    private fun drawGameplay(canvas: Canvas) {
        BackgroundRenderer.drawBackAlleysBackground(canvas, screenWidth, screenHeight, paint)

        player.draw(canvas, paint)
        enemy.draw(canvas, paint)

        touchController.draw(canvas, paint)

        paint.color = Color.RED
        canvas.drawRect(40f, 30f, 240f, 55f, paint)
        paint.color = Color.GREEN
        val currentHealthWidth = 200f * (player.health.toFloat() / player.maxHealth)
        canvas.drawRect(40f, 30f, 40f.coerceAtLeast(40f + currentHealthWidth), 55f, paint)

        val chapter = ChapterManager.getCurrentChapter()
        paint.color = Color.RED
        canvas.drawRect(screenWidth - 240f, 30f, screenWidth - 40f, 55f, paint)
        paint.color = Color.rgb(200, 50, 50)
        val enemyHealthWidth = 200f * (enemy.health.toFloat() / enemy.maxHealth)
        canvas.drawRect(screenWidth - 240f, 30f, (screenWidth - 240f) + enemyHealthWidth, 55f, paint)

        paint.color = Color.WHITE
        paint.textSize = 18f
        canvas.drawText(chapter.title, 40f, 22f, paint)
        canvas.drawText(chapter.enemyName, screenWidth - 240f, 22f, paint)

        val dialogue = DialogueManager.getIntroDialogue(chapter.id)
        paint.color = Color.argb(200, 0, 0, 0)
        canvas.drawRect(40f, screenHeight - 120f, screenWidth - 40f, screenHeight - 60f, paint)
        paint.color = Color.YELLOW
        paint.textSize = 16f
        canvas.drawText("${dialogue.first}: \"${dialogue.second}\"", 60f, screenHeight - 85f, paint)
    }

    private fun drawGameOver(canvas: Canvas) {
        paint.color = Color.RED
        paint.textSize = 64f
        canvas.drawText("OYUN BİTTİ", screenWidth / 2f - 160f, screenHeight / 2f, paint)
        paint.textSize = 24f
        paint.color = Color.WHITE
        canvas.drawText("Yeniden başlamak için ekrana dokunun.", screenWidth / 2f - 180f, screenHeight / 2f + 50f, paint)
    }

    private fun drawVictory(canvas: Canvas) {
        paint.color = Color.GREEN
        paint.textSize = 56f
        canvas.drawText("TEBRİKLER! EŞİNİZİ KURTARDINIZ!", screenWidth / 2f - 340f, screenHeight / 2f - 20f, paint)
        paint.textSize = 22f
        paint.color = Color.WHITE
        canvas.drawText("Mafya çökertildi. Oyunu yeniden başlatmak için dokun.", screenWidth / 2f - 240f, screenHeight / 2f + 40f, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y

            when (currentState) {
                GameState.MAIN_MENU -> {
                    if (y in 210f..250f) {
                        ChapterManager.reset()
                        player.health = 100
                        player.x = 100f
                        initNewChapter()
                        currentState = GameState.PLAYING
                    } else if (y in 280f..320f) {
                        currentState = GameState.SETTINGS
                    } else if (y in 350f..390f) {
                        (context as? android.app.Activity)?.finish()
                    }
                }
                GameState.SETTINGS -> {
                    if (y in 170f..210f) {
                        SettingsManager.cycleDifficulty()
                    } else if (y in 230f..270f) {
                        SettingsManager.toggleSound()
                    } else if (y in 290f..330f) {
                        SettingsManager.toggleMusic()
                    } else if (y in 390f..430f) {
                        currentState = GameState.MAIN_MENU
                    }
                }
                GameState.PLAYING -> {
                    touchController.handleTouchEvent(event, screenWidth, screenHeight)
                }
                GameState.GAME_OVER, GameState.VICTORY -> {
                    ChapterManager.reset()
                    player.health = 100
                    player.x = 100f
                    initNewChapter()
                    currentState = GameState.PLAYING
                }
            }
        } else if (currentState == GameState.PLAYING) {
            touchController.handleTouchEvent(event, screenWidth, screenHeight)
        }
        return true
    }
}
