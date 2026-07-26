package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public class JDKRandomGenerator extends java.util.Random implements org.apache.commons.math.random.RandomGenerator {
    private static final long serialVersionUID = -7745277476784028798L;

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
}
