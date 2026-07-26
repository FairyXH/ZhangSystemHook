package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Context.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000&\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u0015\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0002¢\u0006\u0002\b\u0004\u001a(\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00062\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a0\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00062\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b\u001a\u001c\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u0006\u001a\u001c\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u0006\u001a$\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00062\u0006\u0010\u0002\u001a\u00020\u0003¨\u0006\u000f"}, d2 = {"checkFlowContext", "", "context", "Lkotlin/coroutines/CoroutineContext;", "checkFlowContext$FlowKt__ContextKt", "buffer", "Lkotlinx/coroutines/flow/Flow;", "T", "capacity", "", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "cancellable", "conflate", "flowOn", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
final /* synthetic */ class FlowKt__ContextKt {
    public static /* synthetic */ kotlinx.coroutines.flow.Flow buffer$default(kotlinx.coroutines.flow.Flow flow, int i, kotlinx.coroutines.channels.BufferOverflow bufferOverflow, int i2, java.lang.Object obj) {
        if ((i2 & 1) != 0) {
            i = -2;
        }
        if ((i2 & 2) != 0) {
            bufferOverflow = kotlinx.coroutines.channels.BufferOverflow.SUSPEND;
        }
        return kotlinx.coroutines.flow.FlowKt.buffer(flow, i, bufferOverflow);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> buffer(kotlinx.coroutines.flow.Flow<? extends T> flow, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        if (!(capacity >= 0 || capacity == -2 || capacity == -1)) {
            throw new java.lang.IllegalArgumentException(("Buffer size should be non-negative, BUFFERED, or CONFLATED, but was " + capacity).toString());
        }
        if (!(capacity != -1 || onBufferOverflow == kotlinx.coroutines.channels.BufferOverflow.SUSPEND)) {
            throw new java.lang.IllegalArgumentException("CONFLATED capacity cannot be used with non-default onBufferOverflow".toString());
        }
        int capacity2 = capacity;
        kotlinx.coroutines.channels.BufferOverflow onBufferOverflow2 = onBufferOverflow;
        if (capacity2 == -1) {
            capacity2 = 0;
            onBufferOverflow2 = kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST;
        }
        if (flow instanceof kotlinx.coroutines.flow.internal.FusibleFlow) {
            return kotlinx.coroutines.flow.internal.FusibleFlow.DefaultImpls.fuse$default((kotlinx.coroutines.flow.internal.FusibleFlow) flow, null, capacity2, onBufferOverflow2, 1, null);
        }
        return new kotlinx.coroutines.flow.internal.ChannelFlowOperatorImpl(flow, null, capacity2, onBufferOverflow2, 2, null);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Since 1.4.0, binary compatibility with earlier versions")
    public static final /* synthetic */ kotlinx.coroutines.flow.Flow buffer(kotlinx.coroutines.flow.Flow $this$buffer, int capacity) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$buffer, "<this>");
        return buffer$default($this$buffer, capacity, null, 2, null);
    }

    public static /* synthetic */ kotlinx.coroutines.flow.Flow buffer$default(kotlinx.coroutines.flow.Flow flow, int i, int i2, java.lang.Object obj) {
        if ((i2 & 1) != 0) {
            i = -2;
        }
        return buffer(flow, i);
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> conflate(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        return buffer$default(flow, -1, null, 2, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> kotlinx.coroutines.flow.Flow<T> flowOn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        checkFlowContext$FlowKt__ContextKt(context);
        if (kotlin.jvm.internal.Intrinsics.areEqual(context, kotlin.coroutines.EmptyCoroutineContext.INSTANCE)) {
            return flow;
        }
        if (flow instanceof kotlinx.coroutines.flow.internal.FusibleFlow) {
            return kotlinx.coroutines.flow.internal.FusibleFlow.DefaultImpls.fuse$default((kotlinx.coroutines.flow.internal.FusibleFlow) flow, context, 0, null, 6, null);
        }
        return new kotlinx.coroutines.flow.internal.ChannelFlowOperatorImpl(flow, context, 0, null, 12, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> kotlinx.coroutines.flow.Flow<T> cancellable(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        return flow instanceof kotlinx.coroutines.flow.CancellableFlow ? flow : new kotlinx.coroutines.flow.CancellableFlowImpl(flow);
    }

    private static final void checkFlowContext$FlowKt__ContextKt(kotlin.coroutines.CoroutineContext context) {
        if (!(context.get(kotlinx.coroutines.Job.INSTANCE) == null)) {
            throw new java.lang.IllegalArgumentException(("Flow context cannot contain job in it. Had " + context).toString());
        }
    }
}
