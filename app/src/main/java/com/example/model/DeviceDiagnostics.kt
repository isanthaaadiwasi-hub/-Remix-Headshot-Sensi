package com.example.model

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager

data class DeviceSpecs(
    val manufacturer: String,
    val model: String,
    val androidVersion: String,
    val sdkInt: Int,
    val screenWidthPx: Int,
    val screenHeightPx: Int,
    val densityDpi: Int,
    val densityScale: Float,
    val smallestWidthDp: Int,
    val refreshRateHz: Int,
    val totalRamGb: Int,
    val isHighEnd: Boolean
)

object DeviceInfoHelper {
    fun getDeviceSpecs(context: Context): DeviceSpecs {
        var widthPx = 1080
        var heightPx = 2400
        var densityDpi = 440
        var density = 2.75f
        var refreshRate = 60

        try {
            val resources = context.resources
            val displayMetrics = resources.displayMetrics
            densityDpi = displayMetrics.densityDpi
            density = if (displayMetrics.density > 0f) displayMetrics.density else 2.75f
            widthPx = displayMetrics.widthPixels
            heightPx = displayMetrics.heightPixels

            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    val metrics = windowManager?.currentWindowMetrics
                    val bounds = metrics?.bounds
                    if (bounds != null) {
                        widthPx = bounds.width()
                        heightPx = bounds.height()
                    }
                    val display = try { context.display } catch (_: Exception) { null }
                    display?.let {
                        val mode = it.mode
                        if (mode != null && mode.refreshRate > 0) {
                            refreshRate = mode.refreshRate.toInt()
                        }
                    }
                } catch (_: Exception) {}
            } else {
                @Suppress("DEPRECATION")
                val display = windowManager?.defaultDisplay
                @Suppress("DEPRECATION")
                if (display != null) {
                    val rate = display.refreshRate.toInt()
                    if (rate > 0) refreshRate = rate
                }
            }
        } catch (_: Exception) {}

        val widthDp = if (density > 0f) (widthPx / density).toInt() else 360
        val heightDp = if (density > 0f) (heightPx / density).toInt() else 640
        val smallestWidthDp = minOf(widthDp, heightDp).coerceIn(320, 800)

        var totalRamGb = 4
        try {
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager?.getMemoryInfo(memInfo)
            val ramCalculated = (memInfo.totalMem / (1024L * 1024L * 1024L)).toInt()
            if (ramCalculated > 0) {
                totalRamGb = ramCalculated
            }
        } catch (_: Exception) {}

        val isHighEnd = totalRamGb >= 6 || refreshRate >= 90

        return DeviceSpecs(
            manufacturer = Build.MANUFACTURER.replaceFirstChar { it.uppercase() },
            model = Build.MODEL,
            androidVersion = Build.VERSION.RELEASE ?: "14",
            sdkInt = Build.VERSION.SDK_INT,
            screenWidthPx = widthPx,
            screenHeightPx = heightPx,
            densityDpi = densityDpi,
            densityScale = density,
            smallestWidthDp = smallestWidthDp,
            refreshRateHz = refreshRate,
            totalRamGb = totalRamGb,
            isHighEnd = isHighEnd
        )
    }
}
