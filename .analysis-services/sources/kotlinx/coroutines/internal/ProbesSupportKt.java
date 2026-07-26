package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: ProbesSupport.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0080\b¨\u0006\u0004"}, d2 = {"probeCoroutineCreated", "Lkotlin/coroutines/Continuation;", "T", "completion", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ProbesSupportKt {
    public static final <T> kotlin.coroutines.Continuation<T> probeCoroutineCreated(kotlin.coroutines.Continuation<? super T> completion) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        return kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineCreated(completion);
    }
}
