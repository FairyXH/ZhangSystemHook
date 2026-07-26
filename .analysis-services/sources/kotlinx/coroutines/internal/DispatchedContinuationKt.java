package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: DispatchedContinuation.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000J\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a;\u0010\u0003\u001a\u00020\u0004*\u0006\u0012\u0002\b\u00030\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00042\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u0082\b\u001aR\u0010\u000e\u001a\u00020\r\"\u0004\b\u0000\u0010\u000f*\b\u0012\u0004\u0012\u0002H\u000f0\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u00122%\b\u0002\u0010\u0013\u001a\u001f\u0012\u0013\u0012\u00110\u0015¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0004\u0012\u00020\r\u0018\u00010\u0014H\u0007¢\u0006\u0002\u0010\u0019\u001a\u0012\u0010\u001a\u001a\u00020\u0004*\b\u0012\u0004\u0012\u00020\r0\u0005H\u0000\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"REUSABLE_CLAIMED", "Lkotlinx/coroutines/internal/Symbol;", "UNDEFINED", "executeUnconfined", "", "Lkotlinx/coroutines/internal/DispatchedContinuation;", "contState", "", com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY, "", "doYield", "block", "Lkotlin/Function0;", "", "resumeCancellableWith", "T", "Lkotlin/coroutines/Continuation;", "result", "Lkotlin/Result;", "onCancellation", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "cause", "(Lkotlin/coroutines/Continuation;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "yieldUndispatched", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class DispatchedContinuationKt {
    private static final kotlinx.coroutines.internal.Symbol UNDEFINED = new kotlinx.coroutines.internal.Symbol("UNDEFINED");
    public static final kotlinx.coroutines.internal.Symbol REUSABLE_CLAIMED = new kotlinx.coroutines.internal.Symbol("REUSABLE_CLAIMED");

    public static /* synthetic */ void resumeCancellableWith$default(kotlin.coroutines.Continuation continuation, java.lang.Object obj, kotlin.jvm.functions.Function1 function1, int i, java.lang.Object obj2) {
        if ((i & 2) != 0) {
            function1 = null;
        }
        resumeCancellableWith(continuation, obj, function1);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00b4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <T> void resumeCancellableWith(kotlin.coroutines.Continuation<? super T> r22, java.lang.Object r23, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> r24) {
        /*
            Method dump skipped, instruction units count: 338
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.DispatchedContinuationKt.resumeCancellableWith(kotlin.coroutines.Continuation, java.lang.Object, kotlin.jvm.functions.Function1):void");
    }

    /* JADX WARN: Finally extract failed */
    public static final boolean yieldUndispatched(kotlinx.coroutines.internal.DispatchedContinuation<? super kotlin.Unit> dispatchedContinuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(dispatchedContinuation, "<this>");
        java.lang.Object contState$iv = kotlin.Unit.INSTANCE;
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
        }
        kotlinx.coroutines.EventLoop eventLoop$iv = kotlinx.coroutines.ThreadLocalEventLoop.INSTANCE.getEventLoop$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (eventLoop$iv.isUnconfinedQueueEmpty()) {
            return false;
        }
        if (eventLoop$iv.isUnconfinedLoopActive()) {
            dispatchedContinuation._state = contState$iv;
            dispatchedContinuation.resumeMode = 1;
            eventLoop$iv.dispatchUnconfined(dispatchedContinuation);
            return true;
        }
        kotlinx.coroutines.internal.DispatchedContinuation<? super kotlin.Unit> $this$runUnconfinedEventLoop$iv$iv = dispatchedContinuation;
        eventLoop$iv.incrementUseCount(true);
        try {
            dispatchedContinuation.run();
            do {
            } while (eventLoop$iv.processUnconfinedEvent());
        } catch (java.lang.Throwable e$iv$iv) {
            try {
                $this$runUnconfinedEventLoop$iv$iv.handleFatalException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(e$iv$iv, null);
            } finally {
                eventLoop$iv.decrementUseCount(true);
            }
        }
        return false;
    }

    static /* synthetic */ boolean executeUnconfined$default(kotlinx.coroutines.internal.DispatchedContinuation $this$executeUnconfined_u24default, java.lang.Object contState, int mode, boolean doYield, kotlin.jvm.functions.Function0 block, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            doYield = false;
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if ((mode != -1 ? 1 : 0) == 0) {
                throw new java.lang.AssertionError();
            }
        }
        kotlinx.coroutines.EventLoop eventLoop = kotlinx.coroutines.ThreadLocalEventLoop.INSTANCE.getEventLoop$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (doYield && eventLoop.isUnconfinedQueueEmpty()) {
            return false;
        }
        if (eventLoop.isUnconfinedLoopActive()) {
            $this$executeUnconfined_u24default._state = contState;
            $this$executeUnconfined_u24default.resumeMode = mode;
            eventLoop.dispatchUnconfined($this$executeUnconfined_u24default);
            return true;
        }
        kotlinx.coroutines.internal.DispatchedContinuation $this$runUnconfinedEventLoop$iv = $this$executeUnconfined_u24default;
        eventLoop.incrementUseCount(true);
        try {
            block.invoke();
            do {
            } while (eventLoop.processUnconfinedEvent());
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
        } catch (java.lang.Throwable e$iv) {
            try {
                $this$runUnconfinedEventLoop$iv.handleFatalException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(e$iv, null);
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
        return false;
    }

    private static final boolean executeUnconfined(kotlinx.coroutines.internal.DispatchedContinuation<?> dispatchedContinuation, java.lang.Object contState, int mode, boolean doYield, kotlin.jvm.functions.Function0<kotlin.Unit> function0) {
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if ((mode != -1 ? 1 : 0) == 0) {
                throw new java.lang.AssertionError();
            }
        }
        kotlinx.coroutines.EventLoop eventLoop = kotlinx.coroutines.ThreadLocalEventLoop.INSTANCE.getEventLoop$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (doYield && eventLoop.isUnconfinedQueueEmpty()) {
            return false;
        }
        if (eventLoop.isUnconfinedLoopActive()) {
            dispatchedContinuation._state = contState;
            dispatchedContinuation.resumeMode = mode;
            eventLoop.dispatchUnconfined(dispatchedContinuation);
            return true;
        }
        kotlinx.coroutines.internal.DispatchedContinuation<?> $this$runUnconfinedEventLoop$iv = dispatchedContinuation;
        eventLoop.incrementUseCount(true);
        try {
            function0.invoke();
            do {
            } while (eventLoop.processUnconfinedEvent());
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
        } catch (java.lang.Throwable e$iv) {
            try {
                $this$runUnconfinedEventLoop$iv.handleFatalException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(e$iv, null);
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
        return false;
    }
}
