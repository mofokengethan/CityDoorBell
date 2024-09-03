package app.citydoorbell.content.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import app.citydoorbell.MainViewModel
import app.citydoorbell.content.model.AppColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class IconButtonViewModel(reverse: Boolean, mainViewModel: MainViewModel) : ViewModel() {
    private val _isDarkMode = MutableStateFlow(false)
    private val isDarkMode: StateFlow<Boolean> = _isDarkMode

    private val _appColors = MutableStateFlow(darkModeColors)
    val appColors: StateFlow<AppColors> = _appColors

    private val _backgroundColor: MutableStateFlow<Color> = MutableStateFlow(Color.Transparent)
    val backgroundColor: StateFlow<Color> = _backgroundColor

    private val _iconColor: MutableStateFlow<Color> = MutableStateFlow(Color.Transparent)
    val iconColor: StateFlow<Color> = _iconColor

    private val _textColor: MutableStateFlow<Color> = MutableStateFlow(Color.Transparent)
    val textColor: StateFlow<Color> = _textColor

    init {
        _isDarkMode.value = mainViewModel.isDarkMode.value
        _appColors.value = mainViewModel.appColors.value

        _backgroundColor.value = getBackgroundColor(reverse)
        _iconColor.value = getIconColor(reverse)
        _textColor.value = getTextColor(reverse)
    }

    private fun getBackgroundColor(reverse: Boolean): Color {
        return if (!reverse) {
            appColors.value.cardBackgroundColor
        } else {
            if (isDarkMode.value) lightModeColors.backgroundColor
            else darkModeColors.backgroundColor
        }
    }

    private fun getIconColor(reverse: Boolean): Color {
        return if (!reverse) {
            appColors.value.textColor
        } else {
            if (isDarkMode.value) lightModeColors.textColor
            else darkModeColors.textColor
        }
    }

    private fun getTextColor(reverse: Boolean): Color {
        return if (!reverse) {
            appColors.value.textColor
        } else {
            if (isDarkMode.value) lightModeColors.textColor
            else darkModeColors.textColor
        }
    }
}

@Composable
fun PrimaryButton(
    title: String,
    appColors: AppColors,
    action: () -> Unit
) {
    OutlinedButton(
        onClick = {
            action()
        },
        border = BorderStroke(1.18.dp, appColors.borderColor),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text =  title,
            fontSize = 12.sp,
            fontFamily = FontFamily.Serif,
            color = appColors.textColor,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.8.sp,
            modifier = Modifier
        )
    }
}

@Composable
fun PrimaryImageVectorButton(viewModel: IconButtonViewModel, size: Dp, icon: ImageVector, action: () -> Unit) {

    val backgroundColor = viewModel.backgroundColor.collectAsState().value
    val iconColor = viewModel.iconColor.collectAsState().value
    val textColor = viewModel.textColor.collectAsState().value

    IconButton(onClick = {
        action()
    }, modifier = Modifier
        .padding(16.dp)
        .background(backgroundColor, CircleShape)
    ) {
        Image(
            imageVector = icon, contentDescription = icon.toString(),
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier
                .background(backgroundColor)
                .size(size)
                .border(1.18.dp, textColor, CircleShape)
                .padding(5.dp)
        )
    }
}

@Composable
fun PrimaryIconButton(viewModel: IconButtonViewModel, size: Dp, icon: Int, action: () -> Unit) {

    val backgroundColor = viewModel.backgroundColor.collectAsState().value
    val iconColor = viewModel.iconColor.collectAsState().value
    val textColor = viewModel.textColor.collectAsState().value

    IconButton(onClick = {
        action()
    }, modifier = Modifier
        .padding(16.dp)
        .background(backgroundColor, CircleShape)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = painterResource(id = icon).toString(),
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier
                .background(backgroundColor)
                .size(size)
                .border(1.18.dp, textColor, CircleShape)
                .padding(5.dp)
        )
    }
}