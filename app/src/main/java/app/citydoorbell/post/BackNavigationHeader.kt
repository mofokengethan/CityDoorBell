package app.citydoorbell.post

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import app.citydoorbell.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackNavigationHeader(screenTitle: String, navController: NavController, navDestination: String, mainViewModel: MainViewModel, action: (()->Unit)? = null) {
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = mainViewModel.appColors.collectAsState().value.backgroundColor,
            scrolledContainerColor = mainViewModel.appColors.collectAsState().value.backgroundColor,
            navigationIconContentColor = mainViewModel.appColors.collectAsState().value.textColor,
            titleContentColor = mainViewModel.appColors.collectAsState().value.textColor,
            actionIconContentColor = mainViewModel.appColors.collectAsState().value.textColor,
        ),
        modifier = Modifier.fillMaxWidth(),
        title = { Text(screenTitle, fontFamily = FontFamily.Serif) },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
            }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}