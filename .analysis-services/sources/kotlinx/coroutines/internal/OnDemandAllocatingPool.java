package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: OnDemandAllocatingPool.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B!\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00028\u00000\u0006¢\u0006\u0002\u0010\u0007J\u0006\u0010\f\u001a\u00020\rJ\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u000fJ\r\u0010\u0010\u001a\u00020\u0011H\u0000¢\u0006\u0002\b\u0012J\b\u0010\u0013\u001a\u00020\u0011H\u0016J\t\u0010\u0014\u001a\u00020\u0004H\u0082\bJ\r\u0010\u0015\u001a\u00020\r*\u00020\u0004H\u0082\bR\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00028\u00000\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, d2 = {"Lkotlinx/coroutines/internal/OnDemandAllocatingPool;", "T", "", "maxCapacity", "", "create", "Lkotlin/Function1;", "(ILkotlin/jvm/functions/Function1;)V", "controlState", "Lkotlinx/atomicfu/AtomicInt;", "elements", "Lkotlinx/atomicfu/AtomicArray;", "allocate", "", "close", "", "stateRepresentation", "", "stateRepresentation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "toString", "tryForbidNewElements", "isClosed", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class OnDemandAllocatingPool<T> {
    private final kotlinx.atomicfu.AtomicInt controlState;
    private final kotlin.jvm.functions.Function1<java.lang.Integer, T> create;
    private final kotlinx.atomicfu.AtomicArray<T> elements;
    private final int maxCapacity;

    /* JADX WARN: Multi-variable type inference failed */
    public OnDemandAllocatingPool(int maxCapacity, kotlin.jvm.functions.Function1<? super java.lang.Integer, ? extends T> create) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(create, "create");
        this.maxCapacity = maxCapacity;
        this.create = create;
        this.controlState = kotlinx.atomicfu.AtomicFU.atomic(0);
        this.elements = kotlinx.atomicfu.AtomicFU_commonKt.atomicArrayOfNulls(this.maxCapacity);
    }

    private final int tryForbidNewElements() {
        int it;
        kotlinx.atomicfu.AtomicInt $this$loop$iv = this.controlState;
        do {
            it = $this$loop$iv.getValue();
            if ((it & Integer.MIN_VALUE) != 0) {
                return 0;
            }
        } while (!this.controlState.compareAndSet(it, it | Integer.MIN_VALUE));
        return it;
    }

    private final boolean isClosed(int $this$isClosed) {
        return (Integer.MIN_VALUE & $this$isClosed) != 0;
    }

    public final boolean allocate() {
        int value;
        kotlinx.atomicfu.AtomicInt atomicInt = this.controlState;
        do {
            value = atomicInt.getValue();
            if (((Integer.MIN_VALUE & value) != 0 ? (char) 1 : (char) 0) != 0) {
                return false;
            }
            if (value >= this.maxCapacity) {
                return true;
            }
        } while (!this.controlState.compareAndSet(value, value + 1));
        this.elements.get(value).setValue(this.create.invoke(java.lang.Integer.valueOf(value)));
        return true;
    }

    public final java.util.List<T> close() {
        int it$iv;
        T andSet;
        kotlinx.atomicfu.AtomicInt $this$loop$iv$iv = this.controlState;
        while (true) {
            it$iv = $this$loop$iv$iv.getValue();
            if ((it$iv & Integer.MIN_VALUE) != 0) {
                it$iv = 0;
                break;
            }
            if (this.controlState.compareAndSet(it$iv, it$iv | Integer.MIN_VALUE)) {
                break;
            }
        }
        int elementsExisting = it$iv;
        java.lang.Iterable $this$map$iv = kotlin.ranges.RangesKt.until(0, elementsExisting);
        java.util.Collection destination$iv$iv = new java.util.ArrayList(kotlin.collections.CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        java.util.Iterator<java.lang.Integer> it = $this$map$iv.iterator();
        while (it.hasNext()) {
            int item$iv$iv = ((kotlin.collections.IntIterator) it).nextInt();
            do {
                andSet = this.elements.get(item$iv$iv).getAndSet(null);
            } while (andSet == null);
            destination$iv$iv.add(andSet);
        }
        return (java.util.List) destination$iv$iv;
    }

    public final java.lang.String stateRepresentation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        int ctl = this.controlState.getValue();
        java.lang.Iterable $this$map$iv = kotlin.ranges.RangesKt.until(0, Integer.MAX_VALUE & ctl);
        java.util.Collection destination$iv$iv = new java.util.ArrayList(kotlin.collections.CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        java.util.Iterator<java.lang.Integer> it = $this$map$iv.iterator();
        while (it.hasNext()) {
            int item$iv$iv = ((kotlin.collections.IntIterator) it).nextInt();
            destination$iv$iv.add(this.elements.get(item$iv$iv).getValue());
        }
        java.lang.String elementsStr = ((java.util.List) destination$iv$iv).toString();
        java.lang.String closedStr = (Integer.MIN_VALUE & ctl) != 0 ? "[closed]" : "";
        return elementsStr + closedStr;
    }

    public java.lang.String toString() {
        return "OnDemandAllocatingPool(" + stateRepresentation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() + ")";
    }
}
