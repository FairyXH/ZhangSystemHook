package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Share.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002BB\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012-\u0010\u0004\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0005¢\u0006\u0002\b\t¢\u0006\u0002\u0010\nJ\u0016\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00028\u0000H\u0096A¢\u0006\u0002\u0010\u000eJ\u000e\u0010\u000f\u001a\u00020\u0007H\u0086@¢\u0006\u0002\u0010\u0010R7\u0010\u0004\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0005¢\u0006\u0002\b\tX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u000bR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lkotlinx/coroutines/flow/SubscribedFlowCollector;", "T", "Lkotlinx/coroutines/flow/FlowCollector;", "collector", "action", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/jvm/functions/Function2;)V", "Lkotlin/jvm/functions/Function2;", "emit", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onSubscription", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SubscribedFlowCollector<T> implements kotlinx.coroutines.flow.FlowCollector<T> {
    private final kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.FlowCollector<? super T>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> action;
    private final kotlinx.coroutines.flow.FlowCollector<T> collector;

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1, reason: invalid class name */
    /* JADX INFO: compiled from: Share.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.SubscribedFlowCollector", f = "Share.kt", i = {0, 0}, l = {com.android.internal.art.ArtStatsLog.EARLY_BOOT_COMP_OS_ARTIFACTS_CHECK_REPORTED, 423}, m = "onSubscription", n = {"this", "safeCollector"}, s = {"L$0", "L$1"})
    static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        java.lang.Object L$0;
        java.lang.Object L$1;
        int label;
        /* synthetic */ java.lang.Object result;
        final /* synthetic */ kotlinx.coroutines.flow.SubscribedFlowCollector<T> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(kotlinx.coroutines.flow.SubscribedFlowCollector<T> subscribedFlowCollector, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.SubscribedFlowCollector.AnonymousClass1> continuation) {
            super(continuation);
            this.this$0 = subscribedFlowCollector;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return this.this$0.onSubscription(this);
        }
    }

    @Override // kotlinx.coroutines.flow.FlowCollector
    public java.lang.Object emit(T t, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return this.collector.emit(t, continuation);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public SubscribedFlowCollector(kotlinx.coroutines.flow.FlowCollector<? super T> collector, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collector, "collector");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        this.collector = collector;
        this.action = action;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r2v0, types: [int] */
    /* JADX WARN: Type inference failed for: r2v1, types: [kotlinx.coroutines.flow.internal.SafeCollector] */
    /* JADX WARN: Type inference failed for: r2v5, types: [boolean] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object onSubscription(kotlin.coroutines.Continuation<? super kotlin.Unit> r8) throws java.lang.Throwable {
        /*
            r7 = this;
            boolean r0 = r8 instanceof kotlinx.coroutines.flow.SubscribedFlowCollector.AnonymousClass1
            if (r0 == 0) goto L14
            r0 = r8
            kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1 r0 = (kotlinx.coroutines.flow.SubscribedFlowCollector.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r8 = r0.label
            int r8 = r8 - r2
            r0.label = r8
            goto L19
        L14:
            kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1 r0 = new kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1
            r0.<init>(r7, r8)
        L19:
            r8 = r0
            java.lang.Object r0 = r8.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r8.label
            switch(r2) {
                case 0: goto L3d;
                case 1: goto L31;
                case 2: goto L2d;
                default: goto L25;
            }
        L25:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r8.<init>(r0)
            throw r8
        L2d:
            kotlin.ResultKt.throwOnFailure(r0)
            goto L7b
        L31:
            java.lang.Object r2 = r8.L$1
            kotlinx.coroutines.flow.internal.SafeCollector r2 = (kotlinx.coroutines.flow.internal.SafeCollector) r2
            java.lang.Object r3 = r8.L$0
            kotlinx.coroutines.flow.SubscribedFlowCollector r3 = (kotlinx.coroutines.flow.SubscribedFlowCollector) r3
            kotlin.ResultKt.throwOnFailure(r0)     // Catch: java.lang.Throwable -> L7f
            goto L5e
        L3d:
            kotlin.ResultKt.throwOnFailure(r0)
            r3 = r7
            kotlinx.coroutines.flow.internal.SafeCollector r2 = new kotlinx.coroutines.flow.internal.SafeCollector
            kotlinx.coroutines.flow.FlowCollector<T> r4 = r3.collector
            r5 = 0
            kotlin.coroutines.CoroutineContext r6 = r8.getContext()
            r2.<init>(r4, r6)
            kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.FlowCollector<? super T>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> r4 = r3.action     // Catch: java.lang.Throwable -> L7f
            r8.L$0 = r3     // Catch: java.lang.Throwable -> L7f
            r8.L$1 = r2     // Catch: java.lang.Throwable -> L7f
            r5 = 1
            r8.label = r5     // Catch: java.lang.Throwable -> L7f
            java.lang.Object r4 = r4.invoke(r2, r8)     // Catch: java.lang.Throwable -> L7f
            if (r4 != r1) goto L5e
            return r1
        L5e:
            r2.releaseIntercepted()
            kotlinx.coroutines.flow.FlowCollector<T> r2 = r3.collector
            boolean r2 = r2 instanceof kotlinx.coroutines.flow.SubscribedFlowCollector
            if (r2 == 0) goto L7e
            kotlinx.coroutines.flow.FlowCollector<T> r2 = r3.collector
            kotlinx.coroutines.flow.SubscribedFlowCollector r2 = (kotlinx.coroutines.flow.SubscribedFlowCollector) r2
            r4 = 0
            r8.L$0 = r4
            r8.L$1 = r4
            r4 = 2
            r8.label = r4
            java.lang.Object r2 = r2.onSubscription(r8)
            if (r2 != r1) goto L7b
            return r1
        L7b:
            kotlin.Unit r1 = kotlin.Unit.INSTANCE
            return r1
        L7e:
            goto L7b
        L7f:
            r1 = move-exception
            r2.releaseIntercepted()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.SubscribedFlowCollector.onSubscription(kotlin.coroutines.Continuation):java.lang.Object");
    }
}
