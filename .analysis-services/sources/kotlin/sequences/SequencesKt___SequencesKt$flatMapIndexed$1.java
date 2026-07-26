package kotlin.sequences;

/* JADX INFO: Add missing generic type declarations: [R] */
/* JADX INFO: compiled from: _Sequences.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
/* synthetic */ class SequencesKt___SequencesKt$flatMapIndexed$1<R> extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function1<java.lang.Iterable<? extends R>, java.util.Iterator<? extends R>> {
    public static final kotlin.sequences.SequencesKt___SequencesKt$flatMapIndexed$1 INSTANCE = new kotlin.sequences.SequencesKt___SequencesKt$flatMapIndexed$1();

    SequencesKt___SequencesKt$flatMapIndexed$1() {
        super(1, java.lang.Iterable.class, "iterator", "iterator()Ljava/util/Iterator;", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public final java.util.Iterator<R> invoke(java.lang.Iterable<? extends R> p0) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p0, "p0");
        return p0.iterator();
    }
}
