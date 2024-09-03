package app.citydoorbell

import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.citydoorbell.content.ui.BackgroundViewSlideTransition
import app.citydoorbell.content.ui.IconButtonViewModel
import app.citydoorbell.content.ui.PrimaryButton
import app.citydoorbell.content.ui.PrimaryIconButton
import app.citydoorbell.content.ui.PrimaryImageVectorButton

@Composable
fun LandingScreen(navigation: NavController, mainViewModel: MainViewModel, context: Context) {
    BackgroundViewSlideTransition(visible = true, appColors = mainViewModel.appColors.collectAsState().value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ding",
                        fontSize = 52.sp,
                        color = mainViewModel.appColors.collectAsState().value.textColor,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = mainViewModel.errorMessage.collectAsState().value,
                        fontSize = 12.sp,
                        color = mainViewModel.appColors.collectAsState().value.highlightColor2,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier
                            .padding(20.dp)
                    )
                    PrimaryButton(
                        if (mainViewModel.errorMessage.collectAsState().value == "") "Login"
                        else "Request Location", appColors = mainViewModel.appColors.collectAsState().value
                    ) {
                        if (mainViewModel.errorMessage.value == "") {
                            /** check if full anonymous sign in is complete */
                            mainViewModel.anonymousUserSignInComplete { hasCompleteSignIn ->
                                if (hasCompleteSignIn) {
                                    /** navigate to main screen*/
                                    val lat = mainViewModel.userLocation.value?.latitude ?: 0.0
                                    val long = mainViewModel.userLocation.value?.longitude ?: 0.0
                                    val city = mainViewModel.isPointInAnyGeofence(
                                        MainViewModel.Coordinate(lat, long),
                                        mainViewModel.cities.value?.toList() ?: emptyList()
                                    )
                                    city?.city?.let { id ->
                                        mainViewModel.updateSelectedCity(id)
                                        navigation.navigate("postCollectionScreen/${id}")
                                    } ?: run {
                                        mainViewModel.updateErrorMessage("We're sorry. You aren't close enough to one of our major cities. Try again later.")
                                    }
                                }
                            }
                        } else {
                            /** request location once permission is granted */
                            mainViewModel.requestLocationPermissions(context)
                        }
                        if (mainViewModel.userLocation.value == null) {
                            /** try to get permission and location if user location is null */
                            LocationManagerSingleton.launchPermissions()
                        }
                    }
                    PrimaryImageVectorButton(IconButtonViewModel(false, mainViewModel), 20.dp, Icons.Outlined.QuestionMark) {
                        val id = "menu"
                        navigation.navigate("infoScreen/${id}")
                    }
                }
            }
            PrimaryIconButton(IconButtonViewModel(false, mainViewModel), 48.dp, R.mipmap.nightstayicon) {
                mainViewModel.updateDarkMode()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview_5() {

    val mainViewModel = MainViewModel(LocalContext.current)
    val navController = rememberNavController()
    val startDestination = "landingScreen"
    // Launch App
    MainNavigation(
        navController,
        mainViewModel,
        LocalContext.current,
        Application(),
        startDestination
    )
}