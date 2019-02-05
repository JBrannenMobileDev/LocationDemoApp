package com.locationdemoapp

import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import java.lang.Exception

class LocationManager {
    companion object {
        var TAG = "LocationManager"

        fun getLocation(applicationContext: Context){

            if (PermissionCheckUtil.checkAccessCoarseLocationPermission(applicationContext)) {
                LocationServices
                    .getFusedLocationProviderClient(applicationContext)
                    .lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            Toast.makeText(applicationContext, location.toString(), Toast.LENGTH_SHORT).show()
                            Log.d(TAG, location.toString())
                        }
                    }
                    .addOnFailureListener { exception: Exception ->
                        Log.d(TAG, exception.message)
                    }
            }
        }
    }
}