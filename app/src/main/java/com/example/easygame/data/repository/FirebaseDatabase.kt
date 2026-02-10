package com.example.easygame.data.repository

import com.example.easygame.data.model.RemoteGameObject
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class FirebaseDatabase {

    suspend fun getGameObjectList() = runCatching {
        val getCollectionTask = Firebase.firestore.collection(GAME_OBJECT_COLLECTION).get().await()
        getCollectionTask.toObjects<RemoteGameObject>()
    }.getOrElse {
        emptyList()
    }


    private companion object {
        const val GAME_OBJECT_COLLECTION = "GameObjects"

    }
}
