package kotlinx.coroutines;

/* JADX INFO: compiled from: CoroutineExceptionHandler.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\u001a(\u0010\u0000\u001a\u00020\u00012\u001a\b\u0004\u0010\u0002\u001a\u0014\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0003H\u0086\bø\u0001\u0000\u001a\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0005H\u0007\u001a\u0018\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005H\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\r"}, d2 = {"CoroutineExceptionHandler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "handler", "Lkotlin/Function2;", "Lkotlin/coroutines/CoroutineContext;", "", "", "handleCoroutineException", "context", "exception", "handlerException", "originalException", "thrownException", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class CoroutineExceptionHandlerKt {
    public static final void handleCoroutineException(kotlin.coroutines.CoroutineContext context, java.lang.Throwable exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        try {
            kotlinx.coroutines.CoroutineExceptionHandler it = (kotlinx.coroutines.CoroutineExceptionHandler) context.get(kotlinx.coroutines.CoroutineExceptionHandler.INSTANCE);
            if (it != null) {
                it.handleException(context, exception);
            } else {
                kotlinx.coroutines.internal.CoroutineExceptionHandlerImpl_commonKt.handleUncaughtCoroutineException(context, exception);
            }
        } catch (java.lang.Throwable t) {
            kotlinx.coroutines.internal.CoroutineExceptionHandlerImpl_commonKt.handleUncaughtCoroutineException(context, handlerException(exception, t));
        }
    }

    public static final java.lang.Throwable handlerException(java.lang.Throwable originalException, java.lang.Throwable thrownException) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(originalException, "originalException");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(thrownException, "thrownException");
        if (originalException == thrownException) {
            return originalException;
        }
        java.lang.Throwable $this$handlerException_u24lambda_u241 = new java.lang.RuntimeException("Exception while trying to handle coroutine exception", thrownException);
        java.lang.Throwable $this$addSuppressedThrowable$iv = $this$handlerException_u24lambda_u241;
        kotlin.ExceptionsKt.addSuppressed($this$addSuppressedThrowable$iv, originalException);
        return $this$handlerException_u24lambda_u241;
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.CoroutineExceptionHandlerKt$CoroutineExceptionHandler$1, reason: invalid class name */
    /* JADX INFO: compiled from: CoroutineExceptionHandler.kt */
    @kotlin.Metadata(d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u00012\u00020\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, d2 = {"kotlinx/coroutines/CoroutineExceptionHandlerKt$CoroutineExceptionHandler$1", "Lkotlin/coroutines/AbstractCoroutineContextElement;", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "handleException", "", "context", "Lkotlin/coroutines/CoroutineContext;", "exception", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 176)
    public static final class AnonymousClass1 extends kotlin.coroutines.AbstractCoroutineContextElement implements kotlinx.coroutines.CoroutineExceptionHandler {
        final /* synthetic */ kotlin.jvm.functions.Function2<kotlin.coroutines.CoroutineContext, java.lang.Throwable, kotlin.Unit> $handler;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass1(kotlin.jvm.functions.Function2<? super kotlin.coroutines.CoroutineContext, ? super java.lang.Throwable, kotlin.Unit> function2, kotlinx.coroutines.CoroutineExceptionHandler.Companion $super_call_param$1) {
            super($super_call_param$1);
            this.$handler = function2;
        }

        @Override // kotlinx.coroutines.CoroutineExceptionHandler
        public void handleException(kotlin.coroutines.CoroutineContext context, java.lang.Throwable exception) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
            this.$handler.invoke(context, exception);
        }
    }

    public static final kotlinx.coroutines.CoroutineExceptionHandler CoroutineExceptionHandler(kotlin.jvm.functions.Function2<? super kotlin.coroutines.CoroutineContext, ? super java.lang.Throwable, kotlin.Unit> handler) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(handler, "handler");
        return new kotlinx.coroutines.CoroutineExceptionHandlerKt.AnonymousClass1(handler, kotlinx.coroutines.CoroutineExceptionHandler.INSTANCE);
    }
}
