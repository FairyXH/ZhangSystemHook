package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Limit.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 176)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__LimitKt$collectWhile$collector$1", f = "Limit.kt", i = {0}, l = {130}, m = "emit", n = {"this"}, s = {"L$0"})
public final class FlowKt__LimitKt$collectWhile$collector$1$emit$1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    java.lang.Object L$0;
    int label;
    /* synthetic */ java.lang.Object result;
    final /* synthetic */ kotlinx.coroutines.flow.FlowKt__LimitKt$collectWhile$collector$1<T> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__LimitKt$collectWhile$collector$1$emit$1(kotlinx.coroutines.flow.FlowKt__LimitKt$collectWhile$collector$1<T> flowKt__LimitKt$collectWhile$collector$1, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__LimitKt$collectWhile$collector$1$emit$1> continuation) {
        super(continuation);
        this.this$0 = flowKt__LimitKt$collectWhile$collector$1;
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.emit(null, this);
    }
}
