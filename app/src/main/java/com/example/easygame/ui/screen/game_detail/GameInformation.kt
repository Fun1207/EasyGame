package com.example.easygame.ui.screen.game_detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.easygame.R
import com.example.easygame.domain.usecase.ControlGameUseCase
import com.example.easygame.ui.theme.Dimen

@Preview
@Composable
fun GameInformation(
    score: Long = 1240,
    coin: Long = 84,
    heart: Int = 2,
    maxHeart: Int = ControlGameUseCase.MAX_HP,
    onPaused: () -> Unit = {}
) {
    val background = MaterialTheme.colorScheme.surface.copy(0.25f)
    Column(
        Modifier
            .safeContentPadding()
            .padding(horizontal = Dimen.sixteen)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .background(background, CircleShape)
                    .padding(horizontal = Dimen.twenty, vertical = Dimen.eight),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.score),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(Dimen.ten))
                Text(text = score.toString(), style = MaterialTheme.typography.titleMedium)
            }
            IconButton(
                onClick = onPaused,
                modifier = Modifier.background(background, CircleShape)
            ) {
                Icon(painterResource(R.drawable.icon_pause), null)
            }
        }
        Spacer(Modifier.height(Dimen.twelve))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .background(background, CircleShape)
                    .padding(horizontal = Dimen.twenty, vertical = Dimen.eight),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painterResource(R.drawable.icon_coin_filled), null)
                Spacer(Modifier.width(Dimen.ten))
                Text(text = coin.toString(), style = MaterialTheme.typography.titleMedium)
            }
            Row(
                modifier = Modifier
                    .background(background, CircleShape)
                    .padding(horizontal = Dimen.twenty, vertical = Dimen.eight),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimen.eight, Alignment.End)
            ) {
                repeat(maxHeart) {
                    Box(contentAlignment = Alignment.Center) {
                        val isHeartBroken = it >= heart
                        if (isHeartBroken) HeartImage(isHeartBroken = true, mustShow = true)
                        HeartImage(it >= heart)
                    }
                }
            }
        }
    }
}

@Composable
private fun HeartImage(isHeartBroken: Boolean, mustShow: Boolean = false) {
    val explosionScale by animateFloatAsState(
        targetValue = if (isHeartBroken) 3f else 1f,
        animationSpec = tween(durationMillis = 500)
    )
    val explosionAlpha by animateFloatAsState(
        targetValue = if (isHeartBroken) 0f else 0.8f,
        animationSpec = tween(durationMillis = 500)
    )
    Image(
        modifier = Modifier
            .run {
                if (mustShow) return@run this
                this
                    .graphicsLayer(explosionScale, explosionScale, explosionAlpha)
                    .alpha(if (isHeartBroken) 0.8f else 1f)
            }
            .size(Dimen.twentyFour),
        painter = painterResource(
            if (isHeartBroken) R.drawable.icon_broken_heart
            else R.drawable.icon_heart
        ),
        contentDescription = null
    )
}
