package kotlin.random;

/* JADX INFO: compiled from: URandom.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001f\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a\u001f\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0000¢\u0006\u0004\b\t\u0010\n\u001a\u0019\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007¢\u0006\u0002\u0010\u0010\u001a\u001b\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\fH\u0007¢\u0006\u0004\b\u0012\u0010\u0013\u001a/\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0014\u001a\u00020\u000f2\b\b\u0002\u0010\u0015\u001a\u00020\u000fH\u0007¢\u0006\u0004\b\u0016\u0010\u0017\u001a\u0011\u0010\u0018\u001a\u00020\u0003*\u00020\rH\u0007¢\u0006\u0002\u0010\u0019\u001a\u001b\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0003H\u0007¢\u0006\u0004\b\u001a\u0010\u001b\u001a#\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0007¢\u0006\u0004\b\u001c\u0010\u001d\u001a\u0019\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u001e\u001a\u00020\u001fH\u0007¢\u0006\u0002\u0010 \u001a\u0011\u0010!\u001a\u00020\b*\u00020\rH\u0007¢\u0006\u0002\u0010\"\u001a\u001b\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0004\u001a\u00020\bH\u0007¢\u0006\u0004\b#\u0010$\u001a#\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0007¢\u0006\u0004\b%\u0010&\u001a\u0019\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u001e\u001a\u00020'H\u0007¢\u0006\u0002\u0010(¨\u0006)"}, d2 = {"checkUIntRangeBounds", "", "from", "Lkotlin/UInt;", "until", "checkUIntRangeBounds-J1ME1BU", "(II)V", "checkULongRangeBounds", "Lkotlin/ULong;", "checkULongRangeBounds-eb3DHEI", "(JJ)V", "nextUBytes", "Lkotlin/UByteArray;", "Lkotlin/random/Random;", "size", "", "(Lkotlin/random/Random;I)[B", "array", "nextUBytes-EVgfTAA", "(Lkotlin/random/Random;[B)[B", "fromIndex", "toIndex", "nextUBytes-Wvrt4B4", "(Lkotlin/random/Random;[BII)[B", "nextUInt", "(Lkotlin/random/Random;)I", "nextUInt-qCasIEU", "(Lkotlin/random/Random;I)I", "nextUInt-a8DCA5k", "(Lkotlin/random/Random;II)I", "range", "Lkotlin/ranges/UIntRange;", "(Lkotlin/random/Random;Lkotlin/ranges/UIntRange;)I", "nextULong", "(Lkotlin/random/Random;)J", "nextULong-V1Xi4fY", "(Lkotlin/random/Random;J)J", "nextULong-jmpaW-c", "(Lkotlin/random/Random;JJ)J", "Lkotlin/ranges/ULongRange;", "(Lkotlin/random/Random;Lkotlin/ranges/ULongRange;)J", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class URandomKt {
    public static final int nextUInt(kotlin.random.Random $this$nextUInt) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextUInt, "<this>");
        return kotlin.UInt.m11402constructorimpl($this$nextUInt.nextInt());
    }

    /* JADX INFO: renamed from: nextUInt-qCasIEU, reason: not valid java name */
    public static final int m12527nextUIntqCasIEU(kotlin.random.Random nextUInt, int until) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(nextUInt, "$this$nextUInt");
        return m12526nextUInta8DCA5k(nextUInt, 0, until);
    }

    /* JADX INFO: renamed from: nextUInt-a8DCA5k, reason: not valid java name */
    public static final int m12526nextUInta8DCA5k(kotlin.random.Random nextUInt, int from, int until) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(nextUInt, "$this$nextUInt");
        m12521checkUIntRangeBoundsJ1ME1BU(from, until);
        int signedFrom = from ^ Integer.MIN_VALUE;
        int signedUntil = until ^ Integer.MIN_VALUE;
        int signedResult = Integer.MIN_VALUE ^ nextUInt.nextInt(signedFrom, signedUntil);
        return kotlin.UInt.m11402constructorimpl(signedResult);
    }

    public static final int nextUInt(kotlin.random.Random $this$nextUInt, kotlin.ranges.UIntRange range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextUInt, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        if (range.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot get random in empty range: " + range);
        }
        return java.lang.Integer.compareUnsigned(range.getLast(), -1) < 0 ? m12526nextUInta8DCA5k($this$nextUInt, range.getFirst(), kotlin.UInt.m11402constructorimpl(range.getLast() + 1)) : java.lang.Integer.compareUnsigned(range.getFirst(), 0) > 0 ? kotlin.UInt.m11402constructorimpl(m12526nextUInta8DCA5k($this$nextUInt, kotlin.UInt.m11402constructorimpl(range.getFirst() - 1), range.getLast()) + 1) : nextUInt($this$nextUInt);
    }

    public static final long nextULong(kotlin.random.Random $this$nextULong) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextULong, "<this>");
        return kotlin.ULong.m11481constructorimpl($this$nextULong.nextLong());
    }

    /* JADX INFO: renamed from: nextULong-V1Xi4fY, reason: not valid java name */
    public static final long m12528nextULongV1Xi4fY(kotlin.random.Random nextULong, long until) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(nextULong, "$this$nextULong");
        return m12529nextULongjmpaWc(nextULong, 0L, until);
    }

    /* JADX INFO: renamed from: nextULong-jmpaW-c, reason: not valid java name */
    public static final long m12529nextULongjmpaWc(kotlin.random.Random nextULong, long from, long until) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(nextULong, "$this$nextULong");
        m12522checkULongRangeBoundseb3DHEI(from, until);
        long signedFrom = from ^ Long.MIN_VALUE;
        long signedUntil = until ^ Long.MIN_VALUE;
        long signedResult = Long.MIN_VALUE ^ nextULong.nextLong(signedFrom, signedUntil);
        return kotlin.ULong.m11481constructorimpl(signedResult);
    }

    public static final long nextULong(kotlin.random.Random $this$nextULong, kotlin.ranges.ULongRange range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextULong, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        if (range.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot get random in empty range: " + range);
        }
        if (java.lang.Long.compareUnsigned(range.getLast(), -1L) < 0) {
            return m12529nextULongjmpaWc($this$nextULong, range.getFirst(), kotlin.ULong.m11481constructorimpl(range.getLast() + kotlin.ULong.m11481constructorimpl(((long) 1) & 4294967295L)));
        }
        if (java.lang.Long.compareUnsigned(range.getFirst(), 0L) <= 0) {
            return nextULong($this$nextULong);
        }
        long j = ((long) 1) & 4294967295L;
        return kotlin.ULong.m11481constructorimpl(m12529nextULongjmpaWc($this$nextULong, kotlin.ULong.m11481constructorimpl(range.getFirst() - kotlin.ULong.m11481constructorimpl(j)), range.getLast()) + kotlin.ULong.m11481constructorimpl(j));
    }

    /* JADX INFO: renamed from: nextUBytes-EVgfTAA, reason: not valid java name */
    public static final byte[] m12523nextUBytesEVgfTAA(kotlin.random.Random nextUBytes, byte[] array) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(nextUBytes, "$this$nextUBytes");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        nextUBytes.nextBytes(array);
        return array;
    }

    public static final byte[] nextUBytes(kotlin.random.Random $this$nextUBytes, int size) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextUBytes, "<this>");
        return kotlin.UByteArray.m11378constructorimpl($this$nextUBytes.nextBytes(size));
    }

    /* JADX INFO: renamed from: nextUBytes-Wvrt4B4$default, reason: not valid java name */
    public static /* synthetic */ byte[] m12525nextUBytesWvrt4B4$default(kotlin.random.Random random, byte[] bArr, int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = kotlin.UByteArray.m11384getSizeimpl(bArr);
        }
        return m12524nextUBytesWvrt4B4(random, bArr, i, i2);
    }

    /* JADX INFO: renamed from: nextUBytes-Wvrt4B4, reason: not valid java name */
    public static final byte[] m12524nextUBytesWvrt4B4(kotlin.random.Random nextUBytes, byte[] array, int fromIndex, int toIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(nextUBytes, "$this$nextUBytes");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        nextUBytes.nextBytes(array, fromIndex, toIndex);
        return array;
    }

    /* JADX INFO: renamed from: checkUIntRangeBounds-J1ME1BU, reason: not valid java name */
    public static final void m12521checkUIntRangeBoundsJ1ME1BU(int from, int until) {
        if (!(java.lang.Integer.compareUnsigned(until, from) > 0)) {
            throw new java.lang.IllegalArgumentException(kotlin.random.RandomKt.boundsErrorMessage(kotlin.UInt.m11396boximpl(from), kotlin.UInt.m11396boximpl(until)).toString());
        }
    }

    /* JADX INFO: renamed from: checkULongRangeBounds-eb3DHEI, reason: not valid java name */
    public static final void m12522checkULongRangeBoundseb3DHEI(long from, long until) {
        if (!(java.lang.Long.compareUnsigned(until, from) > 0)) {
            throw new java.lang.IllegalArgumentException(kotlin.random.RandomKt.boundsErrorMessage(kotlin.ULong.m11475boximpl(from), kotlin.ULong.m11475boximpl(until)).toString());
        }
    }
}
