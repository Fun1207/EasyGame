package com.example.easygame.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.easygame.data.local.dao.HighScoreDao
import com.example.easygame.data.local.dao.PurchasedObjectDao
import com.example.easygame.data.local.dao.SelectedItemDao
import com.example.easygame.data.local.entities.HighScoreEntity
import com.example.easygame.data.local.entities.PurchasedObjectEntity
import com.example.easygame.data.local.entities.SelectedItemEntity

@Database(
    entities = [PurchasedObjectEntity::class, SelectedItemEntity::class, HighScoreEntity::class],
    version = 2
)
abstract class EasyGameDatabase : RoomDatabase() {
    abstract fun purchasedObjectDao(): PurchasedObjectDao
    abstract fun selectedItemDao(): SelectedItemDao
    abstract fun highScoreDao(): HighScoreDao
}
