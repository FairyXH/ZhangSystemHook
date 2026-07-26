package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Builders.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0015\n\u0002\u0010\t\n\u0002\u0010\u0016\n\u0002\u0010\u001c\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001aH\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022/\b\u0001\u0010\u0003\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0004¢\u0006\u0002\b\t¢\u0006\u0002\u0010\n\u001aH\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022/\b\u0001\u0010\u0003\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0004¢\u0006\u0002\b\t¢\u0006\u0002\u0010\n\u001a\u0012\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\u001aH\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022/\b\u0001\u0010\u0003\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0004¢\u0006\u0002\b\t¢\u0006\u0002\u0010\n\u001a\u001f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0010\u001a\u0002H\u0002¢\u0006\u0002\u0010\u0011\u001a+\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0013\"\u0002H\u0002¢\u0006\u0002\u0010\u0014\u001a\u001c\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0016\u001a!\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0013¢\u0006\u0002\u0010\u0014\u001a\u0010\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0001*\u00020\u0018\u001a\u0010\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00190\u0001*\u00020\u001a\u001a\u001c\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u001b\u001a\u001c\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u001c\u001a\u0010\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0001*\u00020\u001d\u001a\u0010\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00190\u0001*\u00020\u001e\u001a\u001c\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u001f\u001a1\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0006\u0012\u0006\u0012\u0004\u0018\u00010\b0 ¢\u0006\u0002\u0010!¨\u0006\""}, d2 = {"callbackFlow", "Lkotlinx/coroutines/flow/Flow;", "T", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/channels/ProducerScope;", "Lkotlin/coroutines/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "channelFlow", "emptyFlow", "flow", "Lkotlinx/coroutines/flow/FlowCollector;", "flowOf", "value", "(Ljava/lang/Object;)Lkotlinx/coroutines/flow/Flow;", "elements", "", "([Ljava/lang/Object;)Lkotlinx/coroutines/flow/Flow;", "asFlow", "Lkotlin/Function0;", "", "", "", "", "", "", "Lkotlin/ranges/IntRange;", "Lkotlin/ranges/LongRange;", "Lkotlin/sequences/Sequence;", "Lkotlin/Function1;", "(Lkotlin/jvm/functions/Function1;)Lkotlinx/coroutines/flow/Flow;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
final /* synthetic */ class FlowKt__BuildersKt {
    public static final <T> kotlinx.coroutines.flow.Flow<T> flow(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.flow.FlowCollector<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        return new kotlinx.coroutines.flow.SafeFlow(block);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> asFlow(final kotlin.jvm.functions.Function0<? extends T> function0) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "<this>");
        return new kotlinx.coroutines.flow.Flow<T>() { // from class: kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$1
            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                java.lang.Object objEmit = flowCollector.emit((java.lang.Object) function0.invoke(), continuation);
                return objEmit == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objEmit : kotlin.Unit.INSTANCE;
            }
        };
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> asFlow(kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function1) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "<this>");
        return new kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$2(function1);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> asFlow(java.lang.Iterable<? extends T> iterable) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        return new kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$3(iterable);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> asFlow(java.util.Iterator<? extends T> it) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "<this>");
        return new kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$4(it);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> asFlow(kotlin.sequences.Sequence<? extends T> sequence) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        return new kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$5(sequence);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> flowOf(T... elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return new kotlinx.coroutines.flow.FlowKt__BuildersKt$flowOf$$inlined$unsafeFlow$1(elements);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> flowOf(final T t) {
        return new kotlinx.coroutines.flow.Flow<T>() { // from class: kotlinx.coroutines.flow.FlowKt__BuildersKt$flowOf$$inlined$unsafeFlow$2
            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                java.lang.Object objEmit = flowCollector.emit((java.lang.Object) t, continuation);
                return objEmit == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objEmit : kotlin.Unit.INSTANCE;
            }
        };
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> emptyFlow() {
        return kotlinx.coroutines.flow.EmptyFlow.INSTANCE;
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> asFlow(T[] tArr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "<this>");
        return new kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$6(tArr);
    }

    public static final kotlinx.coroutines.flow.Flow<java.lang.Integer> asFlow(int[] $this$asFlow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asFlow, "<this>");
        return new kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$7($this$asFlow);
    }

    public static final kotlinx.coroutines.flow.Flow<java.lang.Long> asFlow(long[] $this$asFlow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asFlow, "<this>");
        return new kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$8($this$asFlow);
    }

    public static final kotlinx.coroutines.flow.Flow<java.lang.Integer> asFlow(kotlin.ranges.IntRange $this$asFlow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asFlow, "<this>");
        return new kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$9($this$asFlow);
    }

    public static final kotlinx.coroutines.flow.Flow<java.lang.Long> asFlow(kotlin.ranges.LongRange $this$asFlow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asFlow, "<this>");
        return new kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$10($this$asFlow);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> channelFlow(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.channels.ProducerScope<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        return new kotlinx.coroutines.flow.ChannelFlowBuilder(block, null, 0, null, 14, null);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> callbackFlow(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.channels.ProducerScope<? super T>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        return new kotlinx.coroutines.flow.CallbackFlowBuilder(block, null, 0, null, 14, null);
    }
}
