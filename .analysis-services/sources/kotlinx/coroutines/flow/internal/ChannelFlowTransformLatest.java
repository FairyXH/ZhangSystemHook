package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: Merge.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0003Bu\u0012B\u0010\u0004\u001a>\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u0006\u0012\u0013\u0012\u00118\u0000¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u0005¢\u0006\u0002\b\r\u0012\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u000f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0013\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0015¢\u0006\u0002\u0010\u0016J&\u0010\u0018\u001a\b\u0012\u0004\u0012\u00028\u00010\u00192\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0014J\u001c\u0010\u001a\u001a\u00020\u000b2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00028\u00010\u0006H\u0094@¢\u0006\u0002\u0010\u001cRL\u0010\u0004\u001a>\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u0006\u0012\u0013\u0012\u00118\u0000¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u0005¢\u0006\u0002\b\rX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0017¨\u0006\u001d"}, d2 = {"Lkotlinx/coroutines/flow/internal/ChannelFlowTransformLatest;", "T", "R", "Lkotlinx/coroutines/flow/internal/ChannelFlowOperator;", "transform", "Lkotlin/Function3;", "Lkotlinx/coroutines/flow/FlowCollector;", "Lkotlin/ParameterName;", "name", "value", "Lkotlin/coroutines/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "flow", "Lkotlinx/coroutines/flow/Flow;", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "(Lkotlin/jvm/functions/Function3;Lkotlinx/coroutines/flow/Flow;Lkotlin/coroutines/CoroutineContext;ILkotlinx/coroutines/channels/BufferOverflow;)V", "Lkotlin/jvm/functions/Function3;", "create", "Lkotlinx/coroutines/flow/internal/ChannelFlow;", "flowCollect", "collector", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ChannelFlowTransformLatest<T, R> extends kotlinx.coroutines.flow.internal.ChannelFlowOperator<T, R> {
    private final kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> transform;

    public /* synthetic */ ChannelFlowTransformLatest(kotlin.jvm.functions.Function3 function3, kotlinx.coroutines.flow.Flow flow, kotlin.coroutines.CoroutineContext coroutineContext, int i, kotlinx.coroutines.channels.BufferOverflow bufferOverflow, int i2, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(function3, flow, (i2 & 4) != 0 ? kotlin.coroutines.EmptyCoroutineContext.INSTANCE : coroutineContext, (i2 & 8) != 0 ? -2 : i, (i2 & 16) != 0 ? kotlinx.coroutines.channels.BufferOverflow.SUSPEND : bufferOverflow);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public ChannelFlowTransformLatest(kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> transform, kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        super(flow, context, capacity, onBufferOverflow);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        this.transform = transform;
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow
    protected kotlinx.coroutines.flow.internal.ChannelFlow<R> create(kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        return new kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest(this.transform, this.flow, context, capacity, onBufferOverflow);
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3, reason: invalid class name */
    /* JADX INFO: compiled from: Merge.kt */
    @kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3", f = "Merge.kt", i = {}, l = {27}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass3 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ kotlinx.coroutines.flow.FlowCollector<R> $collector;
        private /* synthetic */ java.lang.Object L$0;
        int label;
        final /* synthetic */ kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest<T, R> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass3(kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest<T, R> channelFlowTransformLatest, kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest.AnonymousClass3> continuation) {
            super(2, continuation);
            this.this$0 = channelFlowTransformLatest;
            this.$collector = flowCollector;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest.AnonymousClass3 anonymousClass3 = new kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest.AnonymousClass3(this.this$0, this.$collector, continuation);
            anonymousClass3.L$0 = obj;
            return anonymousClass3;
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest.AnonymousClass3) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        /* JADX WARN: Type inference incomplete: some casts might be missing */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure(obj);
                    kotlinx.coroutines.CoroutineScope coroutineScope = (kotlinx.coroutines.CoroutineScope) this.L$0;
                    kotlin.jvm.internal.Ref.ObjectRef objectRef = new kotlin.jvm.internal.Ref.ObjectRef();
                    kotlinx.coroutines.flow.Flow<S> flow = this.this$0.flow;
                    kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest.AnonymousClass3.AnonymousClass1 anonymousClass1 = new kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest.AnonymousClass3.AnonymousClass1(objectRef, coroutineScope, this.this$0, this.$collector);
                    this.label = 1;
                    if (flow.collect(anonymousClass1, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    kotlin.ResultKt.throwOnFailure(obj);
                    break;
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return kotlin.Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1, reason: invalid class name */
        /* JADX INFO: compiled from: Merge.kt */
        @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "R", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"}, k = 3, mv = {1, 9, 0}, xi = 48)
        static final class AnonymousClass1<T> implements kotlinx.coroutines.flow.FlowCollector {
            final /* synthetic */ kotlinx.coroutines.CoroutineScope $$this$coroutineScope;
            final /* synthetic */ kotlinx.coroutines.flow.FlowCollector<R> $collector;
            final /* synthetic */ kotlin.jvm.internal.Ref.ObjectRef<kotlinx.coroutines.Job> $previousFlow;
            final /* synthetic */ kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest<T, R> this$0;

            /* JADX WARN: Multi-variable type inference failed */
            AnonymousClass1(kotlin.jvm.internal.Ref.ObjectRef<kotlinx.coroutines.Job> objectRef, kotlinx.coroutines.CoroutineScope coroutineScope, kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest<T, R> channelFlowTransformLatest, kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector) {
                this.$previousFlow = objectRef;
                this.$$this$coroutineScope = coroutineScope;
                this.this$0 = channelFlowTransformLatest;
                this.$collector = flowCollector;
            }

            /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
            @Override // kotlinx.coroutines.flow.FlowCollector
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final java.lang.Object emit(T r10, kotlin.coroutines.Continuation<? super kotlin.Unit> r11) throws java.lang.Throwable {
                /*
                    r9 = this;
                    boolean r0 = r11 instanceof kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$emit$1
                    if (r0 == 0) goto L14
                    r0 = r11
                    kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$emit$1 r0 = (kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$emit$1) r0
                    int r1 = r0.label
                    r2 = -2147483648(0xffffffff80000000, float:-0.0)
                    r1 = r1 & r2
                    if (r1 == 0) goto L14
                    int r11 = r0.label
                    int r11 = r11 - r2
                    r0.label = r11
                    goto L19
                L14:
                    kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$emit$1 r0 = new kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$emit$1
                    r0.<init>(r9, r11)
                L19:
                    r11 = r0
                    java.lang.Object r0 = r11.result
                    java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                    int r2 = r11.label
                    switch(r2) {
                        case 0: goto L3c;
                        case 1: goto L2d;
                        default: goto L25;
                    }
                L25:
                    java.lang.IllegalStateException r10 = new java.lang.IllegalStateException
                    java.lang.String r11 = "call to 'resume' before 'invoke' with coroutine"
                    r10.<init>(r11)
                    throw r10
                L2d:
                    r10 = 0
                    java.lang.Object r1 = r11.L$2
                    kotlinx.coroutines.Job r1 = (kotlinx.coroutines.Job) r1
                    java.lang.Object r1 = r11.L$1
                    java.lang.Object r2 = r11.L$0
                    kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1 r2 = (kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest.AnonymousClass3.AnonymousClass1) r2
                    kotlin.ResultKt.throwOnFailure(r0)
                    goto L66
                L3c:
                    kotlin.ResultKt.throwOnFailure(r0)
                    r2 = r9
                    kotlin.jvm.internal.Ref$ObjectRef<kotlinx.coroutines.Job> r3 = r2.$previousFlow
                    T r3 = r3.element
                    kotlinx.coroutines.Job r3 = (kotlinx.coroutines.Job) r3
                    if (r3 == 0) goto L68
                    r4 = r3
                    r5 = 0
                    kotlinx.coroutines.flow.internal.ChildCancelledException r6 = new kotlinx.coroutines.flow.internal.ChildCancelledException
                    r6.<init>()
                    java.util.concurrent.CancellationException r6 = (java.util.concurrent.CancellationException) r6
                    r4.cancel(r6)
                    r11.L$0 = r2
                    r11.L$1 = r10
                    r11.L$2 = r3
                    r3 = 1
                    r11.label = r3
                    java.lang.Object r3 = r4.join(r11)
                    if (r3 != r1) goto L64
                    return r1
                L64:
                    r1 = r10
                    r10 = r5
                L66:
                    r10 = r1
                L68:
                    kotlin.jvm.internal.Ref$ObjectRef<kotlinx.coroutines.Job> r1 = r2.$previousFlow
                    kotlinx.coroutines.CoroutineScope r3 = r2.$$this$coroutineScope
                    kotlinx.coroutines.CoroutineStart r5 = kotlinx.coroutines.CoroutineStart.UNDISPATCHED
                    kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$2 r4 = new kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$2
                    kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest<T, R> r6 = r2.this$0
                    kotlinx.coroutines.flow.FlowCollector<R> r7 = r2.$collector
                    r8 = 0
                    r4.<init>(r6, r7, r10, r8)
                    r6 = r4
                    kotlin.jvm.functions.Function2 r6 = (kotlin.jvm.functions.Function2) r6
                    r7 = 1
                    r4 = 0
                    kotlinx.coroutines.Job r3 = kotlinx.coroutines.BuildersKt.launch$default(r3, r4, r5, r6, r7, r8)
                    r1.element = r3
                    kotlin.Unit r1 = kotlin.Unit.INSTANCE
                    return r1
                */
                throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest.AnonymousClass3.AnonymousClass1.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
            }

            /* JADX INFO: renamed from: kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$2, reason: invalid class name */
            /* JADX INFO: compiled from: Merge.kt */
            @kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
            @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$2", f = "Merge.kt", i = {}, l = {34}, m = "invokeSuspend", n = {}, s = {})
            static final class AnonymousClass2 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
                final /* synthetic */ kotlinx.coroutines.flow.FlowCollector<R> $collector;
                final /* synthetic */ T $value;
                int label;
                final /* synthetic */ kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest<T, R> this$0;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                /* JADX WARN: Multi-variable type inference failed */
                AnonymousClass2(kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest<T, R> channelFlowTransformLatest, kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, T t, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest.AnonymousClass3.AnonymousClass1.AnonymousClass2> continuation) {
                    super(2, continuation);
                    this.this$0 = channelFlowTransformLatest;
                    this.$collector = flowCollector;
                    this.$value = t;
                }

                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
                    return new kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest.AnonymousClass3.AnonymousClass1.AnonymousClass2(this.this$0, this.$collector, this.$value, continuation);
                }

                @Override // kotlin.jvm.functions.Function2
                public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                    return ((kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest.AnonymousClass3.AnonymousClass1.AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
                }

                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
                    java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    switch (this.label) {
                        case 0:
                            kotlin.ResultKt.throwOnFailure($result);
                            kotlin.jvm.functions.Function3 function3 = ((kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest) this.this$0).transform;
                            kotlinx.coroutines.flow.FlowCollector<R> flowCollector = this.$collector;
                            T t = this.$value;
                            this.label = 1;
                            if (function3.invoke(flowCollector, t, this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            break;
                        case 1:
                            kotlin.ResultKt.throwOnFailure($result);
                            break;
                        default:
                            throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    return kotlin.Unit.INSTANCE;
                }
            }
        }
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlowOperator
    protected java.lang.Object flowCollect(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !(flowCollector instanceof kotlinx.coroutines.flow.internal.SendingCollector)) {
            throw new java.lang.AssertionError();
        }
        java.lang.Object objCoroutineScope = kotlinx.coroutines.CoroutineScopeKt.coroutineScope(new kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest.AnonymousClass3(this, flowCollector, null), continuation);
        return objCoroutineScope == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCoroutineScope : kotlin.Unit.INSTANCE;
    }
}
