package com.android.server.permission.jarjar.kotlin.random;

/* JADX INFO: compiled from: URandom.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001f\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a\u001f\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0000¢\u0006\u0004\b\t\u0010\n\u001a\u0019\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007¢\u0006\u0002\u0010\u0010\u001a\u001b\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\fH\u0007¢\u0006\u0004\b\u0012\u0010\u0013\u001a/\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0014\u001a\u00020\u000f2\b\b\u0002\u0010\u0015\u001a\u00020\u000fH\u0007¢\u0006\u0004\b\u0016\u0010\u0017\u001a\u0011\u0010\u0018\u001a\u00020\u0003*\u00020\rH\u0007¢\u0006\u0002\u0010\u0019\u001a\u001b\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0003H\u0007¢\u0006\u0004\b\u001a\u0010\u001b\u001a#\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0007¢\u0006\u0004\b\u001c\u0010\u001d\u001a\u0019\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u001e\u001a\u00020\u001fH\u0007¢\u0006\u0002\u0010 \u001a\u0011\u0010!\u001a\u00020\b*\u00020\rH\u0007¢\u0006\u0002\u0010\"\u001a\u001b\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0004\u001a\u00020\bH\u0007¢\u0006\u0004\b#\u0010$\u001a#\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0007¢\u0006\u0004\b%\u0010&\u001a\u0019\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u001e\u001a\u00020'H\u0007¢\u0006\u0002\u0010(¨\u0006)"}, d2 = {"checkUIntRangeBounds", "", "from", "Lkotlin/UInt;", "until", "checkUIntRangeBounds-J1ME1BU", "(II)V", "checkULongRangeBounds", "Lkotlin/ULong;", "checkULongRangeBounds-eb3DHEI", "(JJ)V", "nextUBytes", "Lkotlin/UByteArray;", "Lkotlin/random/Random;", "size", "", "(Lkotlin/random/Random;I)[B", "array", "nextUBytes-EVgfTAA", "(Lkotlin/random/Random;[B)[B", "fromIndex", "toIndex", "nextUBytes-Wvrt4B4", "(Lkotlin/random/Random;[BII)[B", "nextUInt", "(Lkotlin/random/Random;)I", "nextUInt-qCasIEU", "(Lkotlin/random/Random;I)I", "nextUInt-a8DCA5k", "(Lkotlin/random/Random;II)I", "range", "Lkotlin/ranges/UIntRange;", "(Lkotlin/random/Random;Lkotlin/ranges/UIntRange;)I", "nextULong", "(Lkotlin/random/Random;)J", "nextULong-V1Xi4fY", "(Lkotlin/random/Random;J)J", "nextULong-jmpaW-c", "(Lkotlin/random/Random;JJ)J", "Lkotlin/ranges/ULongRange;", "(Lkotlin/random/Random;Lkotlin/ranges/ULongRange;)J", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class URandomKt {
    public static final int nextUInt(com.android.server.permission.jarjar.kotlin.random.Random $this$nextUInt) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextUInt, "<this>");
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl($this$nextUInt.nextInt());
    }

    /* JADX INFO: renamed from: nextUInt-qCasIEU, reason: not valid java name */
    public static final int m7309nextUIntqCasIEU(com.android.server.permission.jarjar.kotlin.random.Random $this$nextUInt_u2dqCasIEU, int until) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextUInt_u2dqCasIEU, "$this$nextUInt");
        return m7308nextUInta8DCA5k($this$nextUInt_u2dqCasIEU, 0, until);
    }

    /* JADX INFO: renamed from: nextUInt-a8DCA5k, reason: not valid java name */
    public static final int m7308nextUInta8DCA5k(com.android.server.permission.jarjar.kotlin.random.Random $this$nextUInt_u2da8DCA5k, int from, int until) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextUInt_u2da8DCA5k, "$this$nextUInt");
        m7303checkUIntRangeBoundsJ1ME1BU(from, until);
        int signedFrom = from ^ Integer.MIN_VALUE;
        int signedUntil = until ^ Integer.MIN_VALUE;
        int signedResult = Integer.MIN_VALUE ^ $this$nextUInt_u2da8DCA5k.nextInt(signedFrom, signedUntil);
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(signedResult);
    }

    public static final int nextUInt(com.android.server.permission.jarjar.kotlin.random.Random $this$nextUInt, com.android.server.permission.jarjar.kotlin.ranges.UIntRange range) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextUInt, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        if (range.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot get random in empty range: " + range);
        }
        return java.lang.Integer.compareUnsigned(range.m7313getLastpVg5ArA(), -1) < 0 ? m7308nextUInta8DCA5k($this$nextUInt, range.m7312getFirstpVg5ArA(), com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(range.m7313getLastpVg5ArA() + 1)) : java.lang.Integer.compareUnsigned(range.m7312getFirstpVg5ArA(), 0) > 0 ? com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(m7308nextUInta8DCA5k($this$nextUInt, com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(range.m7312getFirstpVg5ArA() - 1), range.m7313getLastpVg5ArA()) + 1) : nextUInt($this$nextUInt);
    }

    public static final long nextULong(com.android.server.permission.jarjar.kotlin.random.Random $this$nextULong) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextULong, "<this>");
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl($this$nextULong.nextLong());
    }

    /* JADX INFO: renamed from: nextULong-V1Xi4fY, reason: not valid java name */
    public static final long m7310nextULongV1Xi4fY(com.android.server.permission.jarjar.kotlin.random.Random $this$nextULong_u2dV1Xi4fY, long until) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextULong_u2dV1Xi4fY, "$this$nextULong");
        return m7311nextULongjmpaWc($this$nextULong_u2dV1Xi4fY, 0L, until);
    }

    /* JADX INFO: renamed from: nextULong-jmpaW-c, reason: not valid java name */
    public static final long m7311nextULongjmpaWc(com.android.server.permission.jarjar.kotlin.random.Random $this$nextULong_u2djmpaW_u2dc, long from, long until) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextULong_u2djmpaW_u2dc, "$this$nextULong");
        m7304checkULongRangeBoundseb3DHEI(from, until);
        long signedFrom = from ^ Long.MIN_VALUE;
        long signedUntil = until ^ Long.MIN_VALUE;
        long signedResult = Long.MIN_VALUE ^ $this$nextULong_u2djmpaW_u2dc.nextLong(signedFrom, signedUntil);
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(signedResult);
    }

    public static final long nextULong(com.android.server.permission.jarjar.kotlin.random.Random $this$nextULong, com.android.server.permission.jarjar.kotlin.ranges.ULongRange range) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextULong, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        if (range.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot get random in empty range: " + range);
        }
        if (java.lang.Long.compareUnsigned(range.m7322getLastsVKNKU(), -1L) < 0) {
            return m7311nextULongjmpaWc($this$nextULong, range.m7321getFirstsVKNKU(), com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(range.m7322getLastsVKNKU() + com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) 1) & 4294967295L)));
        }
        if (java.lang.Long.compareUnsigned(range.m7321getFirstsVKNKU(), 0L) <= 0) {
            return nextULong($this$nextULong);
        }
        long j = ((long) 1) & 4294967295L;
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(m7311nextULongjmpaWc($this$nextULong, com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(range.m7321getFirstsVKNKU() - com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(j)), range.m7322getLastsVKNKU()) + com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(j));
    }

    /* JADX INFO: renamed from: nextUBytes-EVgfTAA, reason: not valid java name */
    public static final byte[] m7305nextUBytesEVgfTAA(com.android.server.permission.jarjar.kotlin.random.Random $this$nextUBytes_u2dEVgfTAA, byte[] array) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextUBytes_u2dEVgfTAA, "$this$nextUBytes");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        $this$nextUBytes_u2dEVgfTAA.nextBytes(array);
        return array;
    }

    public static final byte[] nextUBytes(com.android.server.permission.jarjar.kotlin.random.Random $this$nextUBytes, int size) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextUBytes, "<this>");
        return com.android.server.permission.jarjar.kotlin.UByteArray.m6160constructorimpl($this$nextUBytes.nextBytes(size));
    }

    /* JADX INFO: renamed from: nextUBytes-Wvrt4B4$default, reason: not valid java name */
    public static /* synthetic */ byte[] m7307nextUBytesWvrt4B4$default(com.android.server.permission.jarjar.kotlin.random.Random random, byte[] bArr, int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = com.android.server.permission.jarjar.kotlin.UByteArray.m6166getSizeimpl(bArr);
        }
        return m7306nextUBytesWvrt4B4(random, bArr, i, i2);
    }

    /* JADX INFO: renamed from: nextUBytes-Wvrt4B4, reason: not valid java name */
    public static final byte[] m7306nextUBytesWvrt4B4(com.android.server.permission.jarjar.kotlin.random.Random $this$nextUBytes_u2dWvrt4B4, byte[] array, int fromIndex, int toIndex) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextUBytes_u2dWvrt4B4, "$this$nextUBytes");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        $this$nextUBytes_u2dWvrt4B4.nextBytes(array, fromIndex, toIndex);
        return array;
    }

    /* JADX INFO: renamed from: checkUIntRangeBounds-J1ME1BU, reason: not valid java name */
    public static final void m7303checkUIntRangeBoundsJ1ME1BU(int from, int until) {
        if (!(java.lang.Integer.compareUnsigned(until, from) > 0)) {
            throw new java.lang.IllegalArgumentException(com.android.server.permission.jarjar.kotlin.random.RandomKt.boundsErrorMessage(com.android.server.permission.jarjar.kotlin.UInt.m6178boximpl(from), com.android.server.permission.jarjar.kotlin.UInt.m6178boximpl(until)).toString());
        }
    }

    /* JADX INFO: renamed from: checkULongRangeBounds-eb3DHEI, reason: not valid java name */
    public static final void m7304checkULongRangeBoundseb3DHEI(long from, long until) {
        if (!(java.lang.Long.compareUnsigned(until, from) > 0)) {
            throw new java.lang.IllegalArgumentException(com.android.server.permission.jarjar.kotlin.random.RandomKt.boundsErrorMessage(com.android.server.permission.jarjar.kotlin.ULong.m6257boximpl(from), com.android.server.permission.jarjar.kotlin.ULong.m6257boximpl(until)).toString());
        }
    }
}
