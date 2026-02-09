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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easygame.R
import com.example.easygame.data.model.GameObjectType
import com.example.easygame.ui.theme.BackgroundColor
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
fun GameDetailScreen(
    viewModel: GameDetailViewModel = koinViewModel(),
    onBack: () -> Unit = {}
) {
    HandleLifecycle(viewModel)
    GameView(viewModel)
    TopBar(viewModel)
    PausedGameView(viewModel, onBack)
}

@Composable
private fun PausedGameView(viewModel: GameDetailViewModel, onBack: () -> Unit = {}) {
    BackHandler(enabled = true) {
        viewModel.togglePauseGame(!viewModel.isGamePaused)
    }
    if (!viewModel.isGamePaused) return
    Box(
        Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(48.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.are_you_sure, viewModel.score))
            Row {
                TextButton(onClick = onBack) {
                    Text(stringResource(R.string.end_this_round))
                }
                TextButton(onClick = { viewModel.togglePauseGame(false) }) {
                    Text(stringResource(R.string.back_to_game))
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
                        apple.x * size.width - objectSize / 2,
                        apple.y * size.height + objectSize
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
        Modifier.fillMaxWidth(),
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
fun HandleLifecycle(viewModel: GameDetailViewModel) {
    val isWindowFocused = LocalWindowInfo.current.isWindowFocused
    LaunchedEffect(isWindowFocused) {
        if (viewModel.isGameOver) return@LaunchedEffect
        if (viewModel.isGamePaused) return@LaunchedEffect
        if (isWindowFocused) return@LaunchedEffect
        viewModel.togglePauseGame(true)
    }
}
