package kotlin.sequences;

/* JADX INFO: compiled from: Sequences.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\b\u0012\u0004\u0012\u0002H\u00020\u0003B-\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003\u0012\u0018\u0010\u0005\u001a\u0014\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0006¢\u0006\u0002\u0010\bJ\u000f\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00010\nH\u0096\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R \u0010\u0005\u001a\u0014\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lkotlin/sequences/TransformingIndexedSequence;", "T", "R", "Lkotlin/sequences/Sequence;", "sequence", "transformer", "Lkotlin/Function2;", "", "(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function2;)V", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class TransformingIndexedSequence<T, R> implements kotlin.sequences.Sequence<R> {
    private final kotlin.sequences.Sequence<T> sequence;
    private final kotlin.jvm.functions.Function2<java.lang.Integer, T, R> transformer;

    /* JADX WARN: Multi-variable type inference failed */
    public TransformingIndexedSequence(kotlin.sequences.Sequence<? extends T> sequence, kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, ? extends R> transformer) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "sequence");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transformer, "transformer");
        this.sequence = sequence;
        this.transformer = transformer;
    }

    /* JADX INFO: renamed from: kotlin.sequences.TransformingIndexedSequence$iterator$1, reason: invalid class name */
    /* JADX INFO: compiled from: Sequences.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001b\n\u0000\n\u0002\u0010(\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\t\u0010\u000b\u001a\u00020\fH\u0096\u0002J\u000e\u0010\r\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u000eR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00010\u0001¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u000f"}, d2 = {"kotlin/sequences/TransformingIndexedSequence$iterator$1", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "getIndex", "()I", "setIndex", "(I)V", "iterator", "getIterator", "()Ljava/util/Iterator;", "hasNext", "", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 implements java.util.Iterator<R>, kotlin.jvm.internal.markers.KMappedMarker {
        private int index;
        private final java.util.Iterator<T> iterator;
        final /* synthetic */ kotlin.sequences.TransformingIndexedSequence<T, R> this$0;

        @Override // java.util.Iterator
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        AnonymousClass1(kotlin.sequences.TransformingIndexedSequence<T, R> transformingIndexedSequence) {
            this.this$0 = transformingIndexedSequence;
            this.iterator = ((kotlin.sequences.TransformingIndexedSequence) transformingIndexedSequence).sequence.iterator();
        }

        public final java.util.Iterator<T> getIterator() {
            return this.iterator;
        }

        public final int getIndex() {
            return this.index;
        }

        public final void setIndex(int i) {
            this.index = i;
        }

        @Override // java.util.Iterator
        public R next() {
            kotlin.jvm.functions.Function2 function2 = ((kotlin.sequences.TransformingIndexedSequence) this.this$0).transformer;
            int i = this.index;
            this.index = i + 1;
            if (i < 0) {
                kotlin.collections.CollectionsKt.throwIndexOverflow();
            }
            return (R) function2.invoke(java.lang.Integer.valueOf(i), this.iterator.next());
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
    }

    @Override // kotlin.sequences.Sequence
    public java.util.Iterator<R> iterator() {
        return new kotlin.sequences.TransformingIndexedSequence.AnonymousClass1(this);
    }
}
