package kotlin;

/* JADX INFO: compiled from: DeepRecursive.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a2\u0010\u0004\u001a\u0002H\u0005\"\u0004\b\u0000\u0010\u0006\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0006\u0012\u0004\u0012\u0002H\u00050\u00072\u0006\u0010\b\u001a\u0002H\u0006H\u0087\u0002¢\u0006\u0002\u0010\t\"\u0016\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0003*r\b\u0002\u0010\n\"5\b\u0001\u0012\f\u0012\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\f\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000b¢\u0006\u0002\b\u000e25\b\u0001\u0012\f\u0012\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\f\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000b¢\u0006\u0002\b\u000e¨\u0006\u000f"}, d2 = {"UNDEFINED_RESULT", "Lkotlin/Result;", "", "Ljava/lang/Object;", "invoke", "R", "T", "Lkotlin/DeepRecursiveFunction;", "value", "(Lkotlin/DeepRecursiveFunction;Ljava/lang/Object;)Ljava/lang/Object;", "DeepRecursiveFunctionBlock", "Lkotlin/Function3;", "Lkotlin/DeepRecursiveScope;", "Lkotlin/coroutines/Continuation;", "Lkotlin/ExtensionFunctionType;", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class DeepRecursiveKt {
    private static final java.lang.Object UNDEFINED_RESULT;

    public static final <T, R> R invoke(kotlin.DeepRecursiveFunction<T, R> deepRecursiveFunction, T t) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(deepRecursiveFunction, "<this>");
        return (R) new kotlin.DeepRecursiveScopeImpl(deepRecursiveFunction.getBlock$kotlin_stdlib(), t).runCallLoop();
    }

    static {
        kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
        UNDEFINED_RESULT = kotlin.Result.m11307constructorimpl(kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED());
    }
}
