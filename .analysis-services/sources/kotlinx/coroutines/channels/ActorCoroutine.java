package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: Actor.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0012\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B#\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0010\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\rH\u0014J\u0012\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\rH\u0014¨\u0006\u0011"}, d2 = {"Lkotlinx/coroutines/channels/ActorCoroutine;", "E", "Lkotlinx/coroutines/channels/ChannelCoroutine;", "Lkotlinx/coroutines/channels/ActorScope;", "parentContext", "Lkotlin/coroutines/CoroutineContext;", "channel", "Lkotlinx/coroutines/channels/Channel;", com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE, "", "(Lkotlin/coroutines/CoroutineContext;Lkotlinx/coroutines/channels/Channel;Z)V", "handleJobException", "exception", "", "onCancelling", "", "cause", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
class ActorCoroutine<E> extends kotlinx.coroutines.channels.ChannelCoroutine<E> implements kotlinx.coroutines.channels.ActorScope<E> {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ActorCoroutine(kotlin.coroutines.CoroutineContext parentContext, kotlinx.coroutines.channels.Channel<E> channel, boolean active) {
        super(parentContext, channel, false, active);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(parentContext, "parentContext");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(channel, "channel");
        initParentJob((kotlinx.coroutines.Job) parentContext.get(kotlinx.coroutines.Job.INSTANCE));
    }

    @Override // kotlinx.coroutines.JobSupport
    protected void onCancelling(java.lang.Throwable cause) {
        kotlinx.coroutines.channels.Channel<E> channel = get_channel();
        if (cause != null) {
            CancellationException = cause instanceof java.util.concurrent.CancellationException ? (java.util.concurrent.CancellationException) cause : null;
            if (CancellationException == null) {
                CancellationException = kotlinx.coroutines.ExceptionsKt.CancellationException(kotlinx.coroutines.DebugStringsKt.getClassSimpleName(this) + " was cancelled", cause);
            }
        }
        channel.cancel(CancellationException);
    }

    @Override // kotlinx.coroutines.JobSupport
    protected boolean handleJobException(java.lang.Throwable exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        kotlinx.coroutines.CoroutineExceptionHandlerKt.handleCoroutineException(get$context(), exception);
        return true;
    }
}
