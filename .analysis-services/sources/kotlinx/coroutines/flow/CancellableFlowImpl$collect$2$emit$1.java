package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Context.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.CancellableFlowImpl$collect$2", f = "Context.kt", i = {}, l = {com.android.bluetooth.BluetoothStatsLog.BLUETOOTH_SMP_PAIRING_EVENT_REPORTED__SMP_FAIL_REASON__PAIRING_FAIL_REASON_BUSY}, m = "emit", n = {}, s = {})
final class CancellableFlowImpl$collect$2$emit$1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    int label;
    /* synthetic */ java.lang.Object result;
    final /* synthetic */ kotlinx.coroutines.flow.CancellableFlowImpl.AnonymousClass2<T> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    CancellableFlowImpl$collect$2$emit$1(kotlinx.coroutines.flow.CancellableFlowImpl.AnonymousClass2<? super T> anonymousClass2, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.CancellableFlowImpl$collect$2$emit$1> continuation) {
        super(continuation);
        this.this$0 = anonymousClass2;
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.emit(null, this);
    }
}
