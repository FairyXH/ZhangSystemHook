package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: Actor.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000R\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0099\u0001\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2-\b\u0002\u0010\n\u001a'\u0012\u0015\u0012\u0013\u0018\u00010\f¢\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000bj\u0004\u0018\u0001`\u00112-\u0010\u0012\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u0015\u0012\u0006\u0012\u0004\u0018\u00010\u00160\u0013¢\u0006\u0002\b\u0017H\u0007¢\u0006\u0002\u0010\u0018¨\u0006\u0019"}, d2 = {"actor", "Lkotlinx/coroutines/channels/SendChannel;", "E", "Lkotlinx/coroutines/CoroutineScope;", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "", "start", "Lkotlinx/coroutines/CoroutineStart;", "onCompletion", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "cause", "", "Lkotlinx/coroutines/CompletionHandler;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/channels/ActorScope;", "Lkotlin/coroutines/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;ILkotlinx/coroutines/CoroutineStart;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/channels/SendChannel;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ActorKt {
    public static final <E> kotlinx.coroutines.channels.SendChannel<E> actor(kotlinx.coroutines.CoroutineScope $this$actor, kotlin.coroutines.CoroutineContext context, int capacity, kotlinx.coroutines.CoroutineStart start, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> function1, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.channels.ActorScope<E>, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> block) {
        kotlinx.coroutines.channels.LazyActorCoroutine coroutine;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$actor, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(start, "start");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlin.coroutines.CoroutineContext newContext = kotlinx.coroutines.CoroutineContextKt.newCoroutineContext($this$actor, context);
        kotlinx.coroutines.channels.Channel channel = kotlinx.coroutines.channels.ChannelKt.Channel$default(capacity, null, null, 6, null);
        if (start.isLazy()) {
            coroutine = new kotlinx.coroutines.channels.LazyActorCoroutine(newContext, channel, block);
        } else {
            coroutine = new kotlinx.coroutines.channels.ActorCoroutine(newContext, channel, true);
        }
        if (function1 != null) {
            coroutine.invokeOnCompletion(function1);
        }
        coroutine.start(start, coroutine, block);
        return coroutine;
    }
}
