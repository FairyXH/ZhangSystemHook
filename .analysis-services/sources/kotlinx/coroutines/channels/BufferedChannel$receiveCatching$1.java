package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: BufferedChannel.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.channels.BufferedChannel", f = "BufferedChannel.kt", i = {}, l = {739}, m = "receiveCatching-JP2dKIU$suspendImpl", n = {}, s = {})
final class BufferedChannel$receiveCatching$1<E> extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    int label;
    /* synthetic */ java.lang.Object result;
    final /* synthetic */ kotlinx.coroutines.channels.BufferedChannel<E> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    BufferedChannel$receiveCatching$1(kotlinx.coroutines.channels.BufferedChannel<E> bufferedChannel, kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.BufferedChannel$receiveCatching$1> continuation) {
        super(continuation);
        this.this$0 = bufferedChannel;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        java.lang.Object objM12811receiveCatchingJP2dKIU$suspendImpl = kotlinx.coroutines.channels.BufferedChannel.m12811receiveCatchingJP2dKIU$suspendImpl(this.this$0, this);
        return objM12811receiveCatchingJP2dKIU$suspendImpl == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objM12811receiveCatchingJP2dKIU$suspendImpl : kotlinx.coroutines.channels.ChannelResult.m12819boximpl(objM12811receiveCatchingJP2dKIU$suspendImpl);
    }
}
