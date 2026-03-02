package com.example.easygame.ui.screen.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.easygame.R
import com.example.easygame.ui.theme.Dimen

@Composable
fun StoreScreen(viewModel: StoreViewModel, onBack: () -> Unit) {
    val itemList by viewModel.itemListStateFlow.collectAsStateWithLifecycle()
    val selectedItem by viewModel.selectedItemFlow.collectAsStateWithLifecycle()
    val enableBuyButton by viewModel.enableBuyButtonFlow.collectAsStateWithLifecycle()
    val ownedCoin by viewModel.coinFlow.collectAsStateWithLifecycle()
    val purchaseState by viewModel.purchaseItemFlow.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
    ) {
        TopBarView(onBack)
        Spacer(Modifier.height(Dimen.eight))
        BalanceView(ownedCoin)
        StoreItemList(
            Modifier.weight(1f),
            itemList,
            selectedItem?.id,
            viewModel::buyItem,
            viewModel::selectedItem
        )
        GetCoinsButton()
        Spacer(Modifier.height(Dimen.sixteen))
        BottomStoreView(selectedTabIndex) { selectedTabIndex = it }
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
private fun BalanceView(ownedCoin: Long) {
    Row(
        Modifier
            .padding(horizontal = Dimen.twentyFour)
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimen.twentyFour))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = Dimen.sixteen, horizontal = Dimen.twentyFour),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .padding(Dimen.eight), contentAlignment = Alignment.Center
        ) {
            Image(painterResource(R.drawable.icon_coin_filled), null)
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
            color = MaterialTheme.colorScheme.secondary,
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
            .padding(Dimen.sixteen),
        verticalAlignment = Alignment.CenterVertically
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
