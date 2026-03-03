package com.example.easygame.domain.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDateTime(): String? = runCatching {
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
    format.format(Date(this))
}.getOrNull()
