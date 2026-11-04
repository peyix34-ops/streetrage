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
        enemy = Enemy(screenWidth - 300f, screenHeight * 0.48f)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        gameLoop.stopLoop()
    }

    fun update() {
        when (currentState) {
            GameState.MAIN_MENU -> {}
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

                // Çarpışma ve Vuruş Algılama (Hitbox / Hurtbox)
                if ((player.currentState == PlayerState.PUNCH || player.currentState == PlayerState.KICK) &&
                    player.attackBox.intersect(enemy.hurtBox)) {
                    if (enemy.health > 0) {
                        enemy.health -= if (player.currentState == PlayerState.PUNCH) 5 else 8
                        if (enemy.health < 0) enemy.health = 0
                    }
                }

                // Düşmanın oyuncuya saldırı teması
                if (enemy.currentState == EnemyState.ATTACKING && enemy.attackBox.intersect(player.hurtBox)) {
                    player.health -= 1
                    if (player.health < 0) {
                        player.health = 0
                        currentState = GameState.GAME_OVER
                    }
                }
            }
            GameState.GAME_OVER, GameState.VICTORY -> {}
            else -> {}
        }
    }

    fun drawGame(canvas: Canvas) {
        canvas.drawColor(Color.rgb(20, 20, 30))

        when (currentState) {
            GameState.MAIN_MENU -> drawMainMenu(canvas)
            GameState.PLAYING -> drawGameplay(canvas)
            GameState.GAME_OVER -> drawGameOver(canvas)
            GameState.VICTORY -> drawVictory(canvas)
            else -> drawMainMenu(canvas)
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
        canvas.drawText("Oyuna başlamak için ekrana dokunun.", 80f, screenHeight - 40f, paint)
    }

    private fun drawGameplay(canvas: Canvas) {
        BackgroundRenderer.drawBackAlleysBackground(canvas, screenWidth, screenHeight, paint)

        player.draw(canvas, paint)
        enemy.draw(canvas, paint)

        touchController.draw(canvas, paint)

        // Oyuncu Can Barı
        paint.color = Color.RED
        canvas.drawRect(40f, 30f, 240f, 55f, paint)
        paint.color = Color.GREEN
        val currentHealthWidth = 200f * (player.health.toFloat() / player.maxHealth)
        canvas.drawRect(40f, 30f, 40f.coerceAtLeast(40f + currentHealthWidth), 55f, paint)

        // Düşman Can Barı
        paint.color = Color.RED
        canvas.drawRect(screenWidth - 240f, 30f, screenWidth - 40f, 55f, paint)
        paint.color = Color.rgb(200, 50, 50)
        val enemyHealthWidth = 200f * (enemy.health.toFloat() / enemy.maxHealth)
        canvas.drawRect(screenWidth - 240f, 30f, (screenWidth - 240f) + enemyHealthWidth, 55f, paint)

        paint.color = Color.WHITE
        paint.textSize = 18f
        canvas.drawText("BÖLÜM 1: ARKA SOKAKLAR", 40f, 22f, paint)
        canvas.drawText("MAFYA TETİKÇİSİ", screenWidth - 240f, 22f, paint)
    }

    private fun drawGameOver(canvas: Canvas) {
        paint.color = Color.RED
        paint.textSize = 64f
        canvas.drawText("OYUN BİTTİ", screenWidth / 2f - 160f, screenHeight / 2f, paint)
        paint.textSize = 24f
        paint.color = Color.WHITE
        canvas.drawText("Yeniden başlamak için dokun.", screenWidth / 2f - 140f, screenHeight / 2f + 50f, paint)
    }

    private fun drawVictory(canvas: Canvas) {
        paint.color = Color.GREEN
        paint.textSize = 64f
        canvas.drawText("BÖLÜM TAMAMLANDI!", screenWidth / 2f - 220f, screenHeight / 2f, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (currentState == GameState.MAIN_MENU) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentState = GameState.PLAYING
            }
        } else if (currentState == GameState.PLAYING) {
            touchController.handleTouchEvent(event, screenWidth, screenHeight)
        } else if (currentState == GameState.GAME_OVER) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                player.health = 100
                enemy.health = 60
                player.x = 100f
                enemy.x = screenWidth - 300f
                currentState = GameState.PLAYING
            }
        }
        return true
    }
}
