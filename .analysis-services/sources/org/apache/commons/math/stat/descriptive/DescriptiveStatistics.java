package org.apache.commons.math.stat.descriptive;

/* JADX INFO: loaded from: classes4.dex */
public class DescriptiveStatistics implements org.apache.commons.math.stat.descriptive.StatisticalSummary, java.io.Serializable {
    public static final int INFINITE_WINDOW = -1;
    private static final java.lang.String SET_QUANTILE_METHOD_NAME = "setQuantile";
    private static final long serialVersionUID = 4133067267405273064L;
    protected org.apache.commons.math.util.ResizableDoubleArray eDA;
    private org.apache.commons.math.stat.descriptive.UnivariateStatistic geometricMeanImpl;
    private org.apache.commons.math.stat.descriptive.UnivariateStatistic kurtosisImpl;
    private org.apache.commons.math.stat.descriptive.UnivariateStatistic maxImpl;
    private org.apache.commons.math.stat.descriptive.UnivariateStatistic meanImpl;
    private org.apache.commons.math.stat.descriptive.UnivariateStatistic minImpl;
    private org.apache.commons.math.stat.descriptive.UnivariateStatistic percentileImpl;
    private org.apache.commons.math.stat.descriptive.UnivariateStatistic skewnessImpl;
    private org.apache.commons.math.stat.descriptive.UnivariateStatistic sumImpl;
    private org.apache.commons.math.stat.descriptive.UnivariateStatistic sumsqImpl;
    private org.apache.commons.math.stat.descriptive.UnivariateStatistic varianceImpl;
    protected int windowSize;

    public DescriptiveStatistics() {
        this.windowSize = -1;
        this.eDA = new org.apache.commons.math.util.ResizableDoubleArray();
        this.meanImpl = new org.apache.commons.math.stat.descriptive.moment.Mean();
        this.geometricMeanImpl = new org.apache.commons.math.stat.descriptive.moment.GeometricMean();
        this.kurtosisImpl = new org.apache.commons.math.stat.descriptive.moment.Kurtosis();
        this.maxImpl = new org.apache.commons.math.stat.descriptive.rank.Max();
        this.minImpl = new org.apache.commons.math.stat.descriptive.rank.Min();
        this.percentileImpl = new org.apache.commons.math.stat.descriptive.rank.Percentile();
        this.skewnessImpl = new org.apache.commons.math.stat.descriptive.moment.Skewness();
        this.varianceImpl = new org.apache.commons.math.stat.descriptive.moment.Variance();
        this.sumsqImpl = new org.apache.commons.math.stat.descriptive.summary.SumOfSquares();
        this.sumImpl = new org.apache.commons.math.stat.descriptive.summary.Sum();
    }

    public DescriptiveStatistics(int window) {
        this.windowSize = -1;
        this.eDA = new org.apache.commons.math.util.ResizableDoubleArray();
        this.meanImpl = new org.apache.commons.math.stat.descriptive.moment.Mean();
        this.geometricMeanImpl = new org.apache.commons.math.stat.descriptive.moment.GeometricMean();
        this.kurtosisImpl = new org.apache.commons.math.stat.descriptive.moment.Kurtosis();
        this.maxImpl = new org.apache.commons.math.stat.descriptive.rank.Max();
        this.minImpl = new org.apache.commons.math.stat.descriptive.rank.Min();
        this.percentileImpl = new org.apache.commons.math.stat.descriptive.rank.Percentile();
        this.skewnessImpl = new org.apache.commons.math.stat.descriptive.moment.Skewness();
        this.varianceImpl = new org.apache.commons.math.stat.descriptive.moment.Variance();
        this.sumsqImpl = new org.apache.commons.math.stat.descriptive.summary.SumOfSquares();
        this.sumImpl = new org.apache.commons.math.stat.descriptive.summary.Sum();
        setWindowSize(window);
    }

    public DescriptiveStatistics(double[] initialDoubleArray) {
        this.windowSize = -1;
        this.eDA = new org.apache.commons.math.util.ResizableDoubleArray();
        this.meanImpl = new org.apache.commons.math.stat.descriptive.moment.Mean();
        this.geometricMeanImpl = new org.apache.commons.math.stat.descriptive.moment.GeometricMean();
        this.kurtosisImpl = new org.apache.commons.math.stat.descriptive.moment.Kurtosis();
        this.maxImpl = new org.apache.commons.math.stat.descriptive.rank.Max();
        this.minImpl = new org.apache.commons.math.stat.descriptive.rank.Min();
        this.percentileImpl = new org.apache.commons.math.stat.descriptive.rank.Percentile();
        this.skewnessImpl = new org.apache.commons.math.stat.descriptive.moment.Skewness();
        this.varianceImpl = new org.apache.commons.math.stat.descriptive.moment.Variance();
        this.sumsqImpl = new org.apache.commons.math.stat.descriptive.summary.SumOfSquares();
        this.sumImpl = new org.apache.commons.math.stat.descriptive.summary.Sum();
        if (initialDoubleArray != null) {
            this.eDA = new org.apache.commons.math.util.ResizableDoubleArray(initialDoubleArray);
        }
    }

    public DescriptiveStatistics(org.apache.commons.math.stat.descriptive.DescriptiveStatistics original) {
        this.windowSize = -1;
        this.eDA = new org.apache.commons.math.util.ResizableDoubleArray();
        this.meanImpl = new org.apache.commons.math.stat.descriptive.moment.Mean();
        this.geometricMeanImpl = new org.apache.commons.math.stat.descriptive.moment.GeometricMean();
        this.kurtosisImpl = new org.apache.commons.math.stat.descriptive.moment.Kurtosis();
        this.maxImpl = new org.apache.commons.math.stat.descriptive.rank.Max();
        this.minImpl = new org.apache.commons.math.stat.descriptive.rank.Min();
        this.percentileImpl = new org.apache.commons.math.stat.descriptive.rank.Percentile();
        this.skewnessImpl = new org.apache.commons.math.stat.descriptive.moment.Skewness();
        this.varianceImpl = new org.apache.commons.math.stat.descriptive.moment.Variance();
        this.sumsqImpl = new org.apache.commons.math.stat.descriptive.summary.SumOfSquares();
        this.sumImpl = new org.apache.commons.math.stat.descriptive.summary.Sum();
        copy(original, this);
    }

    public void addValue(double v) {
        if (this.windowSize == -1) {
            this.eDA.addElement(v);
        } else if (getN() == this.windowSize) {
            this.eDA.addElementRolling(v);
        } else if (getN() < this.windowSize) {
            this.eDA.addElement(v);
        }
    }

    public void removeMostRecentValue() {
        this.eDA.discardMostRecentElements(1);
    }

    public double replaceMostRecentValue(double v) {
        return this.eDA.substituteMostRecentElement(v);
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getMean() {
        return apply(this.meanImpl);
    }

    public double getGeometricMean() {
        return apply(this.geometricMeanImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getVariance() {
        return apply(this.varianceImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getStandardDeviation() {
        if (getN() <= 0) {
            return Double.NaN;
        }
        if (getN() > 1) {
            double stdDev = org.apache.commons.math.util.FastMath.sqrt(getVariance());
            return stdDev;
        }
        return 0.0d;
    }

    public double getSkewness() {
        return apply(this.skewnessImpl);
    }

    public double getKurtosis() {
        return apply(this.kurtosisImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getMax() {
        return apply(this.maxImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getMin() {
        return apply(this.minImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public long getN() {
        return this.eDA.getNumElements();
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getSum() {
        return apply(this.sumImpl);
    }

    public double getSumsq() {
        return apply(this.sumsqImpl);
    }

    public void clear() {
        this.eDA.clear();
    }

    public int getWindowSize() {
        return this.windowSize;
    }

    public void setWindowSize(int windowSize) {
        if (windowSize < 1 && windowSize != -1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_WINDOW_SIZE, java.lang.Integer.valueOf(windowSize));
        }
        this.windowSize = windowSize;
        if (windowSize != -1 && windowSize < this.eDA.getNumElements()) {
            this.eDA.discardFrontElements(this.eDA.getNumElements() - windowSize);
        }
    }

    public double[] getValues() {
        return this.eDA.getElements();
    }

    public double[] getSortedValues() {
        double[] sort = getValues();
        java.util.Arrays.sort(sort);
        return sort;
    }

    public double getElement(int index) {
        return this.eDA.getElement(index);
    }

    public double getPercentile(double p) {
        if (this.percentileImpl instanceof org.apache.commons.math.stat.descriptive.rank.Percentile) {
            ((org.apache.commons.math.stat.descriptive.rank.Percentile) this.percentileImpl).setQuantile(p);
        } else {
            try {
                this.percentileImpl.getClass().getMethod(SET_QUANTILE_METHOD_NAME, java.lang.Double.TYPE).invoke(this.percentileImpl, java.lang.Double.valueOf(p));
            } catch (java.lang.IllegalAccessException e) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.PERCENTILE_IMPLEMENTATION_CANNOT_ACCESS_METHOD, SET_QUANTILE_METHOD_NAME, this.percentileImpl.getClass().getName());
            } catch (java.lang.NoSuchMethodException e2) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.PERCENTILE_IMPLEMENTATION_UNSUPPORTED_METHOD, this.percentileImpl.getClass().getName(), SET_QUANTILE_METHOD_NAME);
            } catch (java.lang.reflect.InvocationTargetException e3) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(e3.getCause());
            }
        }
        return apply(this.percentileImpl);
    }

    public java.lang.String toString() {
        java.lang.StringBuilder outBuffer = new java.lang.StringBuilder();
        outBuffer.append("DescriptiveStatistics:").append("\n");
        outBuffer.append("n: ").append(getN()).append("\n");
        outBuffer.append("min: ").append(getMin()).append("\n");
        outBuffer.append("max: ").append(getMax()).append("\n");
        outBuffer.append("mean: ").append(getMean()).append("\n");
        outBuffer.append("std dev: ").append(getStandardDeviation()).append("\n");
        outBuffer.append("median: ").append(getPercentile(50.0d)).append("\n");
        outBuffer.append("skewness: ").append(getSkewness()).append("\n");
        outBuffer.append("kurtosis: ").append(getKurtosis()).append("\n");
        return outBuffer.toString();
    }

    public double apply(org.apache.commons.math.stat.descriptive.UnivariateStatistic stat) {
        return stat.evaluate(this.eDA.getInternalValues(), this.eDA.start(), this.eDA.getNumElements());
    }

    public synchronized org.apache.commons.math.stat.descriptive.UnivariateStatistic getMeanImpl() {
        return this.meanImpl;
    }

    public synchronized void setMeanImpl(org.apache.commons.math.stat.descriptive.UnivariateStatistic meanImpl) {
        this.meanImpl = meanImpl;
    }

    public synchronized org.apache.commons.math.stat.descriptive.UnivariateStatistic getGeometricMeanImpl() {
        return this.geometricMeanImpl;
    }

    public synchronized void setGeometricMeanImpl(org.apache.commons.math.stat.descriptive.UnivariateStatistic geometricMeanImpl) {
        this.geometricMeanImpl = geometricMeanImpl;
    }

    public synchronized org.apache.commons.math.stat.descriptive.UnivariateStatistic getKurtosisImpl() {
        return this.kurtosisImpl;
    }

    public synchronized void setKurtosisImpl(org.apache.commons.math.stat.descriptive.UnivariateStatistic kurtosisImpl) {
        this.kurtosisImpl = kurtosisImpl;
    }

    public synchronized org.apache.commons.math.stat.descriptive.UnivariateStatistic getMaxImpl() {
        return this.maxImpl;
    }

    public synchronized void setMaxImpl(org.apache.commons.math.stat.descriptive.UnivariateStatistic maxImpl) {
        this.maxImpl = maxImpl;
    }

    public synchronized org.apache.commons.math.stat.descriptive.UnivariateStatistic getMinImpl() {
        return this.minImpl;
    }

    public synchronized void setMinImpl(org.apache.commons.math.stat.descriptive.UnivariateStatistic minImpl) {
        this.minImpl = minImpl;
    }

    public synchronized org.apache.commons.math.stat.descriptive.UnivariateStatistic getPercentileImpl() {
        return this.percentileImpl;
    }

    public synchronized void setPercentileImpl(org.apache.commons.math.stat.descriptive.UnivariateStatistic percentileImpl) {
        try {
            try {
                try {
                    percentileImpl.getClass().getMethod(SET_QUANTILE_METHOD_NAME, java.lang.Double.TYPE).invoke(percentileImpl, java.lang.Double.valueOf(50.0d));
                    this.percentileImpl = percentileImpl;
                } catch (java.lang.IllegalAccessException e) {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.PERCENTILE_IMPLEMENTATION_CANNOT_ACCESS_METHOD, SET_QUANTILE_METHOD_NAME, percentileImpl.getClass().getName());
                }
            } catch (java.lang.reflect.InvocationTargetException e3) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(e3.getCause());
            }
        } catch (java.lang.NoSuchMethodException e2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.PERCENTILE_IMPLEMENTATION_UNSUPPORTED_METHOD, percentileImpl.getClass().getName(), SET_QUANTILE_METHOD_NAME);
        }
    }

    public synchronized org.apache.commons.math.stat.descriptive.UnivariateStatistic getSkewnessImpl() {
        return this.skewnessImpl;
    }

    public synchronized void setSkewnessImpl(org.apache.commons.math.stat.descriptive.UnivariateStatistic skewnessImpl) {
        this.skewnessImpl = skewnessImpl;
    }

    public synchronized org.apache.commons.math.stat.descriptive.UnivariateStatistic getVarianceImpl() {
        return this.varianceImpl;
    }

    public synchronized void setVarianceImpl(org.apache.commons.math.stat.descriptive.UnivariateStatistic varianceImpl) {
        this.varianceImpl = varianceImpl;
    }

    public synchronized org.apache.commons.math.stat.descriptive.UnivariateStatistic getSumsqImpl() {
        return this.sumsqImpl;
    }

    public synchronized void setSumsqImpl(org.apache.commons.math.stat.descriptive.UnivariateStatistic sumsqImpl) {
        this.sumsqImpl = sumsqImpl;
    }

    public synchronized org.apache.commons.math.stat.descriptive.UnivariateStatistic getSumImpl() {
        return this.sumImpl;
    }

    public synchronized void setSumImpl(org.apache.commons.math.stat.descriptive.UnivariateStatistic sumImpl) {
        this.sumImpl = sumImpl;
    }

    public org.apache.commons.math.stat.descriptive.DescriptiveStatistics copy() {
        org.apache.commons.math.stat.descriptive.DescriptiveStatistics result = new org.apache.commons.math.stat.descriptive.DescriptiveStatistics();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.DescriptiveStatistics source, org.apache.commons.math.stat.descriptive.DescriptiveStatistics dest) {
        dest.eDA = source.eDA.copy();
        dest.windowSize = source.windowSize;
        dest.maxImpl = source.maxImpl.copy();
        dest.meanImpl = source.meanImpl.copy();
        dest.minImpl = source.minImpl.copy();
        dest.sumImpl = source.sumImpl.copy();
        dest.varianceImpl = source.varianceImpl.copy();
        dest.sumsqImpl = source.sumsqImpl.copy();
        dest.geometricMeanImpl = source.geometricMeanImpl.copy();
        dest.kurtosisImpl = source.kurtosisImpl;
        dest.skewnessImpl = source.skewnessImpl;
        dest.percentileImpl = source.percentileImpl;
    }
}
