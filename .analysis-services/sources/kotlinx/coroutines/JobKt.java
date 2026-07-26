package kotlinx.coroutines;

/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"kotlinx/coroutines/JobKt__FutureKt", "kotlinx/coroutines/JobKt__JobKt"}, k = 4, mv = {1, 9, 0}, xi = 48)
public final class JobKt {
    public static final kotlinx.coroutines.CompletableJob Job(kotlinx.coroutines.Job parent) {
        return kotlinx.coroutines.JobKt__JobKt.Job(parent);
    }

    public static final void cancel(kotlin.coroutines.CoroutineContext $this$cancel, java.util.concurrent.CancellationException cause) {
        kotlinx.coroutines.JobKt__JobKt.cancel($this$cancel, cause);
    }

    public static final void cancel(kotlinx.coroutines.Job $this$cancel, java.lang.String message, java.lang.Throwable cause) {
        kotlinx.coroutines.JobKt__JobKt.cancel($this$cancel, message, cause);
    }

    public static final java.lang.Object cancelAndJoin(kotlinx.coroutines.Job $this$cancelAndJoin, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return kotlinx.coroutines.JobKt__JobKt.cancelAndJoin($this$cancelAndJoin, continuation);
    }

    public static final void cancelChildren(kotlin.coroutines.CoroutineContext $this$cancelChildren, java.util.concurrent.CancellationException cause) {
        kotlinx.coroutines.JobKt__JobKt.cancelChildren($this$cancelChildren, cause);
    }

    public static final void cancelChildren(kotlinx.coroutines.Job $this$cancelChildren, java.util.concurrent.CancellationException cause) {
        kotlinx.coroutines.JobKt__JobKt.cancelChildren($this$cancelChildren, cause);
    }

    public static final void cancelFutureOnCancellation(kotlinx.coroutines.CancellableContinuation<?> cancellableContinuation, java.util.concurrent.Future<?> future) {
        kotlinx.coroutines.JobKt__FutureKt.cancelFutureOnCancellation(cancellableContinuation, future);
    }

    public static final kotlinx.coroutines.DisposableHandle cancelFutureOnCompletion(kotlinx.coroutines.Job $this$cancelFutureOnCompletion, java.util.concurrent.Future<?> future) {
        return kotlinx.coroutines.JobKt__FutureKt.cancelFutureOnCompletion($this$cancelFutureOnCompletion, future);
    }

    public static final kotlinx.coroutines.DisposableHandle disposeOnCompletion(kotlinx.coroutines.Job $this$disposeOnCompletion, kotlinx.coroutines.DisposableHandle handle) {
        return kotlinx.coroutines.JobKt__JobKt.disposeOnCompletion($this$disposeOnCompletion, handle);
    }

    public static final void ensureActive(kotlin.coroutines.CoroutineContext $this$ensureActive) {
        kotlinx.coroutines.JobKt__JobKt.ensureActive($this$ensureActive);
    }

    public static final void ensureActive(kotlinx.coroutines.Job $this$ensureActive) {
        kotlinx.coroutines.JobKt__JobKt.ensureActive($this$ensureActive);
    }

    public static final kotlinx.coroutines.Job getJob(kotlin.coroutines.CoroutineContext $this$job) {
        return kotlinx.coroutines.JobKt__JobKt.getJob($this$job);
    }

    public static final boolean isActive(kotlin.coroutines.CoroutineContext $this$isActive) {
        return kotlinx.coroutines.JobKt__JobKt.isActive($this$isActive);
    }
}
