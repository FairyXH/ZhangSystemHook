package kotlinx.coroutines;

/* JADX INFO: compiled from: Runnable.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\u001a \u0010\u0000\u001a\u00060\u0001j\u0002`\u00022\u000e\b\u0004\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0086\bø\u0001\u0000*\n\u0010\u0000\"\u00020\u00012\u00020\u0001\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0006"}, d2 = {"Runnable", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "block", "Lkotlin/Function0;", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class RunnableKt {
    public static final java.lang.Runnable Runnable(final kotlin.jvm.functions.Function0<kotlin.Unit> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        return new java.lang.Runnable() { // from class: kotlinx.coroutines.RunnableKt.Runnable.1
            @Override // java.lang.Runnable
            public final void run() {
                block.invoke();
            }
        };
    }
}
