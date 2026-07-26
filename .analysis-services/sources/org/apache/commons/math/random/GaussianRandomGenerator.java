package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public class GaussianRandomGenerator implements org.apache.commons.math.random.NormalizedRandomGenerator {
    private final org.apache.commons.math.random.RandomGenerator generator;

    public GaussianRandomGenerator(org.apache.commons.math.random.RandomGenerator generator) {
        this.generator = generator;
    }

    @Override // org.apache.commons.math.random.NormalizedRandomGenerator
    public double nextNormalizedDouble() {
        return this.generator.nextGaussian();
    }
}
