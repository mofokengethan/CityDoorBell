package app.citydoorbell.post

import android.app.Application
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.citydoorbell.MainNavigation
import app.citydoorbell.MainViewModel
import app.citydoorbell.MenuSelectionList
import app.citydoorbell.R
import app.citydoorbell.content.model.AppColors
import app.citydoorbell.content.model.CategoryStyleType
import app.citydoorbell.content.model.PostStyleType
import app.citydoorbell.content.ui.BackgroundViewSlideTransition
import app.citydoorbell.content.ui.IconButtonViewModel
import app.citydoorbell.content.ui.PrimaryIconButton
import app.citydoorbell.content.ui.darkModeColors
import app.citydoorbell.content.ui.lightModeColors
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale.Category

@Composable
fun PostCollectionScreen(
    title: String,
    navigation: NavController,
    mainViewModel: MainViewModel
) {
    var categoryStyle = mainViewModel.categoryStyle.collectAsState().value
    var postStyle = mainViewModel.postStyle.collectAsState().value

    BackgroundViewSlideTransition(true, mainViewModel.appColors.collectAsState().value) { appColors: AppColors ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    color = mainViewModel.appColors.collectAsState().value.textColor,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
                MenuSelectionList(true, mainViewModel = mainViewModel) { pStyle ->
                    // TODO: filter posts
                    postStyle = pStyle.first
                 }
                PostList(
                    isComments = false,
                    navigation = navigation,
                    appColors = appColors,
                    category = mainViewModel.categoryStyle.collectAsState().value.description,
                    destination = mainViewModel.selectedCity.collectAsState().value,
                    mainViewModel = mainViewModel
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PrimaryIconButton(IconButtonViewModel(true, mainViewModel), 42.dp, R.mipmap.menubookicon) {
                    val id = "isMenu"
                    navigation.navigate("infoScreen/${id}")
                }
                PrimaryIconButton(IconButtonViewModel(true, mainViewModel), 42.dp, R.mipmap.sendicon) {
                    val id = "sendPost"
                    val cat = categoryStyle.ordinal.toString()
                    navigation.navigate("postingScreen/${id}/${mainViewModel.selectedCity.value}/${cat}")
                }
            }
        }
    }
}

@Composable
fun PostList(isComments: Boolean, navigation: NavController, appColors: AppColors, category: String, destination: String, mainViewModel: MainViewModel) {

    // Fetch posts in a LaunchedEffect
    LaunchedEffect(Unit) {
        if (isComments) {
            AppDatabase.getCollection(destination, category, true, mainViewModel.postForComments.value)
        }
        else {
            AppDatabase.getCollection(destination, category, false)
        }
    }

    // Display loading indicator or post list
    if (AppDatabase.isLoading.value) {
        // Show a horizontal progress indicator
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = appColors.highlightColor
            )
        }
    } else {
        // Show the posts in a LazyColumn
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(AppDatabase.posts.size) { postIndex ->
            if (isComments) {
                    PostCard(AppDatabase.posts[postIndex], false, appColors = appColors, null)
            } else {
                    PostCard(AppDatabase.posts[postIndex], true, appColors = appColors) {
                        val id = "viewComments"
                        val postId = AppDatabase.posts[postIndex].id.split("/")[1]
                        val post = AppDatabase.getPostModelWithId(postId)
                        mainViewModel.updatePostForComments(post)
                        navigation.navigate("postingScreen1/${id}/${destination}/${postId}")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview_1() {

    val mainViewModel = MainViewModel(LocalContext.current)
    val navController = rememberNavController()
    val startDestination = "postCollectionScreen/New York City, NY, USA"
    // Launch App
    MainNavigation(
        navController,
        mainViewModel,
        LocalContext.current,
        Application(),
        startDestination
    )
}