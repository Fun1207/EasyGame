package com.example.easygame.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchased_objects")
data class PurchasedObjectEntity(
    @PrimaryKey
    val id: String,
    val objectName: String,
    val version: String,
    val localPath: String
)
