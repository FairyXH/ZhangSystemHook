package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: BufferedChannel.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.channels.BufferedChannel", f = "BufferedChannel.kt", i = {0, 0, 0, 0}, l = {3056}, m = "receiveCatchingOnNoWaiterSuspend-GKJJFZk", n = {"this", "segment", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD}, s = {"L$0", "L$1", "I$0", "J$0"})
final class BufferedChannel$receiveCatchingOnNoWaiterSuspend$1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    int I$0;
    long J$0;
    java.lang.Object L$0;
    java.lang.Object L$1;
    int label;
    /* synthetic */ java.lang.Object result;
    final /* synthetic */ kotlinx.coroutines.channels.BufferedChannel<E> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    BufferedChannel$receiveCatchingOnNoWaiterSuspend$1(kotlinx.coroutines.channels.BufferedChannel<E> bufferedChannel, kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.BufferedChannel$receiveCatchingOnNoWaiterSuspend$1> continuation) {
        super(continuation);
        this.this$0 = bufferedChannel;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        java.lang.Object objM12812receiveCatchingOnNoWaiterSuspendGKJJFZk = this.this$0.m12812receiveCatchingOnNoWaiterSuspendGKJJFZk(null, 0, 0L, this);
        return objM12812receiveCatchingOnNoWaiterSuspendGKJJFZk == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objM12812receiveCatchingOnNoWaiterSuspendGKJJFZk : kotlinx.coroutines.channels.ChannelResult.m12819boximpl(objM12812receiveCatchingOnNoWaiterSuspendGKJJFZk);
    }
}
