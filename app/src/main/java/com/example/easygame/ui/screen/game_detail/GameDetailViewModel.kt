package com.example.easygame.ui.screen.game_detail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easygame.data.model.Apple
import com.example.easygame.data.repository.GameSensorManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameDetailViewModel(val gameSensorManager: GameSensorManager) : ViewModel() {

    var arrowX by mutableFloatStateOf(0.5f)
        private set

    var appleList by mutableStateOf(listOf<Apple>())
    var score by mutableIntStateOf(0)
    private var isGameOver: Boolean = false
    var threshold by mutableFloatStateOf(0.05f)

    init {
        gameSensorManager.startListening()

        viewModelScope.launch {
            gameSensorManager.tiltData.collect { coordinate ->
                arrowX = (arrowX - coordinate * 0.005f).coerceIn(0f, 1f)
            }
        }


        viewModelScope.launch {
            while (!isGameOver) {
                val currentApples = appleList.map { it.copy(y = it.y + 0.008f) }

                appleList = currentApples.filter { apple ->
                    if (apple.y < 1f - threshold) {
                        Log.e("TAG", "case na1: ")
                        return@filter true
                    }
                    if (apple.x >= arrowX - threshold && apple.x <= arrowX + threshold) {
                        score += 1
                        Log.e("TAG", "score=$score: ")
                        return@filter false
                    }
                    Log.e("TAG", "case 2: ")
                    apple.y < 1.1f
                }

                if (appleList.size < 5 && Random.nextInt(100) < 4) {
                    val newApple = Apple(x = Random.nextFloat(), y = -0.1f)
                    appleList = appleList + newApple
                }

                delay(16)
            }
        }
    }

    private fun generateApples() {
        appleList = List(5) {
            Apple(x = Random.nextFloat(), 0f)
        }
        Log.e("TAG", "generateApples: $appleList")
    }

    override fun onCleared() {
        gameSensorManager.stopListening()
        isGameOver = true
        super.onCleared()
    }
}
