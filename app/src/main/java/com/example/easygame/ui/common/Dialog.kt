package com.example.easygame.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.easygame.R
import com.example.easygame.ui.theme.Dimen

enum class DialogType { ERROR, INFO }

@Composable
fun GameDialog(
    shouldShow: Boolean,
    title: String? = null,
    message: String? = null,
    dialogType: DialogType = DialogType.ERROR,
    iconResource: Int = R.drawable.icon_error,
    isShowCloseButton: Boolean = true,
    onClose: () -> Unit = {},
    content: @Composable () -> Unit = {},
    topButton: @Composable () -> Unit = {},
    bottomButton: @Composable () -> Unit = {
        DialogButton(nameResource = R.string.close, onClick = onClose)
    },
    footer: @Composable () -> Unit = {}
) {
    if (!shouldShow) return
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f))
            .safeContentPadding()
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        val dialogColor = when (dialogType) {
            DialogType.INFO -> MaterialTheme.colorScheme.primary.copy(0.2f)
            DialogType.ERROR -> MaterialTheme.colorScheme.onErrorContainer
        }
        Box(
            Modifier
                .padding(Dimen.twentyFour)
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimen.forty))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    Dimen.one,
                    dialogColor,
                    RoundedCornerShape(Dimen.forty)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimen.thirtyTwo),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .size(Dimen.sixtyFour)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(dialogColor),
                    contentAlignment = Alignment.Center
                ) {
                    Image(painterResource(iconResource), null)
                }
                Spacer(Modifier.height(Dimen.twentyFour))
                title?.let {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(Dimen.twelve))
                }
                message?.let {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(Dimen.thirtyTwo))
                }
                content()
                topButton()
                bottomButton()
                footer()
            }
            if (!isShowCloseButton) return
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Dimen.eight)
            ) {
                Icon(painterResource(R.drawable.icon_close), null)
            }
        }
    }
}

@Composable
fun DialogButton(
    nameResource: Int,
    icon: @Composable (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit = {}
) = Row(
    Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(Dimen.sixteen))
        .background(backgroundColor)
        .safeClickable(onClick = onClick)
        .padding(vertical = Dimen.sixteen),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
) {
    icon?.let {
        icon()
        Spacer(Modifier.width(Dimen.eight))
    }
    Text(
        text = stringResource(nameResource),
        textAlign = TextAlign.Center,
        color = textColor,
        style = MaterialTheme.typography.titleSmall
    )
}
