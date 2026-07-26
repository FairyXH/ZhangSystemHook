package kotlin.system;

/* JADX INFO: compiled from: Timing.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a'\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0086\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a'\u0010\u0005\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0086\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0006"}, d2 = {"measureNanoTime", "", "block", "Lkotlin/Function0;", "", "measureTimeMillis", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class TimingKt {
    public static final long measureTimeMillis(kotlin.jvm.functions.Function0<kotlin.Unit> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        long start = java.lang.System.currentTimeMillis();
        block.invoke();
        return java.lang.System.currentTimeMillis() - start;
    }

    public static final long measureNanoTime(kotlin.jvm.functions.Function0<kotlin.Unit> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        long start = java.lang.System.nanoTime();
        block.invoke();
        return java.lang.System.nanoTime() - start;
    }
}
