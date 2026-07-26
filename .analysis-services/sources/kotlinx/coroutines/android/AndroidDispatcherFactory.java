package kotlinx.coroutines.android;

/* JADX INFO: compiled from: HandlerDispatcher.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\nH\u0016J\b\u0010\u000b\u001a\u00020\fH\u0016R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/android/AndroidDispatcherFactory;", "Lkotlinx/coroutines/internal/MainDispatcherFactory;", "()V", "loadPriority", "", "getLoadPriority", "()I", "createDispatcher", "Lkotlinx/coroutines/MainCoroutineDispatcher;", "allFactories", "", "hintOnError", "", "external__kotlinx.coroutines__android_common__kotlinx_coroutines_android"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AndroidDispatcherFactory implements kotlinx.coroutines.internal.MainDispatcherFactory {
    @Override // kotlinx.coroutines.internal.MainDispatcherFactory
    public kotlinx.coroutines.MainCoroutineDispatcher createDispatcher(java.util.List<? extends kotlinx.coroutines.internal.MainDispatcherFactory> allFactories) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(allFactories, "allFactories");
        android.os.Looper mainLooper = android.os.Looper.getMainLooper();
        if (mainLooper == null) {
            throw new java.lang.IllegalStateException("The main looper is not available");
        }
        return new kotlinx.coroutines.android.HandlerContext(kotlinx.coroutines.android.HandlerDispatcherKt.asHandler(mainLooper, true), null, 2, null);
    }

    @Override // kotlinx.coroutines.internal.MainDispatcherFactory
    public java.lang.String hintOnError() {
        return "For tests Dispatchers.setMain from kotlinx-coroutines-test module can be used";
    }

    @Override // kotlinx.coroutines.internal.MainDispatcherFactory
    public int getLoadPriority() {
        return kotlinx.coroutines.internal.LockFreeTaskQueueCore.MAX_CAPACITY_MASK;
    }
}
