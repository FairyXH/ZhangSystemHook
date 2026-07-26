package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: ResizableAtomicArray.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0006\u0010\b\u001a\u00020\u0004J\u0018\u0010\t\u001a\u0004\u0018\u00018\u00002\u0006\u0010\n\u001a\u00020\u0004H\u0086\u0002¢\u0006\u0002\u0010\u000bJ\u001d\u0010\f\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u00042\b\u0010\u000e\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010\u000fR\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lkotlinx/coroutines/internal/ResizableAtomicArray;", "T", "", "initialLength", "", "(I)V", "array", "Ljava/util/concurrent/atomic/AtomicReferenceArray;", "currentLength", "get", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(I)Ljava/lang/Object;", "setSynchronized", "", "value", "(ILjava/lang/Object;)V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ResizableAtomicArray<T> {
    private volatile java.util.concurrent.atomic.AtomicReferenceArray<T> array;

    public ResizableAtomicArray(int initialLength) {
        this.array = new java.util.concurrent.atomic.AtomicReferenceArray<>(initialLength);
    }

    public final int currentLength() {
        return this.array.length();
    }

    public final T get(int index) {
        java.util.concurrent.atomic.AtomicReferenceArray<T> atomicReferenceArray = this.array;
        if (index < atomicReferenceArray.length()) {
            return atomicReferenceArray.get(index);
        }
        return null;
    }

    public final void setSynchronized(int index, T value) {
        java.util.concurrent.atomic.AtomicReferenceArray<T> atomicReferenceArray = this.array;
        int curLen = atomicReferenceArray.length();
        if (index < curLen) {
            atomicReferenceArray.set(index, value);
            return;
        }
        java.util.concurrent.atomic.AtomicReferenceArray<T> atomicReferenceArray2 = new java.util.concurrent.atomic.AtomicReferenceArray<>(kotlin.ranges.RangesKt.coerceAtLeast(index + 1, curLen * 2));
        for (int i = 0; i < curLen; i++) {
            atomicReferenceArray2.set(i, atomicReferenceArray.get(i));
        }
        atomicReferenceArray2.set(index, value);
        this.array = atomicReferenceArray2;
    }
}
