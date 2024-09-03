package app.citydoorbell.content.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
 import app.citydoorbell.content.model.AppColors

@Composable
fun BackgroundViewSlideTransition(
    visible: Boolean,
    appColors: AppColors,
    content: @Composable (AppColors) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth }, // Slide in from right
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth }, // Slide out to left
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        Scaffold(
            modifier = Modifier.padding(PaddingValues().calculateTopPadding())
        ) { innerPadding: PaddingValues ->
            Column(
                modifier = Modifier
                    .background(appColors.backgroundColor)
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                content(appColors)
            }
        }
    }
}
