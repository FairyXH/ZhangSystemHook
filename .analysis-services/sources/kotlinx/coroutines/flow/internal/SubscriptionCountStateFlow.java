package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: AbstractSharedFlow.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\b\u0012\u0004\u0012\u00020\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0002¢\u0006\u0002\u0010\u0005J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0002R\u0014\u0010\u0006\u001a\u00020\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/flow/internal/SubscriptionCountStateFlow;", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lkotlinx/coroutines/flow/SharedFlowImpl;", "initialValue", "(I)V", "value", "getValue", "()Ljava/lang/Integer;", "increment", "", "delta", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class SubscriptionCountStateFlow extends kotlinx.coroutines.flow.SharedFlowImpl<java.lang.Integer> implements kotlinx.coroutines.flow.StateFlow<java.lang.Integer> {
    public SubscriptionCountStateFlow(int initialValue) {
        super(1, Integer.MAX_VALUE, kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST);
        tryEmit(java.lang.Integer.valueOf(initialValue));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlinx.coroutines.flow.StateFlow
    public java.lang.Integer getValue() {
        java.lang.Integer numValueOf;
        synchronized (this) {
            numValueOf = java.lang.Integer.valueOf(getLastReplayedLocked().intValue());
        }
        return numValueOf;
    }

    public final boolean increment(int delta) {
        boolean zTryEmit;
        synchronized (this) {
            zTryEmit = tryEmit(java.lang.Integer.valueOf(getLastReplayedLocked().intValue() + delta));
        }
        return zTryEmit;
    }
}
