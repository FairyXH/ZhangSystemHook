package io.github.fairyxh.ZhangSystemHook.core

import android.content.Context
import android.os.SystemClock
import android.util.Log
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.libxposed.api.XposedInterface
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.concurrent.ConcurrentHashMap

object HookRuntime {
    lateinit var api: XposedInterface
    var classLoader: ClassLoader = ClassLoader.getSystemClassLoader()
    var packageName: String = "android"
    private data class Registration(val method: Method, val before: Class<*>?, val after: Class<*>?)
    private val installed = ConcurrentHashMap.newKeySet<Registration>()
    private val methodCache = ConcurrentHashMap<Class<*>, List<Method>>()
    private val lastLog = ConcurrentHashMap<String, Long>()
    @Volatile var context: Context? = null

    fun systemContext(): Context? = context ?: runCatching {
        val type = Class.forName("android.app.ActivityThread")
        val thread = type.getDeclaredMethod("currentActivityThread").invoke(null)
        (type.getDeclaredMethod("getSystemContext").invoke(thread) as Context).also { context = it }
    }.getOrNull()

    fun findClass(name: String): Class<*>? = runCatching { Class.forName(name, false, classLoader) }.getOrNull()
    fun methods(type: Class<*>): List<Method> = methodCache.computeIfAbsent(type) {
        generateSequence(it) { c -> c.superclass }.flatMap { c -> c.declaredMethods.asSequence() }
            .filter { m -> !Modifier.isAbstract(m.modifiers) }.distinct().toList()
    }

    fun log(priority: Int, tag: String, message: String, error: Throwable? = null) {
        if (::api.isInitialized) api.log(priority, tag, message, error)
        else Log.println(priority, tag, message + (error?.let { "\n${Log.getStackTraceString(it)}" } ?: ""))
    }

    fun diagnostic(tag: String, force: Boolean = false, message: () -> String) {
        if (!force && !ConfigData.flag("show_notice")) return
        val now = SystemClock.elapsedRealtime()
        val key = "$tag:${Thread.currentThread().id}"
        val previous = lastLog[key] ?: 0L
        if (now - previous >= 1000) {
            lastLog[key] = now
            log(Log.DEBUG, tag, message())
        }
    }

    fun install(method: Method, before: (HookCall.() -> Unit)? = null, after: (HookCall.() -> Unit)? = null,
                propagateFailure: Boolean = false) {
        val registration = Registration(method, before?.javaClass, after?.javaClass)
        if (!installed.add(registration)) return
        try {
            method.isAccessible = true
            val builder = api.hook(method)
            if (propagateFailure) builder.setExceptionMode(XposedInterface.ExceptionMode.PASSTHROUGH)
            builder.intercept { chain ->
                if (!ConfigData.isEnableModule) return@intercept chain.proceed()
                if (ConfigData.flag("MothodMonitor_SW") && method.declaringClass.name.contains("accessibility", true)) {
                    diagnostic("MethodMonitor", force = true) { method.toGenericString() }
                }
                val call = HookCall(chain)
                try { before?.invoke(call) } catch (error: Throwable) {
                    log(Log.ERROR, "HookCallback", "before failed: $method", error)
                    return@intercept chain.proceed()
                }
                call.failure?.let { throw it }
                val original = if (call.hasResult) call.result else {
                    // Original exceptions propagate; never replay an operation that may have changed state.
                    if (call.hasArguments) chain.proceed(call.args) else chain.proceed()
                }
                call.originalResult(original)
                try { after?.invoke(call) } catch (error: Throwable) {
                    log(Log.ERROR, "HookCallback", "after failed: $method", error)
                    return@intercept original
                }
                call.failure?.let { throw it }
                call.result
            }
            log(Log.INFO, "HookInstall", method.toGenericString())
        } catch (error: Throwable) {
            installed.remove(registration)
            log(Log.WARN, "HookInstall", "Unavailable: $method", error)
        }
    }

    fun named(className: String, vararg names: String, before: (HookCall.() -> Unit)? = null,
              after: (HookCall.() -> Unit)? = null, propagateFailure: Boolean = false,
              filter: (Method) -> Boolean = { true }) {
        val type = findClass(className)
        if (type == null) {
            log(Log.WARN, "HookInstall", "Class unavailable: $className")
            return
        }
        val matches = methods(type).filter { it.name in names && filter(it) }
        if (matches.isEmpty()) log(Log.WARN, "HookInstall", "Methods unavailable: $className ${names.toList()}")
        matches.forEach { install(it, before, after, propagateFailure) }
    }
}

class HookCall(private val chain: XposedInterface.Chain) {
    var failure: Throwable? = null
    val instance: Any? get() = chain.thisObject
    val method: Method get() = chain.executable as Method
    private var changedArguments: Array<Any?>? = null
    val hasArguments: Boolean get() = changedArguments != null
    val args: Array<Any?> get() = changedArguments ?: chain.args.toTypedArray().also { changedArguments = it }
    fun arg(index: Int): Any? = if (hasArguments) changedArguments?.getOrNull(index) else chain.args.getOrNull(index)
    var hasResult: Boolean = false
        private set
    var result: Any? = null
        set(value) { field = value; hasResult = true }
    internal fun originalResult(value: Any?) { result = value }
    fun resultNull() { result = null }
    fun resultFalse() { result = false }
}

class HookCallbacks {
    var beforeCallback: (HookCall.() -> Unit)? = null
    var afterCallback: (HookCall.() -> Unit)? = null
    fun before(block: HookCall.() -> Unit) { beforeCallback = block }
    fun after(block: HookCall.() -> Unit) { afterCallback = block }
}

abstract class BaseHooker {
    abstract fun onHook()
    fun String.toClass(): Class<*> = HookRuntime.findClass(this) ?: throw ClassNotFoundException(this)
    fun Method.hook(block: HookCallbacks.() -> Unit) {
        val callbacks = HookCallbacks().apply(block)
        HookRuntime.install(this, callbacks.beforeCallback, callbacks.afterCallback)
    }
}
