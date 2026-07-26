package kotlin.collections;

/* JADX INFO: Add missing generic type declarations: [T] */
/* JADX INFO: compiled from: Iterables.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0011\n\u0000\n\u0002\u0010\u001c\n\u0000\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u000f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096\u0002¨\u0006\u0004¸\u0006\u0000"}, d2 = {"kotlin/collections/CollectionsKt__IterablesKt$Iterable$1", "", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ArraysKt___ArraysKt$asIterable$$inlined$Iterable$1<T> implements java.lang.Iterable<T>, kotlin.jvm.internal.markers.KMappedMarker {
    final /* synthetic */ java.lang.Object[] $this_asIterable$inlined;

    public ArraysKt___ArraysKt$asIterable$$inlined$Iterable$1(java.lang.Object[] objArr) {
        this.$this_asIterable$inlined = objArr;
    }

    @Override // java.lang.Iterable
    public java.util.Iterator<T> iterator() {
        return kotlin.jvm.internal.ArrayIteratorKt.iterator(this.$this_asIterable$inlined);
    }
}
