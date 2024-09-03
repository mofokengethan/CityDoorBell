package app.citydoorbell.post


import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import app.citydoorbell.content.model.PostModel
import app.citydoorbell.content.model.PostStyleType
import app.citydoorbell.content.ui.PrimaryIconButton
import kotlinx.coroutines.delay

@Composable
fun PostingScreen(
    screenType: String,
    navigation: NavController,
    mainViewModel: MainViewModel,
    cityName: String,
    post: AppDatabase.PostModel = AppDatabase.PostModel(),
    cat: String = ""
) {


    var currentText by remember { mutableStateOf("") }

    val textAction = { textEntry: String ->
        currentText = textEntry
    }

    LaunchedEffect(Unit) {
        while (true) {
            if (currentText == "") {
                textAction.invoke("")
                currentText = ""
            }
            delay(1000)
        }
    }

    TextEntryBackground(
        mainViewModel,
        screenType,
        navigation,
        cityName,
        currentText,
        action = if (screenType == "viewComments") null else textAction,
        cat
    ) {
        if (screenType == "sendPost" || screenType == "sendCommentPost") {
            PostContent(screenType, mainViewModel, currentText, cat, mainViewModel.selectedCity.collectAsState().value, cat) {
                currentText = it
            }
        } else {
            CommentsContent(navigation, mainViewModel, post, cat, mainViewModel.selectedCity.collectAsState().value)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview_2() {

    val mainViewModel = MainViewModel(LocalContext.current)
    val navController = rememberNavController()
    val startDestination = "postingScreen/Send Post"
    // Launch App
    MainNavigation(
        navController,
        mainViewModel,
        LocalContext.current,
        Application(),
        startDestination
    )
}