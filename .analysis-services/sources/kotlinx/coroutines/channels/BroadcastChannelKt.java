package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: BroadcastChannel.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u001a\u001c\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"NO_ELEMENT", "Lkotlinx/coroutines/internal/Symbol;", "BroadcastChannel", "Lkotlinx/coroutines/channels/BroadcastChannel;", "E", "capacity", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class BroadcastChannelKt {
    private static final kotlinx.coroutines.internal.Symbol NO_ELEMENT = new kotlinx.coroutines.internal.Symbol("NO_ELEMENT");

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "BroadcastChannel is deprecated in the favour of SharedFlow and StateFlow, and is no longer supported")
    public static final <E> kotlinx.coroutines.channels.BroadcastChannel<E> BroadcastChannel(int capacity) {
        switch (capacity) {
            case -2:
                return new kotlinx.coroutines.channels.BroadcastChannelImpl(kotlinx.coroutines.channels.Channel.INSTANCE.getCHANNEL_DEFAULT_CAPACITY$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host());
            case -1:
                return new kotlinx.coroutines.channels.ConflatedBroadcastChannel();
            case 0:
                throw new java.lang.IllegalArgumentException("Unsupported 0 capacity for BroadcastChannel");
            case Integer.MAX_VALUE:
                throw new java.lang.IllegalArgumentException("Unsupported UNLIMITED capacity for BroadcastChannel");
            default:
                return new kotlinx.coroutines.channels.BroadcastChannelImpl(capacity);
        }
    }
}
