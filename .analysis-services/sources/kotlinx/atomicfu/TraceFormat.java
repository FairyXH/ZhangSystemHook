package kotlinx.atomicfu;

/* JADX INFO: compiled from: TraceFormat.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0017\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0001H\u0017¨\u0006\b"}, d2 = {"Lkotlinx/atomicfu/TraceFormat;", "", "()V", "format", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "event", "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class TraceFormat {
    public java.lang.String format(int index, java.lang.Object event) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(event, "event");
        return index + ": " + event;
    }
}
