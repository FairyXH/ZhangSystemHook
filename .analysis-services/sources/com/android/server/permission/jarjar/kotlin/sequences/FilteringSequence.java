package com.android.server.permission.jarjar.kotlin.sequences;

/* JADX INFO: compiled from: Sequences.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B1\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00050\u0007¢\u0006\u0002\u0010\bJ\u000f\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\nH\u0096\u0002R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00050\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lkotlin/sequences/FilteringSequence;", "T", "Lkotlin/sequences/Sequence;", "sequence", "sendWhen", "", "predicate", "Lkotlin/Function1;", "(Lkotlin/sequences/Sequence;ZLkotlin/jvm/functions/Function1;)V", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class FilteringSequence<T> implements com.android.server.permission.jarjar.kotlin.sequences.Sequence<T> {
    private final com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<T, java.lang.Boolean> predicate;
    private final boolean sendWhen;
    private final com.android.server.permission.jarjar.kotlin.sequences.Sequence<T> sequence;

    /* JADX WARN: Multi-variable type inference failed */
    public FilteringSequence(com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends T> sequence, boolean sendWhen, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "sequence");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "predicate");
        this.sequence = sequence;
        this.sendWhen = sendWhen;
        this.predicate = function1;
    }

    public /* synthetic */ FilteringSequence(com.android.server.permission.jarjar.kotlin.sequences.Sequence sequence, boolean z, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 function1, int i, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(sequence, (i & 2) != 0 ? true : z, function1);
    }

    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.sequences.FilteringSequence$iterator$1, reason: invalid class name */
    /* JADX INFO: compiled from: Sequences.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000#\n\u0000\n\u0002\u0010(\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\t\u0010\u0013\u001a\u00020\u0014H\u0096\u0002J\u000e\u0010\u0015\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0007R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0004R\u001e\u0010\u0005\u001a\u0004\u0018\u00018\u0000X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\n\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010¨\u0006\u0016"}, d2 = {"com/android/server/permission/jarjar/kotlin/sequences/FilteringSequence$iterator$1", "", "iterator", "getIterator", "()Ljava/util/Iterator;", "nextItem", "getNextItem", "()Ljava/lang/Object;", "setNextItem", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "nextState", "", "getNextState", "()I", "setNextState", "(I)V", "calcNext", "", "hasNext", "", "next", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 implements java.util.Iterator<T>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
        private final java.util.Iterator<T> iterator;
        private T nextItem;
        private int nextState = -1;
        final /* synthetic */ com.android.server.permission.jarjar.kotlin.sequences.FilteringSequence<T> this$0;

        @Override // java.util.Iterator
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        AnonymousClass1(com.android.server.permission.jarjar.kotlin.sequences.FilteringSequence<T> filteringSequence) {
            this.this$0 = filteringSequence;
            this.iterator = ((com.android.server.permission.jarjar.kotlin.sequences.FilteringSequence) filteringSequence).sequence.iterator();
        }

        public final java.util.Iterator<T> getIterator() {
            return this.iterator;
        }

        public final int getNextState() {
            return this.nextState;
        }

        public final void setNextState(int i) {
            this.nextState = i;
        }

        public final T getNextItem() {
            return this.nextItem;
        }

        public final void setNextItem(T t) {
            this.nextItem = t;
        }

        private final void calcNext() {
            while (this.iterator.hasNext()) {
                T next = this.iterator.next();
                if (((java.lang.Boolean) ((com.android.server.permission.jarjar.kotlin.sequences.FilteringSequence) this.this$0).predicate.invoke(next)).booleanValue() == ((com.android.server.permission.jarjar.kotlin.sequences.FilteringSequence) this.this$0).sendWhen) {
                    this.nextItem = next;
                    this.nextState = 1;
                    return;
                }
            }
            this.nextState = 0;
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.nextState == -1) {
                calcNext();
            }
            if (this.nextState == 0) {
                throw new java.util.NoSuchElementException();
            }
            T t = this.nextItem;
            this.nextItem = null;
            this.nextState = -1;
            return t;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextState == -1) {
                calcNext();
            }
            return this.nextState == 1;
        }
    }

    @Override // com.android.server.permission.jarjar.kotlin.sequences.Sequence
    public java.util.Iterator<T> iterator() {
        return new com.android.server.permission.jarjar.kotlin.sequences.FilteringSequence.AnonymousClass1(this);
    }
}
