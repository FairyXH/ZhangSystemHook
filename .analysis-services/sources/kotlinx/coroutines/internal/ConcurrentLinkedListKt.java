package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: ConcurrentLinkedList.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000N\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u001a8\u0010\u0004\u001a\u00020\u0005*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00032!\u0010\b\u001a\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0004\u0012\u00020\u00050\tH\u0082\b\u001a!\u0010\r\u001a\u0002H\u000e\"\u000e\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\u000f*\u0002H\u000eH\u0000¢\u0006\u0002\u0010\u0010\u001a{\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00130\u0012\"\u000e\b\u0000\u0010\u0013*\b\u0012\u0004\u0012\u0002H\u00130\u0014*\b\u0012\u0004\u0012\u0002H\u00130\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u0002H\u001328\b\b\u0010\u0019\u001a2\u0012\u0013\u0012\u00110\u0017¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\u0016\u0012\u0013\u0012\u0011H\u0013¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\u001b\u0012\u0004\u0012\u0002H\u00130\u001aH\u0080\bø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001ag\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u00130\u0012\"\u000e\b\u0000\u0010\u0013*\b\u0012\u0004\u0012\u0002H\u00130\u0014*\u0002H\u00132\u0006\u0010\u0016\u001a\u00020\u001726\u0010\u0019\u001a2\u0012\u0013\u0012\u00110\u0017¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\u0016\u0012\u0013\u0012\u0011H\u0013¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\u001b\u0012\u0004\u0012\u0002H\u00130\u001aH\u0000¢\u0006\u0002\u0010\u001e\u001a0\u0010\u001f\u001a\u00020\u0005\"\u000e\b\u0000\u0010\u0013*\b\u0012\u0004\u0012\u0002H\u00130\u0014*\b\u0012\u0004\u0012\u0002H\u00130\u00152\u0006\u0010 \u001a\u0002H\u0013H\u0080\b¢\u0006\u0002\u0010!\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0082T¢\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\""}, d2 = {"CLOSED", "Lkotlinx/coroutines/internal/Symbol;", "POINTERS_SHIFT", "", "addConditionally", "", "Lkotlinx/atomicfu/AtomicInt;", "delta", "condition", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "cur", "close", "N", "Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;", "(Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;)Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;", "findSegmentAndMoveForward", "Lkotlinx/coroutines/internal/SegmentOrClosed;", "S", "Lkotlinx/coroutines/internal/Segment;", "Lkotlinx/atomicfu/AtomicRef;", "id", "", "startFrom", "createNewSegment", "Lkotlin/Function2;", "prev", "(Lkotlinx/atomicfu/AtomicRef;JLkotlinx/coroutines/internal/Segment;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "findSegmentInternal", "(Lkotlinx/coroutines/internal/Segment;JLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "moveForward", "to", "(Lkotlinx/atomicfu/AtomicRef;Lkotlinx/coroutines/internal/Segment;)Z", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ConcurrentLinkedListKt {
    private static final kotlinx.coroutines.internal.Symbol CLOSED = new kotlinx.coroutines.internal.Symbol("CLOSED");
    private static final int POINTERS_SHIFT = 16;

    public static final <S extends kotlinx.coroutines.internal.Segment<S>> java.lang.Object findSegmentInternal(S s, long id, kotlin.jvm.functions.Function2<? super java.lang.Long, ? super S, ? extends S> createNewSegment) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(s, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(createNewSegment, "createNewSegment");
        kotlinx.coroutines.internal.Segment cur = s;
        while (true) {
            if (cur.id < id || cur.isRemoved()) {
                kotlinx.coroutines.internal.ConcurrentLinkedListNode this_$iv = cur;
                java.lang.Object it$iv = this_$iv.getNextOrClosed();
                if (it$iv == CLOSED) {
                    return kotlinx.coroutines.internal.SegmentOrClosed.m12867constructorimpl(CLOSED);
                }
                kotlinx.coroutines.internal.Segment next = (kotlinx.coroutines.internal.Segment) ((kotlinx.coroutines.internal.ConcurrentLinkedListNode) it$iv);
                if (next != null) {
                    cur = next;
                } else {
                    kotlinx.coroutines.internal.Segment newTail = createNewSegment.invoke(java.lang.Long.valueOf(cur.id + 1), cur);
                    if (cur.trySetNext(newTail)) {
                        if (cur.isRemoved()) {
                            cur.remove();
                        }
                        cur = newTail;
                    }
                }
            } else {
                return kotlinx.coroutines.internal.SegmentOrClosed.m12867constructorimpl(cur);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <S extends kotlinx.coroutines.internal.Segment<S>> boolean moveForward(kotlinx.atomicfu.AtomicRef<S> atomicRef, S to) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(atomicRef, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(to, "to");
        while (true) {
            kotlinx.coroutines.internal.Segment cur = (kotlinx.coroutines.internal.Segment) atomicRef.getValue();
            if (cur.id >= to.id) {
                return true;
            }
            if (!to.tryIncPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                return false;
            }
            if (atomicRef.compareAndSet(cur, to)) {
                if (cur.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    cur.remove();
                }
                return true;
            }
            if (to.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                to.remove();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <S extends kotlinx.coroutines.internal.Segment<S>> java.lang.Object findSegmentAndMoveForward(kotlinx.atomicfu.AtomicRef<S> atomicRef, long id, S s, kotlin.jvm.functions.Function2<? super java.lang.Long, ? super S, ? extends S> function2) {
        java.lang.Object s2;
        boolean z;
        S startFrom = s;
        kotlin.jvm.functions.Function2<? super java.lang.Long, ? super S, ? extends S> createNewSegment = function2;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(atomicRef, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(startFrom, "startFrom");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(createNewSegment, "createNewSegment");
        while (true) {
            s2 = findSegmentInternal(startFrom, id, createNewSegment);
            if (kotlinx.coroutines.internal.SegmentOrClosed.m12872isClosedimpl(s2)) {
                break;
            }
            kotlinx.coroutines.internal.Segment to$iv = kotlinx.coroutines.internal.SegmentOrClosed.m12870getSegmentimpl(s2);
            while (true) {
                kotlinx.coroutines.internal.Segment cur$iv = (kotlinx.coroutines.internal.Segment) atomicRef.getValue();
                z = true;
                if (cur$iv.id >= to$iv.id) {
                    break;
                }
                if (!to$iv.tryIncPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    z = false;
                    break;
                }
                if (atomicRef.compareAndSet(cur$iv, to$iv)) {
                    if (cur$iv.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        cur$iv.remove();
                    }
                } else if (to$iv.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    to$iv.remove();
                }
            }
            if (z) {
                break;
            }
            startFrom = s;
            createNewSegment = function2;
        }
        return s2;
    }

    public static final <N extends kotlinx.coroutines.internal.ConcurrentLinkedListNode<N>> N close(N n) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(n, "<this>");
        N n2 = n;
        while (true) {
            java.lang.Object nextOrClosed = n2.getNextOrClosed();
            if (nextOrClosed == CLOSED) {
                return n2;
            }
            kotlinx.coroutines.internal.ConcurrentLinkedListNode concurrentLinkedListNode = (kotlinx.coroutines.internal.ConcurrentLinkedListNode) nextOrClosed;
            if (concurrentLinkedListNode == null) {
                if (n2.markAsClosed()) {
                    return n2;
                }
            } else {
                n2 = (N) concurrentLinkedListNode;
            }
        }
    }

    private static final boolean addConditionally(kotlinx.atomicfu.AtomicInt $this$addConditionally, int delta, kotlin.jvm.functions.Function1<? super java.lang.Integer, java.lang.Boolean> function1) {
        int cur;
        do {
            cur = $this$addConditionally.getValue();
            if (!function1.invoke(java.lang.Integer.valueOf(cur)).booleanValue()) {
                return false;
            }
        } while (!$this$addConditionally.compareAndSet(cur, cur + delta));
        return true;
    }
}
