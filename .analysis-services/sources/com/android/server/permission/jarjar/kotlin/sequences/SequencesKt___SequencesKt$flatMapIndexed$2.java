package com.android.server.permission.jarjar.kotlin.sequences;

/* JADX INFO: Add missing generic type declarations: [R] */
/* JADX INFO: compiled from: _Sequences.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
/* synthetic */ class SequencesKt___SequencesKt$flatMapIndexed$2<R> extends com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReferenceImpl implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends R>, java.util.Iterator<? extends R>> {
    public static final com.android.server.permission.jarjar.kotlin.sequences.SequencesKt___SequencesKt$flatMapIndexed$2 INSTANCE = new com.android.server.permission.jarjar.kotlin.sequences.SequencesKt___SequencesKt$flatMapIndexed$2();

    SequencesKt___SequencesKt$flatMapIndexed$2() {
        super(1, com.android.server.permission.jarjar.kotlin.sequences.Sequence.class, "iterator", "iterator()Ljava/util/Iterator;", 0);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function1
    public final java.util.Iterator<R> invoke(com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends R> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "p0");
        return sequence.iterator();
    }
}
