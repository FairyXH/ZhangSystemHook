package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: SharedFlow.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\b\u0000\u0018\u00002\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u0014\u0010\t\u001a\u00020\n2\n\u0010\u000b\u001a\u0006\u0012\u0002\b\u00030\u0002H\u0016J'\u0010\f\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u00050\r2\n\u0010\u000b\u001a\u0006\u0012\u0002\b\u00030\u0002H\u0016¢\u0006\u0002\u0010\u000eR\u001a\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u00058\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u00020\b8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lkotlinx/coroutines/flow/SharedFlowSlot;", "Lkotlinx/coroutines/flow/internal/AbstractSharedFlowSlot;", "Lkotlinx/coroutines/flow/SharedFlowImpl;", "()V", "cont", "Lkotlin/coroutines/Continuation;", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "allocateLocked", "", "flow", "freeLocked", "", "(Lkotlinx/coroutines/flow/SharedFlowImpl;)[Lkotlin/coroutines/Continuation;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SharedFlowSlot extends kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot<kotlinx.coroutines.flow.SharedFlowImpl<?>> {
    public kotlin.coroutines.Continuation<? super kotlin.Unit> cont;
    public long index = -1;

    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot
    public boolean allocateLocked(kotlinx.coroutines.flow.SharedFlowImpl<?> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        if (this.index >= 0) {
            return false;
        }
        this.index = flow.updateNewCollectorIndexLocked$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        return true;
    }

    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot
    public kotlin.coroutines.Continuation<kotlin.Unit>[] freeLocked(kotlinx.coroutines.flow.SharedFlowImpl<?> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(this.index >= 0)) {
                throw new java.lang.AssertionError();
            }
        }
        long oldIndex = this.index;
        this.index = -1L;
        this.cont = null;
        return flow.updateCollectorIndexLocked$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(oldIndex);
    }
}
