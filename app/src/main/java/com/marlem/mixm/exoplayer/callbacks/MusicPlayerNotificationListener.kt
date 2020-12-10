package com.marlem.mixm.exoplayer.callbacks

import android.app.Notification
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.marlem.mixm.exoplayer.MusicService
import com.marlem.mixm.utilities.Constants.NOTIFICATION_ID

class MusicPlayerNotificationListener(private val musicService: MusicService):PlayerNotificationManager.NotificationListener {
    //CTRL + O
    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        //current foreground service when swipe , foreground service is stop
        musicService.apply {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        musicService.apply {
            if(ongoing && !isForegroundService)
                ContextCompat.startForegroundService(this, Intent(applicationContext, this::class.java))
            startForeground(NOTIFICATION_ID, notification)
            isForegroundService = true
        }
    }
}