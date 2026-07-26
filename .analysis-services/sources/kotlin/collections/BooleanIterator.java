package kotlin.collections;

/* JADX INFO: compiled from: PrimitiveIterators.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0010\u000b\n\u0002\b\u0005\b&\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0002H&¨\u0006\u0007"}, d2 = {"Lkotlin/collections/BooleanIterator;", "", "", "()V", "next", "()Ljava/lang/Boolean;", "nextBoolean", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class BooleanIterator implements java.util.Iterator<java.lang.Boolean>, kotlin.jvm.internal.markers.KMappedMarker {
    public abstract boolean nextBoolean();

    @Override // java.util.Iterator
    public void remove() {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Iterator
    public /* bridge */ /* synthetic */ java.lang.Boolean next() {
        return java.lang.Boolean.valueOf(nextBoolean());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public final java.lang.Boolean next() {
        return java.lang.Boolean.valueOf(nextBoolean());
    }
}
