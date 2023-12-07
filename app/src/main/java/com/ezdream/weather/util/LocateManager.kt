package com.ezdream.weather.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocateManager(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getDeviceLocation(onLocationResult: (Location?) -> Unit) {
        if (!checkLocationPermission()) {
            onLocationResult(null)
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.i(
                    "LocationManager",
                    "latitude:${location?.latitude} longitude:${location?.longitude}"
                )
                onLocationResult(location)
            }
            .addOnFailureListener { _ ->
                onLocationResult(null)
            }
    }

    fun checkLocationServices() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        }else{
            checkLocationPermission()
        }
    }
}
