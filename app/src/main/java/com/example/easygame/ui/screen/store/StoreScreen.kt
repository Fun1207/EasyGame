package com.example.easygame.ui.screen.store

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.easygame.R
import com.example.easygame.domain.model.RemoteGameObject
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

    Text(
        stringResource(R.string.store),
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
        itemsIndexed(viewModel.remoteGameObjectList) { index, item ->
            GameObjectItem(item)
        }
    }
    TextButton(onClick = {viewModel.buyItem(viewModel.remoteGameObjectList.firstOrNull())}) {
        Text(stringResource(R.string.buy))
    }
}


@Composable
fun GameObjectItem(remoteGameObject: RemoteGameObject) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(remoteGameObject.source)
                .size(64)
                .build(),
            contentDescription = null
        )
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(R.string.game_object_price, remoteGameObject.price ?: 0),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
