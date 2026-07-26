package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: Channel.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00004\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\f\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0007\u001a>\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00062\u0016\b\u0002\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\t\u0018\u00010\b\u001aX\u0010\n\u001a\u0002H\u000b\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\f2#\u0010\r\u001a\u001f\u0012\u0015\u0012\u0013\u0018\u00010\u000e¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u0002H\u000b0\bH\u0086\bø\u0001\u0000ø\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0004\b\u0012\u0010\u0013\u001a^\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u000b0\f\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\f2#\u0010\u0015\u001a\u001f\u0012\u0015\u0012\u0013\u0018\u00010\u000e¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u00020\t0\bH\u0086\bø\u0001\u0000ø\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0004\b\u0016\u0010\u0013\u001a^\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u000b0\f\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\f2#\u0010\u0015\u001a\u001f\u0012\u0015\u0012\u0013\u0018\u00010\u000e¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u00020\t0\bH\u0086\bø\u0001\u0000ø\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0004\b\u0017\u0010\u0013\u001a\\\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u000b0\f\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\f2!\u0010\u0015\u001a\u001d\u0012\u0013\u0012\u0011H\u000b¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0019\u0012\u0004\u0012\u00020\t0\bH\u0086\bø\u0001\u0000ø\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0004\b\u001a\u0010\u0013\u0082\u0002\u000e\n\u0005\b\u009920\u0001\n\u0005\b¡\u001e0\u0001¨\u0006\u001b"}, d2 = {"Channel", "Lkotlinx/coroutines/channels/Channel;", "E", "capacity", "", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "onUndeliveredElement", "Lkotlin/Function1;", "", "getOrElse", "T", "Lkotlinx/coroutines/channels/ChannelResult;", "onFailure", "", "Lkotlin/ParameterName;", "name", "exception", "getOrElse-WpGqRn0", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "onClosed", "action", "onClosed-WpGqRn0", "onFailure-WpGqRn0", "onSuccess", "value", "onSuccess-WpGqRn0", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ChannelKt {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: renamed from: getOrElse-WpGqRn0, reason: not valid java name */
    public static final <T> T m12815getOrElseWpGqRn0(java.lang.Object obj, kotlin.jvm.functions.Function1<? super java.lang.Throwable, ? extends T> onFailure) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onFailure, "onFailure");
        return obj instanceof kotlinx.coroutines.channels.ChannelResult.Failed ? onFailure.invoke(kotlinx.coroutines.channels.ChannelResult.m12823exceptionOrNullimpl(obj)) : obj;
    }

    /* JADX INFO: renamed from: onSuccess-WpGqRn0, reason: not valid java name */
    public static final <T> java.lang.Object m12818onSuccessWpGqRn0(java.lang.Object $this$onSuccess_u2dWpGqRn0, kotlin.jvm.functions.Function1<? super T, kotlin.Unit> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        if (!($this$onSuccess_u2dWpGqRn0 instanceof kotlinx.coroutines.channels.ChannelResult.Failed)) {
            action.invoke($this$onSuccess_u2dWpGqRn0);
        }
        return $this$onSuccess_u2dWpGqRn0;
    }

    /* JADX INFO: renamed from: onFailure-WpGqRn0, reason: not valid java name */
    public static final <T> java.lang.Object m12817onFailureWpGqRn0(java.lang.Object $this$onFailure_u2dWpGqRn0, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        if ($this$onFailure_u2dWpGqRn0 instanceof kotlinx.coroutines.channels.ChannelResult.Failed) {
            action.invoke(kotlinx.coroutines.channels.ChannelResult.m12823exceptionOrNullimpl($this$onFailure_u2dWpGqRn0));
        }
        return $this$onFailure_u2dWpGqRn0;
    }

    /* JADX INFO: renamed from: onClosed-WpGqRn0, reason: not valid java name */
    public static final <T> java.lang.Object m12816onClosedWpGqRn0(java.lang.Object $this$onClosed_u2dWpGqRn0, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        if ($this$onClosed_u2dWpGqRn0 instanceof kotlinx.coroutines.channels.ChannelResult.Closed) {
            action.invoke(kotlinx.coroutines.channels.ChannelResult.m12823exceptionOrNullimpl($this$onClosed_u2dWpGqRn0));
        }
        return $this$onClosed_u2dWpGqRn0;
    }

    public static /* synthetic */ kotlinx.coroutines.channels.Channel Channel$default(int i, kotlinx.coroutines.channels.BufferOverflow bufferOverflow, kotlin.jvm.functions.Function1 function1, int i2, java.lang.Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        if ((i2 & 2) != 0) {
            bufferOverflow = kotlinx.coroutines.channels.BufferOverflow.SUSPEND;
        }
        if ((i2 & 4) != 0) {
            function1 = null;
        }
        return Channel(i, bufferOverflow, function1);
    }

    public static final <E> kotlinx.coroutines.channels.Channel<E> Channel(int capacity, kotlinx.coroutines.channels.BufferOverflow onBufferOverflow, kotlin.jvm.functions.Function1<? super E, kotlin.Unit> function1) {
        kotlinx.coroutines.channels.ConflatedBufferedChannel conflatedBufferedChannel;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onBufferOverflow, "onBufferOverflow");
        switch (capacity) {
            case -2:
                return onBufferOverflow == kotlinx.coroutines.channels.BufferOverflow.SUSPEND ? new kotlinx.coroutines.channels.BufferedChannel<>(kotlinx.coroutines.channels.Channel.INSTANCE.getCHANNEL_DEFAULT_CAPACITY$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(), function1) : new kotlinx.coroutines.channels.ConflatedBufferedChannel(1, onBufferOverflow, function1);
            case -1:
                if (!(onBufferOverflow == kotlinx.coroutines.channels.BufferOverflow.SUSPEND)) {
                    throw new java.lang.IllegalArgumentException("CONFLATED capacity cannot be used with non-default onBufferOverflow".toString());
                }
                return new kotlinx.coroutines.channels.ConflatedBufferedChannel(1, kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST, function1);
            case 0:
                if (onBufferOverflow == kotlinx.coroutines.channels.BufferOverflow.SUSPEND) {
                    conflatedBufferedChannel = new kotlinx.coroutines.channels.BufferedChannel<>(0, function1);
                } else {
                    conflatedBufferedChannel = new kotlinx.coroutines.channels.ConflatedBufferedChannel(1, onBufferOverflow, function1);
                }
                return conflatedBufferedChannel;
            case Integer.MAX_VALUE:
                return new kotlinx.coroutines.channels.BufferedChannel(Integer.MAX_VALUE, function1);
            default:
                return onBufferOverflow == kotlinx.coroutines.channels.BufferOverflow.SUSPEND ? new kotlinx.coroutines.channels.BufferedChannel<>(capacity, function1) : new kotlinx.coroutines.channels.ConflatedBufferedChannel(capacity, onBufferOverflow, function1);
        }
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Since 1.4.0, binary compatibility with earlier versions")
    public static final /* synthetic */ kotlinx.coroutines.channels.Channel Channel(int capacity) {
        return Channel$default(capacity, null, null, 6, null);
    }

    public static /* synthetic */ kotlinx.coroutines.channels.Channel Channel$default(int i, int i2, java.lang.Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        return Channel(i);
    }
}
