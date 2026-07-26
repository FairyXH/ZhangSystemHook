package kotlinx.coroutines;

/* JADX INFO: compiled from: Delay.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\ba\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007\u0082\u0002\u0007\n\u0005\b¡\u001e0\u0001¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/DelayWithTimeoutDiagnostics;", "Lkotlinx/coroutines/Delay;", "timeoutMessage", "", "timeout", "Lkotlin/time/Duration;", "timeoutMessage-LRDsOJo", "(J)Ljava/lang/String;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface DelayWithTimeoutDiagnostics extends kotlinx.coroutines.Delay {
    /* JADX INFO: renamed from: timeoutMessage-LRDsOJo, reason: not valid java name */
    java.lang.String m12800timeoutMessageLRDsOJo(long timeout);

    /* JADX INFO: compiled from: Delay.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Deprecated without replacement as an internal method never intended for public use")
        public static java.lang.Object delay(kotlinx.coroutines.DelayWithTimeoutDiagnostics $this, long time, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            java.lang.Object objDelay = kotlinx.coroutines.Delay.DefaultImpls.delay($this, time, continuation);
            return objDelay == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objDelay : kotlin.Unit.INSTANCE;
        }

        public static kotlinx.coroutines.DisposableHandle invokeOnTimeout(kotlinx.coroutines.DelayWithTimeoutDiagnostics $this, long timeMillis, java.lang.Runnable block, kotlin.coroutines.CoroutineContext context) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
            return kotlinx.coroutines.Delay.DefaultImpls.invokeOnTimeout($this, timeMillis, block, context);
        }
    }
}
