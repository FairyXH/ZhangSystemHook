package kotlinx.coroutines.flow.internal;

/* JADX INFO: Add missing generic type declarations: [T] */
/* JADX INFO: compiled from: ChannelFlow.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H\u008a@"}, d2 = {"<anonymous>", "", "T", "it"}, k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.internal.UndispatchedContextCollector$emitRef$1", f = "ChannelFlow.kt", i = {}, l = {212}, m = "invokeSuspend", n = {}, s = {})
final class UndispatchedContextCollector$emitRef$1<T> extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<T, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
    final /* synthetic */ kotlinx.coroutines.flow.FlowCollector<T> $downstream;
    /* synthetic */ java.lang.Object L$0;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    UndispatchedContextCollector$emitRef$1(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.internal.UndispatchedContextCollector$emitRef$1> continuation) {
        super(2, continuation);
        this.$downstream = flowCollector;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
        kotlinx.coroutines.flow.internal.UndispatchedContextCollector$emitRef$1 undispatchedContextCollector$emitRef$1 = new kotlinx.coroutines.flow.internal.UndispatchedContextCollector$emitRef$1(this.$downstream, continuation);
        undispatchedContextCollector$emitRef$1.L$0 = obj;
        return undispatchedContextCollector$emitRef$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final java.lang.Object invoke(T t, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return ((kotlinx.coroutines.flow.internal.UndispatchedContextCollector$emitRef$1) create(t, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
        java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure(obj);
                java.lang.Object obj2 = this.L$0;
                kotlinx.coroutines.flow.FlowCollector<T> flowCollector = this.$downstream;
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
