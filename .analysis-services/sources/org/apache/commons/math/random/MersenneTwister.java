package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public class MersenneTwister extends org.apache.commons.math.random.BitsStreamGenerator implements java.io.Serializable {
    private static final int M = 397;
    private static final int[] MAG01 = {0, -1727483681};
    private static final int N = 624;
    private static final long serialVersionUID = 8661194735290153518L;
    private int[] mt;
    private int mti;

    public MersenneTwister() {
        this.mt = new int[N];
        setSeed(java.lang.System.currentTimeMillis());
    }

    public MersenneTwister(int seed) {
        this.mt = new int[N];
        setSeed(seed);
    }

    public MersenneTwister(int[] seed) {
        this.mt = new int[N];
        setSeed(seed);
    }

    public MersenneTwister(long seed) {
        this.mt = new int[N];
        setSeed(seed);
    }

    @Override // org.apache.commons.math.random.BitsStreamGenerator, org.apache.commons.math.random.RandomGenerator
    public void setSeed(int seed) {
        long longMT = seed;
        this.mt[0] = (int) longMT;
        this.mti = 1;
        while (this.mti < N) {
            longMT = ((((longMT >> 30) ^ longMT) * 1812433253) + ((long) this.mti)) & 4294967295L;
            this.mt[this.mti] = (int) longMT;
            this.mti++;
        }
    }

    @Override // org.apache.commons.math.random.BitsStreamGenerator, org.apache.commons.math.random.RandomGenerator
    public void setSeed(int[] seed) {
        char c;
        if (seed == null) {
            setSeed(java.lang.System.currentTimeMillis());
            return;
        }
        setSeed(19650218);
        int i = 1;
        int j = 0;
        int k = org.apache.commons.math.util.FastMath.max(N, seed.length);
        while (true) {
            c = 30;
            if (k == 0) {
                break;
            }
            long l0 = (((long) this.mt[i]) & 2147483647L) | (this.mt[i] < 0 ? 2147483648L : 0L);
            long l1 = (((long) this.mt[i - 1]) & 2147483647L) | (this.mt[i + (-1)] < 0 ? 2147483648L : 0L);
            long l = ((((l1 >> 30) ^ l1) * 1664525) ^ l0) + ((long) seed[j]) + ((long) j);
            this.mt[i] = (int) (4294967295L & l);
            i++;
            j++;
            if (i >= N) {
                this.mt[0] = this.mt[623];
                i = 1;
            }
            if (j >= seed.length) {
                j = 0;
            }
            k--;
        }
        int k2 = 623;
        while (k2 != 0) {
            long l02 = (((long) this.mt[i]) & 2147483647L) | (this.mt[i] < 0 ? 2147483648L : 0L);
            long l12 = (((long) this.mt[i - 1]) & 2147483647L) | (this.mt[i + (-1)] < 0 ? 2147483648L : 0L);
            long l2 = (l02 ^ ((l12 ^ (l12 >> c)) * 1566083941)) - ((long) i);
            this.mt[i] = (int) (l2 & 4294967295L);
            i++;
            if (i >= N) {
                this.mt[0] = this.mt[623];
                i = 1;
            }
            k2--;
            c = 30;
        }
        this.mt[0] = Integer.MIN_VALUE;
    }

    @Override // org.apache.commons.math.random.BitsStreamGenerator, org.apache.commons.math.random.RandomGenerator
    public void setSeed(long seed) {
        setSeed(new int[]{(int) (seed >>> 32), (int) (4294967295L & seed)});
    }

    @Override // org.apache.commons.math.random.BitsStreamGenerator
    protected int next(int bits) {
        if (this.mti >= N) {
            int mtNext = this.mt[0];
            for (int k = 0; k < 227; k++) {
                int mtCurr = mtNext;
                mtNext = this.mt[k + 1];
                int y = (Integer.MAX_VALUE & mtNext) | (Integer.MIN_VALUE & mtCurr);
                this.mt[k] = (this.mt[k + M] ^ (y >>> 1)) ^ MAG01[y & 1];
            }
            for (int k2 = com.android.internal.util.FrameworkStatsLog.CAMERA_ACTION_EVENT; k2 < 623; k2++) {
                int mtCurr2 = mtNext;
                mtNext = this.mt[k2 + 1];
                int y2 = (mtCurr2 & Integer.MIN_VALUE) | (mtNext & Integer.MAX_VALUE);
                this.mt[k2] = (this.mt[k2 - 227] ^ (y2 >>> 1)) ^ MAG01[y2 & 1];
            }
            int k3 = mtNext & Integer.MIN_VALUE;
            int y3 = k3 | (Integer.MAX_VALUE & this.mt[0]);
            this.mt[623] = (this.mt[396] ^ (y3 >>> 1)) ^ MAG01[y3 & 1];
            this.mti = 0;
        }
        int[] iArr = this.mt;
        int i = this.mti;
        this.mti = i + 1;
        int y4 = iArr[i];
        int y5 = y4 ^ (y4 >>> 11);
        int y6 = y5 ^ ((y5 << 7) & (-1658038656));
        int y7 = y6 ^ ((y6 << 15) & (-272236544));
        return (y7 ^ (y7 >>> 18)) >>> (32 - bits);
    }
}
