package com.kyleduo.aladdin.utils

import android.app.AppOpsManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings

/**
 * Utility methods related to permissions
 *
 * @author kyleduo on 2021/5/18
 */
object PermissionUtils {

    private const val OP_SYSTEM_ALERT_WINDOW = 24

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

    fun requestAlertWindowPermission(application: Application) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        } else {
            Intent(
                "android.settings.action.MANAGE_OVERLAY_PERMISSION",
                Uri.parse("package:" + application.packageName)
            )
        }.also {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.startActivity(intent)
    }
}