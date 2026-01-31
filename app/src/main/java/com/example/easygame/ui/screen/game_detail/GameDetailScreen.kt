package com.example.easygame.ui.screen.game_detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
fun GameDetailScreen(
    viewModel: GameDetailViewModel = koinViewModel(),
    onBack: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { size ->
                    viewModel.screenWidthPx = size.width.toFloat()
                    viewModel.screenHeightPx = size.height.toFloat()
                }
        ) {
            val radius = 20.dp.toPx()
            val centerX = viewModel.circleX * size.width
            val centerY = size.height

            drawCircle(
                color = Color.Yellow,
                radius = radius,
                center = Offset(centerX, centerY)
            )
        }

        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text("Back")
        }
    }
}
