package kotlinx.coroutines;

/* JADX INFO: compiled from: JobSupport.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b \u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B\u0005¢\u0006\u0002\u0010\u0004J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0015H\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0007R\u001a\u0010\b\u001a\u00020\tX\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0016\u0010\u000e\u001a\u0004\u0018\u00010\u000f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u0016"}, d2 = {"Lkotlinx/coroutines/JobNode;", "Lkotlinx/coroutines/CompletionHandlerBase;", "Lkotlinx/coroutines/DisposableHandle;", "Lkotlinx/coroutines/Incomplete;", "()V", "isActive", "", "()Z", com.android.server.am.HostingRecord.TRIGGER_TYPE_JOB, "Lkotlinx/coroutines/JobSupport;", "getJob", "()Lkotlinx/coroutines/JobSupport;", "setJob", "(Lkotlinx/coroutines/JobSupport;)V", "list", "Lkotlinx/coroutines/NodeList;", "getList", "()Lkotlinx/coroutines/NodeList;", "dispose", "", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class JobNode extends kotlinx.coroutines.CompletionHandlerBase implements kotlinx.coroutines.DisposableHandle, kotlinx.coroutines.Incomplete {
    public kotlinx.coroutines.JobSupport job;

    public final kotlinx.coroutines.JobSupport getJob() {
        kotlinx.coroutines.JobSupport jobSupport = this.job;
        if (jobSupport != null) {
            return jobSupport;
        }
        kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(com.android.server.am.HostingRecord.TRIGGER_TYPE_JOB);
        return null;
    }

    public final void setJob(kotlinx.coroutines.JobSupport jobSupport) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jobSupport, "<set-?>");
        this.job = jobSupport;
    }

    @Override // kotlinx.coroutines.Incomplete
    public boolean isActive() {
        return true;
    }

    @Override // kotlinx.coroutines.Incomplete
    public kotlinx.coroutines.NodeList getList() {
        return null;
    }

    @Override // kotlinx.coroutines.DisposableHandle
    public void dispose() {
        getJob().removeNode$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(this);
    }

    @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
    public java.lang.String toString() {
        return kotlinx.coroutines.DebugStringsKt.getClassSimpleName(this) + "@" + kotlinx.coroutines.DebugStringsKt.getHexAddress(this) + "[job@" + kotlinx.coroutines.DebugStringsKt.getHexAddress(getJob()) + "]";
    }
}
