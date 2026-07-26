package org.apache.commons.math.stat.descriptive;

/* JADX INFO: loaded from: classes4.dex */
public class StatisticalSummaryValues implements java.io.Serializable, org.apache.commons.math.stat.descriptive.StatisticalSummary {
    private static final long serialVersionUID = -5108854841843722536L;
    private final double max;
    private final double mean;
    private final double min;
    private final long n;
    private final double sum;
    private final double variance;

    public StatisticalSummaryValues(double mean, double variance, long n, double max, double min, double sum) {
        this.mean = mean;
        this.variance = variance;
        this.n = n;
        this.max = max;
        this.min = min;
        this.sum = sum;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getMax() {
        return this.max;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getMean() {
        return this.mean;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getMin() {
        return this.min;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public long getN() {
        return this.n;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getSum() {
        return this.sum;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getStandardDeviation() {
        return org.apache.commons.math.util.FastMath.sqrt(this.variance);
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getVariance() {
        return this.variance;
    }

    public boolean equals(java.lang.Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof org.apache.commons.math.stat.descriptive.StatisticalSummaryValues)) {
            return false;
        }
        org.apache.commons.math.stat.descriptive.StatisticalSummaryValues stat = (org.apache.commons.math.stat.descriptive.StatisticalSummaryValues) object;
        return org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getMax(), getMax()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getMean(), getMean()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getMin(), getMin()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN((float) stat.getN(), (float) getN()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getSum(), getSum()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getVariance(), getVariance());
    }

    public int hashCode() {
        int result = org.apache.commons.math.util.MathUtils.hash(getMax()) + 31;
        return (((((((((result * 31) + org.apache.commons.math.util.MathUtils.hash(getMean())) * 31) + org.apache.commons.math.util.MathUtils.hash(getMin())) * 31) + org.apache.commons.math.util.MathUtils.hash(getN())) * 31) + org.apache.commons.math.util.MathUtils.hash(getSum())) * 31) + org.apache.commons.math.util.MathUtils.hash(getVariance());
    }

    public java.lang.String toString() {
        java.lang.StringBuilder outBuffer = new java.lang.StringBuilder();
        outBuffer.append("StatisticalSummaryValues:").append("\n");
        outBuffer.append("n: ").append(getN()).append("\n");
        outBuffer.append("min: ").append(getMin()).append("\n");
        outBuffer.append("max: ").append(getMax()).append("\n");
        outBuffer.append("mean: ").append(getMean()).append("\n");
        outBuffer.append("std dev: ").append(getStandardDeviation()).append("\n");
        outBuffer.append("variance: ").append(getVariance()).append("\n");
        outBuffer.append("sum: ").append(getSum()).append("\n");
        return outBuffer.toString();
    }
}
