package com.example.easygame.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Navigator(startDestination: Any, private val coroutineScope: CoroutineScope) {
    val backStack: SnapshotStateList<Any> = mutableStateListOf(startDestination)
    var onQuitApplication: (() -> Unit)? = null
        private set
    private var isNavigating = false

    private fun updateBackStackIfSafe(onSafe: () -> Unit) {
        if (isNavigating) return
        isNavigating = true
        onSafe()
        coroutineScope.launch {
            delay(NAVIGATE_DELAY)
            isNavigating = false
        }
    }

    fun navigateTo(destination: Any) {
        if (destination == backStack.lastOrNull()) return
        updateBackStackIfSafe {
            backStack.add(destination)
        }
    }

    fun popBackStack() {
        if (backStack.size <= 1) return
        updateBackStackIfSafe {
            backStack.removeLastOrNull()
        }
    }

    fun setOnQuitApplication(action: () -> Unit) {
        onQuitApplication = action
    }

    fun onClear() {
        coroutineScope.cancel()
    }

    private companion object {
        const val NAVIGATE_DELAY = 500L
    }
}
