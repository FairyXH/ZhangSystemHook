package kotlinx.coroutines.flow;

/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"kotlinx/coroutines/flow/FlowKt__BuildersKt", "kotlinx/coroutines/flow/FlowKt__ChannelsKt", "kotlinx/coroutines/flow/FlowKt__CollectKt", "kotlinx/coroutines/flow/FlowKt__CollectionKt", "kotlinx/coroutines/flow/FlowKt__ContextKt", "kotlinx/coroutines/flow/FlowKt__CountKt", "kotlinx/coroutines/flow/FlowKt__DelayKt", "kotlinx/coroutines/flow/FlowKt__DistinctKt", "kotlinx/coroutines/flow/FlowKt__EmittersKt", "kotlinx/coroutines/flow/FlowKt__ErrorsKt", "kotlinx/coroutines/flow/FlowKt__LimitKt", "kotlinx/coroutines/flow/FlowKt__MergeKt", "kotlinx/coroutines/flow/FlowKt__MigrationKt", "kotlinx/coroutines/flow/FlowKt__ReduceKt", "kotlinx/coroutines/flow/FlowKt__ShareKt", "kotlinx/coroutines/flow/FlowKt__TransformKt", "kotlinx/coroutines/flow/FlowKt__ZipKt"}, k = 4, mv = {1, 9, 0}, xi = 48)
public final class FlowKt {
    public static final java.lang.String DEFAULT_CONCURRENCY_PROPERTY_NAME = "kotlinx.coroutines.flow.defaultConcurrency";

    public static final <T> kotlinx.coroutines.flow.Flow<T> asFlow(java.lang.Iterable<? extends T> iterable) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.asFlow(iterable);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> asFlow(java.util.Iterator<? extends T> it) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.asFlow(it);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> asFlow(kotlin.jvm.functions.Function0<? extends T> function0) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.asFlow(function0);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> asFlow(kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function1) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.asFlow(function1);
    }

    public static final kotlinx.coroutines.flow.Flow<java.lang.Integer> asFlow(kotlin.ranges.IntRange $this$asFlow) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.asFlow($this$asFlow);
    }

    public static final kotlinx.coroutines.flow.Flow<java.lang.Long> asFlow(kotlin.ranges.LongRange $this$asFlow) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.asFlow($this$asFlow);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> asFlow(kotlin.sequences.Sequence<? extends T> sequence) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.asFlow(sequence);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "'BroadcastChannel' is obsolete and all corresponding operators are deprecated in the favour of StateFlow and SharedFlow")
    public static final <T> kotlinx.coroutines.flow.Flow<T> asFlow(kotlinx.coroutines.channels.BroadcastChannel<T> broadcastChannel) {
        return kotlinx.coroutines.flow.FlowKt__ChannelsKt.asFlow(broadcastChannel);
    }

    public static final kotlinx.coroutines.flow.Flow<java.lang.Integer> asFlow(int[] $this$asFlow) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.asFlow($this$asFlow);
    }

    public static final kotlinx.coroutines.flow.Flow<java.lang.Long> asFlow(long[] $this$asFlow) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.asFlow($this$asFlow);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> asFlow(T[] tArr) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.asFlow(tArr);
    }

    public static final <T> kotlinx.coroutines.flow.SharedFlow<T> asSharedFlow(kotlinx.coroutines.flow.MutableSharedFlow<T> mutableSharedFlow) {
        return kotlinx.coroutines.flow.FlowKt__ShareKt.asSharedFlow(mutableSharedFlow);
    }

    public static final <T> kotlinx.coroutines.flow.StateFlow<T> asStateFlow(kotlinx.coroutines.flow.MutableStateFlow<T> mutableStateFlow) {
        return kotlinx.coroutines.flow.FlowKt__ShareKt.asStateFlow(mutableStateFlow);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> buffer(kotlinx.coroutines.flow.Flow<? extends T> flow, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        return kotlinx.coroutines.flow.FlowKt__ContextKt.buffer(flow, capacity, onBufferOverflow);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'cache()' is 'shareIn' with unlimited replay and 'started = SharingStared.Lazily' argument'", replaceWith = @kotlin.ReplaceWith(expression = "this.shareIn(scope, Int.MAX_VALUE, started = SharingStared.Lazily)", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> cache(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.cache(flow);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> callbackFlow(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.channels.ProducerScope<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.callbackFlow(function2);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> cancellable(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        return kotlinx.coroutines.flow.FlowKt__ContextKt.cancellable(flow);
    }

    /* JADX INFO: renamed from: catch, reason: not valid java name */
    public static final <T> kotlinx.coroutines.flow.Flow<T> m12838catch(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super java.lang.Throwable, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__ErrorsKt.m12849catch(flow, function3);
    }

    public static final <T> java.lang.Object catchImpl(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super java.lang.Throwable> continuation) {
        return kotlinx.coroutines.flow.FlowKt__ErrorsKt.catchImpl(flow, flowCollector, continuation);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> channelFlow(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.channels.ProducerScope<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.channelFlow(function2);
    }

    public static final java.lang.Object collect(kotlinx.coroutines.flow.Flow<?> flow, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return kotlinx.coroutines.flow.FlowKt__CollectKt.collect(flow, continuation);
    }

    public static final <T> java.lang.Object collectIndexed(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return kotlinx.coroutines.flow.FlowKt__CollectKt.collectIndexed(flow, function3, continuation);
    }

    public static final <T> java.lang.Object collectLatest(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return kotlinx.coroutines.flow.FlowKt__CollectKt.collectLatest(flow, function2, continuation);
    }

    public static final <T> java.lang.Object collectWhile(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return kotlinx.coroutines.flow.FlowKt__LimitKt.collectWhile(flow, function2, continuation);
    }

    public static final <T1, T2, R> kotlinx.coroutines.flow.Flow<R> combine(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlin.jvm.functions.Function3<? super T1, ? super T2, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__ZipKt.combine(flow, flow2, function3);
    }

    public static final <T1, T2, T3, R> kotlinx.coroutines.flow.Flow<R> combine(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, kotlin.jvm.functions.Function4<? super T1, ? super T2, ? super T3, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function4) {
        return kotlinx.coroutines.flow.FlowKt__ZipKt.combine(flow, flow2, flow3, function4);
    }

    public static final <T1, T2, T3, T4, R> kotlinx.coroutines.flow.Flow<R> combine(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, kotlinx.coroutines.flow.Flow<? extends T4> flow4, kotlin.jvm.functions.Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function5) {
        return kotlinx.coroutines.flow.FlowKt__ZipKt.combine(flow, flow2, flow3, flow4, function5);
    }

    public static final <T1, T2, T3, T4, T5, R> kotlinx.coroutines.flow.Flow<R> combine(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, kotlinx.coroutines.flow.Flow<? extends T4> flow4, kotlinx.coroutines.flow.Flow<? extends T5> flow5, kotlin.jvm.functions.Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function6) {
        return kotlinx.coroutines.flow.FlowKt__ZipKt.combine(flow, flow2, flow3, flow4, flow5, function6);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'combineLatest' is 'combine'", replaceWith = @kotlin.ReplaceWith(expression = "this.combine(other, transform)", imports = {}))
    public static final <T1, T2, R> kotlinx.coroutines.flow.Flow<R> combineLatest(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlin.jvm.functions.Function3<? super T1, ? super T2, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.combineLatest(flow, flow2, function3);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'combineLatest' is 'combine'", replaceWith = @kotlin.ReplaceWith(expression = "combine(this, other, other2, transform)", imports = {}))
    public static final <T1, T2, T3, R> kotlinx.coroutines.flow.Flow<R> combineLatest(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, kotlin.jvm.functions.Function4<? super T1, ? super T2, ? super T3, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function4) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.combineLatest(flow, flow2, flow3, function4);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'combineLatest' is 'combine'", replaceWith = @kotlin.ReplaceWith(expression = "combine(this, other, other2, other3, transform)", imports = {}))
    public static final <T1, T2, T3, T4, R> kotlinx.coroutines.flow.Flow<R> combineLatest(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, kotlinx.coroutines.flow.Flow<? extends T4> flow4, kotlin.jvm.functions.Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function5) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.combineLatest(flow, flow2, flow3, flow4, function5);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'combineLatest' is 'combine'", replaceWith = @kotlin.ReplaceWith(expression = "combine(this, other, other2, other3, transform)", imports = {}))
    public static final <T1, T2, T3, T4, T5, R> kotlinx.coroutines.flow.Flow<R> combineLatest(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, kotlinx.coroutines.flow.Flow<? extends T4> flow4, kotlinx.coroutines.flow.Flow<? extends T5> flow5, kotlin.jvm.functions.Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function6) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.combineLatest(flow, flow2, flow3, flow4, flow5, function6);
    }

    public static final <T1, T2, R> kotlinx.coroutines.flow.Flow<R> combineTransform(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlin.jvm.functions.Function4<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T1, ? super T2, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function4) {
        return kotlinx.coroutines.flow.FlowKt__ZipKt.combineTransform(flow, flow2, function4);
    }

    public static final <T1, T2, T3, R> kotlinx.coroutines.flow.Flow<R> combineTransform(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, kotlin.jvm.functions.Function5<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T1, ? super T2, ? super T3, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function5) {
        return kotlinx.coroutines.flow.FlowKt__ZipKt.combineTransform(flow, flow2, flow3, function5);
    }

    public static final <T1, T2, T3, T4, R> kotlinx.coroutines.flow.Flow<R> combineTransform(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, kotlinx.coroutines.flow.Flow<? extends T4> flow4, kotlin.jvm.functions.Function6<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T1, ? super T2, ? super T3, ? super T4, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function6) {
        return kotlinx.coroutines.flow.FlowKt__ZipKt.combineTransform(flow, flow2, flow3, flow4, function6);
    }

    public static final <T1, T2, T3, T4, T5, R> kotlinx.coroutines.flow.Flow<R> combineTransform(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, kotlinx.coroutines.flow.Flow<? extends T4> flow4, kotlinx.coroutines.flow.Flow<? extends T5> flow5, kotlin.jvm.functions.Function7<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function7) {
        return kotlinx.coroutines.flow.FlowKt__ZipKt.combineTransform(flow, flow2, flow3, flow4, flow5, function7);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'compose' is 'let'", replaceWith = @kotlin.ReplaceWith(expression = "let(transformer)", imports = {}))
    public static final <T, R> kotlinx.coroutines.flow.Flow<R> compose(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function1<? super kotlinx.coroutines.flow.Flow<? extends T>, ? extends kotlinx.coroutines.flow.Flow<? extends R>> function1) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.compose(flow, function1);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'concatMap' is 'flatMapConcat'", replaceWith = @kotlin.ReplaceWith(expression = "flatMapConcat(mapper)", imports = {}))
    public static final <T, R> kotlinx.coroutines.flow.Flow<R> concatMap(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function1<? super T, ? extends kotlinx.coroutines.flow.Flow<? extends R>> function1) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.concatMap(flow, function1);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'concatWith' is 'onCompletion'. Use 'onCompletion { emit(value) }'", replaceWith = @kotlin.ReplaceWith(expression = "onCompletion { emit(value) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> concatWith(kotlinx.coroutines.flow.Flow<? extends T> flow, T t) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.concatWith(flow, t);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'concatWith' is 'onCompletion'. Use 'onCompletion { if (it == null) emitAll(other) }'", replaceWith = @kotlin.ReplaceWith(expression = "onCompletion { if (it == null) emitAll(other) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> concatWith(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.flow.Flow<? extends T> flow2) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.concatWith((kotlinx.coroutines.flow.Flow) flow, (kotlinx.coroutines.flow.Flow) flow2);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> conflate(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        return kotlinx.coroutines.flow.FlowKt__ContextKt.conflate(flow);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> consumeAsFlow(kotlinx.coroutines.channels.ReceiveChannel<? extends T> receiveChannel) {
        return kotlinx.coroutines.flow.FlowKt__ChannelsKt.consumeAsFlow(receiveChannel);
    }

    public static final <T> java.lang.Object count(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.Continuation<? super java.lang.Integer> continuation) {
        return kotlinx.coroutines.flow.FlowKt__CountKt.count(flow, continuation);
    }

    public static final <T> java.lang.Object count(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super java.lang.Integer> continuation) {
        return kotlinx.coroutines.flow.FlowKt__CountKt.count(flow, function2, continuation);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> debounce(kotlinx.coroutines.flow.Flow<? extends T> flow, long timeoutMillis) {
        return kotlinx.coroutines.flow.FlowKt__DelayKt.debounce(flow, timeoutMillis);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> debounce(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function1<? super T, java.lang.Long> function1) {
        return kotlinx.coroutines.flow.FlowKt__DelayKt.debounce(flow, function1);
    }

    /* JADX INFO: renamed from: debounce-HG0u8IE, reason: not valid java name */
    public static final <T> kotlinx.coroutines.flow.Flow<T> m12839debounceHG0u8IE(kotlinx.coroutines.flow.Flow<? extends T> flow, long timeout) {
        return kotlinx.coroutines.flow.FlowKt__DelayKt.m12842debounceHG0u8IE(flow, timeout);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> debounceDuration(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function1<? super T, kotlin.time.Duration> function1) {
        return kotlinx.coroutines.flow.FlowKt__DelayKt.debounceDuration(flow, function1);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use 'onEach { delay(timeMillis) }'", replaceWith = @kotlin.ReplaceWith(expression = "onEach { delay(timeMillis) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> delayEach(kotlinx.coroutines.flow.Flow<? extends T> flow, long timeMillis) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.delayEach(flow, timeMillis);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use 'onStart { delay(timeMillis) }'", replaceWith = @kotlin.ReplaceWith(expression = "onStart { delay(timeMillis) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> delayFlow(kotlinx.coroutines.flow.Flow<? extends T> flow, long timeMillis) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.delayFlow(flow, timeMillis);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> distinctUntilChanged(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        return kotlinx.coroutines.flow.FlowKt__DistinctKt.distinctUntilChanged(flow);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> distinctUntilChanged(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super T, java.lang.Boolean> function2) {
        return kotlinx.coroutines.flow.FlowKt__DistinctKt.distinctUntilChanged(flow, function2);
    }

    public static final <T, K> kotlinx.coroutines.flow.Flow<T> distinctUntilChangedBy(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function1<? super T, ? extends K> function1) {
        return kotlinx.coroutines.flow.FlowKt__DistinctKt.distinctUntilChangedBy(flow, function1);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> drop(kotlinx.coroutines.flow.Flow<? extends T> flow, int count) {
        return kotlinx.coroutines.flow.FlowKt__LimitKt.drop(flow, count);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> dropWhile(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__LimitKt.dropWhile(flow, function2);
    }

    public static final <T> java.lang.Object emitAll(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlinx.coroutines.channels.ReceiveChannel<? extends T> receiveChannel, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return kotlinx.coroutines.flow.FlowKt__ChannelsKt.emitAll(flowCollector, receiveChannel, continuation);
    }

    public static final <T> java.lang.Object emitAll(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return kotlinx.coroutines.flow.FlowKt__CollectKt.emitAll(flowCollector, flow, continuation);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> emptyFlow() {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.emptyFlow();
    }

    public static final void ensureActive(kotlinx.coroutines.flow.FlowCollector<?> flowCollector) {
        kotlinx.coroutines.flow.FlowKt__EmittersKt.ensureActive(flowCollector);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> filter(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__TransformKt.filter(flow, function2);
    }

    public static final <R> kotlinx.coroutines.flow.Flow<R> filterIsInstance(kotlinx.coroutines.flow.Flow<?> flow, kotlin.reflect.KClass<R> kClass) {
        return kotlinx.coroutines.flow.FlowKt__TransformKt.filterIsInstance(flow, kClass);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> filterNot(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__TransformKt.filterNot(flow, function2);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> filterNotNull(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        return kotlinx.coroutines.flow.FlowKt__TransformKt.filterNotNull(flow);
    }

    public static final <T> java.lang.Object first(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.Continuation<? super T> continuation) {
        return kotlinx.coroutines.flow.FlowKt__ReduceKt.first(flow, continuation);
    }

    public static final <T> java.lang.Object first(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super T> continuation) {
        return kotlinx.coroutines.flow.FlowKt__ReduceKt.first(flow, function2, continuation);
    }

    public static final <T> java.lang.Object firstOrNull(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.Continuation<? super T> continuation) {
        return kotlinx.coroutines.flow.FlowKt__ReduceKt.firstOrNull(flow, continuation);
    }

    public static final <T> java.lang.Object firstOrNull(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super T> continuation) {
        return kotlinx.coroutines.flow.FlowKt__ReduceKt.firstOrNull(flow, function2, continuation);
    }

    public static final kotlinx.coroutines.channels.ReceiveChannel<kotlin.Unit> fixedPeriodTicker(kotlinx.coroutines.CoroutineScope $this$fixedPeriodTicker, long delayMillis, long initialDelayMillis) {
        return kotlinx.coroutines.flow.FlowKt__DelayKt.fixedPeriodTicker($this$fixedPeriodTicker, delayMillis, initialDelayMillis);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue is 'flatMapConcat'", replaceWith = @kotlin.ReplaceWith(expression = "flatMapConcat(mapper)", imports = {}))
    public static final <T, R> kotlinx.coroutines.flow.Flow<R> flatMap(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends R>>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.flatMap(flow, function2);
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> flatMapConcat(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends R>>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__MergeKt.flatMapConcat(flow, function2);
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> flatMapLatest(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends R>>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__MergeKt.flatMapLatest(flow, function2);
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> flatMapMerge(kotlinx.coroutines.flow.Flow<? extends T> flow, int concurrency, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends R>>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__MergeKt.flatMapMerge(flow, concurrency, function2);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'flatten' is 'flattenConcat'", replaceWith = @kotlin.ReplaceWith(expression = "flattenConcat()", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> flatten(kotlinx.coroutines.flow.Flow<? extends kotlinx.coroutines.flow.Flow<? extends T>> flow) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.flatten(flow);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> flattenConcat(kotlinx.coroutines.flow.Flow<? extends kotlinx.coroutines.flow.Flow<? extends T>> flow) {
        return kotlinx.coroutines.flow.FlowKt__MergeKt.flattenConcat(flow);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> flattenMerge(kotlinx.coroutines.flow.Flow<? extends kotlinx.coroutines.flow.Flow<? extends T>> flow, int concurrency) {
        return kotlinx.coroutines.flow.FlowKt__MergeKt.flattenMerge(flow, concurrency);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> flow(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.flow(function2);
    }

    public static final <T1, T2, R> kotlinx.coroutines.flow.Flow<R> flowCombine(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlin.jvm.functions.Function3<? super T1, ? super T2, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__ZipKt.flowCombine(flow, flow2, function3);
    }

    public static final <T1, T2, R> kotlinx.coroutines.flow.Flow<R> flowCombineTransform(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlin.jvm.functions.Function4<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T1, ? super T2, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function4) {
        return kotlinx.coroutines.flow.FlowKt__ZipKt.flowCombineTransform(flow, flow2, function4);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> flowOf(T t) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.flowOf(t);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> flowOf(T... tArr) {
        return kotlinx.coroutines.flow.FlowKt__BuildersKt.flowOf((java.lang.Object[]) tArr);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> flowOn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.CoroutineContext context) {
        return kotlinx.coroutines.flow.FlowKt__ContextKt.flowOn(flow, context);
    }

    public static final <T, R> java.lang.Object fold(kotlinx.coroutines.flow.Flow<? extends T> flow, R r, kotlin.jvm.functions.Function3<? super R, ? super T, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function3, kotlin.coroutines.Continuation<? super R> continuation) {
        return kotlinx.coroutines.flow.FlowKt__ReduceKt.fold(flow, r, function3, continuation);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'forEach' is 'collect'", replaceWith = @kotlin.ReplaceWith(expression = "collect(action)", imports = {}))
    public static final <T> void forEach(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2) {
        kotlinx.coroutines.flow.FlowKt__MigrationKt.forEach(flow, function2);
    }

    public static final int getDEFAULT_CONCURRENCY() {
        return kotlinx.coroutines.flow.FlowKt__MergeKt.getDEFAULT_CONCURRENCY();
    }

    public static final <T> java.lang.Object last(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.Continuation<? super T> continuation) {
        return kotlinx.coroutines.flow.FlowKt__ReduceKt.last(flow, continuation);
    }

    public static final <T> java.lang.Object lastOrNull(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.Continuation<? super T> continuation) {
        return kotlinx.coroutines.flow.FlowKt__ReduceKt.lastOrNull(flow, continuation);
    }

    public static final <T> kotlinx.coroutines.Job launchIn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.CoroutineScope scope) {
        return kotlinx.coroutines.flow.FlowKt__CollectKt.launchIn(flow, scope);
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> map(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__TransformKt.map(flow, function2);
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> mapLatest(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__MergeKt.mapLatest(flow, function2);
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> mapNotNull(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__TransformKt.mapNotNull(flow, function2);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> merge(java.lang.Iterable<? extends kotlinx.coroutines.flow.Flow<? extends T>> iterable) {
        return kotlinx.coroutines.flow.FlowKt__MergeKt.merge(iterable);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'merge' is 'flattenConcat'", replaceWith = @kotlin.ReplaceWith(expression = "flattenConcat()", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> merge(kotlinx.coroutines.flow.Flow<? extends kotlinx.coroutines.flow.Flow<? extends T>> flow) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.merge(flow);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> merge(kotlinx.coroutines.flow.Flow<? extends T>... flowArr) {
        return kotlinx.coroutines.flow.FlowKt__MergeKt.merge(flowArr);
    }

    public static final java.lang.Void noImpl() {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.noImpl();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Collect flow in the desired context instead")
    public static final <T> kotlinx.coroutines.flow.Flow<T> observeOn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.CoroutineContext context) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.observeOn(flow, context);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> onCompletion(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super java.lang.Throwable, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__EmittersKt.onCompletion(flow, function3);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> onEach(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__TransformKt.onEach(flow, function2);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> onEmpty(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__EmittersKt.onEmpty(flow, function2);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'onErrorXxx' is 'catch'. Use 'catch { emitAll(fallback) }'", replaceWith = @kotlin.ReplaceWith(expression = "catch { emitAll(fallback) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> onErrorResume(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.flow.Flow<? extends T> flow2) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.onErrorResume(flow, flow2);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'onErrorXxx' is 'catch'. Use 'catch { emitAll(fallback) }'", replaceWith = @kotlin.ReplaceWith(expression = "catch { emitAll(fallback) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> onErrorResumeNext(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.flow.Flow<? extends T> flow2) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.onErrorResumeNext(flow, flow2);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'onErrorXxx' is 'catch'. Use 'catch { emit(fallback) }'", replaceWith = @kotlin.ReplaceWith(expression = "catch { emit(fallback) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> onErrorReturn(kotlinx.coroutines.flow.Flow<? extends T> flow, T t) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.onErrorReturn(flow, t);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'onErrorXxx' is 'catch'. Use 'catch { e -> if (predicate(e)) emit(fallback) else throw e }'", replaceWith = @kotlin.ReplaceWith(expression = "catch { e -> if (predicate(e)) emit(fallback) else throw e }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> onErrorReturn(kotlinx.coroutines.flow.Flow<? extends T> flow, T t, kotlin.jvm.functions.Function1<? super java.lang.Throwable, java.lang.Boolean> function1) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.onErrorReturn(flow, t, function1);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> onStart(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__EmittersKt.onStart(flow, function2);
    }

    public static final <T> kotlinx.coroutines.flow.SharedFlow<T> onSubscription(kotlinx.coroutines.flow.SharedFlow<? extends T> sharedFlow, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__ShareKt.onSubscription(sharedFlow, function2);
    }

    public static final <T> kotlinx.coroutines.channels.ReceiveChannel<T> produceIn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.CoroutineScope scope) {
        return kotlinx.coroutines.flow.FlowKt__ChannelsKt.produceIn(flow, scope);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'publish()' is 'shareIn'. \npublish().connect() is the default strategy (no extra call is needed), \npublish().autoConnect() translates to 'started = SharingStared.Lazily' argument, \npublish().refCount() translates to 'started = SharingStared.WhileSubscribed()' argument.", replaceWith = @kotlin.ReplaceWith(expression = "this.shareIn(scope, 0)", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> publish(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.publish(flow);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'publish(bufferSize)' is 'buffer' followed by 'shareIn'. \npublish().connect() is the default strategy (no extra call is needed), \npublish().autoConnect() translates to 'started = SharingStared.Lazily' argument, \npublish().refCount() translates to 'started = SharingStared.WhileSubscribed()' argument.", replaceWith = @kotlin.ReplaceWith(expression = "this.buffer(bufferSize).shareIn(scope, 0)", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> publish(kotlinx.coroutines.flow.Flow<? extends T> flow, int bufferSize) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.publish(flow, bufferSize);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Collect flow in the desired context instead")
    public static final <T> kotlinx.coroutines.flow.Flow<T> publishOn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.CoroutineContext context) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.publishOn(flow, context);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> receiveAsFlow(kotlinx.coroutines.channels.ReceiveChannel<? extends T> receiveChannel) {
        return kotlinx.coroutines.flow.FlowKt__ChannelsKt.receiveAsFlow(receiveChannel);
    }

    public static final <S, T extends S> java.lang.Object reduce(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super S, ? super T, ? super kotlin.coroutines.Continuation<? super S>, ? extends java.lang.Object> function3, kotlin.coroutines.Continuation<? super S> continuation) {
        return kotlinx.coroutines.flow.FlowKt__ReduceKt.reduce(flow, function3, continuation);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'replay()' is 'shareIn' with unlimited replay. \nreplay().connect() is the default strategy (no extra call is needed), \nreplay().autoConnect() translates to 'started = SharingStared.Lazily' argument, \nreplay().refCount() translates to 'started = SharingStared.WhileSubscribed()' argument.", replaceWith = @kotlin.ReplaceWith(expression = "this.shareIn(scope, Int.MAX_VALUE)", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> replay(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.replay(flow);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'replay(bufferSize)' is 'shareIn' with the specified replay parameter. \nreplay().connect() is the default strategy (no extra call is needed), \nreplay().autoConnect() translates to 'started = SharingStared.Lazily' argument, \nreplay().refCount() translates to 'started = SharingStared.WhileSubscribed()' argument.", replaceWith = @kotlin.ReplaceWith(expression = "this.shareIn(scope, bufferSize)", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> replay(kotlinx.coroutines.flow.Flow<? extends T> flow, int bufferSize) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.replay(flow, bufferSize);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> retry(kotlinx.coroutines.flow.Flow<? extends T> flow, long retries, kotlin.jvm.functions.Function2<? super java.lang.Throwable, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__ErrorsKt.retry(flow, retries, function2);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> retryWhen(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function4<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super java.lang.Throwable, ? super java.lang.Long, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function4) {
        return kotlinx.coroutines.flow.FlowKt__ErrorsKt.retryWhen(flow, function4);
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> runningFold(kotlinx.coroutines.flow.Flow<? extends T> flow, R r, kotlin.jvm.functions.Function3<? super R, ? super T, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__TransformKt.runningFold(flow, r, function3);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> runningReduce(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super T, ? super T, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__TransformKt.runningReduce(flow, function3);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> sample(kotlinx.coroutines.flow.Flow<? extends T> flow, long periodMillis) {
        return kotlinx.coroutines.flow.FlowKt__DelayKt.sample(flow, periodMillis);
    }

    /* JADX INFO: renamed from: sample-HG0u8IE, reason: not valid java name */
    public static final <T> kotlinx.coroutines.flow.Flow<T> m12840sampleHG0u8IE(kotlinx.coroutines.flow.Flow<? extends T> flow, long period) {
        return kotlinx.coroutines.flow.FlowKt__DelayKt.m12843sampleHG0u8IE(flow, period);
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> scan(kotlinx.coroutines.flow.Flow<? extends T> flow, R r, kotlin.jvm.functions.Function3<? super R, ? super T, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__TransformKt.scan(flow, r, function3);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow has less verbose 'scan' shortcut", replaceWith = @kotlin.ReplaceWith(expression = "scan(initial, operation)", imports = {}))
    public static final <T, R> kotlinx.coroutines.flow.Flow<R> scanFold(kotlinx.coroutines.flow.Flow<? extends T> flow, R r, kotlin.jvm.functions.Function3<? super R, ? super T, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.scanFold(flow, r, function3);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "'scanReduce' was renamed to 'runningReduce' to be consistent with Kotlin standard library", replaceWith = @kotlin.ReplaceWith(expression = "runningReduce(operation)", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> scanReduce(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super T, ? super T, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.scanReduce(flow, function3);
    }

    public static final <T> kotlinx.coroutines.flow.SharedFlow<T> shareIn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.CoroutineScope scope, kotlinx.coroutines.flow.SharingStarted started, int replay) {
        return kotlinx.coroutines.flow.FlowKt__ShareKt.shareIn(flow, scope, started, replay);
    }

    public static final <T> java.lang.Object single(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.Continuation<? super T> continuation) {
        return kotlinx.coroutines.flow.FlowKt__ReduceKt.single(flow, continuation);
    }

    public static final <T> java.lang.Object singleOrNull(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.Continuation<? super T> continuation) {
        return kotlinx.coroutines.flow.FlowKt__ReduceKt.singleOrNull(flow, continuation);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'skip' is 'drop'", replaceWith = @kotlin.ReplaceWith(expression = "drop(count)", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> skip(kotlinx.coroutines.flow.Flow<? extends T> flow, int count) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.skip(flow, count);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'startWith' is 'onStart'. Use 'onStart { emit(value) }'", replaceWith = @kotlin.ReplaceWith(expression = "onStart { emit(value) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> startWith(kotlinx.coroutines.flow.Flow<? extends T> flow, T t) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.startWith(flow, t);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'startWith' is 'onStart'. Use 'onStart { emitAll(other) }'", replaceWith = @kotlin.ReplaceWith(expression = "onStart { emitAll(other) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> startWith(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.flow.Flow<? extends T> flow2) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.startWith((kotlinx.coroutines.flow.Flow) flow, (kotlinx.coroutines.flow.Flow) flow2);
    }

    public static final <T> java.lang.Object stateIn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.CoroutineScope scope, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.StateFlow<? extends T>> continuation) {
        return kotlinx.coroutines.flow.FlowKt__ShareKt.stateIn(flow, scope, continuation);
    }

    public static final <T> kotlinx.coroutines.flow.StateFlow<T> stateIn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.CoroutineScope scope, kotlinx.coroutines.flow.SharingStarted started, T t) {
        return kotlinx.coroutines.flow.FlowKt__ShareKt.stateIn(flow, scope, started, t);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use 'launchIn' with 'onEach', 'onCompletion' and 'catch' instead")
    public static final <T> void subscribe(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        kotlinx.coroutines.flow.FlowKt__MigrationKt.subscribe(flow);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use 'launchIn' with 'onEach', 'onCompletion' and 'catch' instead")
    public static final <T> void subscribe(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2) {
        kotlinx.coroutines.flow.FlowKt__MigrationKt.subscribe(flow, function2);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use 'launchIn' with 'onEach', 'onCompletion' and 'catch' instead")
    public static final <T> void subscribe(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2, kotlin.jvm.functions.Function2<? super java.lang.Throwable, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function22) {
        kotlinx.coroutines.flow.FlowKt__MigrationKt.subscribe(flow, function2, function22);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use 'flowOn' instead")
    public static final <T> kotlinx.coroutines.flow.Flow<T> subscribeOn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.CoroutineContext context) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.subscribeOn(flow, context);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogues of 'switchMap' are 'transformLatest', 'flatMapLatest' and 'mapLatest'", replaceWith = @kotlin.ReplaceWith(expression = "this.flatMapLatest(transform)", imports = {}))
    public static final <T, R> kotlinx.coroutines.flow.Flow<R> switchMap(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends R>>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__MigrationKt.switchMap(flow, function2);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> take(kotlinx.coroutines.flow.Flow<? extends T> flow, int count) {
        return kotlinx.coroutines.flow.FlowKt__LimitKt.take(flow, count);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> takeWhile(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.flow.FlowKt__LimitKt.takeWhile(flow, function2);
    }

    /* JADX INFO: renamed from: timeout-HG0u8IE, reason: not valid java name */
    public static final <T> kotlinx.coroutines.flow.Flow<T> m12841timeoutHG0u8IE(kotlinx.coroutines.flow.Flow<? extends T> flow, long timeout) {
        return kotlinx.coroutines.flow.FlowKt__DelayKt.m12844timeoutHG0u8IE(flow, timeout);
    }

    public static final <T, C extends java.util.Collection<? super T>> java.lang.Object toCollection(kotlinx.coroutines.flow.Flow<? extends T> flow, C c, kotlin.coroutines.Continuation<? super C> continuation) {
        return kotlinx.coroutines.flow.FlowKt__CollectionKt.toCollection(flow, c, continuation);
    }

    public static final <T> java.lang.Object toList(kotlinx.coroutines.flow.Flow<? extends T> flow, java.util.List<T> list, kotlin.coroutines.Continuation<? super java.util.List<? extends T>> continuation) {
        return kotlinx.coroutines.flow.FlowKt__CollectionKt.toList(flow, list, continuation);
    }

    public static final <T> java.lang.Object toSet(kotlinx.coroutines.flow.Flow<? extends T> flow, java.util.Set<T> set, kotlin.coroutines.Continuation<? super java.util.Set<? extends T>> continuation) {
        return kotlinx.coroutines.flow.FlowKt__CollectionKt.toSet(flow, set, continuation);
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> transform(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__EmittersKt.transform(flow, function3);
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> transformLatest(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__MergeKt.transformLatest(flow, function3);
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> transformWhile(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__LimitKt.transformWhile(flow, function3);
    }

    public static final <T, R> kotlinx.coroutines.flow.Flow<R> unsafeTransform(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__EmittersKt.unsafeTransform(flow, function3);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<kotlin.collections.IndexedValue<T>> withIndex(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        return kotlinx.coroutines.flow.FlowKt__TransformKt.withIndex(flow);
    }

    public static final <T1, T2, R> kotlinx.coroutines.flow.Flow<R> zip(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlin.jvm.functions.Function3<? super T1, ? super T2, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.flow.FlowKt__ZipKt.zip(flow, flow2, function3);
    }
}
