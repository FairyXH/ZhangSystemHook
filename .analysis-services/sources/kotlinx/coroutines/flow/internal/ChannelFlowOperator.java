package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: ChannelFlow.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\b \u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\b\u0012\u0004\u0012\u0002H\u00020\u0003B+\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u001c\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00010\u0010H\u0096@¢\u0006\u0002\u0010\u0011J\u001c\u0010\u0012\u001a\u00020\u000e2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00028\u00010\u0014H\u0094@¢\u0006\u0002\u0010\u0015J$\u0010\u0016\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00010\u00102\u0006\u0010\u0017\u001a\u00020\u0007H\u0082@¢\u0006\u0002\u0010\u0018J\u001c\u0010\u0019\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00010\u0010H¤@¢\u0006\u0002\u0010\u0011J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u0016\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u00058\u0004X\u0085\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"}, d2 = {"Lkotlinx/coroutines/flow/internal/ChannelFlowOperator;", "S", "T", "Lkotlinx/coroutines/flow/internal/ChannelFlow;", "flow", "Lkotlinx/coroutines/flow/Flow;", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/coroutines/CoroutineContext;ILkotlinx/coroutines/channels/BufferOverflow;)V", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "collectTo", "scope", "Lkotlinx/coroutines/channels/ProducerScope;", "(Lkotlinx/coroutines/channels/ProducerScope;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "collectWithContextUndispatched", "newContext", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/CoroutineContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "flowCollect", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class ChannelFlowOperator<S, T> extends kotlinx.coroutines.flow.internal.ChannelFlow<T> {
    protected final kotlinx.coroutines.flow.Flow<S> flow;

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow, kotlinx.coroutines.flow.Flow
    public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return collect$suspendImpl((kotlinx.coroutines.flow.internal.ChannelFlowOperator) this, (kotlinx.coroutines.flow.FlowCollector) flowCollector, continuation);
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow
    protected java.lang.Object collectTo(kotlinx.coroutines.channels.ProducerScope<? super T> producerScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return collectTo$suspendImpl(this, producerScope, continuation);
    }

    protected abstract java.lang.Object flowCollect(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public ChannelFlowOperator(kotlinx.coroutines.flow.Flow<? extends S> flow, kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        super(context, capacity, onBufferOverflow);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        this.flow = flow;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object collectWithContextUndispatched(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.CoroutineContext newContext, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlinx.coroutines.flow.FlowCollector originalContextCollector = kotlinx.coroutines.flow.internal.ChannelFlowKt.withUndispatchedContextCollector(flowCollector, continuation.get$context());
        return kotlinx.coroutines.flow.internal.ChannelFlowKt.withContextUndispatched$default(newContext, originalContextCollector, null, new kotlinx.coroutines.flow.internal.ChannelFlowOperator.AnonymousClass2(this, null), continuation, 4, null);
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.internal.ChannelFlowOperator$collectWithContextUndispatched$2, reason: invalid class name */
    /* JADX INFO: compiled from: ChannelFlow.kt */
    @kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0005H\u008a@"}, d2 = {"<anonymous>", "", "S", "T", "it", "Lkotlinx/coroutines/flow/FlowCollector;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.internal.ChannelFlowOperator$collectWithContextUndispatched$2", f = "ChannelFlow.kt", i = {}, l = {152}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass2 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.FlowCollector<? super T>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        /* synthetic */ java.lang.Object L$0;
        int label;
        final /* synthetic */ kotlinx.coroutines.flow.internal.ChannelFlowOperator<S, T> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(kotlinx.coroutines.flow.internal.ChannelFlowOperator<S, T> channelFlowOperator, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.internal.ChannelFlowOperator.AnonymousClass2> continuation) {
            super(2, continuation);
            this.this$0 = channelFlowOperator;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlinx.coroutines.flow.internal.ChannelFlowOperator.AnonymousClass2 anonymousClass2 = new kotlinx.coroutines.flow.internal.ChannelFlowOperator.AnonymousClass2(this.this$0, continuation);
            anonymousClass2.L$0 = obj;
            return anonymousClass2;
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.flow.internal.ChannelFlowOperator.AnonymousClass2) create(flowCollector, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
                    this.label = 1;
                    if (this.this$0.flowCollect(flowCollector, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    kotlin.ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return kotlin.Unit.INSTANCE;
        }
    }

    static /* synthetic */ <S, T> java.lang.Object collectTo$suspendImpl(kotlinx.coroutines.flow.internal.ChannelFlowOperator<S, T> channelFlowOperator, kotlinx.coroutines.channels.ProducerScope<? super T> producerScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        java.lang.Object objFlowCollect = channelFlowOperator.flowCollect(new kotlinx.coroutines.flow.internal.SendingCollector(producerScope), continuation);
        return objFlowCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objFlowCollect : kotlin.Unit.INSTANCE;
    }

    static /* synthetic */ <S, T> java.lang.Object collect$suspendImpl(kotlinx.coroutines.flow.internal.ChannelFlowOperator<S, T> channelFlowOperator, kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        if (channelFlowOperator.capacity == -3) {
            kotlin.coroutines.CoroutineContext collectContext = continuation.get$context();
            kotlin.coroutines.CoroutineContext newContext = kotlinx.coroutines.CoroutineContextKt.newCoroutineContext(collectContext, channelFlowOperator.context);
            if (kotlin.jvm.internal.Intrinsics.areEqual(newContext, collectContext)) {
                java.lang.Object objFlowCollect = channelFlowOperator.flowCollect(flowCollector, continuation);
                return objFlowCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objFlowCollect : kotlin.Unit.INSTANCE;
            }
            if (kotlin.jvm.internal.Intrinsics.areEqual(newContext.get(kotlin.coroutines.ContinuationInterceptor.INSTANCE), collectContext.get(kotlin.coroutines.ContinuationInterceptor.INSTANCE))) {
                java.lang.Object objCollectWithContextUndispatched = channelFlowOperator.collectWithContextUndispatched(flowCollector, newContext, continuation);
                return objCollectWithContextUndispatched == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollectWithContextUndispatched : kotlin.Unit.INSTANCE;
            }
        }
        java.lang.Object objCollect = super.collect(flowCollector, continuation);
        return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow
    public java.lang.String toString() {
        return this.flow + " -> " + super.toString();
    }
}
