package com.example.easygame.domain.usecase

import com.example.easygame.data.local.dao.HighScoreDao
import com.example.easygame.data.local.datastore.CoinDataStore
import com.example.easygame.data.local.entities.HighScoreEntity
import com.example.easygame.data.repository.GameSensorManager
import com.example.easygame.domain.model.GameObject
import com.example.easygame.domain.model.GameObjectType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import kotlin.random.Random

class ControlGameUseCase(
    private val gameSensorManager: GameSensorManager,
    private val coinDataStore: CoinDataStore,
    private val highScoreDao: HighScoreDao
) {
    val isGameOver = MutableStateFlow(false)
    val isGamePause = MutableStateFlow(false)
    val speedLevel = MutableStateFlow(1f)
    val healPoint = MutableStateFlow(MAX_HP)
    val score = MutableStateFlow(0L)
    val coin = MutableStateFlow(0L)
    val gameObjectList = MutableStateFlow<List<GameObject>>(emptyList())
    val basketX = MutableStateFlow(0.5f)
    private val date by lazy { Date() }
    private val generateGameObjectFlow = flow {
        while (!isGameOver.value) {
            if (isGamePause.value || gameObjectList.value.size > OBJECT_SPAWN_LIMIT)
                delay(FPS_FRAME_RATE_DELAY)
            else {
                val rate = Random.nextInt(0, 100)
                val gameObjectType = when {
                    (rate <= BOMB_SPAWN_RATE) -> GameObjectType.BOMB
                    (rate < BOMB_SPAWN_RATE + COIN_SPAWN_RATE) -> GameObjectType.COIN
                    else -> GameObjectType.APPLE
                }
                emit(GameObject(x = Random.nextFloat(), y = -0.1f, gameObjectType = gameObjectType))
                delay(Random.nextLong(50, 800))
            }
        }
    }
    private val moveGameObjectFlow = flow {
        while (!isGameOver.value) {
            if (!isGamePause.value) {
                val currentApples = gameObjectList.value.map { apple ->
                    apple.copy(y = apple.y + speedLevel.value * SPEED)
                }
                emit(
                    currentApples.filter { apple ->
                        if (apple.y < 1f - HIT_BOX_SIZE * 0.9f) {
                            return@filter true
                        }
                        if (apple.x >= basketX.value - HIT_BOX_SIZE && apple.x <= basketX.value + HIT_BOX_SIZE && apple.y < 1f) {
                            when (apple.gameObjectType) {
                                GameObjectType.BOMB -> calculateHP()
                                GameObjectType.COIN -> coin.update { it + 10 }
                                GameObjectType.APPLE -> calculateScore()
                                else -> Unit
                            }
                            return@filter false
                        }
                        apple.y < 1.1f
                    }
                )
            }
            delay(FPS_FRAME_RATE_DELAY)
        }
    }

    private suspend fun calculateHP() {
        val heart = (healPoint.value - 1).coerceAtLeast(0)
        if (heart <= 0) quitGame()
        healPoint.update { heart }
    }

    private fun calculateScore() {
        score.update { it + 1 }
        if (score.value.mod(NUMBER_SCORE_PER_LEVEL) == 0 && speedLevel.value < SPEED_LEVEL_MAX)
            speedLevel.update { it + 0.5f }
    }

    private suspend fun updateOwnedCoin() = withContext(Dispatchers.IO) {
        val ownedCoin = coinDataStore.coins.firstOrNull() ?: 0L
        coinDataStore.setCoins(ownedCoin + coin.value + score.value)
    }

    private suspend fun updateHighScore() = withContext(Dispatchers.IO) {
        val highScores = highScoreDao.getHighScores()
        if (highScores.size < 5) {
            highScoreDao.upsertHighScore(
                HighScoreEntity(
                    name = "Player",
                    score = score.value,
                    time = date.time
                )
            )
            return@withContext
        }
        val lowestHighScore = highScores.lastOrNull() ?: return@withContext
        highScoreDao.upsertHighScore(lowestHighScore.copy(score = score.value, time = date.time))
    }

    suspend fun startGame() = coroutineScope {
        gameSensorManager.startListening()
        launch {
            gameSensorManager.tiltData.collect { coordinate ->
                basketX.update { (it - coordinate * 0.005f).coerceIn(0f, 1f) }
            }
        }
        launch {
            generateGameObjectFlow.collect { newObject ->
                gameObjectList.update { it + newObject }
            }
        }
        launch {
            moveGameObjectFlow.collect { gameObjects ->
                gameObjectList.update { gameObjects }
            }
        }
    }

    fun togglePause(isPause: Boolean) {
        if (isPause) gameSensorManager.stopListening()
        else gameSensorManager.startListening()
        isGamePause.update { isPause }
    }

    suspend fun quitGame() {
        gameSensorManager.stopListening()
        withContext(Dispatchers.IO) {
            updateOwnedCoin()
            updateHighScore()
        }
        isGameOver.update { true }
    }

    companion object {
        private const val FPS_FRAME_RATE_DELAY: Long = 1000 / 60
        private const val OBJECT_SPAWN_LIMIT: Int = 15
        private const val BOMB_SPAWN_RATE: Int = 13
        private const val COIN_SPAWN_RATE: Int = 5
        private const val SPEED: Float = 0.0025f
        private const val HIT_BOX_SIZE: Float = 0.1f
        private const val SPEED_LEVEL_MAX: Float = 5f
        private const val NUMBER_SCORE_PER_LEVEL: Int = 10
        const val MAX_HP: Int = 3
    }
}
