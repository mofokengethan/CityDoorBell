package app.citydoorbell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.citydoorbell.content.model.CategoryStyleType
import app.citydoorbell.content.model.PostStyleType
import app.citydoorbell.content.ui.darkModeColors
import app.citydoorbell.content.ui.lightModeColors

@Composable
fun CategoryItem(type: CategoryStyleType, selectedMenuItem: CategoryStyleType, mainViewModel: MainViewModel, action: () -> Unit) {
    if (type == selectedMenuItem) {
        Text(
            text = type.description,
            fontSize = 11.sp,
            color = if (mainViewModel.isDarkMode.collectAsState().value) {
                lightModeColors.borderColor
            } else {
                darkModeColors.textColor
            },
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.W600,
            modifier = Modifier
                .padding(6.dp)
                .background(
                    if (mainViewModel.isDarkMode.collectAsState().value) {
                        lightModeColors.secondaryTextColor
                    } else {
                        darkModeColors.backgroundColor
                    }, shape = RoundedCornerShape(8.dp)
                )
                .border(
                    1.dp, if (mainViewModel.isDarkMode.collectAsState().value) {
                        lightModeColors.borderColor
                    } else {
                        darkModeColors.borderColor
                    }, shape = RoundedCornerShape(8.dp)
                )
                .padding(6.dp)
                .clickable {
                    action()
                }
        )
    }
    else {
        Text(
            text = type.description,
            fontSize = 11.sp,
            color = lightModeColors.textColor,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            modifier = Modifier
                .padding(6.dp)
                .background(lightModeColors.borderColor, shape = RoundedCornerShape(8.dp))
                .border(1.dp, lightModeColors.textColor, shape = RoundedCornerShape(8.dp))
                .padding(6.dp)
                .clickable {
                    action()
                }
        )
    }
}

@Composable
fun CategoryItem(type: PostStyleType, selectedMenuItem: PostStyleType, mainViewModel: MainViewModel, action: () -> Unit) {
    if (type == selectedMenuItem) {
        Text(
            text = type.toString(),
            fontSize = 11.sp,
            color = if (mainViewModel.isDarkMode.collectAsState().value) {
                lightModeColors.borderColor
            } else {
                darkModeColors.textColor
            },
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.W600,
            modifier = Modifier
                .padding(6.dp)
                .background(
                    if (mainViewModel.isDarkMode.collectAsState().value) {
                        lightModeColors.secondaryTextColor
                    } else {
                        darkModeColors.backgroundColor
                    }, shape = RoundedCornerShape(8.dp)
                )
                .border(
                    1.dp, if (mainViewModel.isDarkMode.collectAsState().value) {
                        lightModeColors.borderColor
                    } else {
                        darkModeColors.borderColor
                    }, shape = RoundedCornerShape(8.dp)
                )
                .padding(6.dp)
                .clickable {
                    action()
                }
        )
    }
    else {
        Text(
            text = type.toString(),
            fontSize = 11.sp,
            color = lightModeColors.textColor,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            modifier = Modifier
                .padding(6.dp)
                .background(lightModeColors.borderColor, shape = RoundedCornerShape(8.dp))
                .border(1.dp, lightModeColors.textColor, shape = RoundedCornerShape(8.dp))
                .padding(6.dp)
                .clickable {
                    action()
                }
        )
    }
}
