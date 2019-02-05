package com.locationdemoapp

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.util.Log


class LocationWorker (context : Context, params : WorkerParameters) : Worker(context, params){
    companion object {
        const val TAG = "location_worker"
    }

    override fun doWork(): Result {
        return try {
            LocationManager.getLocation(applicationContext)
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Get location failed", e)
            Result.failure()
        }
    }
}