package kotlinx.coroutines.sync;

/* JADX INFO: compiled from: Mutex.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
/* synthetic */ class MutexImpl$onLock$1 extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function3<kotlinx.coroutines.sync.MutexImpl, kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, kotlin.Unit> {
    public static final kotlinx.coroutines.sync.MutexImpl$onLock$1 INSTANCE = new kotlinx.coroutines.sync.MutexImpl$onLock$1();

    MutexImpl$onLock$1() {
        super(3, kotlinx.coroutines.sync.MutexImpl.class, "onLockRegFunction", "onLockRegFunction(Lkotlinx/coroutines/selects/SelectInstance;Ljava/lang/Object;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function3
    public /* bridge */ /* synthetic */ kotlin.Unit invoke(kotlinx.coroutines.sync.MutexImpl mutexImpl, kotlinx.coroutines.selects.SelectInstance<?> selectInstance, java.lang.Object p3) {
        invoke2(mutexImpl, selectInstance, p3);
        return kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public final void invoke2(kotlinx.coroutines.sync.MutexImpl p0, kotlinx.coroutines.selects.SelectInstance<?> p1, java.lang.Object p2) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p0, "p0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p1, "p1");
        p0.onLockRegFunction(p1, p2);
    }
}
