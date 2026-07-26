package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Channels.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0007\u001a\u001c\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001a,\u0010\u0006\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0086@¢\u0006\u0002\u0010\n\u001a6\u0010\u000b\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\f\u001a\u00020\rH\u0082@¢\u0006\u0004\b\u000e\u0010\u000f\u001a$\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u0012\u001a\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005¨\u0006\u0014"}, d2 = {"asFlow", "Lkotlinx/coroutines/flow/Flow;", "T", "Lkotlinx/coroutines/channels/BroadcastChannel;", "consumeAsFlow", "Lkotlinx/coroutines/channels/ReceiveChannel;", "emitAll", "", "Lkotlinx/coroutines/flow/FlowCollector;", "channel", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "emitAllImpl", "consume", "", "emitAllImpl$FlowKt__ChannelsKt", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlinx/coroutines/channels/ReceiveChannel;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "produceIn", "scope", "Lkotlinx/coroutines/CoroutineScope;", "receiveAsFlow", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
final /* synthetic */ class FlowKt__ChannelsKt {
    public static final <T> java.lang.Object emitAll(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlinx.coroutines.channels.ReceiveChannel<? extends T> receiveChannel, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) throws java.lang.Throwable {
        java.lang.Object objEmitAllImpl$FlowKt__ChannelsKt = emitAllImpl$FlowKt__ChannelsKt(flowCollector, receiveChannel, true, continuation);
        return objEmitAllImpl$FlowKt__ChannelsKt == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objEmitAllImpl$FlowKt__ChannelsKt : kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:24:0x007e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0090 A[Catch: all -> 0x00b7, TRY_LEAVE, TryCatch #1 {all -> 0x00b7, blocks: (B:26:0x0088, B:28:0x0090), top: B:49:0x0088 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00ae A[DONT_GENERATE] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r2v0, types: [int] */
    /* JADX WARN: Type inference failed for: r2v1, types: [kotlinx.coroutines.channels.ReceiveChannel] */
    /* JADX WARN: Type inference failed for: r2v10, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v15 */
    /* JADX WARN: Type inference failed for: r2v2, types: [kotlinx.coroutines.channels.ReceiveChannel] */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v4, types: [java.lang.Object, kotlinx.coroutines.channels.ReceiveChannel] */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Type inference failed for: r4v1, types: [java.lang.Object, kotlinx.coroutines.flow.FlowCollector] */
    /* JADX WARN: Type inference failed for: r4v4 */
    /* JADX WARN: Type inference failed for: r7v0, types: [kotlinx.coroutines.flow.FlowCollector, kotlinx.coroutines.flow.FlowCollector<? super T>] */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v16 */
    /* JADX WARN: Type inference failed for: r7v21 */
    /* JADX WARN: Type inference failed for: r7v22 */
    /* JADX WARN: Type inference failed for: r7v23 */
    /* JADX WARN: Type inference failed for: r7v24 */
    /* JADX WARN: Type inference failed for: r7v25 */
    /* JADX WARN: Type inference failed for: r7v4 */
    /* JADX WARN: Type inference failed for: r7v5, types: [boolean] */
    /* JADX WARN: Type inference failed for: r7v8, types: [java.lang.Object] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:31:0x00a6 -> B:22:0x006d). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <T> java.lang.Object emitAllImpl$FlowKt__ChannelsKt(kotlinx.coroutines.flow.FlowCollector<? super T> r7, kotlinx.coroutines.channels.ReceiveChannel<? extends T> r8, boolean r9, kotlin.coroutines.Continuation<? super kotlin.Unit> r10) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 212
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__ChannelsKt.emitAllImpl$FlowKt__ChannelsKt(kotlinx.coroutines.flow.FlowCollector, kotlinx.coroutines.channels.ReceiveChannel, boolean, kotlin.coroutines.Continuation):java.lang.Object");
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> receiveAsFlow(kotlinx.coroutines.channels.ReceiveChannel<? extends T> receiveChannel) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(receiveChannel, "<this>");
        return new kotlinx.coroutines.flow.ChannelAsFlow(receiveChannel, false, null, 0, null, 28, null);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> consumeAsFlow(kotlinx.coroutines.channels.ReceiveChannel<? extends T> receiveChannel) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(receiveChannel, "<this>");
        return new kotlinx.coroutines.flow.ChannelAsFlow(receiveChannel, true, null, 0, null, 28, null);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "'BroadcastChannel' is obsolete and all corresponding operators are deprecated in the favour of StateFlow and SharedFlow")
    public static final <T> kotlinx.coroutines.flow.Flow<T> asFlow(final kotlinx.coroutines.channels.BroadcastChannel<T> broadcastChannel) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(broadcastChannel, "<this>");
        return new kotlinx.coroutines.flow.Flow<T>() { // from class: kotlinx.coroutines.flow.FlowKt__ChannelsKt$asFlow$$inlined$unsafeFlow$1
            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                java.lang.Object objEmitAll = kotlinx.coroutines.flow.FlowKt.emitAll(flowCollector, broadcastChannel.openSubscription(), continuation);
                return objEmitAll == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objEmitAll : kotlin.Unit.INSTANCE;
            }
        };
    }

    public static final <T> kotlinx.coroutines.channels.ReceiveChannel<T> produceIn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.CoroutineScope scope) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scope, "scope");
        return kotlinx.coroutines.flow.internal.ChannelFlowKt.asChannelFlow(flow).produceImpl(scope);
    }
}
