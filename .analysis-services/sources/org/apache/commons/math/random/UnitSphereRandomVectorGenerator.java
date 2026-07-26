package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public class UnitSphereRandomVectorGenerator implements org.apache.commons.math.random.RandomVectorGenerator {
    private final int dimension;
    private final org.apache.commons.math.random.RandomGenerator rand;

    public UnitSphereRandomVectorGenerator(int dimension, org.apache.commons.math.random.RandomGenerator rand) {
        this.dimension = dimension;
        this.rand = rand;
    }

    public UnitSphereRandomVectorGenerator(int dimension) {
        this(dimension, new org.apache.commons.math.random.MersenneTwister());
    }

    @Override // org.apache.commons.math.random.RandomVectorGenerator
    public double[] nextVector() {
        double normSq;
        double[] v = new double[this.dimension];
        do {
            normSq = 0.0d;
            for (int i = 0; i < this.dimension; i++) {
                double comp = (this.rand.nextDouble() * 2.0d) - 1.0d;
                v[i] = comp;
                normSq += comp * comp;
            }
        } while (normSq > 1.0d);
        double f = 1.0d / org.apache.commons.math.util.FastMath.sqrt(normSq);
        for (int i2 = 0; i2 < this.dimension; i2++) {
            v[i2] = v[i2] * f;
        }
        return v;
    }
}
