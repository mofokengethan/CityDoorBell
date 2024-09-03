package app.citydoorbell.post

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import app.citydoorbell.MainViewModel
import app.citydoorbell.MenuSelectionList

@Composable
fun CommentsContent(
    navigation: NavController,
    viewModel: MainViewModel,
    post: AppDatabase.PostModel,
    title: String,
    destination: String
) {
    MenuSelectionList(false, mainViewModel = viewModel) {
        // TODO: filter comments
        println()
    }
    PostCard(post, true, appColors = viewModel.appColors.collectAsState().value, null)
    PostList(true, navigation, appColors = viewModel.appColors.collectAsState().value, category = title, destination = destination, viewModel)
}