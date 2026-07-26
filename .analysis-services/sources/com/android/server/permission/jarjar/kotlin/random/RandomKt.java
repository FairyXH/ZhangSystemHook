package com.android.server.permission.jarjar.kotlin.random;

/* JADX INFO: compiled from: Random.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000:\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0004H\u0007\u001a\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\fH\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0003H\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0000\u001a\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0003H\u0000\u001a\u0014\u0010\u000f\u001a\u00020\u0003*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0011H\u0007\u001a\u0014\u0010\u0012\u001a\u00020\u0004*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0013H\u0007\u001a\u0014\u0010\u0014\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0003H\u0000¨\u0006\u0016"}, d2 = {"Random", "Lkotlin/random/Random;", "seed", "", "", "boundsErrorMessage", "", "from", "", "until", "checkRangeBounds", "", "", "fastLog2", "value", "nextInt", "range", "Lkotlin/ranges/IntRange;", "nextLong", "Lkotlin/ranges/LongRange;", "takeUpperBits", "bitCount", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class RandomKt {
    public static final com.android.server.permission.jarjar.kotlin.random.Random Random(int seed) {
        return new com.android.server.permission.jarjar.kotlin.random.XorWowRandom(seed, seed >> 31);
    }

    public static final com.android.server.permission.jarjar.kotlin.random.Random Random(long seed) {
        return new com.android.server.permission.jarjar.kotlin.random.XorWowRandom((int) seed, (int) (seed >> 32));
    }

    public static final int nextInt(com.android.server.permission.jarjar.kotlin.random.Random $this$nextInt, com.android.server.permission.jarjar.kotlin.ranges.IntRange range) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextInt, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        if (range.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot get random in empty range: " + range);
        }
        return range.getLast() < Integer.MAX_VALUE ? $this$nextInt.nextInt(range.getFirst(), range.getLast() + 1) : range.getFirst() > Integer.MIN_VALUE ? $this$nextInt.nextInt(range.getFirst() - 1, range.getLast()) + 1 : $this$nextInt.nextInt();
    }

    public static final long nextLong(com.android.server.permission.jarjar.kotlin.random.Random $this$nextLong, com.android.server.permission.jarjar.kotlin.ranges.LongRange range) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nextLong, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        if (range.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot get random in empty range: " + range);
        }
        return range.getLast() < Long.MAX_VALUE ? $this$nextLong.nextLong(range.getFirst(), range.getLast() + 1) : range.getFirst() > Long.MIN_VALUE ? $this$nextLong.nextLong(range.getFirst() - 1, range.getLast()) + 1 : $this$nextLong.nextLong();
    }

    public static final int fastLog2(int value) {
        return 31 - java.lang.Integer.numberOfLeadingZeros(value);
    }

    public static final int takeUpperBits(int $this$takeUpperBits, int bitCount) {
        return ($this$takeUpperBits >>> (32 - bitCount)) & ((-bitCount) >> 31);
    }

    public static final void checkRangeBounds(int from, int until) {
        if (!(until > from)) {
            throw new java.lang.IllegalArgumentException(boundsErrorMessage(java.lang.Integer.valueOf(from), java.lang.Integer.valueOf(until)).toString());
        }
    }

    public static final void checkRangeBounds(long from, long until) {
        if (!(until > from)) {
            throw new java.lang.IllegalArgumentException(boundsErrorMessage(java.lang.Long.valueOf(from), java.lang.Long.valueOf(until)).toString());
        }
    }

    public static final void checkRangeBounds(double from, double until) {
        if (!(until > from)) {
            throw new java.lang.IllegalArgumentException(boundsErrorMessage(java.lang.Double.valueOf(from), java.lang.Double.valueOf(until)).toString());
        }
    }

    public static final java.lang.String boundsErrorMessage(java.lang.Object from, java.lang.Object until) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(from, "from");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(until, "until");
        return "Random range is empty: [" + from + ", " + until + ").";
    }
}
