package com.example.easygame.domain.model

import com.example.easygame.data.local.entities.PurchasedObjectEntity

data class GameObject(
    val id: String = "",
    val name: String = "",
    val price: Long = 0,
    val source: Any? = null,
    val isPurchased: Boolean = false,
    val type: GameObjectType = GameObjectType.APPLE,
    val x: Float = 0f,
    var y: Float = 0f
) {
    companion object {
        fun GameObject.toPurChasedGameEntity() = PurchasedObjectEntity(
            id = id,
            objectName = name,
            price = price,
            type = type.type,
            localPath = source.toString(),
        )
    }
}

enum class GameObjectType(val type: String) { APPLE("apple"), BOMB("bomb"), COIN("coin"), BASKET("basket") }
