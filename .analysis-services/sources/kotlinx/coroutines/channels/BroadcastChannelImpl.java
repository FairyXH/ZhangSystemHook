package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: BroadcastChannel.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\n\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u000245B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0017\u0010\u001f\u001a\u00020\n2\b\u0010 \u001a\u0004\u0018\u00010!H\u0010¢\u0006\u0002\b\"J\u0012\u0010#\u001a\u00020\n2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\u000e\u0010$\u001a\b\u0012\u0004\u0012\u00028\u00000%H\u0016J\u001e\u0010&\u001a\u00020'2\n\u0010(\u001a\u0006\u0012\u0002\b\u00030\u00132\b\u0010)\u001a\u0004\u0018\u00010\rH\u0014J\u0016\u0010*\u001a\u00020'2\f\u0010+\u001a\b\u0012\u0004\u0012\u00028\u00000%H\u0002J\u0016\u0010,\u001a\u00020'2\u0006\u0010)\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010-J\b\u0010.\u001a\u00020/H\u0016J#\u00100\u001a\b\u0012\u0004\u0012\u00020'012\u0006\u0010)\u001a\u00028\u0000H\u0016ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b2\u00103R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\u000bR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000e\u001a\u00060\u000fj\u0002`\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R6\u0010\u0011\u001a*\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0013\u0012\u0006\u0012\u0004\u0018\u00010\r0\u0012j\u0014\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0013\u0012\u0006\u0012\u0004\u0018\u00010\r`\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0017\u0010\u0017\u001a\u00028\u00008F¢\u0006\f\u0012\u0004\b\u0018\u0010\u0019\u001a\u0004\b\u001a\u0010\u001bR\u0019\u0010\u001c\u001a\u0004\u0018\u00018\u00008F¢\u0006\f\u0012\u0004\b\u001d\u0010\u0019\u001a\u0004\b\u001e\u0010\u001b\u0082\u0002\u000b\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u00066"}, d2 = {"Lkotlinx/coroutines/channels/BroadcastChannelImpl;", "E", "Lkotlinx/coroutines/channels/BufferedChannel;", "Lkotlinx/coroutines/channels/BroadcastChannel;", "capacity", "", "(I)V", "getCapacity", "()I", "isClosedForSend", "", "()Z", "lastConflatedElement", "", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/internal/ReentrantLock;", "onSendInternalResult", "Ljava/util/HashMap;", "Lkotlinx/coroutines/selects/SelectInstance;", "Lkotlin/collections/HashMap;", "subscribers", "", "value", "getValue$annotations", "()V", "getValue", "()Ljava/lang/Object;", "valueOrNull", "getValueOrNull$annotations", "getValueOrNull", "cancelImpl", "cause", "", "cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "close", "openSubscription", "Lkotlinx/coroutines/channels/ReceiveChannel;", "registerSelectForSend", "", "select", "element", "removeSubscriber", "s", "send", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toString", "", "trySend", "Lkotlinx/coroutines/channels/ChannelResult;", "trySend-JP2dKIU", "(Ljava/lang/Object;)Ljava/lang/Object;", "SubscriberBuffered", "SubscriberConflated", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class BroadcastChannelImpl<E> extends kotlinx.coroutines.channels.BufferedChannel<E> implements kotlinx.coroutines.channels.BroadcastChannel<E> {
    private final int capacity;
    private java.lang.Object lastConflatedElement;
    private final java.util.concurrent.locks.ReentrantLock lock;
    private final java.util.HashMap<kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object> onSendInternalResult;
    private java.util.List<? extends kotlinx.coroutines.channels.BufferedChannel<E>> subscribers;

    /* JADX INFO: renamed from: kotlinx.coroutines.channels.BroadcastChannelImpl$send$1, reason: invalid class name */
    /* JADX INFO: compiled from: BroadcastChannel.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.channels.BroadcastChannelImpl", f = "BroadcastChannel.kt", i = {0, 0}, l = {com.android.internal.util.FrameworkStatsLog.APP_PROCESS_DIED__IMPORTANCE__IMPORTANCE_PERCEPTIBLE}, m = "send", n = {"this", "element"}, s = {"L$0", "L$1"})
    static final class AnonymousClass1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        java.lang.Object L$0;
        java.lang.Object L$1;
        java.lang.Object L$2;
        int label;
        /* synthetic */ java.lang.Object result;
        final /* synthetic */ kotlinx.coroutines.channels.BroadcastChannelImpl<E> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(kotlinx.coroutines.channels.BroadcastChannelImpl<E> broadcastChannelImpl, kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.BroadcastChannelImpl.AnonymousClass1> continuation) {
            super(continuation);
            this.this$0 = broadcastChannelImpl;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return this.this$0.send(null, this);
        }
    }

    public static /* synthetic */ void getValue$annotations() {
    }

    public static /* synthetic */ void getValueOrNull$annotations() {
    }

    public final int getCapacity() {
        return this.capacity;
    }

    public BroadcastChannelImpl(int capacity) {
        super(0, null);
        this.capacity = capacity;
        if (!(this.capacity >= 1 || this.capacity == -1)) {
            throw new java.lang.IllegalArgumentException(("BroadcastChannel capacity must be positive or Channel.CONFLATED, but " + this.capacity + " was specified").toString());
        }
        this.lock = new java.util.concurrent.locks.ReentrantLock();
        this.subscribers = kotlin.collections.CollectionsKt.emptyList();
        this.lastConflatedElement = kotlinx.coroutines.channels.BroadcastChannelKt.NO_ELEMENT;
        this.onSendInternalResult = new java.util.HashMap<>();
    }

    @Override // kotlinx.coroutines.channels.BroadcastChannel
    public kotlinx.coroutines.channels.ReceiveChannel<E> openSubscription() {
        java.util.concurrent.locks.ReentrantLock $this$withLock$iv = this.lock;
        java.util.concurrent.locks.ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            kotlinx.coroutines.channels.BufferedChannel s = this.capacity == -1 ? new kotlinx.coroutines.channels.BroadcastChannelImpl.SubscriberConflated() : new kotlinx.coroutines.channels.BroadcastChannelImpl.SubscriberBuffered();
            if (!isClosedForSend() || this.lastConflatedElement != kotlinx.coroutines.channels.BroadcastChannelKt.NO_ELEMENT) {
                if (this.lastConflatedElement != kotlinx.coroutines.channels.BroadcastChannelKt.NO_ELEMENT) {
                    s.mo12809trySendJP2dKIU(getValue());
                }
                this.subscribers = kotlin.collections.CollectionsKt.plus((java.util.Collection<? extends kotlinx.coroutines.channels.BufferedChannel>) this.subscribers, s);
                reentrantLock.unlock();
                return s;
            }
            s.close(getCloseCause());
            return s;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void removeSubscriber(kotlinx.coroutines.channels.ReceiveChannel<? extends E> s) {
        java.util.concurrent.locks.ReentrantLock $this$withLock$iv = this.lock;
        java.util.concurrent.locks.ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            java.lang.Iterable $this$filter$iv = this.subscribers;
            java.util.ArrayList arrayList = new java.util.ArrayList();
            for (java.lang.Object element$iv$iv : $this$filter$iv) {
                kotlinx.coroutines.channels.BufferedChannel it = (kotlinx.coroutines.channels.BufferedChannel) element$iv$iv;
                if (it != s) {
                    arrayList.add(element$iv$iv);
                }
            }
            this.subscribers = arrayList;
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x008a -> B:27:0x008e). Please report as a decompilation issue!!! */
    @Override // kotlinx.coroutines.channels.BufferedChannel, kotlinx.coroutines.channels.SendChannel
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object send(E r10, kotlin.coroutines.Continuation<? super kotlin.Unit> r11) throws java.lang.Throwable {
        /*
            r9 = this;
            boolean r0 = r11 instanceof kotlinx.coroutines.channels.BroadcastChannelImpl.AnonymousClass1
            if (r0 == 0) goto L14
            r0 = r11
            kotlinx.coroutines.channels.BroadcastChannelImpl$send$1 r0 = (kotlinx.coroutines.channels.BroadcastChannelImpl.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r11 = r0.label
            int r11 = r11 - r2
            r0.label = r11
            goto L19
        L14:
            kotlinx.coroutines.channels.BroadcastChannelImpl$send$1 r0 = new kotlinx.coroutines.channels.BroadcastChannelImpl$send$1
            r0.<init>(r9, r11)
        L19:
            r11 = r0
            java.lang.Object r0 = r11.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r11.label
            switch(r2) {
                case 0: goto L40;
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
            r2 = 0
            java.lang.Object r3 = r11.L$2
            java.util.Iterator r3 = (java.util.Iterator) r3
            java.lang.Object r4 = r11.L$1
            java.lang.Object r5 = r11.L$0
            kotlinx.coroutines.channels.BroadcastChannelImpl r5 = (kotlinx.coroutines.channels.BroadcastChannelImpl) r5
            kotlin.ResultKt.throwOnFailure(r0)
            r6 = r2
            r2 = r1
            r1 = r0
            goto L8e
        L40:
            kotlin.ResultKt.throwOnFailure(r0)
            r2 = r9
            java.util.concurrent.locks.ReentrantLock r3 = r2.lock
            r4 = 0
            r5 = r3
            java.util.concurrent.locks.Lock r5 = (java.util.concurrent.locks.Lock) r5
            r5.lock()
            r3 = 0
            boolean r6 = r2.isClosedForSend()     // Catch: java.lang.Throwable -> Laf
            if (r6 != 0) goto Laa
            int r6 = r2.capacity     // Catch: java.lang.Throwable -> Laf
            r7 = -1
            if (r6 != r7) goto L5b
            r2.lastConflatedElement = r10     // Catch: java.lang.Throwable -> Laf
        L5b:
            java.util.List<? extends kotlinx.coroutines.channels.BufferedChannel<E>> r6 = r2.subscribers     // Catch: java.lang.Throwable -> Laf
            r5.unlock()
            r3 = r6
            java.lang.Iterable r3 = (java.lang.Iterable) r3
            r4 = 0
            java.util.Iterator r5 = r3.iterator()
            r3 = r5
            r5 = r2
            r8 = r4
            r4 = r10
            r10 = r8
        L6d:
            boolean r2 = r3.hasNext()
            if (r2 == 0) goto La6
            java.lang.Object r2 = r3.next()
            kotlinx.coroutines.channels.BufferedChannel r2 = (kotlinx.coroutines.channels.BufferedChannel) r2
            r6 = 0
            r11.L$0 = r5
            r11.L$1 = r4
            r11.L$2 = r3
            r7 = 1
            r11.label = r7
            java.lang.Object r2 = r2.sendBroadcast$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r4, r11)
            if (r2 != r1) goto L8a
            return r1
        L8a:
            r8 = r1
            r1 = r0
            r0 = r2
            r2 = r8
        L8e:
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 != 0) goto La2
            boolean r7 = r5.isClosedForSend()
            if (r7 != 0) goto L9d
            goto La2
        L9d:
            java.lang.Throwable r2 = r5.getSendException()
            throw r2
        La2:
            r0 = r1
            r1 = r2
            goto L6d
        La6:
            kotlin.Unit r10 = kotlin.Unit.INSTANCE
            return r10
        Laa:
            java.lang.Throwable r1 = r2.getSendException()     // Catch: java.lang.Throwable -> Laf
            throw r1     // Catch: java.lang.Throwable -> Laf
        Laf:
            r10 = move-exception
            r5.unlock()
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.BroadcastChannelImpl.send(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel, kotlinx.coroutines.channels.SendChannel
    /* JADX INFO: renamed from: trySend-JP2dKIU, reason: not valid java name */
    public java.lang.Object mo12809trySendJP2dKIU(E element) {
        java.util.concurrent.locks.ReentrantLock $this$withLock$iv = this.lock;
        java.util.concurrent.locks.ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            if (isClosedForSend()) {
                return super.mo12809trySendJP2dKIU(element);
            }
            java.lang.Iterable $this$any$iv = this.subscribers;
            boolean z = false;
            if (!($this$any$iv instanceof java.util.Collection) || !((java.util.Collection) $this$any$iv).isEmpty()) {
                java.util.Iterator it = $this$any$iv.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    java.lang.Object element$iv = it.next();
                    kotlinx.coroutines.channels.BufferedChannel it2 = (kotlinx.coroutines.channels.BufferedChannel) element$iv;
                    if (it2.shouldSendSuspend$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        z = true;
                        break;
                    }
                }
            }
            boolean shouldSuspend = z;
            if (shouldSuspend) {
                return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12833failurePtdJZtk();
            }
            if (this.capacity == -1) {
                this.lastConflatedElement = element;
            }
            java.lang.Iterable $this$forEach$iv = this.subscribers;
            for (java.lang.Object element$iv2 : $this$forEach$iv) {
                kotlinx.coroutines.channels.BufferedChannel it3 = (kotlinx.coroutines.channels.BufferedChannel) element$iv2;
                it3.mo12809trySendJP2dKIU(element);
            }
            return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12834successJP2dKIU(kotlin.Unit.INSTANCE);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel
    protected void registerSelectForSend(kotlinx.coroutines.selects.SelectInstance<?> select, java.lang.Object element) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(select, "select");
        java.util.concurrent.locks.ReentrantLock $this$withLock$iv = this.lock;
        java.util.concurrent.locks.ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            java.lang.Object result = this.onSendInternalResult.remove(select);
            if (result != null) {
                select.selectInRegistrationPhase(result);
                return;
            }
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
            reentrantLock.unlock();
            kotlinx.coroutines.BuildersKt__Builders_commonKt.launch$default(kotlinx.coroutines.CoroutineScopeKt.CoroutineScope(select.getContext()), null, kotlinx.coroutines.CoroutineStart.UNDISPATCHED, new kotlinx.coroutines.channels.BroadcastChannelImpl.AnonymousClass2(this, element, select, null), 1, null);
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.channels.BroadcastChannelImpl$registerSelectForSend$2, reason: invalid class name */
    /* JADX INFO: compiled from: BroadcastChannel.kt */
    @kotlin.Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.channels.BroadcastChannelImpl$registerSelectForSend$2", f = "BroadcastChannel.kt", i = {}, l = {291}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass2 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ java.lang.Object $element;
        final /* synthetic */ kotlinx.coroutines.selects.SelectInstance<?> $select;
        int label;
        final /* synthetic */ kotlinx.coroutines.channels.BroadcastChannelImpl<E> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(kotlinx.coroutines.channels.BroadcastChannelImpl<E> broadcastChannelImpl, java.lang.Object obj, kotlinx.coroutines.selects.SelectInstance<?> selectInstance, kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.BroadcastChannelImpl.AnonymousClass2> continuation) {
            super(2, continuation);
            this.this$0 = broadcastChannelImpl;
            this.$element = obj;
            this.$select = selectInstance;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            return new kotlinx.coroutines.channels.BroadcastChannelImpl.AnonymousClass2(this.this$0, this.$element, this.$select, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.channels.BroadcastChannelImpl.AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:29:0x0064 A[Catch: all -> 0x00b1, TryCatch #0 {all -> 0x00b1, blocks: (B:27:0x005e, B:29:0x0064, B:34:0x0073, B:35:0x0078, B:36:0x0079, B:38:0x0081, B:40:0x0088, B:42:0x00a1, B:43:0x00a8, B:39:0x0084), top: B:50:0x005e }] */
        /* JADX WARN: Removed duplicated region for block: B:38:0x0081 A[Catch: all -> 0x00b1, TryCatch #0 {all -> 0x00b1, blocks: (B:27:0x005e, B:29:0x0064, B:34:0x0073, B:35:0x0078, B:36:0x0079, B:38:0x0081, B:40:0x0088, B:42:0x00a1, B:43:0x00a8, B:39:0x0084), top: B:50:0x005e }] */
        /* JADX WARN: Removed duplicated region for block: B:39:0x0084 A[Catch: all -> 0x00b1, TryCatch #0 {all -> 0x00b1, blocks: (B:27:0x005e, B:29:0x0064, B:34:0x0073, B:35:0x0078, B:36:0x0079, B:38:0x0081, B:40:0x0088, B:42:0x00a1, B:43:0x00a8, B:39:0x0084), top: B:50:0x005e }] */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00a1 A[Catch: all -> 0x00b1, TryCatch #0 {all -> 0x00b1, blocks: (B:27:0x005e, B:29:0x0064, B:34:0x0073, B:35:0x0078, B:36:0x0079, B:38:0x0081, B:40:0x0088, B:42:0x00a1, B:43:0x00a8, B:39:0x0084), top: B:50:0x005e }] */
        /* JADX WARN: Type inference incomplete: some casts might be missing */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final java.lang.Object invokeSuspend(java.lang.Object r13) throws java.lang.Throwable {
            /*
                r12 = this;
                java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                int r1 = r12.label
                r2 = 0
                r3 = 1
                switch(r1) {
                    case 0: goto L1a;
                    case 1: goto L13;
                    default: goto Lb;
                }
            Lb:
                java.lang.IllegalStateException r13 = new java.lang.IllegalStateException
                java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
                r13.<init>(r0)
                throw r13
            L13:
                r0 = r12
                kotlin.ResultKt.throwOnFailure(r13)     // Catch: java.lang.Throwable -> L18
                goto L30
            L18:
                r1 = move-exception
                goto L36
            L1a:
                kotlin.ResultKt.throwOnFailure(r13)
                r1 = r12
                kotlinx.coroutines.channels.BroadcastChannelImpl<E> r4 = r1.this$0     // Catch: java.lang.Throwable -> L32
                java.lang.Object r5 = r1.$element     // Catch: java.lang.Throwable -> L32
                r6 = r1
                kotlin.coroutines.Continuation r6 = (kotlin.coroutines.Continuation) r6     // Catch: java.lang.Throwable -> L32
                r1.label = r3     // Catch: java.lang.Throwable -> L32
                java.lang.Object r4 = r4.send(r5, r6)     // Catch: java.lang.Throwable -> L32
                if (r4 != r0) goto L2f
                return r0
            L2f:
                r0 = r1
            L30:
                r1 = r3
                goto L4b
            L32:
                r0 = move-exception
                r11 = r1
                r1 = r0
                r0 = r11
            L36:
                kotlinx.coroutines.channels.BroadcastChannelImpl<E> r4 = r0.this$0
                boolean r4 = r4.isClosedForSend()
                if (r4 == 0) goto Lb6
                boolean r4 = r1 instanceof kotlinx.coroutines.channels.ClosedSendChannelException
                if (r4 != 0) goto L4a
                kotlinx.coroutines.channels.BroadcastChannelImpl<E> r4 = r0.this$0
                java.lang.Throwable r4 = r4.getSendException()
                if (r4 != r1) goto Lb6
            L4a:
                r1 = r2
            L4b:
                kotlinx.coroutines.channels.BroadcastChannelImpl<E> r4 = r0.this$0
                java.util.concurrent.locks.ReentrantLock r4 = kotlinx.coroutines.channels.BroadcastChannelImpl.access$getLock$p(r4)
                kotlinx.coroutines.channels.BroadcastChannelImpl<E> r5 = r0.this$0
                kotlinx.coroutines.selects.SelectInstance<?> r6 = r0.$select
                r7 = 0
                r8 = r4
                java.util.concurrent.locks.Lock r8 = (java.util.concurrent.locks.Lock) r8
                r8.lock()
                r4 = 0
                boolean r9 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()     // Catch: java.lang.Throwable -> Lb1
                if (r9 == 0) goto L79
                r9 = 0
                java.util.HashMap r10 = kotlinx.coroutines.channels.BroadcastChannelImpl.access$getOnSendInternalResult$p(r5)     // Catch: java.lang.Throwable -> Lb1
                java.lang.Object r10 = r10.get(r6)     // Catch: java.lang.Throwable -> Lb1
                if (r10 != 0) goto L70
                r2 = r3
            L70:
                if (r2 == 0) goto L73
                goto L79
            L73:
                java.lang.AssertionError r2 = new java.lang.AssertionError     // Catch: java.lang.Throwable -> Lb1
                r2.<init>()     // Catch: java.lang.Throwable -> Lb1
                throw r2     // Catch: java.lang.Throwable -> Lb1
            L79:
                java.util.HashMap r2 = kotlinx.coroutines.channels.BroadcastChannelImpl.access$getOnSendInternalResult$p(r5)     // Catch: java.lang.Throwable -> Lb1
                java.util.Map r2 = (java.util.Map) r2     // Catch: java.lang.Throwable -> Lb1
                if (r1 == 0) goto L84
                kotlin.Unit r3 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> Lb1
                goto L88
            L84:
                kotlinx.coroutines.internal.Symbol r3 = kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED()     // Catch: java.lang.Throwable -> Lb1
            L88:
                r2.put(r6, r3)     // Catch: java.lang.Throwable -> Lb1
                java.lang.String r1 = "null cannot be cast to non-null type kotlinx.coroutines.selects.SelectImplementation<*>"
                kotlin.jvm.internal.Intrinsics.checkNotNull(r6, r1)     // Catch: java.lang.Throwable -> Lb1
                r1 = r6
                kotlinx.coroutines.selects.SelectImplementation r1 = (kotlinx.coroutines.selects.SelectImplementation) r1     // Catch: java.lang.Throwable -> Lb1
                r1 = r6
                kotlinx.coroutines.selects.SelectImplementation r1 = (kotlinx.coroutines.selects.SelectImplementation) r1     // Catch: java.lang.Throwable -> Lb1
                kotlin.Unit r2 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> Lb1
                kotlinx.coroutines.selects.TrySelectDetailedResult r1 = r1.trySelectDetailed(r5, r2)     // Catch: java.lang.Throwable -> Lb1
                kotlinx.coroutines.selects.TrySelectDetailedResult r2 = kotlinx.coroutines.selects.TrySelectDetailedResult.REREGISTER     // Catch: java.lang.Throwable -> Lb1
                if (r1 == r2) goto La8
                java.util.HashMap r1 = kotlinx.coroutines.channels.BroadcastChannelImpl.access$getOnSendInternalResult$p(r5)     // Catch: java.lang.Throwable -> Lb1
                r1.remove(r6)     // Catch: java.lang.Throwable -> Lb1
            La8:
                kotlin.Unit r1 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> Lb1
                r8.unlock()
                kotlin.Unit r1 = kotlin.Unit.INSTANCE
                return r1
            Lb1:
                r1 = move-exception
                r8.unlock()
                throw r1
            Lb6:
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.BroadcastChannelImpl.AnonymousClass2.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel, kotlinx.coroutines.channels.SendChannel
    public boolean close(java.lang.Throwable cause) {
        java.util.concurrent.locks.ReentrantLock $this$withLock$iv = this.lock;
        java.util.concurrent.locks.ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            java.lang.Iterable $this$forEach$iv = this.subscribers;
            for (java.lang.Object element$iv : $this$forEach$iv) {
                kotlinx.coroutines.channels.BufferedChannel it = (kotlinx.coroutines.channels.BufferedChannel) element$iv;
                it.close(cause);
            }
            java.lang.Iterable $this$forEach$iv2 = this.subscribers;
            java.lang.Iterable $this$filter$iv = $this$forEach$iv2;
            java.util.ArrayList arrayList = new java.util.ArrayList();
            for (java.lang.Object element$iv$iv : $this$filter$iv) {
                kotlinx.coroutines.channels.BufferedChannel it2 = (kotlinx.coroutines.channels.BufferedChannel) element$iv$iv;
                if (it2.hasElements$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    arrayList.add(element$iv$iv);
                }
            }
            this.subscribers = arrayList;
            return super.close(cause);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel
    public boolean cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Throwable cause) {
        java.util.concurrent.locks.ReentrantLock $this$withLock$iv = this.lock;
        java.util.concurrent.locks.ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            java.lang.Iterable $this$forEach$iv = this.subscribers;
            for (java.lang.Object element$iv : $this$forEach$iv) {
                kotlinx.coroutines.channels.BufferedChannel it = (kotlinx.coroutines.channels.BufferedChannel) element$iv;
                it.cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(cause);
            }
            this.lastConflatedElement = kotlinx.coroutines.channels.BroadcastChannelKt.NO_ELEMENT;
            return super.cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(cause);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel, kotlinx.coroutines.channels.SendChannel
    public boolean isClosedForSend() {
        java.util.concurrent.locks.ReentrantLock $this$withLock$iv = this.lock;
        java.util.concurrent.locks.ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            return super.isClosedForSend();
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: compiled from: BroadcastChannel.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0000\b\u0082\u0004\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/channels/BroadcastChannelImpl$SubscriberBuffered;", "Lkotlinx/coroutines/channels/BufferedChannel;", "(Lkotlinx/coroutines/channels/BroadcastChannelImpl;)V", "cancelImpl", "", "cause", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class SubscriberBuffered extends kotlinx.coroutines.channels.BufferedChannel<E> {
        /* JADX WARN: Multi-variable type inference failed */
        public SubscriberBuffered() {
            super(kotlinx.coroutines.channels.BroadcastChannelImpl.this.getCapacity(), null, 2, 0 == true ? 1 : 0);
        }

        @Override // kotlinx.coroutines.channels.BufferedChannel
        /* JADX INFO: renamed from: cancelImpl, reason: merged with bridge method [inline-methods] */
        public boolean cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Throwable cause) {
            java.util.concurrent.locks.ReentrantLock $this$withLock$iv = ((kotlinx.coroutines.channels.BroadcastChannelImpl) kotlinx.coroutines.channels.BroadcastChannelImpl.this).lock;
            kotlinx.coroutines.channels.BroadcastChannelImpl<E> broadcastChannelImpl = kotlinx.coroutines.channels.BroadcastChannelImpl.this;
            java.util.concurrent.locks.ReentrantLock reentrantLock = $this$withLock$iv;
            reentrantLock.lock();
            try {
                broadcastChannelImpl.removeSubscriber(this);
                return super.cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(cause);
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    /* JADX INFO: compiled from: BroadcastChannel.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0000\b\u0082\u0004\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/channels/BroadcastChannelImpl$SubscriberConflated;", "Lkotlinx/coroutines/channels/ConflatedBufferedChannel;", "(Lkotlinx/coroutines/channels/BroadcastChannelImpl;)V", "cancelImpl", "", "cause", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class SubscriberConflated extends kotlinx.coroutines.channels.ConflatedBufferedChannel<E> {
        public SubscriberConflated() {
            super(1, kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST, null, 4, null);
        }

        @Override // kotlinx.coroutines.channels.BufferedChannel
        /* JADX INFO: renamed from: cancelImpl, reason: merged with bridge method [inline-methods] */
        public boolean cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Throwable cause) {
            kotlinx.coroutines.channels.BroadcastChannelImpl.this.removeSubscriber(this);
            return super.cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(cause);
        }
    }

    public final E getValue() throws java.lang.Throwable {
        java.util.concurrent.locks.ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            if (!isClosedForSend()) {
                if (this.lastConflatedElement == kotlinx.coroutines.channels.BroadcastChannelKt.NO_ELEMENT) {
                    throw new java.lang.IllegalStateException("No value".toString());
                }
                return (E) this.lastConflatedElement;
            }
            java.lang.Throwable closeCause = getCloseCause();
            if (closeCause == null) {
                throw new java.lang.IllegalStateException("This broadcast channel is closed");
            }
            throw closeCause;
        } finally {
            reentrantLock.unlock();
        }
    }

    public final E getValueOrNull() {
        java.util.concurrent.locks.ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            E e = null;
            if (!isClosedForReceive() && this.lastConflatedElement != kotlinx.coroutines.channels.BroadcastChannelKt.NO_ELEMENT) {
                e = (E) this.lastConflatedElement;
            }
            return e;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel
    public java.lang.String toString() {
        java.lang.String str;
        if (this.lastConflatedElement != kotlinx.coroutines.channels.BroadcastChannelKt.NO_ELEMENT) {
            str = "CONFLATED_ELEMENT=" + this.lastConflatedElement + "; ";
        } else {
            str = "";
        }
        return str + "BROADCAST=<" + super.toString() + ">; SUBSCRIBERS=" + kotlin.collections.CollectionsKt.joinToString$default(this.subscribers, ";", "<", ">", 0, null, null, 56, null);
    }
}
