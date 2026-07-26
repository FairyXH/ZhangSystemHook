package com.android.server.permission.jarjar.kotlin.sequences;

/* JADX INFO: compiled from: Sequences.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B#\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0002\u0010\bJ\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0016J\u000f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u000fH\u0096\u0002J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0016R\u0014\u0010\t\u001a\u00020\u00068BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lkotlin/sequences/SubSequence;", "T", "Lkotlin/sequences/Sequence;", "Lkotlin/sequences/DropTakeSequence;", "sequence", "startIndex", "", "endIndex", "(Lkotlin/sequences/Sequence;II)V", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, "getCount", "()I", "drop", "n", "iterator", "", "take", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SubSequence<T> implements com.android.server.permission.jarjar.kotlin.sequences.Sequence<T>, com.android.server.permission.jarjar.kotlin.sequences.DropTakeSequence<T> {
    private final int endIndex;
    private final com.android.server.permission.jarjar.kotlin.sequences.Sequence<T> sequence;
    private final int startIndex;

    /* JADX WARN: Multi-variable type inference failed */
    public SubSequence(com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends T> sequence, int startIndex, int endIndex) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "sequence");
        this.sequence = sequence;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        if (!(this.startIndex >= 0)) {
            throw new java.lang.IllegalArgumentException(("startIndex should be non-negative, but is " + this.startIndex).toString());
        }
        if (!(this.endIndex >= 0)) {
            throw new java.lang.IllegalArgumentException(("endIndex should be non-negative, but is " + this.endIndex).toString());
        }
        if (this.endIndex >= this.startIndex) {
        } else {
            throw new java.lang.IllegalArgumentException(("endIndex should be not less than startIndex, but was " + this.endIndex + " < " + this.startIndex).toString());
        }
    }

    private final int getCount() {
        return this.endIndex - this.startIndex;
    }

    @Override // com.android.server.permission.jarjar.kotlin.sequences.DropTakeSequence
    public com.android.server.permission.jarjar.kotlin.sequences.Sequence<T> drop(int n) {
        return n >= getCount() ? com.android.server.permission.jarjar.kotlin.sequences.SequencesKt.emptySequence() : new com.android.server.permission.jarjar.kotlin.sequences.SubSequence(this.sequence, this.startIndex + n, this.endIndex);
    }

    @Override // com.android.server.permission.jarjar.kotlin.sequences.DropTakeSequence
    public com.android.server.permission.jarjar.kotlin.sequences.Sequence<T> take(int n) {
        return n >= getCount() ? this : new com.android.server.permission.jarjar.kotlin.sequences.SubSequence(this.sequence, this.startIndex, this.startIndex + n);
    }

    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.sequences.SubSequence$iterator$1, reason: invalid class name */
    /* JADX INFO: compiled from: Sequences.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000#\n\u0000\n\u0002\u0010(\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\b\u0010\u000b\u001a\u00020\fH\u0002J\t\u0010\r\u001a\u00020\u000eH\u0096\u0002J\u000e\u0010\u000f\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0010R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n¨\u0006\u0011"}, d2 = {"com/android/server/permission/jarjar/kotlin/sequences/SubSequence$iterator$1", "", "iterator", "getIterator", "()Ljava/util/Iterator;", "position", "", "getPosition", "()I", "setPosition", "(I)V", "drop", "", "hasNext", "", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 implements java.util.Iterator<T>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
        private final java.util.Iterator<T> iterator;
        private int position;
        final /* synthetic */ com.android.server.permission.jarjar.kotlin.sequences.SubSequence<T> this$0;

        @Override // java.util.Iterator
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        AnonymousClass1(com.android.server.permission.jarjar.kotlin.sequences.SubSequence<T> subSequence) {
            this.this$0 = subSequence;
            this.iterator = ((com.android.server.permission.jarjar.kotlin.sequences.SubSequence) subSequence).sequence.iterator();
        }

        public final java.util.Iterator<T> getIterator() {
            return this.iterator;
        }

        public final int getPosition() {
            return this.position;
        }

        public final void setPosition(int i) {
            this.position = i;
        }

        private final void drop() {
            while (this.position < ((com.android.server.permission.jarjar.kotlin.sequences.SubSequence) this.this$0).startIndex && this.iterator.hasNext()) {
                this.iterator.next();
                this.position++;
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            drop();
            return this.position < ((com.android.server.permission.jarjar.kotlin.sequences.SubSequence) this.this$0).endIndex && this.iterator.hasNext();
        }

        @Override // java.util.Iterator
        public T next() {
            drop();
            if (this.position >= ((com.android.server.permission.jarjar.kotlin.sequences.SubSequence) this.this$0).endIndex) {
                throw new java.util.NoSuchElementException();
            }
            this.position++;
            return this.iterator.next();
        }
    }

    @Override // com.android.server.permission.jarjar.kotlin.sequences.Sequence
    public java.util.Iterator<T> iterator() {
        return new com.android.server.permission.jarjar.kotlin.sequences.SubSequence.AnonymousClass1(this);
    }
}
