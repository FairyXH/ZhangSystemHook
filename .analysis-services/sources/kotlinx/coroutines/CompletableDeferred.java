package kotlinx.coroutines;

/* JADX INFO: compiled from: CompletableDeferred.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0000\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002J\u0015\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00028\u0000H&¢\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH&¨\u0006\n"}, d2 = {"Lkotlinx/coroutines/CompletableDeferred;", "T", "Lkotlinx/coroutines/Deferred;", "complete", "", "value", "(Ljava/lang/Object;)Z", "completeExceptionally", "exception", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface CompletableDeferred<T> extends kotlinx.coroutines.Deferred<T> {
    boolean complete(T value);

    boolean completeExceptionally(java.lang.Throwable exception);

    /* JADX INFO: compiled from: CompletableDeferred.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static <T, R> R fold(kotlinx.coroutines.CompletableDeferred<T> completableDeferred, R r, kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.CoroutineContext.Element, ? extends R> operation) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(operation, "operation");
            return (R) kotlinx.coroutines.Deferred.DefaultImpls.fold(completableDeferred, r, operation);
        }

        public static <T, E extends kotlin.coroutines.CoroutineContext.Element> E get(kotlinx.coroutines.CompletableDeferred<T> completableDeferred, kotlin.coroutines.CoroutineContext.Key<E> key) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
            return (E) kotlinx.coroutines.Deferred.DefaultImpls.get(completableDeferred, key);
        }

        public static <T> kotlin.coroutines.CoroutineContext minusKey(kotlinx.coroutines.CompletableDeferred<T> completableDeferred, kotlin.coroutines.CoroutineContext.Key<?> key) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
            return kotlinx.coroutines.Deferred.DefaultImpls.minusKey(completableDeferred, key);
        }

        public static <T> kotlin.coroutines.CoroutineContext plus(kotlinx.coroutines.CompletableDeferred<T> completableDeferred, kotlin.coroutines.CoroutineContext context) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
            return kotlinx.coroutines.Deferred.DefaultImpls.plus(completableDeferred, context);
        }

        @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
        public static <T> kotlinx.coroutines.Job plus(kotlinx.coroutines.CompletableDeferred<T> completableDeferred, kotlinx.coroutines.Job other) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
            return kotlinx.coroutines.Deferred.DefaultImpls.plus((kotlinx.coroutines.Deferred) completableDeferred, other);
        }
    }
}
