package kotlinx.coroutines;

/* JADX INFO: compiled from: JobSupport.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\u0003\n\u0000\b\u0011\u0018\u00002\u00020\u00012\u00020\u0002B\u000f\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\f\u001a\u00020\u0007H\u0016J\u0010\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0006\u001a\u00020\u0007H\u0003R\u0014\u0010\u0006\u001a\u00020\u0007X\u0090\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u00078PX\u0090\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\t¨\u0006\u0010"}, d2 = {"Lkotlinx/coroutines/JobImpl;", "Lkotlinx/coroutines/JobSupport;", "Lkotlinx/coroutines/CompletableJob;", "parent", "Lkotlinx/coroutines/Job;", "(Lkotlinx/coroutines/Job;)V", "handlesException", "", "getHandlesException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()Z", "onCancelComplete", "getOnCancelComplete$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "complete", "completeExceptionally", "exception", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class JobImpl extends kotlinx.coroutines.JobSupport implements kotlinx.coroutines.CompletableJob {
    private final boolean handlesException;

    public JobImpl(kotlinx.coroutines.Job parent) {
        super(true);
        initParentJob(parent);
        this.handlesException = handlesException();
    }

    @Override // kotlinx.coroutines.JobSupport
    public boolean getOnCancelComplete$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return true;
    }

    @Override // kotlinx.coroutines.JobSupport
    /* JADX INFO: renamed from: getHandlesException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host, reason: from getter */
    public boolean getHandlesException() {
        return this.handlesException;
    }

    @Override // kotlinx.coroutines.CompletableJob
    public boolean complete() {
        return makeCompleting$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlin.Unit.INSTANCE);
    }

    @Override // kotlinx.coroutines.CompletableJob
    public boolean completeExceptionally(java.lang.Throwable exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        return makeCompleting$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(new kotlinx.coroutines.CompletedExceptionally(exception, false, 2, null));
    }

    private final boolean handlesException() {
        kotlinx.coroutines.JobSupport parentJob;
        kotlinx.coroutines.JobSupport job;
        kotlinx.coroutines.ChildHandle parentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = getParentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        kotlinx.coroutines.ChildHandleNode childHandleNode = parentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host instanceof kotlinx.coroutines.ChildHandleNode ? (kotlinx.coroutines.ChildHandleNode) parentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host : null;
        if (childHandleNode == null || (parentJob = childHandleNode.getJob()) == null) {
            return false;
        }
        while (!parentJob.getHandlesException()) {
            kotlinx.coroutines.ChildHandle parentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host2 = parentJob.getParentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            kotlinx.coroutines.ChildHandleNode childHandleNode2 = parentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host2 instanceof kotlinx.coroutines.ChildHandleNode ? (kotlinx.coroutines.ChildHandleNode) parentHandle$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host2 : null;
            if (childHandleNode2 == null || (job = childHandleNode2.getJob()) == null) {
                return false;
            }
            parentJob = job;
        }
        return true;
    }
}
