package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: LocalAtomics.common.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\".\u0010\u0000\u001a\u00020\u0001*\u00060\u0002j\u0002`\u00032\u0006\u0010\u0000\u001a\u00020\u00018À\u0002@À\u0002X\u0080\u000e¢\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"value", "", "Ljava/util/concurrent/atomic/AtomicInteger;", "Lkotlinx/coroutines/internal/LocalAtomicInt;", "getValue", "(Ljava/util/concurrent/atomic/AtomicInteger;)I", "setValue", "(Ljava/util/concurrent/atomic/AtomicInteger;I)V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class LocalAtomics_commonKt {
    public static final int getValue(java.util.concurrent.atomic.AtomicInteger $this$value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$value, "<this>");
        return $this$value.get();
    }

    public static final void setValue(java.util.concurrent.atomic.AtomicInteger $this$value, int value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$value, "<this>");
        $this$value.set(value);
    }
}
