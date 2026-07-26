package kotlinx.coroutines.debug.internal;

/* JADX INFO: compiled from: ConcurrentWeakMap.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\"\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\u0000\n\u0000\u001a\b\u0010\b\u001a\u00020\tH\u0002\u001a\u000e\u0010\n\u001a\u00020\u0003*\u0004\u0018\u00010\u000bH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"MAGIC", "", "MARKED_NULL", "Lkotlinx/coroutines/debug/internal/Marked;", "MARKED_TRUE", "MIN_CAPACITY", "REHASH", "Lkotlinx/coroutines/internal/Symbol;", "noImpl", "", "mark", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ConcurrentWeakMapKt {
    private static final int MAGIC = -1640531527;
    private static final int MIN_CAPACITY = 16;
    private static final kotlinx.coroutines.internal.Symbol REHASH = new kotlinx.coroutines.internal.Symbol("REHASH");
    private static final kotlinx.coroutines.debug.internal.Marked MARKED_NULL = new kotlinx.coroutines.debug.internal.Marked(null);
    private static final kotlinx.coroutines.debug.internal.Marked MARKED_TRUE = new kotlinx.coroutines.debug.internal.Marked(true);

    /* JADX INFO: Access modifiers changed from: private */
    public static final kotlinx.coroutines.debug.internal.Marked mark(java.lang.Object $this$mark) {
        return $this$mark == null ? MARKED_NULL : kotlin.jvm.internal.Intrinsics.areEqual($this$mark, (java.lang.Object) true) ? MARKED_TRUE : new kotlinx.coroutines.debug.internal.Marked($this$mark);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final java.lang.Void noImpl() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }
}
