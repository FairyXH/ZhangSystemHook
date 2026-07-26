package kotlinx.coroutines;

/* JADX INFO: compiled from: CancellableContinuationImpl.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u0019\u0010\u000e\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u0001H\u0082\b\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u0010\u0010\u0005\u001a\u00020\u00068\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u0019\u0010\t\u001a\u00020\u0001*\u00020\u00018Â\u0002X\u0082\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b\"\u0019\u0010\f\u001a\u00020\u0001*\u00020\u00018Â\u0002X\u0082\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000b¨\u0006\u000f"}, d2 = {"DECISION_SHIFT", "", "INDEX_MASK", "NO_INDEX", "RESUMED", "RESUME_TOKEN", "Lkotlinx/coroutines/internal/Symbol;", kotlinx.coroutines.debug.internal.DebugCoroutineInfoImplKt.SUSPENDED, "UNDECIDED", "decision", "getDecision", "(I)I", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "getIndex", "decisionAndIndex", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class CancellableContinuationImplKt {
    private static final int DECISION_SHIFT = 29;
    private static final int INDEX_MASK = 536870911;
    private static final int NO_INDEX = 536870911;
    private static final int RESUMED = 2;
    public static final kotlinx.coroutines.internal.Symbol RESUME_TOKEN = new kotlinx.coroutines.internal.Symbol("RESUME_TOKEN");
    private static final int SUSPENDED = 1;
    private static final int UNDECIDED = 0;

    private static final int getDecision(int $this$decision) {
        return $this$decision >> 29;
    }

    private static final int getIndex(int $this$index) {
        return 536870911 & $this$index;
    }

    private static final int decisionAndIndex(int decision, int index) {
        return (decision << 29) + index;
    }
}
