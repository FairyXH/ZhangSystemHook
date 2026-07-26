package kotlinx.coroutines;

/* JADX INFO: compiled from: ThreadContextElement.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002J\u001d\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00028\u0000H&¢\u0006\u0002\u0010\bJ\u0015\u0010\t\u001a\u00028\u00002\u0006\u0010\u0005\u001a\u00020\u0006H&¢\u0006\u0002\u0010\n¨\u0006\u000b"}, d2 = {"Lkotlinx/coroutines/ThreadContextElement;", "S", "Lkotlin/coroutines/CoroutineContext$Element;", "restoreThreadContext", "", "context", "Lkotlin/coroutines/CoroutineContext;", "oldState", "(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)V", "updateThreadContext", "(Lkotlin/coroutines/CoroutineContext;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface ThreadContextElement<S> extends kotlin.coroutines.CoroutineContext.Element {
    void restoreThreadContext(kotlin.coroutines.CoroutineContext context, S oldState);

    S updateThreadContext(kotlin.coroutines.CoroutineContext context);

    /* JADX INFO: compiled from: ThreadContextElement.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static <S, R> R fold(kotlinx.coroutines.ThreadContextElement<S> threadContextElement, R r, kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.CoroutineContext.Element, ? extends R> operation) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(operation, "operation");
            return (R) kotlin.coroutines.CoroutineContext.Element.DefaultImpls.fold(threadContextElement, r, operation);
        }

        public static <S, E extends kotlin.coroutines.CoroutineContext.Element> E get(kotlinx.coroutines.ThreadContextElement<S> threadContextElement, kotlin.coroutines.CoroutineContext.Key<E> key) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
            return (E) kotlin.coroutines.CoroutineContext.Element.DefaultImpls.get(threadContextElement, key);
        }

        public static <S> kotlin.coroutines.CoroutineContext minusKey(kotlinx.coroutines.ThreadContextElement<S> threadContextElement, kotlin.coroutines.CoroutineContext.Key<?> key) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
            return kotlin.coroutines.CoroutineContext.Element.DefaultImpls.minusKey(threadContextElement, key);
        }

        public static <S> kotlin.coroutines.CoroutineContext plus(kotlinx.coroutines.ThreadContextElement<S> threadContextElement, kotlin.coroutines.CoroutineContext context) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
            return kotlin.coroutines.CoroutineContext.Element.DefaultImpls.plus(threadContextElement, context);
        }
    }
}
