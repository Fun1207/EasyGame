package com.example.easygame.ui.screen.store

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.easygame.R
import com.example.easygame.ui.theme.WhiteColor

@Composable
fun StoreScreen(viewModel: StoreViewModel) = Column(
    modifier = Modifier
        .fillMaxSize()
        .background(WhiteColor),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceAround
) {
    val lazyListState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
    val hehe = listOf(
        R.drawable.icon_coin,
        R.drawable.icon_heart,
        R.drawable.icon_bomb,
        R.drawable.icon_bomb,
        R.drawable.icon_bomb,
        R.drawable.icon_apple,
        R.drawable.icon_coin,
        R.drawable.icon_heart,
        R.drawable.icon_bomb,
        R.drawable.icon_apple,
        R.drawable.icon_coin,
        R.drawable.icon_heart,
        R.drawable.icon_bomb,
        R.drawable.icon_apple,
        R.drawable.icon_coin,
        R.drawable.icon_heart,
        R.drawable.icon_bomb,
        R.drawable.icon_apple
    )

    Text(
        "Store",
        style = MaterialTheme.typography.headlineLarge
    )
    Text("Coin: 5000$", style = MaterialTheme.typography.bodyLarge)
    LazyRow(
        modifier = Modifier.graphicsLayer(clip = false),
        state = lazyListState,
        flingBehavior = snapBehavior,
        contentPadding = PaddingValues(24.dp),
        horizontalArrangement = Arrangement.spacedBy(64.dp)
    ) {
        itemsIndexed(hehe) { index, item ->
            GameObjectItem(item, lazyListState, index)
        }
    }
    Text("Buy")
}


@Composable
fun GameObjectItem(itemId: Int, lazyListState: LazyListState, index: Int) {
    // Tính toán cả Scale và Alpha dựa trên khoảng cách tới tâm
    val itemValues by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val itemInfo = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }

            if (itemInfo != null) {
                val center = layoutInfo.viewportEndOffset / 2
                val itemCenter = itemInfo.offset + (itemInfo.size / 2)
                val distanceFromCenter = Math.abs(center - itemCenter).toFloat()

                // Tỷ lệ khoảng cách (0.0 khi ở giữa, 1.0 khi ở mép)
                val distanceFraction =
                    (distanceFromCenter / (layoutInfo.viewportEndOffset / 2)).coerceIn(0f, 1f)

                // Tính Scale: Từ 1.5f (tâm) về 1.0f (mép)
                val scale = 2f - (distanceFraction * 0.5f)

                // Tính Alpha: Từ 1.0f (tâm) về 0.3f (mép) để làm mờ hẳn các item bên cạnh
                val alpha = 1.0f - (distanceFraction * 0.7f)

                Pair(scale, alpha)
            } else {
                Pair(1.0f, 0.3f)
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.graphicsLayer {
            scaleX = itemValues.first
            scaleY = itemValues.first
            alpha = itemValues.second // Áp dụng độ mờ
        }
    ) {
        Image(
            painter = painterResource(itemId),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "100$",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black.copy(alpha = itemValues.second) // Làm mờ cả chữ cho đồng bộ
        )
    }
}
