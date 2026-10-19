package io.github.fairyxh.ZhangSystemHook.core

import io.github.libxposed.api.XposedInterface
import org.junit.Assert.*
import org.junit.Test
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class HookRuntimeTest {
    class Targets {
        fun rewrite(value: String): String = value
        fun skip(): String = "original"
        fun after(): String = "original"
        fun beforeFailure(): String = "original"
        fun afterFailure(): String = "original"
        fun originalFailure(): String = "original"
        fun layered(): String = "original"
    }
    private fun install(name: String, before: (HookCall.() -> Unit)? = null, after: (HookCall.() -> Unit)? = null): Pair<Method, XposedInterface.Hooker> {
        var interceptor: XposedInterface.Hooker? = null
        val builder = Proxy.newProxyInstance(javaClass.classLoader, arrayOf(XposedInterface.HookBuilder::class.java)) { proxy, method, args ->
            if (method.name == "intercept") { interceptor = args[0] as XposedInterface.Hooker; null } else proxy
        }
        HookRuntime.api = Proxy.newProxyInstance(javaClass.classLoader, arrayOf(XposedInterface::class.java)) { _, method, _ ->
            if (method.name == "hook") builder else null
        } as XposedInterface
        val method = Targets::class.java.declaredMethods.first { it.name == name }
        HookRuntime.install(method, before, after)
        return method to requireNotNull(interceptor)
    }
    private fun chain(method: Method, args: List<Any?> = emptyList(), proceed: (Array<Any?>) -> Any?): XposedInterface.Chain =
        Proxy.newProxyInstance(javaClass.classLoader, arrayOf(XposedInterface.Chain::class.java)) { _, called, parameters ->
            when (called.name) {
                "getExecutable" -> method
                "getThisObject" -> Targets()
                "getArgs" -> args
                "proceed" -> proceed(if (parameters.isNullOrEmpty()) args.toTypedArray() else parameters[0] as Array<Any?>)
                else -> null
            }
        } as XposedInterface.Chain
    @Test fun rewrittenArgumentsReachOriginalExactlyOnce() {
        val (method, hook) = install("rewrite", before = { args[0] = "new" })
        var calls = 0
        val result = hook.intercept(chain(method, listOf("old")) { calls++; it[0] })
        assertEquals("new", result); assertEquals(1, calls)
    }
    @Test fun nullResultSkipsOriginal() {
        val (method, hook) = install("skip", before = { resultNull() })
        assertNull(hook.intercept(chain(method) { error("must not execute") }))
    }
    @Test fun afterReplacesResultWithoutReplaying() {
        val (method, hook) = install("after", after = { result = "changed" })
        var calls = 0
        assertEquals("changed", hook.intercept(chain(method) { calls++; "original" }))
        assertEquals(1, calls)
    }
    @Test fun brokenBeforeFallsBackToOriginalArguments() {
        val (method, hook) = install("beforeFailure", before = { error("callback") })
        var calls = 0
        assertEquals("original", hook.intercept(chain(method) { calls++; "original" }))
        assertEquals(1, calls)
    }
    @Test fun brokenAfterKeepsOriginalResult() {
        val (method, hook) = install("afterFailure", after = { error("callback") })
        var calls = 0
        assertEquals("original", hook.intercept(chain(method) { calls++; "original" }))
        assertEquals(1, calls)
    }
    @Test fun originalExceptionIsNeverRetried() {
        val (method, hook) = install("originalFailure")
        var calls = 0
        val failure = IllegalStateException("original")
        val actual = assertThrows(IllegalStateException::class.java) {
            hook.intercept(chain(method) { calls++; throw failure })
        }
        assertSame(failure, actual); assertEquals(1, calls)
    }
    @Test fun independentFeaturesCanHookTheSameMethodWithoutDuplicateRegistration() {
        var registrations = 0
        val builder = Proxy.newProxyInstance(javaClass.classLoader, arrayOf(XposedInterface.HookBuilder::class.java)) { proxy, called, _ ->
            if (called.name == "intercept") null else proxy
        }
        HookRuntime.api = Proxy.newProxyInstance(javaClass.classLoader, arrayOf(XposedInterface::class.java)) { _, called, _ ->
            if (called.name == "hook") { registrations++; builder } else null
        } as XposedInterface
        val method = Targets::class.java.getDeclaredMethod("layered")
        val accessibility: HookCall.() -> Unit = { result = "accessibility" }
        val screenshot: HookCall.() -> Unit = { result = "screenshot" }
        HookRuntime.install(method, after = accessibility)
        HookRuntime.install(method, after = screenshot)
        HookRuntime.install(method, after = accessibility)
        assertEquals(2, registrations)
    }
}
