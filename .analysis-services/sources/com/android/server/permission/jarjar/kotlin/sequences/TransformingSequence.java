package com.android.server.permission.jarjar.kotlin.sequences;

/* JADX INFO: compiled from: Sequences.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\b\u0012\u0004\u0012\u0002H\u00020\u0003B'\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0006¢\u0006\u0002\u0010\u0007J3\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\t0\u0003\"\u0004\b\u0002\u0010\t2\u0018\u0010\n\u001a\u0014\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u000b0\u0006H\u0000¢\u0006\u0002\b\fJ\u000f\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00010\u000bH\u0096\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlin/sequences/TransformingSequence;", "T", "R", "Lkotlin/sequences/Sequence;", "sequence", "transformer", "Lkotlin/Function1;", "(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;)V", "flatten", "E", "iterator", "", "flatten$kotlin_stdlib", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class TransformingSequence<T, R> implements com.android.server.permission.jarjar.kotlin.sequences.Sequence<R> {
    private final com.android.server.permission.jarjar.kotlin.sequences.Sequence<T> sequence;
    private final com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<T, R> transformer;

    /* JADX WARN: Multi-variable type inference failed */
    public TransformingSequence(com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends T> sequence, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "sequence");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "transformer");
        this.sequence = sequence;
        this.transformer = function1;
    }

    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.sequences.TransformingSequence$iterator$1, reason: invalid class name */
    /* JADX INFO: compiled from: Sequences.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0015\n\u0000\n\u0002\u0010(\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\t\u0010\u0005\u001a\u00020\u0006H\u0096\u0002J\u000e\u0010\u0007\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\bR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00010\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0004¨\u0006\t"}, d2 = {"com/android/server/permission/jarjar/kotlin/sequences/TransformingSequence$iterator$1", "", "iterator", "getIterator", "()Ljava/util/Iterator;", "hasNext", "", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 implements java.util.Iterator<R>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
        private final java.util.Iterator<T> iterator;
        final /* synthetic */ com.android.server.permission.jarjar.kotlin.sequences.TransformingSequence<T, R> this$0;

        @Override // java.util.Iterator
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        AnonymousClass1(com.android.server.permission.jarjar.kotlin.sequences.TransformingSequence<T, R> transformingSequence) {
            this.this$0 = transformingSequence;
            this.iterator = ((com.android.server.permission.jarjar.kotlin.sequences.TransformingSequence) transformingSequence).sequence.iterator();
        }

        public final java.util.Iterator<T> getIterator() {
            return this.iterator;
        }

        @Override // java.util.Iterator
        public R next() {
            return (R) ((com.android.server.permission.jarjar.kotlin.sequences.TransformingSequence) this.this$0).transformer.invoke(this.iterator.next());
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
    }

    @Override // com.android.server.permission.jarjar.kotlin.sequences.Sequence
    public java.util.Iterator<R> iterator() {
        return new com.android.server.permission.jarjar.kotlin.sequences.TransformingSequence.AnonymousClass1(this);
    }

    public final <E> com.android.server.permission.jarjar.kotlin.sequences.Sequence<E> flatten$kotlin_stdlib(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super R, ? extends java.util.Iterator<? extends E>> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "iterator");
        return new com.android.server.permission.jarjar.kotlin.sequences.FlatteningSequence(this.sequence, this.transformer, function1);
    }
}
