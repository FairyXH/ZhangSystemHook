package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: ThreadSafeHeap.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\bg\u0018\u00002\u00020\u0001R\u001e\u0010\u0002\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u0018\u0010\b\u001a\u00020\tX¦\u000e¢\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/internal/ThreadSafeHeapNode;", "", "heap", "Lkotlinx/coroutines/internal/ThreadSafeHeap;", "getHeap", "()Lkotlinx/coroutines/internal/ThreadSafeHeap;", "setHeap", "(Lkotlinx/coroutines/internal/ThreadSafeHeap;)V", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "getIndex", "()I", "setIndex", "(I)V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface ThreadSafeHeapNode {
    kotlinx.coroutines.internal.ThreadSafeHeap<?> getHeap();

    int getIndex();

    void setHeap(kotlinx.coroutines.internal.ThreadSafeHeap<?> threadSafeHeap);

    void setIndex(int i);
}
