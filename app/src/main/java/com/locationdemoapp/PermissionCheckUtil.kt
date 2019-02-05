package com.locationdemoapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

class PermissionCheckUtil {

    companion object {
        fun checkAccessCoarseLocationPermission(context: Context): Boolean{
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                return false
            }
            return true
        }
    }
}