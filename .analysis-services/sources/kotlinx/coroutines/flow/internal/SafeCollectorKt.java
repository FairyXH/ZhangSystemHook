package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: SafeCollector.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001a\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\">\u0010\u0000\u001a,\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u0001X\u0082\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"emitFun", "Lkotlin/Function3;", "Lkotlinx/coroutines/flow/FlowCollector;", "", "Lkotlin/coroutines/Continuation;", "", "getEmitFun$annotations", "()V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class SafeCollectorKt {
    private static final kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<java.lang.Object>, java.lang.Object, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> emitFun;

    private static /* synthetic */ void getEmitFun$annotations() {
    }

    static {
        kotlinx.coroutines.flow.internal.SafeCollectorKt$emitFun$1 safeCollectorKt$emitFun$1 = kotlinx.coroutines.flow.internal.SafeCollectorKt$emitFun$1.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(safeCollectorKt$emitFun$1, "null cannot be cast to non-null type kotlin.Function3<kotlinx.coroutines.flow.FlowCollector<kotlin.Any?>, kotlin.Any?, kotlin.coroutines.Continuation<kotlin.Unit>, kotlin.Any?>");
        emitFun = (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(safeCollectorKt$emitFun$1, 3);
    }
}
