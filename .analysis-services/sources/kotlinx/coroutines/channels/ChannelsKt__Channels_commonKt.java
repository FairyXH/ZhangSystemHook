package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: Channels.common.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000>\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\u001a\u001a\u0010\u0002\u001a\u00020\u0003*\u0006\u0012\u0002\b\u00030\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0001\u001aF\u0010\u0007\u001a\u0002H\b\"\u0004\b\u0000\u0010\t\"\u0004\b\u0001\u0010\b*\b\u0012\u0004\u0012\u0002H\t0\n2\u001d\u0010\u000b\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u0004\u0012\u0004\u0012\u0002H\b0\f¢\u0006\u0002\b\rH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u000e\u001aS\u0010\u0007\u001a\u0002H\b\"\u0004\b\u0000\u0010\t\"\u0004\b\u0001\u0010\b*\b\u0012\u0004\u0012\u0002H\t0\u00042\u001d\u0010\u000b\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u0004\u0012\u0004\u0012\u0002H\b0\f¢\u0006\u0002\b\rH\u0086\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\u000f\u001a2\u0010\u0010\u001a\u00020\u0003\"\u0004\b\u0000\u0010\t*\b\u0012\u0004\u0012\u0002H\t0\n2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\t\u0012\u0004\u0012\u00020\u00030\fH\u0087H¢\u0006\u0002\u0010\u0012\u001a2\u0010\u0010\u001a\u00020\u0003\"\u0004\b\u0000\u0010\t*\b\u0012\u0004\u0012\u0002H\t0\u00042\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\t\u0012\u0004\u0012\u00020\u00030\fH\u0086H¢\u0006\u0002\u0010\u0013\u001a$\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\t0\u0015\"\b\b\u0000\u0010\t*\u00020\u0016*\b\u0012\u0004\u0012\u0002H\t0\u0004H\u0007\u001a$\u0010\u0017\u001a\u0004\u0018\u0001H\t\"\b\b\u0000\u0010\t*\u00020\u0016*\b\u0012\u0004\u0012\u0002H\t0\u0004H\u0087@¢\u0006\u0002\u0010\u0018\u001a$\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\t0\u001a\"\u0004\b\u0000\u0010\t*\b\u0012\u0004\u0012\u0002H\t0\u0004H\u0086@¢\u0006\u0002\u0010\u0018\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u001b"}, d2 = {"DEFAULT_CLOSE_MESSAGE", "", "cancelConsumed", "", "Lkotlinx/coroutines/channels/ReceiveChannel;", "cause", "", "consume", "R", "E", "Lkotlinx/coroutines/channels/BroadcastChannel;", "block", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/channels/BroadcastChannel;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "consumeEach", "action", "(Lkotlinx/coroutines/channels/BroadcastChannel;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onReceiveOrNull", "Lkotlinx/coroutines/selects/SelectClause1;", "", "receiveOrNull", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toList", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/channels/ChannelsKt")
final /* synthetic */ class ChannelsKt__Channels_commonKt {

    /* JADX INFO: renamed from: kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$consumeEach$1, reason: invalid class name */
    /* JADX INFO: compiled from: Channels.common.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 176)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt", f = "Channels.common.kt", i = {0, 0}, l = {106}, m = "consumeEach", n = {"action", "$this$consume$iv"}, s = {"L$0", "L$1"})
    static final class AnonymousClass1<E> extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        java.lang.Object L$0;
        java.lang.Object L$1;
        java.lang.Object L$2;
        int label;
        /* synthetic */ java.lang.Object result;

        AnonymousClass1(kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.consumeEach((kotlinx.coroutines.channels.ReceiveChannel) null, (kotlin.jvm.functions.Function1) null, this);
        }
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$consumeEach$3, reason: invalid class name */
    /* JADX INFO: compiled from: Channels.common.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 176)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt", f = "Channels.common.kt", i = {0, 0}, l = {130}, m = "consumeEach", n = {"action", "channel$iv"}, s = {"L$0", "L$1"})
    static final class AnonymousClass3<E> extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        java.lang.Object L$0;
        java.lang.Object L$1;
        java.lang.Object L$2;
        int label;
        /* synthetic */ java.lang.Object result;

        AnonymousClass3(kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.AnonymousClass3> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.consumeEach((kotlinx.coroutines.channels.BroadcastChannel) null, (kotlin.jvm.functions.Function1) null, this);
        }
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$toList$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Channels.common.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt", f = "Channels.common.kt", i = {0, 0}, l = {149}, m = "toList", n = {"$this$toList_u24lambda_u243", "$this$consume$iv$iv"}, s = {"L$1", "L$2"})
    static final class C01201<E> extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        java.lang.Object L$0;
        java.lang.Object L$1;
        java.lang.Object L$2;
        java.lang.Object L$3;
        int label;
        /* synthetic */ java.lang.Object result;

        C01201(kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.C01201> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return kotlinx.coroutines.channels.ChannelsKt.toList(null, this);
        }
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "BroadcastChannel is deprecated in the favour of SharedFlow and is no longer supported")
    public static final <E, R> R consume(kotlinx.coroutines.channels.BroadcastChannel<E> broadcastChannel, kotlin.jvm.functions.Function1<? super kotlinx.coroutines.channels.ReceiveChannel<? extends E>, ? extends R> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(broadcastChannel, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlinx.coroutines.channels.ReceiveChannel<E> receiveChannelOpenSubscription = broadcastChannel.openSubscription();
        try {
            return block.invoke(receiveChannelOpenSubscription);
        } finally {
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
            kotlinx.coroutines.channels.ReceiveChannel.DefaultImpls.cancel$default((kotlinx.coroutines.channels.ReceiveChannel) receiveChannelOpenSubscription, (java.util.concurrent.CancellationException) null, 1, (java.lang.Object) null);
            kotlin.jvm.internal.InlineMarker.finallyEnd(1);
        }
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Deprecated in the favour of 'receiveCatching'", replaceWith = @kotlin.ReplaceWith(expression = "receiveCatching().getOrNull()", imports = {}))
    public static final /* synthetic */ java.lang.Object receiveOrNull(kotlinx.coroutines.channels.ReceiveChannel $this$receiveOrNull, kotlin.coroutines.Continuation $completion) {
        kotlin.jvm.internal.Intrinsics.checkNotNull($this$receiveOrNull, "null cannot be cast to non-null type kotlinx.coroutines.channels.ReceiveChannel<E of kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.receiveOrNull?>");
        return $this$receiveOrNull.receiveOrNull($completion);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Deprecated in the favour of 'onReceiveCatching'")
    public static final /* synthetic */ kotlinx.coroutines.selects.SelectClause1 onReceiveOrNull(kotlinx.coroutines.channels.ReceiveChannel $this$onReceiveOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$onReceiveOrNull, "<this>");
        return $this$onReceiveOrNull.getOnReceiveOrNull();
    }

    public static final <E, R> R consume(kotlinx.coroutines.channels.ReceiveChannel<? extends E> receiveChannel, kotlin.jvm.functions.Function1<? super kotlinx.coroutines.channels.ReceiveChannel<? extends E>, ? extends R> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(receiveChannel, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        try {
            R rInvoke = block.invoke(receiveChannel);
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
            kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(receiveChannel, null);
            kotlin.jvm.internal.InlineMarker.finallyEnd(1);
            return rInvoke;
        } finally {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x006f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0081 A[Catch: all -> 0x00a0, TryCatch #0 {all -> 0x00a0, blocks: (B:24:0x0079, B:26:0x0081, B:27:0x0090), top: B:39:0x0079 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0090 A[Catch: all -> 0x00a0, TRY_LEAVE, TryCatch #0 {all -> 0x00a0, blocks: (B:24:0x0079, B:26:0x0081, B:27:0x0090), top: B:39:0x0079 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0070 -> B:39:0x0079). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <E> java.lang.Object consumeEach(kotlinx.coroutines.channels.ReceiveChannel<? extends E> r10, kotlin.jvm.functions.Function1<? super E, kotlin.Unit> r11, kotlin.coroutines.Continuation<? super kotlin.Unit> r12) throws java.lang.Throwable {
        /*
            boolean r0 = r12 instanceof kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.AnonymousClass1
            if (r0 == 0) goto L14
            r0 = r12
            kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$consumeEach$1 r0 = (kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r12 = r0.label
            int r12 = r12 - r2
            r0.label = r12
            goto L19
        L14:
            kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$consumeEach$1 r0 = new kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$consumeEach$1
            r0.<init>(r12)
        L19:
            r12 = r0
            java.lang.Object r0 = r12.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r12.label
            r3 = 1
            switch(r2) {
                case 0: goto L4c;
                case 1: goto L2e;
                default: goto L26;
            }
        L26:
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException
            java.lang.String r11 = "call to 'resume' before 'invoke' with coroutine"
            r10.<init>(r11)
            throw r10
        L2e:
            r10 = 0
            r11 = 0
            r2 = 0
            java.lang.Object r4 = r12.L$2
            kotlinx.coroutines.channels.ChannelIterator r4 = (kotlinx.coroutines.channels.ChannelIterator) r4
            r5 = 0
            java.lang.Object r6 = r12.L$1
            kotlinx.coroutines.channels.ReceiveChannel r6 = (kotlinx.coroutines.channels.ReceiveChannel) r6
            java.lang.Object r7 = r12.L$0
            kotlin.jvm.functions.Function1 r7 = (kotlin.jvm.functions.Function1) r7
            kotlin.ResultKt.throwOnFailure(r0)     // Catch: java.lang.Throwable -> L49
            r8 = r7
            r7 = r6
            r6 = r5
            r5 = r4
            r4 = r2
            r2 = r1
            r1 = r0
            goto L79
        L49:
            r1 = move-exception
            goto Laa
        L4c:
            kotlin.ResultKt.throwOnFailure(r0)
            r2 = 0
            r6 = r10
            r10 = 0
            r5 = 0
            r4 = r6
            r7 = 0
            kotlinx.coroutines.channels.ChannelIterator r8 = r4.iterator()     // Catch: java.lang.Throwable -> La7
            r4 = r8
            r9 = r11
            r11 = r10
            r10 = r2
            r2 = r7
            r7 = r9
        L61:
            r12.L$0 = r7     // Catch: java.lang.Throwable -> L49
            r12.L$1 = r6     // Catch: java.lang.Throwable -> L49
            r12.L$2 = r4     // Catch: java.lang.Throwable -> L49
            r12.label = r3     // Catch: java.lang.Throwable -> L49
            java.lang.Object r8 = r4.hasNext(r12)     // Catch: java.lang.Throwable -> L49
            if (r8 != r1) goto L70
            return r1
        L70:
            r9 = r1
            r1 = r0
            r0 = r8
            r8 = r7
            r7 = r6
            r6 = r5
            r5 = r4
            r4 = r2
            r2 = r9
        L79:
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch: java.lang.Throwable -> La0
            boolean r0 = r0.booleanValue()     // Catch: java.lang.Throwable -> La0
            if (r0 == 0) goto L90
            java.lang.Object r0 = r5.next()     // Catch: java.lang.Throwable -> La0
            r8.invoke(r0)     // Catch: java.lang.Throwable -> La0
            r0 = r1
            r1 = r2
            r2 = r4
            r4 = r5
            r5 = r6
            r6 = r7
            r7 = r8
            goto L61
        L90:
            kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> La0
            kotlin.jvm.internal.InlineMarker.finallyStart(r3)
            kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r7, r6)
            kotlin.jvm.internal.InlineMarker.finallyEnd(r3)
            kotlin.Unit r11 = kotlin.Unit.INSTANCE
            return r11
        La0:
            r0 = move-exception
            r5 = r6
            r6 = r7
            r9 = r1
            r1 = r0
            r0 = r9
            goto Laa
        La7:
            r1 = move-exception
            r11 = r10
            r10 = r2
        Laa:
            r2 = r1
            throw r1     // Catch: java.lang.Throwable -> Lad
        Lad:
            r1 = move-exception
            kotlin.jvm.internal.InlineMarker.finallyStart(r3)
            kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r6, r2)
            kotlin.jvm.internal.InlineMarker.finallyEnd(r3)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.consumeEach(kotlinx.coroutines.channels.ReceiveChannel, kotlin.jvm.functions.Function1, kotlin.coroutines.Continuation):java.lang.Object");
    }

    private static final <E> java.lang.Object consumeEach$$forInline(kotlinx.coroutines.channels.ReceiveChannel<? extends E> receiveChannel, kotlin.jvm.functions.Function1<? super E, kotlin.Unit> function1, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        try {
            kotlinx.coroutines.channels.ChannelIterator<? extends E> it = receiveChannel.iterator();
            while (true) {
                kotlin.jvm.internal.InlineMarker.mark(3);
                kotlin.jvm.internal.InlineMarker.mark(0);
                java.lang.Object objHasNext = it.hasNext(null);
                kotlin.jvm.internal.InlineMarker.mark(1);
                if (!((java.lang.Boolean) objHasNext).booleanValue()) {
                    kotlin.Unit unit = kotlin.Unit.INSTANCE;
                    kotlin.jvm.internal.InlineMarker.finallyStart(1);
                    kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(receiveChannel, null);
                    kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                    return kotlin.Unit.INSTANCE;
                }
                java.lang.Object e = it.next();
                function1.invoke(e);
            }
        } finally {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0081 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0096 A[Catch: all -> 0x00ba, TryCatch #3 {all -> 0x00ba, blocks: (B:24:0x008e, B:26:0x0096, B:27:0x00ab), top: B:45:0x008e }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00ab A[Catch: all -> 0x00ba, TRY_LEAVE, TryCatch #3 {all -> 0x00ba, blocks: (B:24:0x008e, B:26:0x0096, B:27:0x00ab), top: B:45:0x008e }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v2, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r8v5 */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v4, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r9v8 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0082 -> B:45:0x008e). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <E> java.lang.Object toList(kotlinx.coroutines.channels.ReceiveChannel<? extends E> r14, kotlin.coroutines.Continuation<? super java.util.List<? extends E>> r15) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 216
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.toList(kotlinx.coroutines.channels.ReceiveChannel, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x006f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0080 A[Catch: all -> 0x009e, TryCatch #2 {all -> 0x009e, blocks: (B:24:0x0078, B:26:0x0080, B:27:0x008e), top: B:40:0x0078 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x008e A[Catch: all -> 0x009e, TRY_LEAVE, TryCatch #2 {all -> 0x009e, blocks: (B:24:0x0078, B:26:0x0080, B:27:0x008e), top: B:40:0x0078 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0070 -> B:40:0x0078). Please report as a decompilation issue!!! */
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "BroadcastChannel is deprecated in the favour of SharedFlow and is no longer supported")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <E> java.lang.Object consumeEach(kotlinx.coroutines.channels.BroadcastChannel<E> r10, kotlin.jvm.functions.Function1<? super E, kotlin.Unit> r11, kotlin.coroutines.Continuation<? super kotlin.Unit> r12) throws java.lang.Throwable {
        /*
            boolean r0 = r12 instanceof kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.AnonymousClass3
            if (r0 == 0) goto L14
            r0 = r12
            kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$consumeEach$3 r0 = (kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.AnonymousClass3) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r12 = r0.label
            int r12 = r12 - r2
            r0.label = r12
            goto L19
        L14:
            kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$consumeEach$3 r0 = new kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$consumeEach$3
            r0.<init>(r12)
        L19:
            r12 = r0
            java.lang.Object r0 = r12.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r12.label
            r3 = 0
            r4 = 1
            switch(r2) {
                case 0: goto L4b;
                case 1: goto L2f;
                default: goto L27;
            }
        L27:
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException
            java.lang.String r11 = "call to 'resume' before 'invoke' with coroutine"
            r10.<init>(r11)
            throw r10
        L2f:
            r10 = 0
            r11 = 0
            r2 = 0
            java.lang.Object r5 = r12.L$2
            kotlinx.coroutines.channels.ChannelIterator r5 = (kotlinx.coroutines.channels.ChannelIterator) r5
            java.lang.Object r6 = r12.L$1
            kotlinx.coroutines.channels.ReceiveChannel r6 = (kotlinx.coroutines.channels.ReceiveChannel) r6
            java.lang.Object r7 = r12.L$0
            kotlin.jvm.functions.Function1 r7 = (kotlin.jvm.functions.Function1) r7
            kotlin.ResultKt.throwOnFailure(r0)     // Catch: java.lang.Throwable -> L48
            r8 = r7
            r7 = r6
            r6 = r5
            r5 = r2
            r2 = r1
            r1 = r0
            goto L78
        L48:
            r1 = move-exception
            goto La7
        L4b:
            kotlin.ResultKt.throwOnFailure(r0)
            r2 = 0
            r5 = 0
            kotlinx.coroutines.channels.ReceiveChannel r6 = r10.openSubscription()
            r10 = r6
            r7 = 0
            kotlinx.coroutines.channels.ChannelIterator r8 = r10.iterator()     // Catch: java.lang.Throwable -> La4
            r10 = r2
            r2 = r7
            r7 = r11
            r11 = r5
            r5 = r8
        L61:
            r12.L$0 = r7     // Catch: java.lang.Throwable -> L48
            r12.L$1 = r6     // Catch: java.lang.Throwable -> L48
            r12.L$2 = r5     // Catch: java.lang.Throwable -> L48
            r12.label = r4     // Catch: java.lang.Throwable -> L48
            java.lang.Object r8 = r5.hasNext(r12)     // Catch: java.lang.Throwable -> L48
            if (r8 != r1) goto L70
            return r1
        L70:
            r9 = r1
            r1 = r0
            r0 = r8
            r8 = r7
            r7 = r6
            r6 = r5
            r5 = r2
            r2 = r9
        L78:
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch: java.lang.Throwable -> L9e
            boolean r0 = r0.booleanValue()     // Catch: java.lang.Throwable -> L9e
            if (r0 == 0) goto L8e
            java.lang.Object r0 = r6.next()     // Catch: java.lang.Throwable -> L9e
            r8.invoke(r0)     // Catch: java.lang.Throwable -> L9e
            r0 = r1
            r1 = r2
            r2 = r5
            r5 = r6
            r6 = r7
            r7 = r8
            goto L61
        L8e:
            kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> L9e
            kotlin.jvm.internal.InlineMarker.finallyStart(r4)
            kotlinx.coroutines.channels.ReceiveChannel.DefaultImpls.cancel$default(r7, r3, r4, r3)
            kotlin.jvm.internal.InlineMarker.finallyEnd(r4)
            kotlin.Unit r11 = kotlin.Unit.INSTANCE
            return r11
        L9e:
            r0 = move-exception
            r6 = r7
            r9 = r1
            r1 = r0
            r0 = r9
            goto La7
        La4:
            r1 = move-exception
            r10 = r2
            r11 = r5
        La7:
            kotlin.jvm.internal.InlineMarker.finallyStart(r4)
            kotlinx.coroutines.channels.ReceiveChannel.DefaultImpls.cancel$default(r6, r3, r4, r3)
            kotlin.jvm.internal.InlineMarker.finallyEnd(r4)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.consumeEach(kotlinx.coroutines.channels.BroadcastChannel, kotlin.jvm.functions.Function1, kotlin.coroutines.Continuation):java.lang.Object");
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "BroadcastChannel is deprecated in the favour of SharedFlow and is no longer supported")
    private static final <E> java.lang.Object consumeEach$$forInline(kotlinx.coroutines.channels.BroadcastChannel<E> broadcastChannel, kotlin.jvm.functions.Function1<? super E, kotlin.Unit> function1, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlinx.coroutines.channels.ReceiveChannel<E> receiveChannelOpenSubscription = broadcastChannel.openSubscription();
        try {
            kotlinx.coroutines.channels.ChannelIterator<E> it = receiveChannelOpenSubscription.iterator();
            while (true) {
                kotlin.jvm.internal.InlineMarker.mark(3);
                kotlin.jvm.internal.InlineMarker.mark(0);
                java.lang.Object objHasNext = it.hasNext(null);
                kotlin.jvm.internal.InlineMarker.mark(1);
                if (!((java.lang.Boolean) objHasNext).booleanValue()) {
                    kotlin.Unit unit = kotlin.Unit.INSTANCE;
                    kotlin.jvm.internal.InlineMarker.finallyStart(1);
                    kotlinx.coroutines.channels.ReceiveChannel.DefaultImpls.cancel$default((kotlinx.coroutines.channels.ReceiveChannel) receiveChannelOpenSubscription, (java.util.concurrent.CancellationException) null, 1, (java.lang.Object) null);
                    kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                    return kotlin.Unit.INSTANCE;
                }
                java.lang.Object element = it.next();
                function1.invoke(element);
            }
        } catch (java.lang.Throwable th) {
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
            kotlinx.coroutines.channels.ReceiveChannel.DefaultImpls.cancel$default((kotlinx.coroutines.channels.ReceiveChannel) receiveChannelOpenSubscription, (java.util.concurrent.CancellationException) null, 1, (java.lang.Object) null);
            kotlin.jvm.internal.InlineMarker.finallyEnd(1);
            throw th;
        }
    }

    public static final void cancelConsumed(kotlinx.coroutines.channels.ReceiveChannel<?> receiveChannel, java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(receiveChannel, "<this>");
        if (cause != null) {
            CancellationException = cause instanceof java.util.concurrent.CancellationException ? (java.util.concurrent.CancellationException) cause : null;
            if (CancellationException == null) {
                CancellationException = kotlinx.coroutines.ExceptionsKt.CancellationException("Channel was consumed, consumer had failed", cause);
            }
        }
        receiveChannel.cancel(CancellationException);
    }
}
