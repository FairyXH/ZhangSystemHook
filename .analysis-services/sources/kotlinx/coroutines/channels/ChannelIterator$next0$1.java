package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: Channel.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.channels.ChannelIterator$DefaultImpls", f = "Channel.kt", i = {0}, l = {com.android.internal.util.FrameworkStatsLog.SCREEN_STATE_CHANGED_V2}, m = "next", n = {"$this"}, s = {"L$0"})
final class ChannelIterator$next0$1<E> extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    java.lang.Object L$0;
    int label;
    /* synthetic */ java.lang.Object result;

    ChannelIterator$next0$1(kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ChannelIterator$next0$1> continuation) {
        super(continuation);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final java.lang.Object invokeSuspend(java.lang.Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return kotlinx.coroutines.channels.ChannelIterator.DefaultImpls.next(null, this);
    }
}
