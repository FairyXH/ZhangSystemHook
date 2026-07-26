package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: SharedFlow.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000J\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u001a0\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\t\u001a6\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00040\u000b\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\tH\u0000\u001a#\u0010\u0010\u001a\u0004\u0018\u00010\u0011*\n\u0012\u0006\u0012\u0004\u0018\u00010\u00110\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0002¢\u0006\u0002\u0010\u0015\u001a+\u0010\u0016\u001a\u00020\u0017*\n\u0012\u0006\u0012\u0004\u0018\u00010\u00110\u00122\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0018\u001a\u0004\u0018\u00010\u0011H\u0002¢\u0006\u0002\u0010\u0019\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"NO_VALUE", "Lkotlinx/coroutines/internal/Symbol;", "MutableSharedFlow", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "T", "replay", "", "extraBufferCapacity", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "fuseSharedFlow", "Lkotlinx/coroutines/flow/Flow;", "Lkotlinx/coroutines/flow/SharedFlow;", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "getBufferAt", "", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "([Ljava/lang/Object;J)Ljava/lang/Object;", "setBufferAt", "", com.android.server.pm.Settings.TAG_ITEM, "([Ljava/lang/Object;JLjava/lang/Object;)V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class SharedFlowKt {
    public static final kotlinx.coroutines.internal.Symbol NO_VALUE = new kotlinx.coroutines.internal.Symbol("NO_VALUE");

    public static /* synthetic */ kotlinx.coroutines.flow.MutableSharedFlow MutableSharedFlow$default(int i, int i2, kotlinx.coroutines.channels.BufferOverflow bufferOverflow, int i3, java.lang.Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = 0;
        }
        if ((i3 & 4) != 0) {
            bufferOverflow = kotlinx.coroutines.channels.BufferOverflow.SUSPEND;
        }
        return MutableSharedFlow(i, i2, bufferOverflow);
    }

    public static final <T> kotlinx.coroutines.flow.MutableSharedFlow<T> MutableSharedFlow(int replay, int extraBufferCapacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        boolean z = true;
        if (!(replay >= 0)) {
            throw new java.lang.IllegalArgumentException(("replay cannot be negative, but was " + replay).toString());
        }
        if (!(extraBufferCapacity >= 0)) {
            throw new java.lang.IllegalArgumentException(("extraBufferCapacity cannot be negative, but was " + extraBufferCapacity).toString());
        }
        if (replay <= 0 && extraBufferCapacity <= 0 && onBufferOverflow != kotlinx.coroutines.channels.BufferOverflow.SUSPEND) {
            z = false;
        }
        if (!z) {
            throw new java.lang.IllegalArgumentException(("replay or extraBufferCapacity must be positive with non-default onBufferOverflow strategy " + onBufferOverflow).toString());
        }
        int bufferCapacity0 = replay + extraBufferCapacity;
        int bufferCapacity = bufferCapacity0 < 0 ? Integer.MAX_VALUE : bufferCapacity0;
        return new kotlinx.coroutines.flow.SharedFlowImpl(replay, bufferCapacity, onBufferOverflow);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final java.lang.Object getBufferAt(java.lang.Object[] $this$getBufferAt, long index) {
        return $this$getBufferAt[((int) index) & ($this$getBufferAt.length - 1)];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void setBufferAt(java.lang.Object[] $this$setBufferAt, long index, java.lang.Object item) {
        $this$setBufferAt[((int) index) & ($this$setBufferAt.length - 1)] = item;
    }

    public static final <T> kotlinx.coroutines.flow.Flow<T> fuseSharedFlow(kotlinx.coroutines.flow.SharedFlow<? extends T> sharedFlow, kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sharedFlow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        if ((capacity == 0 || capacity == -3) && onBufferOverflow == kotlinx.coroutines.channels.BufferOverflow.SUSPEND) {
            return sharedFlow;
        }
        return new kotlinx.coroutines.flow.internal.ChannelFlowOperatorImpl(sharedFlow, context, capacity, onBufferOverflow);
    }
}
