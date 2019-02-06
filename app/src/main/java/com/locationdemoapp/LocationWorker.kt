package com.locationdemoapp

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.util.Log


class LocationWorker (context : Context, params : WorkerParameters) : Worker(context, params){
    companion object {
        const val TAG = "location_worker"
    }




    /**
     * Every time this LocationWorker class is triggered to run, doWork() will get called on a background thread.
     */
    override fun doWork(): Result {
        return try {
            LocationUtil.fetchAndSaveLocation(applicationContext)
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Get location failed", e)
            Result.failure()
        }
    }
}