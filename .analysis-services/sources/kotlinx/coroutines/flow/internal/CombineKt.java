package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: Combine.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000>\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001ak\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0003\"\u0004\b\u0001\u0010\u0004\"\u0004\b\u0002\u0010\u00022\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00030\u00012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00040\u00012(\u0010\u0007\u001a$\b\u0001\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\t\u0012\u0006\u0012\u0004\u0018\u00010\n0\bH\u0000¢\u0006\u0002\u0010\u000b\u001a\u008d\u0001\u0010\f\u001a\u00020\r\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0014\u0010\u0010\u001a\u0010\u0012\f\b\u0001\u0012\b\u0012\u0004\u0012\u0002H\u000e0\u00010\u00112\u0016\u0010\u0012\u001a\u0012\u0012\u000e\u0012\f\u0012\u0006\u0012\u0004\u0018\u0001H\u000e\u0018\u00010\u00110\u001329\u0010\u0007\u001a5\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\t\u0012\u0006\u0012\u0004\u0018\u00010\n0\b¢\u0006\u0002\b\u0014H\u0081@¢\u0006\u0002\u0010\u0015*\u001c\b\u0002\u0010\u0016\"\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u00172\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0017¨\u0006\u0018"}, d2 = {"zipImpl", "Lkotlinx/coroutines/flow/Flow;", "R", "T1", "T2", "flow", "flow2", "transform", "Lkotlin/Function3;", "Lkotlin/coroutines/Continuation;", "", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "combineInternal", "", "T", "Lkotlinx/coroutines/flow/FlowCollector;", "flows", "", "arrayFactory", "Lkotlin/Function0;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/flow/FlowCollector;[Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Update", "Lkotlin/collections/IndexedValue;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class CombineKt {

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2, reason: invalid class name */
    /* JADX INFO: compiled from: Combine.kt */
    @kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", "", "R", "T", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2", f = "Combine.kt", i = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2}, l = {54, 76, 79}, m = "invokeSuspend", n = {"latestValues", "resultChannel", "lastReceivedEpoch", "remainingAbsentValues", "currentEpoch", "latestValues", "resultChannel", "lastReceivedEpoch", "remainingAbsentValues", "currentEpoch", "latestValues", "resultChannel", "lastReceivedEpoch", "remainingAbsentValues", "currentEpoch"}, s = {"L$0", "L$1", "L$2", "I$0", "I$1", "L$0", "L$1", "L$2", "I$0", "I$1", "L$0", "L$1", "L$2", "I$0", "I$1"})
    static final class AnonymousClass2 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ kotlin.jvm.functions.Function0<T[]> $arrayFactory;
        final /* synthetic */ kotlinx.coroutines.flow.Flow<T>[] $flows;
        final /* synthetic */ kotlinx.coroutines.flow.FlowCollector<R> $this_combineInternal;
        final /* synthetic */ kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> $transform;
        int I$0;
        int I$1;
        private /* synthetic */ java.lang.Object L$0;
        java.lang.Object L$1;
        java.lang.Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass2(kotlinx.coroutines.flow.Flow<? extends T>[] flowArr, kotlin.jvm.functions.Function0<T[]> function0, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T[], ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3, kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.internal.CombineKt.AnonymousClass2> continuation) {
            super(2, continuation);
            this.$flows = flowArr;
            this.$arrayFactory = function0;
            this.$transform = function3;
            this.$this_combineInternal = flowCollector;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlinx.coroutines.flow.internal.CombineKt.AnonymousClass2 anonymousClass2 = new kotlinx.coroutines.flow.internal.CombineKt.AnonymousClass2(this.$flows, this.$arrayFactory, this.$transform, this.$this_combineInternal, continuation);
            anonymousClass2.L$0 = obj;
            return anonymousClass2;
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.flow.internal.CombineKt.AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x00e8 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:20:0x00e9  */
        /* JADX WARN: Removed duplicated region for block: B:23:0x00f2  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x00f6 A[LOOP:0: B:25:0x00f6->B:50:?, LOOP_START, PHI: r5 r8
  0x00f6: PHI (r5v11 'remainingAbsentValues' int) = (r5v10 'remainingAbsentValues' int), (r5v12 'remainingAbsentValues' int) binds: [B:22:0x00f0, B:50:?] A[DONT_GENERATE, DONT_INLINE]
  0x00f6: PHI (r8v8 'element' kotlin.collections.IndexedValue) = (r8v7 'element' kotlin.collections.IndexedValue), (r8v20 'element' kotlin.collections.IndexedValue) binds: [B:22:0x00f0, B:50:?] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:35:0x0120  */
        /* JADX WARN: Removed duplicated region for block: B:47:0x016a  */
        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:40:0x0142 -> B:17:0x00ce). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:45:0x0167 -> B:17:0x00ce). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:47:0x016a -> B:17:0x00ce). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final java.lang.Object invokeSuspend(java.lang.Object r22) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 378
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.internal.CombineKt.AnonymousClass2.invokeSuspend(java.lang.Object):java.lang.Object");
        }

        /* JADX INFO: renamed from: kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1, reason: invalid class name */
        /* JADX INFO: compiled from: Combine.kt */
        @kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", "", "R", "T", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
        @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1", f = "Combine.kt", i = {}, l = {31}, m = "invokeSuspend", n = {}, s = {})
        static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
            final /* synthetic */ kotlinx.coroutines.flow.Flow<T>[] $flows;
            final /* synthetic */ int $i;
            final /* synthetic */ java.util.concurrent.atomic.AtomicInteger $nonClosed;
            final /* synthetic */ kotlinx.coroutines.channels.Channel<kotlin.collections.IndexedValue<java.lang.Object>> $resultChannel;
            int label;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            AnonymousClass1(kotlinx.coroutines.flow.Flow<? extends T>[] flowArr, int i, java.util.concurrent.atomic.AtomicInteger atomicInteger, kotlinx.coroutines.channels.Channel<kotlin.collections.IndexedValue<java.lang.Object>> channel, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.internal.CombineKt.AnonymousClass2.AnonymousClass1> continuation) {
                super(2, continuation);
                this.$flows = flowArr;
                this.$i = i;
                this.$nonClosed = atomicInteger;
                this.$resultChannel = channel;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
                return new kotlinx.coroutines.flow.internal.CombineKt.AnonymousClass2.AnonymousClass1(this.$flows, this.$i, this.$nonClosed, this.$resultChannel, continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                return ((kotlinx.coroutines.flow.internal.CombineKt.AnonymousClass2.AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
            }

            /* JADX WARN: Removed duplicated region for block: B:18:0x0045  */
            /* JADX WARN: Removed duplicated region for block: B:25:0x005c  */
            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final java.lang.Object invokeSuspend(java.lang.Object r10) throws java.lang.Throwable {
                /*
                    r9 = this;
                    java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                    int r1 = r9.label
                    r2 = 1
                    r3 = 0
                    switch(r1) {
                        case 0: goto L1a;
                        case 1: goto L13;
                        default: goto Lb;
                    }
                Lb:
                    java.lang.IllegalStateException r10 = new java.lang.IllegalStateException
                    java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
                    r10.<init>(r0)
                    throw r10
                L13:
                    r0 = r9
                    kotlin.ResultKt.throwOnFailure(r10)     // Catch: java.lang.Throwable -> L18
                    goto L3d
                L18:
                    r1 = move-exception
                    goto L54
                L1a:
                    kotlin.ResultKt.throwOnFailure(r10)
                    r1 = r9
                    kotlinx.coroutines.flow.Flow<T>[] r4 = r1.$flows     // Catch: java.lang.Throwable -> L50
                    int r5 = r1.$i     // Catch: java.lang.Throwable -> L50
                    r4 = r4[r5]     // Catch: java.lang.Throwable -> L50
                    kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1$1 r5 = new kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1$1     // Catch: java.lang.Throwable -> L50
                    kotlinx.coroutines.channels.Channel<kotlin.collections.IndexedValue<java.lang.Object>> r6 = r1.$resultChannel     // Catch: java.lang.Throwable -> L50
                    int r7 = r1.$i     // Catch: java.lang.Throwable -> L50
                    r5.<init>(r6, r7)     // Catch: java.lang.Throwable -> L50
                    kotlinx.coroutines.flow.FlowCollector r5 = (kotlinx.coroutines.flow.FlowCollector) r5     // Catch: java.lang.Throwable -> L50
                    r6 = r1
                    kotlin.coroutines.Continuation r6 = (kotlin.coroutines.Continuation) r6     // Catch: java.lang.Throwable -> L50
                    r1.label = r2     // Catch: java.lang.Throwable -> L50
                    java.lang.Object r4 = r4.collect(r5, r6)     // Catch: java.lang.Throwable -> L50
                    if (r4 != r0) goto L3c
                    return r0
                L3c:
                    r0 = r1
                L3d:
                    java.util.concurrent.atomic.AtomicInteger r1 = r0.$nonClosed
                    int r1 = r1.decrementAndGet()
                    if (r1 != 0) goto L4c
                    kotlinx.coroutines.channels.Channel<kotlin.collections.IndexedValue<java.lang.Object>> r1 = r0.$resultChannel
                    kotlinx.coroutines.channels.SendChannel r1 = (kotlinx.coroutines.channels.SendChannel) r1
                    kotlinx.coroutines.channels.SendChannel.DefaultImpls.close$default(r1, r3, r2, r3)
                L4c:
                    kotlin.Unit r1 = kotlin.Unit.INSTANCE
                    return r1
                L50:
                    r0 = move-exception
                    r8 = r1
                    r1 = r0
                    r0 = r8
                L54:
                    java.util.concurrent.atomic.AtomicInteger r4 = r0.$nonClosed
                    int r4 = r4.decrementAndGet()
                    if (r4 != 0) goto L63
                    kotlinx.coroutines.channels.Channel<kotlin.collections.IndexedValue<java.lang.Object>> r4 = r0.$resultChannel
                    kotlinx.coroutines.channels.SendChannel r4 = (kotlinx.coroutines.channels.SendChannel) r4
                    kotlinx.coroutines.channels.SendChannel.DefaultImpls.close$default(r4, r3, r2, r3)
                L63:
                    throw r1
                */
                throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.internal.CombineKt.AnonymousClass2.AnonymousClass1.invokeSuspend(java.lang.Object):java.lang.Object");
            }

            /* JADX INFO: renamed from: kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1$1, reason: invalid class name and collision with other inner class name */
            /* JADX INFO: compiled from: Combine.kt */
            @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0003H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "R", "T", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"}, k = 3, mv = {1, 9, 0}, xi = 48)
            static final class C00221<T> implements kotlinx.coroutines.flow.FlowCollector {
                final /* synthetic */ int $i;
                final /* synthetic */ kotlinx.coroutines.channels.Channel<kotlin.collections.IndexedValue<java.lang.Object>> $resultChannel;

                C00221(kotlinx.coroutines.channels.Channel<kotlin.collections.IndexedValue<java.lang.Object>> channel, int i) {
                    this.$resultChannel = channel;
                    this.$i = i;
                }

                /* JADX WARN: Removed duplicated region for block: B:19:0x0055 A[RETURN] */
                /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
                @Override // kotlinx.coroutines.flow.FlowCollector
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final java.lang.Object emit(T r7, kotlin.coroutines.Continuation<? super kotlin.Unit> r8) throws java.lang.Throwable {
                    /*
                        r6 = this;
                        boolean r0 = r8 instanceof kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1$1$emit$1
                        if (r0 == 0) goto L14
                        r0 = r8
                        kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1$1$emit$1 r0 = (kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1$1$emit$1) r0
                        int r1 = r0.label
                        r2 = -2147483648(0xffffffff80000000, float:-0.0)
                        r1 = r1 & r2
                        if (r1 == 0) goto L14
                        int r8 = r0.label
                        int r8 = r8 - r2
                        r0.label = r8
                        goto L19
                    L14:
                        kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1$1$emit$1 r0 = new kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1$1$emit$1
                        r0.<init>(r6, r8)
                    L19:
                        r8 = r0
                        java.lang.Object r0 = r8.result
                        java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                        int r2 = r8.label
                        switch(r2) {
                            case 0: goto L35;
                            case 1: goto L31;
                            case 2: goto L2d;
                            default: goto L25;
                        }
                    L25:
                        java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
                        java.lang.String r8 = "call to 'resume' before 'invoke' with coroutine"
                        r7.<init>(r8)
                        throw r7
                    L2d:
                        kotlin.ResultKt.throwOnFailure(r0)
                        goto L56
                    L31:
                        kotlin.ResultKt.throwOnFailure(r0)
                        goto L4c
                    L35:
                        kotlin.ResultKt.throwOnFailure(r0)
                        r2 = r6
                        kotlinx.coroutines.channels.Channel<kotlin.collections.IndexedValue<java.lang.Object>> r3 = r2.$resultChannel
                        kotlin.collections.IndexedValue r4 = new kotlin.collections.IndexedValue
                        int r5 = r2.$i
                        r4.<init>(r5, r7)
                        r5 = 1
                        r8.label = r5
                        java.lang.Object r7 = r3.send(r4, r8)
                        if (r7 != r1) goto L4c
                        return r1
                    L4c:
                        r7 = 2
                        r8.label = r7
                        java.lang.Object r7 = kotlinx.coroutines.YieldKt.yield(r8)
                        if (r7 != r1) goto L56
                        return r1
                    L56:
                        kotlin.Unit r7 = kotlin.Unit.INSTANCE
                        return r7
                    */
                    throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.internal.CombineKt.AnonymousClass2.AnonymousClass1.C00221.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
                }
            }
        }
    }

    public static final <R, T> java.lang.Object combineInternal(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, kotlinx.coroutines.flow.Flow<? extends T>[] flowArr, kotlin.jvm.functions.Function0<T[]> function0, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T[], ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        java.lang.Object objFlowScope = kotlinx.coroutines.flow.internal.FlowCoroutineKt.flowScope(new kotlinx.coroutines.flow.internal.CombineKt.AnonymousClass2(flowArr, function0, function3, flowCollector, null), continuation);
        return objFlowScope == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objFlowScope : kotlin.Unit.INSTANCE;
    }

    public static final <T1, T2, R> kotlinx.coroutines.flow.Flow<R> zipImpl(final kotlinx.coroutines.flow.Flow<? extends T1> flow, final kotlinx.coroutines.flow.Flow<? extends T2> flow2, final kotlin.jvm.functions.Function3<? super T1, ? super T2, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow2, "flow2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return new kotlinx.coroutines.flow.Flow<R>() { // from class: kotlinx.coroutines.flow.internal.CombineKt$zipImpl$$inlined$unsafeFlow$1
            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                java.lang.Object objCoroutineScope = kotlinx.coroutines.CoroutineScopeKt.coroutineScope(new kotlinx.coroutines.flow.internal.CombineKt$zipImpl$1$1(flowCollector, flow2, flow, transform, null), continuation);
                return objCoroutineScope == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCoroutineScope : kotlin.Unit.INSTANCE;
            }
        };
    }
}
