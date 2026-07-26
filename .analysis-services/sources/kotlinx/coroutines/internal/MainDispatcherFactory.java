package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: MainDispatcherFactory.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0000\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00000\tH&J\n\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/internal/MainDispatcherFactory;", "", "loadPriority", "", "getLoadPriority", "()I", "createDispatcher", "Lkotlinx/coroutines/MainCoroutineDispatcher;", "allFactories", "", "hintOnError", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface MainDispatcherFactory {
    kotlinx.coroutines.MainCoroutineDispatcher createDispatcher(java.util.List<? extends kotlinx.coroutines.internal.MainDispatcherFactory> allFactories);

    int getLoadPriority();

    java.lang.String hintOnError();

    /* JADX INFO: compiled from: MainDispatcherFactory.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static java.lang.String hintOnError(kotlinx.coroutines.internal.MainDispatcherFactory $this) {
            return null;
        }
    }
}
