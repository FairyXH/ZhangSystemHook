package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Delay.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u008a@"}, d2 = {"<anonymous>", "", "T", "result", "Lkotlinx/coroutines/channels/ChannelResult;", ""}, k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2$1$1", f = "Delay.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
final class FlowKt__DelayKt$sample$2$1$1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.channels.ChannelResult<? extends java.lang.Object>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
    final /* synthetic */ kotlin.jvm.internal.Ref.ObjectRef<java.lang.Object> $lastValue;
    final /* synthetic */ kotlinx.coroutines.channels.ReceiveChannel<kotlin.Unit> $ticker;
    /* synthetic */ java.lang.Object L$0;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    FlowKt__DelayKt$sample$2$1$1(kotlin.jvm.internal.Ref.ObjectRef<java.lang.Object> objectRef, kotlinx.coroutines.channels.ReceiveChannel<kotlin.Unit> receiveChannel, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2$1$1> continuation) {
        super(2, continuation);
        this.$lastValue = objectRef;
        this.$ticker = receiveChannel;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
        kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2$1$1 flowKt__DelayKt$sample$2$1$1 = new kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2$1$1(this.$lastValue, this.$ticker, continuation);
        flowKt__DelayKt$sample$2$1$1.L$0 = obj;
        return flowKt__DelayKt$sample$2$1$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ java.lang.Object invoke(kotlinx.coroutines.channels.ChannelResult<? extends java.lang.Object> channelResult, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return m12847invokeWpGqRn0(channelResult.getHolder(), continuation);
    }

    /* JADX INFO: renamed from: invoke-WpGqRn0, reason: not valid java name */
    public final java.lang.Object m12847invokeWpGqRn0(java.lang.Object obj, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return ((kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2$1$1) create(kotlinx.coroutines.channels.ChannelResult.m12819boximpl(obj), continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [T, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r2v3, types: [T, kotlinx.coroutines.internal.Symbol] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
        kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure(obj);
                ?? holder = ((kotlinx.coroutines.channels.ChannelResult) this.L$0).getHolder();
                kotlin.jvm.internal.Ref.ObjectRef<java.lang.Object> objectRef = this.$lastValue;
                if (!(holder instanceof kotlinx.coroutines.channels.ChannelResult.Failed)) {
                    objectRef.element = holder;
                }
                kotlinx.coroutines.channels.ReceiveChannel<kotlin.Unit> receiveChannel = this.$ticker;
                kotlin.jvm.internal.Ref.ObjectRef<java.lang.Object> objectRef2 = this.$lastValue;
                if (holder instanceof kotlinx.coroutines.channels.ChannelResult.Failed) {
                    java.lang.Throwable it = kotlinx.coroutines.channels.ChannelResult.m12823exceptionOrNullimpl(holder);
                    if (it != null) {
                        throw it;
                    }
                    receiveChannel.cancel((java.util.concurrent.CancellationException) new kotlinx.coroutines.flow.internal.ChildCancelledException());
                    objectRef2.element = kotlinx.coroutines.flow.internal.NullSurrogateKt.DONE;
                }
                java.lang.Object $this$onFailure_u2dWpGqRn0$iv = kotlin.Unit.INSTANCE;
                return $this$onFailure_u2dWpGqRn0$iv;
            default:
                throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
