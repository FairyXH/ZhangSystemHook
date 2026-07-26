package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: FlowCoroutine.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a?\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012)\b\u0001\u0010\u0002\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0003¢\u0006\u0002\b\u0007H\u0080@¢\u0006\u0002\u0010\b\u001aP\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00010\n\"\u0004\b\u0000\u0010\u000125\b\u0001\u0010\u0002\u001a/\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u000b¢\u0006\u0002\b\u0007H\u0000¢\u0006\u0002\u0010\u000e¨\u0006\u000f"}, d2 = {"flowScope", "R", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/CoroutineScope;", "Lkotlin/coroutines/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "scopedFlow", "Lkotlinx/coroutines/flow/Flow;", "Lkotlin/Function3;", "Lkotlinx/coroutines/flow/FlowCollector;", "", "(Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class FlowCoroutineKt {
    public static final <R> java.lang.Object flowScope(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super R> continuation) {
        kotlinx.coroutines.flow.internal.FlowCoroutine coroutine = new kotlinx.coroutines.flow.internal.FlowCoroutine(continuation.getContext(), continuation);
        java.lang.Object objStartUndispatchedOrReturn = kotlinx.coroutines.intrinsics.UndispatchedKt.startUndispatchedOrReturn(coroutine, coroutine, function2);
        if (objStartUndispatchedOrReturn == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return objStartUndispatchedOrReturn;
    }

    public static final <R> kotlinx.coroutines.flow.Flow<R> scopedFlow(final kotlin.jvm.functions.Function3<? super kotlinx.coroutines.CoroutineScope, ? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        return new kotlinx.coroutines.flow.Flow<R>() { // from class: kotlinx.coroutines.flow.internal.FlowCoroutineKt$scopedFlow$$inlined$unsafeFlow$1
            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                java.lang.Object objFlowScope = kotlinx.coroutines.flow.internal.FlowCoroutineKt.flowScope(new kotlinx.coroutines.flow.internal.FlowCoroutineKt$scopedFlow$1$1(block, flowCollector, null), continuation);
                return objFlowScope == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objFlowScope : kotlin.Unit.INSTANCE;
            }
        };
    }
}
