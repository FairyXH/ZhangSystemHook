package kotlinx.coroutines.sync;

/* JADX INFO: compiled from: Mutex.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000.\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0010\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r\u001a?\u0010\u000e\u001a\u0002H\u000f\"\u0004\b\u0000\u0010\u000f*\u00020\u000b2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u0013H\u0086H\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001¢\u0006\u0002\u0010\u0014\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"HOLDS_LOCK_ANOTHER_OWNER", "", "HOLDS_LOCK_UNLOCKED", "HOLDS_LOCK_YES", "NO_OWNER", "Lkotlinx/coroutines/internal/Symbol;", "ON_LOCK_ALREADY_LOCKED_BY_OWNER", "TRY_LOCK_ALREADY_LOCKED_BY_OWNER", "TRY_LOCK_FAILED", "TRY_LOCK_SUCCESS", "Mutex", "Lkotlinx/coroutines/sync/Mutex;", "locked", "", "withLock", "T", "owner", "", "action", "Lkotlin/Function0;", "(Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class MutexKt {
    private static final int HOLDS_LOCK_ANOTHER_OWNER = 2;
    private static final int HOLDS_LOCK_UNLOCKED = 0;
    private static final int HOLDS_LOCK_YES = 1;
    private static final kotlinx.coroutines.internal.Symbol NO_OWNER = new kotlinx.coroutines.internal.Symbol("NO_OWNER");
    private static final kotlinx.coroutines.internal.Symbol ON_LOCK_ALREADY_LOCKED_BY_OWNER = new kotlinx.coroutines.internal.Symbol("ALREADY_LOCKED_BY_OWNER");
    private static final int TRY_LOCK_ALREADY_LOCKED_BY_OWNER = 2;
    private static final int TRY_LOCK_FAILED = 1;
    private static final int TRY_LOCK_SUCCESS = 0;

    /* JADX INFO: renamed from: kotlinx.coroutines.sync.MutexKt$withLock$1, reason: invalid class name */
    /* JADX INFO: compiled from: Mutex.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 176)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.sync.MutexKt", f = "Mutex.kt", i = {0, 0, 0}, l = {125}, m = "withLock", n = {"$this$withLock", "owner", "action"}, s = {"L$0", "L$1", "L$2"})
    static final class AnonymousClass1<T> extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        java.lang.Object L$0;
        java.lang.Object L$1;
        java.lang.Object L$2;
        int label;
        /* synthetic */ java.lang.Object result;

        AnonymousClass1(kotlin.coroutines.Continuation<? super kotlinx.coroutines.sync.MutexKt.AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return kotlinx.coroutines.sync.MutexKt.withLock(null, null, null, this);
        }
    }

    public static /* synthetic */ kotlinx.coroutines.sync.Mutex Mutex$default(boolean z, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        return Mutex(z);
    }

    public static final kotlinx.coroutines.sync.Mutex Mutex(boolean locked) {
        return new kotlinx.coroutines.sync.MutexImpl(locked);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <T> java.lang.Object withLock(kotlinx.coroutines.sync.Mutex r6, java.lang.Object r7, kotlin.jvm.functions.Function0<? extends T> r8, kotlin.coroutines.Continuation<? super T> r9) throws java.lang.Throwable {
        /*
            boolean r0 = r9 instanceof kotlinx.coroutines.sync.MutexKt.AnonymousClass1
            if (r0 == 0) goto L14
            r0 = r9
            kotlinx.coroutines.sync.MutexKt$withLock$1 r0 = (kotlinx.coroutines.sync.MutexKt.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r9 = r0.label
            int r9 = r9 - r2
            r0.label = r9
            goto L19
        L14:
            kotlinx.coroutines.sync.MutexKt$withLock$1 r0 = new kotlinx.coroutines.sync.MutexKt$withLock$1
            r0.<init>(r9)
        L19:
            r9 = r0
            java.lang.Object r0 = r9.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r9.label
            r3 = 1
            switch(r2) {
                case 0: goto L3d;
                case 1: goto L2e;
                default: goto L26;
            }
        L26:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
            r6.<init>(r7)
            throw r6
        L2e:
            r6 = 0
            java.lang.Object r7 = r9.L$2
            kotlin.jvm.functions.Function0 r7 = (kotlin.jvm.functions.Function0) r7
            java.lang.Object r8 = r9.L$1
            java.lang.Object r1 = r9.L$0
            kotlinx.coroutines.sync.Mutex r1 = (kotlinx.coroutines.sync.Mutex) r1
            kotlin.ResultKt.throwOnFailure(r0)
            goto L56
        L3d:
            kotlin.ResultKt.throwOnFailure(r0)
            r2 = 0
            r9.L$0 = r6
            r9.L$1 = r7
            r9.L$2 = r8
            r9.label = r3
            java.lang.Object r4 = r6.lock(r7, r9)
            if (r4 != r1) goto L51
            return r1
        L51:
            r1 = r6
            r6 = r2
            r5 = r8
            r8 = r7
            r7 = r5
        L56:
            java.lang.Object r2 = r7.invoke()     // Catch: java.lang.Throwable -> L65
            kotlin.jvm.internal.InlineMarker.finallyStart(r3)
            r1.unlock(r8)
            kotlin.jvm.internal.InlineMarker.finallyEnd(r3)
            return r2
        L65:
            r7 = move-exception
            kotlin.jvm.internal.InlineMarker.finallyStart(r3)
            r1.unlock(r8)
            kotlin.jvm.internal.InlineMarker.finallyEnd(r3)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.sync.MutexKt.withLock(kotlinx.coroutines.sync.Mutex, java.lang.Object, kotlin.jvm.functions.Function0, kotlin.coroutines.Continuation):java.lang.Object");
    }

    public static /* synthetic */ java.lang.Object withLock$default(kotlinx.coroutines.sync.Mutex $this$withLock_u24default, java.lang.Object owner, kotlin.jvm.functions.Function0 action, kotlin.coroutines.Continuation $completion, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            owner = null;
        }
        kotlin.jvm.internal.InlineMarker.mark(0);
        $this$withLock_u24default.lock(owner, $completion);
        kotlin.jvm.internal.InlineMarker.mark(1);
        try {
            return action.invoke();
        } finally {
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
            $this$withLock_u24default.unlock(owner);
            kotlin.jvm.internal.InlineMarker.finallyEnd(1);
        }
    }

    private static final <T> java.lang.Object withLock$$forInline(kotlinx.coroutines.sync.Mutex $this$withLock, java.lang.Object owner, kotlin.jvm.functions.Function0<? extends T> function0, kotlin.coroutines.Continuation<? super T> continuation) {
        kotlin.jvm.internal.InlineMarker.mark(0);
        $this$withLock.lock(owner, continuation);
        kotlin.jvm.internal.InlineMarker.mark(1);
        try {
            return function0.invoke();
        } finally {
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
            $this$withLock.unlock(owner);
            kotlin.jvm.internal.InlineMarker.finallyEnd(1);
        }
    }
}
