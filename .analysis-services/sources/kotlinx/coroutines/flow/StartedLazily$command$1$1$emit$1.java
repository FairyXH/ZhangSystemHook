package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: SharingStarted.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.StartedLazily$command$1$1", f = "SharingStarted.kt", i = {}, l = {158}, m = "emit", n = {}, s = {})
final class StartedLazily$command$1$1$emit$1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    int label;
    /* synthetic */ java.lang.Object result;
    final /* synthetic */ kotlinx.coroutines.flow.StartedLazily.AnonymousClass1.C00211<T> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    StartedLazily$command$1$1$emit$1(kotlinx.coroutines.flow.StartedLazily.AnonymousClass1.C00211<? super T> c00211, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.StartedLazily$command$1$1$emit$1> continuation) {
        super(continuation);
        this.this$0 = c00211;
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.emit(0, this);
    }
}
