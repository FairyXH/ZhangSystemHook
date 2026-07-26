package kotlinx.atomicfu;

/* JADX INFO: compiled from: Trace.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016J\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0016J \u0010\u0006\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\tH\u0016J(\u0010\u0006\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\tH\u0016J\b\u0010\u000e\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lkotlinx/atomicfu/NamedTrace;", "Lkotlinx/atomicfu/TraceBase;", "trace", "name", "", "(Lkotlinx/atomicfu/TraceBase;Ljava/lang/String;)V", "append", "", "event", "", "event1", "event2", "event3", "event4", "toString", "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class NamedTrace extends kotlinx.atomicfu.TraceBase {
    private final java.lang.String name;
    private final kotlinx.atomicfu.TraceBase trace;

    public NamedTrace(kotlinx.atomicfu.TraceBase trace, java.lang.String name) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(trace, "trace");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
        this.trace = trace;
        this.name = name;
    }

    @Override // kotlinx.atomicfu.TraceBase
    public void append(java.lang.Object event) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event, "event");
        this.trace.append(this.name + "." + event);
    }

    @Override // kotlinx.atomicfu.TraceBase
    public void append(java.lang.Object event1, java.lang.Object event2) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event1, "event1");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event2, "event2");
        this.trace.append(this.name + "." + event1, this.name + "." + event2);
    }

    @Override // kotlinx.atomicfu.TraceBase
    public void append(java.lang.Object event1, java.lang.Object event2, java.lang.Object event3) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event1, "event1");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event2, "event2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event3, "event3");
        this.trace.append(this.name + "." + event1, this.name + "." + event2, this.name + "." + event3);
    }

    @Override // kotlinx.atomicfu.TraceBase
    public void append(java.lang.Object event1, java.lang.Object event2, java.lang.Object event3, java.lang.Object event4) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event1, "event1");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event2, "event2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event3, "event3");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event4, "event4");
        this.trace.append(this.name + "." + event1, this.name + "." + event2, this.name + "." + event3, this.name + "." + event4);
    }

    public java.lang.String toString() {
        return this.trace.toString();
    }
}
