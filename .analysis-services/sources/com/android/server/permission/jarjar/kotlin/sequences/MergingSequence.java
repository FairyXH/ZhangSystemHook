package com.android.server.permission.jarjar.kotlin.sequences;

/* JADX INFO: compiled from: Sequences.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u0002*\u0004\b\u0002\u0010\u00032\b\u0012\u0004\u0012\u0002H\u00030\u0004B;\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004\u0012\u0018\u0010\u0007\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\b¢\u0006\u0002\u0010\tJ\u000f\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00020\u000bH\u0096\u0002R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R \u0010\u0007\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lkotlin/sequences/MergingSequence;", "T1", "T2", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lkotlin/sequences/Sequence;", "sequence1", "sequence2", "transform", "Lkotlin/Function2;", "(Lkotlin/sequences/Sequence;Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function2;)V", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MergingSequence<T1, T2, V> implements com.android.server.permission.jarjar.kotlin.sequences.Sequence<V> {
    private final com.android.server.permission.jarjar.kotlin.sequences.Sequence<T1> sequence1;
    private final com.android.server.permission.jarjar.kotlin.sequences.Sequence<T2> sequence2;
    private final com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<T1, T2, V> transform;

    /* JADX WARN: Multi-variable type inference failed */
    public MergingSequence(com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends T1> sequence, com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends T2> sequence2, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super T1, ? super T2, ? extends V> function2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "sequence1");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence2, "sequence2");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "transform");
        this.sequence1 = sequence;
        this.sequence2 = sequence2;
        this.transform = function2;
    }

    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.sequences.MergingSequence$iterator$1, reason: invalid class name */
    /* JADX INFO: compiled from: Sequences.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0015\n\u0000\n\u0002\u0010(\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\t\u0010\u0007\u001a\u00020\bH\u0096\u0002J\u000e\u0010\t\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\nR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00010\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0004R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00010\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0004¨\u0006\u000b"}, d2 = {"com/android/server/permission/jarjar/kotlin/sequences/MergingSequence$iterator$1", "", "iterator1", "getIterator1", "()Ljava/util/Iterator;", "iterator2", "getIterator2", "hasNext", "", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 implements java.util.Iterator<V>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
        private final java.util.Iterator<T1> iterator1;
        private final java.util.Iterator<T2> iterator2;
        final /* synthetic */ com.android.server.permission.jarjar.kotlin.sequences.MergingSequence<T1, T2, V> this$0;

        @Override // java.util.Iterator
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        AnonymousClass1(com.android.server.permission.jarjar.kotlin.sequences.MergingSequence<T1, T2, V> mergingSequence) {
            this.this$0 = mergingSequence;
            this.iterator1 = ((com.android.server.permission.jarjar.kotlin.sequences.MergingSequence) mergingSequence).sequence1.iterator();
            this.iterator2 = ((com.android.server.permission.jarjar.kotlin.sequences.MergingSequence) mergingSequence).sequence2.iterator();
        }

        public final java.util.Iterator<T1> getIterator1() {
            return this.iterator1;
        }

        public final java.util.Iterator<T2> getIterator2() {
            return this.iterator2;
        }

        @Override // java.util.Iterator
        public V next() {
            return (V) ((com.android.server.permission.jarjar.kotlin.sequences.MergingSequence) this.this$0).transform.invoke(this.iterator1.next(), this.iterator2.next());
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iterator1.hasNext() && this.iterator2.hasNext();
        }
    }

    @Override // com.android.server.permission.jarjar.kotlin.sequences.Sequence
    public java.util.Iterator<V> iterator() {
        return new com.android.server.permission.jarjar.kotlin.sequences.MergingSequence.AnonymousClass1(this);
    }
}
