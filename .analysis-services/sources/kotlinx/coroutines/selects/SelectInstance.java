package kotlinx.coroutines.selects;

/* JADX INFO: compiled from: Select.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bw\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH&J\u0012\u0010\u000b\u001a\u00020\b2\b\u0010\f\u001a\u0004\u0018\u00010\u0002H&J\u001a\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00022\b\u0010\u0010\u001a\u0004\u0018\u00010\u0002H&R\u0012\u0010\u0003\u001a\u00020\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\u0082\u0001\u0001\u0011¨\u0006\u0012"}, d2 = {"Lkotlinx/coroutines/selects/SelectInstance;", "R", "", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "disposeOnCompletion", "", "disposableHandle", "Lkotlinx/coroutines/DisposableHandle;", "selectInRegistrationPhase", "internalResult", "trySelect", "", "clauseObject", "result", "Lkotlinx/coroutines/selects/SelectInstanceInternal;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface SelectInstance<R> {
    void disposeOnCompletion(kotlinx.coroutines.DisposableHandle disposableHandle);

    kotlin.coroutines.CoroutineContext getContext();

    void selectInRegistrationPhase(java.lang.Object internalResult);

    boolean trySelect(java.lang.Object clauseObject, java.lang.Object result);
}
