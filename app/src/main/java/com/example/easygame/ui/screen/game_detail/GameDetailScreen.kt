package com.example.easygame.ui.screen.game_detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easygame.R
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
fun GameDetailScreen(
    viewModel: GameDetailViewModel = koinViewModel(), onBack: () -> Unit = {}
) {
    val arrowVectorPainter =
        rememberVectorPainter(ImageVector.vectorResource(R.drawable.icon_basket))
    val appleVectorPainter =
        rememberVectorPainter(ImageVector.vectorResource(R.drawable.icon_apple))
    val svgSize = 48.dp
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
            val sizePx = svgSize.toPx()
            viewModel.threshold = sizePx / size.width
            withTransform(
                {
                    translate(viewModel.arrowX * size.width - sizePx / 2, size.height)
//                    rotate(-135f, Offset(sizePx / 2, sizePx / 2))
                }) {
                with(arrowVectorPainter) {
                    draw(Size(sizePx, sizePx))
                }
            }

            viewModel.appleList.forEach { apple ->
                withTransform(
                    {
                        translate(apple.x * size.width - sizePx / 2, apple.y * size.height + sizePx)
                    }
                ) {
                    with(appleVectorPainter) {
                        draw(Size(sizePx, sizePx))
                    }
                }
            }
        }
    }
}
