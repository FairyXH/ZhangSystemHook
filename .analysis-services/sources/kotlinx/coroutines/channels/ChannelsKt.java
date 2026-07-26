package kotlinx.coroutines.channels;

/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"kotlinx/coroutines/channels/ChannelsKt__ChannelsKt", "kotlinx/coroutines/channels/ChannelsKt__Channels_commonKt", "kotlinx/coroutines/channels/ChannelsKt__DeprecatedKt"}, k = 4, mv = {1, 9, 0}, xi = 48)
public final class ChannelsKt {
    public static final java.lang.String DEFAULT_CLOSE_MESSAGE = "Channel was closed";

    public static final void cancelConsumed(kotlinx.coroutines.channels.ReceiveChannel<?> receiveChannel, java.lang.Throwable cause) {
        kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.cancelConsumed(receiveChannel, cause);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "BroadcastChannel is deprecated in the favour of SharedFlow and is no longer supported")
    public static final <E, R> R consume(kotlinx.coroutines.channels.BroadcastChannel<E> broadcastChannel, kotlin.jvm.functions.Function1<? super kotlinx.coroutines.channels.ReceiveChannel<? extends E>, ? extends R> function1) {
        return (R) kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.consume(broadcastChannel, function1);
    }

    public static final <E, R> R consume(kotlinx.coroutines.channels.ReceiveChannel<? extends E> receiveChannel, kotlin.jvm.functions.Function1<? super kotlinx.coroutines.channels.ReceiveChannel<? extends E>, ? extends R> function1) {
        return (R) kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.consume(receiveChannel, function1);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "BroadcastChannel is deprecated in the favour of SharedFlow and is no longer supported")
    public static final <E> java.lang.Object consumeEach(kotlinx.coroutines.channels.BroadcastChannel<E> broadcastChannel, kotlin.jvm.functions.Function1<? super E, kotlin.Unit> function1, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.consumeEach(broadcastChannel, function1, continuation);
    }

    public static final <E> java.lang.Object consumeEach(kotlinx.coroutines.channels.ReceiveChannel<? extends E> receiveChannel, kotlin.jvm.functions.Function1<? super E, kotlin.Unit> function1, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.consumeEach(receiveChannel, function1, continuation);
    }

    public static final kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> consumes(kotlinx.coroutines.channels.ReceiveChannel<?> receiveChannel) {
        return kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.consumes(receiveChannel);
    }

    public static final kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> consumesAll(kotlinx.coroutines.channels.ReceiveChannel<?>... receiveChannelArr) {
        return kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.consumesAll(receiveChannelArr);
    }

    public static final <E, K> kotlinx.coroutines.channels.ReceiveChannel<E> distinctBy(kotlinx.coroutines.channels.ReceiveChannel<? extends E> receiveChannel, kotlin.coroutines.CoroutineContext context, kotlin.jvm.functions.Function2<? super E, ? super kotlin.coroutines.Continuation<? super K>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.distinctBy(receiveChannel, context, function2);
    }

    public static final <E> kotlinx.coroutines.channels.ReceiveChannel<E> filter(kotlinx.coroutines.channels.ReceiveChannel<? extends E> receiveChannel, kotlin.coroutines.CoroutineContext context, kotlin.jvm.functions.Function2<? super E, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.filter(receiveChannel, context, function2);
    }

    public static final <E> kotlinx.coroutines.channels.ReceiveChannel<E> filterNotNull(kotlinx.coroutines.channels.ReceiveChannel<? extends E> receiveChannel) {
        return kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.filterNotNull(receiveChannel);
    }

    public static final <E, R> kotlinx.coroutines.channels.ReceiveChannel<R> map(kotlinx.coroutines.channels.ReceiveChannel<? extends E> receiveChannel, kotlin.coroutines.CoroutineContext context, kotlin.jvm.functions.Function2<? super E, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.map(receiveChannel, context, function2);
    }

    public static final <E, R> kotlinx.coroutines.channels.ReceiveChannel<R> mapIndexed(kotlinx.coroutines.channels.ReceiveChannel<? extends E> receiveChannel, kotlin.coroutines.CoroutineContext context, kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super E, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function3) {
        return kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.mapIndexed(receiveChannel, context, function3);
    }

    public static final <E, C extends kotlinx.coroutines.channels.SendChannel<? super E>> java.lang.Object toChannel(kotlinx.coroutines.channels.ReceiveChannel<? extends E> receiveChannel, C c, kotlin.coroutines.Continuation<? super C> continuation) {
        return kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.toChannel(receiveChannel, c, continuation);
    }

    public static final <E, C extends java.util.Collection<? super E>> java.lang.Object toCollection(kotlinx.coroutines.channels.ReceiveChannel<? extends E> receiveChannel, C c, kotlin.coroutines.Continuation<? super C> continuation) {
        return kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.toCollection(receiveChannel, c, continuation);
    }

    public static final <E> java.lang.Object toList(kotlinx.coroutines.channels.ReceiveChannel<? extends E> receiveChannel, kotlin.coroutines.Continuation<? super java.util.List<? extends E>> continuation) {
        return kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.toList(receiveChannel, continuation);
    }

    public static final <K, V, M extends java.util.Map<? super K, ? super V>> java.lang.Object toMap(kotlinx.coroutines.channels.ReceiveChannel<? extends kotlin.Pair<? extends K, ? extends V>> receiveChannel, M m, kotlin.coroutines.Continuation<? super M> continuation) {
        return kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.toMap(receiveChannel, m, continuation);
    }

    public static final <E> java.lang.Object toMutableSet(kotlinx.coroutines.channels.ReceiveChannel<? extends E> receiveChannel, kotlin.coroutines.Continuation<? super java.util.Set<E>> continuation) {
        return kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.toMutableSet(receiveChannel, continuation);
    }

    public static final <E> java.lang.Object trySendBlocking(kotlinx.coroutines.channels.SendChannel<? super E> sendChannel, E e) {
        return kotlinx.coroutines.channels.ChannelsKt__ChannelsKt.trySendBlocking(sendChannel, e);
    }

    public static final <E, R, V> kotlinx.coroutines.channels.ReceiveChannel<V> zip(kotlinx.coroutines.channels.ReceiveChannel<? extends E> receiveChannel, kotlinx.coroutines.channels.ReceiveChannel<? extends R> receiveChannel2, kotlin.coroutines.CoroutineContext context, kotlin.jvm.functions.Function2<? super E, ? super R, ? extends V> function2) {
        return kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.zip(receiveChannel, receiveChannel2, context, function2);
    }
}
