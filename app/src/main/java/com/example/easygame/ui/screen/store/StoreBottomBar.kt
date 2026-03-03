package com.example.easygame.ui.screen.store

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.easygame.R
import com.example.easygame.ui.theme.Dimen

private data class StoreTab(
    val index: Int,
    val name: String,
    val selectedResource: Int,
    val unselectedResource: Int
)

private val storeTabList by lazy {
    listOf(
        StoreTab(
            index = 0,
            name = "BASKET",
            selectedResource = R.drawable.icon_basket_filled,
            unselectedResource = R.drawable.icon_basket_outlined
        ),
        StoreTab(
            index = 1,
            name = "COIN",
            selectedResource = R.drawable.icon_coin_filled,
            unselectedResource = R.drawable.icon_coin_outlined
        ),
        StoreTab(
            index = 2,
            name = "APPLE",
            selectedResource = R.drawable.icon_apple_filled,
            unselectedResource = R.drawable.icon_apple_outlined
        ),
        StoreTab(
            index = 3,
            name = "BOMB",
            selectedResource = R.drawable.icon_bomb_filled,
            unselectedResource = R.drawable.icon_bomb_outlined
        )
    )
}

@Preview
@Composable
fun BottomStoreView(selectedIndex: Int = 0, onSelected: (Int) -> Unit = {}) {
    Row {
        storeTabList.forEach { tab ->
            val isSelected = tab.index == selectedIndex
            BottomItem(
                modifier = Modifier.weight(1f),
                storeTab = tab,
                isSelected = isSelected,
                onSelected = onSelected
            )
        }
    }
}

@Composable
private fun BottomItem(
    modifier: Modifier,
    storeTab: StoreTab,
    isSelected: Boolean,
    onSelected: (Int) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.9f else 1f)
    Column(
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clip(RoundedCornerShape(Dimen.eight))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onSelected(storeTab.index) }
            )
            .padding(Dimen.twelve),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Icon(
            painter = painterResource(
                if (isSelected) storeTab.selectedResource
                else storeTab.unselectedResource
            ),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Spacer(Modifier.height(Dimen.six))
        Text(
            storeTab.name,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
