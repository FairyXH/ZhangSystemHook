package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: ThreadLocal.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a&\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0004\u001a\u00020\u0005H\u0000*\u001e\b\u0000\u0010\u0006\u001a\u0004\b\u0000\u0010\u0002\"\b\u0012\u0004\u0012\u0002H\u00020\u00012\b\u0012\u0004\u0012\u0002H\u00020\u0001¨\u0006\u0007"}, d2 = {"commonThreadLocal", "Ljava/lang/ThreadLocal;", "T", "Lkotlinx/coroutines/internal/CommonThreadLocal;", "name", "Lkotlinx/coroutines/internal/Symbol;", "CommonThreadLocal", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ThreadLocalKt {
    public static /* synthetic */ void CommonThreadLocal$annotations() {
    }

    public static final <T> java.lang.ThreadLocal<T> commonThreadLocal(kotlinx.coroutines.internal.Symbol name) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
        return new java.lang.ThreadLocal<>();
    }
}
