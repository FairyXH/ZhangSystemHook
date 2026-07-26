package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: BufferedChannel.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000T\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\u0018\u00106\u001a\u00020\u00032\u0006\u00107\u001a\u00020\u00032\u0006\u00108\u001a\u00020.H\u0002\u001a\u0018\u00109\u001a\u00020\u00032\u0006\u00107\u001a\u00020\u00032\u0006\u0010:\u001a\u00020\u000bH\u0002\u001a*\u0010;\u001a\b\u0012\u0004\u0012\u0002H<0\u001a\"\u0004\b\u0000\u0010<2\u0006\u0010=\u001a\u00020\u00032\f\u0010>\u001a\b\u0012\u0004\u0012\u0002H<0\u001aH\u0002\u001a,\u0010?\u001a \u0012\u0004\u0012\u00020\u0003\u0012\n\u0012\b\u0012\u0004\u0012\u0002H<0\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H<0\u001a0@\"\u0004\b\u0000\u0010<H\u0000\u001a\u0010\u0010A\u001a\u00020\u00032\u0006\u0010B\u001a\u00020\u000bH\u0002\u001aL\u0010C\u001a\u00020.\"\u0004\b\u0000\u0010D*\b\u0012\u0004\u0012\u0002HD0E2\u0006\u0010F\u001a\u0002HD2%\b\u0002\u0010G\u001a\u001f\u0012\u0013\u0012\u00110I¢\u0006\f\bJ\u0012\b\bK\u0012\u0004\b\b(L\u0012\u0004\u0012\u00020M\u0018\u00010HH\u0002¢\u0006\u0002\u0010N\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0003X\u0082T¢\u0006\u0002\n\u0000\"\u0014\u0010\u0005\u001a\u00020\u0001X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u000e\u0010\b\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\f\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\r\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u000e\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u000f\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0010\u001a\u00020\u0003X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0011\u001a\u00020\u0003X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0012\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0013\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0014\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0015\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0016\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0017\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0018\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u0016\u0010\u0019\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u001aX\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u001c\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u001d\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u001e\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u001f\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010 \u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010!\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\"\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010#\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010$\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010%\u001a\u00020\u000b8\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010&\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010'\u001a\u00020\u0003X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010(\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010)\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u0019\u0010*\u001a\u00020\u0003*\u00020\u00038Â\u0002X\u0082\u0004¢\u0006\u0006\u001a\u0004\b+\u0010,\"\u0019\u0010-\u001a\u00020.*\u00020\u00038Â\u0002X\u0082\u0004¢\u0006\u0006\u001a\u0004\b/\u00100\"\u0019\u00101\u001a\u00020\u000b*\u00020\u00038Â\u0002X\u0082\u0004¢\u0006\u0006\u001a\u0004\b2\u00103\"\u0019\u00104\u001a\u00020\u0003*\u00020\u00038Â\u0002X\u0082\u0004¢\u0006\u0006\u001a\u0004\b5\u0010,¨\u0006O"}, d2 = {"BUFFERED", "Lkotlinx/coroutines/internal/Symbol;", "BUFFER_END_RENDEZVOUS", "", "BUFFER_END_UNLIMITED", "CHANNEL_CLOSED", "getCHANNEL_CLOSED", "()Lkotlinx/coroutines/internal/Symbol;", "CLOSE_HANDLER_CLOSED", "CLOSE_HANDLER_INVOKED", "CLOSE_STATUS_ACTIVE", "", "CLOSE_STATUS_CANCELLATION_STARTED", "CLOSE_STATUS_CANCELLED", "CLOSE_STATUS_CLOSED", "DONE_RCV", "EB_COMPLETED_COUNTER_MASK", "EB_COMPLETED_PAUSE_EXPAND_BUFFERS_BIT", "EXPAND_BUFFER_COMPLETION_WAIT_ITERATIONS", "FAILED", "INTERRUPTED_RCV", "INTERRUPTED_SEND", "IN_BUFFER", "NO_CLOSE_CAUSE", "NO_RECEIVE_RESULT", "NULL_SEGMENT", "Lkotlinx/coroutines/channels/ChannelSegment;", "", "POISONED", "RESULT_BUFFERED", "RESULT_CLOSED", "RESULT_FAILED", "RESULT_RENDEZVOUS", "RESULT_SUSPEND", "RESULT_SUSPEND_NO_WAITER", "RESUMING_BY_EB", "RESUMING_BY_RCV", "SEGMENT_SIZE", "SENDERS_CLOSE_STATUS_SHIFT", "SENDERS_COUNTER_MASK", "SUSPEND", "SUSPEND_NO_WAITER", "ebCompletedCounter", "getEbCompletedCounter", "(J)J", "ebPauseExpandBuffers", "", "getEbPauseExpandBuffers", "(J)Z", "sendersCloseStatus", "getSendersCloseStatus", "(J)I", "sendersCounter", "getSendersCounter", "constructEBCompletedAndPauseFlag", "counter", "pauseEB", "constructSendersAndCloseStatus", "closeStatus", "createSegment", "E", "id", "prev", "createSegmentFunction", "Lkotlin/reflect/KFunction2;", "initialBufferEnd", "capacity", "tryResume0", "T", "Lkotlinx/coroutines/CancellableContinuation;", "value", "onCancellation", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "cause", "", "(Lkotlinx/coroutines/CancellableContinuation;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Z", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class BufferedChannelKt {
    private static final long BUFFER_END_RENDEZVOUS = 0;
    private static final long BUFFER_END_UNLIMITED = Long.MAX_VALUE;
    private static final int CLOSE_STATUS_ACTIVE = 0;
    private static final int CLOSE_STATUS_CANCELLATION_STARTED = 1;
    private static final int CLOSE_STATUS_CANCELLED = 3;
    private static final int CLOSE_STATUS_CLOSED = 2;
    private static final long EB_COMPLETED_COUNTER_MASK = 4611686018427387903L;
    private static final long EB_COMPLETED_PAUSE_EXPAND_BUFFERS_BIT = 4611686018427387904L;
    private static final int RESULT_BUFFERED = 1;
    private static final int RESULT_CLOSED = 4;
    private static final int RESULT_FAILED = 5;
    private static final int RESULT_RENDEZVOUS = 0;
    private static final int RESULT_SUSPEND = 2;
    private static final int RESULT_SUSPEND_NO_WAITER = 3;
    private static final int SENDERS_CLOSE_STATUS_SHIFT = 60;
    private static final long SENDERS_COUNTER_MASK = 1152921504606846975L;
    private static final kotlinx.coroutines.channels.ChannelSegment<java.lang.Object> NULL_SEGMENT = new kotlinx.coroutines.channels.ChannelSegment<>(-1, null, null, 0);
    public static final int SEGMENT_SIZE = kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.bufferedChannel.segmentSize", 32, 0, 0, 12, (java.lang.Object) null);
    private static final int EXPAND_BUFFER_COMPLETION_WAIT_ITERATIONS = kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.bufferedChannel.expandBufferCompletionWaitIterations", 10000, 0, 0, 12, (java.lang.Object) null);
    public static final kotlinx.coroutines.internal.Symbol BUFFERED = new kotlinx.coroutines.internal.Symbol("BUFFERED");
    private static final kotlinx.coroutines.internal.Symbol IN_BUFFER = new kotlinx.coroutines.internal.Symbol("SHOULD_BUFFER");
    private static final kotlinx.coroutines.internal.Symbol RESUMING_BY_RCV = new kotlinx.coroutines.internal.Symbol("S_RESUMING_BY_RCV");
    private static final kotlinx.coroutines.internal.Symbol RESUMING_BY_EB = new kotlinx.coroutines.internal.Symbol("RESUMING_BY_EB");
    private static final kotlinx.coroutines.internal.Symbol POISONED = new kotlinx.coroutines.internal.Symbol("POISONED");
    private static final kotlinx.coroutines.internal.Symbol DONE_RCV = new kotlinx.coroutines.internal.Symbol("DONE_RCV");
    private static final kotlinx.coroutines.internal.Symbol INTERRUPTED_SEND = new kotlinx.coroutines.internal.Symbol("INTERRUPTED_SEND");
    private static final kotlinx.coroutines.internal.Symbol INTERRUPTED_RCV = new kotlinx.coroutines.internal.Symbol("INTERRUPTED_RCV");
    private static final kotlinx.coroutines.internal.Symbol CHANNEL_CLOSED = new kotlinx.coroutines.internal.Symbol("CHANNEL_CLOSED");
    private static final kotlinx.coroutines.internal.Symbol SUSPEND = new kotlinx.coroutines.internal.Symbol("SUSPEND");
    private static final kotlinx.coroutines.internal.Symbol SUSPEND_NO_WAITER = new kotlinx.coroutines.internal.Symbol("SUSPEND_NO_WAITER");
    private static final kotlinx.coroutines.internal.Symbol FAILED = new kotlinx.coroutines.internal.Symbol("FAILED");
    private static final kotlinx.coroutines.internal.Symbol NO_RECEIVE_RESULT = new kotlinx.coroutines.internal.Symbol("NO_RECEIVE_RESULT");
    private static final kotlinx.coroutines.internal.Symbol CLOSE_HANDLER_CLOSED = new kotlinx.coroutines.internal.Symbol("CLOSE_HANDLER_CLOSED");
    private static final kotlinx.coroutines.internal.Symbol CLOSE_HANDLER_INVOKED = new kotlinx.coroutines.internal.Symbol("CLOSE_HANDLER_INVOKED");
    private static final kotlinx.coroutines.internal.Symbol NO_CLOSE_CAUSE = new kotlinx.coroutines.internal.Symbol("NO_CLOSE_CAUSE");

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* JADX INFO: renamed from: kotlinx.coroutines.channels.BufferedChannelKt$createSegmentFunction$1, reason: invalid class name */
    /* JADX INFO: compiled from: BufferedChannel.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    /* synthetic */ class AnonymousClass1<E> extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function2<java.lang.Long, kotlinx.coroutines.channels.ChannelSegment<E>, kotlinx.coroutines.channels.ChannelSegment<E>> {
        public static final kotlinx.coroutines.channels.BufferedChannelKt.AnonymousClass1 INSTANCE = new kotlinx.coroutines.channels.BufferedChannelKt.AnonymousClass1();

        AnonymousClass1() {
            super(2, kotlinx.coroutines.channels.BufferedChannelKt.class, "createSegment", "createSegment(JLkotlinx/coroutines/channels/ChannelSegment;)Lkotlinx/coroutines/channels/ChannelSegment;", 1);
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ java.lang.Object invoke(java.lang.Long l, java.lang.Object p2) {
            return invoke(l.longValue(), (kotlinx.coroutines.channels.ChannelSegment) p2);
        }

        public final kotlinx.coroutines.channels.ChannelSegment<E> invoke(long p0, kotlinx.coroutines.channels.ChannelSegment<E> p1) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p1, "p1");
            return kotlinx.coroutines.channels.BufferedChannelKt.createSegment(p0, p1);
        }
    }

    public static final <E> kotlin.reflect.KFunction<kotlinx.coroutines.channels.ChannelSegment<E>> createSegmentFunction() {
        return kotlinx.coroutines.channels.BufferedChannelKt.AnonymousClass1.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final <E> kotlinx.coroutines.channels.ChannelSegment<E> createSegment(long id, kotlinx.coroutines.channels.ChannelSegment<E> channelSegment) {
        return new kotlinx.coroutines.channels.ChannelSegment<>(id, channelSegment, channelSegment.getChannel(), 0);
    }

    static /* synthetic */ boolean tryResume0$default(kotlinx.coroutines.CancellableContinuation cancellableContinuation, java.lang.Object obj, kotlin.jvm.functions.Function1 function1, int i, java.lang.Object obj2) {
        if ((i & 2) != 0) {
            function1 = null;
        }
        return tryResume0(cancellableContinuation, obj, function1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> boolean tryResume0(kotlinx.coroutines.CancellableContinuation<? super T> cancellableContinuation, T t, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> function1) {
        java.lang.Object token = cancellableContinuation.tryResume(t, null, function1);
        if (token != null) {
            cancellableContinuation.completeResume(token);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final long initialBufferEnd(int capacity) {
        switch (capacity) {
            case 0:
                return 0L;
            case Integer.MAX_VALUE:
                return Long.MAX_VALUE;
            default:
                return capacity;
        }
    }

    public static final kotlinx.coroutines.internal.Symbol getCHANNEL_CLOSED() {
        return CHANNEL_CLOSED;
    }

    private static final long getSendersCounter(long $this$sendersCounter) {
        return SENDERS_COUNTER_MASK & $this$sendersCounter;
    }

    private static final int getSendersCloseStatus(long $this$sendersCloseStatus) {
        return (int) ($this$sendersCloseStatus >> 60);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final long constructSendersAndCloseStatus(long counter, int closeStatus) {
        return (((long) closeStatus) << 60) + counter;
    }

    private static final long getEbCompletedCounter(long $this$ebCompletedCounter) {
        return 4611686018427387903L & $this$ebCompletedCounter;
    }

    private static final boolean getEbPauseExpandBuffers(long $this$ebPauseExpandBuffers) {
        return (EB_COMPLETED_PAUSE_EXPAND_BUFFERS_BIT & $this$ebPauseExpandBuffers) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final long constructEBCompletedAndPauseFlag(long counter, boolean pauseEB) {
        return (pauseEB ? EB_COMPLETED_PAUSE_EXPAND_BUFFERS_BIT : 0L) + counter;
    }
}
