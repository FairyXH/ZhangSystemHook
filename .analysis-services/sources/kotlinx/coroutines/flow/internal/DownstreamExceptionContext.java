package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: SafeCollector.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0001¢\u0006\u0002\u0010\u0005J6\u0010\u0006\u001a\u0002H\u0007\"\u0004\b\u0000\u0010\u00072\u0006\u0010\b\u001a\u0002H\u00072\u0018\u0010\t\u001a\u0014\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u0002H\u00070\nH\u0096\u0001¢\u0006\u0002\u0010\fJ(\u0010\r\u001a\u0004\u0018\u0001H\u000e\"\b\b\u0000\u0010\u000e*\u00020\u000b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u0010H\u0096\u0003¢\u0006\u0002\u0010\u0011J\u0015\u0010\u0012\u001a\u00020\u00012\n\u0010\u000f\u001a\u0006\u0012\u0002\b\u00030\u0010H\u0096\u0001J\u0011\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u0001H\u0096\u0003R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lkotlinx/coroutines/flow/internal/DownstreamExceptionContext;", "Lkotlin/coroutines/CoroutineContext;", "e", "", "originalContext", "(Ljava/lang/Throwable;Lkotlin/coroutines/CoroutineContext;)V", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "Lkotlin/coroutines/CoroutineContext$Element;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", "E", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "minusKey", "plus", "context", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class DownstreamExceptionContext implements kotlin.coroutines.CoroutineContext {
    private final /* synthetic */ kotlin.coroutines.CoroutineContext $$delegate_0;
    public final java.lang.Throwable e;

    @Override // kotlin.coroutines.CoroutineContext
    public <R> R fold(R initial, kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.CoroutineContext.Element, ? extends R> operation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(operation, "operation");
        return (R) this.$$delegate_0.fold(initial, operation);
    }

    @Override // kotlin.coroutines.CoroutineContext
    public <E extends kotlin.coroutines.CoroutineContext.Element> E get(kotlin.coroutines.CoroutineContext.Key<E> key) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
        return (E) this.$$delegate_0.get(key);
    }

    @Override // kotlin.coroutines.CoroutineContext
    public kotlin.coroutines.CoroutineContext minusKey(kotlin.coroutines.CoroutineContext.Key<?> key) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
        return this.$$delegate_0.minusKey(key);
    }

    @Override // kotlin.coroutines.CoroutineContext
    public kotlin.coroutines.CoroutineContext plus(kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        return this.$$delegate_0.plus(context);
    }

    public DownstreamExceptionContext(java.lang.Throwable e, kotlin.coroutines.CoroutineContext originalContext) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(e, "e");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(originalContext, "originalContext");
        this.e = e;
        this.$$delegate_0 = originalContext;
    }
}
