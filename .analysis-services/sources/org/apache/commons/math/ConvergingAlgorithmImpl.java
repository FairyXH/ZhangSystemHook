package org.apache.commons.math;

/* JADX INFO: loaded from: classes4.dex */
public abstract class ConvergingAlgorithmImpl implements org.apache.commons.math.ConvergingAlgorithm {
    protected double absoluteAccuracy;
    protected double defaultAbsoluteAccuracy;
    protected int defaultMaximalIterationCount;
    protected double defaultRelativeAccuracy;
    protected int iterationCount;
    protected int maximalIterationCount;
    protected double relativeAccuracy;

    @java.lang.Deprecated
    protected ConvergingAlgorithmImpl(int defaultMaximalIterationCount, double defaultAbsoluteAccuracy) {
        this.defaultAbsoluteAccuracy = defaultAbsoluteAccuracy;
        this.defaultRelativeAccuracy = 1.0E-14d;
        this.absoluteAccuracy = defaultAbsoluteAccuracy;
        this.relativeAccuracy = this.defaultRelativeAccuracy;
        this.defaultMaximalIterationCount = defaultMaximalIterationCount;
        this.maximalIterationCount = defaultMaximalIterationCount;
        this.iterationCount = 0;
    }

    @java.lang.Deprecated
    protected ConvergingAlgorithmImpl() {
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public int getIterationCount() {
        return this.iterationCount;
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public void setAbsoluteAccuracy(double accuracy) {
        this.absoluteAccuracy = accuracy;
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public double getAbsoluteAccuracy() {
        return this.absoluteAccuracy;
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public void resetAbsoluteAccuracy() {
        this.absoluteAccuracy = this.defaultAbsoluteAccuracy;
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public void setMaximalIterationCount(int count) {
        this.maximalIterationCount = count;
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public int getMaximalIterationCount() {
        return this.maximalIterationCount;
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public void resetMaximalIterationCount() {
        this.maximalIterationCount = this.defaultMaximalIterationCount;
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public void setRelativeAccuracy(double accuracy) {
        this.relativeAccuracy = accuracy;
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public double getRelativeAccuracy() {
        return this.relativeAccuracy;
    }

    @Override // org.apache.commons.math.ConvergingAlgorithm
    public void resetRelativeAccuracy() {
        this.relativeAccuracy = this.defaultRelativeAccuracy;
    }

    protected void resetIterationsCounter() {
        this.iterationCount = 0;
    }

    protected void incrementIterationsCounter() throws org.apache.commons.math.MaxIterationsExceededException {
        int i = this.iterationCount + 1;
        this.iterationCount = i;
        if (i > this.maximalIterationCount) {
            throw new org.apache.commons.math.MaxIterationsExceededException(this.maximalIterationCount);
        }
    }
}
