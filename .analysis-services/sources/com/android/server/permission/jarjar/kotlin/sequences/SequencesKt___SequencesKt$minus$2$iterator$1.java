package com.android.server.permission.jarjar.kotlin.sequences;

/* JADX INFO: Add missing generic type declarations: [T] */
/* JADX INFO: compiled from: _Sequences.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "T", "it", "invoke", "(Ljava/lang/Object;)Ljava/lang/Boolean;"}, k = 3, mv = {1, 9, 0}, xi = 48)
final class SequencesKt___SequencesKt$minus$2$iterator$1<T> extends com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<T, java.lang.Boolean> {
    final /* synthetic */ T[] $elements;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    SequencesKt___SequencesKt$minus$2$iterator$1(T[] tArr) {
        super(1);
        this.$elements = tArr;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function1
    public final java.lang.Boolean invoke(T t) {
        return java.lang.Boolean.valueOf(com.android.server.permission.jarjar.kotlin.collections.ArraysKt.contains(this.$elements, t));
    }
}
