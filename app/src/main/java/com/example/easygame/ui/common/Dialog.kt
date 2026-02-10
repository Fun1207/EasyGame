package com.example.easygame.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.easygame.ui.theme.BackgroundColor
import com.example.easygame.ui.theme.WhiteColor

@Composable
fun GameDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: String? = null,
    cancel: String? = null,
    onCancel: () -> Unit = {},
    confirm: String? = null,
    onConfirm: () -> Unit = {}
) {
    Box(
        modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(16.dp))
                .background(WhiteColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            title?.let {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            content?.let {
                Text(
                    text = content,
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
            Row {
                cancel?.let {
                    TextButton(onClick = onCancel) {
                        Text(
                            text = cancel,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
                confirm?.let {
                    TextButton(onClick = onConfirm) {
                        Text(
                            text = confirm,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}
