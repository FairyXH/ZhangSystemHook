package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: Produce.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003R\u0018\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/channels/ProducerScope;", "E", "Lkotlinx/coroutines/CoroutineScope;", "Lkotlinx/coroutines/channels/SendChannel;", "channel", "getChannel", "()Lkotlinx/coroutines/channels/SendChannel;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface ProducerScope<E> extends kotlinx.coroutines.CoroutineScope, kotlinx.coroutines.channels.SendChannel<E> {
    kotlinx.coroutines.channels.SendChannel<E> getChannel();

    /* JADX INFO: compiled from: Produce.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Deprecated in the favour of 'trySend' method", replaceWith = @kotlin.ReplaceWith(expression = "trySend(element).isSuccess", imports = {}))
        public static <E> boolean offer(kotlinx.coroutines.channels.ProducerScope<? super E> producerScope, E e) {
            return kotlinx.coroutines.channels.SendChannel.DefaultImpls.offer(producerScope, e);
        }
    }
}
