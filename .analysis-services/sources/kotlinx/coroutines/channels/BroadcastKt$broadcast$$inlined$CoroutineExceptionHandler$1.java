package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: CoroutineExceptionHandler.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u00012\u00020\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t¸\u0006\u0000"}, d2 = {"kotlinx/coroutines/CoroutineExceptionHandlerKt$CoroutineExceptionHandler$1", "Lkotlin/coroutines/AbstractCoroutineContextElement;", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "handleException", "", "context", "Lkotlin/coroutines/CoroutineContext;", "exception", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class BroadcastKt$broadcast$$inlined$CoroutineExceptionHandler$1 extends kotlin.coroutines.AbstractCoroutineContextElement implements kotlinx.coroutines.CoroutineExceptionHandler {
    public BroadcastKt$broadcast$$inlined$CoroutineExceptionHandler$1(kotlinx.coroutines.CoroutineExceptionHandler.Companion $super_call_param$1) {
        super($super_call_param$1);
    }

    @Override // kotlinx.coroutines.CoroutineExceptionHandler
    public void handleException(kotlin.coroutines.CoroutineContext context, java.lang.Throwable exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
    }
}
