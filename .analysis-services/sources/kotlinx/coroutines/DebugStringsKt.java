package kotlinx.coroutines;

/* JADX INFO: compiled from: DebugStrings.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0007\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\bH\u0000\"\u0018\u0010\u0000\u001a\u00020\u0001*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0018\u0010\u0005\u001a\u00020\u0001*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004¨\u0006\t"}, d2 = {"classSimpleName", "", "", "getClassSimpleName", "(Ljava/lang/Object;)Ljava/lang/String;", "hexAddress", "getHexAddress", "toDebugString", "Lkotlin/coroutines/Continuation;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class DebugStringsKt {
    public static final java.lang.String getHexAddress(java.lang.Object $this$hexAddress) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexAddress, "<this>");
        java.lang.String hexString = java.lang.Integer.toHexString(java.lang.System.identityHashCode($this$hexAddress));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(hexString, "toHexString(...)");
        return hexString;
    }

    public static final java.lang.String toDebugString(kotlin.coroutines.Continuation<?> continuation) {
        java.lang.Object objM11307constructorimpl;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "<this>");
        if (continuation instanceof kotlinx.coroutines.internal.DispatchedContinuation) {
            return continuation.toString();
        }
        try {
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(continuation + "@" + getHexAddress(continuation));
        } catch (java.lang.Throwable th) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(th));
        }
        if (kotlin.Result.m11310exceptionOrNullimpl(objM11307constructorimpl) != null) {
            objM11307constructorimpl = continuation.getClass().getName() + "@" + getHexAddress(continuation);
        }
        return (java.lang.String) objM11307constructorimpl;
    }

    public static final java.lang.String getClassSimpleName(java.lang.Object $this$classSimpleName) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$classSimpleName, "<this>");
        java.lang.String simpleName = $this$classSimpleName.getClass().getSimpleName();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(simpleName, "getSimpleName(...)");
        return simpleName;
    }
}
