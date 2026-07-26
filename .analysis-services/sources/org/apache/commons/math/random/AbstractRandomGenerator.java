package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractRandomGenerator implements org.apache.commons.math.random.RandomGenerator {
    private double cachedNormalDeviate = Double.NaN;

    @Override // org.apache.commons.math.random.RandomGenerator
    public abstract double nextDouble();

    @Override // org.apache.commons.math.random.RandomGenerator
    public abstract void setSeed(long j);

    public void clear() {
        this.cachedNormalDeviate = Double.NaN;
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public void setSeed(int seed) {
        setSeed(seed);
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public void setSeed(int[] seed) {
        long combined = 0;
        for (int s : seed) {
            combined = (4294967291L * combined) + ((long) s);
        }
        setSeed(combined);
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public void nextBytes(byte[] bytes) {
        int bytesOut = 0;
        while (bytesOut < bytes.length) {
            int randInt = nextInt();
            int i = 0;
            while (i < 3) {
                if (i > 0) {
                    randInt >>= 8;
                }
                int bytesOut2 = bytesOut + 1;
                bytes[bytesOut] = (byte) randInt;
                if (bytesOut2 != bytes.length) {
                    i++;
                    bytesOut = bytesOut2;
                } else {
                    return;
                }
            }
        }
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public int nextInt() {
        return (int) (nextDouble() * 2.147483647E9d);
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public int nextInt(int n) {
        if (n <= 0) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(java.lang.Integer.valueOf(n));
        }
        int result = (int) (nextDouble() * ((double) n));
        return result < n ? result : n - 1;
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public long nextLong() {
        return (long) (nextDouble() * 9.223372036854776E18d);
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public boolean nextBoolean() {
        return nextDouble() <= 0.5d;
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public float nextFloat() {
        return (float) nextDouble();
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public double nextGaussian() {
        if (!java.lang.Double.isNaN(this.cachedNormalDeviate)) {
            double dev = this.cachedNormalDeviate;
            this.cachedNormalDeviate = Double.NaN;
            return dev;
        }
        double v1 = 0.0d;
        double v2 = 0.0d;
        double s = 1.0d;
        while (s >= 1.0d) {
            v1 = (nextDouble() * 2.0d) - 1.0d;
            v2 = (nextDouble() * 2.0d) - 1.0d;
            s = (v1 * v1) + (v2 * v2);
        }
        if (s != 0.0d) {
            s = org.apache.commons.math.util.FastMath.sqrt((org.apache.commons.math.util.FastMath.log(s) * (-2.0d)) / s);
        }
        this.cachedNormalDeviate = v2 * s;
        return v1 * s;
    }
}
