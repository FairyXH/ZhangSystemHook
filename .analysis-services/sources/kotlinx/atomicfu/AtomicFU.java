package kotlinx.atomicfu;

/* JADX INFO: compiled from: AtomicFU.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000*\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\u001a\u001f\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002¢\u0006\u0002\u0010\u0004\u001a)\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u00022\b\b\u0002\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007\u001a\u000e\u0010\u0000\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\t\u001a\u0018\u0010\u0000\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\t2\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u001a\u000e\u0010\u0000\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\u000b\u001a\u0018\u0010\u0000\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\u000b2\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u001a\u000e\u0010\u0000\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\r\u001a\u0018\u0010\u0000\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\r2\b\b\u0002\u0010\u0005\u001a\u00020\u0006¨\u0006\u000e"}, d2 = {"atomic", "Lkotlinx/atomicfu/AtomicRef;", "T", "initial", "(Ljava/lang/Object;)Lkotlinx/atomicfu/AtomicRef;", "trace", "Lkotlinx/atomicfu/TraceBase;", "(Ljava/lang/Object;Lkotlinx/atomicfu/TraceBase;)Lkotlinx/atomicfu/AtomicRef;", "Lkotlinx/atomicfu/AtomicBoolean;", "", "Lkotlinx/atomicfu/AtomicInt;", "", "Lkotlinx/atomicfu/AtomicLong;", "", "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class AtomicFU {
    public static final <T> kotlinx.atomicfu.AtomicRef<T> atomic(T t, kotlinx.atomicfu.TraceBase trace) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(trace, "trace");
        return new kotlinx.atomicfu.AtomicRef<>(t, trace);
    }

    public static /* synthetic */ kotlinx.atomicfu.AtomicRef atomic$default(java.lang.Object obj, kotlinx.atomicfu.TraceBase traceBase, int i, java.lang.Object obj2) {
        if ((i & 2) != 0) {
            traceBase = kotlinx.atomicfu.TraceBase.None.INSTANCE;
        }
        return atomic(obj, traceBase);
    }

    public static final <T> kotlinx.atomicfu.AtomicRef<T> atomic(T t) {
        return atomic(t, kotlinx.atomicfu.TraceBase.None.INSTANCE);
    }

    public static final kotlinx.atomicfu.AtomicInt atomic(int initial, kotlinx.atomicfu.TraceBase trace) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(trace, "trace");
        return new kotlinx.atomicfu.AtomicInt(initial, trace);
    }

    public static /* synthetic */ kotlinx.atomicfu.AtomicInt atomic$default(int i, kotlinx.atomicfu.TraceBase traceBase, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            traceBase = kotlinx.atomicfu.TraceBase.None.INSTANCE;
        }
        return atomic(i, traceBase);
    }

    public static final kotlinx.atomicfu.AtomicInt atomic(int initial) {
        return atomic(initial, (kotlinx.atomicfu.TraceBase) kotlinx.atomicfu.TraceBase.None.INSTANCE);
    }

    public static final kotlinx.atomicfu.AtomicLong atomic(long initial, kotlinx.atomicfu.TraceBase trace) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(trace, "trace");
        return new kotlinx.atomicfu.AtomicLong(initial, trace);
    }

    public static /* synthetic */ kotlinx.atomicfu.AtomicLong atomic$default(long j, kotlinx.atomicfu.TraceBase traceBase, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            traceBase = kotlinx.atomicfu.TraceBase.None.INSTANCE;
        }
        return atomic(j, traceBase);
    }

    public static final kotlinx.atomicfu.AtomicLong atomic(long initial) {
        return atomic(initial, kotlinx.atomicfu.TraceBase.None.INSTANCE);
    }

    public static final kotlinx.atomicfu.AtomicBoolean atomic(boolean initial, kotlinx.atomicfu.TraceBase trace) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(trace, "trace");
        return new kotlinx.atomicfu.AtomicBoolean(initial, trace);
    }

    public static /* synthetic */ kotlinx.atomicfu.AtomicBoolean atomic$default(boolean z, kotlinx.atomicfu.TraceBase traceBase, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            traceBase = kotlinx.atomicfu.TraceBase.None.INSTANCE;
        }
        return atomic(z, traceBase);
    }

    public static final kotlinx.atomicfu.AtomicBoolean atomic(boolean initial) {
        return atomic(initial, kotlinx.atomicfu.TraceBase.None.INSTANCE);
    }
}
