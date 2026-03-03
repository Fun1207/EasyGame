package com.example.easygame.ui.screen.store

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.easygame.R
import com.example.easygame.domain.model.GameError
import com.example.easygame.domain.usecase.BuyItemUseCase
import com.example.easygame.ui.common.DialogButton
import com.example.easygame.ui.common.GameDialog
import com.example.easygame.ui.theme.Dimen
import com.example.easygame.ui.theme.Transparent

@Composable
fun StoreScreen(viewModel: StoreViewModel, onBack: () -> Unit) {
    val itemList by viewModel.itemListStateFlow.collectAsStateWithLifecycle()
    val selectedItem by viewModel.selectedBasketFlow.collectAsStateWithLifecycle()
    val ownedCoin by viewModel.ownedCoinFlow.collectAsStateWithLifecycle()
    val buyItemError by viewModel.buyItemError.collectAsStateWithLifecycle()
    val buyItemState by viewModel.buyItemState.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)

    ) {
        Column(Modifier.safeContentPadding()) {
            TopBarView(onBack)
            Spacer(Modifier.height(Dimen.eight))
            BalanceView(ownedCoin, buyItemError)
            StoreItemList(
                Modifier.weight(1f),
                itemList,
                selectedItem.id,
                buyItemState,
                viewModel::buyItem,
                viewModel::selectedItem
            )
            GetCoinsButton()
            Spacer(Modifier.height(Dimen.sixteen))
            BottomStoreView(selectedTabIndex) { selectedTabIndex = it }
        }
        GameDialog(
            shouldShow = buyItemError != null,
            title = buyItemError?.title,
            message = buyItemError?.message,
            onClose = viewModel::dismissError,
            topButton = {
                if (buyItemError?.code != BuyItemUseCase.NOT_ENOUGH_COIN_ERROR_CODE) return@GameDialog
                DialogButton(
                    nameResource = R.string.get_coins,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.height(Dimen.twelve))
            }
        )
    }
}

@Composable
private fun TopBarView(onBack: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimen.twentyFour)
            .padding(bottom = Dimen.twentyFour)
    ) {
        Box(
            Modifier
                .size(Dimen.fortyEight)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Image(painterResource(R.drawable.icon_back), null)
        }
        Text(
            text = stringResource(R.string.basket_store),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun BalanceView(ownedCoin: Long, buyItemError: GameError?) {
    val isNotEnoughCoinError = buyItemError?.code == BuyItemUseCase.NOT_ENOUGH_COIN_ERROR_CODE
    val borderColor = animateColorAsState(
        targetValue = if (isNotEnoughCoinError) MaterialTheme.colorScheme.error else Transparent,
        animationSpec = infiniteRepeatable(tween(durationMillis = 1000))
    )
    Row(
        Modifier
            .padding(horizontal = Dimen.twentyFour)
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimen.twentyFour))
            .run {
                if (!isNotEnoughCoinError) return@run this
                border(Dimen.two, borderColor.value, RoundedCornerShape(Dimen.twentyFour))
            }
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = Dimen.sixteen, horizontal = Dimen.twentyFour),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .clip(CircleShape)
                .background(
                    if (isNotEnoughCoinError) MaterialTheme.colorScheme.error.copy(alpha = 0.25f)
                    else MaterialTheme.colorScheme.background
                )
                .padding(Dimen.eight), contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_coin_filled),
                contentDescription = null,
                tint = if (isNotEnoughCoinError) MaterialTheme.colorScheme.error else Color.Unspecified
            )
        }
        Spacer(Modifier.width(Dimen.twelve))
        Text(
            text = stringResource(R.string.balance),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = ownedCoin.toString(),
            modifier = Modifier.weight(1f),
            color = if (isNotEnoughCoinError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
private fun GetCoinsButton() {
    Row(
        modifier = Modifier
            .padding(horizontal = Dimen.thirtyTwo)
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimen.sixteen))
            .background(MaterialTheme.colorScheme.surface)
            .padding(Dimen.sixteen), verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Dimen.forty)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Image(painterResource(R.drawable.icon_plus), null)
        }
        Spacer(Modifier.width(Dimen.sixteen))
        Column(Modifier.weight(1f)) {
            Text(
                stringResource(R.string.need_more),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.End,
            )
            Text(
                stringResource(R.string.get_coins),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold
            )
        }
        Image(painterResource(R.drawable.icon_arrow_right), null)
    }
}
