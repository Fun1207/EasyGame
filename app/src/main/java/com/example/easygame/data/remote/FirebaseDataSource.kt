package com.example.easygame.data.remote

import com.example.easygame.domain.model.RemoteGameObject
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class FirebaseDataSource {

    private val firebaseFirestore = Firebase.firestore

    suspend fun getGameObjectList() = runCatching {
        val getCollectionTask = firebaseFirestore.collection(GAME_OBJECT_COLLECTION).get().await()
        getCollectionTask.toObjects<RemoteGameObject>()
    }.getOrElse {
        emptyList()
    }

    private companion object {
        const val GAME_OBJECT_COLLECTION = "GameObjects"

    }
}
