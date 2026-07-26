package kotlin.sequences;

/* JADX INFO: compiled from: Sequences.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u0002*\u0004\b\u0002\u0010\u00032\b\u0012\u0004\u0012\u0002H\u00030\u0004BA\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0007\u0012\u0018\u0010\b\u001a\u0014\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\t0\u0007¢\u0006\u0002\u0010\nJ\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00020\tH\u0096\u0002R \u0010\b\u001a\u0014\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\t0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lkotlin/sequences/FlatteningSequence;", "T", "R", "E", "Lkotlin/sequences/Sequence;", "sequence", "transformer", "Lkotlin/Function1;", "iterator", "", "(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class FlatteningSequence<T, R, E> implements kotlin.sequences.Sequence<E> {
    private final kotlin.jvm.functions.Function1<R, java.util.Iterator<E>> iterator;
    private final kotlin.sequences.Sequence<T> sequence;
    private final kotlin.jvm.functions.Function1<T, R> transformer;

    /* JADX WARN: Multi-variable type inference failed */
    public FlatteningSequence(kotlin.sequences.Sequence<? extends T> sequence, kotlin.jvm.functions.Function1<? super T, ? extends R> transformer, kotlin.jvm.functions.Function1<? super R, ? extends java.util.Iterator<? extends E>> iterator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "sequence");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transformer, "transformer");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterator, "iterator");
        this.sequence = sequence;
        this.transformer = transformer;
        this.iterator = iterator;
    }

    /* JADX INFO: renamed from: kotlin.sequences.FlatteningSequence$iterator$1, reason: invalid class name */
    /* JADX INFO: compiled from: Sequences.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0015\n\u0000\n\u0002\u0010(\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\b\u0010\t\u001a\u00020\nH\u0002J\t\u0010\u000b\u001a\u00020\nH\u0096\u0002J\u000e\u0010\f\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\rR\"\u0010\u0002\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0001X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0003\u0010\u0004\"\u0004\b\u0005\u0010\u0006R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00010\u0001¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0004¨\u0006\u000e"}, d2 = {"kotlin/sequences/FlatteningSequence$iterator$1", "", "itemIterator", "getItemIterator", "()Ljava/util/Iterator;", "setItemIterator", "(Ljava/util/Iterator;)V", "iterator", "getIterator", "ensureItemIterator", "", "hasNext", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 implements java.util.Iterator<E>, kotlin.jvm.internal.markers.KMappedMarker {
        private java.util.Iterator<? extends E> itemIterator;
        private final java.util.Iterator<T> iterator;
        final /* synthetic */ kotlin.sequences.FlatteningSequence<T, R, E> this$0;

        @Override // java.util.Iterator
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        AnonymousClass1(kotlin.sequences.FlatteningSequence<T, R, E> flatteningSequence) {
            this.this$0 = flatteningSequence;
            this.iterator = ((kotlin.sequences.FlatteningSequence) flatteningSequence).sequence.iterator();
        }

        public final java.util.Iterator<T> getIterator() {
            return this.iterator;
        }

        public final java.util.Iterator<E> getItemIterator() {
            return this.itemIterator;
        }

        public final void setItemIterator(java.util.Iterator<? extends E> it) {
            this.itemIterator = it;
        }

        @Override // java.util.Iterator
        public E next() {
            if (!ensureItemIterator()) {
                throw new java.util.NoSuchElementException();
            }
            java.util.Iterator<? extends E> it = this.itemIterator;
            kotlin.jvm.internal.Intrinsics.checkNotNull(it);
            return it.next();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return ensureItemIterator();
        }

        private final boolean ensureItemIterator() {
            java.util.Iterator<? extends E> it = this.itemIterator;
            if ((it == null || it.hasNext()) ? false : true) {
                this.itemIterator = null;
            }
            while (this.itemIterator == null) {
                if (!this.iterator.hasNext()) {
                    return false;
                }
                java.util.Iterator<? extends E> it2 = (java.util.Iterator) ((kotlin.sequences.FlatteningSequence) this.this$0).iterator.invoke(((kotlin.sequences.FlatteningSequence) this.this$0).transformer.invoke(this.iterator.next()));
                if (it2.hasNext()) {
                    this.itemIterator = it2;
                    return true;
                }
            }
            return true;
        }
    }

    @Override // kotlin.sequences.Sequence
    public java.util.Iterator<E> iterator() {
        return new kotlin.sequences.FlatteningSequence.AnonymousClass1(this);
    }
}
