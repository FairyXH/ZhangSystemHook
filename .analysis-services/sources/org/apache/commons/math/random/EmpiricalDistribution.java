package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public interface EmpiricalDistribution {
    int getBinCount();

    java.util.List<org.apache.commons.math.stat.descriptive.SummaryStatistics> getBinStats();

    double getNextValue() throws java.lang.IllegalStateException;

    org.apache.commons.math.stat.descriptive.StatisticalSummary getSampleStats() throws java.lang.IllegalStateException;

    double[] getUpperBounds();

    boolean isLoaded();

    void load(java.io.File file) throws java.io.IOException;

    void load(java.net.URL url) throws java.io.IOException;

    void load(double[] dArr);
}
