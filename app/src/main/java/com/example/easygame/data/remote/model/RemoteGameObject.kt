package com.example.easygame.data.remote.model

import com.example.easygame.domain.model.GameObject
import com.example.easygame.domain.model.GameObjectType
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class RemoteGameObject(
    @DocumentId
    val id: String? = null,
    @PropertyName("Name")
    val name: String? = null,
    @PropertyName("Type")
    val type: String? = null,
    @PropertyName("Price")
    val price: Long? = null,
    @PropertyName("Source")
    val source: Any? = null,
    val isPurchased: Boolean = false
) {
    fun toGameObject() = GameObject(
        id = id.orEmpty(),
        name = name.orEmpty(),
        price = price ?: 0L,
        source = source,
        isPurchased = isPurchased,
        type = runCatching { GameObjectType.valueOf(type!!) }.getOrNull() ?: GameObjectType.BASKET
    )
}
