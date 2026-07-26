package com.android.server.permission.jarjar.kotlin.random;

/* JADX INFO: compiled from: Random.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\b'\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H&J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0016J$\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\u00042\b\b\u0002\u0010\f\u001a\u00020\u0004H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u0004H\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0004H\u0016J\u0010\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\u0018\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0016H\u0016J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0011\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0016H\u0016¨\u0006\u0018"}, d2 = {"Lkotlin/random/Random;", "", "()V", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "fromIndex", "toIndex", "size", "nextDouble", "", "until", "from", "nextFloat", "", "nextInt", "nextLong", "", "Default", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class Random {
    public static final com.android.server.permission.jarjar.kotlin.random.Random.Default Default = new com.android.server.permission.jarjar.kotlin.random.Random.Default(null);
    private static final com.android.server.permission.jarjar.kotlin.random.Random defaultRandom = com.android.server.permission.jarjar.kotlin.internal.PlatformImplementationsKt.IMPLEMENTATIONS.defaultPlatformRandom();

    public abstract int nextBits(int i);

    public int nextInt() {
        return nextBits(32);
    }

    public int nextInt(int until) {
        return nextInt(0, until);
    }

    public int nextInt(int from, int until) {
        int bits;
        int v;
        int bitCount;
        int rnd;
        boolean z;
        com.android.server.permission.jarjar.kotlin.random.RandomKt.checkRangeBounds(from, until);
        int n = until - from;
        if (n <= 0 && n != Integer.MIN_VALUE) {
            do {
                rnd = nextInt();
                z = false;
                if (from <= rnd && rnd < until) {
                    z = true;
                }
            } while (!z);
            return rnd;
        }
        if (((-n) & n) == n) {
            int bitCount2 = com.android.server.permission.jarjar.kotlin.random.RandomKt.fastLog2(n);
            bitCount = nextBits(bitCount2);
        } else {
            do {
                bits = nextInt() >>> 1;
                v = bits % n;
            } while ((bits - v) + (n - 1) < 0);
            bitCount = v;
        }
        return from + bitCount;
    }

    public long nextLong() {
        return (((long) nextInt()) << 32) + ((long) nextInt());
    }

    public long nextLong(long until) {
        return nextLong(0L, until);
    }

    public long nextLong(long from, long until) {
        long rnd;
        boolean z;
        long bits;
        long v;
        long rnd2;
        long jNextInt;
        com.android.server.permission.jarjar.kotlin.random.RandomKt.checkRangeBounds(from, until);
        long n = until - from;
        if (n <= 0) {
            do {
                rnd = nextLong();
                z = false;
                if (from <= rnd && rnd < until) {
                    z = true;
                }
            } while (!z);
            return rnd;
        }
        if (((-n) & n) == n) {
            int nLow = (int) n;
            int nHigh = (int) (n >>> 32);
            if (nLow != 0) {
                int bitCount = com.android.server.permission.jarjar.kotlin.random.RandomKt.fastLog2(nLow);
                jNextInt = 4294967295L & ((long) nextBits(bitCount));
            } else if (nHigh == 1) {
                jNextInt = 4294967295L & ((long) nextInt());
            } else {
                int bitCount2 = com.android.server.permission.jarjar.kotlin.random.RandomKt.fastLog2(nHigh);
                jNextInt = (4294967295L & ((long) nextInt())) + (((long) nextBits(bitCount2)) << 32);
            }
            rnd2 = jNextInt;
        } else {
            do {
                bits = nextLong() >>> 1;
                v = bits % n;
            } while ((bits - v) + (n - 1) < 0);
            rnd2 = v;
        }
        return from + rnd2;
    }

    public boolean nextBoolean() {
        return nextBits(1) != 0;
    }

    public double nextDouble() {
        return com.android.server.permission.jarjar.kotlin.random.PlatformRandomKt.doubleFromParts(nextBits(26), nextBits(27));
    }

    public double nextDouble(double until) {
        return nextDouble(0.0d, until);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x003e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public double nextDouble(double r9, double r11) {
        /*
            r8 = this;
            com.android.server.permission.jarjar.kotlin.random.RandomKt.checkRangeBounds(r9, r11)
            double r0 = r11 - r9
            boolean r2 = java.lang.Double.isInfinite(r0)
            if (r2 == 0) goto L3e
            boolean r2 = java.lang.Double.isInfinite(r9)
            r3 = 1
            r4 = 0
            if (r2 != 0) goto L1b
            boolean r2 = java.lang.Double.isNaN(r9)
            if (r2 != 0) goto L1b
            r2 = r3
            goto L1c
        L1b:
            r2 = r4
        L1c:
            if (r2 == 0) goto L3e
            boolean r2 = java.lang.Double.isInfinite(r11)
            if (r2 != 0) goto L2b
            boolean r2 = java.lang.Double.isNaN(r11)
            if (r2 != 0) goto L2b
            goto L2c
        L2b:
            r3 = r4
        L2c:
            if (r3 == 0) goto L3e
            double r2 = r8.nextDouble()
            r4 = 2
            double r4 = (double) r4
            double r6 = r11 / r4
            double r4 = r9 / r4
            double r6 = r6 - r4
            double r2 = r2 * r6
            double r4 = r9 + r2
            double r4 = r4 + r2
            goto L45
        L3e:
            double r2 = r8.nextDouble()
            double r2 = r2 * r0
            double r4 = r9 + r2
        L45:
            r2 = r4
            int r4 = (r2 > r11 ? 1 : (r2 == r11 ? 0 : -1))
            if (r4 < 0) goto L51
            r4 = -4503599627370496(0xfff0000000000000, double:-Infinity)
            double r4 = java.lang.Math.nextAfter(r11, r4)
            goto L52
        L51:
            r4 = r2
        L52:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.jarjar.kotlin.random.Random.nextDouble(double, double):double");
    }

    public float nextFloat() {
        return nextBits(24) / 1.6777216E7f;
    }

    public static /* synthetic */ byte[] nextBytes$default(com.android.server.permission.jarjar.kotlin.random.Random random, byte[] bArr, int i, int i2, int i3, java.lang.Object obj) {
        if (obj != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: nextBytes");
        }
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = bArr.length;
        }
        return random.nextBytes(bArr, i, i2);
    }

    public byte[] nextBytes(byte[] array, int fromIndex, int toIndex) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        if (!(new com.android.server.permission.jarjar.kotlin.ranges.IntRange(0, array.length).contains(fromIndex) && new com.android.server.permission.jarjar.kotlin.ranges.IntRange(0, array.length).contains(toIndex))) {
            throw new java.lang.IllegalArgumentException(("fromIndex (" + fromIndex + ") or toIndex (" + toIndex + ") are out of range: 0.." + array.length + '.').toString());
        }
        if (!(fromIndex <= toIndex)) {
            throw new java.lang.IllegalArgumentException(("fromIndex (" + fromIndex + ") must be not greater than toIndex (" + toIndex + ").").toString());
        }
        int steps = (toIndex - fromIndex) / 4;
        int position = fromIndex;
        for (int i = 0; i < steps; i++) {
            int v = nextInt();
            array[position] = (byte) v;
            array[position + 1] = (byte) (v >>> 8);
            array[position + 2] = (byte) (v >>> 16);
            array[position + 3] = (byte) (v >>> 24);
            position += 4;
        }
        int remainder = toIndex - position;
        int vr = nextBits(remainder * 8);
        for (int i2 = 0; i2 < remainder; i2++) {
            array[position + i2] = (byte) (vr >>> (i2 * 8));
        }
        return array;
    }

    public byte[] nextBytes(byte[] array) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        return nextBytes(array, 0, array.length);
    }

    public byte[] nextBytes(int size) {
        return nextBytes(new byte[size]);
    }

    /* JADX INFO: compiled from: Random.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003:\u0001\u001cB\u0007\b\u0002¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0016J\b\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0016J \u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0007H\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u0007H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0016J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0007H\u0016J\u0010\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0007H\u0016J\u0018\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0007H\u0016J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0013\u001a\u00020\u0019H\u0016J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0014\u001a\u00020\u00192\u0006\u0010\u0013\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0002R\u000e\u0010\u0005\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lkotlin/random/Random$Default;", "Lkotlin/random/Random;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "defaultRandom", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "fromIndex", "toIndex", "size", "nextDouble", "", "until", "from", "nextFloat", "", "nextInt", "nextLong", "", "writeReplace", "", "Serialized", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Default extends com.android.server.permission.jarjar.kotlin.random.Random implements java.io.Serializable {
        public /* synthetic */ Default(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Default() {
        }

        /* JADX INFO: compiled from: Random.kt */
        @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0000\bÂ\u0002\u0018\u00002\u00060\u0001j\u0002`\u0002B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\b\u0010\u0006\u001a\u00020\u0007H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lkotlin/random/Random$Default$Serialized;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "serialVersionUID", "", "readResolve", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
        private static final class Serialized implements java.io.Serializable {
            public static final com.android.server.permission.jarjar.kotlin.random.Random.Default.Serialized INSTANCE = new com.android.server.permission.jarjar.kotlin.random.Random.Default.Serialized();
            private static final long serialVersionUID = 0;

            private Serialized() {
            }

            private final java.lang.Object readResolve() {
                return com.android.server.permission.jarjar.kotlin.random.Random.Default;
            }
        }

        private final java.lang.Object writeReplace() {
            return com.android.server.permission.jarjar.kotlin.random.Random.Default.Serialized.INSTANCE;
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public int nextBits(int bitCount) {
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextBits(bitCount);
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public int nextInt() {
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextInt();
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public int nextInt(int until) {
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextInt(until);
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public int nextInt(int from, int until) {
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextInt(from, until);
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public long nextLong() {
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextLong();
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public long nextLong(long until) {
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextLong(until);
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public long nextLong(long from, long until) {
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextLong(from, until);
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public boolean nextBoolean() {
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextBoolean();
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public double nextDouble() {
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextDouble();
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public double nextDouble(double until) {
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextDouble(until);
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public double nextDouble(double from, double until) {
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextDouble(from, until);
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public float nextFloat() {
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextFloat();
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public byte[] nextBytes(byte[] array) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextBytes(array);
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public byte[] nextBytes(int size) {
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextBytes(size);
        }

        @Override // com.android.server.permission.jarjar.kotlin.random.Random
        public byte[] nextBytes(byte[] array, int fromIndex, int toIndex) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
            return com.android.server.permission.jarjar.kotlin.random.Random.defaultRandom.nextBytes(array, fromIndex, toIndex);
        }
    }
}
