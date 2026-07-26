package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractContinuousDistribution extends org.apache.commons.math.distribution.AbstractDistribution implements org.apache.commons.math.distribution.ContinuousDistribution, java.io.Serializable {
    private static final long serialVersionUID = -38038050983108802L;
    protected final org.apache.commons.math.random.RandomDataImpl randomData = new org.apache.commons.math.random.RandomDataImpl();
    private double solverAbsoluteAccuracy = 1.0E-6d;

    protected abstract double getDomainLowerBound(double d);

    protected abstract double getDomainUpperBound(double d);

    protected abstract double getInitialDomain(double d);

    protected AbstractContinuousDistribution() {
    }

    public double density(double x) throws org.apache.commons.math.MathRuntimeException {
        throw new org.apache.commons.math.MathRuntimeException(new java.lang.UnsupportedOperationException(), org.apache.commons.math.exception.util.LocalizedFormats.NO_DENSITY_FOR_THIS_DISTRIBUTION, new java.lang.Object[0]);
    }

    @Override // org.apache.commons.math.distribution.ContinuousDistribution
    public double inverseCumulativeProbability(final double p) throws org.apache.commons.math.MathException {
        if (p < 0.0d || p > 1.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_RANGE_SIMPLE, java.lang.Double.valueOf(p), java.lang.Double.valueOf(0.0d), java.lang.Double.valueOf(1.0d));
        }
        org.apache.commons.math.analysis.UnivariateRealFunction rootFindingFunction = new org.apache.commons.math.analysis.UnivariateRealFunction() { // from class: org.apache.commons.math.distribution.AbstractContinuousDistribution.1
            @Override // org.apache.commons.math.analysis.UnivariateRealFunction
            public double value(double x) throws org.apache.commons.math.FunctionEvaluationException {
                try {
                    double ret = org.apache.commons.math.distribution.AbstractContinuousDistribution.this.cumulativeProbability(x) - p;
                    if (java.lang.Double.isNaN(ret)) {
                        throw new org.apache.commons.math.FunctionEvaluationException(x, org.apache.commons.math.exception.util.LocalizedFormats.CUMULATIVE_PROBABILITY_RETURNED_NAN, java.lang.Double.valueOf(x), java.lang.Double.valueOf(p));
                    }
                    return ret;
                } catch (org.apache.commons.math.MathException ex) {
                    throw new org.apache.commons.math.FunctionEvaluationException(x, ex.getSpecificPattern(), ex.getGeneralPattern(), ex.getArguments());
                }
            }
        };
        double lowerBound = getDomainLowerBound(p);
        double upperBound = getDomainUpperBound(p);
        try {
            double[] bracket = org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.bracket(rootFindingFunction, getInitialDomain(p), lowerBound, upperBound);
            double root = org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.solve(rootFindingFunction, bracket[0], bracket[1], getSolverAbsoluteAccuracy());
            return root;
        } catch (org.apache.commons.math.ConvergenceException ex) {
            if (org.apache.commons.math.util.FastMath.abs(rootFindingFunction.value(lowerBound)) < getSolverAbsoluteAccuracy()) {
                return lowerBound;
            }
            if (org.apache.commons.math.util.FastMath.abs(rootFindingFunction.value(upperBound)) < getSolverAbsoluteAccuracy()) {
                return upperBound;
            }
            throw new org.apache.commons.math.MathException(ex);
        }
    }

    public void reseedRandomGenerator(long seed) {
        this.randomData.reSeed(seed);
    }

    public double sample() throws org.apache.commons.math.MathException {
        return this.randomData.nextInversionDeviate(this);
    }

    public double[] sample(int sampleSize) throws org.apache.commons.math.MathException {
        if (sampleSize <= 0) {
            org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_SAMPLE_SIZE, java.lang.Integer.valueOf(sampleSize));
        }
        double[] out = new double[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            out[i] = sample();
        }
        return out;
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }
}
