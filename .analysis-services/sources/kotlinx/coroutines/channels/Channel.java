package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: Channel.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u0000 \u0004*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u0001\u0004¨\u0006\u0005"}, d2 = {"Lkotlinx/coroutines/channels/Channel;", "E", "Lkotlinx/coroutines/channels/SendChannel;", "Lkotlinx/coroutines/channels/ReceiveChannel;", "Factory", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface Channel<E> extends kotlinx.coroutines.channels.SendChannel<E>, kotlinx.coroutines.channels.ReceiveChannel<E> {
    public static final int BUFFERED = -2;
    public static final int CONFLATED = -1;
    public static final java.lang.String DEFAULT_BUFFER_PROPERTY_NAME = "kotlinx.coroutines.channels.defaultBuffer";

    /* JADX INFO: renamed from: Factory, reason: from kotlin metadata */
    public static final kotlinx.coroutines.channels.Channel.Companion INSTANCE = kotlinx.coroutines.channels.Channel.Companion.$$INSTANCE;
    public static final int OPTIONAL_CHANNEL = -3;
    public static final int RENDEZVOUS = 0;
    public static final int UNLIMITED = Integer.MAX_VALUE;

    /* JADX INFO: compiled from: Channel.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static <E> kotlinx.coroutines.selects.SelectClause1<E> getOnReceiveOrNull(kotlinx.coroutines.channels.Channel<E> channel) {
            return kotlinx.coroutines.channels.ReceiveChannel.DefaultImpls.getOnReceiveOrNull(channel);
        }

        @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Deprecated in the favour of 'trySend' method", replaceWith = @kotlin.ReplaceWith(expression = "trySend(element).isSuccess", imports = {}))
        public static <E> boolean offer(kotlinx.coroutines.channels.Channel<E> channel, E e) {
            return kotlinx.coroutines.channels.SendChannel.DefaultImpls.offer(channel, e);
        }

        @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Deprecated in the favour of 'tryReceive'. Please note that the provided replacement does not rethrow channel's close cause as 'poll' did, for the precise replacement please refer to the 'poll' documentation", replaceWith = @kotlin.ReplaceWith(expression = "tryReceive().getOrNull()", imports = {}))
        public static <E> E poll(kotlinx.coroutines.channels.Channel<E> channel) {
            return (E) kotlinx.coroutines.channels.ReceiveChannel.DefaultImpls.poll(channel);
        }

        @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Deprecated in favor of 'receiveCatching'. Please note that the provided replacement does not rethrow channel's close cause as 'receiveOrNull' did, for the detailed replacement please refer to the 'receiveOrNull' documentation", replaceWith = @kotlin.ReplaceWith(expression = "receiveCatching().getOrNull()", imports = {}))
        public static <E> java.lang.Object receiveOrNull(kotlinx.coroutines.channels.Channel<E> channel, kotlin.coroutines.Continuation<? super E> continuation) {
            return kotlinx.coroutines.channels.ReceiveChannel.DefaultImpls.receiveOrNull(channel, continuation);
        }
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.channels.Channel$Factory, reason: from kotlin metadata */
    /* JADX INFO: compiled from: Channel.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u0004X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/channels/Channel$Factory;", "", "()V", "BUFFERED", "", "CHANNEL_DEFAULT_CAPACITY", "getCHANNEL_DEFAULT_CAPACITY$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()I", "CONFLATED", "DEFAULT_BUFFER_PROPERTY_NAME", "", "OPTIONAL_CHANNEL", "RENDEZVOUS", "UNLIMITED", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public static final int BUFFERED = -2;
        public static final int CONFLATED = -1;
        public static final java.lang.String DEFAULT_BUFFER_PROPERTY_NAME = "kotlinx.coroutines.channels.defaultBuffer";
        public static final int OPTIONAL_CHANNEL = -3;
        public static final int RENDEZVOUS = 0;
        public static final int UNLIMITED = Integer.MAX_VALUE;
        static final /* synthetic */ kotlinx.coroutines.channels.Channel.Companion $$INSTANCE = new kotlinx.coroutines.channels.Channel.Companion();
        private static final int CHANNEL_DEFAULT_CAPACITY = kotlinx.coroutines.internal.SystemPropsKt.systemProp("kotlinx.coroutines.channels.defaultBuffer", 64, 1, 2147483646);

        private Companion() {
        }

        public final int getCHANNEL_DEFAULT_CAPACITY$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
            return CHANNEL_DEFAULT_CAPACITY;
        }
    }
}
