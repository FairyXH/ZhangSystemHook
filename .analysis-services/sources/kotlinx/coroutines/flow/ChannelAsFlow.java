package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Channels.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B9\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f¢\u0006\u0002\u0010\rJ\b\u0010\u0010\u001a\u00020\u0011H\u0014J\u001c\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\u0015H\u0096@¢\u0006\u0002\u0010\u0016J\u001c\u0010\u0017\u001a\u00020\u00132\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00028\u00000\u0019H\u0094@¢\u0006\u0002\u0010\u001aJ&\u0010\u001b\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0014J\u000e\u0010\u001c\u001a\b\u0012\u0004\u0012\u00028\u00000\u001dH\u0016J\b\u0010\u001e\u001a\u00020\u0013H\u0002J\u0016\u0010\u001f\u001a\b\u0012\u0004\u0012\u00028\u00000\u00042\u0006\u0010\u0018\u001a\u00020 H\u0016R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006!"}, d2 = {"Lkotlinx/coroutines/flow/ChannelAsFlow;", "T", "Lkotlinx/coroutines/flow/internal/ChannelFlow;", "channel", "Lkotlinx/coroutines/channels/ReceiveChannel;", "consume", "", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "(Lkotlinx/coroutines/channels/ReceiveChannel;ZLkotlin/coroutines/CoroutineContext;ILkotlinx/coroutines/channels/BufferOverflow;)V", "consumed", "Lkotlinx/atomicfu/AtomicBoolean;", "additionalToStringProps", "", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "collectTo", "scope", "Lkotlinx/coroutines/channels/ProducerScope;", "(Lkotlinx/coroutines/channels/ProducerScope;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "create", "dropChannelOperators", "Lkotlinx/coroutines/flow/Flow;", "markConsumed", "produceImpl", "Lkotlinx/coroutines/CoroutineScope;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class ChannelAsFlow<T> extends kotlinx.coroutines.flow.internal.ChannelFlow<T> {
    private final kotlinx.coroutines.channels.ReceiveChannel<T> channel;
    private final boolean consume;
    private final kotlinx.atomicfu.AtomicBoolean consumed;

    public /* synthetic */ ChannelAsFlow(kotlinx.coroutines.channels.ReceiveChannel receiveChannel, boolean z, kotlin.coroutines.CoroutineContext coroutineContext, int i, kotlinx.coroutines.channels.BufferOverflow bufferOverflow, int i2, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(receiveChannel, z, (i2 & 4) != 0 ? kotlin.coroutines.EmptyCoroutineContext.INSTANCE : coroutineContext, (i2 & 8) != 0 ? -3 : i, (i2 & 16) != 0 ? kotlinx.coroutines.channels.BufferOverflow.SUSPEND : bufferOverflow);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public ChannelAsFlow(kotlinx.coroutines.channels.ReceiveChannel<? extends T> channel, boolean consume, kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        super(context, capacity, onBufferOverflow);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(channel, "channel");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        this.channel = channel;
        this.consume = consume;
        this.consumed = kotlinx.atomicfu.AtomicFU.atomic(false);
    }

    private final void markConsumed() {
        if (this.consume && !(!this.consumed.getAndSet(true))) {
            throw new java.lang.IllegalStateException("ReceiveChannel.consumeAsFlow can be collected just once".toString());
        }
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow
    protected kotlinx.coroutines.flow.internal.ChannelFlow<T> create(kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        return new kotlinx.coroutines.flow.ChannelAsFlow(this.channel, this.consume, context, capacity, onBufferOverflow);
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow
    public kotlinx.coroutines.flow.Flow<T> dropChannelOperators() {
        return new kotlinx.coroutines.flow.ChannelAsFlow(this.channel, this.consume, null, 0, null, 28, null);
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow
    protected java.lang.Object collectTo(kotlinx.coroutines.channels.ProducerScope<? super T> producerScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) throws java.lang.Throwable {
        java.lang.Object objEmitAllImpl$FlowKt__ChannelsKt = kotlinx.coroutines.flow.FlowKt__ChannelsKt.emitAllImpl$FlowKt__ChannelsKt(new kotlinx.coroutines.flow.internal.SendingCollector(producerScope), this.channel, this.consume, continuation);
        return objEmitAllImpl$FlowKt__ChannelsKt == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objEmitAllImpl$FlowKt__ChannelsKt : kotlin.Unit.INSTANCE;
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow
    public kotlinx.coroutines.channels.ReceiveChannel<T> produceImpl(kotlinx.coroutines.CoroutineScope scope) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scope, "scope");
        markConsumed();
        if (this.capacity == -3) {
            return this.channel;
        }
        return super.produceImpl(scope);
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow, kotlinx.coroutines.flow.Flow
    public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) throws java.lang.Throwable {
        if (this.capacity == -3) {
            markConsumed();
            java.lang.Object objEmitAllImpl$FlowKt__ChannelsKt = kotlinx.coroutines.flow.FlowKt__ChannelsKt.emitAllImpl$FlowKt__ChannelsKt(flowCollector, this.channel, this.consume, continuation);
            return objEmitAllImpl$FlowKt__ChannelsKt == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objEmitAllImpl$FlowKt__ChannelsKt : kotlin.Unit.INSTANCE;
        }
        java.lang.Object objCollect = super.collect(flowCollector, continuation);
        return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow
    protected java.lang.String additionalToStringProps() {
        return "channel=" + this.channel;
    }
}
