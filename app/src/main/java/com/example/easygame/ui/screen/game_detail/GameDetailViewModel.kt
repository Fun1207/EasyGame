package com.example.easygame.ui.screen.game_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easygame.domain.model.GameObject
import com.example.easygame.domain.model.GameObjectType
import com.example.easygame.data.repository.GameSensorManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameDetailViewModel(val gameSensorManager: GameSensorManager) : ViewModel() {

    var basketX by mutableFloatStateOf(0.5f)
        private set
    var gameObjectList by mutableStateOf(listOf<GameObject>())
        private set

    var score by mutableIntStateOf(0)
        private set
    var coin by mutableIntStateOf(0)
        private set
    var heart by mutableIntStateOf(MAX_HEART_VALUE)
        private set
    var isGamePaused by mutableStateOf(false)
        private set
    var isGameOver by mutableStateOf(false)
        private set
    private var hitBoxSize by mutableFloatStateOf(0f)
    private var speedLevel by mutableFloatStateOf(1f)

    init {
        moveBasket()
        moveAppleAndCalculateScore()
        generateGameObject()
    }

    override fun onCleared() {
        gameSensorManager.stopListening()
        isGameOver = true
        super.onCleared()
    }

    private fun moveBasket() {
        gameSensorManager.startListening()
        viewModelScope.launch {
            gameSensorManager.tiltData.collect { coordinate ->
                basketX = (basketX - coordinate * 0.005f).coerceIn(0f, 1f)
            }
        }
    }

    private fun generateGameObject() = viewModelScope.launch {
        while (!isGameOver) {
            if (!isGamePaused && gameObjectList.size < APPLES_MAX_SIZE) {
                val rate = Random.nextInt(0, 100)
                val gameObjectType = when {
                    (rate <= BOMB_SPAWN_RATE) -> GameObjectType.BOMB
                    (rate < BOMB_SPAWN_RATE + COIN_SPAWN_RATE) -> GameObjectType.COIN
                    else -> GameObjectType.APPLE
                }
                val newApple = GameObject(
                    x = Random.nextFloat(),
                    y = -0.1f,
                    gameObjectType = gameObjectType
                )
                gameObjectList = gameObjectList + newApple
                delay(Random.nextLong(50, 800))
            } else delay(FPS_FRAME_RATE_DELAY)
        }
    }

    private fun moveAppleAndCalculateScore() = viewModelScope.launch {
        while (!isGameOver) {
            if (!isGamePaused) {
                val currentApples = gameObjectList.map { apple ->
                    apple.copy(y = apple.y + speedLevel * APPLE_SPEED)
                }
                gameObjectList = currentApples.filter { apple ->
                    if (apple.y < 1f - hitBoxSize * 0.9f) {
                        return@filter true
                    }
                    if (apple.x >= basketX - hitBoxSize && apple.x <= basketX + hitBoxSize && apple.y < 1f) {
                        when (apple.gameObjectType) {
                            GameObjectType.BOMB -> decreaseHeart()
                            GameObjectType.COIN -> coin += 10
                            GameObjectType.APPLE -> {
                                score += 1
                                if (score.mod(10) == 0 && speedLevel < SPEED_LEVEL_MAX) speedLevel += 0.5f
                            }
                        }
                        return@filter false
                    }
                    apple.y < 1.1f
                }
            }
            delay(FPS_FRAME_RATE_DELAY)
        }
    }

    private fun decreaseHeart() {
        heart -= 1
        if (heart > 0) return
        isGameOver = true
        gameSensorManager.stopListening()
    }

    fun togglePauseGame(isPause: Boolean) {
        isGamePaused = isPause
        if (isPause) gameSensorManager.stopListening()
        else gameSensorManager.startListening()
    }

    fun measureHitBoxSize(size: Float) {
        hitBoxSize = size
    }

    companion object {
        private const val FPS_FRAME_RATE_DELAY: Long = 1000 / 60
        private const val APPLE_SPEED: Float = 0.0025f
        private const val SPEED_LEVEL_MAX: Float = 5f
        private const val APPLES_MAX_SIZE: Int = 10
        private const val BOMB_SPAWN_RATE: Int = 13
        private const val COIN_SPAWN_RATE: Int = 5
        const val MAX_HEART_VALUE: Int = 3
    }
}
