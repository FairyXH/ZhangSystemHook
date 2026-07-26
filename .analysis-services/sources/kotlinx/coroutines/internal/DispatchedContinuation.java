package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: DispatchedContinuation.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0001\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u00042\b\u0012\u0004\u0012\u0002H\u00010\u0005B\u001b\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005¢\u0006\u0002\u0010\tJ\r\u0010\u001f\u001a\u00020 H\u0000¢\u0006\u0002\b!J\u001f\u0010\"\u001a\u00020 2\b\u0010#\u001a\u0004\u0018\u00010\f2\u0006\u0010$\u001a\u00020%H\u0010¢\u0006\u0002\b&J\u0015\u0010'\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u001cH\u0000¢\u0006\u0002\b(J\u001f\u0010)\u001a\u00020 2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010*\u001a\u00028\u0000H\u0000¢\u0006\u0004\b+\u0010,J\u0010\u0010-\u001a\n\u0018\u00010.j\u0004\u0018\u0001`/H\u0016J\r\u00100\u001a\u000201H\u0000¢\u0006\u0002\b2J\u0015\u00103\u001a\u0002012\u0006\u0010$\u001a\u00020%H\u0000¢\u0006\u0002\b4J\r\u00105\u001a\u00020 H\u0000¢\u0006\u0002\b6JH\u00107\u001a\u00020 2\f\u00108\u001a\b\u0012\u0004\u0012\u00028\u0000092%\b\b\u0010:\u001a\u001f\u0012\u0013\u0012\u00110%¢\u0006\f\b<\u0012\b\b=\u0012\u0004\b\b($\u0012\u0004\u0012\u00020 \u0018\u00010;H\u0080\bø\u0001\u0000¢\u0006\u0004\b>\u0010?J\u0018\u0010@\u001a\u0002012\b\u0010A\u001a\u0004\u0018\u00010\fH\u0080\b¢\u0006\u0002\bBJ\u001e\u0010C\u001a\u00020 2\f\u00108\u001a\b\u0012\u0004\u0012\u00028\u000009H\u0080\b¢\u0006\u0004\bD\u0010EJ\u001b\u0010F\u001a\u00020 2\f\u00108\u001a\b\u0012\u0004\u0012\u00028\u000009H\u0016¢\u0006\u0002\u0010EJ\u000f\u0010G\u001a\u0004\u0018\u00010\fH\u0010¢\u0006\u0002\bHJ\b\u0010I\u001a\u00020JH\u0016J\u001b\u0010K\u001a\u0004\u0018\u00010%2\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030LH\u0000¢\u0006\u0002\bMR\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u0004\u0018\u00010\f8\u0000@\u0000X\u0081\u000e¢\u0006\b\n\u0000\u0012\u0004\b\u000e\u0010\u000fR\u001c\u0010\u0010\u001a\n\u0018\u00010\u0003j\u0004\u0018\u0001`\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u0012\u0010\u0013\u001a\u00020\u0014X\u0096\u0005¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016R\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u00020\f8\u0000X\u0081\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0018\u001a\b\u0012\u0004\u0012\u00028\u00000\u00058PX\u0090\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001aR\u0010\u0010\u0006\u001a\u00020\u00078\u0000X\u0081\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001b\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u001c8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u001d\u0010\u001e\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006N"}, d2 = {"Lkotlinx/coroutines/internal/DispatchedContinuation;", "T", "Lkotlinx/coroutines/DispatchedTask;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "Lkotlinx/coroutines/internal/CoroutineStackFrame;", "Lkotlin/coroutines/Continuation;", "dispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "continuation", "(Lkotlinx/coroutines/CoroutineDispatcher;Lkotlin/coroutines/Continuation;)V", "_reusableCancellableContinuation", "Lkotlinx/atomicfu/AtomicRef;", "", "_state", "get_state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host$annotations", "()V", "callerFrame", "getCallerFrame", "()Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "countOrElement", "delegate", "getDelegate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()Lkotlin/coroutines/Continuation;", "reusableCancellableContinuation", "Lkotlinx/coroutines/CancellableContinuationImpl;", "getReusableCancellableContinuation", "()Lkotlinx/coroutines/CancellableContinuationImpl;", "awaitReusability", "", "awaitReusability$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "cancelCompletedResult", "takenState", "cause", "", "cancelCompletedResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "claimReusableCancellableContinuation", "claimReusableCancellableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "dispatchYield", "value", "dispatchYield$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)V", "getStackTraceElement", "Ljava/lang/StackTraceElement;", "Lkotlinx/coroutines/internal/StackTraceElement;", "isReusable", "", "isReusable$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "postponeCancellation", "postponeCancellation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "release", "release$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "resumeCancellableWith", "result", "Lkotlin/Result;", "onCancellation", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "resumeCancellableWith$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "resumeCancelled", "state", "resumeCancelled$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "resumeUndispatchedWith", "resumeUndispatchedWith$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "(Ljava/lang/Object;)V", "resumeWith", "takeState", "takeState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "toString", "", "tryReleaseClaimedContinuation", "Lkotlinx/coroutines/CancellableContinuation;", "tryReleaseClaimedContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class DispatchedContinuation<T> extends kotlinx.coroutines.DispatchedTask<T> implements kotlin.coroutines.jvm.internal.CoroutineStackFrame, kotlin.coroutines.Continuation<T> {
    private final kotlinx.atomicfu.AtomicRef<java.lang.Object> _reusableCancellableContinuation;
    public java.lang.Object _state;
    public final kotlin.coroutines.Continuation<T> continuation;
    public final java.lang.Object countOrElement;
    public final kotlinx.coroutines.CoroutineDispatcher dispatcher;

    public static /* synthetic */ void get_state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host$annotations() {
    }

    @Override // kotlin.coroutines.Continuation
    public kotlin.coroutines.CoroutineContext getContext() {
        return this.continuation.getContext();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public DispatchedContinuation(kotlinx.coroutines.CoroutineDispatcher dispatcher, kotlin.coroutines.Continuation<? super T> continuation) {
        super(-1);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(dispatcher, "dispatcher");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "continuation");
        this.dispatcher = dispatcher;
        this.continuation = continuation;
        this._state = kotlinx.coroutines.internal.DispatchedContinuationKt.UNDEFINED;
        this.countOrElement = kotlinx.coroutines.internal.ThreadContextKt.threadContextElements(getContext());
        this._reusableCancellableContinuation = kotlinx.atomicfu.AtomicFU.atomic((java.lang.Object) null);
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public kotlin.coroutines.jvm.internal.CoroutineStackFrame getCallerFrame() {
        kotlin.coroutines.Continuation<T> continuation = this.continuation;
        if (continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame) {
            return (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation;
        }
        return null;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public java.lang.StackTraceElement getStackTraceElement() {
        return null;
    }

    private final kotlinx.coroutines.CancellableContinuationImpl<?> getReusableCancellableContinuation() {
        java.lang.Object value = this._reusableCancellableContinuation.getValue();
        if (value instanceof kotlinx.coroutines.CancellableContinuationImpl) {
            return (kotlinx.coroutines.CancellableContinuationImpl) value;
        }
        return null;
    }

    public final boolean isReusable$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return this._reusableCancellableContinuation.getValue() != null;
    }

    public final void awaitReusability$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        java.lang.Object it;
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._reusableCancellableContinuation;
        do {
            it = atomicRef.getValue();
        } while (it == kotlinx.coroutines.internal.DispatchedContinuationKt.REUSABLE_CLAIMED);
    }

    public final void release$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        awaitReusability$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        kotlinx.coroutines.CancellableContinuationImpl<?> reusableCancellableContinuation = getReusableCancellableContinuation();
        if (reusableCancellableContinuation != null) {
            reusableCancellableContinuation.detachChild$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        }
    }

    public final kotlinx.coroutines.CancellableContinuationImpl<T> claimReusableCancellableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._reusableCancellableContinuation;
        while (true) {
            java.lang.Object state = atomicRef.getValue();
            if (state == null) {
                this._reusableCancellableContinuation.setValue(kotlinx.coroutines.internal.DispatchedContinuationKt.REUSABLE_CLAIMED);
                return null;
            }
            if (state instanceof kotlinx.coroutines.CancellableContinuationImpl) {
                if (this._reusableCancellableContinuation.compareAndSet(state, kotlinx.coroutines.internal.DispatchedContinuationKt.REUSABLE_CLAIMED)) {
                    return (kotlinx.coroutines.CancellableContinuationImpl) state;
                }
            } else if (state != kotlinx.coroutines.internal.DispatchedContinuationKt.REUSABLE_CLAIMED && !(state instanceof java.lang.Throwable)) {
                throw new java.lang.IllegalStateException(("Inconsistent state " + state).toString());
            }
        }
    }

    public final java.lang.Throwable tryReleaseClaimedContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlinx.coroutines.CancellableContinuation<?> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "continuation");
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._reusableCancellableContinuation;
        do {
            java.lang.Object state = atomicRef.getValue();
            if (state != kotlinx.coroutines.internal.DispatchedContinuationKt.REUSABLE_CLAIMED) {
                if (state instanceof java.lang.Throwable) {
                    if (!this._reusableCancellableContinuation.compareAndSet(state, null)) {
                        throw new java.lang.IllegalArgumentException("Failed requirement.".toString());
                    }
                    return (java.lang.Throwable) state;
                }
                throw new java.lang.IllegalStateException(("Inconsistent state " + state).toString());
            }
        } while (!this._reusableCancellableContinuation.compareAndSet(kotlinx.coroutines.internal.DispatchedContinuationKt.REUSABLE_CLAIMED, continuation));
        return null;
    }

    public final boolean postponeCancellation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._reusableCancellableContinuation;
        while (true) {
            java.lang.Object state = atomicRef.getValue();
            if (kotlin.jvm.internal.Intrinsics.areEqual(state, kotlinx.coroutines.internal.DispatchedContinuationKt.REUSABLE_CLAIMED)) {
                if (this._reusableCancellableContinuation.compareAndSet(kotlinx.coroutines.internal.DispatchedContinuationKt.REUSABLE_CLAIMED, cause)) {
                    return true;
                }
            } else {
                if (state instanceof java.lang.Throwable) {
                    return true;
                }
                if (this._reusableCancellableContinuation.compareAndSet(state, null)) {
                    return false;
                }
            }
        }
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public java.lang.Object takeState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        java.lang.Object state = this._state;
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(state != kotlinx.coroutines.internal.DispatchedContinuationKt.UNDEFINED)) {
                throw new java.lang.AssertionError();
            }
        }
        this._state = kotlinx.coroutines.internal.DispatchedContinuationKt.UNDEFINED;
        return state;
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public kotlin.coroutines.Continuation<T> getDelegate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return this;
    }

    @Override // kotlin.coroutines.Continuation
    public void resumeWith(java.lang.Object result) {
        kotlin.coroutines.CoroutineContext context = this.continuation.getContext();
        java.lang.Object state = kotlinx.coroutines.CompletionStateKt.toState$default(result, null, 1, null);
        if (this.dispatcher.isDispatchNeeded(context)) {
            this._state = state;
            this.resumeMode = 0;
            this.dispatcher.mo12864dispatch(context, this);
            return;
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
        }
        kotlinx.coroutines.EventLoop eventLoop$iv = kotlinx.coroutines.ThreadLocalEventLoop.INSTANCE.getEventLoop$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (eventLoop$iv.isUnconfinedLoopActive()) {
            this._state = state;
            this.resumeMode = 0;
            eventLoop$iv.dispatchUnconfined(this);
            return;
        }
        kotlinx.coroutines.internal.DispatchedContinuation<T> $this$runUnconfinedEventLoop$iv$iv = this;
        eventLoop$iv.incrementUseCount(true);
        try {
            kotlin.coroutines.CoroutineContext context$iv = getContext();
            java.lang.Object countOrElement$iv = this.countOrElement;
            java.lang.Object oldValue$iv = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(context$iv, countOrElement$iv);
            try {
                this.continuation.resumeWith(result);
                kotlin.Unit unit = kotlin.Unit.INSTANCE;
                while (eventLoop$iv.processUnconfinedEvent()) {
                }
            } finally {
                kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(context$iv, oldValue$iv);
            }
        } finally {
            try {
            } finally {
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00a8  */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r1v5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void resumeCancellableWith$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object r23, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> r24) {
        /*
            Method dump skipped, instruction units count: 349
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.DispatchedContinuation.resumeCancellableWith$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object, kotlin.jvm.functions.Function1):void");
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public void cancelCompletedResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object takenState, java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
        if (takenState instanceof kotlinx.coroutines.CompletedWithCancellation) {
            ((kotlinx.coroutines.CompletedWithCancellation) takenState).onCancellation.invoke(cause);
        }
    }

    public final boolean resumeCancelled$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object state) {
        kotlinx.coroutines.Job job = (kotlinx.coroutines.Job) getContext().get(kotlinx.coroutines.Job.INSTANCE);
        if (job != null && !job.isActive()) {
            java.util.concurrent.CancellationException cause = job.getCancellationException();
            cancelCompletedResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(state, cause);
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(cause)));
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x002f A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void resumeUndispatchedWith$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object r11) {
        /*
            r10 = this;
            r0 = 0
            kotlin.coroutines.Continuation<T> r1 = r10.continuation
            java.lang.Object r2 = r10.countOrElement
            r3 = 0
            kotlin.coroutines.CoroutineContext r4 = r1.getContext()
            java.lang.Object r5 = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(r4, r2)
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.internal.ThreadContextKt.NO_THREAD_ELEMENTS
            if (r5 == r6) goto L17
            kotlinx.coroutines.UndispatchedCoroutine r6 = kotlinx.coroutines.CoroutineContextKt.updateUndispatchedCompletion(r1, r4, r5)
            goto L18
        L17:
            r6 = 0
        L18:
            r7 = 0
            r8 = 1
            kotlin.coroutines.Continuation<T> r9 = r10.continuation     // Catch: java.lang.Throwable -> L37
            r9.resumeWith(r11)     // Catch: java.lang.Throwable -> L37
            kotlin.Unit r7 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> L37
            kotlin.jvm.internal.InlineMarker.finallyStart(r8)
            if (r6 == 0) goto L2f
            boolean r7 = r6.clearThreadContext()
            if (r7 == 0) goto L32
        L2f:
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(r4, r5)
        L32:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r8)
            return
        L37:
            r7 = move-exception
            kotlin.jvm.internal.InlineMarker.finallyStart(r8)
            if (r6 == 0) goto L43
            boolean r9 = r6.clearThreadContext()
            if (r9 == 0) goto L46
        L43:
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(r4, r5)
        L46:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r8)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.DispatchedContinuation.resumeUndispatchedWith$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object):void");
    }

    public final void dispatchYield$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlin.coroutines.CoroutineContext context, T value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        this._state = value;
        this.resumeMode = 1;
        this.dispatcher.dispatchYield(context, this);
    }

    public java.lang.String toString() {
        return "DispatchedContinuation[" + this.dispatcher + ", " + kotlinx.coroutines.DebugStringsKt.toDebugString(this.continuation) + "]";
    }
}
