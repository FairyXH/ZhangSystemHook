package kotlinx.coroutines;

/* JADX INFO: compiled from: Job.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "This is internal API and may be removed in the future releases")
@kotlin.Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H'¨\u0006\u0006"}, d2 = {"Lkotlinx/coroutines/ChildJob;", "Lkotlinx/coroutines/Job;", "parentCancelled", "", "parentJob", "Lkotlinx/coroutines/ParentJob;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface ChildJob extends kotlinx.coroutines.Job {
    void parentCancelled(kotlinx.coroutines.ParentJob parentJob);

    /* JADX INFO: compiled from: Job.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static <R> R fold(kotlinx.coroutines.ChildJob childJob, R r, kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.CoroutineContext.Element, ? extends R> operation) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(operation, "operation");
            return (R) kotlinx.coroutines.Job.DefaultImpls.fold(childJob, r, operation);
        }

        public static <E extends kotlin.coroutines.CoroutineContext.Element> E get(kotlinx.coroutines.ChildJob childJob, kotlin.coroutines.CoroutineContext.Key<E> key) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
            return (E) kotlinx.coroutines.Job.DefaultImpls.get(childJob, key);
        }

        public static kotlin.coroutines.CoroutineContext minusKey(kotlinx.coroutines.ChildJob $this, kotlin.coroutines.CoroutineContext.Key<?> key) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(key, "key");
            return kotlinx.coroutines.Job.DefaultImpls.minusKey($this, key);
        }

        public static kotlin.coroutines.CoroutineContext plus(kotlinx.coroutines.ChildJob $this, kotlin.coroutines.CoroutineContext context) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
            return kotlinx.coroutines.Job.DefaultImpls.plus($this, context);
        }

        @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
        public static kotlinx.coroutines.Job plus(kotlinx.coroutines.ChildJob $this, kotlinx.coroutines.Job other) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
            return kotlinx.coroutines.Job.DefaultImpls.plus((kotlinx.coroutines.Job) $this, other);
        }
    }
}
