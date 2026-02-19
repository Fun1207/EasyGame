package com.example.easygame.ui.screen.store

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.easygame.R
import com.example.easygame.domain.model.PurchaseState
import com.example.easygame.domain.model.RemoteGameObject
import com.example.easygame.ui.common.GameDialog
import com.example.easygame.ui.common.carouselScaleEffect
import com.example.easygame.ui.theme.WhiteColor
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.abs

@Composable
fun StoreScreen(viewModel: StoreViewModel, onBack: () -> Unit) = Box(
    modifier = Modifier
        .fillMaxSize()
        .background(WhiteColor),
) {
    val itemList by viewModel.itemListStateFlow.collectAsStateWithLifecycle()
    val selectedItem by viewModel.selectedItemFlow.collectAsStateWithLifecycle()
    val enableBuyButton by viewModel.enableBuyButtonFlow.collectAsStateWithLifecycle()
    val ownedCoin by viewModel.coinFlow.collectAsStateWithLifecycle()
    val purchaseState by viewModel.purchaseItemFlow.collectAsStateWithLifecycle()
    BackPressHandler(
        viewModel.isShowConfirmDialog,
        viewModel::toggleConfirmDialog
    )
    HeaderView(ownedCoin, purchaseState)
    GameObjectList(
        Modifier.align(Alignment.CenterStart),
        itemList,
        purchaseState,
        viewModel::setSelectedGameObject
    )
    PurchaseView(
        Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 64.dp),
        purchaseState,
        enableBuyButton,
        selectedItem?.isPurchased,
        viewModel::buyItem
    )
    SelectedItemDialog(
        selectedItem,
        viewModel.isShowConfirmDialog,
        {
            viewModel.selectedItem()
            onBack()
        },
        { viewModel.toggleConfirmDialog(false) },
        onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeaderView(ownedCoin: Long, purchaseState: PurchaseState) = Column {
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(R.string.store), style = MaterialTheme.typography.headlineLarge)
        },
        navigationIcon = {
            IconButton(
                onClick = {}, enabled = purchaseState !is PurchaseState.Loading
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_back),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }
        })
    Spacer(Modifier.height(48.dp))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.owned_coin, ownedCoin),
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun GameObjectList(
    modifier: Modifier,
    itemList: List<RemoteGameObject>,
    purchaseState: PurchaseState,
    onSelectItem: (String?) -> Unit
) = BoxWithConstraints(
    modifier = modifier.fillMaxWidth()
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
        verticalAlignment = Alignment.CenterVertically,
        userScrollEnabled = purchaseState != PurchaseState.Loading
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
    val price = remoteGameObject.price ?: 0L
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (remoteGameObject.isPurchased) Text(
            text = stringResource(R.string.owned),
            style = MaterialTheme.typography.bodySmall,
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(remoteGameObject.source)
                .build(), contentDescription = null
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = remoteGameObject.name.orEmpty(), style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = if (price != 0L) stringResource(R.string.game_object_price, price)
            else stringResource(R.string.free),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun PurchaseView(
    modifier: Modifier,
    purchaseState: PurchaseState,
    enableBuyButton: Boolean,
    isItemPurchased: Boolean?,
    buyItem: () -> Unit
) {
    val isLoading = purchaseState == PurchaseState.Loading
    val context = LocalContext.current
    LaunchedEffect(purchaseState) {
        if (purchaseState is PurchaseState.Error) Toast.makeText(
            context, purchaseState.throwable?.message, Toast.LENGTH_SHORT
        ).show()
    }
    Button(
        onClick = buyItem, enabled = enableBuyButton && !isLoading, modifier = modifier
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp), strokeWidth = 2.dp
            )
            return@Button
        }
        Text(
            text = stringResource(if (isItemPurchased == true) R.string.purchased else R.string.buy),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun SelectedItemDialog(
    selectedGameObject: RemoteGameObject?,
    isShow: Boolean,
    onYes: () -> Unit,
    onNo: () -> Unit,
    onBack: () -> Unit
) {
    if (!isShow) return
    if (selectedGameObject?.isPurchased == true) {
        GameDialog(
            content = stringResource(
                R.string.do_you_want_to_select,
                selectedGameObject.name.orEmpty()
            ),
            confirm = stringResource(R.string.yes),
            onConfirm = onYes,
            cancel = stringResource(R.string.no),
            onCancel = onNo
        )
        return
    }
    GameDialog(
        content = stringResource(R.string.go_back_with_out_changes),
        confirm = stringResource(R.string.yes),
        onConfirm = onBack
    )
}

@Composable
private fun BackPressHandler(
    isShowConfirmDialog: Boolean,
    toggleConfirmDialog: (Boolean) -> Unit
) = BackHandler(enabled = true) {
    if (isShowConfirmDialog) toggleConfirmDialog(false)
    else toggleConfirmDialog(true)
}
