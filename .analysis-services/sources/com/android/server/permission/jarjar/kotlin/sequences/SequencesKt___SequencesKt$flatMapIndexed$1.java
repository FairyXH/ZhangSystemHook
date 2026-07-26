package com.android.server.permission.jarjar.kotlin.sequences;

/* JADX INFO: Add missing generic type declarations: [R] */
/* JADX INFO: compiled from: _Sequences.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
/* synthetic */ class SequencesKt___SequencesKt$flatMapIndexed$1<R> extends com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReferenceImpl implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<java.lang.Iterable<? extends R>, java.util.Iterator<? extends R>> {
    public static final com.android.server.permission.jarjar.kotlin.sequences.SequencesKt___SequencesKt$flatMapIndexed$1 INSTANCE = new com.android.server.permission.jarjar.kotlin.sequences.SequencesKt___SequencesKt$flatMapIndexed$1();

    SequencesKt___SequencesKt$flatMapIndexed$1() {
        super(1, java.lang.Iterable.class, "iterator", "iterator()Ljava/util/Iterator;", 0);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function1
    public final java.util.Iterator<R> invoke(java.lang.Iterable<? extends R> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "p0");
        return iterable.iterator();
    }
}
