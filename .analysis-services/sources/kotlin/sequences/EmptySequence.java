package kotlin.sequences;

/* JADX INFO: compiled from: Sequences.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010(\n\u0002\b\u0002\bÂ\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\b\u0012\u0004\u0012\u00020\u00020\u0003B\u0007\b\u0002¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00020\tH\u0096\u0002J\u0010\u0010\n\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\u000b"}, d2 = {"Lkotlin/sequences/EmptySequence;", "Lkotlin/sequences/Sequence;", "", "Lkotlin/sequences/DropTakeSequence;", "()V", "drop", "n", "", "iterator", "", "take", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class EmptySequence implements kotlin.sequences.Sequence, kotlin.sequences.DropTakeSequence {
    public static final kotlin.sequences.EmptySequence INSTANCE = new kotlin.sequences.EmptySequence();

    private EmptySequence() {
    }

    @Override // kotlin.sequences.Sequence
    public java.util.Iterator iterator() {
        return kotlin.collections.EmptyIterator.INSTANCE;
    }

    @Override // kotlin.sequences.DropTakeSequence
    public kotlin.sequences.EmptySequence drop(int n) {
        return INSTANCE;
    }

    @Override // kotlin.sequences.DropTakeSequence
    public kotlin.sequences.EmptySequence take(int n) {
        return INSTANCE;
    }
}
