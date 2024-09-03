package app.citydoorbell

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.citydoorbell.content.model.CityModel
import app.citydoorbell.content.ui.IconButtonViewModel
import app.citydoorbell.content.ui.PrimaryButton
import app.citydoorbell.content.ui.PrimaryIconButton
import app.citydoorbell.post.BackNavigationHeader

@Composable
fun InfoScreen(
    isMenu: String,
    navigation: NavController,
    mainViewModel: MainViewModel
) {
    Scaffold { innerPadding: PaddingValues ->
        val bottomPadding = innerPadding.calculateBottomPadding()
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .background(mainViewModel.appColors.collectAsState().value.backgroundColor)
                .fillMaxSize()
                .padding(bottom = bottomPadding)) {
            Column(modifier = Modifier
                .fillMaxSize()
            ) {
               BackNavigationHeader(
                   screenTitle = "City Door Bell",
                   navController = navigation,
                   navDestination = "landingScreen",
                   mainViewModel
               )
                Text(
                    text = "Cities",
                    fontSize = 48.sp,
                    color = mainViewModel.appColors.collectAsState().value.textColor,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                if (isMenu != "isMenu") {
                    Text(
                        text = "All of our included cities, North America, UK, Europe, Latin America, Asia, and UAE",
                        fontSize = 12.sp,
                        color = mainViewModel.appColors.collectAsState().value.highlightColor2,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )
                }
                LazyColumn(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                   item {
                       mainViewModel.cities.collectAsState().value?.forEach { city: CityModel ->
                           if (isMenu != "isMenu") {
                               Text(
                                   text = city.city,
                                   fontSize = 18.sp,
                                   fontFamily = FontFamily.Serif,
                                   color = mainViewModel.appColors.collectAsState().value.textColor,
                                   fontWeight = FontWeight.W300,
                                   letterSpacing = 0.8.sp,
                                   modifier = Modifier
                                       .padding(8.dp)

                               )
                           } else {
                               Text(
                                   text = city.city,
                                   fontSize = 18.sp,
                                   fontFamily = FontFamily.Serif,
                                   color = mainViewModel.appColors.collectAsState().value.textColor,
                                   fontWeight = FontWeight.W300,
                                   letterSpacing = 0.8.sp,
                                   modifier = Modifier
                                       .padding(8.dp)
                                       .clickable {
                                           val id = city.city
                                           navigation.navigate("postCollectionScreen/${id}")
                                           mainViewModel.updateSelectedCity(id)
                                       }
                               )
                           }
                       }
                   }
                }
           }
            if (isMenu == "isMenu") {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                    .fillMaxWidth()
                    .background(mainViewModel.appColors.collectAsState().value.backgroundColor)) {
                    PrimaryButton("Sign Out", mainViewModel.appColors.collectAsState().value) {
                        mainViewModel.signOut()
                        navigation.navigate("landingScreen")
                    }
                    PrimaryIconButton(IconButtonViewModel(false, mainViewModel), 48.dp, R.mipmap.nightstayicon) {
                        mainViewModel.updateDarkMode()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview_6() {
    val mainViewModel = MainViewModel(LocalContext.current)
    val navController = rememberNavController()
    val startDestination = "infoScreen/isMenu"
    // Launch App
    MainNavigation(
        navController,
        mainViewModel,
        LocalContext.current,
        Application(),
        startDestination
    )
}
