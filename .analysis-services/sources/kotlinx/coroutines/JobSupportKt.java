package kotlinx.coroutines;

/* JADX INFO: compiled from: JobSupport.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a\u0010\u0010\r\u001a\u0004\u0018\u00010\u000e*\u0004\u0018\u00010\u000eH\u0000\u001a\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u000e*\u0004\u0018\u00010\u000eH\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u0003\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u000b\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\f\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"COMPLETING_ALREADY", "Lkotlinx/coroutines/internal/Symbol;", "COMPLETING_RETRY", "COMPLETING_WAITING_CHILDREN", "EMPTY_ACTIVE", "Lkotlinx/coroutines/Empty;", "EMPTY_NEW", "FALSE", "", "RETRY", "SEALED", "TOO_LATE_TO_CANCEL", "TRUE", "boxIncomplete", "", "unboxState", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class JobSupportKt {
    private static final int FALSE = 0;
    private static final int RETRY = -1;
    private static final int TRUE = 1;
    private static final kotlinx.coroutines.internal.Symbol COMPLETING_ALREADY = new kotlinx.coroutines.internal.Symbol("COMPLETING_ALREADY");
    public static final kotlinx.coroutines.internal.Symbol COMPLETING_WAITING_CHILDREN = new kotlinx.coroutines.internal.Symbol("COMPLETING_WAITING_CHILDREN");
    private static final kotlinx.coroutines.internal.Symbol COMPLETING_RETRY = new kotlinx.coroutines.internal.Symbol("COMPLETING_RETRY");
    private static final kotlinx.coroutines.internal.Symbol TOO_LATE_TO_CANCEL = new kotlinx.coroutines.internal.Symbol("TOO_LATE_TO_CANCEL");
    private static final kotlinx.coroutines.internal.Symbol SEALED = new kotlinx.coroutines.internal.Symbol("SEALED");
    private static final kotlinx.coroutines.Empty EMPTY_NEW = new kotlinx.coroutines.Empty(false);
    private static final kotlinx.coroutines.Empty EMPTY_ACTIVE = new kotlinx.coroutines.Empty(true);

    public static final java.lang.Object boxIncomplete(java.lang.Object $this$boxIncomplete) {
        return $this$boxIncomplete instanceof kotlinx.coroutines.Incomplete ? new kotlinx.coroutines.IncompleteStateBox((kotlinx.coroutines.Incomplete) $this$boxIncomplete) : $this$boxIncomplete;
    }

    public static final java.lang.Object unboxState(java.lang.Object $this$unboxState) {
        kotlinx.coroutines.Incomplete incomplete;
        kotlinx.coroutines.IncompleteStateBox incompleteStateBox = $this$unboxState instanceof kotlinx.coroutines.IncompleteStateBox ? (kotlinx.coroutines.IncompleteStateBox) $this$unboxState : null;
        return (incompleteStateBox == null || (incomplete = incompleteStateBox.state) == null) ? $this$unboxState : incomplete;
    }
}
