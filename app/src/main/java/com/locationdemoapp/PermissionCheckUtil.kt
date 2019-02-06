package com.locationdemoapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

class PermissionCheckUtil {

    companion object {
        /**
         * Checks if the user has granted permissions for ACCESS_COARSE_LOCATION
         */
        fun checkAccessCoarseLocationPermission(context: Context): Boolean{
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                return false
            }
            return true
        }


        /**
         * Checks if the user has granted permissions for ACCESS_FINE_LOCATION
         */
        fun checkAccessFineLocationPermission(context: Context): Boolean{
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                return false
            }
            return true
        }
    }
}