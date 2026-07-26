package kotlinx.coroutines.debug.internal;

/* JADX INFO: compiled from: DebuggerInfo.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000e\b\u0001\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0015\u0010\u0007\u001a\u0004\u0018\u00010\b¢\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\nR\u0013\u0010\f\u001a\u0004\u0018\u00010\r¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0013\u0010\u0015\u001a\u0004\u0018\u00010\r¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000fR\u0013\u0010\u0017\u001a\u0004\u0018\u00010\r¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u000fR\u0013\u0010\u0019\u001a\u0004\u0018\u00010\r¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u000fR\u0011\u0010\u001b\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u001e\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u000f¨\u0006 "}, d2 = {"Lkotlinx/coroutines/debug/internal/DebuggerInfo;", "Ljava/io/Serializable;", "source", "Lkotlinx/coroutines/debug/internal/DebugCoroutineInfoImpl;", "context", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlinx/coroutines/debug/internal/DebugCoroutineInfoImpl;Lkotlin/coroutines/CoroutineContext;)V", "coroutineId", "", "getCoroutineId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "dispatcher", "", "getDispatcher", "()Ljava/lang/String;", "lastObservedStackTrace", "", "Ljava/lang/StackTraceElement;", "getLastObservedStackTrace", "()Ljava/util/List;", "lastObservedThreadName", "getLastObservedThreadName", "lastObservedThreadState", "getLastObservedThreadState", "name", "getName", "sequenceNumber", "getSequenceNumber", "()J", "state", "getState", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class DebuggerInfo implements java.io.Serializable {
    private final java.lang.Long coroutineId;
    private final java.lang.String dispatcher;
    private final java.util.List<java.lang.StackTraceElement> lastObservedStackTrace;
    private final java.lang.String lastObservedThreadName;
    private final java.lang.String lastObservedThreadState;
    private final java.lang.String name;
    private final long sequenceNumber;
    private final java.lang.String state;

    public DebuggerInfo(kotlinx.coroutines.debug.internal.DebugCoroutineInfo source, kotlin.coroutines.CoroutineContext context) {
        java.lang.Thread.State state;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlinx.coroutines.CoroutineId coroutineId = (kotlinx.coroutines.CoroutineId) context.get(kotlinx.coroutines.CoroutineId.INSTANCE);
        this.coroutineId = coroutineId != null ? java.lang.Long.valueOf(coroutineId.getId()) : null;
        kotlin.coroutines.ContinuationInterceptor continuationInterceptor = (kotlin.coroutines.ContinuationInterceptor) context.get(kotlin.coroutines.ContinuationInterceptor.INSTANCE);
        this.dispatcher = continuationInterceptor != null ? continuationInterceptor.toString() : null;
        kotlinx.coroutines.CoroutineName coroutineName = (kotlinx.coroutines.CoroutineName) context.get(kotlinx.coroutines.CoroutineName.INSTANCE);
        this.name = coroutineName != null ? coroutineName.getName() : null;
        this.state = source.get_state();
        java.lang.Thread thread = source.lastObservedThread;
        this.lastObservedThreadState = (thread == null || (state = thread.getState()) == null) ? null : state.toString();
        java.lang.Thread thread2 = source.lastObservedThread;
        this.lastObservedThreadName = thread2 != null ? thread2.getName() : null;
        this.lastObservedStackTrace = source.lastObservedStackTrace$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        this.sequenceNumber = source.sequenceNumber;
    }

    public final java.lang.Long getCoroutineId() {
        return this.coroutineId;
    }

    public final java.lang.String getDispatcher() {
        return this.dispatcher;
    }

    public final java.lang.String getName() {
        return this.name;
    }

    public final java.lang.String getState() {
        return this.state;
    }

    public final java.lang.String getLastObservedThreadState() {
        return this.lastObservedThreadState;
    }

    public final java.lang.String getLastObservedThreadName() {
        return this.lastObservedThreadName;
    }

    public final java.util.List<java.lang.StackTraceElement> getLastObservedStackTrace() {
        return this.lastObservedStackTrace;
    }

    public final long getSequenceNumber() {
        return this.sequenceNumber;
    }
}
