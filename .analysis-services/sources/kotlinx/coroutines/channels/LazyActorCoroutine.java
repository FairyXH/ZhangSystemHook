package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: Actor.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002BJ\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006\u0012-\u0010\u0007\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\b¢\u0006\u0002\b\r¢\u0006\u0002\u0010\u000eJ\u0012\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\u0015\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u001c\u001a\u00028\u0000H\u0017¢\u0006\u0002\u0010\u001dJ\u001e\u0010\u001e\u001a\u00020\u000b2\n\u0010\u001f\u001a\u0006\u0012\u0002\b\u00030 2\b\u0010\u001c\u001a\u0004\u0018\u00010\fH\u0002J\b\u0010!\u001a\u00020\u000bH\u0014J\u0016\u0010\"\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010#J#\u0010$\u001a\b\u0012\u0004\u0012\u00020\u000b0%2\u0006\u0010\u001c\u001a\u00028\u0000H\u0016ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b&\u0010'R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u000e¢\u0006\u0002\n\u0000R,\u0010\u0010\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00120\u00118VX\u0096\u0004¢\u0006\f\u0012\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0015\u0010\u0016\u0082\u0002\u000b\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u0006("}, d2 = {"Lkotlinx/coroutines/channels/LazyActorCoroutine;", "E", "Lkotlinx/coroutines/channels/ActorCoroutine;", "parentContext", "Lkotlin/coroutines/CoroutineContext;", "channel", "Lkotlinx/coroutines/channels/Channel;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/channels/ActorScope;", "Lkotlin/coroutines/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/coroutines/CoroutineContext;Lkotlinx/coroutines/channels/Channel;Lkotlin/jvm/functions/Function2;)V", "continuation", "onSend", "Lkotlinx/coroutines/selects/SelectClause2;", "Lkotlinx/coroutines/channels/SendChannel;", "getOnSend$annotations", "()V", "getOnSend", "()Lkotlinx/coroutines/selects/SelectClause2;", "close", "", "cause", "", "offer", "element", "(Ljava/lang/Object;)Z", "onSendRegFunction", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "onStart", "send", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "trySend", "Lkotlinx/coroutines/channels/ChannelResult;", "trySend-JP2dKIU", "(Ljava/lang/Object;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class LazyActorCoroutine<E> extends kotlinx.coroutines.channels.ActorCoroutine<E> {
    private kotlin.coroutines.Continuation<? super kotlin.Unit> continuation;

    public static /* synthetic */ void getOnSend$annotations() {
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LazyActorCoroutine(kotlin.coroutines.CoroutineContext parentContext, kotlinx.coroutines.channels.Channel<E> channel, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.channels.ActorScope<E>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> block) {
        super(parentContext, channel, false);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(parentContext, "parentContext");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(channel, "channel");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        this.continuation = kotlin.coroutines.intrinsics.IntrinsicsKt.createCoroutineUnintercepted(block, this, this);
    }

    @Override // kotlinx.coroutines.JobSupport
    protected void onStart() throws java.lang.Throwable {
        kotlinx.coroutines.intrinsics.CancellableKt.startCoroutineCancellable(this.continuation, this);
    }

    @Override // kotlinx.coroutines.channels.ChannelCoroutine, kotlinx.coroutines.channels.SendChannel
    public java.lang.Object send(E e, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        start();
        java.lang.Object objSend = super.send(e, continuation);
        return objSend == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objSend : kotlin.Unit.INSTANCE;
    }

    @Override // kotlinx.coroutines.channels.ChannelCoroutine, kotlinx.coroutines.channels.SendChannel
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Deprecated in the favour of 'trySend' method", replaceWith = @kotlin.ReplaceWith(expression = "trySend(element).isSuccess", imports = {}))
    public boolean offer(E element) {
        start();
        return super.offer(element);
    }

    @Override // kotlinx.coroutines.channels.ChannelCoroutine, kotlinx.coroutines.channels.SendChannel
    /* JADX INFO: renamed from: trySend-JP2dKIU */
    public java.lang.Object mo12809trySendJP2dKIU(E element) {
        start();
        return super.mo12809trySendJP2dKIU(element);
    }

    @Override // kotlinx.coroutines.channels.ChannelCoroutine, kotlinx.coroutines.channels.SendChannel
    public boolean close(java.lang.Throwable cause) {
        boolean closed = super.close(cause);
        start();
        return closed;
    }

    @Override // kotlinx.coroutines.channels.ChannelCoroutine, kotlinx.coroutines.channels.SendChannel
    public kotlinx.coroutines.selects.SelectClause2<E, kotlinx.coroutines.channels.SendChannel<E>> getOnSend() {
        kotlinx.coroutines.channels.LazyActorCoroutine$onSend$1 lazyActorCoroutine$onSend$1 = kotlinx.coroutines.channels.LazyActorCoroutine$onSend$1.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(lazyActorCoroutine$onSend$1, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'select')] kotlinx.coroutines.selects.SelectInstance<*>, @[ParameterName(name = 'param')] kotlin.Any?, kotlin.Unit>{ kotlinx.coroutines.selects.SelectKt.RegistrationFunction }");
        return new kotlinx.coroutines.selects.SelectClause2Impl(this, (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(lazyActorCoroutine$onSend$1, 3), super.getOnSend().getProcessResFunc(), null, 8, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onSendRegFunction(kotlinx.coroutines.selects.SelectInstance<?> select, java.lang.Object element) throws java.lang.Throwable {
        onStart();
        super.getOnSend().getRegFunc().invoke(this, select, element);
    }
}
