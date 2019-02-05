package com.locationdemoapp

import android.Manifest
import android.app.DownloadManager
import android.content.pm.PackageManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initClickableText()
    }

    companion object {
        var ACCESS_COARS_LOACTION_REQUEST_CODE = 0
    }




    /**
     * Initializes the id_text_view to have a clickable text of "Hello"
     */
    private fun initClickableText() {
        val helloClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                if(PermissionCheckUtil.checkAccessCoarseLocationPermission(applicationContext)) {
                    Toast.makeText(applicationContext, R.string.Task_1_toast, Toast.LENGTH_SHORT).show()
                    startBackgroundTask()
                }else{
                    ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), ACCESS_COARS_LOACTION_REQUEST_CODE)
                }
            }
        }

        var spannableString = SpannableString(id_text_view.text)
        spannableString.setSpan(helloClickableSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        id_text_view.movementMethod = LinkMovementMethod.getInstance()
        id_text_view.setText(spannableString, TextView.BufferType.SPANNABLE)
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            ACCESS_COARS_LOACTION_REQUEST_CODE -> {
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
        val locationWork = PeriodicWorkRequest.Builder(LocationWorker::class.java, R.string.interval_time_in_minutes.toLong(), TimeUnit.MINUTES).addTag(LocationWorker.TAG).build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(LocationWorker.TAG, ExistingPeriodicWorkPolicy.KEEP, locationWork)
    }
}
