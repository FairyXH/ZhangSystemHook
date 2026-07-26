package kotlinx.coroutines;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: Job.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000B\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a\u0012\u0010\b\u001a\u00020\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0005\u001a\u0019\u0010\u000b\u001a\u00020\u00052\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0005H\u0007¢\u0006\u0002\b\b\u001a\f\u0010\f\u001a\u00020\r*\u00020\u0002H\u0007\u001a\u0018\u0010\f\u001a\u00020\u0001*\u00020\u00022\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0007\u001a\u001c\u0010\f\u001a\u00020\r*\u00020\u00022\u0010\b\u0002\u0010\u000e\u001a\n\u0018\u00010\u0010j\u0004\u0018\u0001`\u0011\u001a\u001e\u0010\f\u001a\u00020\r*\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u00132\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u001a\u0012\u0010\u0014\u001a\u00020\r*\u00020\u0005H\u0086@¢\u0006\u0002\u0010\u0015\u001a\f\u0010\u0016\u001a\u00020\r*\u00020\u0002H\u0007\u001a\u0018\u0010\u0016\u001a\u00020\r*\u00020\u00022\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0007\u001a\u001c\u0010\u0016\u001a\u00020\r*\u00020\u00022\u0010\b\u0002\u0010\u000e\u001a\n\u0018\u00010\u0010j\u0004\u0018\u0001`\u0011\u001a\f\u0010\u0016\u001a\u00020\r*\u00020\u0005H\u0007\u001a\u0018\u0010\u0016\u001a\u00020\r*\u00020\u00052\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0007\u001a\u001c\u0010\u0016\u001a\u00020\r*\u00020\u00052\u0010\b\u0002\u0010\u000e\u001a\n\u0018\u00010\u0010j\u0004\u0018\u0001`\u0011\u001a\u0014\u0010\u0017\u001a\u00020\u0018*\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u0018H\u0000\u001a\n\u0010\u001a\u001a\u00020\r*\u00020\u0002\u001a\n\u0010\u001a\u001a\u00020\r*\u00020\u0005\u001a\u001b\u0010\u001b\u001a\u00020\u000f*\u0004\u0018\u00010\u000f2\u0006\u0010\u0004\u001a\u00020\u0005H\u0002¢\u0006\u0002\b\u001c\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0000\u0010\u0003\"\u0015\u0010\u0004\u001a\u00020\u0005*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u001d"}, d2 = {"isActive", "", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)Z", com.android.server.am.HostingRecord.TRIGGER_TYPE_JOB, "Lkotlinx/coroutines/Job;", "getJob", "(Lkotlin/coroutines/CoroutineContext;)Lkotlinx/coroutines/Job;", "Job", "Lkotlinx/coroutines/CompletableJob;", "parent", "Job0", "cancel", "", "cause", "", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "message", "", "cancelAndJoin", "(Lkotlinx/coroutines/Job;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cancelChildren", "disposeOnCompletion", "Lkotlinx/coroutines/DisposableHandle;", "handle", "ensureActive", "orCancellation", "orCancellation$JobKt__JobKt", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/JobKt")
public final /* synthetic */ class JobKt__JobKt {
    public static final kotlinx.coroutines.CompletableJob Job(kotlinx.coroutines.Job parent) {
        return new kotlinx.coroutines.JobImpl(parent);
    }

    public static /* synthetic */ kotlinx.coroutines.CompletableJob Job$default(kotlinx.coroutines.Job job, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            job = null;
        }
        return kotlinx.coroutines.JobKt.Job(job);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    /* JADX INFO: renamed from: Job, reason: collision with other method in class */
    public static final /* synthetic */ kotlinx.coroutines.Job m12803Job(kotlinx.coroutines.Job parent) {
        return kotlinx.coroutines.JobKt.Job(parent);
    }

    /* JADX INFO: renamed from: Job$default, reason: collision with other method in class */
    public static /* synthetic */ kotlinx.coroutines.Job m12804Job$default(kotlinx.coroutines.Job job, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            job = null;
        }
        return m12803Job(job);
    }

    public static final kotlinx.coroutines.DisposableHandle disposeOnCompletion(kotlinx.coroutines.Job $this$disposeOnCompletion, kotlinx.coroutines.DisposableHandle handle) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$disposeOnCompletion, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(handle, "handle");
        kotlinx.coroutines.CompletionHandlerBase $this$asHandler$iv = new kotlinx.coroutines.DisposeOnCompletion(handle);
        return $this$disposeOnCompletion.invokeOnCompletion($this$asHandler$iv);
    }

    public static final java.lang.Object cancelAndJoin(kotlinx.coroutines.Job $this$cancelAndJoin, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlinx.coroutines.Job.DefaultImpls.cancel$default($this$cancelAndJoin, (java.util.concurrent.CancellationException) null, 1, (java.lang.Object) null);
        java.lang.Object objJoin = $this$cancelAndJoin.join(continuation);
        return objJoin == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objJoin : kotlin.Unit.INSTANCE;
    }

    public static /* synthetic */ void cancelChildren$default(kotlinx.coroutines.Job job, java.util.concurrent.CancellationException cancellationException, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            cancellationException = null;
        }
        kotlinx.coroutines.JobKt.cancelChildren(job, cancellationException);
    }

    public static final void cancelChildren(kotlinx.coroutines.Job $this$cancelChildren, java.util.concurrent.CancellationException cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$cancelChildren, "<this>");
        for (java.lang.Object element$iv : $this$cancelChildren.getChildren()) {
            kotlinx.coroutines.Job it = (kotlinx.coroutines.Job) element$iv;
            it.cancel(cause);
        }
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    public static final /* synthetic */ void cancelChildren(kotlinx.coroutines.Job $this$cancelChildren) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$cancelChildren, "<this>");
        kotlinx.coroutines.JobKt.cancelChildren($this$cancelChildren, (java.util.concurrent.CancellationException) null);
    }

    public static /* synthetic */ void cancelChildren$default(kotlinx.coroutines.Job job, java.lang.Throwable th, int i, java.lang.Object obj) throws java.lang.Throwable {
        if ((i & 1) != 0) {
            th = null;
        }
        cancelChildren(job, th);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    public static final /* synthetic */ void cancelChildren(kotlinx.coroutines.Job $this$cancelChildren, java.lang.Throwable cause) throws java.lang.Throwable {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$cancelChildren, "<this>");
        for (java.lang.Object element$iv : $this$cancelChildren.getChildren()) {
            kotlinx.coroutines.Job it = (kotlinx.coroutines.Job) element$iv;
            kotlinx.coroutines.JobSupport jobSupport = it instanceof kotlinx.coroutines.JobSupport ? (kotlinx.coroutines.JobSupport) it : null;
            if (jobSupport != null) {
                jobSupport.cancelInternal(orCancellation$JobKt__JobKt(cause, $this$cancelChildren));
            }
        }
    }

    public static final boolean isActive(kotlin.coroutines.CoroutineContext $this$isActive) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$isActive, "<this>");
        kotlinx.coroutines.Job job = (kotlinx.coroutines.Job) $this$isActive.get(kotlinx.coroutines.Job.INSTANCE);
        if (job != null) {
            return job.isActive();
        }
        return true;
    }

    public static /* synthetic */ void cancel$default(kotlin.coroutines.CoroutineContext coroutineContext, java.util.concurrent.CancellationException cancellationException, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            cancellationException = null;
        }
        kotlinx.coroutines.JobKt.cancel(coroutineContext, cancellationException);
    }

    public static final void cancel(kotlin.coroutines.CoroutineContext $this$cancel, java.util.concurrent.CancellationException cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$cancel, "<this>");
        kotlinx.coroutines.Job job = (kotlinx.coroutines.Job) $this$cancel.get(kotlinx.coroutines.Job.INSTANCE);
        if (job != null) {
            job.cancel(cause);
        }
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    public static final /* synthetic */ void cancel(kotlin.coroutines.CoroutineContext $this$cancel) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$cancel, "<this>");
        kotlinx.coroutines.JobKt.cancel($this$cancel, (java.util.concurrent.CancellationException) null);
    }

    public static final void ensureActive(kotlinx.coroutines.Job $this$ensureActive) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$ensureActive, "<this>");
        if (!$this$ensureActive.isActive()) {
            throw $this$ensureActive.getCancellationException();
        }
    }

    public static final void ensureActive(kotlin.coroutines.CoroutineContext $this$ensureActive) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$ensureActive, "<this>");
        kotlinx.coroutines.Job job = (kotlinx.coroutines.Job) $this$ensureActive.get(kotlinx.coroutines.Job.INSTANCE);
        if (job != null) {
            kotlinx.coroutines.JobKt.ensureActive(job);
        }
    }

    public static final void cancel(kotlinx.coroutines.Job $this$cancel, java.lang.String message, java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$cancel, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(message, "message");
        $this$cancel.cancel(kotlinx.coroutines.ExceptionsKt.CancellationException(message, cause));
    }

    public static /* synthetic */ void cancel$default(kotlinx.coroutines.Job job, java.lang.String str, java.lang.Throwable th, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            th = null;
        }
        kotlinx.coroutines.JobKt.cancel(job, str, th);
    }

    public static /* synthetic */ boolean cancel$default(kotlin.coroutines.CoroutineContext coroutineContext, java.lang.Throwable th, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            th = null;
        }
        return cancel(coroutineContext, th);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    public static final /* synthetic */ boolean cancel(kotlin.coroutines.CoroutineContext $this$cancel, java.lang.Throwable cause) throws java.lang.Throwable {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$cancel, "<this>");
        kotlin.coroutines.CoroutineContext.Element element = $this$cancel.get(kotlinx.coroutines.Job.INSTANCE);
        kotlinx.coroutines.JobSupport job = element instanceof kotlinx.coroutines.JobSupport ? (kotlinx.coroutines.JobSupport) element : null;
        if (job == null) {
            return false;
        }
        job.cancelInternal(orCancellation$JobKt__JobKt(cause, job));
        return true;
    }

    public static /* synthetic */ void cancelChildren$default(kotlin.coroutines.CoroutineContext coroutineContext, java.util.concurrent.CancellationException cancellationException, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            cancellationException = null;
        }
        kotlinx.coroutines.JobKt.cancelChildren(coroutineContext, cancellationException);
    }

    public static final void cancelChildren(kotlin.coroutines.CoroutineContext $this$cancelChildren, java.util.concurrent.CancellationException cause) {
        kotlin.sequences.Sequence<kotlinx.coroutines.Job> children;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$cancelChildren, "<this>");
        kotlinx.coroutines.Job job = (kotlinx.coroutines.Job) $this$cancelChildren.get(kotlinx.coroutines.Job.INSTANCE);
        if (job == null || (children = job.getChildren()) == null) {
            return;
        }
        for (java.lang.Object element$iv : children) {
            kotlinx.coroutines.Job it = (kotlinx.coroutines.Job) element$iv;
            it.cancel(cause);
        }
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    public static final /* synthetic */ void cancelChildren(kotlin.coroutines.CoroutineContext $this$cancelChildren) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$cancelChildren, "<this>");
        kotlinx.coroutines.JobKt.cancelChildren($this$cancelChildren, (java.util.concurrent.CancellationException) null);
    }

    public static final kotlinx.coroutines.Job getJob(kotlin.coroutines.CoroutineContext $this$job) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$job, "<this>");
        kotlinx.coroutines.Job job = (kotlinx.coroutines.Job) $this$job.get(kotlinx.coroutines.Job.INSTANCE);
        if (job != null) {
            return job;
        }
        throw new java.lang.IllegalStateException(("Current context doesn't contain Job in it: " + $this$job).toString());
    }

    public static /* synthetic */ void cancelChildren$default(kotlin.coroutines.CoroutineContext coroutineContext, java.lang.Throwable th, int i, java.lang.Object obj) throws java.lang.Throwable {
        if ((i & 1) != 0) {
            th = null;
        }
        cancelChildren(coroutineContext, th);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    public static final /* synthetic */ void cancelChildren(kotlin.coroutines.CoroutineContext $this$cancelChildren, java.lang.Throwable cause) throws java.lang.Throwable {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$cancelChildren, "<this>");
        kotlinx.coroutines.Job job = (kotlinx.coroutines.Job) $this$cancelChildren.get(kotlinx.coroutines.Job.INSTANCE);
        if (job == null) {
            return;
        }
        for (java.lang.Object element$iv : job.getChildren()) {
            kotlinx.coroutines.Job it = (kotlinx.coroutines.Job) element$iv;
            kotlinx.coroutines.JobSupport jobSupport = it instanceof kotlinx.coroutines.JobSupport ? (kotlinx.coroutines.JobSupport) it : null;
            if (jobSupport != null) {
                jobSupport.cancelInternal(orCancellation$JobKt__JobKt(cause, job));
            }
        }
    }

    private static final java.lang.Throwable orCancellation$JobKt__JobKt(java.lang.Throwable $this$orCancellation, kotlinx.coroutines.Job job) {
        return $this$orCancellation == null ? new kotlinx.coroutines.JobCancellationException("Job was cancelled", null, job) : $this$orCancellation;
    }
}
