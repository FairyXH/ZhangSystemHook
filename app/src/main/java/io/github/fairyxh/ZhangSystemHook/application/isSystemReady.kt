package io.github.fairyxh.ZhangSystemHook.application
import android.annotation.SuppressLint
import android.content.Context

object BootStateChecker {

    /**
     * 判断系统是否完全开机
     * @param context 可选，如果传入 context，则顺便判断用户是否解锁
     * @return true 表示系统已完全开机（并且已解锁，如果 context 不为 null）
     */
    @SuppressLint("PrivateApi")
    fun isBootCompletedAndUnlocked(context: Context? = null): Boolean {
        val bootCompleted = try {
            val spClass = Class.forName("android.os.SystemProperties")
            val getInt = spClass.getMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
            val v = getInt.invoke(null, "sys.boot_completed", 0) as Int
            v == 1
        } catch (t: Throwable) {
            false
        }

        if (!bootCompleted) return false

        if (context != null) {
            try {
                val um = context.getSystemService(Context.USER_SERVICE) as android.os.UserManager
                return um.isUserUnlocked
            } catch (_: Throwable) {
                try {
                    val km = context.getSystemService(Context.KEYGUARD_SERVICE) as android.app.KeyguardManager
                    return !km.isDeviceLocked
                } catch (_: Throwable) {
                    return true // 兜底，认为已解锁
                }
            }
        }

        return true
    }
}
