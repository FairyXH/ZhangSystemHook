package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: StateFlow.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u0014\u0010\u0007\u001a\u00020\b2\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\u0002H\u0016J\u000e\u0010\n\u001a\u00020\u000bH\u0086@¢\u0006\u0002\u0010\fJ'\u0010\r\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u000f0\u000e2\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\u0002H\u0016¢\u0006\u0002\u0010\u0010J\u0006\u0010\u0011\u001a\u00020\u000bJ\u0006\u0010\u0012\u001a\u00020\bR\u0016\u0010\u0004\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lkotlinx/coroutines/flow/StateFlowSlot;", "Lkotlinx/coroutines/flow/internal/AbstractSharedFlowSlot;", "Lkotlinx/coroutines/flow/StateFlowImpl;", "()V", "_state", "Lkotlinx/atomicfu/AtomicRef;", "", "allocateLocked", "", "flow", "awaitPending", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "freeLocked", "", "Lkotlin/coroutines/Continuation;", "(Lkotlinx/coroutines/flow/StateFlowImpl;)[Lkotlin/coroutines/Continuation;", "makePending", "takePending", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class StateFlowSlot extends kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot<kotlinx.coroutines.flow.StateFlowImpl<?>> {
    private final kotlinx.atomicfu.AtomicRef<java.lang.Object> _state = kotlinx.atomicfu.AtomicFU.atomic((java.lang.Object) null);

    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot
    public boolean allocateLocked(kotlinx.coroutines.flow.StateFlowImpl<?> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        if (this._state.getValue() != null) {
            return false;
        }
        this._state.setValue(kotlinx.coroutines.flow.StateFlowKt.NONE);
        return true;
    }

    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot
    public kotlin.coroutines.Continuation<kotlin.Unit>[] freeLocked(kotlinx.coroutines.flow.StateFlowImpl<?> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        this._state.setValue(null);
        return kotlinx.coroutines.flow.internal.AbstractSharedFlowKt.EMPTY_RESUMES;
    }

    public final void makePending() {
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this._state;
        while (true) {
            java.lang.Object state = atomicRef.getValue();
            if (state == null || state == kotlinx.coroutines.flow.StateFlowKt.PENDING) {
                return;
            }
            if (state == kotlinx.coroutines.flow.StateFlowKt.NONE) {
                if (this._state.compareAndSet(state, kotlinx.coroutines.flow.StateFlowKt.PENDING)) {
                    return;
                }
            } else if (this._state.compareAndSet(state, kotlinx.coroutines.flow.StateFlowKt.NONE)) {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                ((kotlinx.coroutines.CancellableContinuationImpl) state).resumeWith(kotlin.Result.m11307constructorimpl(kotlin.Unit.INSTANCE));
                return;
            }
        }
    }

    public final boolean takePending() {
        java.lang.Object state = this._state.getAndSet(kotlinx.coroutines.flow.StateFlowKt.NONE);
        kotlin.jvm.internal.Intrinsics.checkNotNull(state);
        if (!kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() || (!(state instanceof kotlinx.coroutines.CancellableContinuationImpl))) {
            return state == kotlinx.coroutines.flow.StateFlowKt.PENDING;
        }
        throw new java.lang.AssertionError();
    }

    public final java.lang.Object awaitPending(kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlinx.coroutines.CancellableContinuationImpl cancellable$iv = new kotlinx.coroutines.CancellableContinuationImpl(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation), 1);
        cancellable$iv.initCancellability();
        kotlinx.coroutines.CancellableContinuationImpl cancellableContinuationImpl = cancellable$iv;
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !(!(this._state.getValue() instanceof kotlinx.coroutines.CancellableContinuationImpl))) {
            throw new java.lang.AssertionError();
        }
        if (!this._state.compareAndSet(kotlinx.coroutines.flow.StateFlowKt.NONE, cancellableContinuationImpl)) {
            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                if (!(this._state.getValue() == kotlinx.coroutines.flow.StateFlowKt.PENDING)) {
                    throw new java.lang.AssertionError();
                }
            }
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            cancellableContinuationImpl.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.Unit.INSTANCE));
        }
        java.lang.Object result = cancellable$iv.getResult();
        if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? result : kotlin.Unit.INSTANCE;
    }
}
