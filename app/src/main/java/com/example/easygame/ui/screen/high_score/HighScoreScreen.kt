package com.example.easygame.ui.screen.high_score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.easygame.R
import com.example.easygame.data.local.entities.HighScoreEntity
import com.example.easygame.domain.util.toDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighScoreScreen(viewModel: HighScoreViewModel, onBack: () -> Unit) {
    val highScores by viewModel.highScoresFlow.collectAsStateWithLifecycle()

    Column {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(R.string.high_score)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(painterResource(R.drawable.icon_back), contentDescription = null)
                }
            })
        if (highScores.isEmpty()) EmptyHighScoreText()
        else HighScoreTable(highScores)
    }
}

@Composable
fun EmptyHighScoreText() =
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "There is no high score yet",
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            textAlign = TextAlign.Center
        )
    }

@Composable
fun HighScoreTable(highScores: List<HighScoreEntity>) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleItem()
        LazyColumn {
            items(highScores) { item ->
                HighScoreItem(item)
            }
        }
    }
}

@Preview
@Composable
fun TitleItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Name", modifier = Modifier.weight(2f))
        Text(text = "Score", modifier = Modifier.weight(2f))
        Text(text = "Time", modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
fun HighScoreItem(
    highScore: HighScoreEntity = HighScoreEntity(0, "Long", 100, 1771840816045)
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = highScore.name, modifier = Modifier.weight(1f))
        Text(text = highScore.score.toString(), modifier = Modifier.weight(1f))
        Text(text = highScore.time.toDateTime().orEmpty())
    }
}
