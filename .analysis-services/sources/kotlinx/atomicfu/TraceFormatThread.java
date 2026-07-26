package kotlinx.atomicfu;

/* JADX INFO: compiled from: Trace.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, d2 = {"Lkotlinx/atomicfu/TraceFormatThread;", "Lkotlinx/atomicfu/TraceFormat;", "()V", "format", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "event", "", "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class TraceFormatThread extends kotlinx.atomicfu.TraceFormat {
    @Override // kotlinx.atomicfu.TraceFormat
    public java.lang.String format(int index, java.lang.Object event) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event, "event");
        return index + ": [" + java.lang.Thread.currentThread().getName() + "] " + event;
    }
}
