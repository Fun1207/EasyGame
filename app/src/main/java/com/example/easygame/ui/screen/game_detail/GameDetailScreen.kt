package com.example.easygame.ui.screen.game_detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easygame.R
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 64.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(text = "Score: ${viewModel.score}")
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
                    with(appleVectorPainter) {
                        draw(Size(objectSize, objectSize))
                    }
                }
            }
        }
    }
}

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
