package com.example.activitytracker

import android.app.usage.UsageStatsManager
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices

class TrackingWorker(
    private val ctx: Context,
    params: WorkerParameters
) : Worker(ctx, params) {

    override fun doWork(): Result {
        collectAppUsage()
        collectLocation()
        return Result.success()
    }

    private fun collectAppUsage() {
        val usageStatsManager =
            ctx.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val endTime = System.currentTimeMillis()
        val startTime = endTime - 30 * 60 * 1000

        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )

        for (usage in stats) {
            if (usage.totalTimeInForeground > 0) {
                Log.d(
                    "ActivityTracker",
                    "App: ${usage.packageName}, Time: ${usage.totalTimeInForeground / 1000}s"
                )
            }
        }
    }

    private fun collectLocation() {
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(ctx)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d(
                        "ActivityTracker",
                        "Location: ${location.latitude}, ${location.longitude}"
                    )
                }
            }
    }
}
