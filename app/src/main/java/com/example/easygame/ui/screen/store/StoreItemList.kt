package com.example.easygame.ui.screen.store

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.easygame.R
import com.example.easygame.domain.model.GameObject
import com.example.easygame.domain.model.PurchaseState
import com.example.easygame.ui.common.shimmerEffect
import com.example.easygame.ui.theme.Dimen
import com.example.easygame.ui.theme.Transparent

@Composable
fun StoreItemList(
    modifier: Modifier = Modifier,
    itemList: List<GameObject>,
    selectedItemId: String?,
    buyItemState: PurchaseState,
    onBuyClick: (GameObject) -> Unit,
    onSelected: (String) -> Unit
) {
    if (itemList.isEmpty()) {
        EmptyItemList(modifier)
        return
    }
    val pageState = rememberPagerState(pageCount = itemList::size)
    HorizontalPager(
        state = pageState,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = Dimen.fortyEight, vertical = Dimen.twentyFour)
    ) { page ->
        StoreItemCardView(
            item = itemList.getOrNull(page) ?: return@HorizontalPager,
            selectedItemId = selectedItemId,
            isFocused = page == pageState.currentPage,
            buyItemState = buyItemState,
            onBuyClick = onBuyClick,
            onSelectedClick = onSelected
        )
    }
}

@Composable
private fun StoreItemCardView(
    item: GameObject,
    selectedItemId: String?,
    isFocused: Boolean,
    buyItemState: PurchaseState,
    onBuyClick: (GameObject) -> Unit,
    onSelectedClick: (String) -> Unit
) {
    val scale by animateFloatAsState(if (isFocused) 1.0f else 0.85f)
    val borderColor = if (isFocused) MaterialTheme.colorScheme.primary else Transparent
    Card(
        shape = RoundedCornerShape(Dimen.thirtyTwo),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .border(Dimen.three, borderColor, RoundedCornerShape(Dimen.thirtyTwo))
    ) {
        Column(
            Modifier.padding(Dimen.twentyFour),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val backgroundItemColor = if (isFocused) MaterialTheme.colorScheme.errorContainer
            else MaterialTheme.colorScheme.background
            Box(
                modifier = Modifier
                    .background(
                        color = backgroundItemColor, shape = RoundedCornerShape(Dimen.sixteen)
                    )
                    .weight(1f), contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(item.source).build(),
                    contentDescription = null
                )
            }
            Spacer(Modifier.height(Dimen.twentyFour))
            Text(
                text = item.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(Dimen.eight))
            Text(
                text = "COMMON", style = MaterialTheme.typography.bodyMedium
            ) // TODO: update item rare later
            Spacer(Modifier.height(Dimen.twentyFour))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(
                        if (item.isPurchased) R.drawable.icon_check
                        else R.drawable.icon_coin_filled
                    ), contentDescription = null
                )
                Spacer(Modifier.width(Dimen.six))
                Text(
                    text = if (item.isPurchased) stringResource(R.string.owned)
                    else item.price.toString(), style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(Modifier.height(Dimen.sixteen))
            val isSelected = item.id == selectedItemId
            val buttonText = when {
                !item.isPurchased -> stringResource(R.string.buy)
                isSelected -> stringResource(R.string.equipped)
                else -> stringResource(R.string.select)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Dimen.twelve))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.primary
                    )
                    .border(
                        Dimen.two, if (isSelected) MaterialTheme.colorScheme.primary.copy(0.2f)
                        else Transparent, RoundedCornerShape(Dimen.twelve)
                    )
                    .clickable(enabled = !isSelected) {
                        when {
                            buyItemState is PurchaseState.Loading -> Unit
                            !item.isPurchased -> onBuyClick(item)
                            !isSelected -> onSelectedClick(item.id)
                            else -> Unit
                        }
                    }
                    .padding(vertical = Dimen.sixteen), contentAlignment = Alignment.Center) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun EmptyItemList(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(Dimen.thirtyTwo),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        modifier = modifier
            .padding(horizontal = Dimen.fortyEight, vertical = Dimen.twentyFour)
            .fillMaxWidth()
    ) {
        Column(
            Modifier.padding(Dimen.twentyFour),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                Modifier
                    .getShimmerModifier()
                    .weight(1f)
            )
            Spacer(Modifier.height(Dimen.twentyFour))
            Box(Modifier.getShimmerModifier())
            Spacer(Modifier.height(Dimen.eight))
            Box(Modifier.getShimmerModifier(Dimen.twenty))
            Spacer(Modifier.height(Dimen.twentyFour))
            Box(Modifier.getShimmerModifier())
            Spacer(Modifier.height(Dimen.sixteen))
            Box(Modifier.getShimmerModifier(Dimen.fortyEight, Dimen.twelve))
        }
    }
}

@Composable
private fun Modifier.getShimmerModifier(
    height: Dp = Dimen.twentyFour, cornerSize: Dp = Dimen.sixteen
) = this
    .fillMaxWidth()
    .height(height)
    .clip(RoundedCornerShape(cornerSize))
    .shimmerEffect()
