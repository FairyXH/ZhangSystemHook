package kotlinx.coroutines;

/* JADX INFO: compiled from: JobSupport.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
/* synthetic */ class JobSupport$onAwaitInternal$1 extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function3<kotlinx.coroutines.JobSupport, kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, kotlin.Unit> {
    public static final kotlinx.coroutines.JobSupport$onAwaitInternal$1 INSTANCE = new kotlinx.coroutines.JobSupport$onAwaitInternal$1();

    JobSupport$onAwaitInternal$1() {
        super(3, kotlinx.coroutines.JobSupport.class, "onAwaitInternalRegFunc", "onAwaitInternalRegFunc(Lkotlinx/coroutines/selects/SelectInstance;Ljava/lang/Object;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function3
    public /* bridge */ /* synthetic */ kotlin.Unit invoke(kotlinx.coroutines.JobSupport jobSupport, kotlinx.coroutines.selects.SelectInstance<?> selectInstance, java.lang.Object p3) {
        invoke2(jobSupport, selectInstance, p3);
        return kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public final void invoke2(kotlinx.coroutines.JobSupport p0, kotlinx.coroutines.selects.SelectInstance<?> p1, java.lang.Object p2) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p0, "p0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p1, "p1");
        p0.onAwaitInternalRegFunc(p1, p2);
    }
}
