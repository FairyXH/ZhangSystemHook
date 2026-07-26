package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Emitters.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__EmittersKt", f = "Emitters.kt", i = {0}, l = {com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MTE_POLICY}, m = "invokeSafely$FlowKt__EmittersKt", n = {"cause"}, s = {"L$0"})
final class FlowKt__EmittersKt$invokeSafely$1<T> extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    java.lang.Object L$0;
    int label;
    /* synthetic */ java.lang.Object result;

    FlowKt__EmittersKt$invokeSafely$1(kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__EmittersKt$invokeSafely$1> continuation) {
        super(continuation);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return kotlinx.coroutines.flow.FlowKt__EmittersKt.invokeSafely$FlowKt__EmittersKt(null, null, null, this);
    }
}
