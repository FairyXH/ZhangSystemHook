package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: Synchronized.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a1\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0007*\u0010\b\u0007\u0010\b\"\u00020\u00032\u00020\u0003B\u0002\b\t\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\n"}, d2 = {"synchronizedImpl", "T", "lock", "", "Lkotlinx/coroutines/internal/SynchronizedObject;", "block", "Lkotlin/Function0;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "SynchronizedObject", "Lkotlinx/coroutines/InternalCoroutinesApi;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class SynchronizedKt {
    public static /* synthetic */ void SynchronizedObject$annotations() {
    }

    public static final <T> T synchronizedImpl(java.lang.Object lock, kotlin.jvm.functions.Function0<? extends T> block) {
        T tInvoke;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(lock, "lock");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        synchronized (lock) {
            try {
                tInvoke = block.invoke();
                kotlin.jvm.internal.InlineMarker.finallyStart(1);
            } catch (java.lang.Throwable th) {
                kotlin.jvm.internal.InlineMarker.finallyStart(1);
                kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                throw th;
            }
        }
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
        return tInvoke;
    }
}
