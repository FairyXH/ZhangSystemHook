package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: Broadcast.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0012\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00020\u00030\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00042\b\u0012\u0004\u0012\u0002H\u00010\u0005B#\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\u0012\u0010\u0019\u001a\u00020\n2\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0007J\u0016\u0010\u0019\u001a\u00020\u00032\u000e\u0010\u001a\u001a\n\u0018\u00010\u001cj\u0004\u0018\u0001`\u001dJ\u0010\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\u0012\u0010\u001f\u001a\u00020\n2\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J.\u0010 \u001a\u00020\u00032#\u0010!\u001a\u001f\u0012\u0015\u0012\u0013\u0018\u00010\u001b¢\u0006\f\b#\u0012\b\b$\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u00030\"H\u0096\u0001J\u0016\u0010%\u001a\u00020\n2\u0006\u0010&\u001a\u00028\u0000H\u0097\u0001¢\u0006\u0002\u0010'J\u0018\u0010(\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010)\u001a\u00020\nH\u0014J\u0015\u0010*\u001a\u00020\u00032\u0006\u0010+\u001a\u00020\u0003H\u0014¢\u0006\u0002\u0010,J\u000f\u0010-\u001a\b\u0012\u0004\u0012\u00028\u00000.H\u0096\u0001J\u0016\u0010/\u001a\u00020\u00032\u0006\u0010&\u001a\u00028\u0000H\u0096A¢\u0006\u0002\u00100J$\u00101\u001a\b\u0012\u0004\u0012\u00020\u0003022\u0006\u0010&\u001a\u00028\u0000H\u0096\u0001ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b3\u00104R\u001a\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005X\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u000f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\n8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0014\u0010\u0014\u001a\u00020\n8\u0016X\u0097\u0005¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0013R$\u0010\u0015\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000f0\u0016X\u0096\u0005¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018\u0082\u0002\u000b\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u00065"}, d2 = {"Lkotlinx/coroutines/channels/BroadcastCoroutine;", "E", "Lkotlinx/coroutines/AbstractCoroutine;", "", "Lkotlinx/coroutines/channels/ProducerScope;", "Lkotlinx/coroutines/channels/BroadcastChannel;", "parentContext", "Lkotlin/coroutines/CoroutineContext;", "_channel", com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE, "", "(Lkotlin/coroutines/CoroutineContext;Lkotlinx/coroutines/channels/BroadcastChannel;Z)V", "get_channel", "()Lkotlinx/coroutines/channels/BroadcastChannel;", "channel", "Lkotlinx/coroutines/channels/SendChannel;", "getChannel", "()Lkotlinx/coroutines/channels/SendChannel;", "isActive", "()Z", "isClosedForSend", "onSend", "Lkotlinx/coroutines/selects/SelectClause2;", "getOnSend", "()Lkotlinx/coroutines/selects/SelectClause2;", "cancel", "cause", "", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "cancelInternal", "close", "invokeOnClose", "handler", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "offer", "element", "(Ljava/lang/Object;)Z", "onCancelled", "handled", "onCompleted", "value", "(Lkotlin/Unit;)V", "openSubscription", "Lkotlinx/coroutines/channels/ReceiveChannel;", "send", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "trySend", "Lkotlinx/coroutines/channels/ChannelResult;", "trySend-JP2dKIU", "(Ljava/lang/Object;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
class BroadcastCoroutine<E> extends kotlinx.coroutines.AbstractCoroutine<kotlin.Unit> implements kotlinx.coroutines.channels.ProducerScope<E>, kotlinx.coroutines.channels.BroadcastChannel<E> {
    private final kotlinx.coroutines.channels.BroadcastChannel<E> _channel;

    @Override // kotlinx.coroutines.channels.SendChannel
    public kotlinx.coroutines.selects.SelectClause2<E, kotlinx.coroutines.channels.SendChannel<E>> getOnSend() {
        return this._channel.getOnSend();
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public void invokeOnClose(kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> handler) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(handler, "handler");
        this._channel.invokeOnClose(handler);
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public boolean isClosedForSend() {
        return this._channel.isClosedForSend();
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Deprecated in the favour of 'trySend' method", replaceWith = @kotlin.ReplaceWith(expression = "trySend(element).isSuccess", imports = {}))
    public boolean offer(E element) {
        return this._channel.offer(element);
    }

    @Override // kotlinx.coroutines.channels.BroadcastChannel
    public kotlinx.coroutines.channels.ReceiveChannel<E> openSubscription() {
        return this._channel.openSubscription();
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public java.lang.Object send(E e, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return this._channel.send(e, continuation);
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    /* JADX INFO: renamed from: trySend-JP2dKIU */
    public java.lang.Object mo12809trySendJP2dKIU(E element) {
        return this._channel.mo12809trySendJP2dKIU(element);
    }

    protected final kotlinx.coroutines.channels.BroadcastChannel<E> get_channel() {
        return this._channel;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BroadcastCoroutine(kotlin.coroutines.CoroutineContext parentContext, kotlinx.coroutines.channels.BroadcastChannel<E> _channel, boolean active) {
        super(parentContext, false, active);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(parentContext, "parentContext");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(_channel, "_channel");
        this._channel = _channel;
        initParentJob((kotlinx.coroutines.Job) parentContext.get(kotlinx.coroutines.Job.INSTANCE));
    }

    @Override // kotlinx.coroutines.AbstractCoroutine, kotlinx.coroutines.JobSupport, kotlinx.coroutines.Job
    public boolean isActive() {
        return super.isActive();
    }

    @Override // kotlinx.coroutines.channels.ProducerScope
    public kotlinx.coroutines.channels.SendChannel<E> getChannel() {
        return this;
    }

    @Override // kotlinx.coroutines.JobSupport, kotlinx.coroutines.Job
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    public final /* synthetic */ boolean cancel(java.lang.Throwable cause) {
        kotlinx.coroutines.JobCancellationException jobCancellationException;
        if (cause != null) {
            jobCancellationException = cause;
        } else {
            jobCancellationException = new kotlinx.coroutines.JobCancellationException(cancellationExceptionMessage(), null, this);
        }
        cancelInternal(jobCancellationException);
        return true;
    }

    @Override // kotlinx.coroutines.JobSupport, kotlinx.coroutines.Job
    public final void cancel(java.util.concurrent.CancellationException cause) {
        kotlinx.coroutines.JobCancellationException jobCancellationException;
        if (cause != null) {
            jobCancellationException = cause;
        } else {
            jobCancellationException = new kotlinx.coroutines.JobCancellationException(cancellationExceptionMessage(), null, this);
        }
        cancelInternal(jobCancellationException);
    }

    @Override // kotlinx.coroutines.JobSupport
    public void cancelInternal(java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
        java.util.concurrent.CancellationException exception = kotlinx.coroutines.JobSupport.toCancellationException$default(this, cause, null, 1, null);
        this._channel.cancel(exception);
        cancelCoroutine(exception);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.AbstractCoroutine
    public void onCompleted(kotlin.Unit value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        kotlinx.coroutines.channels.SendChannel.DefaultImpls.close$default(this._channel, null, 1, null);
    }

    @Override // kotlinx.coroutines.AbstractCoroutine
    protected void onCancelled(java.lang.Throwable cause, boolean handled) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
        boolean processed = this._channel.close(cause);
        if (processed || handled) {
            return;
        }
        kotlinx.coroutines.CoroutineExceptionHandlerKt.handleCoroutineException(get$context(), cause);
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public boolean close(java.lang.Throwable cause) {
        boolean result = this._channel.close(cause);
        start();
        return result;
    }
}
