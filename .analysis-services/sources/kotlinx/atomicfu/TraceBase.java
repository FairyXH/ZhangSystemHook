package kotlinx.atomicfu;

/* JADX INFO: compiled from: Trace.common.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0017\u0018\u00002\u00020\u0001:\u0001\fB\u0007\b\u0000¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0001H\u0017J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0001H\u0017J \u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\u0001H\u0017J(\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u0001H\u0017J\u001a\u0010\n\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0087\nø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\r"}, d2 = {"Lkotlinx/atomicfu/TraceBase;", "", "()V", "append", "", "event", "event1", "event2", "event3", "event4", "invoke", "Lkotlin/Function0;", com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG, "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class TraceBase {
    public void append(java.lang.Object event) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event, "event");
    }

    public void append(java.lang.Object event1, java.lang.Object event2) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event1, "event1");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event2, "event2");
    }

    public void append(java.lang.Object event1, java.lang.Object event2, java.lang.Object event3) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event1, "event1");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event2, "event2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event3, "event3");
    }

    public void append(java.lang.Object event1, java.lang.Object event2, java.lang.Object event3, java.lang.Object event4) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event1, "event1");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event2, "event2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event3, "event3");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event4, "event4");
    }

    private final void invoke(kotlin.jvm.functions.Function0<? extends java.lang.Object> event) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event, "event");
        append(event.invoke());
    }

    /* JADX INFO: compiled from: Trace.common.kt */
    @kotlin.Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lkotlinx/atomicfu/TraceBase$None;", "Lkotlinx/atomicfu/TraceBase;", "()V", "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class None extends kotlinx.atomicfu.TraceBase {
        public static final kotlinx.atomicfu.TraceBase.None INSTANCE = new kotlinx.atomicfu.TraceBase.None();

        private None() {
        }
    }
}
