package com.example.easygame.domain.model

import com.example.easygame.data.local.entities.PurchasedObjectEntity
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class RemoteGameObject(
    @DocumentId
    val id: String? = null,
    @PropertyName("Name")
    val name: String? = null,
    @PropertyName("Price")
    val price: Long? = null,
    @PropertyName("Source")
    val source: String? = null
) {
    fun getVersion(): String? {
        val versionRegex = VERSION_PATTERN.toRegex()
        val groupValues = versionRegex.find(source ?: return null)
        return groupValues?.groupValues?.lastOrNull()
    }

    fun toPurchasedEntityOrNull(path: String?): PurchasedObjectEntity? {
        return PurchasedObjectEntity(
            id = id ?: return null,
            objectName = name ?: return null,
            version = getVersion() ?: return null,
            localPath = path ?: return null
        )
    }

    private companion object {
        const val VERSION_PATTERN = "/v(\\d+)/"
    }
}
