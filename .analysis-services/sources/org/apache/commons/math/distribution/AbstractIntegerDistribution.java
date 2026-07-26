package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractIntegerDistribution extends org.apache.commons.math.distribution.AbstractDistribution implements org.apache.commons.math.distribution.IntegerDistribution, java.io.Serializable {
    private static final long serialVersionUID = -1146319659338487221L;
    protected final org.apache.commons.math.random.RandomDataImpl randomData = new org.apache.commons.math.random.RandomDataImpl();

    @Override // org.apache.commons.math.distribution.IntegerDistribution
    public abstract double cumulativeProbability(int i) throws org.apache.commons.math.MathException;

    protected abstract int getDomainLowerBound(double d);

    protected abstract int getDomainUpperBound(double d);

    protected AbstractIntegerDistribution() {
    }

    @Override // org.apache.commons.math.distribution.Distribution
    public double cumulativeProbability(double x) throws org.apache.commons.math.MathException {
        return cumulativeProbability((int) org.apache.commons.math.util.FastMath.floor(x));
    }

    @Override // org.apache.commons.math.distribution.AbstractDistribution, org.apache.commons.math.distribution.Distribution
    public double cumulativeProbability(double x0, double x1) throws org.apache.commons.math.MathException {
        if (x0 > x1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, java.lang.Double.valueOf(x0), java.lang.Double.valueOf(x1));
        }
        if (org.apache.commons.math.util.FastMath.floor(x0) < x0) {
            return cumulativeProbability(((int) org.apache.commons.math.util.FastMath.floor(x0)) + 1, (int) org.apache.commons.math.util.FastMath.floor(x1));
        }
        return cumulativeProbability((int) org.apache.commons.math.util.FastMath.floor(x0), (int) org.apache.commons.math.util.FastMath.floor(x1));
    }

    @Override // org.apache.commons.math.distribution.DiscreteDistribution
    public double probability(double x) {
        double fl = org.apache.commons.math.util.FastMath.floor(x);
        if (fl == x) {
            return probability((int) x);
        }
        return 0.0d;
    }

    @Override // org.apache.commons.math.distribution.IntegerDistribution
    public double cumulativeProbability(int x0, int x1) throws org.apache.commons.math.MathException {
        if (x0 > x1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, java.lang.Integer.valueOf(x0), java.lang.Integer.valueOf(x1));
        }
        return cumulativeProbability(x1) - cumulativeProbability(x0 - 1);
    }

    @Override // org.apache.commons.math.distribution.IntegerDistribution
    public int inverseCumulativeProbability(double p) throws org.apache.commons.math.MathException {
        if (p < 0.0d || p > 1.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_RANGE_SIMPLE, java.lang.Double.valueOf(p), java.lang.Double.valueOf(0.0d), java.lang.Double.valueOf(1.0d));
        }
        int x0 = getDomainLowerBound(p);
        int x1 = getDomainUpperBound(p);
        while (x0 < x1) {
            int xm = ((x1 - x0) / 2) + x0;
            double pm = checkedCumulativeProbability(xm);
            if (pm > p) {
                if (xm == x1) {
                    x1--;
                } else {
                    x1 = xm;
                }
            } else if (xm == x0) {
                x0++;
            } else {
                x0 = xm;
            }
        }
        double pm2 = checkedCumulativeProbability(x0);
        while (pm2 > p) {
            x0--;
            pm2 = checkedCumulativeProbability(x0);
        }
        return x0;
    }

    public void reseedRandomGenerator(long seed) {
        this.randomData.reSeed(seed);
    }

    public int sample() throws org.apache.commons.math.MathException {
        return this.randomData.nextInversionDeviate(this);
    }

    public int[] sample(int sampleSize) throws org.apache.commons.math.MathException {
        if (sampleSize <= 0) {
            org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_SAMPLE_SIZE, java.lang.Integer.valueOf(sampleSize));
        }
        int[] out = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            out[i] = sample();
        }
        return out;
    }

    private double checkedCumulativeProbability(int argument) throws org.apache.commons.math.MathException {
        double result = cumulativeProbability(argument);
        if (java.lang.Double.isNaN(result)) {
            throw new org.apache.commons.math.MathException(org.apache.commons.math.exception.util.LocalizedFormats.DISCRETE_CUMULATIVE_PROBABILITY_RETURNED_NAN, java.lang.Integer.valueOf(argument));
        }
        return result;
    }

    public boolean isSupportLowerBoundInclusive() {
        return true;
    }

    public boolean isSupportUpperBoundInclusive() {
        return true;
    }
}
