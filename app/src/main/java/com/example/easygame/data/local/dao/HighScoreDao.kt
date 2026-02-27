package com.example.easygame.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.easygame.data.local.entities.HighScoreEntity

@Dao
interface HighScoreDao {

    @Upsert
    suspend fun upsertHighScore(highScore: HighScoreEntity)

    @Query("SELECT * FROM high_scores ORDER BY score DESC, time ASC")
    suspend fun getHighScores(): List<HighScoreEntity>

    @Query("SELECT * FROM high_scores ORDER BY score ASC, time ASC LIMIT 1")
    suspend fun getLowestScore(): HighScoreEntity?
}
