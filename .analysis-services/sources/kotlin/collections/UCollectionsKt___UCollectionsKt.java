package kotlin.collections;

/* JADX INFO: compiled from: _UCollections.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000F\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0019\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0007¢\u0006\u0004\b\u0004\u0010\u0005\u001a\u0019\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u0007¢\u0006\u0004\b\u0006\u0010\u0005\u001a\u0019\u0010\u0000\u001a\u00020\u0007*\b\u0012\u0004\u0012\u00020\u00070\u0002H\u0007¢\u0006\u0004\b\b\u0010\t\u001a\u0019\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\n0\u0002H\u0007¢\u0006\u0004\b\u000b\u0010\u0005\u001a\u0017\u0010\f\u001a\u00020\r*\b\u0012\u0004\u0012\u00020\u00030\u000eH\u0007¢\u0006\u0002\u0010\u000f\u001a\u0017\u0010\u0010\u001a\u00020\u0011*\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007¢\u0006\u0002\u0010\u0012\u001a\u0017\u0010\u0013\u001a\u00020\u0014*\b\u0012\u0004\u0012\u00020\u00070\u000eH\u0007¢\u0006\u0002\u0010\u0015\u001a\u0017\u0010\u0016\u001a\u00020\u0017*\b\u0012\u0004\u0012\u00020\n0\u000eH\u0007¢\u0006\u0002\u0010\u0018¨\u0006\u0019"}, d2 = {"sum", "Lkotlin/UInt;", "", "Lkotlin/UByte;", "sumOfUByte", "(Ljava/lang/Iterable;)I", "sumOfUInt", "Lkotlin/ULong;", "sumOfULong", "(Ljava/lang/Iterable;)J", "Lkotlin/UShort;", "sumOfUShort", "toUByteArray", "Lkotlin/UByteArray;", "", "(Ljava/util/Collection;)[B", "toUIntArray", "Lkotlin/UIntArray;", "(Ljava/util/Collection;)[I", "toULongArray", "Lkotlin/ULongArray;", "(Ljava/util/Collection;)[J", "toUShortArray", "Lkotlin/UShortArray;", "(Ljava/util/Collection;)[S", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/collections/UCollectionsKt")
class UCollectionsKt___UCollectionsKt {
    public static final byte[] toUByteArray(java.util.Collection<kotlin.UByte> collection) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        byte[] result = kotlin.UByteArray.m11377constructorimpl(collection.size());
        int index = 0;
        java.util.Iterator<kotlin.UByte> it = collection.iterator();
        while (it.hasNext()) {
            byte element = it.next().getData();
            kotlin.UByteArray.m11388setVurrAj0(result, index, element);
            index++;
        }
        return result;
    }

    public static final int[] toUIntArray(java.util.Collection<kotlin.UInt> collection) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        int[] result = kotlin.UIntArray.m11456constructorimpl(collection.size());
        int index = 0;
        java.util.Iterator<kotlin.UInt> it = collection.iterator();
        while (it.hasNext()) {
            int element = it.next().getData();
            kotlin.UIntArray.m11467setVXSXFK8(result, index, element);
            index++;
        }
        return result;
    }

    public static final long[] toULongArray(java.util.Collection<kotlin.ULong> collection) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        long[] result = kotlin.ULongArray.m11535constructorimpl(collection.size());
        int index = 0;
        java.util.Iterator<kotlin.ULong> it = collection.iterator();
        while (it.hasNext()) {
            long element = it.next().getData();
            kotlin.ULongArray.m11546setk8EXiF4(result, index, element);
            index++;
        }
        return result;
    }

    public static final short[] toUShortArray(java.util.Collection<kotlin.UShort> collection) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "<this>");
        short[] result = kotlin.UShortArray.m11640constructorimpl(collection.size());
        int index = 0;
        java.util.Iterator<kotlin.UShort> it = collection.iterator();
        while (it.hasNext()) {
            short element = it.next().getData();
            kotlin.UShortArray.m11651set01HTLdE(result, index, element);
            index++;
        }
        return result;
    }

    public static final int sumOfUInt(java.lang.Iterable<kotlin.UInt> iterable) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        int sum = 0;
        java.util.Iterator<kotlin.UInt> it = iterable.iterator();
        while (it.hasNext()) {
            int element = it.next().getData();
            sum = kotlin.UInt.m11402constructorimpl(sum + element);
        }
        return sum;
    }

    public static final long sumOfULong(java.lang.Iterable<kotlin.ULong> iterable) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        long sum = 0;
        java.util.Iterator<kotlin.ULong> it = iterable.iterator();
        while (it.hasNext()) {
            long element = it.next().getData();
            sum = kotlin.ULong.m11481constructorimpl(sum + element);
        }
        return sum;
    }

    public static final int sumOfUByte(java.lang.Iterable<kotlin.UByte> iterable) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        int sum = 0;
        java.util.Iterator<kotlin.UByte> it = iterable.iterator();
        while (it.hasNext()) {
            byte element = it.next().getData();
            sum = kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(element & 255) + sum);
        }
        return sum;
    }

    public static final int sumOfUShort(java.lang.Iterable<kotlin.UShort> iterable) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        int sum = 0;
        java.util.Iterator<kotlin.UShort> it = iterable.iterator();
        while (it.hasNext()) {
            short element = it.next().getData();
            sum = kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(65535 & element) + sum);
        }
        return sum;
    }
}
