package com.example.easygame.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.easygame.data.local.entities.PurchasedObjectEntity

@Dao
interface PurchasedObjectDao {

    @Query("SELECT * FROM purchased_objects ")
    suspend fun getPurchasedObjects(): List<PurchasedObjectEntity>

    @Upsert
    fun insertPurchasedObject(purchasedObject: PurchasedObjectEntity)
}
