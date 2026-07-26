package kotlinx.coroutines;

/* JADX INFO: compiled from: DispatchedTask.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000<\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a \u0010\f\u001a\u00020\r\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\u000f2\u0006\u0010\u0010\u001a\u00020\u0001H\u0000\u001a.\u0010\u0011\u001a\u00020\r\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\u000f2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u00132\u0006\u0010\u0014\u001a\u00020\tH\u0000\u001a\u0010\u0010\u0015\u001a\u00020\r*\u0006\u0012\u0002\b\u00030\u000fH\u0002\u001a\u0019\u0010\u0016\u001a\u00020\r*\u0006\u0012\u0002\b\u00030\u00132\u0006\u0010\u0017\u001a\u00020\u0018H\u0080\b\u001a*\u0010\u0019\u001a\u00020\r*\u0006\u0012\u0002\b\u00030\u000f2\u0006\u0010\u001a\u001a\u00020\u001b2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\r0\u001dH\u0080\bø\u0001\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u0016\u0010\u0002\u001a\u00020\u00018\u0000X\u0081T¢\u0006\b\n\u0000\u0012\u0004\b\u0003\u0010\u0004\"\u000e\u0010\u0005\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u0018\u0010\b\u001a\u00020\t*\u00020\u00018@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\n\"\u0018\u0010\u000b\u001a\u00020\t*\u00020\u00018@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\n\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u001e"}, d2 = {"MODE_ATOMIC", "", "MODE_CANCELLABLE", "getMODE_CANCELLABLE$annotations", "()V", "MODE_CANCELLABLE_REUSABLE", "MODE_UNDISPATCHED", "MODE_UNINITIALIZED", "isCancellableMode", "", "(I)Z", "isReusableMode", "dispatch", "", "T", "Lkotlinx/coroutines/DispatchedTask;", com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY, "resume", "delegate", "Lkotlin/coroutines/Continuation;", "undispatched", "resumeUnconfined", "resumeWithStackTrace", "exception", "", "runUnconfinedEventLoop", "eventLoop", "Lkotlinx/coroutines/EventLoop;", "block", "Lkotlin/Function0;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class DispatchedTaskKt {
    public static final int MODE_ATOMIC = 0;
    public static final int MODE_CANCELLABLE = 1;
    public static final int MODE_CANCELLABLE_REUSABLE = 2;
    public static final int MODE_UNDISPATCHED = 4;
    public static final int MODE_UNINITIALIZED = -1;

    public static /* synthetic */ void getMODE_CANCELLABLE$annotations() {
    }

    public static final boolean isCancellableMode(int $this$isCancellableMode) {
        return $this$isCancellableMode == 1 || $this$isCancellableMode == 2;
    }

    public static final boolean isReusableMode(int $this$isReusableMode) {
        return $this$isReusableMode == 2;
    }

    public static final <T> void dispatch(kotlinx.coroutines.DispatchedTask<? super T> dispatchedTask, int mode) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(dispatchedTask, "<this>");
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if ((mode != -1 ? 1 : 0) == 0) {
                throw new java.lang.AssertionError();
            }
        }
        kotlin.coroutines.Continuation<? super T> delegate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = dispatchedTask.getDelegate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        boolean undispatched = mode == 4;
        if (!undispatched && (delegate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host instanceof kotlinx.coroutines.internal.DispatchedContinuation) && isCancellableMode(mode) == isCancellableMode(dispatchedTask.resumeMode)) {
            kotlinx.coroutines.CoroutineDispatcher dispatcher = ((kotlinx.coroutines.internal.DispatchedContinuation) delegate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host).dispatcher;
            kotlin.coroutines.CoroutineContext context = delegate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host.get$context();
            if (dispatcher.isDispatchNeeded(context)) {
                dispatcher.mo12864dispatch(context, dispatchedTask);
                return;
            } else {
                resumeUnconfined(dispatchedTask);
                return;
            }
        }
        resume(dispatchedTask, delegate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host, undispatched);
    }

    public static final <T> void resume(kotlinx.coroutines.DispatchedTask<? super T> dispatchedTask, kotlin.coroutines.Continuation<? super T> delegate, boolean undispatched) {
        kotlinx.coroutines.UndispatchedCoroutine<?> undispatchedCoroutineUpdateUndispatchedCompletion;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(dispatchedTask, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delegate, "delegate");
        java.lang.Object state = dispatchedTask.takeState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        java.lang.Throwable exception = dispatchedTask.getExceptionalResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(state);
        kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
        java.lang.Object result = kotlin.Result.m11307constructorimpl(exception != null ? kotlin.ResultKt.createFailure(exception) : dispatchedTask.getSuccessfulResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(state));
        if (!undispatched) {
            delegate.resumeWith(result);
            return;
        }
        kotlinx.coroutines.internal.DispatchedContinuation this_$iv = (kotlinx.coroutines.internal.DispatchedContinuation) delegate;
        kotlin.coroutines.Continuation<T> continuation = this_$iv.continuation;
        java.lang.Object countOrElement$iv$iv = this_$iv.countOrElement;
        kotlin.coroutines.CoroutineContext context$iv$iv = continuation.get$context();
        java.lang.Object oldValue$iv$iv = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(context$iv$iv, countOrElement$iv$iv);
        if (oldValue$iv$iv != kotlinx.coroutines.internal.ThreadContextKt.NO_THREAD_ELEMENTS) {
            undispatchedCoroutineUpdateUndispatchedCompletion = kotlinx.coroutines.CoroutineContextKt.updateUndispatchedCompletion(continuation, context$iv$iv, oldValue$iv$iv);
        } else {
            undispatchedCoroutineUpdateUndispatchedCompletion = null;
        }
        try {
            this_$iv.continuation.resumeWith(result);
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
        } finally {
            if (undispatchedCoroutineUpdateUndispatchedCompletion == null || undispatchedCoroutineUpdateUndispatchedCompletion.clearThreadContext()) {
                kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(context$iv$iv, oldValue$iv$iv);
            }
        }
    }

    private static final void resumeUnconfined(kotlinx.coroutines.DispatchedTask<?> dispatchedTask) {
        kotlinx.coroutines.EventLoop eventLoop = kotlinx.coroutines.ThreadLocalEventLoop.INSTANCE.getEventLoop$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (eventLoop.isUnconfinedLoopActive()) {
            eventLoop.dispatchUnconfined(dispatchedTask);
            return;
        }
        eventLoop.incrementUseCount(true);
        try {
            resume(dispatchedTask, dispatchedTask.getDelegate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(), true);
            do {
            } while (eventLoop.processUnconfinedEvent());
        } finally {
            try {
            } finally {
            }
        }
    }

    public static final void runUnconfinedEventLoop(kotlinx.coroutines.DispatchedTask<?> dispatchedTask, kotlinx.coroutines.EventLoop eventLoop, kotlin.jvm.functions.Function0<kotlin.Unit> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(dispatchedTask, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(eventLoop, "eventLoop");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        eventLoop.incrementUseCount(true);
        try {
            block.invoke();
            do {
            } while (eventLoop.processUnconfinedEvent());
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
        } catch (java.lang.Throwable e) {
            try {
                dispatchedTask.handleFatalException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(e, null);
                kotlin.jvm.internal.InlineMarker.finallyStart(1);
            } catch (java.lang.Throwable th) {
                kotlin.jvm.internal.InlineMarker.finallyStart(1);
                eventLoop.decrementUseCount(true);
                kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                throw th;
            }
        }
        eventLoop.decrementUseCount(true);
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
    }

    public static final void resumeWithStackTrace(kotlin.coroutines.Continuation<?> continuation, java.lang.Throwable exception) {
        java.lang.Throwable thRecoverFromStackFrame;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
        if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
            thRecoverFromStackFrame = kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(exception, (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation);
        } else {
            thRecoverFromStackFrame = exception;
        }
        continuation.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(thRecoverFromStackFrame)));
    }
}
