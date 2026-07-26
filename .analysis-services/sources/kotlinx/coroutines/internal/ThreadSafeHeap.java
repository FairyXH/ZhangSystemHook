package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: ThreadSafeHeap.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0014\b\u0017\u0018\u0000*\u0012\b\u0000\u0010\u0001*\u00020\u0002*\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00060\u0004j\u0002`\u0005B\u0005¢\u0006\u0002\u0010\u0006J\u0015\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00028\u0000H\u0001¢\u0006\u0002\u0010\u0019J\u0013\u0010\u001a\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00028\u0000¢\u0006\u0002\u0010\u0019J/\u0010\u001b\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00028\u00002\u0014\u0010\u001c\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00018\u0000\u0012\u0004\u0012\u00020\r0\u001dH\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u001eJ\u0006\u0010\u001f\u001a\u00020\u0017J0\u0010 \u001a\u0004\u0018\u00018\u00002!\u0010!\u001a\u001d\u0012\u0013\u0012\u00118\u0000¢\u0006\f\b\"\u0012\b\b#\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\r0\u001d¢\u0006\u0002\u0010$J\u000f\u0010%\u001a\u0004\u0018\u00018\u0000H\u0001¢\u0006\u0002\u0010&J\r\u0010'\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010&J\u0015\u0010(\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\nH\u0002¢\u0006\u0002\u0010)J\u0013\u0010*\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00028\u0000¢\u0006\u0002\u0010+J\u0015\u0010,\u001a\u00028\u00002\u0006\u0010-\u001a\u00020\u0010H\u0001¢\u0006\u0002\u0010.J'\u0010/\u001a\u0004\u0018\u00018\u00002\u0012\u0010!\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\r0\u001dH\u0086\bø\u0001\u0000¢\u0006\u0002\u0010$J\r\u00100\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010&J\u0011\u00101\u001a\u00020\u00172\u0006\u00102\u001a\u00020\u0010H\u0082\u0010J\u0011\u00103\u001a\u00020\u00172\u0006\u00102\u001a\u00020\u0010H\u0082\u0010J\u0018\u00104\u001a\u00020\u00172\u0006\u00102\u001a\u00020\u00102\u0006\u00105\u001a\u00020\u0010H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\f\u0012\u0006\u0012\u0004\u0018\u00018\u0000\u0018\u00010\nX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u000bR\u0011\u0010\f\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\f\u0010\u000eR$\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u00108F@BX\u0086\u000e¢\u0006\f\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u00066"}, d2 = {"Lkotlinx/coroutines/internal/ThreadSafeHeap;", "T", "Lkotlinx/coroutines/internal/ThreadSafeHeapNode;", "", "", "Lkotlinx/coroutines/internal/SynchronizedObject;", "()V", "_size", "Lkotlinx/atomicfu/AtomicInt;", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "", "[Lkotlinx/coroutines/internal/ThreadSafeHeapNode;", "isEmpty", "", "()Z", "value", "", "size", "getSize", "()I", "setSize", "(I)V", "addImpl", "", "node", "(Lkotlinx/coroutines/internal/ThreadSafeHeapNode;)V", "addLast", "addLastIf", "cond", "Lkotlin/Function1;", "(Lkotlinx/coroutines/internal/ThreadSafeHeapNode;Lkotlin/jvm/functions/Function1;)Z", "clear", "find", "predicate", "Lkotlin/ParameterName;", "name", "(Lkotlin/jvm/functions/Function1;)Lkotlinx/coroutines/internal/ThreadSafeHeapNode;", "firstImpl", "()Lkotlinx/coroutines/internal/ThreadSafeHeapNode;", "peek", "realloc", "()[Lkotlinx/coroutines/internal/ThreadSafeHeapNode;", "remove", "(Lkotlinx/coroutines/internal/ThreadSafeHeapNode;)Z", "removeAtImpl", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(I)Lkotlinx/coroutines/internal/ThreadSafeHeapNode;", "removeFirstIf", "removeFirstOrNull", "siftDownFrom", "i", "siftUpFrom", "swap", "j", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class ThreadSafeHeap<T extends kotlinx.coroutines.internal.ThreadSafeHeapNode & java.lang.Comparable<? super T>> {
    private final kotlinx.atomicfu.AtomicInt _size = kotlinx.atomicfu.AtomicFU.atomic(0);
    private T[] a;

    public final int getSize() {
        return this._size.getValue();
    }

    private final void setSize(int value) {
        this._size.setValue(value);
    }

    public final boolean isEmpty() {
        return getSize() == 0;
    }

    public final void clear() {
        synchronized (this) {
            T[] tArr = this.a;
            if (tArr != null) {
                kotlin.collections.ArraysKt.fill$default(tArr, (java.lang.Object) null, 0, 0, 6, (java.lang.Object) null);
            }
            this._size.setValue(0);
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
        }
    }

    public final T find(kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> predicate) {
        T t;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        synchronized (this) {
            int i = 0;
            int size = getSize();
            while (true) {
                t = null;
                if (i >= size) {
                    break;
                }
                T[] tArr = this.a;
                if (tArr != null) {
                    t = (java.lang.Object) tArr[i];
                }
                kotlin.jvm.internal.Intrinsics.checkNotNull(t);
                if (predicate.invoke(t).booleanValue()) {
                    break;
                }
                i++;
            }
        }
        return t;
    }

    public final T peek() {
        T t;
        synchronized (this) {
            t = (T) firstImpl();
        }
        return t;
    }

    public final T removeFirstOrNull() {
        T t;
        synchronized (this) {
            if (getSize() > 0) {
                t = (T) removeAtImpl(0);
            } else {
                t = null;
            }
        }
        return t;
    }

    public final T removeFirstIf(kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        synchronized (this) {
            try {
                kotlinx.coroutines.internal.ThreadSafeHeapNode threadSafeHeapNodeFirstImpl = firstImpl();
                T t = null;
                if (threadSafeHeapNodeFirstImpl == null) {
                    kotlin.jvm.internal.InlineMarker.finallyStart(2);
                    kotlin.jvm.internal.InlineMarker.finallyEnd(2);
                    return null;
                }
                if (predicate.invoke(threadSafeHeapNodeFirstImpl).booleanValue()) {
                    t = (T) removeAtImpl(0);
                }
                kotlin.jvm.internal.InlineMarker.finallyStart(1);
                kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                return t;
            } catch (java.lang.Throwable th) {
                kotlin.jvm.internal.InlineMarker.finallyStart(1);
                kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                throw th;
            }
        }
    }

    public final void addLast(T node) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(node, "node");
        synchronized (this) {
            addImpl(node);
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
        }
    }

    public final boolean addLastIf(T node, kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> cond) {
        boolean z;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(node, "node");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cond, "cond");
        synchronized (this) {
            try {
                if (cond.invoke(firstImpl()).booleanValue()) {
                    addImpl(node);
                    z = true;
                } else {
                    z = false;
                }
                kotlin.jvm.internal.InlineMarker.finallyStart(1);
            } catch (java.lang.Throwable th) {
                kotlin.jvm.internal.InlineMarker.finallyStart(1);
                kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                throw th;
            }
        }
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
        return z;
    }

    public final boolean remove(T node) {
        boolean z;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(node, "node");
        synchronized (this) {
            if (node.getHeap() != null) {
                int index = node.getIndex();
                if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                    if (!(index >= 0)) {
                        throw new java.lang.AssertionError();
                    }
                }
                removeAtImpl(index);
                z = true;
            }
        }
        return z;
    }

    public final T firstImpl() {
        T[] tArr = this.a;
        if (tArr != null) {
            return tArr[0];
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0055  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final T removeAtImpl(int r8) {
        /*
            r7 = this;
            boolean r0 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L1b
            r0 = 0
            int r3 = r7.getSize()
            if (r3 <= 0) goto L11
            r0 = r2
            goto L12
        L11:
            r0 = r1
        L12:
            if (r0 == 0) goto L15
            goto L1b
        L15:
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r0.<init>()
            throw r0
        L1b:
            T extends kotlinx.coroutines.internal.ThreadSafeHeapNode & java.lang.Comparable<? super T>[] r0 = r7.a
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            int r3 = r7.getSize()
            r4 = -1
            int r3 = r3 + r4
            r7.setSize(r3)
            int r3 = r7.getSize()
            if (r8 >= r3) goto L58
            int r3 = r7.getSize()
            r7.swap(r8, r3)
            int r3 = r8 + (-1)
            int r3 = r3 / 2
            if (r8 <= 0) goto L55
            r5 = r0[r8]
            kotlin.jvm.internal.Intrinsics.checkNotNull(r5)
            java.lang.Comparable r5 = (java.lang.Comparable) r5
            r6 = r0[r3]
            kotlin.jvm.internal.Intrinsics.checkNotNull(r6)
            int r5 = r5.compareTo(r6)
            if (r5 >= 0) goto L55
            r7.swap(r8, r3)
            r7.siftUpFrom(r3)
            goto L58
        L55:
            r7.siftDownFrom(r8)
        L58:
            int r3 = r7.getSize()
            r3 = r0[r3]
            kotlin.jvm.internal.Intrinsics.checkNotNull(r3)
            boolean r5 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()
            if (r5 == 0) goto L78
            r5 = 0
            kotlinx.coroutines.internal.ThreadSafeHeap r6 = r3.getHeap()
            if (r6 != r7) goto L6f
            r1 = r2
        L6f:
            if (r1 == 0) goto L72
            goto L78
        L72:
            java.lang.AssertionError r1 = new java.lang.AssertionError
            r1.<init>()
            throw r1
        L78:
            r1 = 0
            r3.setHeap(r1)
            r3.setIndex(r4)
            int r2 = r7.getSize()
            r0[r2] = r1
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.ThreadSafeHeap.removeAtImpl(int):kotlinx.coroutines.internal.ThreadSafeHeapNode");
    }

    public final void addImpl(T node) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(node, "node");
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(node.getHeap() == null)) {
                throw new java.lang.AssertionError();
            }
        }
        node.setHeap(this);
        kotlinx.coroutines.internal.ThreadSafeHeapNode[] a = realloc();
        int i = getSize();
        setSize(i + 1);
        a[i] = node;
        node.setIndex(i);
        siftUpFrom(i);
    }

    private final void siftUpFrom(int i) {
        while (i > 0) {
            T[] tArr = this.a;
            kotlin.jvm.internal.Intrinsics.checkNotNull(tArr);
            int j = (i - 1) / 2;
            T t = tArr[j];
            kotlin.jvm.internal.Intrinsics.checkNotNull(t);
            T t2 = tArr[i];
            kotlin.jvm.internal.Intrinsics.checkNotNull(t2);
            if (((java.lang.Comparable) t).compareTo(t2) <= 0) {
                return;
            }
            swap(i, j);
            i = j;
        }
    }

    private final void siftDownFrom(int i) {
        while (true) {
            int j = (i * 2) + 1;
            if (j >= getSize()) {
                return;
            }
            T[] tArr = this.a;
            kotlin.jvm.internal.Intrinsics.checkNotNull(tArr);
            if (j + 1 < getSize()) {
                T t = tArr[j + 1];
                kotlin.jvm.internal.Intrinsics.checkNotNull(t);
                T t2 = tArr[j];
                kotlin.jvm.internal.Intrinsics.checkNotNull(t2);
                if (((java.lang.Comparable) t).compareTo(t2) < 0) {
                    j++;
                }
            }
            T t3 = tArr[i];
            kotlin.jvm.internal.Intrinsics.checkNotNull(t3);
            T t4 = tArr[j];
            kotlin.jvm.internal.Intrinsics.checkNotNull(t4);
            if (((java.lang.Comparable) t3).compareTo(t4) <= 0) {
                return;
            }
            swap(i, j);
            i = j;
        }
    }

    private final T[] realloc() {
        T[] tArr = this.a;
        if (tArr == null) {
            T[] tArr2 = (T[]) new kotlinx.coroutines.internal.ThreadSafeHeapNode[4];
            this.a = tArr2;
            return tArr2;
        }
        if (getSize() >= tArr.length) {
            java.lang.Object[] objArrCopyOf = java.util.Arrays.copyOf(tArr, getSize() * 2);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(objArrCopyOf, "copyOf(...)");
            this.a = (T[]) ((kotlinx.coroutines.internal.ThreadSafeHeapNode[]) objArrCopyOf);
            return (T[]) ((kotlinx.coroutines.internal.ThreadSafeHeapNode[]) objArrCopyOf);
        }
        return tArr;
    }

    private final void swap(int i, int j) {
        kotlinx.coroutines.internal.ThreadSafeHeapNode[] a = this.a;
        kotlin.jvm.internal.Intrinsics.checkNotNull(a);
        kotlinx.coroutines.internal.ThreadSafeHeapNode ni = a[j];
        kotlin.jvm.internal.Intrinsics.checkNotNull(ni);
        kotlinx.coroutines.internal.ThreadSafeHeapNode nj = a[i];
        kotlin.jvm.internal.Intrinsics.checkNotNull(nj);
        a[i] = ni;
        a[j] = nj;
        ni.setIndex(i);
        nj.setIndex(j);
    }
}
