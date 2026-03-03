package com.example.easygame.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.easygame.data.local.dao.HighScoreDao
import com.example.easygame.data.local.dao.PurchasedObjectDao
import com.example.easygame.data.local.entities.HighScoreEntity
import com.example.easygame.data.local.entities.PurchasedObjectEntity

@Database(
    entities = [PurchasedObjectEntity::class, HighScoreEntity::class],
    version = 1
)
abstract class EasyGameDatabase : RoomDatabase() {
    abstract fun purchasedObjectDao(): PurchasedObjectDao
    abstract fun highScoreDao(): HighScoreDao
}
