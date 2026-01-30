package com.example.easygame.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.easygame.R

@Preview
@Composable
fun HomeScreen(
    navigateToGameDetail: () -> Unit = {},
    navigateToHighScore: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    quitGame: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = navigateToGameDetail,
            content = { Text(text = stringResource(R.string.play)) }
        )
        Button(
            onClick = navigateToHighScore,
            content = { Text(text = stringResource(R.string.high_score)) }
        )
        Button(
            onClick = navigateToSettings,
            content = { Text(text = stringResource(R.string.settings)) }
        )
        Button(
            onClick = quitGame,
            content = { Text(text = stringResource(R.string.quit_game)) }
        )
    }
}
