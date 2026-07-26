package kotlinx.coroutines.sync;

/* JADX INFO: compiled from: Semaphore.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\b\u0010\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\u000e\u0010\u0016\u001a\u00020\u0014H\u0096@¢\u0006\u0002\u0010\u0017Jb\u0010\u0016\u001a\u00020\u0014\"\u0004\b\u0000\u0010\u00182\u0006\u0010\u0019\u001a\u0002H\u00182!\u0010\u001a\u001a\u001d\u0012\u0013\u0012\u0011H\u0018¢\u0006\f\b\u001b\u0012\b\b\u001c\u0012\u0004\b\b(\u0019\u0012\u0004\u0012\u00020\u001d0\u00122!\u0010\u001e\u001a\u001d\u0012\u0013\u0012\u0011H\u0018¢\u0006\f\b\u001b\u0012\b\b\u001c\u0012\u0004\b\b(\u0019\u0012\u0004\u0012\u00020\u00140\u0012H\u0083\b¢\u0006\u0002\u0010\u001fJ\u0016\u0010\u0016\u001a\u00020\u00142\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00140 H\u0005J\u000e\u0010!\u001a\u00020\u0014H\u0082@¢\u0006\u0002\u0010\u0017J\u0010\u0010\"\u001a\u00020\u001d2\u0006\u0010\u0019\u001a\u00020#H\u0002J\b\u0010$\u001a\u00020\u0014H\u0002J\b\u0010%\u001a\u00020\u0003H\u0002J\u001e\u0010&\u001a\u00020\u00142\n\u0010'\u001a\u0006\u0012\u0002\b\u00030(2\b\u0010)\u001a\u0004\u0018\u00010*H\u0004J\b\u0010+\u001a\u00020\u0014H\u0016J\b\u0010,\u001a\u00020\u001dH\u0016J\b\u0010-\u001a\u00020\u001dH\u0002J\f\u0010.\u001a\u00020\u001d*\u00020*H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\u00038VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00140\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006/"}, d2 = {"Lkotlinx/coroutines/sync/SemaphoreImpl;", "Lkotlinx/coroutines/sync/Semaphore;", "permits", "", "acquiredPermits", "(II)V", "_availablePermits", "Lkotlinx/atomicfu/AtomicInt;", "availablePermits", "getAvailablePermits", "()I", "deqIdx", "Lkotlinx/atomicfu/AtomicLong;", "enqIdx", "head", "Lkotlinx/atomicfu/AtomicRef;", "Lkotlinx/coroutines/sync/SemaphoreSegment;", "onCancellationRelease", "Lkotlin/Function1;", "", "", "tail", "acquire", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "W", "waiter", "suspend", "Lkotlin/ParameterName;", "name", "", "onAcquired", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "Lkotlinx/coroutines/CancellableContinuation;", "acquireSlowPath", "addAcquireToQueue", "Lkotlinx/coroutines/Waiter;", "coerceAvailablePermitsAtMaximum", "decPermits", "onAcquireRegFunction", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "ignoredParam", "", "release", "tryAcquire", "tryResumeNextFromQueue", "tryResumeAcquire", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class SemaphoreImpl implements kotlinx.coroutines.sync.Semaphore {
    private final kotlinx.atomicfu.AtomicInt _availablePermits;
    private final kotlinx.atomicfu.AtomicLong deqIdx = kotlinx.atomicfu.AtomicFU.atomic(0L);
    private final kotlinx.atomicfu.AtomicLong enqIdx = kotlinx.atomicfu.AtomicFU.atomic(0L);
    private final kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.sync.SemaphoreSegment> head;
    private final kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> onCancellationRelease;
    private final int permits;
    private final kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.sync.SemaphoreSegment> tail;

    @Override // kotlinx.coroutines.sync.Semaphore
    public java.lang.Object acquire(kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return acquire$suspendImpl(this, continuation);
    }

    public SemaphoreImpl(int permits, int acquiredPermits) {
        this.permits = permits;
        if (!(this.permits > 0)) {
            throw new java.lang.IllegalArgumentException(("Semaphore should have at least 1 permit, but had " + this.permits).toString());
        }
        if (!(acquiredPermits >= 0 && acquiredPermits <= this.permits)) {
            throw new java.lang.IllegalArgumentException(("The number of acquired permits should be in 0.." + this.permits).toString());
        }
        kotlinx.coroutines.sync.SemaphoreSegment s = new kotlinx.coroutines.sync.SemaphoreSegment(0L, null, 2);
        this.head = kotlinx.atomicfu.AtomicFU.atomic(s);
        this.tail = kotlinx.atomicfu.AtomicFU.atomic(s);
        this._availablePermits = kotlinx.atomicfu.AtomicFU.atomic(this.permits - acquiredPermits);
        this.onCancellationRelease = new kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>() { // from class: kotlinx.coroutines.sync.SemaphoreImpl$onCancellationRelease$1
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) {
                invoke2(th);
                return kotlin.Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(java.lang.Throwable th) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(th, "<anonymous parameter 0>");
                this.this$0.release();
            }
        };
    }

    @Override // kotlinx.coroutines.sync.Semaphore
    public int getAvailablePermits() {
        return java.lang.Math.max(this._availablePermits.getValue(), 0);
    }

    @Override // kotlinx.coroutines.sync.Semaphore
    public boolean tryAcquire() {
        while (true) {
            int p = this._availablePermits.getValue();
            if (p > this.permits) {
                coerceAvailablePermitsAtMaximum();
            } else {
                if (p <= 0) {
                    return false;
                }
                if (this._availablePermits.compareAndSet(p, p - 1)) {
                    return true;
                }
            }
        }
    }

    static /* synthetic */ java.lang.Object acquire$suspendImpl(kotlinx.coroutines.sync.SemaphoreImpl $this, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        java.lang.Object objAcquireSlowPath;
        int p = $this.decPermits();
        return (p <= 0 && (objAcquireSlowPath = $this.acquireSlowPath(continuation)) == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) ? objAcquireSlowPath : kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object acquireSlowPath(kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlinx.coroutines.CancellableContinuationImpl cancellable$iv = kotlinx.coroutines.CancellableContinuationKt.getOrCreateCancellableContinuation(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation));
        try {
            if (!addAcquireToQueue(cancellable$iv)) {
                acquire((kotlinx.coroutines.CancellableContinuation<? super kotlin.Unit>) cancellable$iv);
            }
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

    /* JADX WARN: Incorrect condition in loop: B:4:0x000d */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final void acquire(kotlinx.coroutines.CancellableContinuation<? super kotlin.Unit> r8) {
        /*
            r7 = this;
            java.lang.String r0 = "waiter"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r8, r0)
            r0 = r7
            r1 = 0
        L8:
            int r2 = r0.decPermits()
            if (r2 <= 0) goto L1a
            r3 = r8
            r4 = 0
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> r6 = r7.onCancellationRelease
            r3.resume(r5, r6)
            goto L25
        L1a:
            r3 = r8
            r4 = 0
            r5 = r3
            kotlinx.coroutines.Waiter r5 = (kotlinx.coroutines.Waiter) r5
            boolean r3 = r7.addAcquireToQueue(r5)
            if (r3 == 0) goto L8
        L25:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.sync.SemaphoreImpl.acquire(kotlinx.coroutines.CancellableContinuation):void");
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x0006 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final <W> void acquire(W r4, kotlin.jvm.functions.Function1<? super W, java.lang.Boolean> r5, kotlin.jvm.functions.Function1<? super W, kotlin.Unit> r6) {
        /*
            r3 = this;
            r0 = 0
        L1:
            int r1 = r3.decPermits()
            if (r1 <= 0) goto Lc
            r6.invoke(r4)
            return
        Lc:
            java.lang.Object r2 = r5.invoke(r4)
            java.lang.Boolean r2 = (java.lang.Boolean) r2
            boolean r2 = r2.booleanValue()
            if (r2 == 0) goto L1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.sync.SemaphoreImpl.acquire(java.lang.Object, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function1):void");
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x000d */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final void onAcquireRegFunction(kotlinx.coroutines.selects.SelectInstance<?> r7, java.lang.Object r8) {
        /*
            r6 = this;
            java.lang.String r0 = "select"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r0)
            r0 = r6
            r1 = 0
        L8:
            int r2 = r0.decPermits()
            if (r2 <= 0) goto L18
            r3 = r7
            r4 = 0
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            r3.selectInRegistrationPhase(r5)
            goto L23
        L18:
            r3 = r7
            r4 = 0
            r5 = r3
            kotlinx.coroutines.Waiter r5 = (kotlinx.coroutines.Waiter) r5
            boolean r3 = r6.addAcquireToQueue(r5)
            if (r3 == 0) goto L8
        L23:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.sync.SemaphoreImpl.onAcquireRegFunction(kotlinx.coroutines.selects.SelectInstance, java.lang.Object):void");
    }

    private final int decPermits() {
        int p;
        do {
            p = this._availablePermits.getAndDecrement();
        } while (p > this.permits);
        return p;
    }

    @Override // kotlinx.coroutines.sync.Semaphore
    public void release() {
        do {
            int p = this._availablePermits.getAndIncrement();
            if (p >= this.permits) {
                coerceAvailablePermitsAtMaximum();
                throw new java.lang.IllegalStateException(("The number of released permits cannot be greater than " + this.permits).toString());
            }
            if (p >= 0) {
                return;
            }
        } while (!tryResumeNextFromQueue());
    }

    private final void coerceAvailablePermitsAtMaximum() {
        int cur;
        do {
            cur = this._availablePermits.getValue();
            if (cur <= this.permits) {
                return;
            }
        } while (!this._availablePermits.compareAndSet(cur, this.permits));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean addAcquireToQueue(kotlinx.coroutines.Waiter waiter) {
        java.lang.Object s$iv;
        kotlin.reflect.KFunction createNewSegment;
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.sync.SemaphoreSegment> atomicRef;
        boolean z;
        kotlinx.coroutines.sync.SemaphoreSegment curTail = this.tail.getValue();
        long enqIdx = this.enqIdx.getAndIncrement();
        kotlin.reflect.KFunction createNewSegment2 = kotlinx.coroutines.sync.SemaphoreImpl$addAcquireToQueue$createNewSegment$1.INSTANCE;
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.sync.SemaphoreSegment> atomicRef2 = this.tail;
        long id$iv = enqIdx / ((long) kotlinx.coroutines.sync.SemaphoreKt.SEGMENT_SIZE);
        while (true) {
            s$iv = kotlinx.coroutines.internal.ConcurrentLinkedListKt.findSegmentInternal(curTail, id$iv, (kotlin.jvm.functions.Function2) createNewSegment2);
            if (kotlinx.coroutines.internal.SegmentOrClosed.m12872isClosedimpl(s$iv)) {
                break;
            }
            kotlinx.coroutines.internal.Segment to$iv$iv = kotlinx.coroutines.internal.SegmentOrClosed.m12870getSegmentimpl(s$iv);
            kotlinx.atomicfu.AtomicRef $this$moveForward$iv$iv = atomicRef2;
            while (true) {
                kotlinx.coroutines.internal.Segment cur$iv$iv = (kotlinx.coroutines.internal.Segment) $this$moveForward$iv$iv.getValue();
                kotlinx.atomicfu.AtomicRef $this$moveForward$iv$iv2 = $this$moveForward$iv$iv;
                createNewSegment = createNewSegment2;
                atomicRef = atomicRef2;
                if (cur$iv$iv.id >= to$iv$iv.id) {
                    z = true;
                    break;
                }
                if (!to$iv$iv.tryIncPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    z = false;
                    break;
                }
                if ($this$moveForward$iv$iv2.compareAndSet(cur$iv$iv, to$iv$iv)) {
                    if (cur$iv$iv.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        cur$iv$iv.remove();
                    }
                    z = true;
                } else {
                    if (to$iv$iv.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        to$iv$iv.remove();
                    }
                    $this$moveForward$iv$iv = $this$moveForward$iv$iv2;
                    createNewSegment2 = createNewSegment;
                    atomicRef2 = atomicRef;
                }
            }
            if (z) {
                break;
            }
            createNewSegment2 = createNewSegment;
            atomicRef2 = atomicRef;
        }
        kotlinx.coroutines.sync.SemaphoreSegment segment = (kotlinx.coroutines.sync.SemaphoreSegment) kotlinx.coroutines.internal.SegmentOrClosed.m12870getSegmentimpl(s$iv);
        int i = (int) (enqIdx % ((long) kotlinx.coroutines.sync.SemaphoreKt.SEGMENT_SIZE));
        if (!segment.getAcquirers().get(i).compareAndSet(null, waiter)) {
            java.lang.Object expected$iv = kotlinx.coroutines.sync.SemaphoreKt.PERMIT;
            java.lang.Object value$iv = kotlinx.coroutines.sync.SemaphoreKt.TAKEN;
            if (!segment.getAcquirers().get(i).compareAndSet(expected$iv, value$iv)) {
                if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                    if (segment.getAcquirers().get(i).getValue() == kotlinx.coroutines.sync.SemaphoreKt.BROKEN) {
                        return false;
                    }
                    throw new java.lang.AssertionError();
                }
                return false;
            }
            if (waiter instanceof kotlinx.coroutines.CancellableContinuation) {
                kotlin.jvm.internal.Intrinsics.checkNotNull(waiter, "null cannot be cast to non-null type kotlinx.coroutines.CancellableContinuation<kotlin.Unit>");
                ((kotlinx.coroutines.CancellableContinuation) waiter).resume(kotlin.Unit.INSTANCE, this.onCancellationRelease);
                return true;
            }
            if (waiter instanceof kotlinx.coroutines.selects.SelectInstance) {
                ((kotlinx.coroutines.selects.SelectInstance) waiter).selectInRegistrationPhase(kotlin.Unit.INSTANCE);
                return true;
            }
            throw new java.lang.IllegalStateException(("unexpected: " + waiter).toString());
        }
        waiter.invokeOnCancellation(segment, i);
        return true;
    }

    private final boolean tryResumeNextFromQueue() {
        java.lang.Object s$iv;
        kotlin.reflect.KFunction createNewSegment;
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.sync.SemaphoreSegment> atomicRef;
        boolean z;
        kotlinx.coroutines.sync.SemaphoreSegment curHead = this.head.getValue();
        long deqIdx = this.deqIdx.getAndIncrement();
        long id = deqIdx / ((long) kotlinx.coroutines.sync.SemaphoreKt.SEGMENT_SIZE);
        kotlin.reflect.KFunction createNewSegment2 = kotlinx.coroutines.sync.SemaphoreImpl$tryResumeNextFromQueue$createNewSegment$1.INSTANCE;
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.sync.SemaphoreSegment> atomicRef2 = this.head;
        while (true) {
            s$iv = kotlinx.coroutines.internal.ConcurrentLinkedListKt.findSegmentInternal(curHead, id, (kotlin.jvm.functions.Function2) createNewSegment2);
            if (kotlinx.coroutines.internal.SegmentOrClosed.m12872isClosedimpl(s$iv)) {
                break;
            }
            kotlinx.coroutines.internal.Segment to$iv$iv = kotlinx.coroutines.internal.SegmentOrClosed.m12870getSegmentimpl(s$iv);
            kotlinx.atomicfu.AtomicRef $this$moveForward$iv$iv = atomicRef2;
            while (true) {
                kotlinx.coroutines.internal.Segment cur$iv$iv = (kotlinx.coroutines.internal.Segment) $this$moveForward$iv$iv.getValue();
                createNewSegment = createNewSegment2;
                atomicRef = atomicRef2;
                if (cur$iv$iv.id >= to$iv$iv.id) {
                    z = true;
                    break;
                }
                if (!to$iv$iv.tryIncPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    z = false;
                    break;
                }
                if ($this$moveForward$iv$iv.compareAndSet(cur$iv$iv, to$iv$iv)) {
                    if (cur$iv$iv.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        cur$iv$iv.remove();
                    }
                    z = true;
                } else {
                    if (to$iv$iv.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        to$iv$iv.remove();
                    }
                    createNewSegment2 = createNewSegment;
                    atomicRef2 = atomicRef;
                }
            }
            if (z) {
                break;
            }
            createNewSegment2 = createNewSegment;
            atomicRef2 = atomicRef;
        }
        kotlinx.coroutines.sync.SemaphoreSegment segment = (kotlinx.coroutines.sync.SemaphoreSegment) kotlinx.coroutines.internal.SegmentOrClosed.m12870getSegmentimpl(s$iv);
        segment.cleanPrev();
        if (segment.id > id) {
            return false;
        }
        int i = (int) (deqIdx % ((long) kotlinx.coroutines.sync.SemaphoreKt.SEGMENT_SIZE));
        java.lang.Object value$iv = segment.getAcquirers().get(i).getAndSet(kotlinx.coroutines.sync.SemaphoreKt.PERMIT);
        if (value$iv == null) {
            int i2 = kotlinx.coroutines.sync.SemaphoreKt.MAX_SPIN_CYCLES;
            for (int i3 = 0; i3 < i2; i3++) {
                if (segment.getAcquirers().get(i).getValue() == kotlinx.coroutines.sync.SemaphoreKt.TAKEN) {
                    return true;
                }
            }
            java.lang.Object expected$iv = kotlinx.coroutines.sync.SemaphoreKt.PERMIT;
            return !segment.getAcquirers().get(i).compareAndSet(expected$iv, kotlinx.coroutines.sync.SemaphoreKt.BROKEN);
        }
        if (value$iv == kotlinx.coroutines.sync.SemaphoreKt.CANCELLED) {
            return false;
        }
        return tryResumeAcquire(value$iv);
    }

    private final boolean tryResumeAcquire(java.lang.Object $this$tryResumeAcquire) {
        if ($this$tryResumeAcquire instanceof kotlinx.coroutines.CancellableContinuation) {
            kotlin.jvm.internal.Intrinsics.checkNotNull($this$tryResumeAcquire, "null cannot be cast to non-null type kotlinx.coroutines.CancellableContinuation<kotlin.Unit>");
            java.lang.Object token = ((kotlinx.coroutines.CancellableContinuation) $this$tryResumeAcquire).tryResume(kotlin.Unit.INSTANCE, null, this.onCancellationRelease);
            if (token == null) {
                return false;
            }
            ((kotlinx.coroutines.CancellableContinuation) $this$tryResumeAcquire).completeResume(token);
            return true;
        }
        if ($this$tryResumeAcquire instanceof kotlinx.coroutines.selects.SelectInstance) {
            return ((kotlinx.coroutines.selects.SelectInstance) $this$tryResumeAcquire).trySelect(this, kotlin.Unit.INSTANCE);
        }
        throw new java.lang.IllegalStateException(("unexpected: " + $this$tryResumeAcquire).toString());
    }
}
