package com.example.easygame.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.easygame.data.local.entities.SelectedItemEntity
import com.example.easygame.domain.model.GameObjectType

@Dao
interface SelectedItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectedItem(selectedItem: SelectedItemEntity)

    @Query("SELECT * FROM selected_items WHERE type = :type")
    suspend fun getSelectedItemsByType(type: GameObjectType): SelectedItemEntity?
}
