package com.example.easygame.ui.screen.game_detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.easygame.R
import com.example.easygame.data.model.GameObjectType
import com.example.easygame.ui.theme.BackgroundColor
import com.example.easygame.ui.theme.WhiteColor

@Composable
fun GameDetailScreen(viewModel: GameDetailViewModel, onBack: () -> Unit) {
    BackPressHandler(viewModel, onBack)
    FocusHandler(viewModel)
    GameView(viewModel)
    TopBar(viewModel)
    PausedGameView(viewModel, onBack)
    GameOverView(viewModel, onBack)
}

@Composable
fun GameOverView(viewModel: GameDetailViewModel, onBack: () -> Unit) {
    if (!viewModel.isGameOver) return
    Box(
        Modifier
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
            Text(
                text = stringResource(R.string.game_over),
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.game_over_result, viewModel.score, viewModel.score),
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            TextButton(onClick = onBack) {
                Text(
                    text = stringResource(R.string.back_to_menu),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun PausedGameView(viewModel: GameDetailViewModel, onBack: () -> Unit = {}) {
    if (!viewModel.isGamePaused) return
    Box(
        Modifier
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
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.game_paused),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.are_you_sure, viewModel.score),
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Row {
                TextButton(onClick = onBack) {
                    Text(
                        text = stringResource(R.string.end_this_round),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                TextButton(onClick = { viewModel.togglePauseGame(false) }) {
                    Text(
                        text = stringResource(R.string.back_to_game),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun GameView(viewModel: GameDetailViewModel) {
    val arrowVectorPainter =
        rememberVectorPainter(ImageVector.vectorResource(R.drawable.icon_basket))
    val appleVectorPainter =
        rememberVectorPainter(ImageVector.vectorResource(R.drawable.icon_apple))
    val bombVectorPainter =
        rememberVectorPainter(ImageVector.vectorResource(R.drawable.icon_bomb))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 64.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val objectSize = size.width * 0.1f
            viewModel.hitBoxSize = objectSize / size.width
            withTransform({
                translate(viewModel.basketX * size.width - objectSize / 2, size.height)
            }) {
                with(arrowVectorPainter) {
                    draw(Size(objectSize, objectSize))
                }
            }

            viewModel.appleList.forEach { apple ->
                withTransform({
                    translate(
                        left = apple.x * size.width - objectSize / 2,
                        top = apple.y * size.height + objectSize
                    )
                }) {
                    with(
                        if (apple.gameObjectType == GameObjectType.BOMB) bombVectorPainter
                        else appleVectorPainter
                    ) {
                        draw(Size(objectSize, objectSize))
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(viewModel: GameDetailViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        ) {
            repeat(GameDetailViewModel.MAX_HEART_VALUE) { count ->
                val isHeartBroken = count >= viewModel.heart
                val explosionScale by animateFloatAsState(
                    targetValue = if (isHeartBroken) 3f else 1f,
                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                )
                val explosionAlpha by animateFloatAsState(
                    targetValue = if (isHeartBroken) 0f else 0.8f,
                    animationSpec = tween(durationMillis = 500)
                )
                Box(contentAlignment = Alignment.Center) {
                    if (isHeartBroken) HeartImageView(true)
                    HeartImageView(
                        isHeartBroken = false,
                        modifier = Modifier.graphicsLayer(
                            scaleX = explosionScale,
                            scaleY = explosionScale,
                            alpha = explosionAlpha
                        )
                    )
                }
            }
        }
        Text(text = stringResource(R.string.score, viewModel.score))
    }
}

@Composable
private fun HeartImageView(isHeartBroken: Boolean, modifier: Modifier = Modifier) = Image(
    modifier = modifier
        .size(24.dp)
        .alpha(if (isHeartBroken) 0.8f else 1f),
    painter = painterResource(
        if (isHeartBroken) R.drawable.icon_broken_heart
        else R.drawable.icon_heart
    ),
    contentDescription = null
)

@Composable
private fun FocusHandler(viewModel: GameDetailViewModel) {
    val isWindowFocused = LocalWindowInfo.current.isWindowFocused
    LaunchedEffect(isWindowFocused) {
        if (viewModel.isGameOver) return@LaunchedEffect
        if (viewModel.isGamePaused) return@LaunchedEffect
        if (isWindowFocused) return@LaunchedEffect
        viewModel.togglePauseGame(true)
    }
}

@Composable
private fun BackPressHandler(viewModel: GameDetailViewModel, onBack: () -> Unit) {
    BackHandler(enabled = true) {
        if (viewModel.isGameOver) {
            onBack()
            return@BackHandler
        }
        viewModel.togglePauseGame(!viewModel.isGamePaused)
    }
}
