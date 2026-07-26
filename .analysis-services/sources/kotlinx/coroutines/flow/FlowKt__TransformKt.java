package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Transform.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000D\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\u001aJ\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012$\b\u0004\u0010\u0003\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004H\u0086\b¢\u0006\u0002\u0010\b\u001a\u001f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\n0\u0001\"\u0006\b\u0000\u0010\n\u0018\u0001*\u0006\u0012\u0002\b\u00030\u0001H\u0086\b\u001a,\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\n0\u0001\"\b\b\u0000\u0010\n*\u00020\u0007*\u0006\u0012\u0002\b\u00030\u00012\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\n0\f\u001aJ\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012$\b\u0004\u0010\u0003\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004H\u0086\b¢\u0006\u0002\u0010\b\u001a\"\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0007*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001\u001a_\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\n0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\b\u0012\u0004\u0012\u0002H\u00020\u000123\b\u0004\u0010\u0010\u001a-\b\u0001\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0013\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\n0\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004H\u0086\b¢\u0006\u0002\u0010\b\u001ae\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\n0\u0001\"\u0004\b\u0000\u0010\u0002\"\b\b\u0001\u0010\n*\u00020\u0007*\b\u0012\u0004\u0012\u0002H\u00020\u000125\b\u0004\u0010\u0010\u001a/\b\u0001\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0013\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u0001H\n0\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004H\u0086\b¢\u0006\u0002\u0010\b\u001aE\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\"\u0010\u0016\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004¢\u0006\u0002\u0010\b\u001ay\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\n0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0019\u001a\u0002H\n2H\b\u0001\u0010\u001a\u001aB\b\u0001\u0012\u0013\u0012\u0011H\n¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u001c\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0013\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\n0\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u001b¢\u0006\u0002\u0010\u001d\u001ai\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012F\u0010\u001a\u001aB\b\u0001\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u001c\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0013\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u001b¢\u0006\u0002\u0010\u001f\u001ay\u0010 \u001a\b\u0012\u0004\u0012\u0002H\n0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0019\u001a\u0002H\n2H\b\u0001\u0010\u001a\u001aB\b\u0001\u0012\u0013\u0012\u0011H\n¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u001c\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0013\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\n0\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u001b¢\u0006\u0002\u0010\u001d\u001a\"\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\"0\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001¨\u0006#"}, d2 = {com.android.server.pm.verify.domain.DomainVerificationPersistence.ATTR_FILTER, "Lkotlinx/coroutines/flow/Flow;", "T", "predicate", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "filterIsInstance", "R", "klass", "Lkotlin/reflect/KClass;", "filterNot", "filterNotNull", "map", "transform", "Lkotlin/ParameterName;", "name", "value", "mapNotNull", "onEach", "action", "", "runningFold", "initial", "operation", "Lkotlin/Function3;", "accumulator", "(Lkotlinx/coroutines/flow/Flow;Ljava/lang/Object;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "runningReduce", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "scan", "withIndex", "Lkotlin/collections/IndexedValue;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
final /* synthetic */ class FlowKt__TransformKt {
    public static final <T> kotlinx.coroutines.flow.Flow<T> filter(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        return new kotlinx.coroutines.flow.FlowKt__TransformKt$filter$$inlined$unsafeTransform$1(flow, predicate);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> filterNot(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        return new kotlinx.coroutines.flow.FlowKt__TransformKt$filterNot$$inlined$unsafeTransform$1(flow, predicate);
    }

    public static final /* synthetic */ <R> kotlinx.coroutines.flow.Flow<R> filterIsInstance(kotlinx.coroutines.flow.Flow<?> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.needClassReification();
        return new kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$1(flow);
    }

    public static final <R> kotlinx.coroutines.flow.Flow<R> filterIsInstance(final kotlinx.coroutines.flow.Flow<?> flow, final kotlin.reflect.KClass<R> klass) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(klass, "klass");
        return (kotlinx.coroutines.flow.Flow) new kotlinx.coroutines.flow.Flow<java.lang.Object>() { // from class: kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$2

            /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$2$2, reason: invalid class name */
            /* JADX INFO: compiled from: Emitters.kt */
            @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\b"}, d2 = {"<anonymous>", "", "T", "R", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1", "kotlinx/coroutines/flow/FlowKt__TransformKt$filter$$inlined$unsafeTransform$1$2"}, k = 3, mv = {1, 9, 0}, xi = 48)
            public static final class AnonymousClass2<T> implements kotlinx.coroutines.flow.FlowCollector {
                final /* synthetic */ kotlin.reflect.KClass $klass$inlined;
                final /* synthetic */ kotlinx.coroutines.flow.FlowCollector $this_unsafeFlow;

                /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$2$2$1, reason: invalid class name */
                /* JADX INFO: compiled from: Emitters.kt */
                @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
                @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$2$2", f = "Transform.kt", i = {}, l = {com.android.internal.util.FrameworkStatsLog.EXCLUSION_RECT_STATE_CHANGED}, m = "emit", n = {}, s = {})
                public static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
                    java.lang.Object L$0;
                    java.lang.Object L$1;
                    int label;
                    /* synthetic */ java.lang.Object result;

                    public AnonymousClass1(kotlin.coroutines.Continuation continuation) {
                        super(continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    public final java.lang.Object invokeSuspend(java.lang.Object obj) {
                        this.result = obj;
                        this.label |= Integer.MIN_VALUE;
                        return kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$2.AnonymousClass2.this.emit(null, this);
                    }
                }

                public AnonymousClass2(kotlinx.coroutines.flow.FlowCollector flowCollector, kotlin.reflect.KClass kClass) {
                    this.$this_unsafeFlow = flowCollector;
                    this.$klass$inlined = kClass;
                }

                /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
                @Override // kotlinx.coroutines.flow.FlowCollector
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final java.lang.Object emit(java.lang.Object r9, kotlin.coroutines.Continuation r10) throws java.lang.Throwable {
                    /*
                        r8 = this;
                        boolean r0 = r10 instanceof kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$2.AnonymousClass2.AnonymousClass1
                        if (r0 == 0) goto L14
                        r0 = r10
                        kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$2$2$1 r0 = (kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$2.AnonymousClass2.AnonymousClass1) r0
                        int r1 = r0.label
                        r2 = -2147483648(0xffffffff80000000, float:-0.0)
                        r1 = r1 & r2
                        if (r1 == 0) goto L14
                        int r10 = r0.label
                        int r10 = r10 - r2
                        r0.label = r10
                        goto L19
                    L14:
                        kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$2$2$1 r0 = new kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$2$2$1
                        r0.<init>(r10)
                    L19:
                        r10 = r0
                        java.lang.Object r0 = r10.result
                        java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                        int r2 = r10.label
                        switch(r2) {
                            case 0: goto L32;
                            case 1: goto L2d;
                            default: goto L25;
                        }
                    L25:
                        java.lang.IllegalStateException r9 = new java.lang.IllegalStateException
                        java.lang.String r10 = "call to 'resume' before 'invoke' with coroutine"
                        r9.<init>(r10)
                        throw r9
                    L2d:
                        r9 = 0
                        kotlin.ResultKt.throwOnFailure(r0)
                        goto L51
                    L32:
                        kotlin.ResultKt.throwOnFailure(r0)
                        r2 = r8
                        kotlinx.coroutines.flow.FlowCollector r3 = r2.$this_unsafeFlow
                        r4 = 0
                        r5 = r10
                        kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
                        r5 = r9
                        r6 = 0
                        kotlin.reflect.KClass r7 = r2.$klass$inlined
                        boolean r2 = r7.isInstance(r5)
                        if (r2 == 0) goto L52
                        r2 = 1
                        r10.label = r2
                        java.lang.Object r9 = r3.emit(r9, r10)
                        if (r9 != r1) goto L50
                        return r1
                    L50:
                        r9 = r4
                    L51:
                        goto L53
                    L52:
                    L53:
                        kotlin.Unit r9 = kotlin.Unit.INSTANCE
                        return r9
                    */
                    throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$2.AnonymousClass2.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
                }
            }

            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super java.lang.Object> flowCollector, kotlin.coroutines.Continuation $completion) {
                java.lang.Object objCollect = flow.collect(new kotlinx.coroutines.flow.FlowKt__TransformKt$filterIsInstance$$inlined$filter$2.AnonymousClass2(flowCollector, klass), $completion);
                return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
            }
        };
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> filterNotNull(final kotlinx.coroutines.flow.Flow<? extends T> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        return new kotlinx.coroutines.flow.Flow<T>() { // from class: kotlinx.coroutines.flow.FlowKt__TransformKt$filterNotNull$$inlined$unsafeTransform$1

            /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__TransformKt$filterNotNull$$inlined$unsafeTransform$1$2, reason: invalid class name */
            /* JADX INFO: compiled from: Emitters.kt */
            @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"<anonymous>", "", "T", "R", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1"}, k = 3, mv = {1, 9, 0}, xi = 48)
            public static final class AnonymousClass2<T> implements kotlinx.coroutines.flow.FlowCollector {
                final /* synthetic */ kotlinx.coroutines.flow.FlowCollector $this_unsafeFlow;

                /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__TransformKt$filterNotNull$$inlined$unsafeTransform$1$2$1, reason: invalid class name */
                /* JADX INFO: compiled from: Emitters.kt */
                @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
                @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__TransformKt$filterNotNull$$inlined$unsafeTransform$1$2", f = "Transform.kt", i = {}, l = {com.android.internal.util.FrameworkStatsLog.EXCLUSION_RECT_STATE_CHANGED}, m = "emit", n = {}, s = {})
                public static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
                    int label;
                    /* synthetic */ java.lang.Object result;

                    public AnonymousClass1(kotlin.coroutines.Continuation continuation) {
                        super(continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    public final java.lang.Object invokeSuspend(java.lang.Object obj) {
                        this.result = obj;
                        this.label |= Integer.MIN_VALUE;
                        return kotlinx.coroutines.flow.FlowKt__TransformKt$filterNotNull$$inlined$unsafeTransform$1.AnonymousClass2.this.emit(null, this);
                    }
                }

                public AnonymousClass2(kotlinx.coroutines.flow.FlowCollector flowCollector) {
                    this.$this_unsafeFlow = flowCollector;
                }

                /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
                @Override // kotlinx.coroutines.flow.FlowCollector
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final java.lang.Object emit(T r6, kotlin.coroutines.Continuation<? super kotlin.Unit> r7) throws java.lang.Throwable {
                    /*
                        r5 = this;
                        boolean r0 = r7 instanceof kotlinx.coroutines.flow.FlowKt__TransformKt$filterNotNull$$inlined$unsafeTransform$1.AnonymousClass2.AnonymousClass1
                        if (r0 == 0) goto L14
                        r0 = r7
                        kotlinx.coroutines.flow.FlowKt__TransformKt$filterNotNull$$inlined$unsafeTransform$1$2$1 r0 = (kotlinx.coroutines.flow.FlowKt__TransformKt$filterNotNull$$inlined$unsafeTransform$1.AnonymousClass2.AnonymousClass1) r0
                        int r1 = r0.label
                        r2 = -2147483648(0xffffffff80000000, float:-0.0)
                        r1 = r1 & r2
                        if (r1 == 0) goto L14
                        int r7 = r0.label
                        int r7 = r7 - r2
                        r0.label = r7
                        goto L19
                    L14:
                        kotlinx.coroutines.flow.FlowKt__TransformKt$filterNotNull$$inlined$unsafeTransform$1$2$1 r0 = new kotlinx.coroutines.flow.FlowKt__TransformKt$filterNotNull$$inlined$unsafeTransform$1$2$1
                        r0.<init>(r7)
                    L19:
                        r7 = r0
                        java.lang.Object r0 = r7.result
                        java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                        int r2 = r7.label
                        switch(r2) {
                            case 0: goto L32;
                            case 1: goto L2d;
                            default: goto L25;
                        }
                    L25:
                        java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
                        java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
                        r6.<init>(r7)
                        throw r6
                    L2d:
                        r6 = 0
                        kotlin.ResultKt.throwOnFailure(r0)
                        goto L4a
                    L32:
                        kotlin.ResultKt.throwOnFailure(r0)
                        r2 = r5
                        kotlinx.coroutines.flow.FlowCollector r3 = r2.$this_unsafeFlow
                        r2 = r7
                        kotlin.coroutines.Continuation r2 = (kotlin.coroutines.Continuation) r2
                        r2 = r3
                        r3 = 0
                        if (r6 == 0) goto L4b
                        r4 = 1
                        r7.label = r4
                        java.lang.Object r6 = r2.emit(r6, r7)
                        if (r6 != r1) goto L49
                        return r1
                    L49:
                        r6 = r3
                    L4a:
                        goto L4c
                    L4b:
                    L4c:
                        kotlin.Unit r6 = kotlin.Unit.INSTANCE
                        return r6
                    */
                    throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__TransformKt$filterNotNull$$inlined$unsafeTransform$1.AnonymousClass2.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
                }
            }

            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector collector, kotlin.coroutines.Continuation $completion) {
                java.lang.Object objCollect = flow.collect(new kotlinx.coroutines.flow.FlowKt__TransformKt$filterNotNull$$inlined$unsafeTransform$1.AnonymousClass2(collector), $completion);
                return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
            }
        };
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> map(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return new kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1(flow, transform);
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> mapNotNull(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return new kotlinx.coroutines.flow.FlowKt__TransformKt$mapNotNull$$inlined$unsafeTransform$1(flow, transform);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<kotlin.collections.IndexedValue<T>> withIndex(final kotlinx.coroutines.flow.Flow<? extends T> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        return new kotlinx.coroutines.flow.Flow<kotlin.collections.IndexedValue<? extends T>>() { // from class: kotlinx.coroutines.flow.FlowKt__TransformKt$withIndex$$inlined$unsafeFlow$1
            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super kotlin.collections.IndexedValue<? extends T>> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                kotlin.jvm.internal.Ref.IntRef index = new kotlin.jvm.internal.Ref.IntRef();
                java.lang.Object objCollect = flow.collect(new kotlinx.coroutines.flow.FlowKt__TransformKt$withIndex$1$1(flowCollector, index), continuation);
                return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
            }
        };
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> onEach(final kotlinx.coroutines.flow.Flow<? extends T> flow, final kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        return new kotlinx.coroutines.flow.Flow<T>() { // from class: kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1

            /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1$2, reason: invalid class name */
            /* JADX INFO: compiled from: Emitters.kt */
            @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"<anonymous>", "", "T", "R", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1"}, k = 3, mv = {1, 9, 0}, xi = 48)
            public static final class AnonymousClass2<T> implements kotlinx.coroutines.flow.FlowCollector {
                final /* synthetic */ kotlin.jvm.functions.Function2 $action$inlined;
                final /* synthetic */ kotlinx.coroutines.flow.FlowCollector $this_unsafeFlow;

                /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1$2$1, reason: invalid class name */
                /* JADX INFO: compiled from: Emitters.kt */
                @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
                @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1$2", f = "Transform.kt", i = {0, 0}, l = {com.android.internal.util.FrameworkStatsLog.EXCLUSION_RECT_STATE_CHANGED, com.android.server.usb.descriptors.UsbDescriptor.CLASSID_WIRELESS}, m = "emit", n = {"value", "$this$onEach_u24lambda_u248"}, s = {"L$0", "L$1"})
                public static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
                    java.lang.Object L$0;
                    java.lang.Object L$1;
                    int label;
                    /* synthetic */ java.lang.Object result;

                    public AnonymousClass1(kotlin.coroutines.Continuation continuation) {
                        super(continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    public final java.lang.Object invokeSuspend(java.lang.Object obj) {
                        this.result = obj;
                        this.label |= Integer.MIN_VALUE;
                        return kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1.AnonymousClass2.this.emit(null, this);
                    }
                }

                public AnonymousClass2(kotlinx.coroutines.flow.FlowCollector flowCollector, kotlin.jvm.functions.Function2 function2) {
                    this.$this_unsafeFlow = flowCollector;
                    this.$action$inlined = function2;
                }

                /* JADX WARN: Multi-variable type inference failed */
                /* JADX WARN: Removed duplicated region for block: B:20:0x0070 A[RETURN] */
                /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
                @Override // kotlinx.coroutines.flow.FlowCollector
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final java.lang.Object emit(T r8, kotlin.coroutines.Continuation<? super kotlin.Unit> r9) throws java.lang.Throwable {
                    /*
                        r7 = this;
                        boolean r0 = r9 instanceof kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1.AnonymousClass2.AnonymousClass1
                        if (r0 == 0) goto L14
                        r0 = r9
                        kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1$2$1 r0 = (kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1.AnonymousClass2.AnonymousClass1) r0
                        int r1 = r0.label
                        r2 = -2147483648(0xffffffff80000000, float:-0.0)
                        r1 = r1 & r2
                        if (r1 == 0) goto L14
                        int r9 = r0.label
                        int r9 = r9 - r2
                        r0.label = r9
                        goto L19
                    L14:
                        kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1$2$1 r0 = new kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1$2$1
                        r0.<init>(r9)
                    L19:
                        r9 = r0
                        java.lang.Object r0 = r9.result
                        java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                        int r2 = r9.label
                        switch(r2) {
                            case 0: goto L3d;
                            case 1: goto L32;
                            case 2: goto L2d;
                            default: goto L25;
                        }
                    L25:
                        java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
                        java.lang.String r9 = "call to 'resume' before 'invoke' with coroutine"
                        r8.<init>(r9)
                        throw r8
                    L2d:
                        r8 = 0
                        kotlin.ResultKt.throwOnFailure(r0)
                        goto L71
                    L32:
                        r8 = 0
                        java.lang.Object r2 = r9.L$1
                        kotlinx.coroutines.flow.FlowCollector r2 = (kotlinx.coroutines.flow.FlowCollector) r2
                        java.lang.Object r3 = r9.L$0
                        kotlin.ResultKt.throwOnFailure(r0)
                        goto L62
                    L3d:
                        kotlin.ResultKt.throwOnFailure(r0)
                        r2 = r7
                        kotlinx.coroutines.flow.FlowCollector r3 = r2.$this_unsafeFlow
                        r4 = r9
                        kotlin.coroutines.Continuation r4 = (kotlin.coroutines.Continuation) r4
                        r4 = 0
                        kotlin.jvm.functions.Function2 r5 = r2.$action$inlined
                        r9.L$0 = r8
                        r9.L$1 = r3
                        r6 = 1
                        r9.label = r6
                        r6 = 6
                        kotlin.jvm.internal.InlineMarker.mark(r6)
                        java.lang.Object r5 = r5.invoke(r8, r9)
                        r6 = 7
                        kotlin.jvm.internal.InlineMarker.mark(r6)
                        if (r5 != r1) goto L5f
                        return r1
                    L5f:
                        r2 = r3
                        r3 = r8
                        r8 = r4
                    L62:
                        r4 = 0
                        r9.L$0 = r4
                        r9.L$1 = r4
                        r4 = 2
                        r9.label = r4
                        java.lang.Object r2 = r2.emit(r3, r9)
                        if (r2 != r1) goto L71
                        return r1
                    L71:
                        kotlin.Unit r8 = kotlin.Unit.INSTANCE
                        return r8
                    */
                    throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1.AnonymousClass2.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
                }
            }

            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector collector, kotlin.coroutines.Continuation $completion) {
                java.lang.Object objCollect = flow.collect(new kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1.AnonymousClass2(collector, action), $completion);
                return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
            }
        };
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> scan(kotlinx.coroutines.flow.Flow<? extends T> flow, R r, kotlin.jvm.functions.Function3<? super R, ? super T, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> operation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(operation, "operation");
        return kotlinx.coroutines.flow.FlowKt.runningFold(flow, r, operation);
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> runningFold(kotlinx.coroutines.flow.Flow<? extends T> flow, R r, kotlin.jvm.functions.Function3<? super R, ? super T, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> operation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(operation, "operation");
        return new kotlinx.coroutines.flow.FlowKt__TransformKt$runningFold$$inlined$unsafeFlow$1(r, flow, operation);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> runningReduce(final kotlinx.coroutines.flow.Flow<? extends T> flow, final kotlin.jvm.functions.Function3<? super T, ? super T, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> operation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(operation, "operation");
        return new kotlinx.coroutines.flow.Flow<T>() { // from class: kotlinx.coroutines.flow.FlowKt__TransformKt$runningReduce$$inlined$unsafeFlow$1
            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                kotlin.jvm.internal.Ref.ObjectRef objectRef = new kotlin.jvm.internal.Ref.ObjectRef();
                objectRef.element = (T) kotlinx.coroutines.flow.internal.NullSurrogateKt.NULL;
                java.lang.Object objCollect = flow.collect(new kotlinx.coroutines.flow.FlowKt__TransformKt$runningReduce$1$1(objectRef, operation, flowCollector), continuation);
                return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
            }
        };
    }
}
