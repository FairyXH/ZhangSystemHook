package kotlinx.coroutines.sync;

/* JADX INFO: compiled from: Semaphore.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0000\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J%\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00062\b\u0010\u0013\u001a\u0004\u0018\u00010\n2\b\u0010\u0014\u001a\u0004\u0018\u00010\nH\u0086\bJ\u0013\u0010\u0015\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0012\u001a\u00020\u0006H\u0086\bJ\u001d\u0010\u0016\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0012\u001a\u00020\u00062\b\u0010\u0014\u001a\u0004\u0018\u00010\nH\u0086\bJ\"\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0012\u001a\u00020\u00062\b\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0016J\u001b\u0010\u001d\u001a\u00020\u00182\u0006\u0010\u0012\u001a\u00020\u00062\b\u0010\u0014\u001a\u0004\u0018\u00010\nH\u0086\bJ\b\u0010\u001e\u001a\u00020\u001fH\u0016R\u0019\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\t¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000f¨\u0006 "}, d2 = {"Lkotlinx/coroutines/sync/SemaphoreSegment;", "Lkotlinx/coroutines/internal/Segment;", "id", "", "prev", "pointers", "", "(JLkotlinx/coroutines/sync/SemaphoreSegment;I)V", "acquirers", "Lkotlinx/atomicfu/AtomicArray;", "", "getAcquirers", "()Lkotlinx/atomicfu/AtomicArray;", "numberOfSlots", "getNumberOfSlots", "()I", "cas", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "expected", "value", "get", "getAndSet", "onCancellation", "", "cause", "", "context", "Lkotlin/coroutines/CoroutineContext;", "set", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class SemaphoreSegment extends kotlinx.coroutines.internal.Segment<kotlinx.coroutines.sync.SemaphoreSegment> {
    private final kotlinx.atomicfu.AtomicArray<java.lang.Object> acquirers;

    public SemaphoreSegment(long id, kotlinx.coroutines.sync.SemaphoreSegment prev, int pointers) {
        super(id, prev, pointers);
        this.acquirers = kotlinx.atomicfu.AtomicFU_commonKt.atomicArrayOfNulls(kotlinx.coroutines.sync.SemaphoreKt.SEGMENT_SIZE);
    }

    public final kotlinx.atomicfu.AtomicArray<java.lang.Object> getAcquirers() {
        return this.acquirers;
    }

    @Override // kotlinx.coroutines.internal.Segment
    public int getNumberOfSlots() {
        return kotlinx.coroutines.sync.SemaphoreKt.SEGMENT_SIZE;
    }

    public final java.lang.Object get(int index) {
        return getAcquirers().get(index).getValue();
    }

    public final void set(int index, java.lang.Object value) {
        getAcquirers().get(index).setValue(value);
    }

    public final boolean cas(int index, java.lang.Object expected, java.lang.Object value) {
        return getAcquirers().get(index).compareAndSet(expected, value);
    }

    public final java.lang.Object getAndSet(int index, java.lang.Object value) {
        return getAcquirers().get(index).getAndSet(value);
    }

    @Override // kotlinx.coroutines.internal.Segment
    public void onCancellation(int index, java.lang.Throwable cause, kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        java.lang.Object value$iv = kotlinx.coroutines.sync.SemaphoreKt.CANCELLED;
        getAcquirers().get(index).setValue(value$iv);
        onSlotCleaned();
    }

    public java.lang.String toString() {
        return "SemaphoreSegment[id=" + this.id + ", hashCode=" + hashCode() + "]";
    }
}
