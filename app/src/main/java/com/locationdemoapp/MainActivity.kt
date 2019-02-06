package com.locationdemoapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit



class MainActivity : AppCompatActivity() {

    companion object {
        var ACCESS_LOCATION_REQUEST_CODE = 0
    }




    /**
     * This method gets called when the application is launched.
     * Here we set the xml layout and handle any initialization that is needed.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initClickableText()
        checkGpsSettings()
    }




    /**
     * Checks if the user has turned off their location on their device.
     * And if they have, makes a toast asking for location to be turned on.
     * Alternatively an AlertDialogue could be used to ask if the user wants ot turn the location service on and if
     * they select yes, we could launch an intent to open the settings for the location.
     */
    private fun checkGpsSettings() {
        val service = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!enabled) {
            Toast.makeText(applicationContext, R.string.GPS_TURNED_OFF_TOAST, Toast.LENGTH_SHORT).show()
        }
    }




    /**
     * Initializes the id_text_view to have a clickable text of "Hello"
     *
     * Sets up the clickable text listener and starts the background location fetching worker on click.
     */
    private fun initClickableText() {
        val helloClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                if(PermissionCheckUtil.checkAccessFineLocationPermission(applicationContext)) {
                    Toast.makeText(applicationContext, R.string.Task_1_toast, Toast.LENGTH_SHORT).show()
                    startBackgroundTask()
                }else{
                    ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_LOCATION_REQUEST_CODE)
                }
            }
        }

        var spannableString = SpannableString(id_text_view.text)
        spannableString.setSpan(helloClickableSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        id_text_view.movementMethod = LinkMovementMethod.getInstance()
        id_text_view.setText(spannableString, TextView.BufferType.SPANNABLE)
    }





    /**
     * If user was prompted with a permission request, than this method will be triggered on response to the user
     * accepting or denying the request.
     *
     * If the user accepted the location request, then a background worker will be started to fetch user location.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            ACCESS_LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startBackgroundTask()
                } else {
                    // permission denied!
                }
                return
            }
        }
    }





    /**
     * Builds the locationWork object with interval time and queues the work with the WorkManager.
     * PeriodicWorkRequests have a minimum time interval of 15 min. Therefore with this approach i am not able to trigger the task every 1min.
     * The enqueueUniquePeriodicWork ensures that there is only one running worker in the WorkManager at a time.
     */
    private fun startBackgroundTask() {
        var intervalValue = if(BuildConfig.DEBUG) 15L else 60L
        val locationWork = PeriodicWorkRequest.Builder(LocationWorker::class.java, intervalValue, TimeUnit.MINUTES).addTag(LocationWorker.TAG).build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(LocationWorker.TAG, ExistingPeriodicWorkPolicy.REPLACE, locationWork)
    }
}
