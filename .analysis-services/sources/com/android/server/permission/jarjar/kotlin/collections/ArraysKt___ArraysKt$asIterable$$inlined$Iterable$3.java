package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: Iterables.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0011\n\u0000\n\u0002\u0010\u001c\n\u0000\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u000f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096\u0002¨\u0006\u0004¸\u0006\u0000"}, d2 = {"com/android/server/permission/jarjar/kotlin/collections/CollectionsKt__IterablesKt$Iterable$1", "", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ArraysKt___ArraysKt$asIterable$$inlined$Iterable$3 implements java.lang.Iterable<java.lang.Short>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
    final /* synthetic */ short[] $this_asIterable$inlined;

    public ArraysKt___ArraysKt$asIterable$$inlined$Iterable$3(short[] sArr) {
        this.$this_asIterable$inlined = sArr;
    }

    @Override // java.lang.Iterable
    public java.util.Iterator<java.lang.Short> iterator() {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.ArrayIteratorsKt.iterator(this.$this_asIterable$inlined);
    }
}
