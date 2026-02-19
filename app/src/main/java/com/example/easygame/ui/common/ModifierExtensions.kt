package com.example.easygame.ui.common

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
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
