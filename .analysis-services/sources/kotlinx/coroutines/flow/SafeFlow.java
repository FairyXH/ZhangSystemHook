package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Builders.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B4\u0012-\u0010\u0003\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0004¢\u0006\u0002\b\t¢\u0006\u0002\u0010\nJ\u001c\u0010\f\u001a\u00020\u00072\f\u0010\r\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0096@¢\u0006\u0002\u0010\u000eR7\u0010\u0003\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0004¢\u0006\u0002\b\tX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u000b¨\u0006\u000f"}, d2 = {"Lkotlinx/coroutines/flow/SafeFlow;", "T", "Lkotlinx/coroutines/flow/AbstractFlow;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/flow/FlowCollector;", "Lkotlin/coroutines/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function2;)V", "Lkotlin/jvm/functions/Function2;", "collectSafely", "collector", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class SafeFlow<T> extends kotlinx.coroutines.flow.AbstractFlow<T> {
    private final kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.FlowCollector<? super T>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> block;

    /* JADX WARN: Multi-variable type inference failed */
    public SafeFlow(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        this.block = block;
    }

    @Override // kotlinx.coroutines.flow.AbstractFlow
    public java.lang.Object collectSafely(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        java.lang.Object objInvoke = this.block.invoke(flowCollector, continuation);
        return objInvoke == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objInvoke : kotlin.Unit.INSTANCE;
    }
}
