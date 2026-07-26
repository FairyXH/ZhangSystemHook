package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: ConcurrentLinkedList.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0002\b\u0006\b \u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00002\u00020\u0002B\u000f\u0012\b\u0010\u0003\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010\u0004J\u0006\u0010\u0017\u001a\u00020\u0018J\u0006\u0010\u0019\u001a\u00020\u000eJ!\u0010\u001a\u001a\u0004\u0018\u00018\u00002\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cH\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u001eJ\u0006\u0010\u001f\u001a\u00020\u0018J\u0013\u0010 \u001a\u00020\u000e2\u0006\u0010!\u001a\u00028\u0000¢\u0006\u0002\u0010\"R\u0016\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\u0004\u0018\u00018\u00008BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00028\u00008BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\nR\u0012\u0010\r\u001a\u00020\u000eX¦\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\u000e8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u000fR\u0013\u0010\u0011\u001a\u0004\u0018\u00018\u00008F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\nR\u0016\u0010\u0013\u001a\u0004\u0018\u00010\u00028BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0013\u0010\u0003\u001a\u0004\u0018\u00018\u00008F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\n\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006#"}, d2 = {"Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;", "N", "", "prev", "(Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;)V", "_next", "Lkotlinx/atomicfu/AtomicRef;", "_prev", "aliveSegmentLeft", "getAliveSegmentLeft", "()Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;", "aliveSegmentRight", "getAliveSegmentRight", "isRemoved", "", "()Z", "isTail", "next", "getNext", "nextOrClosed", "getNextOrClosed", "()Ljava/lang/Object;", "getPrev", "cleanPrev", "", "markAsClosed", "nextOrIfClosed", "onClosedAction", "Lkotlin/Function0;", "", "(Lkotlin/jvm/functions/Function0;)Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;", "remove", "trySetNext", "value", "(Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;)Z", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class ConcurrentLinkedListNode<N extends kotlinx.coroutines.internal.ConcurrentLinkedListNode<N>> {
    private final kotlinx.atomicfu.AtomicRef<java.lang.Object> _next = kotlinx.atomicfu.AtomicFU.atomic((java.lang.Object) null);
    private final kotlinx.atomicfu.AtomicRef<N> _prev;

    public abstract boolean isRemoved();

    public ConcurrentLinkedListNode(N n) {
        this._prev = kotlinx.atomicfu.AtomicFU.atomic(n);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object getNextOrClosed() {
        return this._next.getValue();
    }

    public final N nextOrIfClosed(kotlin.jvm.functions.Function0 onClosedAction) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onClosedAction, "onClosedAction");
        java.lang.Object it = getNextOrClosed();
        if (it == kotlinx.coroutines.internal.ConcurrentLinkedListKt.CLOSED) {
            onClosedAction.invoke();
            throw new kotlin.KotlinNothingValueException();
        }
        return (N) it;
    }

    public final N getNext() {
        java.lang.Object it$iv = getNextOrClosed();
        if (it$iv == kotlinx.coroutines.internal.ConcurrentLinkedListKt.CLOSED) {
            return null;
        }
        return (N) it$iv;
    }

    public final boolean trySetNext(N value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        return this._next.compareAndSet(null, value);
    }

    public final boolean isTail() {
        return getNext() == null;
    }

    public final N getPrev() {
        return this._prev.getValue();
    }

    public final void cleanPrev() {
        this._prev.lazySet(null);
    }

    public final boolean markAsClosed() {
        return this._next.compareAndSet(null, kotlinx.coroutines.internal.ConcurrentLinkedListKt.CLOSED);
    }

    public final void remove() {
        java.lang.Object cur$iv;
        kotlinx.coroutines.internal.ConcurrentLinkedListNode it;
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(isRemoved() || isTail())) {
                throw new java.lang.AssertionError();
            }
        }
        if (isTail()) {
            return;
        }
        while (true) {
            kotlinx.coroutines.internal.ConcurrentLinkedListNode prev = getAliveSegmentLeft();
            kotlinx.coroutines.internal.ConcurrentLinkedListNode next = getAliveSegmentRight();
            kotlinx.atomicfu.AtomicRef $this$update$iv = next._prev;
            do {
                cur$iv = $this$update$iv.getValue();
                kotlinx.coroutines.internal.ConcurrentLinkedListNode it2 = (kotlinx.coroutines.internal.ConcurrentLinkedListNode) cur$iv;
                it = it2 == null ? null : prev;
            } while (!$this$update$iv.compareAndSet(cur$iv, it));
            if (prev != null) {
                prev._next.setValue(next);
            }
            if (!next.isRemoved() || next.isTail()) {
                if (prev == null || !prev.isRemoved()) {
                    return;
                }
            }
        }
    }

    private final N getAliveSegmentLeft() {
        N value = (N) getPrev();
        while (value != null && value.isRemoved()) {
            value = value._prev.getValue();
        }
        return value;
    }

    private final N getAliveSegmentRight() {
        kotlinx.coroutines.internal.ConcurrentLinkedListNode next;
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !(!isTail())) {
            throw new java.lang.AssertionError();
        }
        N n = (N) getNext();
        kotlin.jvm.internal.Intrinsics.checkNotNull(n);
        while (n.isRemoved() && (next = n.getNext()) != null) {
            n = (N) next;
        }
        return n;
    }
}
