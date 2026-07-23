@file:Suppress("DEPRECATION")
package io.github.fairyxh.ZhangSystemHook.application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import android.content.Context
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import de.robv.android.xposed.XposedHelpers
import com.highcapable.yukihookapi.hook.log.YLog
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData


object SystemNotifier {
    val SHOW_NOTICE = PrefsData("show_notice", false)
    fun getSystemContext(): Context {
        val activityThreadClass = XposedHelpers.findClass("android.app.ActivityThread", null)
        val currentActivityThread = XposedHelpers.callStaticMethod(activityThreadClass, "currentActivityThread")
        return XposedHelpers.callMethod(currentActivityThread, "getSystemContext") as Context
    }

    fun showToast(context: Context, msg: String) {
        try {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun sendNotification(context: Context, title: String, content: String) {
        try {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "hook_notify"
            val channel = NotificationChannel(channelId, "ZhangSystemHook", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)

            val notification = Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setChannelId(channelId)
                .build()

            manager.notify(12345, notification)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun sendUserMsg(msg: String, context: Context? = null) {
        try {
            var can_send_msg=false
            try{
                can_send_msg=ConfigData.getBoolean(SHOW_NOTICE)
            } catch (e: Throwable){
                can_send_msg=false
            }
            if(can_send_msg){
                YLog.info(msg = msg)
                val ctx = context ?: getSystemContext()
                showToast(ctx, msg)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
