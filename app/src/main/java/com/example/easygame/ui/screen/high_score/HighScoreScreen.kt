package com.example.easygame.ui.screen.high_score

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.easygame.R
import com.example.easygame.data.local.entities.HighScoreEntity
import com.example.easygame.domain.util.toDateTime
import com.example.easygame.ui.theme.ApplicationColor
import com.example.easygame.ui.theme.Dimen
import com.example.easygame.ui.theme.Typography

@Composable
fun HighScoreScreen(viewModel: HighScoreViewModel, onBack: () -> Unit) {
    val highScores by viewModel.highScoresFlow.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ApplicationColor.coolGreen)
            .safeContentPadding()
            .padding(horizontal = Dimen.sixteen), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(Dimen.twentyFour))
        Text(
            text = stringResource(R.string.leaderboard),
            style = Typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = ApplicationColor.motherboardGreen
        )
        Spacer(Modifier.height(Dimen.four))
        Text(
            text = stringResource(R.string.top_apple_catcher),
            style = Typography.titleSmall,
            color = ApplicationColor.motherboardGreen,
            modifier = Modifier.alpha(0.7f)
        )
        val modifier = Modifier.weight(1f)
        if (highScores.isEmpty()) EmptyHighScoreView(modifier = modifier)
        else HighScoreList(highScores = highScores, modifier = modifier)
        Row(
            Modifier
                .padding(Dimen.twentyFour)
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimen.twentyFour))
                .background(ApplicationColor.white)
                .clickable(onClick = onBack)
                .padding(horizontal = Dimen.twentyFour, vertical = Dimen.sixteen),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(painterResource(R.drawable.icon_back), contentDescription = null)
            Spacer(Modifier.width(Dimen.eight))
            Text(
                text = stringResource(R.string.back),
                style = Typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun HighScoreList(modifier: Modifier = Modifier, highScores: List<HighScoreEntity>) {
    LazyColumn(
        modifier
            .fillMaxWidth()
            .padding(vertical = Dimen.twentyFour)
    ) {
        itemsIndexed(highScores) { index, item ->
            HighScoreItem(index, item)
        }
    }
}

@Preview
@Composable
private fun HighScoreItem(
    index: Int = 3,
    highScoreEntity: HighScoreEntity = HighScoreEntity(
        name = "AppleMaster 1",
        score = 15400,
        time = 1771840816045
    )
) {
    val iconResource = remember {
        when (index) {
            0, 1, 2 -> R.drawable.icon_high_score_cup
            else -> null
        }
    }
    val iconColor = remember {
        when (index) {
            0 -> ApplicationColor.freshGold
            1 -> ApplicationColor.silverSteel
            2 -> ApplicationColor.darkTangerine
            else -> ApplicationColor.sharkGray
        }
    }
    Row(
        Modifier
            .padding(Dimen.eight)
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimen.twentyFour))
            .background(ApplicationColor.white)
            .padding(Dimen.twenty),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.1f))
                .padding(Dimen.twelve), contentAlignment = Alignment.Center
        ) {
            if (iconResource != null) {
                Icon(
                    painter = painterResource(R.drawable.icon_high_score_cup),
                    contentDescription = null,
                    tint = iconColor
                )
            } else Text(
                text = "${index + 1}",
                style = Typography.titleMedium,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.Center,
                color = ApplicationColor.sharkGray,
                modifier = Modifier.sizeIn(Dimen.twentyThree)
            )
        }
        Spacer(Modifier.width(Dimen.sixteen))
        Column(
            Modifier.weight(1f)
        ) {
            Text(
                text = highScoreEntity.name,
                style = Typography.titleMedium,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(highScoreEntity.time.toDateTime().orEmpty())
        }
        Text(
            text = highScoreEntity.score.toString(),
            color = ApplicationColor.cosmos,
            fontWeight = FontWeight.SemiBold,
            style = Typography.titleSmall
        )
    }
}

@Preview
@Composable
private fun EmptyHighScoreView(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = Dimen.twentyFour, vertical = Dimen.thirtyTwo)
            .background(ApplicationColor.white, RoundedCornerShape(Dimen.thirtyTwo)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                Modifier
                    .padding(Dimen.eight)
                    .size(Dimen.oneHundredTwentyEight)
                    .clip(CircleShape)
                    .background(ApplicationColor.coolGreen), contentAlignment = Alignment.Center
            ) {
                Image(painterResource(R.drawable.icon_empty_high_score), contentDescription = null)
            }
            Image(painterResource(R.drawable.icon_leaf), contentDescription = null)
        }
        Spacer(Modifier.height(Dimen.thirtyTwo))
        Text(
            text = stringResource(R.string.no_score_yet),
            color = ApplicationColor.cosmos,
            style = Typography.headlineSmall
        )
        Spacer(Modifier.height(Dimen.eight))
        Text(
            stringResource(R.string.be_the_first_to_catch_some_apples),
            modifier = Modifier.padding(horizontal = Dimen.twentyFour),
            color = ApplicationColor.sharkGray,
            style = Typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}
