package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Share.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000j\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003\u001a\u001c\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006\u001a+\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002¢\u0006\u0002\b\f\u001aM\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002*\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00020\t2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u0002H\u0002H\u0002¢\u0006\u0004\b\u0017\u0010\u0018\u001aA\u0010\u0019\u001a\u00020\u001a\"\u0004\b\u0000\u0010\u0002*\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00020\t2\u0012\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u001cH\u0002¢\u0006\u0002\b\u001d\u001aP\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012-\u0010\u001f\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020!\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\"\u0012\u0006\u0012\u0004\u0018\u00010#0 ¢\u0006\u0002\b$¢\u0006\u0002\u0010%\u001a6\u0010&\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\t2\u0006\u0010'\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\n\u001a\u00020\u000b\u001a,\u0010(\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\t2\u0006\u0010'\u001a\u00020\u000fH\u0086@¢\u0006\u0002\u0010)\u001a9\u0010(\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\t2\u0006\u0010'\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u0002H\u0002¢\u0006\u0002\u0010*¨\u0006+"}, d2 = {"asSharedFlow", "Lkotlinx/coroutines/flow/SharedFlow;", "T", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "asStateFlow", "Lkotlinx/coroutines/flow/StateFlow;", "Lkotlinx/coroutines/flow/MutableStateFlow;", "configureSharing", "Lkotlinx/coroutines/flow/SharingConfig;", "Lkotlinx/coroutines/flow/Flow;", "replay", "", "configureSharing$FlowKt__ShareKt", "launchSharing", "Lkotlinx/coroutines/Job;", "Lkotlinx/coroutines/CoroutineScope;", "context", "Lkotlin/coroutines/CoroutineContext;", "upstream", "shared", "started", "Lkotlinx/coroutines/flow/SharingStarted;", "initialValue", "launchSharing$FlowKt__ShareKt", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/MutableSharedFlow;Lkotlinx/coroutines/flow/SharingStarted;Ljava/lang/Object;)Lkotlinx/coroutines/Job;", "launchSharingDeferred", "", "result", "Lkotlinx/coroutines/CompletableDeferred;", "launchSharingDeferred$FlowKt__ShareKt", "onSubscription", "action", "Lkotlin/Function2;", "Lkotlinx/coroutines/flow/FlowCollector;", "Lkotlin/coroutines/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/flow/SharedFlow;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/SharedFlow;", "shareIn", "scope", "stateIn", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/CoroutineScope;Lkotlinx/coroutines/flow/SharingStarted;Ljava/lang/Object;)Lkotlinx/coroutines/flow/StateFlow;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
final /* synthetic */ class FlowKt__ShareKt {
    public static /* synthetic */ kotlinx.coroutines.flow.SharedFlow shareIn$default(kotlinx.coroutines.flow.Flow flow, kotlinx.coroutines.CoroutineScope coroutineScope, kotlinx.coroutines.flow.SharingStarted sharingStarted, int i, int i2, java.lang.Object obj) {
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return kotlinx.coroutines.flow.FlowKt.shareIn(flow, coroutineScope, sharingStarted, i);
    }

    public static final <T> kotlinx.coroutines.flow.SharedFlow<T> shareIn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.CoroutineScope scope, kotlinx.coroutines.flow.SharingStarted started, int replay) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scope, "scope");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(started, "started");
        kotlinx.coroutines.flow.SharingConfig config = configureSharing$FlowKt__ShareKt(flow, replay);
        kotlinx.coroutines.flow.MutableSharedFlow shared = kotlinx.coroutines.flow.SharedFlowKt.MutableSharedFlow(replay, config.extraBufferCapacity, config.onBufferOverflow);
        kotlinx.coroutines.Job job = launchSharing$FlowKt__ShareKt(scope, config.context, config.upstream, shared, started, kotlinx.coroutines.flow.SharedFlowKt.NO_VALUE);
        return new kotlinx.coroutines.flow.ReadonlySharedFlow(shared, job);
    }

    private static final <T> kotlinx.coroutines.flow.SharingConfig<T> configureSharing$FlowKt__ShareKt(kotlinx.coroutines.flow.Flow<? extends T> flow, int replay) {
        kotlinx.coroutines.flow.Flow<T> flowDropChannelOperators;
        int i = 1;
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if ((replay >= 0 ? 1 : 0) == 0) {
                throw new java.lang.AssertionError();
            }
        }
        int defaultExtraCapacity = kotlin.ranges.RangesKt.coerceAtLeast(replay, kotlinx.coroutines.channels.Channel.INSTANCE.getCHANNEL_DEFAULT_CAPACITY$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) - replay;
        if ((flow instanceof kotlinx.coroutines.flow.internal.ChannelFlow) && (flowDropChannelOperators = ((kotlinx.coroutines.flow.internal.ChannelFlow) flow).dropChannelOperators()) != null) {
            switch (((kotlinx.coroutines.flow.internal.ChannelFlow) flow).capacity) {
                case -3:
                case -2:
                case 0:
                    if (((kotlinx.coroutines.flow.internal.ChannelFlow) flow).onBufferOverflow == kotlinx.coroutines.channels.BufferOverflow.SUSPEND) {
                        i = ((kotlinx.coroutines.flow.internal.ChannelFlow) flow).capacity != 0 ? defaultExtraCapacity : 0;
                    } else if (replay != 0) {
                        i = 0;
                    }
                    break;
                case -1:
                default:
                    i = ((kotlinx.coroutines.flow.internal.ChannelFlow) flow).capacity;
                    break;
            }
            return new kotlinx.coroutines.flow.SharingConfig<>(flowDropChannelOperators, i, ((kotlinx.coroutines.flow.internal.ChannelFlow) flow).onBufferOverflow, ((kotlinx.coroutines.flow.internal.ChannelFlow) flow).context);
        }
        return new kotlinx.coroutines.flow.SharingConfig<>(flow, defaultExtraCapacity, kotlinx.coroutines.channels.BufferOverflow.SUSPEND, kotlin.coroutines.EmptyCoroutineContext.INSTANCE);
    }

    private static final <T> kotlinx.coroutines.Job launchSharing$FlowKt__ShareKt(kotlinx.coroutines.CoroutineScope $this$launchSharing, kotlin.coroutines.CoroutineContext context, kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.flow.MutableSharedFlow<T> mutableSharedFlow, kotlinx.coroutines.flow.SharingStarted started, T t) {
        kotlinx.coroutines.CoroutineStart start = kotlin.jvm.internal.Intrinsics.areEqual(started, kotlinx.coroutines.flow.SharingStarted.INSTANCE.getEagerly()) ? kotlinx.coroutines.CoroutineStart.DEFAULT : kotlinx.coroutines.CoroutineStart.UNDISPATCHED;
        return kotlinx.coroutines.BuildersKt.launch($this$launchSharing, context, start, new kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1(started, flow, mutableSharedFlow, t, null));
    }

    public static final <T> kotlinx.coroutines.flow.StateFlow<T> stateIn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.CoroutineScope scope, kotlinx.coroutines.flow.SharingStarted started, T t) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scope, "scope");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(started, "started");
        kotlinx.coroutines.flow.SharingConfig config = configureSharing$FlowKt__ShareKt(flow, 1);
        kotlinx.coroutines.flow.MutableStateFlow state = kotlinx.coroutines.flow.StateFlowKt.MutableStateFlow(t);
        kotlinx.coroutines.Job job = launchSharing$FlowKt__ShareKt(scope, config.context, config.upstream, state, started, t);
        return new kotlinx.coroutines.flow.ReadonlyStateFlow(state, job);
    }

    public static final <T> java.lang.Object stateIn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.CoroutineScope scope, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.StateFlow<? extends T>> continuation) {
        kotlinx.coroutines.flow.SharingConfig config = configureSharing$FlowKt__ShareKt(flow, 1);
        kotlinx.coroutines.CompletableDeferred result = kotlinx.coroutines.CompletableDeferredKt.CompletableDeferred$default(null, 1, null);
        launchSharingDeferred$FlowKt__ShareKt(scope, config.context, config.upstream, result);
        return result.await(continuation);
    }

    private static final <T> void launchSharingDeferred$FlowKt__ShareKt(kotlinx.coroutines.CoroutineScope $this$launchSharingDeferred, kotlin.coroutines.CoroutineContext context, kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.CompletableDeferred<kotlinx.coroutines.flow.StateFlow<T>> completableDeferred) {
        kotlinx.coroutines.BuildersKt__Builders_commonKt.launch$default($this$launchSharingDeferred, context, null, new kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharingDeferred$1(flow, completableDeferred, null), 2, null);
    }

    public static final <T> kotlinx.coroutines.flow.SharedFlow<T> asSharedFlow(kotlinx.coroutines.flow.MutableSharedFlow<T> mutableSharedFlow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mutableSharedFlow, "<this>");
        return new kotlinx.coroutines.flow.ReadonlySharedFlow(mutableSharedFlow, null);
    }

    public static final <T> kotlinx.coroutines.flow.StateFlow<T> asStateFlow(kotlinx.coroutines.flow.MutableStateFlow<T> mutableStateFlow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mutableStateFlow, "<this>");
        return new kotlinx.coroutines.flow.ReadonlyStateFlow(mutableStateFlow, null);
    }

    public static final <T> kotlinx.coroutines.flow.SharedFlow<T> onSubscription(kotlinx.coroutines.flow.SharedFlow<? extends T> sharedFlow, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sharedFlow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        return new kotlinx.coroutines.flow.SubscribedSharedFlow(sharedFlow, action);
    }
}
