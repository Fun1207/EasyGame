package com.example.easygame.ui.screen.store

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.easygame.R
import com.example.easygame.domain.model.RemoteGameObject
import com.example.easygame.ui.common.carouselScaleEffect
import com.example.easygame.ui.theme.WhiteColor
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.abs

@Composable
fun StoreScreen(viewModel: StoreViewModel) = Column(
    modifier = Modifier
        .fillMaxSize()
        .background(WhiteColor),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceAround
) {
    val getItemListState by viewModel.itemListStateFlow.collectAsStateWithLifecycle()
    val selectedItem by viewModel.selectedItemFlow.collectAsStateWithLifecycle()
    Text(
        stringResource(R.string.store), style = MaterialTheme.typography.headlineLarge
    )
    Text("Coin: 5000$", style = MaterialTheme.typography.bodyLarge)
    GameObjectList(getItemListState, viewModel::setSelectedGameObject)
    TextButton(onClick = viewModel::buyItem, enabled = selectedItem?.isPurchased != true) {
        Text(stringResource(if (selectedItem?.isPurchased == true) R.string.purchased else R.string.buy))
    }
}

@Composable
private fun GameObjectList(
    itemList: List<RemoteGameObject>,
    onSelectItem: (String?) -> Unit
) = BoxWithConstraints(
    modifier = Modifier.fillMaxWidth()
) {
    val itemWidth = maxWidth * 0.2f
    val lazyListState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
    val horizontalPadding = (maxWidth - itemWidth) / 2
    LazyRow(
        state = lazyListState,
        flingBehavior = snapBehavior,
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(itemList) { index, item ->
            Box(
                modifier = Modifier
                    .width(itemWidth)
                    .carouselScaleEffect(index, lazyListState),
                contentAlignment = Alignment.Center
            ) {
                GameObjectItem(item)
            }
        }
    }
    LaunchedEffect(itemList) {
        snapshotFlow {
            val layoutInfo = lazyListState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) return@snapshotFlow null
            val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
            val closestItemIndex =
                visibleItems.minByOrNull { abs((it.offset + it.size / 2) - center) }?.index
            itemList.getOrNull(closestItemIndex ?: return@snapshotFlow null)
        }.distinctUntilChanged().collect { item ->
            onSelectItem(item?.id)
        }
    }
}

@Composable
private fun GameObjectItem(remoteGameObject: RemoteGameObject) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (remoteGameObject.isPurchased) Text(
            text = stringResource(R.string.owned),
            style = MaterialTheme.typography.bodySmall,
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(remoteGameObject.source)
                .build(),
            contentDescription = null
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = remoteGameObject.name.orEmpty(),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.game_object_price, remoteGameObject.price ?: 0),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
