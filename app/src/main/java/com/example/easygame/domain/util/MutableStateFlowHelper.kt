package com.example.easygame.domain.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

fun <T> MutableStateFlow<T>.toStateFlow(scope: CoroutineScope) = asStateFlow().stateIn(
    scope = scope, started = SharingStarted.WhileSubscribed(5000), initialValue = value
)
