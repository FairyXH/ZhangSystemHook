package kotlinx.coroutines.sync;

/* JADX INFO: compiled from: Mutex.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0010\u0018\u00002\u00020\u00012\u00020\u0002:\u0002+,B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\u001d\u001a\u00020\u00042\u0006\u0010\u001b\u001a\u00020\nH\u0016J\u0012\u0010\u001e\u001a\u00020\u001f2\b\u0010\u001b\u001a\u0004\u0018\u00010\nH\u0002J\u0018\u0010 \u001a\u00020\u00192\b\u0010\u001b\u001a\u0004\u0018\u00010\nH\u0096@¢\u0006\u0002\u0010!J\u0018\u0010\"\u001a\u00020\u00192\b\u0010\u001b\u001a\u0004\u0018\u00010\nH\u0082@¢\u0006\u0002\u0010!J\u001e\u0010#\u001a\u0004\u0018\u00010\n2\b\u0010\u001b\u001a\u0004\u0018\u00010\n2\b\u0010$\u001a\u0004\u0018\u00010\nH\u0014J\u001e\u0010%\u001a\u00020\u00192\n\u0010\u0014\u001a\u0006\u0012\u0002\b\u00030\u00112\b\u0010\u001b\u001a\u0004\u0018\u00010\nH\u0014J\b\u0010&\u001a\u00020'H\u0016J\u0012\u0010(\u001a\u00020\u00042\b\u0010\u001b\u001a\u0004\u0018\u00010\nH\u0016J\u0012\u0010)\u001a\u00020\u001f2\b\u0010\u001b\u001a\u0004\u0018\u00010\nH\u0002J\u0012\u0010*\u001a\u00020\u00192\b\u0010\u001b\u001a\u0004\u0018\u00010\nH\u0016R\u0014\u0010\u0006\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R(\u0010\b\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\n\u0012\u0004\u0012\u00020\u00020\t8VX\u0096\u0004¢\u0006\f\u0012\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000eRk\u0010\u000f\u001a_\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\u0011¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0014\u0012\u0015\u0012\u0013\u0018\u00010\n¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0015\u0012\u0015\u0012\u0013\u0018\u00010\n¢\u0006\f\b\u0012\u0012\b\b\u0013\u0012\u0004\b\b(\u0016\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u00190\u00170\u0010j\u0002`\u001aX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u001b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u001cX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006-"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl;", "Lkotlinx/coroutines/sync/SemaphoreImpl;", "Lkotlinx/coroutines/sync/Mutex;", "locked", "", "(Z)V", "isLocked", "()Z", "onLock", "Lkotlinx/coroutines/selects/SelectClause2;", "", "getOnLock$annotations", "()V", "getOnLock", "()Lkotlinx/coroutines/selects/SelectClause2;", "onSelectCancellationUnlockConstructor", "Lkotlin/Function3;", "Lkotlinx/coroutines/selects/SelectInstance;", "Lkotlin/ParameterName;", "name", "select", "param", "internalResult", "Lkotlin/Function1;", "", "", "Lkotlinx/coroutines/selects/OnCancellationConstructor;", "owner", "Lkotlinx/atomicfu/AtomicRef;", "holdsLock", "holdsLockImpl", "", "lock", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "lockSuspend", "onLockProcessResult", "result", "onLockRegFunction", "toString", "", "tryLock", "tryLockImpl", "unlock", "CancellableContinuationWithOwner", "SelectInstanceWithOwner", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class MutexImpl extends kotlinx.coroutines.sync.SemaphoreImpl implements kotlinx.coroutines.sync.Mutex {
    private final kotlin.jvm.functions.Function3<kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, java.lang.Object, kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>> onSelectCancellationUnlockConstructor;
    private final kotlinx.atomicfu.AtomicRef<java.lang.Object> owner;

    public static /* synthetic */ void getOnLock$annotations() {
    }

    @Override // kotlinx.coroutines.sync.Mutex
    public java.lang.Object lock(java.lang.Object obj, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return lock$suspendImpl(this, obj, continuation);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MutexImpl(boolean z) {
        super(1, z ? 1 : 0);
        this.owner = kotlinx.atomicfu.AtomicFU.atomic(z ? null : kotlinx.coroutines.sync.MutexKt.NO_OWNER);
        this.onSelectCancellationUnlockConstructor = new kotlin.jvm.functions.Function3<kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, java.lang.Object, kotlin.jvm.functions.Function1<? super java.lang.Throwable, ? extends kotlin.Unit>>() { // from class: kotlinx.coroutines.sync.MutexImpl$onSelectCancellationUnlockConstructor$1
            {
                super(3);
            }

            @Override // kotlin.jvm.functions.Function3
            public final kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> invoke(kotlinx.coroutines.selects.SelectInstance<?> selectInstance, final java.lang.Object owner, java.lang.Object obj) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selectInstance, "<anonymous parameter 0>");
                final kotlinx.coroutines.sync.MutexImpl mutexImpl = this.this$0;
                return new kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>() { // from class: kotlinx.coroutines.sync.MutexImpl$onSelectCancellationUnlockConstructor$1.1
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(1);
                    }

                    @Override // kotlin.jvm.functions.Function1
                    public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) {
                        invoke2(th);
                        return kotlin.Unit.INSTANCE;
                    }

                    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                    public final void invoke2(java.lang.Throwable it) {
                        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
                        mutexImpl.unlock(owner);
                    }
                };
            }
        };
    }

    @Override // kotlinx.coroutines.sync.Mutex
    public boolean isLocked() {
        return getAvailablePermits() == 0;
    }

    @Override // kotlinx.coroutines.sync.Mutex
    public boolean holdsLock(java.lang.Object owner) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(owner, "owner");
        return holdsLockImpl(owner) == 1;
    }

    private final int holdsLockImpl(java.lang.Object owner) {
        while (isLocked()) {
            java.lang.Object curOwner = this.owner.getValue();
            if (curOwner != kotlinx.coroutines.sync.MutexKt.NO_OWNER) {
                return curOwner == owner ? 1 : 2;
            }
        }
        return 0;
    }

    static /* synthetic */ java.lang.Object lock$suspendImpl(kotlinx.coroutines.sync.MutexImpl $this, java.lang.Object owner, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        java.lang.Object objLockSuspend;
        return (!$this.tryLock(owner) && (objLockSuspend = $this.lockSuspend(owner, continuation)) == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) ? objLockSuspend : kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object lockSuspend(java.lang.Object owner, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlinx.coroutines.CancellableContinuationImpl cancellable$iv = kotlinx.coroutines.CancellableContinuationKt.getOrCreateCancellableContinuation(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation));
        try {
            kotlinx.coroutines.sync.MutexImpl.CancellableContinuationWithOwner contWithOwner = new kotlinx.coroutines.sync.MutexImpl.CancellableContinuationWithOwner(this, cancellable$iv, owner);
            acquire((kotlinx.coroutines.CancellableContinuation<? super kotlin.Unit>) contWithOwner);
            java.lang.Object result = cancellable$iv.getResult();
            if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            return result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? result : kotlin.Unit.INSTANCE;
        } catch (java.lang.Throwable e$iv) {
            cancellable$iv.releaseClaimedReusableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            throw e$iv;
        }
    }

    @Override // kotlinx.coroutines.sync.Mutex
    public boolean tryLock(java.lang.Object owner) {
        switch (tryLockImpl(owner)) {
            case 0:
                return true;
            case 1:
                return false;
            case 2:
                throw new java.lang.IllegalStateException(("This mutex is already locked by the specified owner: " + owner).toString());
            default:
                throw new java.lang.IllegalStateException("unexpected".toString());
        }
    }

    private final int tryLockImpl(java.lang.Object owner) {
        while (true) {
            if (tryAcquire()) {
                if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                    if (!(this.owner.getValue() == kotlinx.coroutines.sync.MutexKt.NO_OWNER)) {
                        throw new java.lang.AssertionError();
                    }
                }
                this.owner.setValue(owner);
                return 0;
            }
            if (owner == null) {
                return 1;
            }
            switch (holdsLockImpl(owner)) {
                case 1:
                    return 2;
                case 2:
                    return 1;
            }
        }
    }

    @Override // kotlinx.coroutines.sync.Mutex
    public void unlock(java.lang.Object owner) {
        while (isLocked()) {
            java.lang.Object curOwner = this.owner.getValue();
            if (curOwner != kotlinx.coroutines.sync.MutexKt.NO_OWNER) {
                if (curOwner == owner || owner == null) {
                    if (this.owner.compareAndSet(curOwner, kotlinx.coroutines.sync.MutexKt.NO_OWNER)) {
                        release();
                        return;
                    }
                } else {
                    throw new java.lang.IllegalStateException(("This mutex is locked by " + curOwner + ", but " + owner + " is expected").toString());
                }
            }
        }
        throw new java.lang.IllegalStateException("This mutex is not locked".toString());
    }

    @Override // kotlinx.coroutines.sync.Mutex
    public kotlinx.coroutines.selects.SelectClause2<java.lang.Object, kotlinx.coroutines.sync.Mutex> getOnLock() {
        kotlinx.coroutines.sync.MutexImpl$onLock$1 mutexImpl$onLock$1 = kotlinx.coroutines.sync.MutexImpl$onLock$1.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(mutexImpl$onLock$1, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'select')] kotlinx.coroutines.selects.SelectInstance<*>, @[ParameterName(name = 'param')] kotlin.Any?, kotlin.Unit>{ kotlinx.coroutines.selects.SelectKt.RegistrationFunction }");
        kotlin.jvm.functions.Function3 function3 = (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(mutexImpl$onLock$1, 3);
        kotlinx.coroutines.sync.MutexImpl$onLock$2 mutexImpl$onLock$2 = kotlinx.coroutines.sync.MutexImpl$onLock$2.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(mutexImpl$onLock$2, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'param')] kotlin.Any?, @[ParameterName(name = 'clauseResult')] kotlin.Any?, kotlin.Any?>{ kotlinx.coroutines.selects.SelectKt.ProcessResultFunction }");
        return new kotlinx.coroutines.selects.SelectClause2Impl(this, function3, (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(mutexImpl$onLock$2, 3), this.onSelectCancellationUnlockConstructor);
    }

    protected void onLockRegFunction(kotlinx.coroutines.selects.SelectInstance<?> select, java.lang.Object owner) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(select, "select");
        if (owner != null && holdsLock(owner)) {
            select.selectInRegistrationPhase(kotlinx.coroutines.sync.MutexKt.ON_LOCK_ALREADY_LOCKED_BY_OWNER);
        } else {
            onAcquireRegFunction(new kotlinx.coroutines.sync.MutexImpl.SelectInstanceWithOwner(this, (kotlinx.coroutines.selects.SelectInstanceInternal) select, owner), owner);
        }
    }

    protected java.lang.Object onLockProcessResult(java.lang.Object owner, java.lang.Object result) {
        if (kotlin.jvm.internal.Intrinsics.areEqual(result, kotlinx.coroutines.sync.MutexKt.ON_LOCK_ALREADY_LOCKED_BY_OWNER)) {
            throw new java.lang.IllegalStateException(("This mutex is already locked by the specified owner: " + owner).toString());
        }
        return this;
    }

    /* JADX INFO: compiled from: Mutex.kt */
    @kotlin.Metadata(d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0082\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003B\u001d\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\u0015\u0010\u0012\u001a\u00020\u000e2\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0096\u0001J\u0011\u0010\u0015\u001a\u00020\u00022\u0006\u0010\u0016\u001a\u00020\u0007H\u0097\u0001J\t\u0010\u0017\u001a\u00020\u0002H\u0097\u0001J2\u0010\u0018\u001a\u00020\u00022'\u0010\u0019\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u0014¢\u0006\f\b\u001b\u0012\b\b\u001c\u0012\u0004\b\b(\u0013\u0012\u0004\u0012\u00020\u00020\u001aj\u0002`\u001dH\u0096\u0001J\u001d\u0010\u0018\u001a\u00020\u00022\n\u0010\u001e\u001a\u0006\u0012\u0002\b\u00030\u001f2\u0006\u0010 \u001a\u00020!H\u0096\u0001J:\u0010\"\u001a\u00020\u00022\u0006\u0010#\u001a\u00020\u00022#\u0010$\u001a\u001f\u0012\u0013\u0012\u00110\u0014¢\u0006\f\b\u001b\u0012\b\b\u001c\u0012\u0004\b\b(\u0013\u0012\u0004\u0012\u00020\u0002\u0018\u00010\u001aH\u0016¢\u0006\u0002\u0010%J\u001c\u0010&\u001a\u00020\u00022\f\u0010'\u001a\b\u0012\u0004\u0012\u00020\u00020(H\u0096\u0001¢\u0006\u0002\u0010)J$\u0010*\u001a\u0004\u0018\u00010\u00072\u0006\u0010#\u001a\u00020\u00022\n\b\u0002\u0010+\u001a\u0004\u0018\u00010\u0007H\u0097\u0001¢\u0006\u0002\u0010,JF\u0010*\u001a\u0004\u0018\u00010\u00072\u0006\u0010#\u001a\u00020\u00022\b\u0010+\u001a\u0004\u0018\u00010\u00072#\u0010$\u001a\u001f\u0012\u0013\u0012\u00110\u0014¢\u0006\f\b\u001b\u0012\b\b\u001c\u0012\u0004\b\b(\u0013\u0012\u0004\u0012\u00020\u0002\u0018\u00010\u001aH\u0016¢\u0006\u0002\u0010-J\u0013\u0010.\u001a\u0004\u0018\u00010\u00072\u0006\u0010/\u001a\u00020\u0014H\u0097\u0001J\u001a\u00100\u001a\u00020\u0002*\u0002012\u0006\u0010#\u001a\u00020\u0002H\u0097\u0001¢\u0006\u0002\u00102J\u0015\u00103\u001a\u00020\u0002*\u0002012\u0006\u0010/\u001a\u00020\u0014H\u0097\u0001R\u0016\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\t\u001a\u00020\nX\u0096\u0005¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0012\u0010\r\u001a\u00020\u000eX\u0096\u0005¢\u0006\u0006\u001a\u0004\b\r\u0010\u000fR\u0012\u0010\u0010\u001a\u00020\u000eX\u0096\u0005¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u000fR\u0012\u0010\u0011\u001a\u00020\u000eX\u0096\u0005¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u000fR\u0012\u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u00064"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$CancellableContinuationWithOwner;", "Lkotlinx/coroutines/CancellableContinuation;", "", "Lkotlinx/coroutines/Waiter;", "cont", "Lkotlinx/coroutines/CancellableContinuationImpl;", "owner", "", "(Lkotlinx/coroutines/sync/MutexImpl;Lkotlinx/coroutines/CancellableContinuationImpl;Ljava/lang/Object;)V", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "isActive", "", "()Z", "isCancelled", "isCompleted", "cancel", "cause", "", "completeResume", "token", "initCancellability", "invokeOnCancellation", "handler", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "Lkotlinx/coroutines/CompletionHandler;", "segment", "Lkotlinx/coroutines/internal/Segment;", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "resume", "value", "onCancellation", "(Lkotlin/Unit;Lkotlin/jvm/functions/Function1;)V", "resumeWith", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "tryResume", "idempotent", "(Lkotlin/Unit;Ljava/lang/Object;)Ljava/lang/Object;", "(Lkotlin/Unit;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "tryResumeWithException", "exception", "resumeUndispatched", "Lkotlinx/coroutines/CoroutineDispatcher;", "(Lkotlinx/coroutines/CoroutineDispatcher;Lkotlin/Unit;)V", "resumeUndispatchedWithException", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class CancellableContinuationWithOwner implements kotlinx.coroutines.CancellableContinuation<kotlin.Unit>, kotlinx.coroutines.Waiter {
        public final kotlinx.coroutines.CancellableContinuationImpl<kotlin.Unit> cont;
        public final java.lang.Object owner;
        final /* synthetic */ kotlinx.coroutines.sync.MutexImpl this$0;

        @Override // kotlinx.coroutines.CancellableContinuation
        public boolean cancel(java.lang.Throwable cause) {
            return this.cont.cancel(cause);
        }

        @Override // kotlinx.coroutines.CancellableContinuation
        public void completeResume(java.lang.Object token) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(token, "token");
            this.cont.completeResume(token);
        }

        @Override // kotlin.coroutines.Continuation
        public kotlin.coroutines.CoroutineContext getContext() {
            return this.cont.getContext();
        }

        @Override // kotlinx.coroutines.CancellableContinuation
        public void initCancellability() {
            this.cont.initCancellability();
        }

        @Override // kotlinx.coroutines.CancellableContinuation
        public void invokeOnCancellation(kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> handler) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(handler, "handler");
            this.cont.invokeOnCancellation(handler);
        }

        @Override // kotlinx.coroutines.Waiter
        public void invokeOnCancellation(kotlinx.coroutines.internal.Segment<?> segment, int index) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(segment, "segment");
            this.cont.invokeOnCancellation(segment, index);
        }

        @Override // kotlinx.coroutines.CancellableContinuation
        public boolean isActive() {
            return this.cont.isActive();
        }

        @Override // kotlinx.coroutines.CancellableContinuation
        public boolean isCancelled() {
            return this.cont.isCancelled();
        }

        @Override // kotlinx.coroutines.CancellableContinuation
        public boolean isCompleted() {
            return this.cont.isCompleted();
        }

        @Override // kotlinx.coroutines.CancellableContinuation
        public void resumeUndispatched(kotlinx.coroutines.CoroutineDispatcher coroutineDispatcher, kotlin.Unit value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(coroutineDispatcher, "<this>");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
            this.cont.resumeUndispatched(coroutineDispatcher, value);
        }

        @Override // kotlinx.coroutines.CancellableContinuation
        public void resumeUndispatchedWithException(kotlinx.coroutines.CoroutineDispatcher coroutineDispatcher, java.lang.Throwable exception) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(coroutineDispatcher, "<this>");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
            this.cont.resumeUndispatchedWithException(coroutineDispatcher, exception);
        }

        @Override // kotlin.coroutines.Continuation
        public void resumeWith(java.lang.Object result) {
            this.cont.resumeWith(result);
        }

        @Override // kotlinx.coroutines.CancellableContinuation
        public java.lang.Object tryResume(kotlin.Unit value, java.lang.Object idempotent) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
            return this.cont.tryResume(value, idempotent);
        }

        @Override // kotlinx.coroutines.CancellableContinuation
        public java.lang.Object tryResumeWithException(java.lang.Throwable exception) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
            return this.cont.tryResumeWithException(exception);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CancellableContinuationWithOwner(kotlinx.coroutines.sync.MutexImpl this$0, kotlinx.coroutines.CancellableContinuationImpl<? super kotlin.Unit> cont, java.lang.Object owner) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cont, "cont");
            this.this$0 = this$0;
            this.cont = cont;
            this.owner = owner;
        }

        @Override // kotlinx.coroutines.CancellableContinuation
        public /* bridge */ /* synthetic */ void resume(kotlin.Unit unit, kotlin.jvm.functions.Function1 onCancellation) {
            resume2(unit, (kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit>) onCancellation);
        }

        @Override // kotlinx.coroutines.CancellableContinuation
        public /* bridge */ /* synthetic */ java.lang.Object tryResume(kotlin.Unit unit, java.lang.Object idempotent, kotlin.jvm.functions.Function1 onCancellation) {
            return tryResume2(unit, idempotent, (kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit>) onCancellation);
        }

        /* JADX INFO: renamed from: tryResume, reason: avoid collision after fix types in other method */
        public java.lang.Object tryResume2(kotlin.Unit value, java.lang.Object idempotent, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> onCancellation) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
            kotlinx.coroutines.sync.MutexImpl mutexImpl = this.this$0;
            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                if (!(mutexImpl.owner.getValue() == kotlinx.coroutines.sync.MutexKt.NO_OWNER)) {
                    throw new java.lang.AssertionError();
                }
            }
            kotlinx.coroutines.CancellableContinuationImpl<kotlin.Unit> cancellableContinuationImpl = this.cont;
            final kotlinx.coroutines.sync.MutexImpl mutexImpl2 = this.this$0;
            java.lang.Object token = cancellableContinuationImpl.tryResume(value, idempotent, new kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>() { // from class: kotlinx.coroutines.sync.MutexImpl$CancellableContinuationWithOwner$tryResume$token$1
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(1);
                }

                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) {
                    invoke2(th);
                    return kotlin.Unit.INSTANCE;
                }

                /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                public final void invoke2(java.lang.Throwable it) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
                    kotlinx.coroutines.sync.MutexImpl mutexImpl3 = mutexImpl2;
                    kotlinx.coroutines.sync.MutexImpl.CancellableContinuationWithOwner cancellableContinuationWithOwner = this;
                    if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                        java.lang.Object it2 = mutexImpl3.owner.getValue();
                        if (!(it2 == kotlinx.coroutines.sync.MutexKt.NO_OWNER || it2 == cancellableContinuationWithOwner.owner)) {
                            throw new java.lang.AssertionError();
                        }
                    }
                    mutexImpl2.owner.setValue(this.owner);
                    mutexImpl2.unlock(this.owner);
                }
            });
            if (token != null) {
                kotlinx.coroutines.sync.MutexImpl mutexImpl3 = this.this$0;
                if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                    if (!(mutexImpl3.owner.getValue() == kotlinx.coroutines.sync.MutexKt.NO_OWNER)) {
                        throw new java.lang.AssertionError();
                    }
                }
                this.this$0.owner.setValue(this.owner);
            }
            return token;
        }

        /* JADX INFO: renamed from: resume, reason: avoid collision after fix types in other method */
        public void resume2(kotlin.Unit value, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> onCancellation) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
            kotlinx.coroutines.sync.MutexImpl mutexImpl = this.this$0;
            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                if (!(mutexImpl.owner.getValue() == kotlinx.coroutines.sync.MutexKt.NO_OWNER)) {
                    throw new java.lang.AssertionError();
                }
            }
            this.this$0.owner.setValue(this.owner);
            kotlinx.coroutines.CancellableContinuationImpl<kotlin.Unit> cancellableContinuationImpl = this.cont;
            final kotlinx.coroutines.sync.MutexImpl mutexImpl2 = this.this$0;
            cancellableContinuationImpl.resume(value, new kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>() { // from class: kotlinx.coroutines.sync.MutexImpl$CancellableContinuationWithOwner$resume$2
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(1);
                }

                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) {
                    invoke2(th);
                    return kotlin.Unit.INSTANCE;
                }

                /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                public final void invoke2(java.lang.Throwable it) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
                    mutexImpl2.unlock(this.owner);
                }
            });
        }
    }

    /* JADX INFO: compiled from: Mutex.kt */
    @kotlin.Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0082\u0004\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001d\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0096\u0001J\u001d\u0010\u000f\u001a\u00020\f2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0096\u0001J\u0012\u0010\u0014\u001a\u00020\f2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0005H\u0016J\u001a\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00052\b\u0010\u0019\u001a\u0004\u0018\u00010\u0005H\u0016R\u0012\u0010\u0007\u001a\u00020\bX\u0096\u0005¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u00028\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$SelectInstanceWithOwner;", "Q", "Lkotlinx/coroutines/selects/SelectInstanceInternal;", "select", "owner", "", "(Lkotlinx/coroutines/sync/MutexImpl;Lkotlinx/coroutines/selects/SelectInstanceInternal;Ljava/lang/Object;)V", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "disposeOnCompletion", "", "disposableHandle", "Lkotlinx/coroutines/DisposableHandle;", "invokeOnCancellation", "segment", "Lkotlinx/coroutines/internal/Segment;", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "selectInRegistrationPhase", "internalResult", "trySelect", "", "clauseObject", "result", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class SelectInstanceWithOwner<Q> implements kotlinx.coroutines.selects.SelectInstanceInternal<Q> {
        public final java.lang.Object owner;
        public final kotlinx.coroutines.selects.SelectInstanceInternal<Q> select;
        final /* synthetic */ kotlinx.coroutines.sync.MutexImpl this$0;

        @Override // kotlinx.coroutines.selects.SelectInstance
        public void disposeOnCompletion(kotlinx.coroutines.DisposableHandle disposableHandle) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(disposableHandle, "disposableHandle");
            this.select.disposeOnCompletion(disposableHandle);
        }

        @Override // kotlinx.coroutines.selects.SelectInstance
        public kotlin.coroutines.CoroutineContext getContext() {
            return this.select.getContext();
        }

        @Override // kotlinx.coroutines.Waiter
        public void invokeOnCancellation(kotlinx.coroutines.internal.Segment<?> segment, int index) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(segment, "segment");
            this.select.invokeOnCancellation(segment, index);
        }

        public SelectInstanceWithOwner(kotlinx.coroutines.sync.MutexImpl this$0, kotlinx.coroutines.selects.SelectInstanceInternal<Q> select, java.lang.Object owner) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(select, "select");
            this.this$0 = this$0;
            this.select = select;
            this.owner = owner;
        }

        @Override // kotlinx.coroutines.selects.SelectInstance
        public boolean trySelect(java.lang.Object clauseObject, java.lang.Object result) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clauseObject, "clauseObject");
            kotlinx.coroutines.sync.MutexImpl mutexImpl = this.this$0;
            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                if (!(mutexImpl.owner.getValue() == kotlinx.coroutines.sync.MutexKt.NO_OWNER)) {
                    throw new java.lang.AssertionError();
                }
            }
            boolean success = this.select.trySelect(clauseObject, result);
            kotlinx.coroutines.sync.MutexImpl mutexImpl2 = this.this$0;
            if (success) {
                mutexImpl2.owner.setValue(this.owner);
            }
            return success;
        }

        @Override // kotlinx.coroutines.selects.SelectInstance
        public void selectInRegistrationPhase(java.lang.Object internalResult) {
            kotlinx.coroutines.sync.MutexImpl mutexImpl = this.this$0;
            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                if (!(mutexImpl.owner.getValue() == kotlinx.coroutines.sync.MutexKt.NO_OWNER)) {
                    throw new java.lang.AssertionError();
                }
            }
            this.this$0.owner.setValue(this.owner);
            this.select.selectInRegistrationPhase(internalResult);
        }
    }

    public java.lang.String toString() {
        return "Mutex@" + kotlinx.coroutines.DebugStringsKt.getHexAddress(this) + "[isLocked=" + isLocked() + ",owner=" + this.owner.getValue() + "]";
    }
}
