package com.example.easygame.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.easygame.domain.model.GameObjectType

@Entity(tableName = "selected_items")
data class SelectedItemEntity(
    @PrimaryKey
    val type: GameObjectType,
    val name: String,
    val source: String?
)
