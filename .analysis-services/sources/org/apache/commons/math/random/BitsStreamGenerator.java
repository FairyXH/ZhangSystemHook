package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public abstract class BitsStreamGenerator implements org.apache.commons.math.random.RandomGenerator {
    private double nextGaussian = Double.NaN;

    protected abstract int next(int i);

    @Override // org.apache.commons.math.random.RandomGenerator
    public abstract void setSeed(int i);

    @Override // org.apache.commons.math.random.RandomGenerator
    public abstract void setSeed(long j);

    @Override // org.apache.commons.math.random.RandomGenerator
    public abstract void setSeed(int[] iArr);

    @Override // org.apache.commons.math.random.RandomGenerator
    public boolean nextBoolean() {
        return next(1) != 0;
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public void nextBytes(byte[] bytes) {
        int i = 0;
        int iEnd = bytes.length - 3;
        while (i < iEnd) {
            int random = next(32);
            bytes[i] = (byte) (random & 255);
            bytes[i + 1] = (byte) ((random >> 8) & 255);
            bytes[i + 2] = (byte) ((random >> 16) & 255);
            bytes[i + 3] = (byte) ((random >> 24) & 255);
            i += 4;
        }
        int random2 = next(32);
        while (i < bytes.length) {
            bytes[i] = (byte) (random2 & 255);
            random2 >>= 8;
            i++;
        }
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public double nextDouble() {
        long high = ((long) next(26)) << 26;
        int low = next(26);
        return (((long) low) | high) * 2.220446049250313E-16d;
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public float nextFloat() {
        return next(23) * 1.1920929E-7f;
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public double nextGaussian() {
        if (java.lang.Double.isNaN(this.nextGaussian)) {
            double x = nextDouble();
            double y = nextDouble();
            double alpha = 6.283185307179586d * x;
            double r = org.apache.commons.math.util.FastMath.sqrt(org.apache.commons.math.util.FastMath.log(y) * (-2.0d));
            double random = org.apache.commons.math.util.FastMath.cos(alpha) * r;
            this.nextGaussian = org.apache.commons.math.util.FastMath.sin(alpha) * r;
            return random;
        }
        double random2 = this.nextGaussian;
        this.nextGaussian = Double.NaN;
        return random2;
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public int nextInt() {
        return next(32);
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public int nextInt(int n) throws java.lang.IllegalArgumentException {
        int random;
        if (n < 1) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(java.lang.Integer.valueOf(n));
        }
        int mask = n | (n >> 1);
        int mask2 = mask | (mask >> 2);
        int mask3 = mask2 | (mask2 >> 4);
        int mask4 = mask3 | (mask3 >> 8);
        int mask5 = mask4 | (mask4 >> 16);
        do {
            random = next(32) & mask5;
        } while (random >= n);
        return random;
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public long nextLong() {
        long high = ((long) next(32)) << 32;
        long low = ((long) next(32)) & 4294967295L;
        return high | low;
    }
}
