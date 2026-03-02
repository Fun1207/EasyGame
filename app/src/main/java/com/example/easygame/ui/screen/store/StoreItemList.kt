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
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.easygame.R
import com.example.easygame.domain.model.RemoteGameObject
import com.example.easygame.ui.theme.Dimen
import com.example.easygame.ui.theme.Transparent

@Composable
fun StoreItemList(
    modifier: Modifier = Modifier,
    itemList: List<RemoteGameObject>,
    selectedItemId: String?,
    onBuyClick: () -> Unit,
    onSelected: (RemoteGameObject) -> Unit
) {
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
            onBuyClick = onBuyClick,
            onSelectedClick = onSelected
        )
    }
}

@Composable
private fun StoreItemCardView(
    item: RemoteGameObject,
    selectedItemId: String?,
    isFocused: Boolean,
    onBuyClick: () -> Unit,
    onSelectedClick: (RemoteGameObject) -> Unit
) {
    val scale by animateFloatAsState(if (isFocused) 1.0f else 0.85f)
    val borderColor = if (isFocused) MaterialTheme.colorScheme.primary else Transparent
    val roundedCornerShape = RoundedCornerShape(Dimen.thirtyTwo)
    Card(
        shape = roundedCornerShape,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .border(Dimen.three, borderColor, roundedCornerShape)
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
                    model = ImageRequest.Builder(LocalContext.current).data(item.source)
                        .build(), contentDescription = null
                )
            }
            Spacer(Modifier.height(Dimen.twentyFour))
            Text(
                text = item.name.orEmpty(),
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
                    ),
                    contentDescription = null
                )
                Spacer(Modifier.width(Dimen.six))
                Text(
                    text = if (item.isPurchased) stringResource(R.string.owned)
                    else item.price?.toString().orEmpty(),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(Modifier.height(Dimen.sixteen))
            val isSelected = item.id == selectedItemId
            val buttonText = when {
                !item.isPurchased -> stringResource(R.string.buy)
                isSelected -> stringResource(R.string.selected)
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
                            !item.isPurchased -> onBuyClick()
                            !isSelected -> onSelectedClick(item)
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
