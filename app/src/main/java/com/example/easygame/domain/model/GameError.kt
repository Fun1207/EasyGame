package com.example.easygame.domain.model

data class GameError(
    val message: String,
    val title: String = UNKNOWN_ERROR_TITLE,
    val code: Int = UNKNOWN_ERROR_CODE
) {
    companion object {
        const val UNKNOWN_ERROR_TITLE = "Unknown error"
        const val UNKNOWN_ERROR_CODE = -1
    }
}
