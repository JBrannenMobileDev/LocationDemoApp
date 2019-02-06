package com.locationdemoapp

import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import java.lang.Exception

class LocationUtil {
    companion object {
        var TAG = "LocationUtil"


        /**
         * Fetches the last known location using the FusedLocationProviderClient after first checking for permission.
         */
        fun fetchAndSaveLocation(applicationContext: Context){

            if (PermissionCheckUtil.checkAccessFineLocationPermission(applicationContext)) {
                LocationServices
                    .getFusedLocationProviderClient(applicationContext)
                    .lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            Toast.makeText(applicationContext, location.toString(), Toast.LENGTH_SHORT).show()
                            //TODO Task #4: Local Storage Options
                            //TODO Option 1: Using Room, an Android Architecture component for persisting data. Room is a wrapper around SQLight.
                            //TODO Option 2: Using Realm, a noSQL option to persist data
                            //TODO Option 3: Storing desired data in a JSON string and persisting it into SharedPreferences.
                            //TODO
                            //TODO The best option depends on the requirements. How much is stored, how the data needs to be retrieved, ease of use.
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