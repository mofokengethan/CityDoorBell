package app.citydoorbell.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.citydoorbell.content.model.AppColors
import app.citydoorbell.content.model.PostStyleType

@Composable
fun CommentCard(appColors: AppColors) {
    var expanded by remember { mutableStateOf(false) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    val postStyleVote: MutableState<PostStyleType?> = remember { mutableStateOf(null) }
    var doubleTap by remember { mutableStateOf(false) }
    val text = "This is a sample text that might be up to 140 characters long to demonstrate interactions in Jetpack Compose."
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                postStyleVote.value?.let { type: PostStyleType ->
                    Text(type.getEmoji(), color = Color.Black, modifier = Modifier.padding(end = 8.dp))
                }
                if (doubleTap) {
                    Image(imageVector = Icons.Default.Favorite, colorFilter = ColorFilter.tint(appColors.highlightColor2), contentDescription = "Favorite", modifier = Modifier.size(20.dp))
                }
            }
            Text(
                text = text,
                fontSize = 14.sp,
                color = appColors.textColor,
                fontWeight = FontWeight(300),
                letterSpacing = 0.8.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Text(
                text = "01:13 pm",
                fontSize = 10.sp,
                color = appColors.secondaryTextColor,
                fontWeight = FontWeight(400),
                letterSpacing = 0.8.sp,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(appColors.backgroundColor)
            ) {
                PostStyleType.entries.subList(1, PostStyleType.entries.size - 1).forEach { styleType: PostStyleType ->
                    DropdownMenuItem(
                        onClick = {
                            println(styleType.toString())
                            postStyleVote.value = styleType
                            expanded = false
                        },
                        colors = MenuItemColors(
                            textColor = appColors.textColor,
                            leadingIconColor = Color.Black,
                            trailingIconColor = Color.Transparent,
                            disabledTextColor = Color.Gray,
                            disabledLeadingIconColor = Color.Gray,
                            disabledTrailingIconColor = Color.Gray
                        ),
                        text = {
                            Column {
                                Text(styleType.toString(), fontSize = 12.sp)
                                HorizontalDivider(color = appColors.borderColor, modifier = Modifier
                                    .padding(top = 6.dp)
                                    .alpha(0.5f))
                            }
                        },
                        interactionSource = interactionSource
                    )
                }
                DropdownMenuItem(
                    onClick = {
                        postStyleVote.value = null
                        expanded = false
                    },
                    colors = MenuItemColors(
                        textColor = appColors.textColor,
                        leadingIconColor = Color.Black,
                        trailingIconColor = Color.Transparent,
                        disabledTextColor = Color.Gray,
                        disabledLeadingIconColor = Color.Gray,
                        disabledTrailingIconColor = Color.Gray
                    ),
                    text = {
                        Column {
                            Text("Remove", fontSize = 12.sp)
                        }
                    },
                    interactionSource = interactionSource
                )
            }
        }
    }
}
