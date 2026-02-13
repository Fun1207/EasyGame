package com.example.easygame.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.easygame.data.local.entities.PurchasedObjectEntity

@Dao
interface PurchasedObjectDao {

    @Query("SELECT id FROM purchased_objects")
    fun getPurchasedObjectIds(): List<String>

    @Query("SELECT version FROM purchased_objects WHERE id = :purchasedObjectId")
    fun getPurchasedObjectVersion(purchasedObjectId: String?): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPurchasedObject(purchasedObject: PurchasedObjectEntity)
}
