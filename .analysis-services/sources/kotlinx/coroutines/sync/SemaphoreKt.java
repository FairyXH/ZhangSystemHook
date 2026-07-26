package kotlinx.coroutines.sync;

/* JADX INFO: compiled from: Semaphore.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00042\b\b\u0002\u0010\u000b\u001a\u00020\u0004\u001a\u001a\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\rH\u0002\u001a3\u0010\u0011\u001a\u0002H\u0012\"\u0004\b\u0000\u0010\u0012*\u00020\t2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00120\u0014H\u0086H\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\u0015\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, d2 = {"BROKEN", "Lkotlinx/coroutines/internal/Symbol;", "CANCELLED", "MAX_SPIN_CYCLES", "", "PERMIT", "SEGMENT_SIZE", "TAKEN", "Semaphore", "Lkotlinx/coroutines/sync/Semaphore;", "permits", "acquiredPermits", "createSegment", "Lkotlinx/coroutines/sync/SemaphoreSegment;", "id", "", "prev", "withPermit", "T", "action", "Lkotlin/Function0;", "(Lkotlinx/coroutines/sync/Semaphore;Lkotlin/jvm/functions/Function0;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class SemaphoreKt {
    private static final int MAX_SPIN_CYCLES = kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.semaphore.maxSpinCycles", 100, 0, 0, 12, (java.lang.Object) null);
    private static final kotlinx.coroutines.internal.Symbol PERMIT = new kotlinx.coroutines.internal.Symbol("PERMIT");
    private static final kotlinx.coroutines.internal.Symbol TAKEN = new kotlinx.coroutines.internal.Symbol("TAKEN");
    private static final kotlinx.coroutines.internal.Symbol BROKEN = new kotlinx.coroutines.internal.Symbol("BROKEN");
    private static final kotlinx.coroutines.internal.Symbol CANCELLED = new kotlinx.coroutines.internal.Symbol("CANCELLED");
    private static final int SEGMENT_SIZE = kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.semaphore.segmentSize", 16, 0, 0, 12, (java.lang.Object) null);

    /* JADX INFO: renamed from: kotlinx.coroutines.sync.SemaphoreKt$withPermit$1, reason: invalid class name */
    /* JADX INFO: compiled from: Semaphore.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 176)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.sync.SemaphoreKt", f = "Semaphore.kt", i = {0, 0}, l = {86}, m = "withPermit", n = {"$this$withPermit", "action"}, s = {"L$0", "L$1"})
    static final class AnonymousClass1<T> extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        java.lang.Object L$0;
        java.lang.Object L$1;
        int label;
        /* synthetic */ java.lang.Object result;

        AnonymousClass1(kotlin.coroutines.Continuation<? super kotlinx.coroutines.sync.SemaphoreKt.AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return kotlinx.coroutines.sync.SemaphoreKt.withPermit(null, null, this);
        }
    }

    public static final kotlinx.coroutines.sync.Semaphore Semaphore(int permits, int acquiredPermits) {
        return new kotlinx.coroutines.sync.SemaphoreImpl(permits, acquiredPermits);
    }

    public static /* synthetic */ kotlinx.coroutines.sync.Semaphore Semaphore$default(int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 0;
        }
        return Semaphore(i, i2);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <T> java.lang.Object withPermit(kotlinx.coroutines.sync.Semaphore r5, kotlin.jvm.functions.Function0<? extends T> r6, kotlin.coroutines.Continuation<? super T> r7) throws java.lang.Throwable {
        /*
            boolean r0 = r7 instanceof kotlinx.coroutines.sync.SemaphoreKt.AnonymousClass1
            if (r0 == 0) goto L14
            r0 = r7
            kotlinx.coroutines.sync.SemaphoreKt$withPermit$1 r0 = (kotlinx.coroutines.sync.SemaphoreKt.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r7 = r0.label
            int r7 = r7 - r2
            r0.label = r7
            goto L19
        L14:
            kotlinx.coroutines.sync.SemaphoreKt$withPermit$1 r0 = new kotlinx.coroutines.sync.SemaphoreKt$withPermit$1
            r0.<init>(r7)
        L19:
            r7 = r0
            java.lang.Object r0 = r7.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r7.label
            r3 = 1
            switch(r2) {
                case 0: goto L3b;
                case 1: goto L2e;
                default: goto L26;
            }
        L26:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r6)
            throw r5
        L2e:
            r5 = 0
            java.lang.Object r6 = r7.L$1
            kotlin.jvm.functions.Function0 r6 = (kotlin.jvm.functions.Function0) r6
            java.lang.Object r1 = r7.L$0
            kotlinx.coroutines.sync.Semaphore r1 = (kotlinx.coroutines.sync.Semaphore) r1
            kotlin.ResultKt.throwOnFailure(r0)
            goto L4f
        L3b:
            kotlin.ResultKt.throwOnFailure(r0)
            r2 = 0
            r7.L$0 = r5
            r7.L$1 = r6
            r7.label = r3
            java.lang.Object r4 = r5.acquire(r7)
            if (r4 != r1) goto L4d
            return r1
        L4d:
            r1 = r5
            r5 = r2
        L4f:
            java.lang.Object r2 = r6.invoke()     // Catch: java.lang.Throwable -> L5e
            kotlin.jvm.internal.InlineMarker.finallyStart(r3)
            r1.release()
            kotlin.jvm.internal.InlineMarker.finallyEnd(r3)
            return r2
        L5e:
            r6 = move-exception
            kotlin.jvm.internal.InlineMarker.finallyStart(r3)
            r1.release()
            kotlin.jvm.internal.InlineMarker.finallyEnd(r3)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.sync.SemaphoreKt.withPermit(kotlinx.coroutines.sync.Semaphore, kotlin.jvm.functions.Function0, kotlin.coroutines.Continuation):java.lang.Object");
    }

    private static final <T> java.lang.Object withPermit$$forInline(kotlinx.coroutines.sync.Semaphore $this$withPermit, kotlin.jvm.functions.Function0<? extends T> function0, kotlin.coroutines.Continuation<? super T> continuation) {
        kotlin.jvm.internal.InlineMarker.mark(0);
        $this$withPermit.acquire(continuation);
        kotlin.jvm.internal.InlineMarker.mark(1);
        try {
            return function0.invoke();
        } finally {
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
            $this$withPermit.release();
            kotlin.jvm.internal.InlineMarker.finallyEnd(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final kotlinx.coroutines.sync.SemaphoreSegment createSegment(long id, kotlinx.coroutines.sync.SemaphoreSegment prev) {
        return new kotlinx.coroutines.sync.SemaphoreSegment(id, prev, 0);
    }
}
