package app.citydoorbell

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.Manifest
import android.location.Location
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import app.citydoorbell.content.model.AppColors
import app.citydoorbell.content.model.CityModel
import app.citydoorbell.content.ui.darkModeColors
import app.citydoorbell.content.ui.lightModeColors
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var mainViewModel: MainViewModel
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        mainViewModel = MainViewModel(applicationContext)

        /** getting firebase auth */
        FirebaseApp.initializeApp(this)
        LocationManagerSingleton.init(this)

        /** initialize the launcher for requesting permissions */
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            LocationManagerSingleton.handlePermissionResult(isGranted)
        }

        /** setup the callback for permission results */
        LocationManagerSingleton.setPermissionResultCallback { granted ->
            if (granted) {
                /** request location once permission is granted */
                LocationManagerSingleton.requestLocationPermissions({ location: Location? ->
                    location?.let {
                        // Getting User Location
                        mainViewModel.updateUserLocation(it)
                    } ?: run {
                        mainViewModel.updateErrorMessage("Missing location data. You can't use the app without using you're location. Sorry. Try requesting your location again.")
                    }
                }, { exception: Exception ->
                    mainViewModel.updateErrorMessage(exception.localizedMessage ?: "")

                }, applicationContext)
            } else {
                // Permission Denied By User
                mainViewModel.updateErrorMessage("Location permissions were denied. You can't use the app without using you're location. Sorry. Please allow location permission to use the app.\n\nEnable location permissions in your phone settings and request your location again.")
            }
        }

        if (LocationManagerSingleton.hasLocationPermissions(this)) {
            /** request location directly if permission is already granted */
            LocationManagerSingleton.requestLocationPermissions({ location: Location? ->
                location?.let {
                    // Getting User Location
                    mainViewModel.updateUserLocation(it)
                } ?: run {
                    mainViewModel.updateErrorMessage("Missing location data. You can't use the app without using you're location. Sorry. Try requesting your location again.")
                }
            }, { exception: Exception ->
                mainViewModel.updateErrorMessage(exception.localizedMessage ?: "")

            }, applicationContext)
        } else {
            // Request permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // Handle Permissions
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            LocationManagerSingleton.handlePermissionResult(isGranted)
        }

        // Set Launcher
        LocationManagerSingleton.setPermissionLauncher(permissionLauncher)

        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            /** getting current anonymous user, returns null if no has signed in */
            /** checking current user is null */
            mainViewModel.updateCurrentAuth(auth)

            /** setting up nav controller */
            val navController = rememberNavController()

            /** setting up nav controller */
            val startDestination = "landingScreen"

            // Launch App Navigation
            MainNavigation(
                navController,
                mainViewModel,
                applicationContext,
                application,
                startDestination
            )
        }

//        val bundle = Bundle().apply {
//            putString(FirebaseAnalytics.Param.ITEM_ID, "id_1")
//            putString(FirebaseAnalytics.Param.ITEM_NAME, "name_1")
//        }
//        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
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

