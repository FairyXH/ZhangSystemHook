package kotlinx.coroutines;

/* JADX INFO: compiled from: MainCoroutineDispatcher.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\nH\u0016J\n\u0010\u000b\u001a\u0004\u0018\u00010\nH\u0005R\u0012\u0010\u0003\u001a\u00020\u0000X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/MainCoroutineDispatcher;", "Lkotlinx/coroutines/CoroutineDispatcher;", "()V", "immediate", "getImmediate", "()Lkotlinx/coroutines/MainCoroutineDispatcher;", "limitedParallelism", "parallelism", "", "toString", "", "toStringInternalImpl", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class MainCoroutineDispatcher extends kotlinx.coroutines.CoroutineDispatcher {
    public abstract kotlinx.coroutines.MainCoroutineDispatcher getImmediate();

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public java.lang.String toString() {
        java.lang.String stringInternalImpl = toStringInternalImpl();
        if (stringInternalImpl != null) {
            return stringInternalImpl;
        }
        return kotlinx.coroutines.DebugStringsKt.getClassSimpleName(this) + "@" + kotlinx.coroutines.DebugStringsKt.getHexAddress(this);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public kotlinx.coroutines.CoroutineDispatcher limitedParallelism(int parallelism) {
        kotlinx.coroutines.internal.LimitedDispatcherKt.checkParallelism(parallelism);
        return this;
    }

    protected final java.lang.String toStringInternalImpl() {
        kotlinx.coroutines.MainCoroutineDispatcher immediate;
        kotlinx.coroutines.MainCoroutineDispatcher main = kotlinx.coroutines.Dispatchers.getMain();
        if (this == main) {
            return "Dispatchers.Main";
        }
        try {
            immediate = main.getImmediate();
        } catch (java.lang.UnsupportedOperationException e) {
            immediate = null;
        }
        if (this == immediate) {
            return "Dispatchers.Main.immediate";
        }
        return null;
    }
}
