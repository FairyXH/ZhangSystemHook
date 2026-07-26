package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: LockFreeTaskQueue.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0010\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0013\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00028\u0000¢\u0006\u0002\u0010\u0012J\u0006\u0010\u0013\u001a\u00020\u0014J\u0006\u0010\u0015\u001a\u00020\u0004J&\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00180\u0017\"\u0004\b\u0001\u0010\u00182\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u0002H\u00180\u001aJ\r\u0010\u001b\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010\u001cR$\u0010\u0006\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00028\u00000\bj\b\u0012\u0004\u0012\u00028\u0000`\t0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\n\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u001d"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeTaskQueue;", "E", "", "singleConsumer", "", "(Z)V", "_cur", "Lkotlinx/atomicfu/AtomicRef;", "Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;", "Lkotlinx/coroutines/internal/Core;", "isEmpty", "()Z", "size", "", "getSize", "()I", "addLast", "element", "(Ljava/lang/Object;)Z", "close", "", "isClosed", "map", "", "R", "transform", "Lkotlin/Function1;", "removeFirstOrNull", "()Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class LockFreeTaskQueue<E> {
    private final kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeTaskQueueCore<E>> _cur;

    public LockFreeTaskQueue(boolean singleConsumer) {
        this._cur = kotlinx.atomicfu.AtomicFU.atomic(new kotlinx.coroutines.internal.LockFreeTaskQueueCore(8, singleConsumer));
    }

    public final boolean isEmpty() {
        return this._cur.getValue().isEmpty();
    }

    public final int getSize() {
        return this._cur.getValue().getSize();
    }

    public final void close() {
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeTaskQueueCore<E>> atomicRef = this._cur;
        while (true) {
            kotlinx.coroutines.internal.LockFreeTaskQueueCore<E> value = atomicRef.getValue();
            if (value.close()) {
                return;
            } else {
                this._cur.compareAndSet(value, value.next());
            }
        }
    }

    public final boolean addLast(E element) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeTaskQueueCore<E>> atomicRef = this._cur;
        while (true) {
            kotlinx.coroutines.internal.LockFreeTaskQueueCore<E> value = atomicRef.getValue();
            switch (value.addLast(element)) {
                case 0:
                    return true;
                case 1:
                    this._cur.compareAndSet(value, value.next());
                    break;
                case 2:
                    return false;
            }
        }
    }

    public final E removeFirstOrNull() {
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeTaskQueueCore<E>> atomicRef = this._cur;
        while (true) {
            kotlinx.coroutines.internal.LockFreeTaskQueueCore<E> value = atomicRef.getValue();
            E e = (E) value.removeFirstOrNull();
            if (e != kotlinx.coroutines.internal.LockFreeTaskQueueCore.REMOVE_FROZEN) {
                return e;
            }
            this._cur.compareAndSet(value, value.next());
        }
    }

    public final <R> java.util.List<R> map(kotlin.jvm.functions.Function1<? super E, ? extends R> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return this._cur.getValue().map(transform);
    }

    public final boolean isClosed() {
        return this._cur.getValue().isClosed();
    }
}
