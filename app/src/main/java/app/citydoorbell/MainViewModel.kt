package app.citydoorbell

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.citydoorbell.content.model.AppColors
import app.citydoorbell.content.model.CategoryStyleType
import app.citydoorbell.content.model.CityModel
import app.citydoorbell.content.model.PostModel
import app.citydoorbell.content.model.PostStyleType
import app.citydoorbell.content.model.UserModel
import app.citydoorbell.content.ui.darkModeColors
import app.citydoorbell.content.ui.lightModeColors
import app.citydoorbell.post.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class MainViewModel(context: Context): ViewModel() {

    private var _userLocation: MutableStateFlow<Location?> = MutableStateFlow(null)
    val userLocation: StateFlow<Location?> = _userLocation

    private var _postForComments: MutableStateFlow<AppDatabase.PostModel> = MutableStateFlow(AppDatabase.PostModel())
    val postForComments: StateFlow<AppDatabase.PostModel> = _postForComments

    private var _postStyle: MutableStateFlow<PostStyleType> = MutableStateFlow(PostStyleType.All)
    val postStyle: StateFlow<PostStyleType> = _postStyle

    private var _categoryStyle: MutableStateFlow<CategoryStyleType> = MutableStateFlow(CategoryStyleType.All)
    val categoryStyle: StateFlow<CategoryStyleType> = _categoryStyle

    private var _selectedCity: MutableStateFlow<String> = MutableStateFlow("")
    val selectedCity: StateFlow<String> = _selectedCity

    private var _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private var _cities: MutableStateFlow<List<CityModel>?> = MutableStateFlow(null)
    val cities: StateFlow<List<CityModel>?> = _cities

    private var _isDarkModeIcon: MutableStateFlow<Int> = MutableStateFlow(R.mipmap.partlycloudyicon)

    private var _isDarkMode: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var isDarkMode: StateFlow<Boolean> = _isDarkMode

    private var _currentAuth: MutableStateFlow<FirebaseAuth?> = MutableStateFlow(null)
    val currentAuth: StateFlow<FirebaseAuth?> = _currentAuth

    private var _appColors: MutableStateFlow<AppColors> = MutableStateFlow(darkModeColors)
    val appColors: StateFlow<AppColors> = _appColors

    init {
        getCitiesFromJson(context)
    }

    fun updatePostForComments(post: AppDatabase.PostModel) {
        _postForComments.value = post
    }

    fun requestLocationPermissions(context: Context) {
        LocationManagerSingleton.requestLocationPermissions({ location: Location? ->
            location?.let {
                updateUserLocation(it)
            } ?: run {
                updateErrorMessage("Missing location data. You can't use the app without using you're location. Sorry. Try requesting your location again.")
            }
        }, { exception: Exception ->
            updateErrorMessage(exception.localizedMessage ?: "")
        }, context)
    }

    fun signOut() {
        currentAuth.value?.signOut()
    }

    fun anonymousUserSignInComplete(action: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result.user
                user?.uid
                user?.metadata?.creationTimestamp
                /** check for anonymous user from firebase */
                val value = updateCurrentAppUser(user)
                if (value != null) {
                    Log.d("LandingScreen.kt - line 75:\n", "Logging in new anonymous user: ${value.user}")
                    action(true)
                } else {
                    updateErrorMessage("Missing user information. Please try to sign in again.")
                    action(false)
                }
            } else {
                // If sign in fails, display a message to the user.
                updateErrorMessage(task.exception?.localizedMessage ?: "Login failed. Please try again.")
                action(false)
            }
        }
    }

    /** function to load cities from JSON, sets city list */
    private fun getCitiesFromJson(context: Context) {
        viewModelScope.launch {
            val jsonString = getJsonFromRawResource(context = context)
            val cityListType: Type = object : TypeToken<List<CityModel>>() {}.type
            _cities.value = Gson().fromJson(jsonString, cityListType)
        }
    }

    /** function to read JSON file from raw resource */
    private fun getJsonFromRawResource(context: Context): String {
        val resourceId = R.raw.cities // Ensure this matches your raw resource file name
        return context.resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
    }

    fun updateCurrentAppUser(user: FirebaseUser?): UserModel? {
        user?.let { firebaseUser ->
            return UserModel(firebaseUser)
        }
        return null
    }

    /** update the user's location */
    fun updateUserLocation(location: Location) {
        _userLocation.value = location
    }

    /** update error message */
    fun updateErrorMessage(errorMsg: String) {
        _errorMessage.value = errorMsg
    }


    /** adding current user to main view model */
    fun updateCurrentAuth(auth: FirebaseAuth) {
        _currentAuth.value = auth
    }

    fun updateDarkMode() {
        _isDarkMode.value = !isDarkMode.value
        if (isDarkMode.value) {
            _appColors.value = darkModeColors
            _isDarkModeIcon.value = R.mipmap.partlycloudyicon
        } else {
            _appColors.value = lightModeColors
            _isDarkModeIcon.value = R.mipmap.nightstayicon
        }
    }

     data class City(val city: String, val cityBounds: List<List<Double>>)
     data class Coordinate(val latitude: Double, val longitude: Double)
     data class Geofence(val name: String, val coordinates: List<Coordinate>)

    // Convert longitude-latitude pairs to Coordinate objects
    fun convertToCoordinateList(polygon: List<List<Double>>): List<Coordinate> {
        return polygon.map { coord -> Coordinate(coord[1], coord[0]) }
    }

    // Ray-Casting algorithm to check if a point is inside a polygon
    fun isPointInPolygon(point: Coordinate, polygon: List<Coordinate>): Boolean {
        val x = point.longitude
        val y = point.latitude

        var inside = false
        var j = polygon.size - 1

        for (i in polygon.indices) {
            val xi = polygon[i].longitude
            val yi = polygon[i].latitude
            val xj = polygon[j].longitude
            val yj = polygon[j].latitude

            val intersect = ((yi > y) != (yj > y)) && (x < (xj - xi) * (y - yi) / (yj - yi) + xi)
            if (intersect) {
                inside = !inside
            }
            j = i
        }

        return inside
    }

    // Check if a point is inside any geofence and return the geofence or null
    fun isPointInAnyGeofence(point: Coordinate, cities: List<CityModel>): CityModel? {
        for (city in cities) {
            if (city.cityBounds.isNotEmpty()) {
                val coordinates = convertToCoordinateList(city.cityBounds)
                if (isPointInPolygon(point, coordinates)) {
                    return city
                }
            }
        }
        return null
    }

    fun updateSelectedCity(cityName: String) {
        _selectedCity.value = cityName
    }

    fun updateSelectCategoryStyle(mCategoryStyle: CategoryStyleType) {
        _categoryStyle.value = mCategoryStyle
        AppDatabase.getCollection(selectedCity.value, mCategoryStyle.description, false)

    }
    fun updateSelectedPostStyle(mPostStyle: PostStyleType) {
        _postStyle.value = mPostStyle
    }
}