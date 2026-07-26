package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: Concurrent.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u001d\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0080\b\u001a\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0000\u001a-\u0010\u000b\u001a\u0002H\f\"\u0004\b\u0000\u0010\f*\u00060\rj\u0002`\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\f0\u0010H\u0080\bø\u0001\u0000¢\u0006\u0002\u0010\u0011\"\u0010\u0010\u0000\u001a\u0004\u0018\u00010\u0001X\u0082\u0004¢\u0006\u0002\n\u0000*\f\b\u0000\u0010\u0012\"\u00020\r2\u00020\r\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0013"}, d2 = {"REMOVE_FUTURE_ON_CANCEL", "Ljava/lang/reflect/Method;", "identitySet", "", "E", "expectedSize", "", "removeFutureOnCancel", "", "executor", "Ljava/util/concurrent/Executor;", "withLock", "T", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/internal/ReentrantLock;", "action", "Lkotlin/Function0;", "(Ljava/util/concurrent/locks/ReentrantLock;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "ReentrantLock", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ConcurrentKt {
    private static final java.lang.reflect.Method REMOVE_FUTURE_ON_CANCEL;

    public static /* synthetic */ void ReentrantLock$annotations() {
    }

    public static final <T> T withLock(java.util.concurrent.locks.ReentrantLock $this$withLock, kotlin.jvm.functions.Function0<? extends T> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$withLock, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        java.util.concurrent.locks.ReentrantLock reentrantLock = $this$withLock;
        reentrantLock.lock();
        try {
            return action.invoke();
        } finally {
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
            reentrantLock.unlock();
            kotlin.jvm.internal.InlineMarker.finallyEnd(1);
        }
    }

    public static final <E> java.util.Set<E> identitySet(int expectedSize) {
        java.util.Set<E> setNewSetFromMap = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap(expectedSize));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(setNewSetFromMap, "newSetFromMap(...)");
        return setNewSetFromMap;
    }

    static {
        java.lang.reflect.Method method;
        try {
            method = java.util.concurrent.ScheduledThreadPoolExecutor.class.getMethod("setRemoveOnCancelPolicy", java.lang.Boolean.TYPE);
        } catch (java.lang.Throwable th) {
            method = null;
        }
        REMOVE_FUTURE_ON_CANCEL = method;
    }

    public static final boolean removeFutureOnCancel(java.util.concurrent.Executor executor) {
        java.lang.reflect.Method method;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(executor, "executor");
        try {
            java.util.concurrent.ScheduledThreadPoolExecutor executor2 = executor instanceof java.util.concurrent.ScheduledThreadPoolExecutor ? (java.util.concurrent.ScheduledThreadPoolExecutor) executor : null;
            if (executor2 == null || (method = REMOVE_FUTURE_ON_CANCEL) == null) {
                return false;
            }
            method.invoke(executor2, true);
            return true;
        } catch (java.lang.Throwable th) {
            return false;
        }
    }
}
