package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: ExceptionsConstructor.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J*\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\b0\tj\u0002`\n2\u000e\u0010\f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\b0\u0007H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R4\u0010\u0005\u001a(\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\b0\u0007\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\b0\tj\u0002`\n0\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/internal/WeakMapCtorCache;", "Lkotlinx/coroutines/internal/CtorCache;", "()V", "cacheLock", "Ljava/util/concurrent/locks/ReentrantReadWriteLock;", "exceptionCtors", "Ljava/util/WeakHashMap;", "Ljava/lang/Class;", "", "Lkotlin/Function1;", "Lkotlinx/coroutines/internal/Ctor;", "get", "key", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class WeakMapCtorCache extends kotlinx.coroutines.internal.CtorCache {
    public static final kotlinx.coroutines.internal.WeakMapCtorCache INSTANCE = new kotlinx.coroutines.internal.WeakMapCtorCache();
    private static final java.util.concurrent.locks.ReentrantReadWriteLock cacheLock = new java.util.concurrent.locks.ReentrantReadWriteLock();
    private static final java.util.WeakHashMap<java.lang.Class<? extends java.lang.Throwable>, kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable>> exceptionCtors = new java.util.WeakHashMap<>();

    private WeakMapCtorCache() {
    }

    @Override // kotlinx.coroutines.internal.CtorCache
    public kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable> get(java.lang.Class<? extends java.lang.Throwable> key) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
        java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock lock = cacheLock.readLock();
        lock.lock();
        try {
            kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable> function1 = exceptionCtors.get(key);
            if (function1 != null) {
                kotlin.jvm.internal.Intrinsics.checkNotNull(function1);
                return function1;
            }
            lock.unlock();
            java.util.concurrent.locks.ReentrantReadWriteLock reentrantReadWriteLock = cacheLock;
            java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock lock2 = reentrantReadWriteLock.readLock();
            int i = 0;
            int readHoldCount = reentrantReadWriteLock.getWriteHoldCount() == 0 ? reentrantReadWriteLock.getReadHoldCount() : 0;
            for (int i2 = 0; i2 < readHoldCount; i2++) {
                lock2.unlock();
            }
            java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
            writeLock.lock();
            try {
                kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable> function12 = exceptionCtors.get(key);
                if (function12 != null) {
                    kotlin.jvm.internal.Intrinsics.checkNotNull(function12);
                    return function12;
                }
                kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable> function1CreateConstructor = kotlinx.coroutines.internal.ExceptionsConstructorKt.createConstructor(key);
                exceptionCtors.put(key, function1CreateConstructor);
                while (i < readHoldCount) {
                    lock2.lock();
                    i++;
                }
                writeLock.unlock();
                return function1CreateConstructor;
            } finally {
                while (i < readHoldCount) {
                    lock2.lock();
                    i++;
                }
                writeLock.unlock();
            }
        } finally {
            lock.unlock();
        }
    }
}
