package kotlinx.coroutines;

/* JADX INFO: compiled from: ThreadContextElement.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\u001a+\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u0002H\u0002¢\u0006\u0002\u0010\u0005\u001a\u0016\u0010\u0006\u001a\u00020\u0007*\u0006\u0012\u0002\b\u00030\u0003H\u0086H¢\u0006\u0002\u0010\b\u001a\u0016\u0010\t\u001a\u00020\n*\u0006\u0012\u0002\b\u00030\u0003H\u0086H¢\u0006\u0002\u0010\b¨\u0006\u000b"}, d2 = {"asContextElement", "Lkotlinx/coroutines/ThreadContextElement;", "T", "Ljava/lang/ThreadLocal;", "value", "(Ljava/lang/ThreadLocal;Ljava/lang/Object;)Lkotlinx/coroutines/ThreadContextElement;", "ensurePresent", "", "(Ljava/lang/ThreadLocal;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isPresent", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ThreadContextElementKt {
    public static /* synthetic */ kotlinx.coroutines.ThreadContextElement asContextElement$default(java.lang.ThreadLocal threadLocal, java.lang.Object obj, int i, java.lang.Object obj2) {
        if ((i & 1) != 0) {
            obj = threadLocal.get();
        }
        return asContextElement(threadLocal, obj);
    }

    public static final <T> kotlinx.coroutines.ThreadContextElement<T> asContextElement(java.lang.ThreadLocal<T> threadLocal, T t) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(threadLocal, "<this>");
        return new kotlinx.coroutines.internal.ThreadLocal(t, threadLocal);
    }

    public static final java.lang.Object isPresent(java.lang.ThreadLocal<?> threadLocal, kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
        return kotlin.coroutines.jvm.internal.Boxing.boxBoolean(continuation.getContext().get(new kotlinx.coroutines.internal.ThreadLocalKey(threadLocal)) != null);
    }

    private static final java.lang.Object isPresent$$forInline(java.lang.ThreadLocal<?> threadLocal, kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
        kotlin.jvm.internal.InlineMarker.mark(3);
        kotlin.coroutines.Continuation continuation2 = null;
        continuation2.getContext();
        throw null;
    }

    public static final java.lang.Object ensurePresent(java.lang.ThreadLocal<?> threadLocal, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        if (continuation.getContext().get(new kotlinx.coroutines.internal.ThreadLocalKey(threadLocal)) != null) {
            return kotlin.Unit.INSTANCE;
        }
        throw new java.lang.IllegalStateException(("ThreadLocal " + threadLocal + " is missing from context " + continuation.getContext()).toString());
    }

    private static final java.lang.Object ensurePresent$$forInline(java.lang.ThreadLocal<?> threadLocal, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlin.jvm.internal.InlineMarker.mark(3);
        kotlin.coroutines.Continuation continuation2 = null;
        continuation2.getContext();
        throw null;
    }
}
