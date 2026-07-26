package org.apache.commons.math.stat.descriptive;

/* JADX INFO: loaded from: classes4.dex */
public class AggregateSummaryStatistics implements org.apache.commons.math.stat.descriptive.StatisticalSummary, java.io.Serializable {
    private static final long serialVersionUID = -8207112444016386906L;
    private final org.apache.commons.math.stat.descriptive.SummaryStatistics statistics;
    private final org.apache.commons.math.stat.descriptive.SummaryStatistics statisticsPrototype;

    public AggregateSummaryStatistics() {
        this(new org.apache.commons.math.stat.descriptive.SummaryStatistics());
    }

    public AggregateSummaryStatistics(org.apache.commons.math.stat.descriptive.SummaryStatistics prototypeStatistics) {
        this(prototypeStatistics, prototypeStatistics == null ? null : new org.apache.commons.math.stat.descriptive.SummaryStatistics(prototypeStatistics));
    }

    public AggregateSummaryStatistics(org.apache.commons.math.stat.descriptive.SummaryStatistics prototypeStatistics, org.apache.commons.math.stat.descriptive.SummaryStatistics initialStatistics) {
        this.statisticsPrototype = prototypeStatistics == null ? new org.apache.commons.math.stat.descriptive.SummaryStatistics() : prototypeStatistics;
        this.statistics = initialStatistics == null ? new org.apache.commons.math.stat.descriptive.SummaryStatistics() : initialStatistics;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getMax() {
        double max;
        synchronized (this.statistics) {
            max = this.statistics.getMax();
        }
        return max;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getMean() {
        double mean;
        synchronized (this.statistics) {
            mean = this.statistics.getMean();
        }
        return mean;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getMin() {
        double min;
        synchronized (this.statistics) {
            min = this.statistics.getMin();
        }
        return min;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public long getN() {
        long n;
        synchronized (this.statistics) {
            n = this.statistics.getN();
        }
        return n;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getStandardDeviation() {
        double standardDeviation;
        synchronized (this.statistics) {
            standardDeviation = this.statistics.getStandardDeviation();
        }
        return standardDeviation;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getSum() {
        double sum;
        synchronized (this.statistics) {
            sum = this.statistics.getSum();
        }
        return sum;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getVariance() {
        double variance;
        synchronized (this.statistics) {
            variance = this.statistics.getVariance();
        }
        return variance;
    }

    public double getSumOfLogs() {
        double sumOfLogs;
        synchronized (this.statistics) {
            sumOfLogs = this.statistics.getSumOfLogs();
        }
        return sumOfLogs;
    }

    public double getGeometricMean() {
        double geometricMean;
        synchronized (this.statistics) {
            geometricMean = this.statistics.getGeometricMean();
        }
        return geometricMean;
    }

    public double getSumsq() {
        double sumsq;
        synchronized (this.statistics) {
            sumsq = this.statistics.getSumsq();
        }
        return sumsq;
    }

    public double getSecondMoment() {
        double secondMoment;
        synchronized (this.statistics) {
            secondMoment = this.statistics.getSecondMoment();
        }
        return secondMoment;
    }

    public org.apache.commons.math.stat.descriptive.StatisticalSummary getSummary() {
        org.apache.commons.math.stat.descriptive.StatisticalSummaryValues statisticalSummaryValues;
        synchronized (this.statistics) {
            statisticalSummaryValues = new org.apache.commons.math.stat.descriptive.StatisticalSummaryValues(getMean(), getVariance(), getN(), getMax(), getMin(), getSum());
        }
        return statisticalSummaryValues;
    }

    public org.apache.commons.math.stat.descriptive.SummaryStatistics createContributingStatistics() {
        org.apache.commons.math.stat.descriptive.SummaryStatistics contributingStatistics = new org.apache.commons.math.stat.descriptive.AggregateSummaryStatistics.AggregatingSummaryStatistics(this.statistics);
        org.apache.commons.math.stat.descriptive.SummaryStatistics.copy(this.statisticsPrototype, contributingStatistics);
        return contributingStatistics;
    }

    public static org.apache.commons.math.stat.descriptive.StatisticalSummaryValues aggregate(java.util.Collection<org.apache.commons.math.stat.descriptive.SummaryStatistics> statistics) {
        double variance;
        if (statistics == null) {
            return null;
        }
        java.util.Iterator<org.apache.commons.math.stat.descriptive.SummaryStatistics> iterator = statistics.iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        org.apache.commons.math.stat.descriptive.SummaryStatistics current = iterator.next();
        long n = current.getN();
        double min = current.getMin();
        double sum = current.getSum();
        double max = current.getMax();
        double m2 = current.getSecondMoment();
        double min2 = min;
        double sum2 = sum;
        double max2 = max;
        double m22 = m2;
        double mean = current.getMean();
        long n2 = n;
        while (iterator.hasNext()) {
            org.apache.commons.math.stat.descriptive.SummaryStatistics current2 = iterator.next();
            if (current2.getMin() < min2 || java.lang.Double.isNaN(min2)) {
                min2 = current2.getMin();
            }
            if (current2.getMax() > max2 || java.lang.Double.isNaN(max2)) {
                max2 = current2.getMax();
            }
            sum2 += current2.getSum();
            double oldN = n2;
            double curN = current2.getN();
            n2 = (long) (n2 + curN);
            double meanDiff = current2.getMean() - mean;
            mean = sum2 / n2;
            m22 = m22 + current2.getSecondMoment() + ((((meanDiff * meanDiff) * oldN) * curN) / n2);
            iterator = iterator;
        }
        if (n2 == 0) {
            variance = Double.NaN;
        } else if (n2 == 1) {
            variance = 0.0d;
        } else {
            variance = m22 / (n2 - 1);
        }
        return new org.apache.commons.math.stat.descriptive.StatisticalSummaryValues(mean, variance, n2, max2, min2, sum2);
    }

    private static class AggregatingSummaryStatistics extends org.apache.commons.math.stat.descriptive.SummaryStatistics {
        private static final long serialVersionUID = 1;
        private final org.apache.commons.math.stat.descriptive.SummaryStatistics aggregateStatistics;

        public AggregatingSummaryStatistics(org.apache.commons.math.stat.descriptive.SummaryStatistics aggregateStatistics) {
            this.aggregateStatistics = aggregateStatistics;
        }

        @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
        public void addValue(double value) {
            super.addValue(value);
            synchronized (this.aggregateStatistics) {
                this.aggregateStatistics.addValue(value);
            }
        }

        @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
        public boolean equals(java.lang.Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof org.apache.commons.math.stat.descriptive.AggregateSummaryStatistics.AggregatingSummaryStatistics)) {
                return false;
            }
            org.apache.commons.math.stat.descriptive.AggregateSummaryStatistics.AggregatingSummaryStatistics stat = (org.apache.commons.math.stat.descriptive.AggregateSummaryStatistics.AggregatingSummaryStatistics) object;
            return super.equals(stat) && this.aggregateStatistics.equals(stat.aggregateStatistics);
        }

        @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
        public int hashCode() {
            return super.hashCode() + 123 + this.aggregateStatistics.hashCode();
        }
    }
}
