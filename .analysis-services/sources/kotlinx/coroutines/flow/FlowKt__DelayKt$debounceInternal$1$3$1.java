package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Delay.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002H\u008a@"}, d2 = {"<anonymous>", "", "T"}, k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1$3$1", f = "Delay.kt", i = {}, l = {232}, m = "invokeSuspend", n = {}, s = {})
final class FlowKt__DelayKt$debounceInternal$1$3$1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
    final /* synthetic */ kotlinx.coroutines.flow.FlowCollector<T> $downstream;
    final /* synthetic */ kotlin.jvm.internal.Ref.ObjectRef<java.lang.Object> $lastValue;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    FlowKt__DelayKt$debounceInternal$1$3$1(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.jvm.internal.Ref.ObjectRef<java.lang.Object> objectRef, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1$3$1> continuation) {
        super(1, continuation);
        this.$downstream = flowCollector;
        this.$lastValue = objectRef;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final kotlin.coroutines.Continuation<kotlin.Unit> create(kotlin.coroutines.Continuation<?> continuation) {
        return new kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1$3$1(this.$downstream, this.$lastValue, continuation);
    }

    @Override // kotlin.jvm.functions.Function1
    public final java.lang.Object invoke(kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return ((kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1$3$1) create(continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
        kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1$3$1 flowKt__DelayKt$debounceInternal$1$3$1;
        java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure(obj);
                kotlinx.coroutines.flow.FlowCollector<T> flowCollector = this.$downstream;
                kotlinx.coroutines.internal.Symbol symbol = kotlinx.coroutines.flow.internal.NullSurrogateKt.NULL;
                java.lang.Object obj2 = this.$lastValue.element;
                if (obj2 == symbol) {
                    obj2 = null;
                }
                this.label = 1;
                if (flowCollector.emit((T) obj2, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                flowKt__DelayKt$debounceInternal$1$3$1 = this;
                break;
                break;
            case 1:
                flowKt__DelayKt$debounceInternal$1$3$1 = this;
                kotlin.ResultKt.throwOnFailure(obj);
                break;
            default:
                throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        flowKt__DelayKt$debounceInternal$1$3$1.$lastValue.element = null;
        return kotlin.Unit.INSTANCE;
    }
}
