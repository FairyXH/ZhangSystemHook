package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public class RandomAdaptor extends java.util.Random implements org.apache.commons.math.random.RandomGenerator {
    private static final long serialVersionUID = 2306581345647615033L;
    private final org.apache.commons.math.random.RandomGenerator randomGenerator;

    private RandomAdaptor() {
        this.randomGenerator = null;
    }

    public RandomAdaptor(org.apache.commons.math.random.RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    public static java.util.Random createAdaptor(org.apache.commons.math.random.RandomGenerator randomGenerator) {
        return new org.apache.commons.math.random.RandomAdaptor(randomGenerator);
    }

    @Override // java.util.Random, org.apache.commons.math.random.RandomGenerator
    public boolean nextBoolean() {
        return this.randomGenerator.nextBoolean();
    }

    @Override // java.util.Random, org.apache.commons.math.random.RandomGenerator
    public void nextBytes(byte[] bytes) {
        this.randomGenerator.nextBytes(bytes);
    }

    @Override // java.util.Random, org.apache.commons.math.random.RandomGenerator
    public double nextDouble() {
        return this.randomGenerator.nextDouble();
    }

    @Override // java.util.Random, org.apache.commons.math.random.RandomGenerator
    public float nextFloat() {
        return this.randomGenerator.nextFloat();
    }

    @Override // java.util.Random, org.apache.commons.math.random.RandomGenerator
    public double nextGaussian() {
        return this.randomGenerator.nextGaussian();
    }

    @Override // java.util.Random, org.apache.commons.math.random.RandomGenerator
    public int nextInt() {
        return this.randomGenerator.nextInt();
    }

    @Override // java.util.Random, org.apache.commons.math.random.RandomGenerator
    public int nextInt(int n) {
        return this.randomGenerator.nextInt(n);
    }

    @Override // java.util.Random, org.apache.commons.math.random.RandomGenerator
    public long nextLong() {
        return this.randomGenerator.nextLong();
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public void setSeed(int seed) {
        if (this.randomGenerator != null) {
            this.randomGenerator.setSeed(seed);
        }
    }

    @Override // org.apache.commons.math.random.RandomGenerator
    public void setSeed(int[] seed) {
        if (this.randomGenerator != null) {
            this.randomGenerator.setSeed(seed);
        }
    }

    @Override // java.util.Random, org.apache.commons.math.random.RandomGenerator
    public void setSeed(long seed) {
        if (this.randomGenerator != null) {
            this.randomGenerator.setSeed(seed);
        }
    }
}
