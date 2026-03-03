package com.example.easygame.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.easygame.domain.model.GameObject
import com.example.easygame.domain.model.GameObjectType

@Entity(tableName = "purchased_objects")
data class PurchasedObjectEntity(
    @PrimaryKey
    val id: String,
    val objectName: String,
    val price: Long,
    val type: String,
    val localPath: String
) {
    companion object {
        fun PurchasedObjectEntity.toGameObject() = GameObject(
            id = id,
            name = objectName,
            price = price,
            source = localPath,
            isPurchased = true,
            type = runCatching { GameObjectType.valueOf(type) }.getOrNull() ?: GameObjectType.BASKET
        )
    }
}
