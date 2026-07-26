package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: NopCollector.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\bÀ\u0002\u0018\u00002\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0002H\u0096@¢\u0006\u0002\u0010\u0007¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/flow/internal/NopCollector;", "Lkotlinx/coroutines/flow/FlowCollector;", "", "()V", "emit", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class NopCollector implements kotlinx.coroutines.flow.FlowCollector<java.lang.Object> {
    public static final kotlinx.coroutines.flow.internal.NopCollector INSTANCE = new kotlinx.coroutines.flow.internal.NopCollector();

    private NopCollector() {
    }

    @Override // kotlinx.coroutines.flow.FlowCollector
    public java.lang.Object emit(java.lang.Object value, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return kotlin.Unit.INSTANCE;
    }
}
