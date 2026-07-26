package kotlinx.coroutines;

/* JADX INFO: compiled from: Deferred.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0003\n\u0000\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002J\u000e\u0010\u0007\u001a\u00028\u0000H¦@¢\u0006\u0002\u0010\bJ\r\u0010\t\u001a\u00028\u0000H'¢\u0006\u0002\u0010\nJ\n\u0010\u000b\u001a\u0004\u0018\u00010\fH'R\u0018\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/Deferred;", "T", "Lkotlinx/coroutines/Job;", "onAwait", "Lkotlinx/coroutines/selects/SelectClause1;", "getOnAwait", "()Lkotlinx/coroutines/selects/SelectClause1;", "await", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCompleted", "()Ljava/lang/Object;", "getCompletionExceptionOrNull", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface Deferred<T> extends kotlinx.coroutines.Job {
    java.lang.Object await(kotlin.coroutines.Continuation<? super T> continuation);

    T getCompleted();

    java.lang.Throwable getCompletionExceptionOrNull();

    kotlinx.coroutines.selects.SelectClause1<T> getOnAwait();

    /* JADX INFO: compiled from: Deferred.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static <T, R> R fold(kotlinx.coroutines.Deferred<? extends T> deferred, R r, kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.CoroutineContext.Element, ? extends R> operation) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(operation, "operation");
            return (R) kotlinx.coroutines.Job.DefaultImpls.fold(deferred, r, operation);
        }

        public static <T, E extends kotlin.coroutines.CoroutineContext.Element> E get(kotlinx.coroutines.Deferred<? extends T> deferred, kotlin.coroutines.CoroutineContext.Key<E> key) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
            return (E) kotlinx.coroutines.Job.DefaultImpls.get(deferred, key);
        }

        public static <T> kotlin.coroutines.CoroutineContext minusKey(kotlinx.coroutines.Deferred<? extends T> deferred, kotlin.coroutines.CoroutineContext.Key<?> key) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
            return kotlinx.coroutines.Job.DefaultImpls.minusKey(deferred, key);
        }

        public static <T> kotlin.coroutines.CoroutineContext plus(kotlinx.coroutines.Deferred<? extends T> deferred, kotlin.coroutines.CoroutineContext context) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
            return kotlinx.coroutines.Job.DefaultImpls.plus(deferred, context);
        }

        @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
        public static <T> kotlinx.coroutines.Job plus(kotlinx.coroutines.Deferred<? extends T> deferred, kotlinx.coroutines.Job other) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
            return kotlinx.coroutines.Job.DefaultImpls.plus((kotlinx.coroutines.Job) deferred, other);
        }
    }
}
