package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public class UniformRandomGenerator implements org.apache.commons.math.random.NormalizedRandomGenerator {
    private static final double SQRT3 = org.apache.commons.math.util.FastMath.sqrt(3.0d);
    private static final long serialVersionUID = 1569292426375546027L;
    private final org.apache.commons.math.random.RandomGenerator generator;

    public UniformRandomGenerator(org.apache.commons.math.random.RandomGenerator generator) {
        this.generator = generator;
    }

    @Override // org.apache.commons.math.random.NormalizedRandomGenerator
    public double nextNormalizedDouble() {
        return SQRT3 * ((this.generator.nextDouble() * 2.0d) - 1.0d);
    }
}
