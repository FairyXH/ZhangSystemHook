package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: ChannelFlow.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00010\u0002B1\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ&\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\r2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0014J\u000e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0016J\u001c\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00028\u00000\u0012H\u0094@¢\u0006\u0002\u0010\u0013¨\u0006\u0014"}, d2 = {"Lkotlinx/coroutines/flow/internal/ChannelFlowOperatorImpl;", "T", "Lkotlinx/coroutines/flow/internal/ChannelFlowOperator;", "flow", "Lkotlinx/coroutines/flow/Flow;", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/coroutines/CoroutineContext;ILkotlinx/coroutines/channels/BufferOverflow;)V", "create", "Lkotlinx/coroutines/flow/internal/ChannelFlow;", "dropChannelOperators", "flowCollect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ChannelFlowOperatorImpl<T> extends kotlinx.coroutines.flow.internal.ChannelFlowOperator<T, T> {
    public /* synthetic */ ChannelFlowOperatorImpl(kotlinx.coroutines.flow.Flow flow, kotlin.coroutines.EmptyCoroutineContext emptyCoroutineContext, int i, kotlinx.coroutines.channels.BufferOverflow bufferOverflow, int i2, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(flow, (i2 & 2) != 0 ? kotlin.coroutines.EmptyCoroutineContext.INSTANCE : emptyCoroutineContext, (i2 & 4) != 0 ? -3 : i, (i2 & 8) != 0 ? kotlinx.coroutines.channels.BufferOverflow.SUSPEND : bufferOverflow);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelFlowOperatorImpl(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        super(flow, context, capacity, onBufferOverflow);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow
    protected kotlinx.coroutines.flow.internal.ChannelFlow<T> create(kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        return new kotlinx.coroutines.flow.internal.ChannelFlowOperatorImpl(this.flow, context, capacity, onBufferOverflow);
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow
    public kotlinx.coroutines.flow.Flow<T> dropChannelOperators() {
        return (kotlinx.coroutines.flow.Flow<T>) this.flow;
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // kotlinx.coroutines.flow.internal.ChannelFlowOperator
    protected java.lang.Object flowCollect(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        java.lang.Object objCollect = this.flow.collect((kotlinx.coroutines.flow.FlowCollector<? super S>) flowCollector, continuation);
        return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
    }
}
