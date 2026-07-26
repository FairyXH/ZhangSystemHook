package kotlinx.coroutines;

/* JADX INFO: compiled from: CancellableContinuationImpl.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000È\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0001\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0011\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00060\u0004j\u0002`\u00052\u00020\u0006B\u001b\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\u0012\u0010+\u001a\u00020,2\b\u0010-\u001a\u0004\u0018\u00010\u0012H\u0002J\u0018\u0010.\u001a\u00020/2\u0006\u00100\u001a\u0002012\b\u00102\u001a\u0004\u0018\u000103J;\u0010.\u001a\u00020/2'\u00100\u001a#\u0012\u0015\u0012\u0013\u0018\u000103¢\u0006\f\b5\u0012\b\b6\u0012\u0004\b\b(2\u0012\u0004\u0012\u00020/04j\u0002`72\b\u00102\u001a\u0004\u0018\u000103H\u0002J\u0017\u00108\u001a\u00020/2\f\u00109\u001a\b\u0012\u0004\u0012\u00020/0:H\u0082\bJ1\u0010;\u001a\u00020/2!\u0010<\u001a\u001d\u0012\u0013\u0012\u001103¢\u0006\f\b5\u0012\b\b6\u0012\u0004\b\b(2\u0012\u0004\u0012\u00020/042\u0006\u00102\u001a\u000203J\u001e\u0010=\u001a\u00020/2\n\u0010>\u001a\u0006\u0012\u0002\b\u00030?2\b\u00102\u001a\u0004\u0018\u000103H\u0002J\u0012\u0010@\u001a\u00020\u001d2\b\u00102\u001a\u0004\u0018\u000103H\u0016J\u001f\u0010A\u001a\u00020/2\b\u0010B\u001a\u0004\u0018\u00010\u00122\u0006\u00102\u001a\u000203H\u0010¢\u0006\u0002\bCJ\u0010\u0010D\u001a\u00020\u001d2\u0006\u00102\u001a\u000203H\u0002J\u0010\u0010E\u001a\u00020/2\u0006\u0010F\u001a\u00020\u0012H\u0016J\r\u0010G\u001a\u00020/H\u0000¢\u0006\u0002\bHJ\b\u0010I\u001a\u00020/H\u0002J\u0010\u0010J\u001a\u00020/2\u0006\u0010K\u001a\u00020\nH\u0002J\u0010\u0010L\u001a\u0002032\u0006\u0010M\u001a\u00020NH\u0016J\u0019\u0010O\u001a\u0004\u0018\u0001032\b\u0010$\u001a\u0004\u0018\u00010\u0012H\u0010¢\u0006\u0002\bPJ\n\u0010Q\u001a\u0004\u0018\u00010\u0012H\u0001J\u0010\u0010R\u001a\n\u0018\u00010Sj\u0004\u0018\u0001`TH\u0016J\u001f\u0010U\u001a\u0002H\u0001\"\u0004\b\u0001\u0010\u00012\b\u0010$\u001a\u0004\u0018\u00010\u0012H\u0010¢\u0006\u0004\bV\u0010WJ\b\u0010X\u001a\u00020/H\u0016J\n\u0010Y\u001a\u0004\u0018\u00010\u0010H\u0002J1\u0010Z\u001a\u00020/2'\u00100\u001a#\u0012\u0015\u0012\u0013\u0018\u000103¢\u0006\f\b5\u0012\b\b6\u0012\u0004\b\b(2\u0012\u0004\u0012\u00020/04j\u0002`7H\u0016J\u001c\u0010Z\u001a\u00020/2\n\u0010>\u001a\u0006\u0012\u0002\b\u00030?2\u0006\u0010[\u001a\u00020\nH\u0016J\u0010\u0010\\\u001a\u00020/2\u0006\u00100\u001a\u00020\u0012H\u0002J\b\u0010]\u001a\u00020\u001dH\u0002J1\u0010^\u001a\u0002012'\u00100\u001a#\u0012\u0015\u0012\u0013\u0018\u000103¢\u0006\f\b5\u0012\b\b6\u0012\u0004\b\b(2\u0012\u0004\u0012\u00020/04j\u0002`7H\u0002J\u001a\u0010_\u001a\u00020/2\u0006\u00100\u001a\u00020\u00122\b\u0010$\u001a\u0004\u0018\u00010\u0012H\u0002J\b\u0010`\u001a\u00020(H\u0014J\u0015\u0010a\u001a\u00020/2\u0006\u00102\u001a\u000203H\u0000¢\u0006\u0002\bbJ\r\u0010c\u001a\u00020/H\u0000¢\u0006\u0002\bdJ\b\u0010e\u001a\u00020\u001dH\u0001J:\u0010f\u001a\u00020/2\u0006\u0010g\u001a\u00028\u00002#\u0010<\u001a\u001f\u0012\u0013\u0012\u001103¢\u0006\f\b5\u0012\b\b6\u0012\u0004\b\b(2\u0012\u0004\u0012\u00020/\u0018\u000104H\u0016¢\u0006\u0002\u0010hJA\u0010i\u001a\u00020/2\b\u0010-\u001a\u0004\u0018\u00010\u00122\u0006\u0010\t\u001a\u00020\n2%\b\u0002\u0010<\u001a\u001f\u0012\u0013\u0012\u001103¢\u0006\f\b5\u0012\b\b6\u0012\u0004\b\b(2\u0012\u0004\u0012\u00020/\u0018\u000104H\u0002J\u001b\u0010j\u001a\u00020/2\f\u0010k\u001a\b\u0012\u0004\u0012\u00028\u00000lH\u0016¢\u0006\u0002\u0010mJS\u0010n\u001a\u0004\u0018\u00010\u00122\u0006\u0010$\u001a\u00020o2\b\u0010-\u001a\u0004\u0018\u00010\u00122\u0006\u0010\t\u001a\u00020\n2#\u0010<\u001a\u001f\u0012\u0013\u0012\u001103¢\u0006\f\b5\u0012\b\b6\u0012\u0004\b\b(2\u0012\u0004\u0012\u00020/\u0018\u0001042\b\u0010p\u001a\u0004\u0018\u00010\u0012H\u0002J\u000f\u0010q\u001a\u0004\u0018\u00010\u0012H\u0010¢\u0006\u0002\brJ\b\u0010s\u001a\u00020(H\u0016J\b\u0010t\u001a\u00020\u001dH\u0002J!\u0010t\u001a\u0004\u0018\u00010\u00122\u0006\u0010g\u001a\u00028\u00002\b\u0010p\u001a\u0004\u0018\u00010\u0012H\u0016¢\u0006\u0002\u0010uJF\u0010t\u001a\u0004\u0018\u00010\u00122\u0006\u0010g\u001a\u00028\u00002\b\u0010p\u001a\u0004\u0018\u00010\u00122#\u0010<\u001a\u001f\u0012\u0013\u0012\u001103¢\u0006\f\b5\u0012\b\b6\u0012\u0004\b\b(2\u0012\u0004\u0012\u00020/\u0018\u000104H\u0016¢\u0006\u0002\u0010vJC\u0010w\u001a\u0004\u0018\u00010x2\b\u0010-\u001a\u0004\u0018\u00010\u00122\b\u0010p\u001a\u0004\u0018\u00010\u00122#\u0010<\u001a\u001f\u0012\u0013\u0012\u001103¢\u0006\f\b5\u0012\b\b6\u0012\u0004\b\b(2\u0012\u0004\u0012\u00020/\u0018\u000104H\u0002J\u0012\u0010y\u001a\u0004\u0018\u00010\u00122\u0006\u0010z\u001a\u000203H\u0016J\b\u0010{\u001a\u00020\u001dH\u0002J\u0019\u0010|\u001a\u00020/*\u00020}2\u0006\u0010g\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010~J\u0014\u0010\u007f\u001a\u00020/*\u00020}2\u0006\u0010z\u001a\u000203H\u0016R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00100\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0013\u001a\n\u0018\u00010\u0004j\u0004\u0018\u0001`\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\u00020\u0017X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0014\u0010\u001c\u001a\u00020\u001d8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u001eR\u0014\u0010\u001f\u001a\u00020\u001d8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u001f\u0010\u001eR\u0014\u0010 \u001a\u00020\u001d8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b \u0010\u001eR\u0016\u0010!\u001a\u0004\u0018\u00010\u00108BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\"\u0010#R\u0016\u0010$\u001a\u0004\u0018\u00010\u00128@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b%\u0010&R\u0014\u0010'\u001a\u00020(8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b)\u0010*¨\u0006\u0080\u0001"}, d2 = {"Lkotlinx/coroutines/CancellableContinuationImpl;", "T", "Lkotlinx/coroutines/DispatchedTask;", "Lkotlinx/coroutines/CancellableContinuation;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "Lkotlinx/coroutines/internal/CoroutineStackFrame;", "Lkotlinx/coroutines/Waiter;", "delegate", "Lkotlin/coroutines/Continuation;", "resumeMode", "", "(Lkotlin/coroutines/Continuation;I)V", "_decisionAndIndex", "Lkotlinx/atomicfu/AtomicInt;", "_parentHandle", "Lkotlinx/atomicfu/AtomicRef;", "Lkotlinx/coroutines/DisposableHandle;", "_state", "", "callerFrame", "getCallerFrame", "()Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "getDelegate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()Lkotlin/coroutines/Continuation;", "isActive", "", "()Z", "isCancelled", "isCompleted", "parentHandle", "getParentHandle", "()Lkotlinx/coroutines/DisposableHandle;", "state", "getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()Ljava/lang/Object;", "stateDebugRepresentation", "", "getStateDebugRepresentation", "()Ljava/lang/String;", "alreadyResumedError", "", "proposedUpdate", "callCancelHandler", "", "handler", "Lkotlinx/coroutines/CancelHandler;", "cause", "", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "Lkotlinx/coroutines/CompletionHandler;", "callCancelHandlerSafely", "block", "Lkotlin/Function0;", "callOnCancellation", "onCancellation", "callSegmentOnCancellation", "segment", "Lkotlinx/coroutines/internal/Segment;", "cancel", "cancelCompletedResult", "takenState", "cancelCompletedResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "cancelLater", "completeResume", "token", "detachChild", "detachChild$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "detachChildIfNonResuable", "dispatchResume", com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY, "getContinuationCancellationCause", "parent", "Lkotlinx/coroutines/Job;", "getExceptionalResult", "getExceptionalResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "getResult", "getStackTraceElement", "Ljava/lang/StackTraceElement;", "Lkotlinx/coroutines/internal/StackTraceElement;", "getSuccessfulResult", "getSuccessfulResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "(Ljava/lang/Object;)Ljava/lang/Object;", "initCancellability", "installParentHandle", "invokeOnCancellation", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "invokeOnCancellationImpl", "isReusable", "makeCancelHandler", "multipleHandlersError", "nameString", "parentCancelled", "parentCancelled$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "releaseClaimedReusableContinuation", "releaseClaimedReusableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "resetStateReusable", "resume", "value", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "resumeImpl", "resumeWith", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "resumedState", "Lkotlinx/coroutines/NotCompleted;", "idempotent", "takeState", "takeState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "toString", "tryResume", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "(Ljava/lang/Object;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "tryResumeImpl", "Lkotlinx/coroutines/internal/Symbol;", "tryResumeWithException", "exception", "trySuspend", "resumeUndispatched", "Lkotlinx/coroutines/CoroutineDispatcher;", "(Lkotlinx/coroutines/CoroutineDispatcher;Ljava/lang/Object;)V", "resumeUndispatchedWithException", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class CancellableContinuationImpl<T> extends kotlinx.coroutines.DispatchedTask<T> implements kotlinx.coroutines.CancellableContinuation<T>, kotlin.coroutines.jvm.internal.CoroutineStackFrame, kotlinx.coroutines.Waiter {
    private final kotlinx.atomicfu.AtomicInt _decisionAndIndex;
    private final kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.DisposableHandle> _parentHandle;
    private final kotlinx.atomicfu.AtomicRef<java.lang.Object> _state;
    private final kotlin.coroutines.CoroutineContext context;
    private final kotlin.coroutines.Continuation<T> delegate;

    @Override // kotlinx.coroutines.DispatchedTask
    public final kotlin.coroutines.Continuation<T> getDelegate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return this.delegate;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public CancellableContinuationImpl(kotlin.coroutines.Continuation<? super T> delegate, int resumeMode) {
        super(resumeMode);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delegate, "delegate");
        this.delegate = delegate;
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(resumeMode != -1)) {
                throw new java.lang.AssertionError();
            }
        }
        this.context = this.delegate.getContext();
        this._decisionAndIndex = kotlinx.atomicfu.AtomicFU.atomic((0 << 29) + 536870911);
        this._state = kotlinx.atomicfu.AtomicFU.atomic(kotlinx.coroutines.Active.INSTANCE);
        this._parentHandle = kotlinx.atomicfu.AtomicFU.atomic((java.lang.Object) null);
    }

    @Override // kotlin.coroutines.Continuation
    public kotlin.coroutines.CoroutineContext getContext() {
        return this.context;
    }

    private final kotlinx.coroutines.DisposableHandle getParentHandle() {
        return this._parentHandle.getValue();
    }

    public final java.lang.Object getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return this._state.getValue();
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public boolean isActive() {
        return getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() instanceof kotlinx.coroutines.NotCompleted;
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public boolean isCompleted() {
        return !(getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() instanceof kotlinx.coroutines.NotCompleted);
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public boolean isCancelled() {
        return getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() instanceof kotlinx.coroutines.CancelledContinuation;
    }

    private final java.lang.String getStateDebugRepresentation() {
        java.lang.Object state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        return state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host instanceof kotlinx.coroutines.NotCompleted ? "Active" : state$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host instanceof kotlinx.coroutines.CancelledContinuation ? "Cancelled" : "Completed";
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public void initCancellability() {
        kotlinx.coroutines.DisposableHandle handle = installParentHandle();
        if (handle != null && isCompleted()) {
            handle.dispose();
            this._parentHandle.setValue(kotlinx.coroutines.NonDisposableHandle.INSTANCE);
        }
    }

    private final boolean isReusable() {
        if (kotlinx.coroutines.DispatchedTaskKt.isReusableMode(this.resumeMode)) {
            kotlin.coroutines.Continuation<T> continuation = this.delegate;
            kotlin.jvm.internal.Intrinsics.checkNotNull(continuation, "null cannot be cast to non-null type kotlinx.coroutines.internal.DispatchedContinuation<*>");
            if (((kotlinx.coroutines.internal.DispatchedContinuation) continuation).isReusable$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                return true;
            }
        }
        return false;
    }

    public final boolean resetStateReusable() {
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if ((this.resumeMode == 2 ? 1 : 0) == 0) {
                throw new java.lang.AssertionError();
            }
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if ((getParentHandle() != kotlinx.coroutines.NonDisposableHandle.INSTANCE ? 1 : 0) == 0) {
                throw new java.lang.AssertionError();
            }
        }
        java.lang.Object state = this._state.getValue();
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !(!(state instanceof kotlinx.coroutines.NotCompleted))) {
            throw new java.lang.AssertionError();
        }
        if ((state instanceof kotlinx.coroutines.CompletedContinuation) && ((kotlinx.coroutines.CompletedContinuation) state).idempotentResume != null) {
            detachChild$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            return false;
        }
        this._decisionAndIndex.setValue((0 << 29) + 536870911);
        this._state.setValue(kotlinx.coroutines.Active.INSTANCE);
        return true;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public kotlin.coroutines.jvm.internal.CoroutineStackFrame getCallerFrame() {
        kotlin.coroutines.Continuation<T> continuation = this.delegate;
        if (continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame) {
            return (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation;
        }
        return null;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public java.lang.StackTraceElement getStackTraceElement() {
        return null;
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public java.lang.Object takeState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public void cancelCompletedResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object takenState, java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._state;
        while (true) {
            java.lang.Object state = atomicRef.getValue();
            if (state instanceof kotlinx.coroutines.NotCompleted) {
                throw new java.lang.IllegalStateException("Not completed".toString());
            }
            if (state instanceof kotlinx.coroutines.CompletedExceptionally) {
                return;
            }
            if (state instanceof kotlinx.coroutines.CompletedContinuation) {
                if (!(!((kotlinx.coroutines.CompletedContinuation) state).getCancelled())) {
                    throw new java.lang.IllegalStateException("Must be called at most once".toString());
                }
                kotlinx.coroutines.CompletedContinuation update = kotlinx.coroutines.CompletedContinuation.copy$default((kotlinx.coroutines.CompletedContinuation) state, null, null, null, null, cause, 15, null);
                if (this._state.compareAndSet(state, update)) {
                    ((kotlinx.coroutines.CompletedContinuation) state).invokeHandlers(this, cause);
                    return;
                }
            } else if (this._state.compareAndSet(state, new kotlinx.coroutines.CompletedContinuation(state, null, null, null, cause, 14, null))) {
                return;
            }
        }
    }

    private final boolean cancelLater(java.lang.Throwable cause) {
        if (!isReusable()) {
            return false;
        }
        kotlin.coroutines.Continuation<T> continuation = this.delegate;
        kotlin.jvm.internal.Intrinsics.checkNotNull(continuation, "null cannot be cast to non-null type kotlinx.coroutines.internal.DispatchedContinuation<*>");
        kotlinx.coroutines.internal.DispatchedContinuation dispatched = (kotlinx.coroutines.internal.DispatchedContinuation) continuation;
        return dispatched.postponeCancellation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(cause);
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public boolean cancel(java.lang.Throwable cause) {
        java.lang.Object state;
        kotlinx.coroutines.CancelledContinuation update;
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._state;
        do {
            state = atomicRef.getValue();
            if (!(state instanceof kotlinx.coroutines.NotCompleted)) {
                return false;
            }
            update = new kotlinx.coroutines.CancelledContinuation(this, cause, (state instanceof kotlinx.coroutines.CancelHandler) || (state instanceof kotlinx.coroutines.internal.Segment));
        } while (!this._state.compareAndSet(state, update));
        kotlinx.coroutines.NotCompleted notCompleted = (kotlinx.coroutines.NotCompleted) state;
        if (notCompleted instanceof kotlinx.coroutines.CancelHandler) {
            callCancelHandler((kotlinx.coroutines.CancelHandler) state, cause);
        } else if (notCompleted instanceof kotlinx.coroutines.internal.Segment) {
            callSegmentOnCancellation((kotlinx.coroutines.internal.Segment) state, cause);
        }
        detachChildIfNonResuable();
        dispatchResume(this.resumeMode);
        return true;
    }

    public final void parentCancelled$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
        if (cancelLater(cause)) {
            return;
        }
        cancel(cause);
        detachChildIfNonResuable();
    }

    private final void callCancelHandlerSafely(kotlin.jvm.functions.Function0<kotlin.Unit> block) {
        try {
            block.invoke();
        } catch (java.lang.Throwable ex) {
            kotlinx.coroutines.CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), new kotlinx.coroutines.CompletionHandlerException("Exception in invokeOnCancellation handler for " + this, ex));
        }
    }

    private final void callCancelHandler(kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> handler, java.lang.Throwable cause) {
        try {
            handler.invoke(cause);
        } catch (java.lang.Throwable ex$iv) {
            kotlinx.coroutines.CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), new kotlinx.coroutines.CompletionHandlerException("Exception in invokeOnCancellation handler for " + this, ex$iv));
        }
    }

    public final void callCancelHandler(kotlinx.coroutines.CancelHandler handler, java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(handler, "handler");
        try {
            handler.invoke(cause);
        } catch (java.lang.Throwable ex$iv) {
            kotlinx.coroutines.CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), new kotlinx.coroutines.CompletionHandlerException("Exception in invokeOnCancellation handler for " + this, ex$iv));
        }
    }

    private final void callSegmentOnCancellation(kotlinx.coroutines.internal.Segment<?> segment, java.lang.Throwable cause) {
        int $this$index$iv = this._decisionAndIndex.getValue();
        int index = $this$index$iv & 536870911;
        if (!(index != 536870911)) {
            throw new java.lang.IllegalStateException("The index for Segment.onCancellation(..) is broken".toString());
        }
        try {
            segment.onCancellation(index, cause, getContext());
        } catch (java.lang.Throwable ex$iv) {
            kotlinx.coroutines.CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), new kotlinx.coroutines.CompletionHandlerException("Exception in invokeOnCancellation handler for " + this, ex$iv));
        }
    }

    public final void callOnCancellation(kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> onCancellation, java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onCancellation, "onCancellation");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
        try {
            onCancellation.invoke(cause);
        } catch (java.lang.Throwable ex) {
            kotlinx.coroutines.CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), new kotlinx.coroutines.CompletionHandlerException("Exception in resume onCancellation handler for " + this, ex));
        }
    }

    public java.lang.Throwable getContinuationCancellationCause(kotlinx.coroutines.Job parent) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(parent, "parent");
        return parent.getCancellationException();
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:104)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private final boolean trySuspend() {
        /*
            r10 = this;
            kotlinx.atomicfu.AtomicInt r0 = r10._decisionAndIndex
            r1 = 0
        L3:
            int r2 = r0.getValue()
            r3 = 0
            r4 = r2
            r5 = 0
            int r4 = r4 >> 29
            switch(r4) {
                case 0: goto L1e;
                case 1: goto L10;
                case 2: goto L1c;
                default: goto L10;
            }
        L10:
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
            java.lang.String r5 = "Already suspended"
            java.lang.String r5 = r5.toString()
            r4.<init>(r5)
            throw r4
        L1c:
            r4 = 0
            return r4
        L1e:
            kotlinx.atomicfu.AtomicInt r4 = r10._decisionAndIndex
            r5 = r2
            r6 = 0
            r7 = 536870911(0x1fffffff, float:1.0842021E-19)
            r5 = r5 & r7
            r6 = 1
            r7 = r6
            r8 = 0
            int r9 = r7 << 29
            int r9 = r9 + r5
            boolean r4 = r4.compareAndSet(r2, r9)
            if (r4 == 0) goto L34
            return r6
        L34:
            goto L3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.CancellableContinuationImpl.trySuspend():boolean");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:104)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private final boolean tryResume() {
        /*
            r9 = this;
            kotlinx.atomicfu.AtomicInt r0 = r9._decisionAndIndex
            r1 = 0
        L3:
            int r2 = r0.getValue()
            r3 = 0
            r4 = r2
            r5 = 0
            int r4 = r4 >> 29
            switch(r4) {
                case 0: goto L1e;
                case 1: goto L1c;
                default: goto L10;
            }
        L10:
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
            java.lang.String r5 = "Already resumed"
            java.lang.String r5 = r5.toString()
            r4.<init>(r5)
            throw r4
        L1c:
            r4 = 0
            return r4
        L1e:
            kotlinx.atomicfu.AtomicInt r4 = r9._decisionAndIndex
            r5 = r2
            r6 = 0
            r7 = 536870911(0x1fffffff, float:1.0842021E-19)
            r5 = r5 & r7
            r6 = 2
            r7 = 0
            int r8 = r6 << 29
            int r8 = r8 + r5
            boolean r4 = r4.compareAndSet(r2, r8)
            if (r4 == 0) goto L34
            r4 = 1
            return r4
        L34:
            goto L3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.CancellableContinuationImpl.tryResume():boolean");
    }

    public final java.lang.Object getResult() {
        kotlinx.coroutines.Job job;
        boolean isReusable = isReusable();
        if (trySuspend()) {
            if (getParentHandle() == null) {
                installParentHandle();
            }
            if (isReusable) {
                releaseClaimedReusableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            }
            return kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        if (isReusable) {
            releaseClaimedReusableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        }
        java.lang.Object state = getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (!(state instanceof kotlinx.coroutines.CompletedExceptionally)) {
            if (kotlinx.coroutines.DispatchedTaskKt.isCancellableMode(this.resumeMode) && (job = (kotlinx.coroutines.Job) getContext().get(kotlinx.coroutines.Job.INSTANCE)) != null && !job.isActive()) {
                java.util.concurrent.CancellationException cause = job.getCancellationException();
                cancelCompletedResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(state, cause);
                if (!kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() || !(this instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
                    throw cause;
                }
                throw kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(cause, this);
            }
            return getSuccessfulResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(state);
        }
        java.lang.Throwable exception$iv = ((kotlinx.coroutines.CompletedExceptionally) state).cause;
        if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (this instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
            throw kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(exception$iv, this);
        }
        throw exception$iv;
    }

    private final kotlinx.coroutines.DisposableHandle installParentHandle() {
        kotlinx.coroutines.Job parent = (kotlinx.coroutines.Job) getContext().get(kotlinx.coroutines.Job.INSTANCE);
        if (parent == null) {
            return null;
        }
        kotlinx.coroutines.CompletionHandlerBase $this$asHandler$iv = new kotlinx.coroutines.ChildContinuation(this);
        kotlinx.coroutines.DisposableHandle handle = kotlinx.coroutines.Job.DefaultImpls.invokeOnCompletion$default(parent, true, false, $this$asHandler$iv, 2, null);
        this._parentHandle.compareAndSet(null, handle);
        return handle;
    }

    public final void releaseClaimedReusableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        java.lang.Throwable cancellationCause;
        kotlin.coroutines.Continuation<T> continuation = this.delegate;
        kotlinx.coroutines.internal.DispatchedContinuation dispatchedContinuation = continuation instanceof kotlinx.coroutines.internal.DispatchedContinuation ? (kotlinx.coroutines.internal.DispatchedContinuation) continuation : null;
        if (dispatchedContinuation == null || (cancellationCause = dispatchedContinuation.tryReleaseClaimedContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(this)) == null) {
            return;
        }
        detachChild$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        cancel(cancellationCause);
    }

    @Override // kotlin.coroutines.Continuation
    public void resumeWith(java.lang.Object result) {
        resumeImpl$default(this, kotlinx.coroutines.CompletionStateKt.toState(result, this), this.resumeMode, null, 4, null);
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public void resume(T value, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> onCancellation) {
        resumeImpl(value, this.resumeMode, onCancellation);
    }

    @Override // kotlinx.coroutines.Waiter
    public void invokeOnCancellation(kotlinx.coroutines.internal.Segment<?> segment, int index) {
        int cur$iv;
        int upd$iv;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(segment, "segment");
        kotlinx.atomicfu.AtomicInt $this$update$iv = this._decisionAndIndex;
        do {
            cur$iv = $this$update$iv.getValue();
            int $this$index$iv = cur$iv & 536870911;
            if (!($this$index$iv == 536870911)) {
                throw new java.lang.IllegalStateException("invokeOnCancellation should be called at most once".toString());
            }
            int decision$iv = cur$iv >> 29;
            upd$iv = (decision$iv << 29) + index;
        } while (!$this$update$iv.compareAndSet(cur$iv, upd$iv));
        invokeOnCancellationImpl(segment);
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public void invokeOnCancellation(kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> handler) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(handler, "handler");
        kotlinx.coroutines.CancelHandler cancelHandler = makeCancelHandler(handler);
        invokeOnCancellationImpl(cancelHandler);
    }

    private final void invokeOnCancellationImpl(java.lang.Object handler) {
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!((handler instanceof kotlinx.coroutines.CancelHandler) || (handler instanceof kotlinx.coroutines.internal.Segment))) {
                throw new java.lang.AssertionError();
            }
        }
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._state;
        while (true) {
            java.lang.Object state = atomicRef.getValue();
            if (state instanceof kotlinx.coroutines.Active) {
                if (this._state.compareAndSet(state, handler)) {
                    return;
                }
            } else {
                if (state instanceof kotlinx.coroutines.CancelHandler ? true : state instanceof kotlinx.coroutines.internal.Segment) {
                    multipleHandlersError(handler, state);
                } else {
                    if (state instanceof kotlinx.coroutines.CompletedExceptionally) {
                        if (!((kotlinx.coroutines.CompletedExceptionally) state).makeHandled()) {
                            multipleHandlersError(handler, state);
                        }
                        if (state instanceof kotlinx.coroutines.CancelledContinuation) {
                            kotlinx.coroutines.CompletedExceptionally completedExceptionally = state instanceof kotlinx.coroutines.CompletedExceptionally ? (kotlinx.coroutines.CompletedExceptionally) state : null;
                            java.lang.Throwable cause = completedExceptionally != null ? completedExceptionally.cause : null;
                            if (handler instanceof kotlinx.coroutines.CancelHandler) {
                                callCancelHandler((kotlinx.coroutines.CancelHandler) handler, cause);
                                return;
                            } else {
                                kotlin.jvm.internal.Intrinsics.checkNotNull(handler, "null cannot be cast to non-null type kotlinx.coroutines.internal.Segment<*>");
                                callSegmentOnCancellation((kotlinx.coroutines.internal.Segment) handler, cause);
                                return;
                            }
                        }
                        return;
                    }
                    if (state instanceof kotlinx.coroutines.CompletedContinuation) {
                        if (((kotlinx.coroutines.CompletedContinuation) state).cancelHandler != null) {
                            multipleHandlersError(handler, state);
                        }
                        if (handler instanceof kotlinx.coroutines.internal.Segment) {
                            return;
                        }
                        kotlin.jvm.internal.Intrinsics.checkNotNull(handler, "null cannot be cast to non-null type kotlinx.coroutines.CancelHandler");
                        if (((kotlinx.coroutines.CompletedContinuation) state).getCancelled()) {
                            callCancelHandler((kotlinx.coroutines.CancelHandler) handler, ((kotlinx.coroutines.CompletedContinuation) state).cancelCause);
                            return;
                        } else {
                            kotlinx.coroutines.CompletedContinuation update = kotlinx.coroutines.CompletedContinuation.copy$default((kotlinx.coroutines.CompletedContinuation) state, null, (kotlinx.coroutines.CancelHandler) handler, null, null, null, 29, null);
                            if (this._state.compareAndSet(state, update)) {
                                return;
                            }
                        }
                    } else {
                        if (handler instanceof kotlinx.coroutines.internal.Segment) {
                            return;
                        }
                        kotlin.jvm.internal.Intrinsics.checkNotNull(handler, "null cannot be cast to non-null type kotlinx.coroutines.CancelHandler");
                        kotlinx.coroutines.CompletedContinuation update2 = new kotlinx.coroutines.CompletedContinuation(state, (kotlinx.coroutines.CancelHandler) handler, null, null, null, 28, null);
                        if (this._state.compareAndSet(state, update2)) {
                            return;
                        }
                    }
                }
            }
        }
    }

    private final void multipleHandlersError(java.lang.Object handler, java.lang.Object state) {
        throw new java.lang.IllegalStateException(("It's prohibited to register multiple handlers, tried to register " + handler + ", already has " + state).toString());
    }

    private final kotlinx.coroutines.CancelHandler makeCancelHandler(kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> handler) {
        return handler instanceof kotlinx.coroutines.CancelHandler ? (kotlinx.coroutines.CancelHandler) handler : new kotlinx.coroutines.InvokeOnCancel(handler);
    }

    private final void dispatchResume(int mode) {
        if (tryResume()) {
            return;
        }
        kotlinx.coroutines.DispatchedTaskKt.dispatch(this, mode);
    }

    private final java.lang.Object resumedState(kotlinx.coroutines.NotCompleted state, java.lang.Object proposedUpdate, int resumeMode, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> onCancellation, java.lang.Object idempotent) {
        if (proposedUpdate instanceof kotlinx.coroutines.CompletedExceptionally) {
            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                if ((idempotent == null ? 1 : 0) == 0) {
                    throw new java.lang.AssertionError();
                }
            }
            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                if (!(onCancellation == null)) {
                    throw new java.lang.AssertionError();
                }
            }
        } else if ((kotlinx.coroutines.DispatchedTaskKt.isCancellableMode(resumeMode) || idempotent != null) && (onCancellation != null || (state instanceof kotlinx.coroutines.CancelHandler) || idempotent != null)) {
            return new kotlinx.coroutines.CompletedContinuation(proposedUpdate, state instanceof kotlinx.coroutines.CancelHandler ? (kotlinx.coroutines.CancelHandler) state : null, onCancellation, idempotent, null, 16, null);
        }
        return proposedUpdate;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void resumeImpl$default(kotlinx.coroutines.CancellableContinuationImpl cancellableContinuationImpl, java.lang.Object obj, int i, kotlin.jvm.functions.Function1 function1, int i2, java.lang.Object obj2) {
        if (obj2 != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: resumeImpl");
        }
        if ((i2 & 4) != 0) {
            function1 = null;
        }
        cancellableContinuationImpl.resumeImpl(obj, i, function1);
    }

    private final void resumeImpl(java.lang.Object proposedUpdate, int resumeMode, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> onCancellation) {
        java.lang.Object state;
        java.lang.Object update;
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._state;
        do {
            state = atomicRef.getValue();
            if (state instanceof kotlinx.coroutines.NotCompleted) {
                update = resumedState((kotlinx.coroutines.NotCompleted) state, proposedUpdate, resumeMode, onCancellation, null);
            } else {
                if ((state instanceof kotlinx.coroutines.CancelledContinuation) && ((kotlinx.coroutines.CancelledContinuation) state).makeResumed()) {
                    if (onCancellation != null) {
                        callOnCancellation(onCancellation, ((kotlinx.coroutines.CancelledContinuation) state).cause);
                        return;
                    }
                    return;
                }
                alreadyResumedError(proposedUpdate);
                throw new kotlin.KotlinNothingValueException();
            }
        } while (!this._state.compareAndSet(state, update));
        detachChildIfNonResuable();
        dispatchResume(resumeMode);
    }

    private final kotlinx.coroutines.internal.Symbol tryResumeImpl(java.lang.Object proposedUpdate, java.lang.Object idempotent, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> onCancellation) {
        java.lang.Object state;
        java.lang.Object update;
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._state;
        do {
            state = atomicRef.getValue();
            if (state instanceof kotlinx.coroutines.NotCompleted) {
                update = resumedState((kotlinx.coroutines.NotCompleted) state, proposedUpdate, this.resumeMode, onCancellation, idempotent);
            } else {
                if (!(state instanceof kotlinx.coroutines.CompletedContinuation) || idempotent == null || ((kotlinx.coroutines.CompletedContinuation) state).idempotentResume != idempotent) {
                    return null;
                }
                if (!kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() || kotlin.jvm.internal.Intrinsics.areEqual(((kotlinx.coroutines.CompletedContinuation) state).result, proposedUpdate)) {
                    return kotlinx.coroutines.CancellableContinuationImplKt.RESUME_TOKEN;
                }
                throw new java.lang.AssertionError();
            }
        } while (!this._state.compareAndSet(state, update));
        detachChildIfNonResuable();
        return kotlinx.coroutines.CancellableContinuationImplKt.RESUME_TOKEN;
    }

    private final java.lang.Void alreadyResumedError(java.lang.Object proposedUpdate) {
        throw new java.lang.IllegalStateException(("Already resumed, but proposed with update " + proposedUpdate).toString());
    }

    private final void detachChildIfNonResuable() {
        if (!isReusable()) {
            detachChild$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        }
    }

    public final void detachChild$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        kotlinx.coroutines.DisposableHandle handle = getParentHandle();
        if (handle == null) {
            return;
        }
        handle.dispose();
        this._parentHandle.setValue(kotlinx.coroutines.NonDisposableHandle.INSTANCE);
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public java.lang.Object tryResume(T value, java.lang.Object idempotent) {
        return tryResumeImpl(value, idempotent, null);
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public java.lang.Object tryResume(T value, java.lang.Object idempotent, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> onCancellation) {
        return tryResumeImpl(value, idempotent, onCancellation);
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public java.lang.Object tryResumeWithException(java.lang.Throwable exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        return tryResumeImpl(new kotlinx.coroutines.CompletedExceptionally(exception, false, 2, null), null, null);
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public void completeResume(java.lang.Object token) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(token, "token");
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(token == kotlinx.coroutines.CancellableContinuationImplKt.RESUME_TOKEN)) {
                throw new java.lang.AssertionError();
            }
        }
        dispatchResume(this.resumeMode);
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public void resumeUndispatched(kotlinx.coroutines.CoroutineDispatcher $this$resumeUndispatched, T t) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$resumeUndispatched, "<this>");
        kotlin.coroutines.Continuation<T> continuation = this.delegate;
        kotlinx.coroutines.internal.DispatchedContinuation dc = continuation instanceof kotlinx.coroutines.internal.DispatchedContinuation ? (kotlinx.coroutines.internal.DispatchedContinuation) continuation : null;
        resumeImpl$default(this, t, (dc != null ? dc.dispatcher : null) == $this$resumeUndispatched ? 4 : this.resumeMode, null, 4, null);
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public void resumeUndispatchedWithException(kotlinx.coroutines.CoroutineDispatcher $this$resumeUndispatchedWithException, java.lang.Throwable exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$resumeUndispatchedWithException, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        kotlin.coroutines.Continuation<T> continuation = this.delegate;
        kotlinx.coroutines.internal.DispatchedContinuation dc = continuation instanceof kotlinx.coroutines.internal.DispatchedContinuation ? (kotlinx.coroutines.internal.DispatchedContinuation) continuation : null;
        resumeImpl$default(this, new kotlinx.coroutines.CompletedExceptionally(exception, false, 2, null), (dc != null ? dc.dispatcher : null) == $this$resumeUndispatchedWithException ? 4 : this.resumeMode, null, 4, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.DispatchedTask
    public <T> T getSuccessfulResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object state) {
        return state instanceof kotlinx.coroutines.CompletedContinuation ? (T) ((kotlinx.coroutines.CompletedContinuation) state).result : state;
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public java.lang.Throwable getExceptionalResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object state) {
        java.lang.Throwable it = super.getExceptionalResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(state);
        if (it == null) {
            return null;
        }
        kotlin.coroutines.Continuation<T> continuation = this.delegate;
        if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
            return kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(it, (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation);
        }
        return it;
    }

    public java.lang.String toString() {
        return nameString() + "(" + kotlinx.coroutines.DebugStringsKt.toDebugString(this.delegate) + "){" + getStateDebugRepresentation() + "}@" + kotlinx.coroutines.DebugStringsKt.getHexAddress(this);
    }

    protected java.lang.String nameString() {
        return "CancellableContinuation";
    }
}
