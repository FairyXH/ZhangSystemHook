package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: Merge.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B?\u0012\u0012\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00040\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0006\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\b\u0010\r\u001a\u00020\u000eH\u0014J\u001c\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00028\u00000\u0012H\u0094@¢\u0006\u0002\u0010\u0013J&\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000bH\u0014J\u0016\u0010\u0015\u001a\b\u0012\u0004\u0012\u00028\u00000\u00162\u0006\u0010\u0011\u001a\u00020\u0017H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lkotlinx/coroutines/flow/internal/ChannelFlowMerge;", "T", "Lkotlinx/coroutines/flow/internal/ChannelFlow;", "flow", "Lkotlinx/coroutines/flow/Flow;", "concurrency", "", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "(Lkotlinx/coroutines/flow/Flow;ILkotlin/coroutines/CoroutineContext;ILkotlinx/coroutines/channels/BufferOverflow;)V", "additionalToStringProps", "", "collectTo", "", "scope", "Lkotlinx/coroutines/channels/ProducerScope;", "(Lkotlinx/coroutines/channels/ProducerScope;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "create", "produceImpl", "Lkotlinx/coroutines/channels/ReceiveChannel;", "Lkotlinx/coroutines/CoroutineScope;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ChannelFlowMerge<T> extends kotlinx.coroutines.flow.internal.ChannelFlow<T> {
    private final int concurrency;
    private final kotlinx.coroutines.flow.Flow<kotlinx.coroutines.flow.Flow<T>> flow;

    public /* synthetic */ ChannelFlowMerge(kotlinx.coroutines.flow.Flow flow, int i, kotlin.coroutines.CoroutineContext coroutineContext, int i2, kotlinx.coroutines.channels.BufferOverflow bufferOverflow, int i3, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(flow, i, (i3 & 4) != 0 ? kotlin.coroutines.EmptyCoroutineContext.INSTANCE : coroutineContext, (i3 & 8) != 0 ? -2 : i2, (i3 & 16) != 0 ? kotlinx.coroutines.channels.BufferOverflow.SUSPEND : bufferOverflow);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public ChannelFlowMerge(kotlinx.coroutines.flow.Flow<? extends kotlinx.coroutines.flow.Flow<? extends T>> flow, int concurrency, kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        super(context, capacity, onBufferOverflow);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        this.flow = flow;
        this.concurrency = concurrency;
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow
    protected kotlinx.coroutines.flow.internal.ChannelFlow<T> create(kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        return new kotlinx.coroutines.flow.internal.ChannelFlowMerge(this.flow, this.concurrency, context, capacity, onBufferOverflow);
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow
    public kotlinx.coroutines.channels.ReceiveChannel<T> produceImpl(kotlinx.coroutines.CoroutineScope scope) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scope, "scope");
        return kotlinx.coroutines.channels.ProduceKt.produce(scope, this.context, this.capacity, getCollectToFun$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host());
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow
    protected java.lang.Object collectTo(kotlinx.coroutines.channels.ProducerScope<? super T> producerScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlinx.coroutines.sync.Semaphore semaphore = kotlinx.coroutines.sync.SemaphoreKt.Semaphore$default(this.concurrency, 0, 2, null);
        kotlinx.coroutines.flow.internal.SendingCollector collector = new kotlinx.coroutines.flow.internal.SendingCollector(producerScope);
        kotlinx.coroutines.Job job = (kotlinx.coroutines.Job) continuation.getContext().get(kotlinx.coroutines.Job.INSTANCE);
        java.lang.Object objCollect = this.flow.collect(new kotlinx.coroutines.flow.internal.ChannelFlowMerge.AnonymousClass2(job, semaphore, producerScope, collector), continuation);
        return objCollect == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$2, reason: invalid class name */
    /* JADX INFO: compiled from: Merge.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "inner", "Lkotlinx/coroutines/flow/Flow;", "emit", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    static final class AnonymousClass2<T> implements kotlinx.coroutines.flow.FlowCollector {
        final /* synthetic */ kotlinx.coroutines.flow.internal.SendingCollector<T> $collector;
        final /* synthetic */ kotlinx.coroutines.Job $job;
        final /* synthetic */ kotlinx.coroutines.channels.ProducerScope<T> $scope;
        final /* synthetic */ kotlinx.coroutines.sync.Semaphore $semaphore;

        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass2(kotlinx.coroutines.Job job, kotlinx.coroutines.sync.Semaphore semaphore, kotlinx.coroutines.channels.ProducerScope<? super T> producerScope, kotlinx.coroutines.flow.internal.SendingCollector<T> sendingCollector) {
            this.$job = job;
            this.$semaphore = semaphore;
            this.$scope = producerScope;
            this.$collector = sendingCollector;
        }

        @Override // kotlinx.coroutines.flow.FlowCollector
        public /* bridge */ /* synthetic */ java.lang.Object emit(java.lang.Object value, kotlin.coroutines.Continuation $completion) {
            return emit((kotlinx.coroutines.flow.Flow) value, (kotlin.coroutines.Continuation<? super kotlin.Unit>) $completion);
        }

        /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final java.lang.Object emit(kotlinx.coroutines.flow.Flow<? extends T> r10, kotlin.coroutines.Continuation<? super kotlin.Unit> r11) throws java.lang.Throwable {
            /*
                r9 = this;
                boolean r0 = r11 instanceof kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$2$emit$1
                if (r0 == 0) goto L14
                r0 = r11
                kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$2$emit$1 r0 = (kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$2$emit$1) r0
                int r1 = r0.label
                r2 = -2147483648(0xffffffff80000000, float:-0.0)
                r1 = r1 & r2
                if (r1 == 0) goto L14
                int r11 = r0.label
                int r11 = r11 - r2
                r0.label = r11
                goto L19
            L14:
                kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$2$emit$1 r0 = new kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$2$emit$1
                r0.<init>(r9, r11)
            L19:
                r11 = r0
                java.lang.Object r0 = r11.result
                java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                int r2 = r11.label
                switch(r2) {
                    case 0: goto L39;
                    case 1: goto L2d;
                    default: goto L25;
                }
            L25:
                java.lang.IllegalStateException r10 = new java.lang.IllegalStateException
                java.lang.String r11 = "call to 'resume' before 'invoke' with coroutine"
                r10.<init>(r11)
                throw r10
            L2d:
                java.lang.Object r10 = r11.L$1
                kotlinx.coroutines.flow.Flow r10 = (kotlinx.coroutines.flow.Flow) r10
                java.lang.Object r1 = r11.L$0
                kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$2 r1 = (kotlinx.coroutines.flow.internal.ChannelFlowMerge.AnonymousClass2) r1
                kotlin.ResultKt.throwOnFailure(r0)
                goto L55
            L39:
                kotlin.ResultKt.throwOnFailure(r0)
                r2 = r9
                kotlinx.coroutines.Job r3 = r2.$job
                if (r3 == 0) goto L44
                kotlinx.coroutines.JobKt.ensureActive(r3)
            L44:
                kotlinx.coroutines.sync.Semaphore r3 = r2.$semaphore
                r11.L$0 = r2
                r11.L$1 = r10
                r4 = 1
                r11.label = r4
                java.lang.Object r3 = r3.acquire(r11)
                if (r3 != r1) goto L54
                return r1
            L54:
                r1 = r2
            L55:
                kotlinx.coroutines.channels.ProducerScope<T> r2 = r1.$scope
                r3 = r2
                kotlinx.coroutines.CoroutineScope r3 = (kotlinx.coroutines.CoroutineScope) r3
                kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$2$1 r2 = new kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$2$1
                kotlinx.coroutines.flow.internal.SendingCollector<T> r4 = r1.$collector
                kotlinx.coroutines.sync.Semaphore r5 = r1.$semaphore
                r6 = 0
                r2.<init>(r10, r4, r5, r6)
                r6 = r2
                kotlin.jvm.functions.Function2 r6 = (kotlin.jvm.functions.Function2) r6
                r7 = 3
                r8 = 0
                r4 = 0
                r5 = 0
                kotlinx.coroutines.BuildersKt.launch$default(r3, r4, r5, r6, r7, r8)
                kotlin.Unit r2 = kotlin.Unit.INSTANCE
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.internal.ChannelFlowMerge.AnonymousClass2.emit(kotlinx.coroutines.flow.Flow, kotlin.coroutines.Continuation):java.lang.Object");
        }

        /* JADX INFO: renamed from: kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$2$1, reason: invalid class name */
        /* JADX INFO: compiled from: Merge.kt */
        @kotlin.Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
        @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$2$1", f = "Merge.kt", i = {}, l = {69}, m = "invokeSuspend", n = {}, s = {})
        static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
            final /* synthetic */ kotlinx.coroutines.flow.internal.SendingCollector<T> $collector;
            final /* synthetic */ kotlinx.coroutines.flow.Flow<T> $inner;
            final /* synthetic */ kotlinx.coroutines.sync.Semaphore $semaphore;
            int label;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            AnonymousClass1(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.flow.internal.SendingCollector<T> sendingCollector, kotlinx.coroutines.sync.Semaphore semaphore, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.internal.ChannelFlowMerge.AnonymousClass2.AnonymousClass1> continuation) {
                super(2, continuation);
                this.$inner = flow;
                this.$collector = sendingCollector;
                this.$semaphore = semaphore;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
                return new kotlinx.coroutines.flow.internal.ChannelFlowMerge.AnonymousClass2.AnonymousClass1(this.$inner, this.$collector, this.$semaphore, continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                return ((kotlinx.coroutines.flow.internal.ChannelFlowMerge.AnonymousClass2.AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
                java.lang.Throwable th;
                kotlinx.coroutines.flow.internal.ChannelFlowMerge.AnonymousClass2.AnonymousClass1 anonymousClass1;
                java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        kotlin.ResultKt.throwOnFailure($result);
                        try {
                            this.label = 1;
                            if (this.$inner.collect(this.$collector, this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            anonymousClass1 = this;
                            anonymousClass1.$semaphore.release();
                            return kotlin.Unit.INSTANCE;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            anonymousClass1 = this;
                            anonymousClass1.$semaphore.release();
                            throw th;
                        }
                    case 1:
                        anonymousClass1 = this;
                        try {
                            kotlin.ResultKt.throwOnFailure($result);
                            anonymousClass1.$semaphore.release();
                            return kotlin.Unit.INSTANCE;
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                            anonymousClass1.$semaphore.release();
                            throw th;
                        }
                    default:
                        throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
        }
    }

    @Override // kotlinx.coroutines.flow.internal.ChannelFlow
    protected java.lang.String additionalToStringProps() {
        return "concurrency=" + this.concurrency;
    }
}
