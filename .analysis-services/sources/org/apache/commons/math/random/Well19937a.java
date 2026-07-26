package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public class Well19937a extends org.apache.commons.math.random.AbstractWell {
    private static final int K = 19937;
    private static final int M1 = 70;
    private static final int M2 = 179;
    private static final int M3 = 449;
    private static final long serialVersionUID = -7462102162223815419L;

    public Well19937a() {
        super(K, 70, 179, 449);
    }

    public Well19937a(int seed) {
        super(K, 70, 179, 449, seed);
    }

    public Well19937a(int[] seed) {
        super(K, 70, 179, 449, seed);
    }

    public Well19937a(long seed) {
        super(K, 70, 179, 449, seed);
    }

    @Override // org.apache.commons.math.random.AbstractWell, org.apache.commons.math.random.BitsStreamGenerator
    protected int next(int bits) {
        int indexRm1 = this.iRm1[this.index];
        int indexRm2 = this.iRm2[this.index];
        int v0 = this.v[this.index];
        int vM1 = this.v[this.i1[this.index]];
        int vM2 = this.v[this.i2[this.index]];
        int vM3 = this.v[this.i3[this.index]];
        int z0 = (this.v[indexRm1] & Integer.MIN_VALUE) ^ (this.v[indexRm2] & Integer.MAX_VALUE);
        int z1 = ((v0 << 25) ^ v0) ^ ((vM1 >>> 27) ^ vM1);
        int z2 = (vM2 >>> 9) ^ ((vM3 >>> 1) ^ vM3);
        int z3 = z1 ^ z2;
        int z4 = ((((z1 << 9) ^ z1) ^ z0) ^ ((z2 << 21) ^ z2)) ^ ((z3 >>> 21) ^ z3);
        this.v[this.index] = z3;
        this.v[indexRm1] = z4;
        int[] iArr = this.v;
        iArr[indexRm2] = Integer.MIN_VALUE & iArr[indexRm2];
        this.index = indexRm1;
        return z4 >>> (32 - bits);
    }
}
