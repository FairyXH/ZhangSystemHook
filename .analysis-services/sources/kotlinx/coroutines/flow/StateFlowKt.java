package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: StateFlow.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000@\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\u001f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0000\u0010\u00052\u0006\u0010\u0006\u001a\u0002H\u0005¢\u0006\u0002\u0010\u0007\u001a6\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\u00050\t\"\u0004\b\u0000\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u00050\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0000\u001a5\u0010\u0011\u001a\u0002H\u0005\"\u0004\b\u0000\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u00050\u00042\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u0002H\u0005\u0012\u0004\u0012\u0002H\u00050\u0013H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a0\u0010\u0015\u001a\u00020\u0016\"\u0004\b\u0000\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u00050\u00042\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u0002H\u0005\u0012\u0004\u0012\u0002H\u00050\u0013H\u0086\bø\u0001\u0000\u001a5\u0010\u0017\u001a\u0002H\u0005\"\u0004\b\u0000\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u00050\u00042\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u0002H\u0005\u0012\u0004\u0012\u0002H\u00050\u0013H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u0014\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0018"}, d2 = {"NONE", "Lkotlinx/coroutines/internal/Symbol;", "PENDING", "MutableStateFlow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "T", "value", "(Ljava/lang/Object;)Lkotlinx/coroutines/flow/MutableStateFlow;", "fuseStateFlow", "Lkotlinx/coroutines/flow/Flow;", "Lkotlinx/coroutines/flow/StateFlow;", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "getAndUpdate", "function", "Lkotlin/Function1;", "(Lkotlinx/coroutines/flow/MutableStateFlow;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "update", "", "updateAndGet", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class StateFlowKt {
    private static final kotlinx.coroutines.internal.Symbol NONE = new kotlinx.coroutines.internal.Symbol("NONE");
    private static final kotlinx.coroutines.internal.Symbol PENDING = new kotlinx.coroutines.internal.Symbol("PENDING");

    public static final <T> kotlinx.coroutines.flow.MutableStateFlow<T> MutableStateFlow(T t) {
        return new kotlinx.coroutines.flow.StateFlowImpl(t == null ? kotlinx.coroutines.flow.internal.NullSurrogateKt.NULL : t);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> T updateAndGet(kotlinx.coroutines.flow.MutableStateFlow<T> mutableStateFlow, kotlin.jvm.functions.Function1<? super T, ? extends T> function) {
        java.lang.Object prevValue;
        T tInvoke;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mutableStateFlow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            prevValue = mutableStateFlow.getValue();
            tInvoke = function.invoke(prevValue);
        } while (!mutableStateFlow.compareAndSet(prevValue, tInvoke));
        return tInvoke;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [T, java.lang.Object] */
    public static final <T> T getAndUpdate(kotlinx.coroutines.flow.MutableStateFlow<T> mutableStateFlow, kotlin.jvm.functions.Function1<? super T, ? extends T> function) {
        ?? r1;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mutableStateFlow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            r1 = (java.lang.Object) mutableStateFlow.getValue();
        } while (!mutableStateFlow.compareAndSet(r1, function.invoke(r1)));
        return r1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> void update(kotlinx.coroutines.flow.MutableStateFlow<T> mutableStateFlow, kotlin.jvm.functions.Function1<? super T, ? extends T> function) {
        java.lang.Object prevValue;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mutableStateFlow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function, "function");
        do {
            prevValue = mutableStateFlow.getValue();
        } while (!mutableStateFlow.compareAndSet(prevValue, function.invoke(prevValue)));
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> fuseStateFlow(kotlinx.coroutines.flow.StateFlow<? extends T> stateFlow, kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(stateFlow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if ((capacity != -1 ? 1 : 0) == 0) {
                throw new java.lang.AssertionError();
            }
        }
        if (((capacity >= 0 && capacity < 2) || capacity == -2) && onBufferOverflow == kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST) {
            return stateFlow;
        }
        return kotlinx.coroutines.flow.SharedFlowKt.fuseSharedFlow(stateFlow, context, capacity, onBufferOverflow);
    }
}
