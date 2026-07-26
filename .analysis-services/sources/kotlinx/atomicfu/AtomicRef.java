package kotlinx.atomicfu;

/* JADX INFO: compiled from: AtomicFU.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 \u001e*\u0004\b\u0000\u0010\u00012\u00020\u0002:\u0001\u001eB\u0017\b\u0000\u0012\u0006\u0010\u0003\u001a\u00028\u0000\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00028\u00002\u0006\u0010\u0011\u001a\u00028\u0000¢\u0006\u0002\u0010\u0012J\u0013\u0010\u0013\u001a\u00028\u00002\u0006\u0010\u0003\u001a\u00028\u0000¢\u0006\u0002\u0010\u0014J$\u0010\t\u001a\u00028\u00002\b\u0010\u0015\u001a\u0004\u0018\u00010\u00022\n\u0010\u0016\u001a\u0006\u0012\u0002\b\u00030\u0017H\u0086\n¢\u0006\u0002\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0003\u001a\u00028\u0000¢\u0006\u0002\u0010\fJ,\u0010\u000b\u001a\u00020\u001a2\b\u0010\u0015\u001a\u0004\u0018\u00010\u00022\n\u0010\u0016\u001a\u0006\u0012\u0002\b\u00030\u00172\u0006\u0010\u0003\u001a\u00028\u0000H\u0086\n¢\u0006\u0002\u0010\u001bJ\b\u0010\u001c\u001a\u00020\u001dH\u0016R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR&\u0010\u0003\u001a\u00028\u00002\u0006\u0010\u0003\u001a\u00028\u0000@FX\u0086\u000e¢\u0006\u0010\n\u0002\u0010\r\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f¨\u0006\u001f"}, d2 = {"Lkotlinx/atomicfu/AtomicRef;", "T", "", "value", "trace", "Lkotlinx/atomicfu/TraceBase;", "(Ljava/lang/Object;Lkotlinx/atomicfu/TraceBase;)V", "getTrace", "()Lkotlinx/atomicfu/TraceBase;", "getValue", "()Ljava/lang/Object;", "setValue", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "compareAndSet", "", "expect", "update", "(Ljava/lang/Object;Ljava/lang/Object;)Z", "getAndSet", "(Ljava/lang/Object;)Ljava/lang/Object;", "thisRef", "property", "Lkotlin/reflect/KProperty;", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;)Ljava/lang/Object;", "lazySet", "", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;Ljava/lang/Object;)V", "toString", "", "Companion", "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AtomicRef<T> {
    private static final kotlinx.atomicfu.AtomicRef.Companion Companion = new kotlinx.atomicfu.AtomicRef.Companion(null);
    private static final java.util.concurrent.atomic.AtomicReferenceFieldUpdater<kotlinx.atomicfu.AtomicRef<?>, java.lang.Object> FU = java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater(kotlinx.atomicfu.AtomicRef.class, java.lang.Object.class, "value");
    private final kotlinx.atomicfu.TraceBase trace;
    private volatile T value;

    public AtomicRef(T t, kotlinx.atomicfu.TraceBase trace) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(trace, "trace");
        this.trace = trace;
        this.value = t;
    }

    public final kotlinx.atomicfu.TraceBase getTrace() {
        return this.trace;
    }

    public final T getValue() {
        return this.value;
    }

    public final void setValue(T t) {
        this.value = t;
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("set(" + t + ")");
        }
    }

    public final T getValue(java.lang.Object thisRef, kotlin.reflect.KProperty<?> property) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(property, "property");
        return getValue();
    }

    public final void setValue(java.lang.Object thisRef, kotlin.reflect.KProperty<?> property, T value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(property, "property");
        setValue(value);
    }

    public final void lazySet(T value) {
        FU.lazySet(this, value);
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("lazySet(" + value + ")");
        }
    }

    public final boolean compareAndSet(T expect, T update) {
        boolean result = FU.compareAndSet(this, expect, update);
        if (result && this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("CAS(" + expect + ", " + update + ")");
        }
        return result;
    }

    public final T getAndSet(T value) {
        T t = (T) FU.getAndSet(this, value);
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("getAndSet(" + value + "):" + t);
        }
        return t;
    }

    public java.lang.String toString() {
        return java.lang.String.valueOf(this.value);
    }

    /* JADX INFO: compiled from: AtomicFU.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R^\u0010\u0003\u001aR\u0012\u0014\u0012\u0012\u0012\u0002\b\u0003 \u0006*\b\u0012\u0002\b\u0003\u0018\u00010\u00050\u0005\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00010\u0001 \u0006*(\u0012\u0014\u0012\u0012\u0012\u0002\b\u0003 \u0006*\b\u0012\u0002\b\u0003\u0018\u00010\u00050\u0005\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00010\u0001\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lkotlinx/atomicfu/AtomicRef$Companion;", "", "()V", "FU", "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "Lkotlinx/atomicfu/AtomicRef;", "kotlin.jvm.PlatformType", "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
