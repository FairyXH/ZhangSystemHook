package kotlinx.coroutines.sync;

/* JADX INFO: compiled from: Semaphore.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
/* synthetic */ class SemaphoreImpl$tryResumeNextFromQueue$createNewSegment$1 extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function2<java.lang.Long, kotlinx.coroutines.sync.SemaphoreSegment, kotlinx.coroutines.sync.SemaphoreSegment> {
    public static final kotlinx.coroutines.sync.SemaphoreImpl$tryResumeNextFromQueue$createNewSegment$1 INSTANCE = new kotlinx.coroutines.sync.SemaphoreImpl$tryResumeNextFromQueue$createNewSegment$1();

    SemaphoreImpl$tryResumeNextFromQueue$createNewSegment$1() {
        super(2, kotlinx.coroutines.sync.SemaphoreKt.class, "createSegment", "createSegment(JLkotlinx/coroutines/sync/SemaphoreSegment;)Lkotlinx/coroutines/sync/SemaphoreSegment;", 1);
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ kotlinx.coroutines.sync.SemaphoreSegment invoke(java.lang.Long l, kotlinx.coroutines.sync.SemaphoreSegment semaphoreSegment) {
        return invoke(l.longValue(), semaphoreSegment);
    }

    public final kotlinx.coroutines.sync.SemaphoreSegment invoke(long p0, kotlinx.coroutines.sync.SemaphoreSegment p1) {
        return kotlinx.coroutines.sync.SemaphoreKt.createSegment(p0, p1);
    }
}
