package org.apache.commons.math.stat.descriptive;

/* JADX INFO: loaded from: classes4.dex */
public class SynchronizedDescriptiveStatistics extends org.apache.commons.math.stat.descriptive.DescriptiveStatistics {
    private static final long serialVersionUID = 1;

    public SynchronizedDescriptiveStatistics() {
        this(-1);
    }

    public SynchronizedDescriptiveStatistics(int window) {
        super(window);
    }

    public SynchronizedDescriptiveStatistics(org.apache.commons.math.stat.descriptive.SynchronizedDescriptiveStatistics original) {
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.DescriptiveStatistics
    public synchronized void addValue(double v) {
        super.addValue(v);
    }

    @Override // org.apache.commons.math.stat.descriptive.DescriptiveStatistics
    public synchronized double apply(org.apache.commons.math.stat.descriptive.UnivariateStatistic stat) {
        return super.apply(stat);
    }

    @Override // org.apache.commons.math.stat.descriptive.DescriptiveStatistics
    public synchronized void clear() {
        super.clear();
    }

    @Override // org.apache.commons.math.stat.descriptive.DescriptiveStatistics
    public synchronized double getElement(int index) {
        return super.getElement(index);
    }

    @Override // org.apache.commons.math.stat.descriptive.DescriptiveStatistics, org.apache.commons.math.stat.descriptive.StatisticalSummary
    public synchronized long getN() {
        return super.getN();
    }

    @Override // org.apache.commons.math.stat.descriptive.DescriptiveStatistics, org.apache.commons.math.stat.descriptive.StatisticalSummary
    public synchronized double getStandardDeviation() {
        return super.getStandardDeviation();
    }

    @Override // org.apache.commons.math.stat.descriptive.DescriptiveStatistics
    public synchronized double[] getValues() {
        return super.getValues();
    }

    @Override // org.apache.commons.math.stat.descriptive.DescriptiveStatistics
    public synchronized int getWindowSize() {
        return super.getWindowSize();
    }

    @Override // org.apache.commons.math.stat.descriptive.DescriptiveStatistics
    public synchronized void setWindowSize(int windowSize) {
        super.setWindowSize(windowSize);
    }

    @Override // org.apache.commons.math.stat.descriptive.DescriptiveStatistics
    public synchronized java.lang.String toString() {
        return super.toString();
    }

    @Override // org.apache.commons.math.stat.descriptive.DescriptiveStatistics
    public synchronized org.apache.commons.math.stat.descriptive.SynchronizedDescriptiveStatistics copy() {
        org.apache.commons.math.stat.descriptive.SynchronizedDescriptiveStatistics result;
        result = new org.apache.commons.math.stat.descriptive.SynchronizedDescriptiveStatistics();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.SynchronizedDescriptiveStatistics source, org.apache.commons.math.stat.descriptive.SynchronizedDescriptiveStatistics dest) {
        synchronized (source) {
            synchronized (dest) {
                org.apache.commons.math.stat.descriptive.DescriptiveStatistics.copy(source, dest);
            }
        }
    }
}
