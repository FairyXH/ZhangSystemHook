package _COROUTINE;

/* JADX INFO: compiled from: CoroutineDebugging.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004J\u0006\u0010\u0005\u001a\u00020\u0004¨\u0006\u0006"}, d2 = {"L_COROUTINE/ArtificialStackFrames;", "", "()V", "coroutineBoundary", "Ljava/lang/StackTraceElement;", "coroutineCreation", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ArtificialStackFrames {
    public final java.lang.StackTraceElement coroutineCreation() {
        java.lang.Exception exc = new java.lang.Exception();
        java.lang.String simpleName = _COROUTINE._CREATION.class.getSimpleName();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(simpleName, "getSimpleName(...)");
        return _COROUTINE.CoroutineDebuggingKt.artificialFrame(exc, simpleName);
    }

    public final java.lang.StackTraceElement coroutineBoundary() {
        java.lang.Exception exc = new java.lang.Exception();
        java.lang.String simpleName = _COROUTINE._BOUNDARY.class.getSimpleName();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(simpleName, "getSimpleName(...)");
        return _COROUTINE.CoroutineDebuggingKt.artificialFrame(exc, simpleName);
    }
}
