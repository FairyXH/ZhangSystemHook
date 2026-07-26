package kotlin.sequences;

/* JADX INFO: compiled from: _USequences.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0019\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0007¢\u0006\u0004\b\u0004\u0010\u0005\u001a\u0019\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u0007¢\u0006\u0004\b\u0006\u0010\u0005\u001a\u0019\u0010\u0000\u001a\u00020\u0007*\b\u0012\u0004\u0012\u00020\u00070\u0002H\u0007¢\u0006\u0004\b\b\u0010\t\u001a\u0019\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\n0\u0002H\u0007¢\u0006\u0004\b\u000b\u0010\u0005¨\u0006\f"}, d2 = {"sum", "Lkotlin/UInt;", "Lkotlin/sequences/Sequence;", "Lkotlin/UByte;", "sumOfUByte", "(Lkotlin/sequences/Sequence;)I", "sumOfUInt", "Lkotlin/ULong;", "sumOfULong", "(Lkotlin/sequences/Sequence;)J", "Lkotlin/UShort;", "sumOfUShort", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/sequences/USequencesKt")
class USequencesKt___USequencesKt {
    public static final int sumOfUInt(kotlin.sequences.Sequence<kotlin.UInt> sequence) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        int sum = 0;
        java.util.Iterator<kotlin.UInt> it = sequence.iterator();
        while (it.hasNext()) {
            int element = it.next().getData();
            sum = kotlin.UInt.m11402constructorimpl(sum + element);
        }
        return sum;
    }

    public static final long sumOfULong(kotlin.sequences.Sequence<kotlin.ULong> sequence) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        long sum = 0;
        java.util.Iterator<kotlin.ULong> it = sequence.iterator();
        while (it.hasNext()) {
            long element = it.next().getData();
            sum = kotlin.ULong.m11481constructorimpl(sum + element);
        }
        return sum;
    }

    public static final int sumOfUByte(kotlin.sequences.Sequence<kotlin.UByte> sequence) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        int sum = 0;
        java.util.Iterator<kotlin.UByte> it = sequence.iterator();
        while (it.hasNext()) {
            byte element = it.next().getData();
            sum = kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(element & 255) + sum);
        }
        return sum;
    }

    public static final int sumOfUShort(kotlin.sequences.Sequence<kotlin.UShort> sequence) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        int sum = 0;
        java.util.Iterator<kotlin.UShort> it = sequence.iterator();
        while (it.hasNext()) {
            short element = it.next().getData();
            sum = kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(65535 & element) + sum);
        }
        return sum;
    }
}
