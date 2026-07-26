package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: Combine.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1$1", f = "Combine.kt", i = {}, l = {32, 33}, m = "emit", n = {}, s = {})
final class CombineKt$combineInternal$2$1$1$emit$1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    int label;
    /* synthetic */ java.lang.Object result;
    final /* synthetic */ kotlinx.coroutines.flow.internal.CombineKt.AnonymousClass2.AnonymousClass1.C00221<T> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    CombineKt$combineInternal$2$1$1$emit$1(kotlinx.coroutines.flow.internal.CombineKt.AnonymousClass2.AnonymousClass1.C00221<? super T> c00221, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1$1$emit$1> continuation) {
        super(continuation);
        this.this$0 = c00221;
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.emit(null, this);
    }
}
