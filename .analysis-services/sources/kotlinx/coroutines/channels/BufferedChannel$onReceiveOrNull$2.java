package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: BufferedChannel.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
/* synthetic */ class BufferedChannel$onReceiveOrNull$2 extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function3<kotlinx.coroutines.channels.BufferedChannel<?>, java.lang.Object, java.lang.Object, java.lang.Object> {
    public static final kotlinx.coroutines.channels.BufferedChannel$onReceiveOrNull$2 INSTANCE = new kotlinx.coroutines.channels.BufferedChannel$onReceiveOrNull$2();

    BufferedChannel$onReceiveOrNull$2() {
        super(3, kotlinx.coroutines.channels.BufferedChannel.class, "processResultSelectReceiveOrNull", "processResultSelectReceiveOrNull(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", 0);
    }

    @Override // kotlin.jvm.functions.Function3
    public final java.lang.Object invoke(kotlinx.coroutines.channels.BufferedChannel<?> p0, java.lang.Object p1, java.lang.Object p2) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p0, "p0");
        return p0.processResultSelectReceiveOrNull(p1, p2);
    }
}
