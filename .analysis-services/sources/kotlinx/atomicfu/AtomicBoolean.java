package kotlinx.atomicfu;

/* JADX INFO: compiled from: AtomicFU.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\u0017\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0016\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0003J\u000e\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\u0003J\u001f\u0010\f\u001a\u00020\u00032\b\u0010\u0014\u001a\u0004\u0018\u00010\u00012\n\u0010\u0015\u001a\u0006\u0012\u0002\b\u00030\u0016H\u0086\nJ\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u000b\u001a\u00020\u0003J'\u0010\u000e\u001a\u00020\u00182\b\u0010\u0014\u001a\u0004\u0018\u00010\u00012\n\u0010\u0015\u001a\u0006\u0012\u0002\b\u00030\u00162\u0006\u0010\u000b\u001a\u00020\u0003H\u0086\nJ\b\u0010\u0019\u001a\u00020\u001aH\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR$\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\u00038F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000f¨\u0006\u001c"}, d2 = {"Lkotlinx/atomicfu/AtomicBoolean;", "", "v", "", "trace", "Lkotlinx/atomicfu/TraceBase;", "(ZLkotlinx/atomicfu/TraceBase;)V", "_value", "", "getTrace", "()Lkotlinx/atomicfu/TraceBase;", "value", "getValue", "()Z", "setValue", "(Z)V", "compareAndSet", "expect", "update", "getAndSet", "thisRef", "property", "Lkotlin/reflect/KProperty;", "lazySet", "", "toString", "", "Companion", "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AtomicBoolean {
    private static final kotlinx.atomicfu.AtomicBoolean.Companion Companion = new kotlinx.atomicfu.AtomicBoolean.Companion(null);
    private static final java.util.concurrent.atomic.AtomicIntegerFieldUpdater<kotlinx.atomicfu.AtomicBoolean> FU = java.util.concurrent.atomic.AtomicIntegerFieldUpdater.newUpdater(kotlinx.atomicfu.AtomicBoolean.class, "_value");
    private volatile int _value;
    private final kotlinx.atomicfu.TraceBase trace;

    public AtomicBoolean(boolean z, kotlinx.atomicfu.TraceBase trace) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(trace, "trace");
        this.trace = trace;
        this._value = z ? 1 : 0;
    }

    public final kotlinx.atomicfu.TraceBase getTrace() {
        return this.trace;
    }

    public final boolean getValue(java.lang.Object thisRef, kotlin.reflect.KProperty<?> property) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(property, "property");
        return getValue();
    }

    public final void setValue(java.lang.Object thisRef, kotlin.reflect.KProperty<?> property, boolean value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(property, "property");
        setValue(value);
    }

    public final boolean getValue() {
        return this._value != 0;
    }

    public final void setValue(boolean z) {
        this._value = z ? 1 : 0;
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("set(" + z + ")");
        }
    }

    public final void lazySet(boolean value) {
        FU.lazySet(this, value ? 1 : 0);
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("lazySet(" + value + ")");
        }
    }

    public final boolean compareAndSet(boolean expect, boolean update) {
        boolean zCompareAndSet = FU.compareAndSet(this, expect ? 1 : 0, update ? 1 : 0);
        if (zCompareAndSet && this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("CAS(" + expect + ", " + update + ")");
        }
        return zCompareAndSet;
    }

    public final boolean getAndSet(boolean value) {
        int andSet = FU.getAndSet(this, value ? 1 : 0);
        if (this.trace != kotlinx.atomicfu.TraceBase.None.INSTANCE) {
            this.trace.append("getAndSet(" + value + "):" + andSet);
        }
        return andSet == 1;
    }

    public java.lang.String toString() {
        return java.lang.String.valueOf(getValue());
    }

    /* JADX INFO: compiled from: AtomicFU.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R2\u0010\u0003\u001a&\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u0005 \u0006*\u0012\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u0005\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lkotlinx/atomicfu/AtomicBoolean$Companion;", "", "()V", "FU", "Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;", "Lkotlinx/atomicfu/AtomicBoolean;", "kotlin.jvm.PlatformType", "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
