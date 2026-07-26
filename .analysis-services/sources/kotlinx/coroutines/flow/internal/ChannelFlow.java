package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: ChannelFlow.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b'\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\n\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\u001c\u0010\u0017\u001a\u00020\u000e2\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00028\u00000\u0019H\u0096@¢\u0006\u0002\u0010\u001aJ\u001c\u0010\u001b\u001a\u00020\u000e2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00028\u00000\fH¤@¢\u0006\u0002\u0010\u001dJ&\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH$J\u0010\u0010\u001f\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010 H\u0016J&\u0010!\u001a\b\u0012\u0004\u0012\u00028\u00000 2\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0016\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#2\u0006\u0010\u001c\u001a\u00020$H\u0016J\b\u0010%\u001a\u00020\u0016H\u0016R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R6\u0010\n\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\u000b8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\b8\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\u00020\u00068@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014¨\u0006&"}, d2 = {"Lkotlinx/coroutines/flow/internal/ChannelFlow;", "T", "Lkotlinx/coroutines/flow/internal/FusibleFlow;", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "(Lkotlin/coroutines/CoroutineContext;ILkotlinx/coroutines/channels/BufferOverflow;)V", "collectToFun", "Lkotlin/Function2;", "Lkotlinx/coroutines/channels/ProducerScope;", "Lkotlin/coroutines/Continuation;", "", "", "getCollectToFun$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()Lkotlin/jvm/functions/Function2;", "produceCapacity", "getProduceCapacity$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()I", "additionalToStringProps", "", "collect", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "collectTo", "scope", "(Lkotlinx/coroutines/channels/ProducerScope;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "create", "dropChannelOperators", "Lkotlinx/coroutines/flow/Flow;", "fuse", "produceImpl", "Lkotlinx/coroutines/channels/ReceiveChannel;", "Lkotlinx/coroutines/CoroutineScope;", "toString", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class ChannelFlow<T> implements kotlinx.coroutines.flow.internal.FusibleFlow<T> {
    public final int capacity;
    public final kotlin.coroutines.CoroutineContext context;
    public final kotlinx.coroutines.channels.BufferOverflow onBufferOverflow;

    @Override // kotlinx.coroutines.flow.Flow
    public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return collect$suspendImpl(this, flowCollector, continuation);
    }

    protected abstract java.lang.Object collectTo(kotlinx.coroutines.channels.ProducerScope<? super T> producerScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);

    protected abstract kotlinx.coroutines.flow.internal.ChannelFlow<T> create(kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow);

    public ChannelFlow(kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        this.context = context;
        this.capacity = capacity;
        this.onBufferOverflow = onBufferOverflow;
        if (!kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            return;
        }
        if (!(this.capacity != -1)) {
            throw new java.lang.AssertionError();
        }
    }

    public final kotlin.jvm.functions.Function2<kotlinx.coroutines.channels.ProducerScope<? super T>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> getCollectToFun$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return new kotlinx.coroutines.flow.internal.ChannelFlow$collectToFun$1(this, null);
    }

    public final int getProduceCapacity$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        if (this.capacity == -3) {
            return -2;
        }
        return this.capacity;
    }

    public kotlinx.coroutines.flow.Flow<T> dropChannelOperators() {
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0043  */
    @Override // kotlinx.coroutines.flow.internal.FusibleFlow
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public kotlinx.coroutines.flow.Flow<T> fuse(kotlin.coroutines.CoroutineContext r8, int r9, kotlinx.coroutines.channels.BufferOverflow r10) {
        /*
            r7 = this;
            java.lang.String r0 = "context"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r8, r0)
            java.lang.String r0 = "onBufferOverflow"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r0)
            boolean r0 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L23
            r0 = 0
            r3 = -1
            if (r9 == r3) goto L19
            r0 = r1
            goto L1a
        L19:
            r0 = r2
        L1a:
            if (r0 == 0) goto L1d
            goto L23
        L1d:
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r0.<init>()
            throw r0
        L23:
            kotlin.coroutines.CoroutineContext r0 = r7.context
            kotlin.coroutines.CoroutineContext r0 = r8.plus(r0)
            r3 = 0
            r4 = 0
            kotlinx.coroutines.channels.BufferOverflow r5 = kotlinx.coroutines.channels.BufferOverflow.SUSPEND
            if (r10 == r5) goto L32
            r1 = r9
            r2 = r10
            goto L82
        L32:
            int r5 = r7.capacity
            r6 = -3
            if (r5 != r6) goto L39
            goto L43
        L39:
            if (r9 != r6) goto L3e
            int r1 = r7.capacity
            goto L7f
        L3e:
            int r5 = r7.capacity
            r6 = -2
            if (r5 != r6) goto L45
        L43:
            r1 = r9
            goto L7f
        L45:
            if (r9 != r6) goto L4a
            int r1 = r7.capacity
            goto L7f
        L4a:
            boolean r5 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()
            if (r5 == 0) goto L61
            r5 = 0
            int r6 = r7.capacity
            if (r6 < 0) goto L57
            r5 = r1
            goto L58
        L57:
            r5 = r2
        L58:
            if (r5 == 0) goto L5b
            goto L61
        L5b:
            java.lang.AssertionError r1 = new java.lang.AssertionError
            r1.<init>()
            throw r1
        L61:
            boolean r5 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()
            if (r5 == 0) goto L75
            r5 = 0
            if (r9 < 0) goto L6b
            goto L6c
        L6b:
            r1 = r2
        L6c:
            if (r1 == 0) goto L6f
            goto L75
        L6f:
            java.lang.AssertionError r1 = new java.lang.AssertionError
            r1.<init>()
            throw r1
        L75:
            int r1 = r7.capacity
            int r1 = r1 + r9
            if (r1 < 0) goto L7b
            goto L7f
        L7b:
            r2 = 2147483647(0x7fffffff, float:NaN)
            r1 = r2
        L7f:
            kotlinx.coroutines.channels.BufferOverflow r2 = r7.onBufferOverflow
        L82:
            kotlin.coroutines.CoroutineContext r3 = r7.context
            boolean r3 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r3)
            if (r3 == 0) goto L96
            int r3 = r7.capacity
            if (r1 != r3) goto L96
            kotlinx.coroutines.channels.BufferOverflow r3 = r7.onBufferOverflow
            if (r2 != r3) goto L96
            r3 = r7
            kotlinx.coroutines.flow.Flow r3 = (kotlinx.coroutines.flow.Flow) r3
            return r3
        L96:
            kotlinx.coroutines.flow.internal.ChannelFlow r3 = r7.create(r0, r1, r2)
            kotlinx.coroutines.flow.Flow r3 = (kotlinx.coroutines.flow.Flow) r3
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.internal.ChannelFlow.fuse(kotlin.coroutines.CoroutineContext, int, kotlinx.coroutines.channels.BufferOverflow):kotlinx.coroutines.flow.Flow");
    }

    public kotlinx.coroutines.channels.ReceiveChannel<T> produceImpl(kotlinx.coroutines.CoroutineScope scope) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scope, "scope");
        return kotlinx.coroutines.channels.ProduceKt.produce(scope, (16 & 1) != 0 ? kotlin.coroutines.EmptyCoroutineContext.INSTANCE : this.context, (16 & 2) != 0 ? 0 : getProduceCapacity$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(), (16 & 4) != 0 ? kotlinx.coroutines.channels.BufferOverflow.SUSPEND : this.onBufferOverflow, (16 & 8) != 0 ? kotlinx.coroutines.CoroutineStart.DEFAULT : kotlinx.coroutines.CoroutineStart.ATOMIC, (16 & 16) != 0 ? null : null, getCollectToFun$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host());
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.internal.ChannelFlow$collect$2, reason: invalid class name */
    /* JADX INFO: compiled from: ChannelFlow.kt */
    @kotlin.Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.internal.ChannelFlow$collect$2", f = "ChannelFlow.kt", i = {}, l = {123}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass2 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ kotlinx.coroutines.flow.FlowCollector<T> $collector;
        private /* synthetic */ java.lang.Object L$0;
        int label;
        final /* synthetic */ kotlinx.coroutines.flow.internal.ChannelFlow<T> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass2(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlinx.coroutines.flow.internal.ChannelFlow<T> channelFlow, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.internal.ChannelFlow.AnonymousClass2> continuation) {
            super(2, continuation);
            this.$collector = flowCollector;
            this.this$0 = channelFlow;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlinx.coroutines.flow.internal.ChannelFlow.AnonymousClass2 anonymousClass2 = new kotlinx.coroutines.flow.internal.ChannelFlow.AnonymousClass2(this.$collector, this.this$0, continuation);
            anonymousClass2.L$0 = obj;
            return anonymousClass2;
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.flow.internal.ChannelFlow.AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    kotlinx.coroutines.CoroutineScope $this$coroutineScope = (kotlinx.coroutines.CoroutineScope) this.L$0;
                    this.label = 1;
                    if (kotlinx.coroutines.flow.FlowKt.emitAll(this.$collector, this.this$0.produceImpl($this$coroutineScope), this) == coroutine_suspended) {
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

    static /* synthetic */ <T> java.lang.Object collect$suspendImpl(kotlinx.coroutines.flow.internal.ChannelFlow<T> channelFlow, kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        java.lang.Object objCoroutineScope = kotlinx.coroutines.CoroutineScopeKt.coroutineScope(new kotlinx.coroutines.flow.internal.ChannelFlow.AnonymousClass2(flowCollector, channelFlow, null), continuation);
        return objCoroutineScope == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCoroutineScope : kotlin.Unit.INSTANCE;
    }

    protected java.lang.String additionalToStringProps() {
        return null;
    }

    public java.lang.String toString() {
        java.util.ArrayList props = new java.util.ArrayList(4);
        java.lang.String it = additionalToStringProps();
        if (it != null) {
            props.add(it);
        }
        if (this.context != kotlin.coroutines.EmptyCoroutineContext.INSTANCE) {
            props.add("context=" + this.context);
        }
        if (this.capacity != -3) {
            props.add("capacity=" + this.capacity);
        }
        if (this.onBufferOverflow != kotlinx.coroutines.channels.BufferOverflow.SUSPEND) {
            props.add("onBufferOverflow=" + this.onBufferOverflow);
        }
        return kotlinx.coroutines.DebugStringsKt.getClassSimpleName(this) + "[" + kotlin.collections.CollectionsKt.joinToString$default(props, ", ", null, null, 0, null, null, 62, null) + "]";
    }
}
