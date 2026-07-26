package kotlinx.coroutines.debug.internal;

/* JADX INFO: compiled from: Comparisons.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u000e\u0010\u0003\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u00022\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"<anonymous>", "", "T", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I", "kotlin/comparisons/ComparisonsKt__ComparisonsKt$compareBy$2"}, k = 3, mv = {1, 9, 0}, xi = 48)
public final class DebugProbesImpl$dumpCoroutinesInfoImpl$$inlined$sortedBy$1<T> implements java.util.Comparator {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Comparator
    public final int compare(T t, T t2) {
        kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner it = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) t;
        kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner it2 = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) t2;
        return kotlin.comparisons.ComparisonsKt.compareValues(java.lang.Long.valueOf(it.info.sequenceNumber), java.lang.Long.valueOf(it2.info.sequenceNumber));
    }
}
