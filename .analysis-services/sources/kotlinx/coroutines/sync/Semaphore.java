package kotlinx.coroutines.sync;

/* JADX INFO: compiled from: Semaphore.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u0006\u001a\u00020\u0007H¦@¢\u0006\u0002\u0010\bJ\b\u0010\t\u001a\u00020\u0007H&J\b\u0010\n\u001a\u00020\u000bH&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/sync/Semaphore;", "", "availablePermits", "", "getAvailablePermits", "()I", "acquire", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "release", "tryAcquire", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface Semaphore {
    java.lang.Object acquire(kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);

    int getAvailablePermits();

    void release();

    boolean tryAcquire();
}
