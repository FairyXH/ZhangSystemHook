package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: ConflatedBufferedChannel.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B9\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\"\b\u0002\u0010\u0007\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\t\u0018\u00010\bj\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\n¢\u0006\u0002\u0010\u000bJ\u001e\u0010\u000f\u001a\u00020\t2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0014J\u0016\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010\u0015J\u0018\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00028\u0000H\u0090@¢\u0006\u0004\b\u0017\u0010\u0015J\r\u0010\u0018\u001a\u00020\rH\u0010¢\u0006\u0002\b\u0019J#\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\t0\u001b2\u0006\u0010\u0012\u001a\u00028\u0000H\u0016ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\u001c\u0010\u001dJ+\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\t0\u001b2\u0006\u0010\u0012\u001a\u00028\u00002\u0006\u0010\u001f\u001a\u00020\rH\u0002ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b \u0010!J#\u0010\"\u001a\b\u0012\u0004\u0012\u00020\t0\u001b2\u0006\u0010\u0012\u001a\u00028\u0000H\u0002ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b#\u0010\u001dJ+\u0010$\u001a\b\u0012\u0004\u0012\u00020\t0\u001b2\u0006\u0010\u0012\u001a\u00028\u00002\u0006\u0010\u001f\u001a\u00020\rH\u0002ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b%\u0010!R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\r8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\u000eR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u0006&"}, d2 = {"Lkotlinx/coroutines/channels/ConflatedBufferedChannel;", "E", "Lkotlinx/coroutines/channels/BufferedChannel;", "capacity", "", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "onUndeliveredElement", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "(ILkotlinx/coroutines/channels/BufferOverflow;Lkotlin/jvm/functions/Function1;)V", "isConflatedDropOldest", "", "()Z", "registerSelectForSend", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "element", "", "send", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendBroadcast", "sendBroadcast$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "shouldSendSuspend", "shouldSendSuspend$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "trySend", "Lkotlinx/coroutines/channels/ChannelResult;", "trySend-JP2dKIU", "(Ljava/lang/Object;)Ljava/lang/Object;", "trySendDropLatest", "isSendOp", "trySendDropLatest-Mj0NB7M", "(Ljava/lang/Object;Z)Ljava/lang/Object;", "trySendDropOldest", "trySendDropOldest-JP2dKIU", "trySendImpl", "trySendImpl-Mj0NB7M", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class ConflatedBufferedChannel<E> extends kotlinx.coroutines.channels.BufferedChannel<E> {
    private final int capacity;
    private final kotlinx.coroutines.channels.BufferOverflow onBufferOverflow;

    @Override // kotlinx.coroutines.channels.BufferedChannel, kotlinx.coroutines.channels.SendChannel
    public java.lang.Object send(E e, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return send$suspendImpl((kotlinx.coroutines.channels.ConflatedBufferedChannel) this, (java.lang.Object) e, continuation);
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel
    public java.lang.Object sendBroadcast$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(E e, kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
        return sendBroadcast$suspendImpl((kotlinx.coroutines.channels.ConflatedBufferedChannel) this, (java.lang.Object) e, continuation);
    }

    public /* synthetic */ ConflatedBufferedChannel(int i, kotlinx.coroutines.channels.BufferOverflow bufferOverflow, kotlin.jvm.functions.Function1 function1, int i2, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(i, bufferOverflow, (i2 & 4) != 0 ? null : function1);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ConflatedBufferedChannel(int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow, kotlin.jvm.functions.Function1<? super E, kotlin.Unit> function1) {
        super(capacity, function1);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        this.capacity = capacity;
        this.onBufferOverflow = onBufferOverflow;
        if (!(this.onBufferOverflow != kotlinx.coroutines.channels.BufferOverflow.SUSPEND)) {
            throw new java.lang.IllegalArgumentException(("This implementation does not support suspension for senders, use " + kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(kotlinx.coroutines.channels.BufferedChannel.class).getSimpleName() + " instead").toString());
        }
        if (this.capacity >= 1) {
        } else {
            throw new java.lang.IllegalArgumentException(("Buffered channel capacity must be at least 1, but " + this.capacity + " was specified").toString());
        }
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel
    protected boolean isConflatedDropOldest() {
        return this.onBufferOverflow == kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST;
    }

    static /* synthetic */ <E> java.lang.Object send$suspendImpl(kotlinx.coroutines.channels.ConflatedBufferedChannel<E> conflatedBufferedChannel, E e, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) throws java.lang.Throwable {
        kotlinx.coroutines.internal.UndeliveredElementException it;
        java.lang.Object $this$onClosed_u2dWpGqRn0$iv = conflatedBufferedChannel.m12837trySendImplMj0NB7M(e, true);
        if ($this$onClosed_u2dWpGqRn0$iv instanceof kotlinx.coroutines.channels.ChannelResult.Closed) {
            kotlinx.coroutines.channels.ChannelResult.m12823exceptionOrNullimpl($this$onClosed_u2dWpGqRn0$iv);
            kotlin.jvm.functions.Function1<E, kotlin.Unit> function1 = conflatedBufferedChannel.onUndeliveredElement;
            if (function1 != null && (it = kotlinx.coroutines.internal.OnUndeliveredElementKt.callUndeliveredElementCatchingException$default(function1, e, null, 2, null)) != null) {
                kotlin.ExceptionsKt.addSuppressed(it, conflatedBufferedChannel.getSendException());
                throw it;
            }
            throw conflatedBufferedChannel.getSendException();
        }
        return kotlin.Unit.INSTANCE;
    }

    static /* synthetic */ <E> java.lang.Object sendBroadcast$suspendImpl(kotlinx.coroutines.channels.ConflatedBufferedChannel<E> conflatedBufferedChannel, E e, kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
        java.lang.Object $this$onSuccess_u2dWpGqRn0$iv = conflatedBufferedChannel.m12837trySendImplMj0NB7M(e, true);
        if (!($this$onSuccess_u2dWpGqRn0$iv instanceof kotlinx.coroutines.channels.ChannelResult.Failed)) {
            return kotlin.coroutines.jvm.internal.Boxing.boxBoolean(true);
        }
        return kotlin.coroutines.jvm.internal.Boxing.boxBoolean(false);
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel, kotlinx.coroutines.channels.SendChannel
    /* JADX INFO: renamed from: trySend-JP2dKIU */
    public java.lang.Object mo12809trySendJP2dKIU(E element) {
        return m12837trySendImplMj0NB7M(element, false);
    }

    /* JADX INFO: renamed from: trySendImpl-Mj0NB7M, reason: not valid java name */
    private final java.lang.Object m12837trySendImplMj0NB7M(E element, boolean isSendOp) {
        return this.onBufferOverflow == kotlinx.coroutines.channels.BufferOverflow.DROP_LATEST ? m12835trySendDropLatestMj0NB7M(element, isSendOp) : m12836trySendDropOldestJP2dKIU(element);
    }

    /* JADX INFO: renamed from: trySendDropLatest-Mj0NB7M, reason: not valid java name */
    private final java.lang.Object m12835trySendDropLatestMj0NB7M(E element, boolean isSendOp) {
        kotlin.jvm.functions.Function1<E, kotlin.Unit> function1;
        kotlinx.coroutines.internal.UndeliveredElementException it;
        java.lang.Object result = super.mo12809trySendJP2dKIU(element);
        if (kotlinx.coroutines.channels.ChannelResult.m12829isSuccessimpl(result) || kotlinx.coroutines.channels.ChannelResult.m12827isClosedimpl(result)) {
            return result;
        }
        if (isSendOp && (function1 = this.onUndeliveredElement) != null && (it = kotlinx.coroutines.internal.OnUndeliveredElementKt.callUndeliveredElementCatchingException$default(function1, element, null, 2, null)) != null) {
            throw it;
        }
        return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12834successJP2dKIU(kotlin.Unit.INSTANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x009a, code lost:
    
        return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12832closedJP2dKIU(getSendException());
     */
    /* JADX INFO: renamed from: trySendDropOldest-JP2dKIU, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final java.lang.Object m12836trySendDropOldestJP2dKIU(E r21) {
        /*
            Method dump skipped, instruction units count: 262
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ConflatedBufferedChannel.m12836trySendDropOldestJP2dKIU(java.lang.Object):java.lang.Object");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.channels.BufferedChannel
    protected void registerSelectForSend(kotlinx.coroutines.selects.SelectInstance<?> select, java.lang.Object element) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(select, "select");
        java.lang.Object it = mo12809trySendJP2dKIU(element);
        if (!(it instanceof kotlinx.coroutines.channels.ChannelResult.Failed)) {
            select.selectInRegistrationPhase(kotlin.Unit.INSTANCE);
        } else {
            if (!(it instanceof kotlinx.coroutines.channels.ChannelResult.Closed)) {
                throw new java.lang.IllegalStateException(android.net.INetd.NEXTHOP_UNREACHABLE.toString());
            }
            kotlinx.coroutines.channels.ChannelResult.m12823exceptionOrNullimpl(it);
            select.selectInRegistrationPhase(kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED());
        }
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel
    public boolean shouldSendSuspend$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return false;
    }
}
