package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public class UncorrelatedRandomVectorGenerator implements org.apache.commons.math.random.RandomVectorGenerator {
    private final org.apache.commons.math.random.NormalizedRandomGenerator generator;
    private final double[] mean;
    private final double[] standardDeviation;

    public UncorrelatedRandomVectorGenerator(double[] mean, double[] standardDeviation, org.apache.commons.math.random.NormalizedRandomGenerator generator) {
        if (mean.length != standardDeviation.length) {
            throw new org.apache.commons.math.exception.DimensionMismatchException(mean.length, standardDeviation.length);
        }
        this.mean = (double[]) mean.clone();
        this.standardDeviation = (double[]) standardDeviation.clone();
        this.generator = generator;
    }

    public UncorrelatedRandomVectorGenerator(int dimension, org.apache.commons.math.random.NormalizedRandomGenerator generator) {
        this.mean = new double[dimension];
        this.standardDeviation = new double[dimension];
        java.util.Arrays.fill(this.standardDeviation, 1.0d);
        this.generator = generator;
    }

    @Override // org.apache.commons.math.random.RandomVectorGenerator
    public double[] nextVector() {
        double[] random = new double[this.mean.length];
        for (int i = 0; i < random.length; i++) {
            random[i] = this.mean[i] + (this.standardDeviation[i] * this.generator.nextNormalizedDouble());
        }
        return random;
    }
}
