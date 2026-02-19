package com.example.easygame.domain.model

import com.example.easygame.data.local.entities.PurchasedObjectEntity
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
    fun toPurchasedEntityOrNull(path: String?): PurchasedObjectEntity? = PurchasedObjectEntity(
        id = id ?: return null,
        objectName = name.orEmpty(),
        type = type ?: GameObjectType.APPLE.type,
        price = price ?: 0L,
        localPath = path ?: return null
    )
}
