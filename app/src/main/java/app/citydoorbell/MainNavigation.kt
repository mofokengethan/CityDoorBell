package app.citydoorbell

import android.app.Application
import android.content.Context
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.citydoorbell.content.model.CategoryStyleType
import app.citydoorbell.content.ui.BackgroundViewFadeTransition
import app.citydoorbell.post.AppDatabase
import app.citydoorbell.post.PostCollectionScreen
import app.citydoorbell.post.PostingScreen

@Composable
fun MainNavigation(
    navigation: NavHostController,
    mainViewModel: MainViewModel,
    context: Context,
    application: Application,
    startDestination: String
) {
    NavHost(
        navController = navigation,
        startDestination = startDestination
    ) {
        composable("landingScreen") {
            LandingScreen(navigation, mainViewModel, context)
        }

        composable("infoScreen/{id}") { backStackEntry ->
            val isMenuValue = backStackEntry.arguments?.getString("id") ?: "isMenu"
            InfoScreen(isMenuValue, navigation, mainViewModel)
        }

        composable("postCollectionScreen/{id}") { backStackEntry ->
            val collectionTitle = backStackEntry.arguments?.getString("id") ?: "City"
            PostCollectionScreen(collectionTitle, navigation, mainViewModel)
        }

        composable("postingScreen/{id}/{title}") { backStackEntry ->
            val screenType = backStackEntry.arguments?.getString("id") ?: "sendPost"
            val cityTitle = backStackEntry.arguments?.getString("title") ?: "New York City, NY, USA"
            PostingScreen(screenType, navigation, mainViewModel, cityTitle)
        }

        composable("postingScreen1/{id}/{title}/{post}") { backStackEntry ->
            val screenType = backStackEntry.arguments?.getString("id") ?: "sendPost"
            val cityTitle = backStackEntry.arguments?.getString("title") ?: "New York City, NY, USA"
            PostingScreen(screenType, navigation, mainViewModel, cityTitle, mainViewModel.postForComments.collectAsState().value)
        }

        composable("postingScreen/{id}/{title}/{cat}") { backStackEntry ->
            val screenType = backStackEntry.arguments?.getString("id") ?: "sendPost"
            val cityTitle = backStackEntry.arguments?.getString("title") ?: "New York City, NY, USA"
            val cat = backStackEntry.arguments?.getString("cat") ?: "0"
            PostingScreen(screenType, navigation, mainViewModel, cityTitle, mainViewModel.postForComments.collectAsState().value, CategoryStyleType.entries[cat.toInt()].description)
        }

        composable("postingScreen/{id}/{title}/{post}/{cat}") { backStackEntry ->
            val screenType = backStackEntry.arguments?.getString("id") ?: "sendPost"
            val cityTitle = backStackEntry.arguments?.getString("title") ?: "New York City, NY, USA"
            val cat = backStackEntry.arguments?.getString("cat") ?: "0"
            PostingScreen(screenType, navigation, mainViewModel, cityTitle, mainViewModel.postForComments.collectAsState().value, CategoryStyleType.entries[cat.toInt()].description)
        }

        // TODO: finish views
        composable("home") {
            BackgroundViewFadeTransition(visible = true, appColors = mainViewModel.appColors.collectAsState().value) {
                Button(onClick = { navigation.navigate(startDestination)}) {
                    Text("Start Destination")
                }
            }
        }
    }
}