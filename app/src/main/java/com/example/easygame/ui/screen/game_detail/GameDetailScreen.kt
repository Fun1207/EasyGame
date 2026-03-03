package com.example.easygame.ui.screen.game_detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.example.easygame.R
import com.example.easygame.domain.model.GameObject
import com.example.easygame.domain.model.GameObjectType
import com.example.easygame.ui.common.DialogButton
import com.example.easygame.ui.common.DialogType
import com.example.easygame.ui.common.GameDialog
import com.example.easygame.ui.theme.ClearFrost
import com.example.easygame.ui.theme.Dimen
import com.example.easygame.ui.theme.Fiona

@Composable
fun GameDetailScreen(viewModel: GameDetailViewModel, onBack: () -> Unit) {
    val isGameOver by viewModel.isGameOver.collectAsStateWithLifecycle()
    val isGamePaused by viewModel.isGamePaused.collectAsStateWithLifecycle()
    val basketX by viewModel.basketX.collectAsStateWithLifecycle()
    val gameObjectList by viewModel.gameObjectList.collectAsStateWithLifecycle()
    val score by viewModel.score.collectAsStateWithLifecycle()
    val coin by viewModel.coin.collectAsStateWithLifecycle()
    val heart by viewModel.heart.collectAsStateWithLifecycle()

    BackPressHandler(isGameOver, isGamePaused, viewModel::togglePauseGame, onBack)
    FocusHandler(isGameOver, isGamePaused, viewModel::togglePauseGame)
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(colors = listOf(ClearFrost, Fiona)))
    ) {
        Image(
            painter = painterResource(R.drawable.icon_tree),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomStart),
            contentScale = ContentScale.FillWidth
        )
        GameView(viewModel.basketResource, basketX, gameObjectList)
        GameInformation(score, coin, heart) { viewModel.togglePauseGame(true) }
    }
    ShowGameDialogs(
        score,
        coin,
        isGameOver,
        isGamePaused,
        onBack,
        viewModel::togglePauseGame,
        viewModel::restartGame,
        viewModel::quitGame
    )
}

@Composable
private fun GameOverContent(score: Long, coin: Long) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        val columnModifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(Dimen.twentyFour))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                Dimen.one,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(Dimen.twentyFour)
            )
            .padding(Dimen.twentyFour)
        Column(modifier = columnModifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.score),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(Dimen.eight))
            Text(text = score.toString(), style = MaterialTheme.typography.titleMedium)
        }
        Spacer(Modifier.width(Dimen.sixteen))
        Column(modifier = columnModifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.coin),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(Dimen.eight))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(R.drawable.icon_coin_filled), null)
                Spacer(Modifier.width(Dimen.four))
                Text(text = coin.toString(), style = MaterialTheme.typography.titleMedium)
            }
        }
    }
    Spacer(Modifier.height(Dimen.thirtyTwo))
}

@Composable
private fun ShowGameDialogs(
    score: Long,
    coin: Long,
    isGameOver: Boolean,
    isGamePaused: Boolean,
    onBack: () -> Unit,
    onTogglePause: (Boolean) -> Unit,
    onRestartGame: () -> Unit,
    onQuitGame: () -> Unit
) {
    GameDialog(
        shouldShow = isGameOver,
        title = stringResource(R.string.game_over),
        message = stringResource(R.string.excellent_run),
        dialogType = DialogType.INFO,
        iconResource = R.drawable.icon_flag,
        isShowCloseButton = false,
        content = { GameOverContent(score, coin) },
        topButton = {
            DialogButton(
                nameResource = R.string.play_again,
                icon = { Image(painterResource(R.drawable.icon_reload), null) },
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                onClick = onRestartGame
            )
            Spacer(Modifier.height(Dimen.twelve))
        },
        bottomButton = {
            DialogButton(
                nameResource = R.string.exit_to_menu,
                icon = { Image(painterResource(R.drawable.icon_home), null) },
                onClick = onBack
            )
        }
    )
    GameDialog(
        shouldShow = isGamePaused,
        title = stringResource(R.string.game_paused),
        message = stringResource(R.string.you_scored, score, score + coin),
        dialogType = DialogType.INFO,
        iconResource = R.drawable.icon_pause,
        onClose = { onTogglePause(false) },
        topButton = {
            DialogButton(
                nameResource = R.string.resume,
                icon = { Image(painterResource(R.drawable.icon_play), null) },
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { onTogglePause(false) }
            )
            Spacer(Modifier.height(Dimen.twelve))
        },
        bottomButton = {
            DialogButton(
                nameResource = R.string.exit_to_menu,
                icon = { Image(painterResource(R.drawable.icon_home), null) },
                onClick = {
                    onQuitGame()
                    onBack()
                }
            )
        }
    )
}

@Composable
private fun GameView(basketResource: Any, basketX: Float, gameObjectList: List<GameObject>) {
    val arrowVectorPainter = rememberAsyncImagePainter(basketResource)
    val appleVectorPainter =
        rememberVectorPainter(ImageVector.vectorResource(R.drawable.icon_apple))
    val bombVectorPainter = rememberVectorPainter(ImageVector.vectorResource(R.drawable.icon_bomb))
    val coinVectorPainter = rememberVectorPainter(ImageVector.vectorResource(R.drawable.icon_coin))
    var objectSize by remember { mutableFloatStateOf(0f) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 64.dp)
            .onSizeChanged { size ->
                objectSize = size.width * 0.1f
            }, contentAlignment = Alignment.TopCenter
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
                        when (apple.type) {
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
private fun FocusHandler(
    isGameOver: Boolean, isGamePaused: Boolean, togglePauseGame: (Boolean) -> Unit
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
