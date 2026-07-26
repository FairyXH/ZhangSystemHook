package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Delay.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0001\n\u0000\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002H\u008a@"}, d2 = {"<anonymous>", "", "T"}, k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__DelayKt$timeoutInternal$1$1$2", f = "Delay.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
final class FlowKt__DelayKt$timeoutInternal$1$1$2 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<?>, java.lang.Object> {
    final /* synthetic */ long $timeout;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    FlowKt__DelayKt$timeoutInternal$1$1$2(long j, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__DelayKt$timeoutInternal$1$1$2> continuation) {
        super(1, continuation);
        this.$timeout = j;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final kotlin.coroutines.Continuation<kotlin.Unit> create(kotlin.coroutines.Continuation<?> continuation) {
        return new kotlinx.coroutines.flow.FlowKt__DelayKt$timeoutInternal$1$1$2(this.$timeout, continuation);
    }

    @Override // kotlin.jvm.functions.Function1
    public final java.lang.Object invoke(kotlin.coroutines.Continuation<?> continuation) {
        return ((kotlinx.coroutines.flow.FlowKt__DelayKt$timeoutInternal$1$1$2) create(continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
        kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure(obj);
                throw new kotlinx.coroutines.TimeoutCancellationException("Timed out waiting for " + kotlin.time.Duration.m12682toStringimpl(this.$timeout));
            default:
                throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
