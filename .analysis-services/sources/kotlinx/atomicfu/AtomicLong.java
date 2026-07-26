package kotlinx.atomicfu;

/* JADX INFO: compiled from: AtomicFU.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 \"2\u00020\u0001:\u0001\"B\u0017\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0003J\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0003J\u0006\u0010\u0013\u001a\u00020\u0003J\u000e\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0003J\u0006\u0010\u0015\u001a\u00020\u0003J\u0006\u0010\u0016\u001a\u00020\u0003J\u000e\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003J\u001f\u0010\t\u001a\u00020\u00032\b\u0010\u0018\u001a\u0004\u0018\u00010\u00012\n\u0010\u0019\u001a\u0006\u0012\u0002\b\u00030\u001aH\u0086\nJ\u0006\u0010\u001b\u001a\u00020\u0003J\u000e\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u0003J\u0011\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u000e\u001a\u00020\u0003H\u0086\nJ\u0011\u0010\u001f\u001a\u00020\u001d2\u0006\u0010\u000e\u001a\u00020\u0003H\u0086\nJ'\u0010\u000b\u001a\u00020\u001d2\b\u0010\u0018\u001a\u0004\u0018\u00010\u00012\n\u0010\u0019\u001a\u0006\u0012\u0002\b\u00030\u001a2\u0006\u0010\u0002\u001a\u00020\u0003H\u0086\nJ\b\u0010 \u001a\u00020!H\u0016R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR$\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f¨\u0006#"}, d2 = {"Lkotlinx/atomicfu/AtomicLong;", "", "value", "", "trace", "Lkotlinx/atomicfu/TraceBase;", "(JLkotlinx/atomicfu/TraceBase;)V", "getTrace", "()Lkotlinx/atomicfu/TraceBase;", "getValue", "()J", "setValue", "(J)V", "addAndGet", "delta", "compareAndSet", "", "expect", "update", "decrementAndGet", "getAndAdd", "getAndDecrement", "getAndIncrement", "getAndSet", "thisRef", "property", "Lkotlin/reflect/KProperty;", "incrementAndGet", "lazySet", "", "minusAssign", "plusAssign", "toString", "", "Companion", "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AtomicLong {
    private static final kotlinx.atomicfu.AtomicLong.Companion Companion = new kotlinx.atomicfu.AtomicLong.Companion(null);
    private static final java.util.concurrent.atomic.AtomicLongFieldUpdater<kotlinx.atomicfu.AtomicLong> FU = java.util.concurrent.atomic.AtomicLongFieldUpdater.newUpdater(kotlinx.atomicfu.AtomicLong.class, "value");
    private final kotlinx.atomicfu.TraceBase trace;
    private volatile long value;

    public AtomicLong(long value, kotlinx.atomicfu.TraceBase trace) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(trace, "trace");
        this.trace = trace;
        this.value = value;
    }

    public final kotlinx.atomicfu.TraceBase getTrace() {
        return this.trace;
    }

    public final long getValue() {
        return this.value;
    }

    public final void setValue(long value) {
        this.value = value;
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("set(" + value + ")");
        }
    }

    public final long getValue(java.lang.Object thisRef, kotlin.reflect.KProperty<?> property) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(property, "property");
        return getValue();
    }

    public final void setValue(java.lang.Object thisRef, kotlin.reflect.KProperty<?> property, long value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(property, "property");
        setValue(value);
    }

    public final void lazySet(long value) {
        FU.lazySet(this, value);
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("lazySet(" + value + ")");
        }
    }

    public final boolean compareAndSet(long expect, long update) {
        boolean result = FU.compareAndSet(this, expect, update);
        if (result && this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("CAS(" + expect + ", " + update + ")");
        }
        return result;
    }

    public final long getAndSet(long value) {
        long oldValue = FU.getAndSet(this, value);
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("getAndSet(" + value + "):" + oldValue);
        }
        return oldValue;
    }

    public final long getAndIncrement() {
        long oldValue = FU.getAndIncrement(this);
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("getAndInc():" + oldValue);
        }
        return oldValue;
    }

    public final long getAndDecrement() {
        long oldValue = FU.getAndDecrement(this);
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("getAndDec():" + oldValue);
        }
        return oldValue;
    }

    public final long getAndAdd(long delta) {
        long oldValue = FU.getAndAdd(this, delta);
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("getAndAdd(" + delta + "):" + oldValue);
        }
        return oldValue;
    }

    public final long addAndGet(long delta) {
        long newValue = FU.addAndGet(this, delta);
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("addAndGet(" + delta + "):" + newValue);
        }
        return newValue;
    }

    public final long incrementAndGet() {
        long newValue = FU.incrementAndGet(this);
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("incAndGet():" + newValue);
        }
        return newValue;
    }

    public final long decrementAndGet() {
        long newValue = FU.decrementAndGet(this);
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("decAndGet():" + newValue);
        }
        return newValue;
    }

    public final void plusAssign(long delta) {
        getAndAdd(delta);
    }

    public final void minusAssign(long delta) {
        getAndAdd(-delta);
    }

    public java.lang.String toString() {
        return java.lang.String.valueOf(this.value);
    }

    /* JADX INFO: compiled from: AtomicFU.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R2\u0010\u0003\u001a&\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u0005 \u0006*\u0012\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u0005\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lkotlinx/atomicfu/AtomicLong$Companion;", "", "()V", "FU", "Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;", "Lkotlinx/atomicfu/AtomicLong;", "kotlin.jvm.PlatformType", "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
