package com.kyleduo.aladdin.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kyleduo.aladdin.R

/**
 * Utility methods related to permissions
 *
 * @author kyleduo on 2021/5/18
 */
object PermissionUtils {

    private const val OP_SYSTEM_ALERT_WINDOW = 24
    private const val NOTIFICATION_CHANNEL_ID = "aladdin"
    private const val NOTIFICATION_CHANNEL_DESC = "aladdin notification"
    private const val NOTIFICATION_ID = 1
    private const val NOTIFICATION_REQ_CODE = 0

    fun hasAlertWindowPermission(application: Application): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(application)
        } else {
            checkOp(application, OP_SYSTEM_ALERT_WINDOW)
        }
    }

    private fun checkOp(context: Context, @Suppress("SameParameterValue") op: Int): Boolean {
        val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val clazz: Class<*> = AppOpsManager::class.java
        try {
            val method = clazz.getDeclaredMethod(
                "checkOp",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                String::class.java
            )
            return AppOpsManager.MODE_ALLOWED == method.invoke(
                manager,
                op,
                Process.myUid(),
                context.packageName
            ) as Int
        } catch (e: Exception) {
        }
        return true
    }

    private fun buildRequestAlertWindowPermissionIntent(application: Application): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        } else {
            Intent(
                "android.settings.action.MANAGE_OVERLAY_PERMISSION",
                Uri.parse("package:" + application.packageName)
            )
        }.also {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private fun createNotificationChannelIfNeeded(application: Application) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NOTIFICATION_CHANNEL_ID
            val descriptionText = NOTIFICATION_CHANNEL_DESC
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showRequestPermissionNotification(application: Application) {
        createNotificationChannelIfNeeded(application)

        val notification = NotificationCompat.Builder(application, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.aladdin_entry_icon)
            .setContentTitle("Request System Alert Permission")
            .setContentText("System alert permission is required by Aladdin")
            .setContentIntent(
                PendingIntent.getActivity(
                    application,
                    NOTIFICATION_REQ_CODE,
                    buildRequestAlertWindowPermissionIntent(application),
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
            )
            .build()
        with(NotificationManagerCompat.from(application)) {
            notify(NOTIFICATION_ID, notification)
        }
    }

    fun cancelRequestPermissionNotification(application: Application) {
        with(NotificationManagerCompat.from(application)) {
            cancel(NOTIFICATION_ID)
        }
    }
}