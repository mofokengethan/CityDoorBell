package app.citydoorbell.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.citydoorbell.content.model.AppColors
import app.citydoorbell.content.model.PostStyleType

@Composable
fun PostCard(post: AppDatabase.PostModel, forComments: Boolean, appColors: AppColors, action: (() -> Unit)?) {
    var expanded by remember { mutableStateOf(false) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    val postStyleVote: MutableState<PostStyleType?> = remember { mutableStateOf(null) }
    var doubleTap by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(appColors.cardBackgroundColor, shape = RoundedCornerShape(12.dp))
            .border(1.18.dp, appColors.borderColor, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset = it; expanded = true },
                    onDoubleTap = { doubleTap = !doubleTap }
                )
            }
    ) {
        Column {
            Text(
                text = post.post,
                fontSize = 14.sp,
                color = appColors.textColor,
                fontWeight = FontWeight(300),
                letterSpacing = 0.8.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth().clickable {
                    action?.invoke()
                }
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.weight(1f)
                ) {
                    if (forComments) {
                        Image(imageVector = Icons.Outlined.ArrowDropDown, colorFilter = ColorFilter.tint(
                            appColors.iconColor), contentDescription = "Favorite", modifier = Modifier.size(20.dp))
                        Text(
                            text = "Comments",
                            fontSize = 11.sp,
                            color = appColors.secondaryTextColor,
                            fontWeight = FontWeight(400),
                            letterSpacing = 0.8.sp,
                        )
                    }
                }

                Text(
                    text = post.time,
                    fontSize = 11.sp,
                    color = appColors.secondaryTextColor,
                    fontWeight = FontWeight(400),
                    letterSpacing = 0.8.sp,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}