package com.example.easygame.data.model

import com.google.firebase.firestore.PropertyName

data class RemoteGameObject(
    @PropertyName("Name")
    val name: String? = null,
    @PropertyName("Price")
    val price: Long? = null,
    @PropertyName("Source")
    val source: String? = null
)
