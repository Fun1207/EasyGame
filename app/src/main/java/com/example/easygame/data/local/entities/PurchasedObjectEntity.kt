package com.example.easygame.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.easygame.domain.model.RemoteGameObject

@Entity(tableName = "purchased_objects")
data class PurchasedObjectEntity(
    @PrimaryKey
    val id: String,
    val objectName: String,
    val price: Long,
    val type: String,
    val localPath: String
) {
    fun toRemoteGameObject() = RemoteGameObject(
        id = id,
        name = objectName,
        type = type,
        price = price,
        source = localPath,
        isPurchased = true
    )
}
