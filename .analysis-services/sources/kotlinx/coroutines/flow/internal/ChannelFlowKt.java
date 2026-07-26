package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: ChannelFlow.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001aX\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001\"\u0004\b\u0001\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u0002H\u00022\b\b\u0002\u0010\u0006\u001a\u00020\u00072\"\u0010\b\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\tH\u0080@¢\u0006\u0002\u0010\u000b\u001a\u001e\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00010\r\"\u0004\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u000eH\u0000\u001a&\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0010\"\u0004\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u0004H\u0002¨\u0006\u0012"}, d2 = {"withContextUndispatched", "T", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "newContext", "Lkotlin/coroutines/CoroutineContext;", "value", "countOrElement", "", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "asChannelFlow", "Lkotlinx/coroutines/flow/internal/ChannelFlow;", "Lkotlinx/coroutines/flow/Flow;", "withUndispatchedContextCollector", "Lkotlinx/coroutines/flow/FlowCollector;", "emitContext", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ChannelFlowKt {
    public static final <T> kotlinx.coroutines.flow.internal.ChannelFlow<T> asChannelFlow(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlinx.coroutines.flow.internal.ChannelFlow<T> channelFlow = flow instanceof kotlinx.coroutines.flow.internal.ChannelFlow ? (kotlinx.coroutines.flow.internal.ChannelFlow) flow : null;
        if (channelFlow == null) {
            return new kotlinx.coroutines.flow.internal.ChannelFlowOperatorImpl(flow, null, 0, null, 14, null);
        }
        return channelFlow;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> kotlinx.coroutines.flow.FlowCollector<T> withUndispatchedContextCollector(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.CoroutineContext emitContext) {
        return flowCollector instanceof kotlinx.coroutines.flow.internal.SendingCollector ? true : flowCollector instanceof kotlinx.coroutines.flow.internal.NopCollector ? flowCollector : new kotlinx.coroutines.flow.internal.UndispatchedContextCollector(flowCollector, emitContext);
    }

    public static /* synthetic */ java.lang.Object withContextUndispatched$default(kotlin.coroutines.CoroutineContext coroutineContext, java.lang.Object obj, java.lang.Object obj2, kotlin.jvm.functions.Function2 function2, kotlin.coroutines.Continuation continuation, int i, java.lang.Object obj3) {
        if ((i & 4) != 0) {
            obj2 = kotlinx.coroutines.internal.ThreadContextKt.threadContextElements(coroutineContext);
        }
        return withContextUndispatched(coroutineContext, obj, obj2, function2, continuation);
    }

    public static final <T, V> java.lang.Object withContextUndispatched(kotlin.coroutines.CoroutineContext newContext, V v, java.lang.Object countOrElement, kotlin.jvm.functions.Function2<? super V, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super T> continuation) {
        java.lang.Object oldValue$iv = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(newContext, countOrElement);
        try {
            kotlinx.coroutines.flow.internal.StackFrameContinuation stackFrameContinuation = new kotlinx.coroutines.flow.internal.StackFrameContinuation(continuation, newContext);
            java.lang.Object objWrapWithContinuationImpl = !(function2 instanceof kotlin.coroutines.jvm.internal.BaseContinuationImpl) ? kotlin.coroutines.intrinsics.IntrinsicsKt.wrapWithContinuationImpl(function2, v, stackFrameContinuation) : ((kotlin.jvm.functions.Function2) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(v, stackFrameContinuation);
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(newContext, oldValue$iv);
            if (objWrapWithContinuationImpl == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            return objWrapWithContinuationImpl;
        } catch (java.lang.Throwable th) {
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(newContext, oldValue$iv);
            throw th;
        }
    }
}
