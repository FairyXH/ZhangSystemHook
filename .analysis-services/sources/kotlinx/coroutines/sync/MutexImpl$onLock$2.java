package kotlinx.coroutines.sync;

/* JADX INFO: compiled from: Mutex.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
/* synthetic */ class MutexImpl$onLock$2 extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function3<kotlinx.coroutines.sync.MutexImpl, java.lang.Object, java.lang.Object, java.lang.Object> {
    public static final kotlinx.coroutines.sync.MutexImpl$onLock$2 INSTANCE = new kotlinx.coroutines.sync.MutexImpl$onLock$2();

    MutexImpl$onLock$2() {
        super(3, kotlinx.coroutines.sync.MutexImpl.class, "onLockProcessResult", "onLockProcessResult(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", 0);
    }

    @Override // kotlin.jvm.functions.Function3
    public final java.lang.Object invoke(kotlinx.coroutines.sync.MutexImpl p0, java.lang.Object p1, java.lang.Object p2) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p0, "p0");
        return p0.onLockProcessResult(p1, p2);
    }
}
