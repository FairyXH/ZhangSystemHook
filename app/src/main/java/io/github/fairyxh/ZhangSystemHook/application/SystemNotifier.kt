package io.github.fairyxh.ZhangSystemHook.application

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Toast
import io.github.fairyxh.ZhangSystemHook.core.HookRuntime
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import java.util.concurrent.atomic.AtomicLong

object SystemNotifier {
    private val lastToast = AtomicLong(0)
    fun sendUserMsg(msg: String, context: Context? = null) {
        if (!ConfigData.flag("show_notice")) return
        HookRuntime.diagnostic("HookNotice") { msg }
        val now = SystemClock.elapsedRealtime()
        val previous = lastToast.get()
        if (now - previous < 2000 || !lastToast.compareAndSet(previous, now)) return
        val ctx = context ?: HookRuntime.systemContext() ?: return
        Handler(Looper.getMainLooper()).post { runCatching { Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show() } }
    }
}
