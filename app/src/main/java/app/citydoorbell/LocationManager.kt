package app.citydoorbell

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.Manifest
import android.os.Looper
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

object LocationManagerSingleton {
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var permissionResultCallback: ((Boolean) -> Unit)? = null
    private var permissionLauncher: ActivityResultLauncher<String>? = null
    private var onLocationRetrieved: ((Location?) -> Unit)? = null
    private var onError: ((Exception) -> Unit)? = null


    fun init(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun setPermissionLauncher(launcher: ActivityResultLauncher<String>) {
        permissionLauncher = launcher
    }


    fun requestLocationPermissions(
        onLocationRetrieved: (Location?) -> Unit,
        onError: (Exception) -> Unit,
        context: Context
    ) {
        this.onLocationRetrieved = onLocationRetrieved
        this.onError = onError

        // Check if permission is already granted
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted, request location
            requestLocation(context)
        } else {
            // Request permission
            permissionLauncher?.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    fun launchPermissions() {
        permissionLauncher?.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun hasLocationPermissions(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocation(context: Context) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMinUpdateIntervalMillis(5000)
            .build()


        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (location in p0.locations) {
                    onLocationRetrieved?.invoke(location)
                    fusedLocationClient.removeLocationUpdates(this)
                    break
                }
                super.onLocationResult(p0)
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } catch (e: SecurityException) {
            onError?.invoke(e)
        }
    }


    fun setPermissionResultCallback(callback: (Boolean) -> Unit) {
        permissionResultCallback = callback
    }

    fun handlePermissionResult(granted: Boolean) {
        permissionResultCallback?.invoke(granted)
    }
}
