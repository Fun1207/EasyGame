package com.example.easygame.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.easygame.data.local.entities.PurchasedObjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchasedObjectDao {

    @Query("SELECT * FROM purchased_objects ")
    suspend fun getPurchasedObjects(): List<PurchasedObjectEntity>

    @Upsert
    fun insertPurchasedObject(purchasedObject: PurchasedObjectEntity)

    @Query("SELECT * FROM purchased_objects WHERE id = :id")
    suspend fun getPurchasedObjectById(id: String): PurchasedObjectEntity?

    @Query("SELECT * FROM purchased_objects WHERE id = :id")
    fun getPurchasedObjectByIdFlow(id: String): Flow<PurchasedObjectEntity?>
}
