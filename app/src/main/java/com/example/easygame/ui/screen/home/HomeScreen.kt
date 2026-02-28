package com.example.easygame.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.easygame.BuildConfig
import com.example.easygame.R
import com.example.easygame.ui.theme.ApplicationColor
import com.example.easygame.ui.theme.Dimen
import com.example.easygame.ui.theme.Typography

@Preview
@Composable
fun HomeScreen(
    navigateToGameDetail: () -> Unit = {},
    navigateToHighScore: () -> Unit = {},
    navigateToStore: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    quitGame: () -> Unit = {}
) {
    val gradient = remember {
        Brush.linearGradient(
            colors = listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9)), start = Offset.Zero
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .safeContentPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painterResource(R.drawable.icon_game_logo), contentDescription = null)
            Text(
                text = "AeroApple",
                style = Typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            Modifier.weight(3f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HomeButton(
                backgroundColor = ApplicationColor.cosmos,
                textColor = ApplicationColor.white,
                onClick = navigateToGameDetail
            )
            Spacer(Modifier.height(Dimen.twentyFour))
            HomeButton(
                textResource = R.string.high_score,
                iconResource = R.drawable.icon_highscore,
                onClick = navigateToHighScore
            )
            Spacer(Modifier.height(Dimen.twentyFour))

            HomeButton(
                textResource = R.string.store,
                iconResource = R.drawable.icon_store,
                onClick = navigateToStore
            )
            Spacer(Modifier.height(Dimen.twentyFour))
            HomeButton(
                textResource = R.string.settings,
                iconResource = R.drawable.icon_settings,
                onClick = navigateToSettings
            )
            Spacer(Modifier.height(Dimen.twentyFour))
            TextButton(quitGame) {
                Text(
                    text = stringResource(R.string.quit_game),
                    color = ApplicationColor.hydrocarbon,
                    modifier = Modifier.padding(
                        vertical = Dimen.twelve, horizontal = Dimen.thirtyTwo
                    )
                )
            }
        }
        Text(
            text = stringResource(
                R.string.version_name, BuildConfig.VERSION_NAME, stringResource(R.string.app_name)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimen.eight)
                .alpha(0.5f),
            textAlign = TextAlign.Center,
            color = ApplicationColor.hydrocarbon
        )
    }
}

@Preview
@Composable
private fun HomeButton(
    backgroundColor: Color = ApplicationColor.white,
    iconResource: Int = R.drawable.icon_play,
    textResource: Int = R.string.play,
    textColor: Color = Color.Black,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimen.twentyFour)
            .clip(RoundedCornerShape(Dimen.thirtyTwo))
            .background(color = backgroundColor)
            .clickable(
                onClick = onClick
            )
            .padding(vertical = Dimen.eighteen),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(iconResource), contentDescription = null, tint = textColor
        )
        Spacer(Modifier.width(Dimen.twelve))
        Text(
            text = stringResource(textResource),
            style = Typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
