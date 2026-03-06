package com.example.easygame.ui.screen.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.easygame.R
import com.example.easygame.ui.common.DialogButton
import com.example.easygame.ui.common.GameDialog
import com.example.easygame.ui.common.safeClickable
import com.example.easygame.ui.theme.Dimen
import com.example.easygame.ui.theme.Transparent
import kotlin.math.roundToInt

@Composable
fun SettingScreen(viewModel: SettingsViewModel, onBack: () -> Unit) {
    val volume = viewModel.volumeFlow.collectAsStateWithLifecycle()
    val soundEffects = viewModel.soundEffectsFlow.collectAsStateWithLifecycle()
    val music = viewModel.musicFlow.collectAsStateWithLifecycle()
    val isShowLanguageDialog = viewModel.isShowLanguageDialogFlow.collectAsStateWithLifecycle()
    val languageList = viewModel.languageListFlow.collectAsStateWithLifecycle()
    val selectedLanguage = viewModel.selectedLanguageFlow.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
            .padding(Dimen.twentyFour)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.settings),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(Dimen.sixteen))
        Text(
            text = stringResource(R.string.audio),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(Dimen.sixteen))
        SettingsSwitch(
            R.drawable.icon_sound,
            stringResource(R.string.sound_effects),
            soundEffects.value,
            viewModel::switchSoundEffects
        )
        Spacer(Modifier.height(Dimen.sixteen))
        SettingsSwitch(
            R.drawable.icon_music,
            stringResource(R.string.musics),
            music.value,
            viewModel::switchMusic
        )
        Spacer(Modifier.height(Dimen.sixteen))
        VolumeSlider(volume.value, viewModel::updateVolume)
        Spacer(Modifier.height(Dimen.thirtyTwo))
        Text(
            text = stringResource(R.string.account),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(Dimen.sixteen))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimen.thirtyTwo))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = Dimen.twentyFour, vertical = Dimen.sixteen),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Dimen.fortyEight)
                    .background(MaterialTheme.colorScheme.primary.copy(0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(painterResource(R.drawable.icon_account), null)
            }
            Spacer(Modifier.width(Dimen.sixteen))
            Column(Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.guest_player),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.not_linked),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Box(
                Modifier
                    .clip(RoundedCornerShape(Dimen.twentyFour))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(vertical = Dimen.ten, horizontal = Dimen.twenty)
            ) {
                Text(
                    text = stringResource(R.string.link),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(Modifier.height(Dimen.thirtyTwo))
        Text(
            text = stringResource(R.string.general),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(Dimen.sixteen))
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimen.thirtyTwo))
                .background(MaterialTheme.colorScheme.surface)
                .safeClickable { viewModel.showLanguageDialog(true) }
                .padding(horizontal = Dimen.twentyFour, vertical = Dimen.sixteen),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(R.drawable.icon_language), null)
            Spacer(Modifier.width(Dimen.sixteen))
            Text(
                stringResource(R.string.language),
                Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = selectedLanguage.value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(Dimen.eight))
            Icon(
                painter = painterResource(R.drawable.icon_arrow_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(Dimen.sixteen))
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimen.thirtyTwo))
                .border(
                    Dimen.one,
                    MaterialTheme.colorScheme.onErrorContainer,
                    RoundedCornerShape(Dimen.thirtyTwo)
                )
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(horizontal = Dimen.twentyFour, vertical = Dimen.sixteen),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_reload),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.width(Dimen.sixteen))
            Text(
                text = stringResource(R.string.reset_game_progress),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(Modifier.height(Dimen.sixteen))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimen.thirtyTwo))
                .background(MaterialTheme.colorScheme.surface)
                .safeClickable(onClick = onBack)
                .padding(horizontal = Dimen.twentyFour, vertical = Dimen.sixteen),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(painterResource(R.drawable.icon_back), contentDescription = null)
            Spacer(Modifier.width(Dimen.eight))
            Text(
                text = stringResource(R.string.back),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
    SwitchLanguageDialog(
        isShow = isShowLanguageDialog.value,
        languageList = languageList.value,
        selectedLanguage = selectedLanguage.value,
        onSave = { selectedLanguage ->
            viewModel.switchLanguage(selectedLanguage)
            viewModel.showLanguageDialog(false)
        },
        onDismiss = { viewModel.showLanguageDialog(false) }
    )
}

@Composable
private fun SettingsSwitch(
    iconResource: Int, text: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimen.thirtyTwo))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = Dimen.twentyFour, vertical = Dimen.sixteen),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painterResource(iconResource), null, Modifier.size(Dimen.sixteen))
        Spacer(Modifier.width(Dimen.sixteen))
        Text(text, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Switch(isChecked, onCheckedChange)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VolumeSlider(value: Float, onValueChange: (Float) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimen.thirtyTwo))
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = Dimen.sixteen, bottom = Dimen.eight)
    ) {
        Row {
            Text(
                text = stringResource(R.string.volume),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Dimen.twentyFour),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "${(value * 100).roundToInt()}%",
                modifier = Modifier.padding(end = Dimen.twenty),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.padding(horizontal = Dimen.twelve),
            thumb = {
                Box(
                    Modifier
                        .size(Dimen.twentyFour)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(Dimen.two, MaterialTheme.colorScheme.primary, CircleShape)
                )
            },
            track = { state ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(Dimen.twelve)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(state.value)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        )
    }
}

@Composable
private fun SwitchLanguageDialog(
    isShow: Boolean,
    languageList: List<String>,
    selectedLanguage: String,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isShow) return
    val selectedItem = remember { mutableStateOf(selectedLanguage) }
    GameDialog(
        true,
        title = stringResource(R.string.select_language),
        onClose = onDismiss,
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimen.sixteen),
                verticalArrangement = Arrangement.spacedBy(Dimen.four)
            ) {
                items(languageList) { item ->
                    LanguageItem(item, selectedItem.value == item) {
                        selectedItem.value = it
                    }
                }
            }
        },
        topButton = {
            DialogButton(
                nameResource = R.string.save,
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { onSave(selectedItem.value) }
            )
            Spacer(Modifier.height(Dimen.twelve))
        },
        bottomButton = {
            DialogButton(nameResource = R.string.cancel, onClick = onDismiss)
        }
    )
}

@Composable
private fun LanguageItem(language: String, isSelected: Boolean, oncClick: (String) -> Unit) {
    Row(
        Modifier
            .heightIn(Dimen.fiftySix)
            .clip(RoundedCornerShape(Dimen.sixteen))
            .background(if (isSelected) MaterialTheme.colorScheme.background else Transparent)
            .safeClickable { oncClick(language) }
            .padding(Dimen.sixteen),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = language,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleSmall,
            color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (!isSelected) return
        Icon(
            painter = painterResource(R.drawable.icon_check),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
