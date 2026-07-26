package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: OnDemandAllocatingPool.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\u001a\u0017\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0082\b\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"IS_CLOSED_MASK", "", "loop", "", "block", "Lkotlin/Function0;", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class OnDemandAllocatingPoolKt {
    private static final int IS_CLOSED_MASK = Integer.MIN_VALUE;

    private static final java.lang.Void loop(kotlin.jvm.functions.Function0<kotlin.Unit> function0) {
        while (true) {
            function0.invoke();
        }
    }
}
