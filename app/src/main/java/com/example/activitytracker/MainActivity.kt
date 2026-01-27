package com.example.activitytracker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this)
        textView.text = "App is running.\nActivity tracking in progress."
        textView.textSize = 18f
        textView.setPadding(40, 200, 40, 40)

        setContentView(textView)

        startTrackingWorker()
    }

    private fun startTrackingWorker() {
        val workRequest =
            PeriodicWorkRequestBuilder<TrackingWorker>(
                30, TimeUnit.MINUTES
            ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "tracking_work",
            androidx.work.ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}
