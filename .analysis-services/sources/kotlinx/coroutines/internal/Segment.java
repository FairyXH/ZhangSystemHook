package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: ConcurrentLinkedList.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b \u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00002\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003B\u001f\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00018\u0000\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\r\u0010\u0012\u001a\u00020\rH\u0000¢\u0006\u0002\b\u0013J\"\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\b2\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0019\u001a\u00020\u001aH&J\u0006\u0010\u001b\u001a\u00020\u0015J\r\u0010\u001c\u001a\u00020\rH\u0000¢\u0006\u0002\b\u001dR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\u000eR\u0012\u0010\u000f\u001a\u00020\bX¦\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u001e"}, d2 = {"Lkotlinx/coroutines/internal/Segment;", "S", "Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;", "Lkotlinx/coroutines/NotCompleted;", "id", "", "prev", "pointers", "", "(JLkotlinx/coroutines/internal/Segment;I)V", "cleanedAndPointers", "Lkotlinx/atomicfu/AtomicInt;", "isRemoved", "", "()Z", "numberOfSlots", "getNumberOfSlots", "()I", "decPointers", "decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "onCancellation", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "cause", "", "context", "Lkotlin/coroutines/CoroutineContext;", "onSlotCleaned", "tryIncPointers", "tryIncPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class Segment<S extends kotlinx.coroutines.internal.Segment<S>> extends kotlinx.coroutines.internal.ConcurrentLinkedListNode<S> implements kotlinx.coroutines.NotCompleted {
    private final kotlinx.atomicfu.AtomicInt cleanedAndPointers;
    public final long id;

    public abstract int getNumberOfSlots();

    public abstract void onCancellation(int index, java.lang.Throwable cause, kotlin.coroutines.CoroutineContext context);

    public Segment(long id, S s, int pointers) {
        super(s);
        this.id = id;
        this.cleanedAndPointers = kotlinx.atomicfu.AtomicFU.atomic(pointers << 16);
    }

    @Override // kotlinx.coroutines.internal.ConcurrentLinkedListNode
    public boolean isRemoved() {
        return this.cleanedAndPointers.getValue() == getNumberOfSlots() && !isTail();
    }

    public final boolean tryIncPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        int cur$iv;
        kotlinx.atomicfu.AtomicInt $this$addConditionally$iv = this.cleanedAndPointers;
        do {
            cur$iv = $this$addConditionally$iv.getValue();
            int it = (cur$iv != getNumberOfSlots() || isTail()) ? 1 : 0;
            if (it == 0) {
                return false;
            }
        } while (!$this$addConditionally$iv.compareAndSet(cur$iv, cur$iv + 65536));
        return true;
    }

    public final boolean decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return this.cleanedAndPointers.addAndGet(-65536) == getNumberOfSlots() && !isTail();
    }

    public final void onSlotCleaned() {
        if (this.cleanedAndPointers.incrementAndGet() == getNumberOfSlots()) {
            remove();
        }
    }
}
