package kotlinx.coroutines;

/* JADX INFO: compiled from: CompletableDeferred.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001f\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002¢\u0006\u0002\u0010\u0004\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u001a)\u0010\u0007\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\n¢\u0006\u0002\u0010\u000b¨\u0006\f"}, d2 = {"CompletableDeferred", "Lkotlinx/coroutines/CompletableDeferred;", "T", "value", "(Ljava/lang/Object;)Lkotlinx/coroutines/CompletableDeferred;", "parent", "Lkotlinx/coroutines/Job;", "completeWith", "", "result", "Lkotlin/Result;", "(Lkotlinx/coroutines/CompletableDeferred;Ljava/lang/Object;)Z", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class CompletableDeferredKt {
    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> boolean completeWith(kotlinx.coroutines.CompletableDeferred<T> completableDeferred, java.lang.Object obj) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completableDeferred, "<this>");
        java.lang.Throwable it = kotlin.Result.m11310exceptionOrNullimpl(obj);
        return it == null ? completableDeferred.complete(obj) : completableDeferred.completeExceptionally(it);
    }

    public static final <T> kotlinx.coroutines.CompletableDeferred<T> CompletableDeferred(kotlinx.coroutines.Job parent) {
        return new kotlinx.coroutines.CompletableDeferredImpl(parent);
    }

    public static /* synthetic */ kotlinx.coroutines.CompletableDeferred CompletableDeferred$default(kotlinx.coroutines.Job job, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            job = null;
        }
        return CompletableDeferred(job);
    }

    public static final <T> kotlinx.coroutines.CompletableDeferred<T> CompletableDeferred(T t) {
        kotlinx.coroutines.CompletableDeferredImpl $this$CompletableDeferred_u24lambda_u242 = new kotlinx.coroutines.CompletableDeferredImpl(null);
        $this$CompletableDeferred_u24lambda_u242.complete(t);
        return $this$CompletableDeferred_u24lambda_u242;
    }
}
