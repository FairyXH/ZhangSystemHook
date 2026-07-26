package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: FlowExceptions.common.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001a\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0081\b\u001a\u0018\u0010\u0003\u001a\u00020\u0004*\u00020\u00052\n\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u0007H\u0000¨\u0006\b"}, d2 = {"checkIndexOverflow", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "checkOwnership", "", "Lkotlinx/coroutines/flow/internal/AbortFlowException;", "owner", "Lkotlinx/coroutines/flow/FlowCollector;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class FlowExceptions_commonKt {
    public static final void checkOwnership(kotlinx.coroutines.flow.internal.AbortFlowException $this$checkOwnership, kotlinx.coroutines.flow.FlowCollector<?> owner) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$checkOwnership, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(owner, "owner");
        if ($this$checkOwnership.owner != owner) {
            throw $this$checkOwnership;
        }
    }

    public static final int checkIndexOverflow(int index) {
        if (index < 0) {
            throw new java.lang.ArithmeticException("Index overflow has happened");
        }
        return index;
    }
}
