package kotlinx.atomicfu;

/* JADX INFO: compiled from: Trace.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\fH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\fH\u0016J \u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\fH\u0016J(\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\fH\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0018\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\r¨\u0006\u0017"}, d2 = {"Lkotlinx/atomicfu/TraceImpl;", "Lkotlinx/atomicfu/TraceBase;", "size", "", "format", "Lkotlinx/atomicfu/TraceFormat;", "(ILkotlinx/atomicfu/TraceFormat;)V", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "Ljava/util/concurrent/atomic/AtomicInteger;", "mask", "trace", "", "", "[Ljava/lang/Object;", "append", "", "event", "event1", "event2", "event3", "event4", "toString", "", "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class TraceImpl extends kotlinx.atomicfu.TraceBase {
    private final kotlinx.atomicfu.TraceFormat format;
    private final java.util.concurrent.atomic.AtomicInteger index;
    private final int mask;
    private final int size;
    private final java.lang.Object[] trace;

    public TraceImpl(int size, kotlinx.atomicfu.TraceFormat format) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        this.format = format;
        if (!(size >= 1)) {
            throw new java.lang.IllegalArgumentException("Failed requirement.".toString());
        }
        this.size = java.lang.Integer.highestOneBit((size << 1) - 1);
        this.mask = this.size - 1;
        this.trace = new java.lang.Object[this.size];
        this.index = new java.util.concurrent.atomic.AtomicInteger(0);
    }

    @Override // kotlinx.atomicfu.TraceBase
    public void append(java.lang.Object event) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event, "event");
        int i = this.index.getAndIncrement();
        this.trace[this.mask & i] = event;
    }

    @Override // kotlinx.atomicfu.TraceBase
    public void append(java.lang.Object event1, java.lang.Object event2) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event1, "event1");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event2, "event2");
        int i = this.index.getAndAdd(2);
        this.trace[this.mask & i] = event1;
        this.trace[(i + 1) & this.mask] = event2;
    }

    @Override // kotlinx.atomicfu.TraceBase
    public void append(java.lang.Object event1, java.lang.Object event2, java.lang.Object event3) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event1, "event1");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event2, "event2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event3, "event3");
        int i = this.index.getAndAdd(3);
        this.trace[this.mask & i] = event1;
        this.trace[(i + 1) & this.mask] = event2;
        this.trace[(i + 2) & this.mask] = event3;
    }

    @Override // kotlinx.atomicfu.TraceBase
    public void append(java.lang.Object event1, java.lang.Object event2, java.lang.Object event3, java.lang.Object event4) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event1, "event1");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event2, "event2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event3, "event3");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event4, "event4");
        int i = this.index.getAndAdd(4);
        this.trace[this.mask & i] = event1;
        this.trace[(i + 1) & this.mask] = event2;
        this.trace[(i + 2) & this.mask] = event3;
        this.trace[(i + 3) & this.mask] = event4;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder $this$toString_u24lambda_u240 = new java.lang.StringBuilder();
        int index = this.index.get();
        int start = this.mask & index;
        int i = index > this.size ? index - this.size : 0;
        int pos = start;
        int cnt = 0;
        do {
            java.lang.Object s = this.trace[pos];
            if (s != null) {
                int cnt2 = cnt + 1;
                if (cnt > 0) {
                    $this$toString_u24lambda_u240.append('\n');
                }
                $this$toString_u24lambda_u240.append(this.format.format(i, s));
                i++;
                cnt = cnt2;
            }
            int cnt3 = pos + 1;
            pos = cnt3 & this.mask;
        } while (pos != start);
        java.lang.String string = $this$toString_u24lambda_u240.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }
}
