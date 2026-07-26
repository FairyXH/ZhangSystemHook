package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Reduce.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 176)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ReduceKt$fold$2", f = "Reduce.kt", i = {}, l = {45}, m = "emit", n = {}, s = {})
public final class FlowKt__ReduceKt$fold$2$emit$1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    java.lang.Object L$0;
    int label;
    /* synthetic */ java.lang.Object result;
    final /* synthetic */ kotlinx.coroutines.flow.FlowKt__ReduceKt.AnonymousClass2<T> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public FlowKt__ReduceKt$fold$2$emit$1(kotlinx.coroutines.flow.FlowKt__ReduceKt.AnonymousClass2<? super T> anonymousClass2, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__ReduceKt$fold$2$emit$1> continuation) {
        super(continuation);
        this.this$0 = anonymousClass2;
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.emit(null, this);
    }
}
