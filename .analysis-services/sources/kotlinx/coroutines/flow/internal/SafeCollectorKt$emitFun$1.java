package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: SafeCollector.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
/* synthetic */ class SafeCollectorKt$emitFun$1 extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super java.lang.Object>, java.lang.Object, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object>, kotlin.coroutines.jvm.internal.SuspendFunction {
    public static final kotlinx.coroutines.flow.internal.SafeCollectorKt$emitFun$1 INSTANCE = new kotlinx.coroutines.flow.internal.SafeCollectorKt$emitFun$1();

    SafeCollectorKt$emitFun$1() {
        super(3, kotlinx.coroutines.flow.FlowCollector.class, "emit", "emit(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", 0);
    }

    @Override // kotlin.jvm.functions.Function3
    public /* bridge */ /* synthetic */ java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super java.lang.Object> flowCollector, java.lang.Object p2, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return invoke2((kotlinx.coroutines.flow.FlowCollector<java.lang.Object>) flowCollector, p2, continuation);
    }

    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public final java.lang.Object invoke2(kotlinx.coroutines.flow.FlowCollector<java.lang.Object> flowCollector, java.lang.Object p1, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return flowCollector.emit(p1, continuation);
    }
}
