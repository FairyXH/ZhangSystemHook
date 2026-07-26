package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: LockFreeTaskQueue.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b\u0000\u0018\u0000 ,*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0002:\u0002,-B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0013\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00028\u0000¢\u0006\u0002\u0010\u0017J \u0010\u0018\u001a\u0012\u0012\u0004\u0012\u00028\u00000\u0000j\b\u0012\u0004\u0012\u00028\u0000`\n2\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J \u0010\u001b\u001a\u0012\u0012\u0004\u0012\u00028\u00000\u0000j\b\u0012\u0004\u0012\u00028\u0000`\n2\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J\u0006\u0010\u001c\u001a\u00020\u0006J1\u0010\u001d\u001a\u0016\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\n2\u0006\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u001fJ\u0006\u0010 \u001a\u00020\u0006J&\u0010!\u001a\b\u0012\u0004\u0012\u0002H#0\"\"\u0004\b\u0001\u0010#2\u0012\u0010$\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u0002H#0%J\b\u0010&\u001a\u00020\u001aH\u0002J\f\u0010'\u001a\b\u0012\u0004\u0012\u00028\u00000\u0000J\b\u0010(\u001a\u0004\u0018\u00010\u0002J,\u0010)\u001a\u0016\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\n2\u0006\u0010*\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0004H\u0002R(\u0010\b\u001a\u001c\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\n0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u000f\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0012\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014¨\u0006."}, d2 = {"Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;", "E", "", "capacity", "", "singleConsumer", "", "(IZ)V", "_next", "Lkotlinx/atomicfu/AtomicRef;", "Lkotlinx/coroutines/internal/Core;", "_state", "Lkotlinx/atomicfu/AtomicLong;", "array", "Lkotlinx/atomicfu/AtomicArray;", "isEmpty", "()Z", "mask", "size", "getSize", "()I", "addLast", "element", "(Ljava/lang/Object;)I", "allocateNextCopy", "state", "", "allocateOrGetNextCopy", "close", "fillPlaceholder", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(ILjava/lang/Object;)Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;", "isClosed", "map", "", "R", "transform", "Lkotlin/Function1;", "markFrozen", "next", "removeFirstOrNull", "removeSlowPath", "oldHead", "newHead", "Companion", "Placeholder", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class LockFreeTaskQueueCore<E> {
    public static final int ADD_CLOSED = 2;
    public static final int ADD_FROZEN = 1;
    public static final int ADD_SUCCESS = 0;
    public static final int CAPACITY_BITS = 30;
    public static final long CLOSED_MASK = 2305843009213693952L;
    public static final int CLOSED_SHIFT = 61;
    public static final long FROZEN_MASK = 1152921504606846976L;
    public static final int FROZEN_SHIFT = 60;
    public static final long HEAD_MASK = 1073741823;
    public static final int HEAD_SHIFT = 0;
    public static final int INITIAL_CAPACITY = 8;
    public static final int MAX_CAPACITY_MASK = 1073741823;
    public static final int MIN_ADD_SPIN_CAPACITY = 1024;
    public static final long TAIL_MASK = 1152921503533105152L;
    public static final int TAIL_SHIFT = 30;
    private final kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeTaskQueueCore<E>> _next = kotlinx.atomicfu.AtomicFU.atomic((java.lang.Object) null);
    private final kotlinx.atomicfu.AtomicLong _state = kotlinx.atomicfu.AtomicFU.atomic(0L);
    private final kotlinx.atomicfu.AtomicArray<java.lang.Object> array;
    private final int capacity;
    private final int mask;
    private final boolean singleConsumer;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final kotlinx.coroutines.internal.LockFreeTaskQueueCore.Companion INSTANCE = new kotlinx.coroutines.internal.LockFreeTaskQueueCore.Companion(null);
    public static final kotlinx.coroutines.internal.Symbol REMOVE_FROZEN = new kotlinx.coroutines.internal.Symbol("REMOVE_FROZEN");

    public LockFreeTaskQueueCore(int capacity, boolean singleConsumer) {
        this.capacity = capacity;
        this.singleConsumer = singleConsumer;
        this.mask = this.capacity - 1;
        this.array = kotlinx.atomicfu.AtomicFU_commonKt.atomicArrayOfNulls(this.capacity);
        if (!(this.mask <= 1073741823)) {
            throw new java.lang.IllegalStateException("Check failed.".toString());
        }
        if ((this.capacity & this.mask) == 0) {
        } else {
            throw new java.lang.IllegalStateException("Check failed.".toString());
        }
    }

    public final boolean isEmpty() {
        kotlinx.coroutines.internal.LockFreeTaskQueueCore.Companion companion = INSTANCE;
        long $this$withState$iv = this._state.getValue();
        int head$iv = (int) ((HEAD_MASK & $this$withState$iv) >> 0);
        int tail$iv = (int) ((TAIL_MASK & $this$withState$iv) >> 30);
        return head$iv == tail$iv;
    }

    public final int getSize() {
        kotlinx.coroutines.internal.LockFreeTaskQueueCore.Companion companion = INSTANCE;
        long $this$withState$iv = this._state.getValue();
        int head$iv = (int) ((HEAD_MASK & $this$withState$iv) >> 0);
        int tail$iv = (int) ((TAIL_MASK & $this$withState$iv) >> 30);
        int head = (tail$iv - head$iv) & MAX_CAPACITY_MASK;
        return head;
    }

    public final boolean close() {
        long cur$iv;
        long upd$iv;
        kotlinx.atomicfu.AtomicLong $this$update$iv = this._state;
        do {
            cur$iv = $this$update$iv.getValue();
            if ((cur$iv & CLOSED_MASK) != 0) {
                return true;
            }
            if ((FROZEN_MASK & cur$iv) != 0) {
                return false;
            }
            upd$iv = cur$iv | CLOSED_MASK;
        } while (!$this$update$iv.compareAndSet(cur$iv, upd$iv));
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0074, code lost:
    
        return 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int addLast(E r27) {
        /*
            r26 = this;
            r0 = r26
            r1 = r27
            java.lang.String r2 = "element"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r1, r2)
            kotlinx.atomicfu.AtomicLong r2 = r0._state
            r3 = 0
        Lc:
            long r4 = r2.getValue()
            r6 = 0
            r7 = 3458764513820540928(0x3000000000000000, double:1.727233711018889E-77)
            long r7 = r7 & r4
            r9 = 0
            int r7 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r7 == 0) goto L22
            kotlinx.coroutines.internal.LockFreeTaskQueueCore$Companion r7 = kotlinx.coroutines.internal.LockFreeTaskQueueCore.INSTANCE
            int r7 = r7.addFailReason(r4)
            return r7
        L22:
            kotlinx.coroutines.internal.LockFreeTaskQueueCore$Companion r7 = kotlinx.coroutines.internal.LockFreeTaskQueueCore.INSTANCE
            r11 = r4
            r8 = 0
            r13 = 1073741823(0x3fffffff, double:5.304989472E-315)
            long r13 = r13 & r11
            r15 = 0
            long r13 = r13 >> r15
            int r13 = (int) r13
            r16 = 1152921503533105152(0xfffffffc0000000, double:1.2882296003504729E-231)
            long r16 = r11 & r16
            r14 = 30
            long r9 = r16 >> r14
            int r9 = (int) r9
            r10 = r13
            r14 = r9
            r16 = 0
            int r15 = r0.mask
            int r20 = r14 + 2
            r21 = r2
            r2 = r20 & r15
            r20 = r3
            r3 = r10 & r15
            r22 = 1
            if (r2 != r3) goto L4e
            return r22
        L4e:
            boolean r2 = r0.singleConsumer
            if (r2 != 0) goto L75
            kotlinx.atomicfu.AtomicArray<java.lang.Object> r2 = r0.array
            r3 = r14 & r15
            kotlinx.atomicfu.AtomicRef r2 = r2.get(r3)
            java.lang.Object r2 = r2.getValue()
            if (r2 == 0) goto L75
            int r2 = r0.capacity
            r3 = 1024(0x400, float:1.435E-42)
            if (r2 < r3) goto L74
            int r2 = r14 - r10
            r3 = 1073741823(0x3fffffff, float:1.9999999)
            r2 = r2 & r3
            int r3 = r0.capacity
            int r3 = r3 >> 1
            if (r2 <= r3) goto L73
            goto L74
        L73:
            goto Lbd
        L74:
            return r22
        L75:
            int r2 = r14 + 1
            r3 = 1073741823(0x3fffffff, float:1.9999999)
            r2 = r2 & r3
            kotlinx.atomicfu.AtomicLong r3 = r0._state
            r22 = r6
            kotlinx.coroutines.internal.LockFreeTaskQueueCore$Companion r6 = kotlinx.coroutines.internal.LockFreeTaskQueueCore.INSTANCE
            r23 = r7
            long r6 = r6.updateTail(r4, r2)
            boolean r3 = r3.compareAndSet(r4, r6)
            if (r3 == 0) goto Lba
            kotlinx.atomicfu.AtomicArray<java.lang.Object> r3 = r0.array
            r6 = r14 & r15
            kotlinx.atomicfu.AtomicRef r3 = r3.get(r6)
            r3.setValue(r1)
            r3 = r26
        L9a:
            kotlinx.atomicfu.AtomicLong r6 = r3._state
            long r6 = r6.getValue()
            r24 = 1152921504606846976(0x1000000000000000, double:1.2882297539194267E-231)
            long r6 = r6 & r24
            r18 = 0
            int r6 = (r6 > r18 ? 1 : (r6 == r18 ? 0 : -1))
            if (r6 == 0) goto Lb8
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r6 = r3.next()
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r6 = r6.fillPlaceholder(r14, r1)
            if (r6 != 0) goto Lb6
            goto Lb8
        Lb6:
            r3 = r6
            goto L9a
        Lb8:
            r6 = 0
            return r6
        Lba:
        Lbd:
            r3 = r20
            r2 = r21
            goto Lc
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.LockFreeTaskQueueCore.addLast(java.lang.Object):int");
    }

    private final kotlinx.coroutines.internal.LockFreeTaskQueueCore<E> fillPlaceholder(int index, E element) {
        java.lang.Object old = this.array.get(this.mask & index).getValue();
        if ((old instanceof kotlinx.coroutines.internal.LockFreeTaskQueueCore.Placeholder) && ((kotlinx.coroutines.internal.LockFreeTaskQueueCore.Placeholder) old).index == index) {
            this.array.get(this.mask & index).setValue(element);
            return this;
        }
        return null;
    }

    public final java.lang.Object removeFirstOrNull() {
        kotlinx.atomicfu.AtomicLong $this$loop$iv = this._state;
        int $i$f$loop = 0;
        while (true) {
            long state = $this$loop$iv.getValue();
            if ((FROZEN_MASK & state) != 0) {
                return REMOVE_FROZEN;
            }
            kotlinx.coroutines.internal.LockFreeTaskQueueCore.Companion companion = INSTANCE;
            int head$iv = (int) ((HEAD_MASK & state) >> 0);
            int tail$iv = (int) ((TAIL_MASK & state) >> 30);
            kotlinx.atomicfu.AtomicLong $this$loop$iv2 = $this$loop$iv;
            int $i$f$loop2 = $i$f$loop;
            if ((this.mask & tail$iv) == (this.mask & head$iv)) {
                return null;
            }
            java.lang.Object element = this.array.get(this.mask & head$iv).getValue();
            if (element == null) {
                if (this.singleConsumer) {
                    return null;
                }
            } else {
                if (element instanceof kotlinx.coroutines.internal.LockFreeTaskQueueCore.Placeholder) {
                    return null;
                }
                int newHead = (head$iv + 1) & MAX_CAPACITY_MASK;
                if (!this._state.compareAndSet(state, INSTANCE.updateHead(state, newHead))) {
                    if (this.singleConsumer) {
                        kotlinx.coroutines.internal.LockFreeTaskQueueCore<E> lockFreeTaskQueueCore = this;
                        while (true) {
                            kotlinx.coroutines.internal.LockFreeTaskQueueCore<E> lockFreeTaskQueueCoreRemoveSlowPath = lockFreeTaskQueueCore.removeSlowPath(head$iv, newHead);
                            if (lockFreeTaskQueueCoreRemoveSlowPath == null) {
                                return element;
                            }
                            lockFreeTaskQueueCore = lockFreeTaskQueueCoreRemoveSlowPath;
                        }
                    }
                } else {
                    this.array.get(this.mask & head$iv).setValue(null);
                    return element;
                }
            }
            $this$loop$iv = $this$loop$iv2;
            $i$f$loop = $i$f$loop2;
        }
    }

    private final kotlinx.coroutines.internal.LockFreeTaskQueueCore<E> removeSlowPath(int oldHead, int newHead) {
        kotlinx.atomicfu.AtomicLong $this$loop$iv = this._state;
        int $i$f$loop = 0;
        while (true) {
            long state = $this$loop$iv.getValue();
            kotlinx.coroutines.internal.LockFreeTaskQueueCore.Companion companion = INSTANCE;
            int head$iv = (int) ((HEAD_MASK & state) >> 0);
            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                if (!(head$iv == oldHead)) {
                    throw new java.lang.AssertionError();
                }
            }
            if ((FROZEN_MASK & state) != 0) {
                return next();
            }
            kotlinx.atomicfu.AtomicLong $this$loop$iv2 = $this$loop$iv;
            int $i$f$loop2 = $i$f$loop;
            if (!this._state.compareAndSet(state, INSTANCE.updateHead(state, newHead))) {
                $this$loop$iv = $this$loop$iv2;
                $i$f$loop = $i$f$loop2;
            } else {
                this.array.get(this.mask & head$iv).setValue(null);
                return null;
            }
        }
    }

    public final kotlinx.coroutines.internal.LockFreeTaskQueueCore<E> next() {
        return allocateOrGetNextCopy(markFrozen());
    }

    private final long markFrozen() {
        long cur$iv;
        long upd$iv;
        kotlinx.atomicfu.AtomicLong $this$updateAndGet$iv = this._state;
        do {
            cur$iv = $this$updateAndGet$iv.getValue();
            if ((cur$iv & FROZEN_MASK) == 0) {
                upd$iv = cur$iv | FROZEN_MASK;
            } else {
                return cur$iv;
            }
        } while (!$this$updateAndGet$iv.compareAndSet(cur$iv, upd$iv));
        return upd$iv;
    }

    private final kotlinx.coroutines.internal.LockFreeTaskQueueCore<E> allocateOrGetNextCopy(long state) {
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeTaskQueueCore<E>> atomicRef = this._next;
        while (true) {
            kotlinx.coroutines.internal.LockFreeTaskQueueCore<E> value = atomicRef.getValue();
            if (value != null) {
                return value;
            }
            this._next.compareAndSet(null, allocateNextCopy(state));
        }
    }

    private final kotlinx.coroutines.internal.LockFreeTaskQueueCore<E> allocateNextCopy(long state) {
        kotlinx.coroutines.internal.LockFreeTaskQueueCore<E> lockFreeTaskQueueCore = new kotlinx.coroutines.internal.LockFreeTaskQueueCore<>(this.capacity * 2, this.singleConsumer);
        kotlinx.coroutines.internal.LockFreeTaskQueueCore.Companion companion = INSTANCE;
        int head$iv = (int) ((HEAD_MASK & state) >> 0);
        int tail$iv = (int) ((TAIL_MASK & state) >> 30);
        for (int index = head$iv; (this.mask & index) != (this.mask & tail$iv); index++) {
            java.lang.Object value = this.array.get(this.mask & index).getValue();
            if (value == null) {
                value = new kotlinx.coroutines.internal.LockFreeTaskQueueCore.Placeholder(index);
            }
            lockFreeTaskQueueCore.array.get(lockFreeTaskQueueCore.mask & index).setValue(value);
        }
        lockFreeTaskQueueCore._state.setValue(INSTANCE.wo(state, FROZEN_MASK));
        return lockFreeTaskQueueCore;
    }

    public final <R> java.util.List<R> map(kotlin.jvm.functions.Function1<? super E, ? extends R> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.util.ArrayList res = new java.util.ArrayList(this.capacity);
        kotlinx.coroutines.internal.LockFreeTaskQueueCore.Companion companion = INSTANCE;
        long $this$withState$iv = this._state.getValue();
        int head$iv = (int) ((HEAD_MASK & $this$withState$iv) >> 0);
        int tail$iv = (int) ((TAIL_MASK & $this$withState$iv) >> 30);
        for (int index = head$iv; (this.mask & index) != (this.mask & tail$iv); index++) {
            java.lang.Object element = this.array.get(this.mask & index).getValue();
            if (element != null && !(element instanceof kotlinx.coroutines.internal.LockFreeTaskQueueCore.Placeholder)) {
                res.add(transform.invoke(element));
            }
        }
        return res;
    }

    public final boolean isClosed() {
        return (this._state.getValue() & CLOSED_MASK) != 0;
    }

    /* JADX INFO: compiled from: LockFreeTaskQueue.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeTaskQueueCore$Placeholder;", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "(I)V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Placeholder {
        public final int index;

        public Placeholder(int index) {
            this.index = index;
        }
    }

    /* JADX INFO: compiled from: LockFreeTaskQueue.kt */
    @kotlin.Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\n\u0010\u0016\u001a\u00020\u0004*\u00020\tJ\u0012\u0010\u0017\u001a\u00020\t*\u00020\t2\u0006\u0010\u0018\u001a\u00020\u0004J\u0012\u0010\u0019\u001a\u00020\t*\u00020\t2\u0006\u0010\u001a\u001a\u00020\u0004JS\u0010\u001b\u001a\u0002H\u001c\"\u0004\b\u0001\u0010\u001c*\u00020\t26\u0010\u001d\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u001f\u0012\b\b \u0012\u0004\b\b(!\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u001f\u0012\b\b \u0012\u0004\b\b(\"\u0012\u0004\u0012\u0002H\u001c0\u001eH\u0086\bø\u0001\u0000¢\u0006\u0002\u0010#J\u0015\u0010$\u001a\u00020\t*\u00020\t2\u0006\u0010%\u001a\u00020\tH\u0086\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\tX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u00020\u00138\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\tX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006&"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeTaskQueueCore$Companion;", "", "()V", "ADD_CLOSED", "", "ADD_FROZEN", "ADD_SUCCESS", "CAPACITY_BITS", "CLOSED_MASK", "", "CLOSED_SHIFT", "FROZEN_MASK", "FROZEN_SHIFT", "HEAD_MASK", "HEAD_SHIFT", "INITIAL_CAPACITY", "MAX_CAPACITY_MASK", "MIN_ADD_SPIN_CAPACITY", "REMOVE_FROZEN", "Lkotlinx/coroutines/internal/Symbol;", "TAIL_MASK", "TAIL_SHIFT", "addFailReason", "updateHead", "newHead", "updateTail", "newTail", "withState", "T", "block", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "head", "tail", "(JLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "wo", "other", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final long wo(long $this$wo, long other) {
            return (~other) & $this$wo;
        }

        public final long updateHead(long $this$updateHead, int newHead) {
            return wo($this$updateHead, kotlinx.coroutines.internal.LockFreeTaskQueueCore.HEAD_MASK) | (((long) newHead) << 0);
        }

        public final long updateTail(long $this$updateTail, int newTail) {
            return wo($this$updateTail, kotlinx.coroutines.internal.LockFreeTaskQueueCore.TAIL_MASK) | (((long) newTail) << 30);
        }

        public final <T> T withState(long $this$withState, kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.Integer, ? extends T> block) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
            int head = (int) ((kotlinx.coroutines.internal.LockFreeTaskQueueCore.HEAD_MASK & $this$withState) >> 0);
            int tail = (int) ((kotlinx.coroutines.internal.LockFreeTaskQueueCore.TAIL_MASK & $this$withState) >> 30);
            return block.invoke(java.lang.Integer.valueOf(head), java.lang.Integer.valueOf(tail));
        }

        public final int addFailReason(long $this$addFailReason) {
            return (kotlinx.coroutines.internal.LockFreeTaskQueueCore.CLOSED_MASK & $this$addFailReason) != 0 ? 2 : 1;
        }
    }
}
