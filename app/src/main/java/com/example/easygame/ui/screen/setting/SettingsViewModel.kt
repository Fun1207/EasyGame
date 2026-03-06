package com.example.easygame.ui.screen.setting

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {

    private val _soundEffectsFlow = MutableStateFlow(true)
    val soundEffectsFlow = _soundEffectsFlow.asStateFlow()

    private val musicsFlow = MutableStateFlow(true)
    val musicFlow = musicsFlow.asStateFlow()

    private val _volumeFlow = MutableStateFlow(0.5f)
    val volumeFlow = _volumeFlow.asStateFlow()

    private val _isShowLanguageDialogFlow = MutableStateFlow(false)
    val isShowLanguageDialogFlow = _isShowLanguageDialogFlow.asStateFlow()

    private val _languageListFlow =
        MutableStateFlow(listOf("English", "Tiếng Việt", "日本語", "한국어"))
    val languageListFlow = _languageListFlow.asStateFlow()

    private val _selectedLanguageFlow = MutableStateFlow("English")
    val selectedLanguageFlow = _selectedLanguageFlow.asStateFlow()

    fun switchSoundEffects(enable: Boolean) {
        _soundEffectsFlow.update { enable }
    }

    fun switchMusic(enable: Boolean) {
        musicsFlow.update { enable }
    }

    fun updateVolume(volume: Float) {
        _volumeFlow.update { volume }
    }

    fun showLanguageDialog(isShow: Boolean) {
        _isShowLanguageDialogFlow.update { isShow }
    }

    fun switchLanguage(language: String) {
        // TODO: Update later 
    }
}
