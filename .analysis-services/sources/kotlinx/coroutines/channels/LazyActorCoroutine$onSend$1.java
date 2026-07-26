package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: Actor.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
/* synthetic */ class LazyActorCoroutine$onSend$1 extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function3<kotlinx.coroutines.channels.LazyActorCoroutine<?>, kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, kotlin.Unit> {
    public static final kotlinx.coroutines.channels.LazyActorCoroutine$onSend$1 INSTANCE = new kotlinx.coroutines.channels.LazyActorCoroutine$onSend$1();

    LazyActorCoroutine$onSend$1() {
        super(3, kotlinx.coroutines.channels.LazyActorCoroutine.class, "onSendRegFunction", "onSendRegFunction(Lkotlinx/coroutines/selects/SelectInstance;Ljava/lang/Object;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function3
    public /* bridge */ /* synthetic */ kotlin.Unit invoke(kotlinx.coroutines.channels.LazyActorCoroutine<?> lazyActorCoroutine, kotlinx.coroutines.selects.SelectInstance<?> selectInstance, java.lang.Object p3) throws java.lang.Throwable {
        invoke2(lazyActorCoroutine, selectInstance, p3);
        return kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public final void invoke2(kotlinx.coroutines.channels.LazyActorCoroutine<?> p0, kotlinx.coroutines.selects.SelectInstance<?> p1, java.lang.Object p2) throws java.lang.Throwable {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p0, "p0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p1, "p1");
        p0.onSendRegFunction(p1, p2);
    }
}
