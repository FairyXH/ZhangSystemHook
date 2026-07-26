package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Channels.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ChannelsKt", f = "Channels.kt", i = {0, 0, 0, 1, 1, 1}, l = {36, 37}, m = "emitAllImpl$FlowKt__ChannelsKt", n = {"$this$emitAllImpl", "channel", "consume", "$this$emitAllImpl", "channel", "consume"}, s = {"L$0", "L$1", "Z$0", "L$0", "L$1", "Z$0"})
final class FlowKt__ChannelsKt$emitAllImpl$1<T> extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    java.lang.Object L$0;
    java.lang.Object L$1;
    java.lang.Object L$2;
    boolean Z$0;
    int label;
    /* synthetic */ java.lang.Object result;

    FlowKt__ChannelsKt$emitAllImpl$1(kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__ChannelsKt$emitAllImpl$1> continuation) {
        super(continuation);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return kotlinx.coroutines.flow.FlowKt__ChannelsKt.emitAllImpl$FlowKt__ChannelsKt(null, null, false, this);
    }
}
