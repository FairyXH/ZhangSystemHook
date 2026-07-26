package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: SharedFlow.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u000f\n\u0002\u0010 \n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0012\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00020\u00030\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00042\b\u0012\u0004\u0012\u0002H\u00010\u00052\b\u0012\u0004\u0012\u0002H\u00010\u0006:\u0001hB\u001d\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\b\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u0016\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\u0003H\u0082@¢\u0006\u0002\u0010.J\u0010\u0010/\u001a\u00020,2\u0006\u00100\u001a\u000201H\u0002J\b\u00102\u001a\u00020,H\u0002J\u001c\u00103\u001a\u0002042\f\u00105\u001a\b\u0012\u0004\u0012\u00028\u000006H\u0096@¢\u0006\u0002\u00107J\u0010\u00108\u001a\u00020,2\u0006\u00109\u001a\u00020\u0012H\u0002J\b\u0010:\u001a\u00020\u0003H\u0014J\u001d\u0010;\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u000e2\u0006\u0010<\u001a\u00020\bH\u0014¢\u0006\u0002\u0010=J\b\u0010>\u001a\u00020,H\u0002J\u0016\u0010?\u001a\u00020,2\u0006\u0010@\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010AJ\u0016\u0010B\u001a\u00020,2\u0006\u0010@\u001a\u00028\u0000H\u0082@¢\u0006\u0002\u0010AJ\u0012\u0010C\u001a\u00020,2\b\u0010D\u001a\u0004\u0018\u00010\u000fH\u0002J1\u0010E\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020,\u0018\u00010F0\u000e2\u0014\u0010G\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020,\u0018\u00010F0\u000eH\u0002¢\u0006\u0002\u0010HJ&\u0010I\u001a\b\u0012\u0004\u0012\u00028\u00000J2\u0006\u0010K\u001a\u00020L2\u0006\u0010M\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0012\u0010N\u001a\u0004\u0018\u00010\u000f2\u0006\u0010O\u001a\u00020\u0012H\u0002J7\u0010P\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\u000e2\u0010\u0010Q\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u000f\u0018\u00010\u000e2\u0006\u0010R\u001a\u00020\b2\u0006\u0010S\u001a\u00020\bH\u0002¢\u0006\u0002\u0010TJ\b\u0010U\u001a\u00020,H\u0016J\u0015\u0010V\u001a\u00020W2\u0006\u0010@\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010XJ\u0015\u0010Y\u001a\u00020W2\u0006\u0010@\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010XJ\u0015\u0010Z\u001a\u00020W2\u0006\u0010@\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010XJ\u0010\u0010[\u001a\u00020\u00122\u0006\u0010-\u001a\u00020\u0003H\u0002J\u0012\u0010\\\u001a\u0004\u0018\u00010\u000f2\u0006\u0010-\u001a\u00020\u0003H\u0002J(\u0010]\u001a\u00020,2\u0006\u0010^\u001a\u00020\u00122\u0006\u0010_\u001a\u00020\u00122\u0006\u0010`\u001a\u00020\u00122\u0006\u0010a\u001a\u00020\u0012H\u0002J%\u0010b\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020,\u0018\u00010F0\u000e2\u0006\u0010c\u001a\u00020\u0012H\u0000¢\u0006\u0004\bd\u0010eJ\r\u0010f\u001a\u00020\u0012H\u0000¢\u0006\u0002\bgR\u001a\u0010\r\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u000f\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0010R\u000e\u0010\t\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\u00020\u00128BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\u00020\u00128BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0014R\u001a\u0010\u0018\u001a\u00028\u00008DX\u0084\u0004¢\u0006\f\u0012\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u001b\u0010\u001cR\u000e\u0010\u001d\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u001e\u001a\u00020\u00128BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u001f\u0010\u0014R\u000e\u0010 \u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010!\u001a\b\u0012\u0004\u0012\u00028\u00000\"8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b#\u0010$R\u000e\u0010%\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010&\u001a\u00020\b8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b'\u0010(R\u0014\u0010)\u001a\u00020\b8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b*\u0010(¨\u0006i"}, d2 = {"Lkotlinx/coroutines/flow/SharedFlowImpl;", "T", "Lkotlinx/coroutines/flow/internal/AbstractSharedFlow;", "Lkotlinx/coroutines/flow/SharedFlowSlot;", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lkotlinx/coroutines/flow/CancellableFlow;", "Lkotlinx/coroutines/flow/internal/FusibleFlow;", "replay", "", "bufferCapacity", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "(IILkotlinx/coroutines/channels/BufferOverflow;)V", "buffer", "", "", "[Ljava/lang/Object;", "bufferEndIndex", "", "getBufferEndIndex", "()J", "bufferSize", "head", "getHead", "lastReplayedLocked", "getLastReplayedLocked$annotations", "()V", "getLastReplayedLocked", "()Ljava/lang/Object;", "minCollectorIndex", "queueEndIndex", "getQueueEndIndex", "queueSize", "replayCache", "", "getReplayCache", "()Ljava/util/List;", "replayIndex", "replaySize", "getReplaySize", "()I", "totalSize", "getTotalSize", "awaitValue", "", "slot", "(Lkotlinx/coroutines/flow/SharedFlowSlot;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cancelEmitter", "emitter", "Lkotlinx/coroutines/flow/SharedFlowImpl$Emitter;", "cleanupTailLocked", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "correctCollectorIndexesOnDropOldest", "newHead", "createSlot", "createSlotArray", "size", "(I)[Lkotlinx/coroutines/flow/SharedFlowSlot;", "dropOldestLocked", "emit", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "emitSuspend", "enqueueLocked", com.android.server.pm.Settings.TAG_ITEM, "findSlotsToResumeLocked", "Lkotlin/coroutines/Continuation;", "resumesIn", "([Lkotlin/coroutines/Continuation;)[Lkotlin/coroutines/Continuation;", "fuse", "Lkotlinx/coroutines/flow/Flow;", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "getPeekedValueLockedAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "growBuffer", "curBuffer", "curSize", "newSize", "([Ljava/lang/Object;II)[Ljava/lang/Object;", "resetReplayCache", "tryEmit", "", "(Ljava/lang/Object;)Z", "tryEmitLocked", "tryEmitNoCollectorsLocked", "tryPeekLocked", "tryTakeValue", "updateBufferLocked", "newReplayIndex", "newMinCollectorIndex", "newBufferEndIndex", "newQueueEndIndex", "updateCollectorIndexLocked", "oldIndex", "updateCollectorIndexLocked$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "(J)[Lkotlin/coroutines/Continuation;", "updateNewCollectorIndexLocked", "updateNewCollectorIndexLocked$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "Emitter", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class SharedFlowImpl<T> extends kotlinx.coroutines.flow.internal.AbstractSharedFlow<kotlinx.coroutines.flow.SharedFlowSlot> implements kotlinx.coroutines.flow.MutableSharedFlow<T>, kotlinx.coroutines.flow.CancellableFlow<T>, kotlinx.coroutines.flow.internal.FusibleFlow<T> {
    private java.lang.Object[] buffer;
    private final int bufferCapacity;
    private int bufferSize;
    private long minCollectorIndex;
    private final kotlinx.coroutines.channels.BufferOverflow onBufferOverflow;
    private int queueSize;
    private final int replay;
    private long replayIndex;

    /* JADX INFO: compiled from: SharedFlow.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[kotlinx.coroutines.channels.BufferOverflow.values().length];
            try {
                iArr[kotlinx.coroutines.channels.BufferOverflow.SUSPEND.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e) {
            }
            try {
                iArr[kotlinx.coroutines.channels.BufferOverflow.DROP_LATEST.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e2) {
            }
            try {
                iArr[kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError e3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.SharedFlowImpl$collect$1, reason: invalid class name */
    /* JADX INFO: compiled from: SharedFlow.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.SharedFlowImpl", f = "SharedFlow.kt", i = {0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2}, l = {372, 379, com.android.internal.util.FrameworkStatsLog.PRIVACY_TOGGLE_DIALOG_INTERACTION}, m = "collect$suspendImpl", n = {"$this", "collector", "slot", "$this", "collector", "slot", "collectorJob", "$this", "collector", "slot", "collectorJob"}, s = {"L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$3", "L$0", "L$1", "L$2", "L$3"})
    static final class AnonymousClass1<T> extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        java.lang.Object L$0;
        java.lang.Object L$1;
        java.lang.Object L$2;
        java.lang.Object L$3;
        int label;
        /* synthetic */ java.lang.Object result;
        final /* synthetic */ kotlinx.coroutines.flow.SharedFlowImpl<T> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(kotlinx.coroutines.flow.SharedFlowImpl<T> sharedFlowImpl, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.SharedFlowImpl.AnonymousClass1> continuation) {
            super(continuation);
            this.this$0 = sharedFlowImpl;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return kotlinx.coroutines.flow.SharedFlowImpl.collect$suspendImpl(this.this$0, null, this);
        }
    }

    protected static /* synthetic */ void getLastReplayedLocked$annotations() {
    }

    @Override // kotlinx.coroutines.flow.SharedFlow, kotlinx.coroutines.flow.Flow
    public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<?> continuation) {
        return collect$suspendImpl(this, flowCollector, continuation);
    }

    @Override // kotlinx.coroutines.flow.MutableSharedFlow, kotlinx.coroutines.flow.FlowCollector
    public java.lang.Object emit(T t, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return emit$suspendImpl(this, t, continuation);
    }

    public SharedFlowImpl(int replay, int bufferCapacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        this.replay = replay;
        this.bufferCapacity = bufferCapacity;
        this.onBufferOverflow = onBufferOverflow;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final long getHead() {
        return java.lang.Math.min(this.minCollectorIndex, this.replayIndex);
    }

    private final int getReplaySize() {
        return (int) ((getHead() + ((long) this.bufferSize)) - this.replayIndex);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int getTotalSize() {
        return this.bufferSize + this.queueSize;
    }

    private final long getBufferEndIndex() {
        return getHead() + ((long) this.bufferSize);
    }

    private final long getQueueEndIndex() {
        return getHead() + ((long) this.bufferSize) + ((long) this.queueSize);
    }

    @Override // kotlinx.coroutines.flow.SharedFlow
    public java.util.List<T> getReplayCache() {
        synchronized (this) {
            int replaySize = getReplaySize();
            if (replaySize == 0) {
                return kotlin.collections.CollectionsKt.emptyList();
            }
            java.util.ArrayList result = new java.util.ArrayList(replaySize);
            java.lang.Object[] buffer = this.buffer;
            kotlin.jvm.internal.Intrinsics.checkNotNull(buffer);
            for (int i = 0; i < replaySize; i++) {
                result.add(kotlinx.coroutines.flow.SharedFlowKt.getBufferAt(buffer, this.replayIndex + ((long) i)));
            }
            return result;
        }
    }

    protected final T getLastReplayedLocked() {
        java.lang.Object[] objArr = this.buffer;
        kotlin.jvm.internal.Intrinsics.checkNotNull(objArr);
        return (T) kotlinx.coroutines.flow.SharedFlowKt.getBufferAt(objArr, (this.replayIndex + ((long) getReplaySize())) - 1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00ab A[Catch: all -> 0x00d9, TryCatch #0 {all -> 0x00d9, blocks: (B:13:0x003d, B:30:0x00a2, B:32:0x00ab, B:37:0x00c0, B:38:0x00c3, B:16:0x0055, B:19:0x0067, B:28:0x008f, B:22:0x0077, B:24:0x007b), top: B:46:0x0022 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00be A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r2v12, types: [kotlinx.coroutines.flow.FlowCollector] */
    /* JADX WARN: Type inference failed for: r2v13, types: [java.lang.Object, kotlinx.coroutines.flow.FlowCollector] */
    /* JADX WARN: Type inference failed for: r2v16 */
    /* JADX WARN: Type inference failed for: r6v0 */
    /* JADX WARN: Type inference failed for: r7v10 */
    /* JADX WARN: Type inference failed for: r7v11 */
    /* JADX WARN: Type inference failed for: r7v15 */
    /* JADX WARN: Type inference failed for: r7v17 */
    /* JADX WARN: Type inference failed for: r7v18 */
    /* JADX WARN: Type inference failed for: r7v19 */
    /* JADX WARN: Type inference failed for: r7v2, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r7v20 */
    /* JADX WARN: Type inference failed for: r7v3 */
    /* JADX WARN: Type inference failed for: r8v0, types: [kotlinx.coroutines.flow.FlowCollector<? super T>] */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v10 */
    /* JADX WARN: Type inference failed for: r8v18 */
    /* JADX WARN: Type inference failed for: r8v19 */
    /* JADX WARN: Type inference failed for: r8v20 */
    /* JADX WARN: Type inference failed for: r8v8, types: [kotlinx.coroutines.flow.SharedFlowSlot] */
    /* JADX WARN: Type inference failed for: r8v9, types: [java.lang.Object, kotlinx.coroutines.flow.SharedFlowSlot] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:41:0x00d5 -> B:29:0x009e). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static /* synthetic */ <T> java.lang.Object collect$suspendImpl(kotlinx.coroutines.flow.SharedFlowImpl<T> r7, kotlinx.coroutines.flow.FlowCollector<? super T> r8, kotlin.coroutines.Continuation<?> r9) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 238
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.SharedFlowImpl.collect$suspendImpl(kotlinx.coroutines.flow.SharedFlowImpl, kotlinx.coroutines.flow.FlowCollector, kotlin.coroutines.Continuation):java.lang.Object");
    }

    @Override // kotlinx.coroutines.flow.MutableSharedFlow
    public boolean tryEmit(T value) {
        int i;
        boolean z;
        kotlin.coroutines.Continuation<kotlin.Unit>[] continuationArrFindSlotsToResumeLocked = kotlinx.coroutines.flow.internal.AbstractSharedFlowKt.EMPTY_RESUMES;
        synchronized (this) {
            if (tryEmitLocked(value)) {
                continuationArrFindSlotsToResumeLocked = findSlotsToResumeLocked(continuationArrFindSlotsToResumeLocked);
                z = true;
            } else {
                z = false;
            }
        }
        boolean emitted = z;
        for (kotlin.coroutines.Continuation<kotlin.Unit> continuation : continuationArrFindSlotsToResumeLocked) {
            if (continuation != null) {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                continuation.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.Unit.INSTANCE));
            }
        }
        return emitted;
    }

    static /* synthetic */ <T> java.lang.Object emit$suspendImpl(kotlinx.coroutines.flow.SharedFlowImpl<T> sharedFlowImpl, T t, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        java.lang.Object objEmitSuspend;
        return (!sharedFlowImpl.tryEmit(t) && (objEmitSuspend = sharedFlowImpl.emitSuspend(t, continuation)) == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) ? objEmitSuspend : kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean tryEmitLocked(T value) {
        if (getNCollectors() == 0) {
            return tryEmitNoCollectorsLocked(value);
        }
        if (this.bufferSize >= this.bufferCapacity && this.minCollectorIndex <= this.replayIndex) {
            switch (kotlinx.coroutines.flow.SharedFlowImpl.WhenMappings.$EnumSwitchMapping$0[this.onBufferOverflow.ordinal()]) {
                case 1:
                    return false;
                case 2:
                    return true;
            }
        }
        enqueueLocked(value);
        this.bufferSize++;
        if (this.bufferSize > this.bufferCapacity) {
            dropOldestLocked();
        }
        if (getReplaySize() > this.replay) {
            updateBufferLocked(this.replayIndex + 1, this.minCollectorIndex, getBufferEndIndex(), getQueueEndIndex());
        }
        return true;
    }

    private final boolean tryEmitNoCollectorsLocked(T value) {
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(getNCollectors() == 0)) {
                throw new java.lang.AssertionError();
            }
        }
        if (this.replay == 0) {
            return true;
        }
        enqueueLocked(value);
        this.bufferSize++;
        if (this.bufferSize > this.replay) {
            dropOldestLocked();
        }
        this.minCollectorIndex = getHead() + ((long) this.bufferSize);
        return true;
    }

    private final void dropOldestLocked() {
        java.lang.Object[] objArr = this.buffer;
        kotlin.jvm.internal.Intrinsics.checkNotNull(objArr);
        kotlinx.coroutines.flow.SharedFlowKt.setBufferAt(objArr, getHead(), null);
        this.bufferSize--;
        long newHead = getHead() + 1;
        if (this.replayIndex < newHead) {
            this.replayIndex = newHead;
        }
        if (this.minCollectorIndex < newHead) {
            correctCollectorIndexesOnDropOldest(newHead);
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(getHead() == newHead)) {
                throw new java.lang.AssertionError();
            }
        }
    }

    private final void correctCollectorIndexesOnDropOldest(long newHead) {
        java.lang.Object[] $this$forEach$iv$iv;
        kotlinx.coroutines.flow.SharedFlowImpl<T> this_$iv = this;
        if (((kotlinx.coroutines.flow.internal.AbstractSharedFlow) this_$iv).nCollectors != 0 && ($this$forEach$iv$iv = ((kotlinx.coroutines.flow.internal.AbstractSharedFlow) this_$iv).slots) != null) {
            for (java.lang.Object element$iv$iv : $this$forEach$iv$iv) {
                if (element$iv$iv != null) {
                    kotlinx.coroutines.flow.SharedFlowSlot slot = (kotlinx.coroutines.flow.SharedFlowSlot) element$iv$iv;
                    if (slot.index >= 0 && slot.index < newHead) {
                        slot.index = newHead;
                    }
                }
            }
        }
        this.minCollectorIndex = newHead;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void enqueueLocked(java.lang.Object item) {
        int curSize = getTotalSize();
        java.lang.Object[] curBuffer = this.buffer;
        if (curBuffer == null) {
            curBuffer = growBuffer(null, 0, 2);
        } else if (curSize >= curBuffer.length) {
            curBuffer = growBuffer(curBuffer, curSize, curBuffer.length * 2);
        }
        kotlinx.coroutines.flow.SharedFlowKt.setBufferAt(curBuffer, getHead() + ((long) curSize), item);
    }

    private final java.lang.Object[] growBuffer(java.lang.Object[] curBuffer, int curSize, int newSize) {
        if (!(newSize > 0)) {
            throw new java.lang.IllegalStateException("Buffer size overflow".toString());
        }
        java.lang.Object[] newBuffer = new java.lang.Object[newSize];
        this.buffer = newBuffer;
        if (curBuffer == null) {
            return newBuffer;
        }
        long head = getHead();
        for (int i = 0; i < curSize; i++) {
            kotlinx.coroutines.flow.SharedFlowKt.setBufferAt(newBuffer, ((long) i) + head, kotlinx.coroutines.flow.SharedFlowKt.getBufferAt(curBuffer, ((long) i) + head));
        }
        return newBuffer;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object emitSuspend(T t, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlin.coroutines.Continuation<kotlin.Unit>[] continuationArrFindSlotsToResumeLocked;
        kotlinx.coroutines.flow.SharedFlowImpl.Emitter emitter;
        kotlinx.coroutines.CancellableContinuationImpl cancellable$iv = new kotlinx.coroutines.CancellableContinuationImpl(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation), 1);
        cancellable$iv.initCancellability();
        kotlinx.coroutines.CancellableContinuationImpl cont = cancellable$iv;
        kotlin.coroutines.Continuation<kotlin.Unit>[] continuationArrFindSlotsToResumeLocked2 = kotlinx.coroutines.flow.internal.AbstractSharedFlowKt.EMPTY_RESUMES;
        synchronized (this) {
            if (tryEmitLocked(t)) {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                cont.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.Unit.INSTANCE));
                continuationArrFindSlotsToResumeLocked = findSlotsToResumeLocked(continuationArrFindSlotsToResumeLocked2);
                emitter = null;
            } else {
                kotlinx.coroutines.flow.SharedFlowImpl.Emitter it = new kotlinx.coroutines.flow.SharedFlowImpl.Emitter(this, ((long) getTotalSize()) + getHead(), t, cont);
                enqueueLocked(it);
                this.queueSize++;
                if (this.bufferCapacity == 0) {
                    continuationArrFindSlotsToResumeLocked2 = findSlotsToResumeLocked(continuationArrFindSlotsToResumeLocked2);
                }
                continuationArrFindSlotsToResumeLocked = continuationArrFindSlotsToResumeLocked2;
                emitter = it;
            }
        }
        kotlinx.coroutines.flow.SharedFlowImpl.Emitter emitter2 = emitter;
        if (emitter2 != null) {
            kotlinx.coroutines.CancellableContinuationKt.disposeOnCancellation(cont, emitter2);
        }
        for (kotlin.coroutines.Continuation<kotlin.Unit> continuation2 : continuationArrFindSlotsToResumeLocked) {
            if (continuation2 != null) {
                kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
                continuation2.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.Unit.INSTANCE));
            }
        }
        java.lang.Object result = cancellable$iv.getResult();
        if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? result : kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void cancelEmitter(kotlinx.coroutines.flow.SharedFlowImpl.Emitter emitter) {
        synchronized (this) {
            if (emitter.index < getHead()) {
                return;
            }
            java.lang.Object[] buffer = this.buffer;
            kotlin.jvm.internal.Intrinsics.checkNotNull(buffer);
            if (kotlinx.coroutines.flow.SharedFlowKt.getBufferAt(buffer, emitter.index) != emitter) {
                return;
            }
            kotlinx.coroutines.flow.SharedFlowKt.setBufferAt(buffer, emitter.index, kotlinx.coroutines.flow.SharedFlowKt.NO_VALUE);
            cleanupTailLocked();
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
        }
    }

    public final long updateNewCollectorIndexLocked$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        long index = this.replayIndex;
        if (index < this.minCollectorIndex) {
            this.minCollectorIndex = index;
        }
        return index;
    }

    /* JADX WARN: Removed duplicated region for block: B:84:0x014b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final kotlin.coroutines.Continuation<kotlin.Unit>[] updateCollectorIndexLocked$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(long r27) {
        /*
            Method dump skipped, instruction units count: 366
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.SharedFlowImpl.updateCollectorIndexLocked$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(long):kotlin.coroutines.Continuation[]");
    }

    private final void updateBufferLocked(long newReplayIndex, long newMinCollectorIndex, long newBufferEndIndex, long newQueueEndIndex) {
        long newHead = java.lang.Math.min(newMinCollectorIndex, newReplayIndex);
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if ((newHead >= getHead() ? 1 : 0) == 0) {
                throw new java.lang.AssertionError();
            }
        }
        for (long index = getHead(); index < newHead; index++) {
            java.lang.Object[] objArr = this.buffer;
            kotlin.jvm.internal.Intrinsics.checkNotNull(objArr);
            kotlinx.coroutines.flow.SharedFlowKt.setBufferAt(objArr, index, null);
        }
        this.replayIndex = newReplayIndex;
        this.minCollectorIndex = newMinCollectorIndex;
        this.bufferSize = (int) (newBufferEndIndex - newHead);
        this.queueSize = (int) (newQueueEndIndex - newBufferEndIndex);
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if ((this.bufferSize >= 0 ? 1 : 0) == 0) {
                throw new java.lang.AssertionError();
            }
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if ((this.queueSize >= 0 ? 1 : 0) == 0) {
                throw new java.lang.AssertionError();
            }
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(this.replayIndex <= getHead() + ((long) this.bufferSize))) {
                throw new java.lang.AssertionError();
            }
        }
    }

    private final void cleanupTailLocked() {
        if (this.bufferCapacity != 0 || this.queueSize > 1) {
            java.lang.Object[] buffer = this.buffer;
            kotlin.jvm.internal.Intrinsics.checkNotNull(buffer);
            while (this.queueSize > 0 && kotlinx.coroutines.flow.SharedFlowKt.getBufferAt(buffer, (getHead() + ((long) getTotalSize())) - 1) == kotlinx.coroutines.flow.SharedFlowKt.NO_VALUE) {
                this.queueSize--;
                kotlinx.coroutines.flow.SharedFlowKt.setBufferAt(buffer, getHead() + ((long) getTotalSize()), null);
            }
        }
    }

    private final java.lang.Object tryTakeValue(kotlinx.coroutines.flow.SharedFlowSlot slot) {
        java.lang.Object obj;
        kotlin.coroutines.Continuation<kotlin.Unit>[] continuationArrUpdateCollectorIndexLocked$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = kotlinx.coroutines.flow.internal.AbstractSharedFlowKt.EMPTY_RESUMES;
        synchronized (this) {
            long index = tryPeekLocked(slot);
            if (index < 0) {
                obj = kotlinx.coroutines.flow.SharedFlowKt.NO_VALUE;
            } else {
                long oldIndex = slot.index;
                java.lang.Object newValue = getPeekedValueLockedAt(index);
                slot.index = 1 + index;
                continuationArrUpdateCollectorIndexLocked$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = updateCollectorIndexLocked$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(oldIndex);
                obj = newValue;
            }
        }
        java.lang.Object value = obj;
        for (kotlin.coroutines.Continuation<kotlin.Unit> continuation : continuationArrUpdateCollectorIndexLocked$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host) {
            if (continuation != null) {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                continuation.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.Unit.INSTANCE));
            }
        }
        return value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final long tryPeekLocked(kotlinx.coroutines.flow.SharedFlowSlot slot) {
        long index = slot.index;
        if (index < getBufferEndIndex()) {
            return index;
        }
        if (this.bufferCapacity <= 0 && index <= getHead() && this.queueSize != 0) {
            return index;
        }
        return -1L;
    }

    private final java.lang.Object getPeekedValueLockedAt(long index) {
        java.lang.Object[] objArr = this.buffer;
        kotlin.jvm.internal.Intrinsics.checkNotNull(objArr);
        java.lang.Object item = kotlinx.coroutines.flow.SharedFlowKt.getBufferAt(objArr, index);
        return item instanceof kotlinx.coroutines.flow.SharedFlowImpl.Emitter ? ((kotlinx.coroutines.flow.SharedFlowImpl.Emitter) item).value : item;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object awaitValue(kotlinx.coroutines.flow.SharedFlowSlot slot, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlinx.coroutines.CancellableContinuationImpl cancellable$iv = new kotlinx.coroutines.CancellableContinuationImpl(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation), 1);
        cancellable$iv.initCancellability();
        kotlinx.coroutines.CancellableContinuationImpl cont = cancellable$iv;
        synchronized (this) {
            long index = tryPeekLocked(slot);
            if (index < 0) {
                slot.cont = cont;
                slot.cont = cont;
            } else {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                cont.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.Unit.INSTANCE));
            }
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
        }
        java.lang.Object result = cancellable$iv.getResult();
        if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? result : kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final kotlin.coroutines.Continuation<kotlin.Unit>[] findSlotsToResumeLocked(kotlin.coroutines.Continuation<kotlin.Unit>[] resumesIn) {
        java.lang.Object[] $this$forEach$iv$iv;
        kotlinx.coroutines.flow.SharedFlowSlot slot;
        kotlin.coroutines.Continuation<? super kotlin.Unit> continuation;
        kotlinx.coroutines.flow.SharedFlowImpl<T> sharedFlowImpl = this;
        java.lang.Object[] objArr = resumesIn;
        int resumeCount = resumesIn.length;
        kotlinx.coroutines.flow.SharedFlowImpl<T> this_$iv = sharedFlowImpl;
        if (((kotlinx.coroutines.flow.internal.AbstractSharedFlow) this_$iv).nCollectors != 0 && ($this$forEach$iv$iv = ((kotlinx.coroutines.flow.internal.AbstractSharedFlow) this_$iv).slots) != null) {
            int length = $this$forEach$iv$iv.length;
            int i = 0;
            while (i < length) {
                java.lang.Object element$iv$iv = $this$forEach$iv$iv[i];
                if (element$iv$iv != null && (continuation = (slot = (kotlinx.coroutines.flow.SharedFlowSlot) element$iv$iv).cont) != null && sharedFlowImpl.tryPeekLocked(slot) >= 0) {
                    if (resumeCount >= objArr.length) {
                        java.lang.Object[] objArrCopyOf = java.util.Arrays.copyOf(objArr, java.lang.Math.max(2, objArr.length * 2));
                        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(objArrCopyOf, "copyOf(...)");
                        objArr = objArrCopyOf;
                    }
                    ((kotlin.coroutines.Continuation[]) objArr)[resumeCount] = continuation;
                    slot.cont = null;
                    resumeCount++;
                }
                i++;
                sharedFlowImpl = this;
            }
        }
        return (kotlin.coroutines.Continuation[]) objArr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlow
    public kotlinx.coroutines.flow.SharedFlowSlot createSlot() {
        return new kotlinx.coroutines.flow.SharedFlowSlot();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlow
    public kotlinx.coroutines.flow.SharedFlowSlot[] createSlotArray(int size) {
        return new kotlinx.coroutines.flow.SharedFlowSlot[size];
    }

    @Override // kotlinx.coroutines.flow.MutableSharedFlow
    public void resetReplayCache() {
        synchronized (this) {
            updateBufferLocked(getBufferEndIndex(), this.minCollectorIndex, getBufferEndIndex(), getQueueEndIndex());
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
        }
    }

    @Override // kotlinx.coroutines.flow.internal.FusibleFlow
    public kotlinx.coroutines.flow.Flow<T> fuse(kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        return kotlinx.coroutines.flow.SharedFlowKt.fuseSharedFlow(this, context, capacity, onBufferOverflow);
    }

    /* JADX INFO: compiled from: SharedFlow.kt */
    @kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B1\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t¢\u0006\u0002\u0010\u000bJ\b\u0010\f\u001a\u00020\nH\u0016R\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t8\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/flow/SharedFlowImpl$Emitter;", "Lkotlinx/coroutines/DisposableHandle;", "flow", "Lkotlinx/coroutines/flow/SharedFlowImpl;", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "value", "", "cont", "Lkotlin/coroutines/Continuation;", "", "(Lkotlinx/coroutines/flow/SharedFlowImpl;JLjava/lang/Object;Lkotlin/coroutines/Continuation;)V", "dispose", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Emitter implements kotlinx.coroutines.DisposableHandle {
        public final kotlin.coroutines.Continuation<kotlin.Unit> cont;
        public final kotlinx.coroutines.flow.SharedFlowImpl<?> flow;
        public long index;
        public final java.lang.Object value;

        /* JADX WARN: Multi-variable type inference failed */
        public Emitter(kotlinx.coroutines.flow.SharedFlowImpl<?> flow, long index, java.lang.Object value, kotlin.coroutines.Continuation<? super kotlin.Unit> cont) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cont, "cont");
            this.flow = flow;
            this.index = index;
            this.value = value;
            this.cont = cont;
        }

        @Override // kotlinx.coroutines.DisposableHandle
        public void dispose() {
            this.flow.cancelEmitter(this);
        }
    }
}
