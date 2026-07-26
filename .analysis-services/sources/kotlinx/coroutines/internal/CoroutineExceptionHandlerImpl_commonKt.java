package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: CoroutineExceptionHandlerImpl.common.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0000¨\u0006\u0006"}, d2 = {"handleUncaughtCoroutineException", "", "context", "Lkotlin/coroutines/CoroutineContext;", "exception", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class CoroutineExceptionHandlerImpl_commonKt {
    public static final void handleUncaughtCoroutineException(kotlin.coroutines.CoroutineContext context, java.lang.Throwable exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        for (kotlinx.coroutines.CoroutineExceptionHandler handler : kotlinx.coroutines.internal.CoroutineExceptionHandlerImplKt.getPlatformExceptionHandlers()) {
            try {
                handler.handleException(context, exception);
            } catch (kotlinx.coroutines.internal.ExceptionSuccessfullyProcessed e) {
                return;
            } catch (java.lang.Throwable t) {
                kotlinx.coroutines.internal.CoroutineExceptionHandlerImplKt.propagateExceptionFinalResort(kotlinx.coroutines.CoroutineExceptionHandlerKt.handlerException(exception, t));
            }
        }
        try {
            kotlin.ExceptionsKt.addSuppressed(exception, new kotlinx.coroutines.internal.DiagnosticCoroutineContextException(context));
        } catch (java.lang.Throwable th) {
        }
        kotlinx.coroutines.internal.CoroutineExceptionHandlerImplKt.propagateExceptionFinalResort(exception);
    }
}
