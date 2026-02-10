package com.example.easygame.ui.screen.store

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easygame.data.repository.FirebaseDatabase
import kotlinx.coroutines.launch

class StoreViewModel : ViewModel() {

    val a = FirebaseDatabase()

    init {
        viewModelScope.launch {
        val b = a.getGameObjectList()
            Log.e("TAG", "$b: ", )

        }
    }
}
