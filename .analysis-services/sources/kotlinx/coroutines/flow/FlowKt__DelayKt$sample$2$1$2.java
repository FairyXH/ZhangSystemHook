package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Delay.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0001H\u008a@"}, d2 = {"<anonymous>", "", "T", "it"}, k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2$1$2", f = "Delay.kt", i = {}, l = {com.android.internal.util.FrameworkStatsLog.BLOB_LEASED}, m = "invokeSuspend", n = {}, s = {})
final class FlowKt__DelayKt$sample$2$1$2 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlin.Unit, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
    final /* synthetic */ kotlinx.coroutines.flow.FlowCollector<T> $downstream;
    final /* synthetic */ kotlin.jvm.internal.Ref.ObjectRef<java.lang.Object> $lastValue;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    FlowKt__DelayKt$sample$2$1$2(kotlin.jvm.internal.Ref.ObjectRef<java.lang.Object> objectRef, kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2$1$2> continuation) {
        super(2, continuation);
        this.$lastValue = objectRef;
        this.$downstream = flowCollector;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
        return new kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2$1$2(this.$lastValue, this.$downstream, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final java.lang.Object invoke(kotlin.Unit unit, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return ((kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2$1$2) create(unit, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
        java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure(obj);
                java.lang.Object obj2 = this.$lastValue.element;
                if (obj2 == null) {
                    return kotlin.Unit.INSTANCE;
                }
                this.$lastValue.element = null;
                kotlinx.coroutines.flow.FlowCollector<T> flowCollector = this.$downstream;
                if (obj2 == kotlinx.coroutines.flow.internal.NullSurrogateKt.NULL) {
                    obj2 = null;
                }
                this.label = 1;
                if (flowCollector.emit((T) obj2, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                kotlin.ResultKt.throwOnFailure(obj);
                break;
            default:
                throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return kotlin.Unit.INSTANCE;
    }
}
