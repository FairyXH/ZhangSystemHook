package com.android.server.permission.jarjar.kotlin.sequences;

/* JADX INFO: compiled from: SequenceBuilder.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b'\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u0002B\u0007\b\u0000¢\u0006\u0002\u0010\u0003J\u0016\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00028\u0000H¦@¢\u0006\u0002\u0010\u0007J\u001c\u0010\b\u001a\u00020\u00052\f\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\nH\u0086@¢\u0006\u0002\u0010\u000bJ\u001c\u0010\b\u001a\u00020\u00052\f\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\rH¦@¢\u0006\u0002\u0010\u000eJ\u001c\u0010\b\u001a\u00020\u00052\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00000\u0010H\u0086@¢\u0006\u0002\u0010\u0011¨\u0006\u0012"}, d2 = {"Lkotlin/sequences/SequenceScope;", "T", "", "()V", "yield", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "yieldAll", "elements", "", "(Ljava/lang/Iterable;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "iterator", "", "(Ljava/util/Iterator;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sequence", "Lkotlin/sequences/Sequence;", "(Lkotlin/sequences/Sequence;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class SequenceScope<T> {
    public abstract java.lang.Object yield(T t, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit> continuation);

    public abstract java.lang.Object yieldAll(java.util.Iterator<? extends T> it, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit> continuation);

    public final java.lang.Object yieldAll(java.lang.Iterable<? extends T> iterable, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit> continuation) {
        java.lang.Object objYieldAll;
        return (!((iterable instanceof java.util.Collection) && ((java.util.Collection) iterable).isEmpty()) && (objYieldAll = yieldAll(iterable.iterator(), continuation)) == com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) ? objYieldAll : com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
    }

    public final java.lang.Object yieldAll(com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends T> sequence, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit> continuation) {
        java.lang.Object objYieldAll = yieldAll(sequence.iterator(), continuation);
        return objYieldAll == com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objYieldAll : com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
    }
}
