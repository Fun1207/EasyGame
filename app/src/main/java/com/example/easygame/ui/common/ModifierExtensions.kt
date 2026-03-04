package com.example.easygame.ui.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import com.example.easygame.ui.theme.Black
import kotlinx.coroutines.delay
import kotlin.math.abs

fun Modifier.carouselScaleEffect(index: Int, lazyListState: LazyListState) = composed {
    val scale by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val itemInfo = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
                ?: return@derivedStateOf 1f

            val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2f
            val itemCenter = itemInfo.offset + itemInfo.size / 2f
            val dist = abs(center - itemCenter)
            val proximity = 1f - (dist / center).coerceIn(0f, 1f)
            1f + (0.5f * proximity)
        }
    }

    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
        alpha = (0.5f + 0.5f * (scale - 1f) / 0.4f).coerceIn(0f, 1f)
    }
}

@Composable
fun Modifier.shimmerEffect(
    shimmerColors: List<Color> = listOf(
        Black.copy(alpha = 0.02f),
        Black.copy(alpha = 0.2f),
        Black.copy(alpha = 0.02f),
    ),
    durationMillis: Int = 2000,
    startOffset: Float = 1000f
): Modifier {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    return this.background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim - startOffset, translateAnim - startOffset),
            end = Offset(translateAnim, translateAnim)
        )
    )
}

@Composable
fun Modifier.safeClickable(onClick: () -> Unit): Modifier {
    val canClick = remember { mutableStateOf(true) }
    LaunchedEffect(canClick) {
        delay(150L)
        canClick.value = true
    }
    return this.clickable {
        canClick.value = false
        onClick()
    }
}
