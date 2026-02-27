package com.example.easygame.ui.screen.high_score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easygame.data.local.dao.HighScoreDao
import com.example.easygame.data.local.entities.HighScoreEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HighScoreViewModel(
    private val highScoreDao: HighScoreDao
) : ViewModel() {

    private val _highScoresFlow = MutableStateFlow<List<HighScoreEntity>>(emptyList())
    val highScoresFlow = _highScoresFlow.asStateFlow()

    init {
        getHighScores()
    }

    private fun getHighScores() = viewModelScope.launch(Dispatchers.IO) {
        _highScoresFlow.value = highScoreDao.getHighScores()
    }
}
