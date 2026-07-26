package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public class Well44497a extends org.apache.commons.math.random.AbstractWell {
    private static final int K = 44497;
    private static final int M1 = 23;
    private static final int M2 = 481;
    private static final int M3 = 229;
    private static final long serialVersionUID = -3859207588353972099L;

    public Well44497a() {
        super(K, 23, M2, M3);
    }

    public Well44497a(int seed) {
        super(K, 23, M2, M3, seed);
    }

    public Well44497a(int[] seed) {
        super(K, 23, M2, M3, seed);
    }

    public Well44497a(long seed) {
        super(K, 23, M2, M3, seed);
    }

    @Override // org.apache.commons.math.random.AbstractWell, org.apache.commons.math.random.BitsStreamGenerator
    protected int next(int bits) {
        int indexRm1 = this.iRm1[this.index];
        int indexRm2 = this.iRm2[this.index];
        int v0 = this.v[this.index];
        int vM1 = this.v[this.i1[this.index]];
        int vM2 = this.v[this.i2[this.index]];
        int vM3 = this.v[this.i3[this.index]];
        int z0 = (this.v[indexRm1] & (-32768)) ^ (this.v[indexRm2] & 32767);
        int z1 = ((v0 << 24) ^ v0) ^ ((vM1 >>> 30) ^ vM1);
        int z2 = ((vM2 << 10) ^ vM2) ^ (vM3 << 26);
        int z3 = z1 ^ z2;
        int z2Prime = ((z2 << 9) ^ (z2 >>> 23)) & (-67108865);
        int z2Second = (131072 & z2) != 0 ? (-1221985044) ^ z2Prime : z2Prime;
        int z4 = ((((z1 >>> 20) ^ z1) ^ z0) ^ z2Second) ^ z3;
        this.v[this.index] = z3;
        this.v[indexRm1] = z4;
        int[] iArr = this.v;
        iArr[indexRm2] = iArr[indexRm2] & (-32768);
        this.index = indexRm1;
        return z4 >>> (32 - bits);
    }
}
