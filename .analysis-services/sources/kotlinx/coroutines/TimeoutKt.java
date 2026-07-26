package kotlinx.coroutines;

/* JADX INFO: compiled from: Timeout.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000D\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0000\u001a\\\u0010\b\u001a\u0004\u0018\u00010\t\"\u0004\b\u0000\u0010\n\"\b\b\u0001\u0010\u000b*\u0002H\n2\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u0002H\n\u0012\u0004\u0012\u0002H\u000b0\f2'\u0010\r\u001a#\b\u0001\u0012\u0004\u0012\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u0010\u0012\u0006\u0012\u0004\u0018\u00010\t0\u000e¢\u0006\u0002\b\u0011H\u0002¢\u0006\u0002\u0010\u0012\u001aR\u0010\u0013\u001a\u0002H\u000b\"\u0004\b\u0000\u0010\u000b2\u0006\u0010\u0014\u001a\u00020\u00032'\u0010\r\u001a#\b\u0001\u0012\u0004\u0012\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u0010\u0012\u0006\u0012\u0004\u0018\u00010\t0\u000e¢\u0006\u0002\b\u0011H\u0086@\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001¢\u0006\u0002\u0010\u0015\u001aW\u0010\u0013\u001a\u0002H\u000b\"\u0004\b\u0000\u0010\u000b2\u0006\u0010\u0016\u001a\u00020\u00172'\u0010\r\u001a#\b\u0001\u0012\u0004\u0012\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u0010\u0012\u0006\u0012\u0004\u0018\u00010\t0\u000e¢\u0006\u0002\b\u0011H\u0086@ø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001¢\u0006\u0004\b\u0018\u0010\u0015\u001aG\u0010\u0019\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b2\u0006\u0010\u0014\u001a\u00020\u00032'\u0010\r\u001a#\b\u0001\u0012\u0004\u0012\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u0010\u0012\u0006\u0012\u0004\u0018\u00010\t0\u000e¢\u0006\u0002\b\u0011H\u0086@¢\u0006\u0002\u0010\u0015\u001aL\u0010\u0019\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b2\u0006\u0010\u0016\u001a\u00020\u00172'\u0010\r\u001a#\b\u0001\u0012\u0004\u0012\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u0010\u0012\u0006\u0012\u0004\u0018\u00010\t0\u000e¢\u0006\u0002\b\u0011H\u0086@ø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u0015\u0082\u0002\u0007\n\u0005\b¡\u001e0\u0001¨\u0006\u001b"}, d2 = {"TimeoutCancellationException", "Lkotlinx/coroutines/TimeoutCancellationException;", "time", "", "delay", "Lkotlinx/coroutines/Delay;", "coroutine", "Lkotlinx/coroutines/Job;", "setupTimeout", "", "U", "T", "Lkotlinx/coroutines/TimeoutCoroutine;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/CoroutineScope;", "Lkotlin/coroutines/Continuation;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/TimeoutCoroutine;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "withTimeout", "timeMillis", "(JLkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "timeout", "Lkotlin/time/Duration;", "withTimeout-KLykuaI", "withTimeoutOrNull", "withTimeoutOrNull-KLykuaI", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class TimeoutKt {

    /* JADX INFO: renamed from: kotlinx.coroutines.TimeoutKt$withTimeoutOrNull$1, reason: invalid class name */
    /* JADX INFO: compiled from: Timeout.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.TimeoutKt", f = "Timeout.kt", i = {0, 0, 0}, l = {104}, m = "withTimeoutOrNull", n = {"block", "coroutine", "timeMillis"}, s = {"L$0", "L$1", "J$0"})
    static final class AnonymousClass1<T> extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        long J$0;
        java.lang.Object L$0;
        java.lang.Object L$1;
        int label;
        /* synthetic */ java.lang.Object result;

        AnonymousClass1(kotlin.coroutines.Continuation<? super kotlinx.coroutines.TimeoutKt.AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return kotlinx.coroutines.TimeoutKt.withTimeoutOrNull(0L, null, this);
        }
    }

    public static final <T> java.lang.Object withTimeout(long timeMillis, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super T> continuation) {
        if (timeMillis <= 0) {
            throw new kotlinx.coroutines.TimeoutCancellationException("Timed out immediately");
        }
        java.lang.Object obj = setupTimeout(new kotlinx.coroutines.TimeoutCoroutine(timeMillis, continuation), function2);
        if (obj == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return obj;
    }

    /* JADX INFO: renamed from: withTimeout-KLykuaI, reason: not valid java name */
    public static final <T> java.lang.Object m12807withTimeoutKLykuaI(long timeout, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super T> continuation) {
        return withTimeout(kotlinx.coroutines.DelayKt.m12799toDelayMillisLRDsOJo(timeout), function2, continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r6v0, types: [T, kotlinx.coroutines.TimeoutCoroutine] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <T> java.lang.Object withTimeoutOrNull(long r7, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> r9, kotlin.coroutines.Continuation<? super T> r10) throws java.lang.Throwable {
        /*
            boolean r0 = r10 instanceof kotlinx.coroutines.TimeoutKt.AnonymousClass1
            if (r0 == 0) goto L14
            r0 = r10
            kotlinx.coroutines.TimeoutKt$withTimeoutOrNull$1 r0 = (kotlinx.coroutines.TimeoutKt.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r10 = r0.label
            int r10 = r10 - r2
            r0.label = r10
            goto L19
        L14:
            kotlinx.coroutines.TimeoutKt$withTimeoutOrNull$1 r0 = new kotlinx.coroutines.TimeoutKt$withTimeoutOrNull$1
            r0.<init>(r10)
        L19:
            r10 = r0
            java.lang.Object r0 = r10.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r10.label
            r3 = 0
            switch(r2) {
                case 0: goto L3f;
                case 1: goto L2e;
                default: goto L26;
            }
        L26:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
            java.lang.String r8 = "call to 'resume' before 'invoke' with coroutine"
            r7.<init>(r8)
            throw r7
        L2e:
            long r7 = r10.J$0
            java.lang.Object r7 = r10.L$1
            kotlin.jvm.internal.Ref$ObjectRef r7 = (kotlin.jvm.internal.Ref.ObjectRef) r7
            java.lang.Object r8 = r10.L$0
            kotlin.jvm.functions.Function2 r8 = (kotlin.jvm.functions.Function2) r8
            kotlin.ResultKt.throwOnFailure(r0)     // Catch: kotlinx.coroutines.TimeoutCancellationException -> L3d
            r8 = r0
            goto L78
        L3d:
            r8 = move-exception
            goto L7b
        L3f:
            kotlin.ResultKt.throwOnFailure(r0)
            r4 = 0
            int r2 = (r7 > r4 ? 1 : (r7 == r4 ? 0 : -1))
            if (r2 > 0) goto L49
            return r3
        L49:
            kotlin.jvm.internal.Ref$ObjectRef r2 = new kotlin.jvm.internal.Ref$ObjectRef
            r2.<init>()
            r10.L$0 = r9     // Catch: kotlinx.coroutines.TimeoutCancellationException -> L79
            r10.L$1 = r2     // Catch: kotlinx.coroutines.TimeoutCancellationException -> L79
            r10.J$0 = r7     // Catch: kotlinx.coroutines.TimeoutCancellationException -> L79
            r4 = 1
            r10.label = r4     // Catch: kotlinx.coroutines.TimeoutCancellationException -> L79
            r4 = r10
            kotlin.coroutines.Continuation r4 = (kotlin.coroutines.Continuation) r4     // Catch: kotlinx.coroutines.TimeoutCancellationException -> L79
            r5 = 0
            kotlinx.coroutines.TimeoutCoroutine r6 = new kotlinx.coroutines.TimeoutCoroutine     // Catch: kotlinx.coroutines.TimeoutCancellationException -> L79
            r6.<init>(r7, r4)     // Catch: kotlinx.coroutines.TimeoutCancellationException -> L79
            r7 = r6
            r2.element = r7     // Catch: kotlinx.coroutines.TimeoutCancellationException -> L79
            java.lang.Object r8 = setupTimeout(r7, r9)     // Catch: kotlinx.coroutines.TimeoutCancellationException -> L79
            java.lang.Object r7 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()     // Catch: kotlinx.coroutines.TimeoutCancellationException -> L79
            if (r8 != r7) goto L74
            r7 = r10
            kotlin.coroutines.Continuation r7 = (kotlin.coroutines.Continuation) r7     // Catch: kotlinx.coroutines.TimeoutCancellationException -> L79
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(r7)     // Catch: kotlinx.coroutines.TimeoutCancellationException -> L79
        L74:
            if (r8 != r1) goto L77
            return r1
        L77:
            r7 = r2
        L78:
            return r8
        L79:
            r8 = move-exception
            r7 = r2
        L7b:
            kotlinx.coroutines.Job r9 = r8.coroutine
            T r1 = r7.element
            if (r9 != r1) goto L82
            return r3
        L82:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.TimeoutKt.withTimeoutOrNull(long, kotlin.jvm.functions.Function2, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX INFO: renamed from: withTimeoutOrNull-KLykuaI, reason: not valid java name */
    public static final <T> java.lang.Object m12808withTimeoutOrNullKLykuaI(long timeout, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super T> continuation) {
        return withTimeoutOrNull(kotlinx.coroutines.DelayKt.m12799toDelayMillisLRDsOJo(timeout), function2, continuation);
    }

    private static final <U, T extends U> java.lang.Object setupTimeout(kotlinx.coroutines.TimeoutCoroutine<U, ? super T> timeoutCoroutine, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2) {
        kotlin.coroutines.CoroutineContext context = timeoutCoroutine.uCont.get$context();
        kotlinx.coroutines.JobKt.disposeOnCompletion(timeoutCoroutine, kotlinx.coroutines.DelayKt.getDelay(context).invokeOnTimeout(timeoutCoroutine.time, timeoutCoroutine, timeoutCoroutine.get$context()));
        return kotlinx.coroutines.intrinsics.UndispatchedKt.startUndispatchedOrReturnIgnoreTimeout(timeoutCoroutine, timeoutCoroutine, function2);
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0023  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlinx.coroutines.TimeoutCancellationException TimeoutCancellationException(long r3, kotlinx.coroutines.Delay r5, kotlinx.coroutines.Job r6) {
        /*
            java.lang.String r0 = "delay"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
            java.lang.String r0 = "coroutine"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            boolean r0 = r5 instanceof kotlinx.coroutines.DelayWithTimeoutDiagnostics
            if (r0 == 0) goto L12
            r0 = r5
            kotlinx.coroutines.DelayWithTimeoutDiagnostics r0 = (kotlinx.coroutines.DelayWithTimeoutDiagnostics) r0
            goto L13
        L12:
            r0 = 0
        L13:
            if (r0 == 0) goto L23
            kotlin.time.Duration$Companion r1 = kotlin.time.Duration.INSTANCE
            kotlin.time.DurationUnit r1 = kotlin.time.DurationUnit.MILLISECONDS
            long r1 = kotlin.time.DurationKt.toDuration(r3, r1)
            java.lang.String r0 = r0.m12800timeoutMessageLRDsOJo(r1)
            if (r0 != 0) goto L3c
        L23:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Timed out waiting for "
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r3)
            java.lang.String r1 = " ms"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
        L3c:
            kotlinx.coroutines.TimeoutCancellationException r1 = new kotlinx.coroutines.TimeoutCancellationException
            r1.<init>(r0, r6)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.TimeoutKt.TimeoutCancellationException(long, kotlinx.coroutines.Delay, kotlinx.coroutines.Job):kotlinx.coroutines.TimeoutCancellationException");
    }
}
