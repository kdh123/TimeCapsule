package com.dhkim.timecapsule.common.presentation

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dhkim.timecapsule.R
import com.dhkim.timecapsule.TimeCapsuleApplication
import com.dhkim.timecapsule.onboarding.OnboardingActivity
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class NotificationManager @Inject constructor(@ActivityContext private val context: Context) {

    fun showNotification(title: String, desc: String) {
        // Create an explicit intent for an Activity in your app.
        val intent = Intent(context, OnboardingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, TimeCapsuleApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_time_primary)
            .setContentTitle(title)
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
}