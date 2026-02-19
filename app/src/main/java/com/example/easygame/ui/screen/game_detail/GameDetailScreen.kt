package com.example.easygame.ui.screen.game_detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.example.easygame.R
import com.example.easygame.domain.model.GameObject
import com.example.easygame.domain.model.GameObjectType
import com.example.easygame.ui.common.GameDialog

@Composable
fun GameDetailScreen(viewModel: GameDetailViewModel, onBack: () -> Unit) {
    BackPressHandler(
        viewModel.isGameOver,
        viewModel.isGamePaused,
        viewModel::togglePauseGame,
        onBack
    )
    FocusHandler(viewModel.isGameOver, viewModel.isGamePaused, viewModel::togglePauseGame)
    GameView(
        viewModel.basketResource,
        viewModel.basketX,
        viewModel.gameObjectList,
        viewModel::measureHitBoxSize
    )
    TopBar(viewModel.heart, viewModel.score, viewModel.coin)
    ShowGameDialogs(
        viewModel.score,
        viewModel.coin,
        viewModel.isGameOver,
        viewModel.isGamePaused,
        onBack,
        viewModel::togglePauseGame
    )
}

@Composable
fun ShowGameDialogs(
    score: Int,
    coin: Int,
    isGameOver: Boolean,
    isGamePaused: Boolean,
    onBack: () -> Unit,
    onTogglePause: (Boolean) -> Unit
) {
    if (isGameOver) GameDialog(
        title = stringResource(R.string.game_over),
        content = stringResource(R.string.game_over_result, score, score + coin),
        confirm = stringResource(R.string.back_to_menu),
        onConfirm = onBack
    )
    if (isGamePaused) GameDialog(
        title = stringResource(R.string.game_paused),
        content = stringResource(R.string.are_you_sure, score + coin),
        cancel = stringResource(R.string.end_this_round),
        onCancel = onBack,
        confirm = stringResource(R.string.back_to_game),
        onConfirm = { onTogglePause(false) }
    )
}

@Composable
private fun GameView(
    basketResource: Any,
    basketX: Float,
    gameObjectList: List<GameObject>,
    onMeasuredSize: (Float) -> Unit
) {
    val arrowVectorPainter = rememberAsyncImagePainter(basketResource)
    val appleVectorPainter =
        rememberVectorPainter(ImageVector.vectorResource(R.drawable.icon_apple))
    val bombVectorPainter =
        rememberVectorPainter(ImageVector.vectorResource(R.drawable.icon_bomb))
    val coinVectorPainter =
        rememberVectorPainter(ImageVector.vectorResource(R.drawable.icon_coin))
    var objectSize by remember { mutableFloatStateOf(0f) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 64.dp)
            .onSizeChanged { size ->
                objectSize = size.width * 0.1f
                if (size.width > 0) onMeasuredSize(objectSize / size.width)
            },
        contentAlignment = Alignment.TopCenter
    ) {
        if (objectSize <= 0) return@Box
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            withTransform({
                translate(basketX * size.width - objectSize / 2, size.height)
            }) {
                with(arrowVectorPainter) {
                    draw(Size(objectSize, objectSize))
                }
            }
            gameObjectList.forEach { apple ->
                withTransform({
                    translate(
                        left = apple.x * size.width - objectSize / 2,
                        top = apple.y * size.height + objectSize
                    )
                }) {
                    with(
                        when (apple.gameObjectType) {
                            GameObjectType.APPLE -> appleVectorPainter
                            GameObjectType.BOMB -> bombVectorPainter
                            GameObjectType.COIN -> coinVectorPainter
                            else -> return@withTransform
                        }
                    ) {
                        draw(Size(objectSize, objectSize))
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(heart: Int, score: Int, coin: Int) =
    Box(Modifier.padding(16.dp), contentAlignment = Alignment.CenterEnd) {
        Row {
            Image(
                painter = painterResource(R.drawable.icon_coin),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(coin.toString(), style = MaterialTheme.typography.bodyLarge)
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)) {
                repeat(GameDetailViewModel.MAX_HEART_VALUE) { count ->
                    val isHeartBroken = count >= heart
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
            Text(text = stringResource(R.string.score, score))
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
private fun FocusHandler(
    isGameOver: Boolean,
    isGamePaused: Boolean,
    togglePauseGame: (Boolean) -> Unit
) {
    val isWindowFocused = LocalWindowInfo.current.isWindowFocused
    LaunchedEffect(isWindowFocused) {
        if (isGameOver) return@LaunchedEffect
        if (isGamePaused) return@LaunchedEffect
        if (isWindowFocused) return@LaunchedEffect
        togglePauseGame(true)
    }
}

@Composable
private fun BackPressHandler(
    isGameOver: Boolean,
    isGamePaused: Boolean,
    togglePauseGame: (Boolean) -> Unit,
    onBack: () -> Unit
) = BackHandler(enabled = true) {
    if (isGameOver) {
        onBack()
        return@BackHandler
    }
    togglePauseGame(!isGamePaused)
}
