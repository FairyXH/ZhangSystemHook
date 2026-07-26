package org.apache.commons.math.stat.descriptive;

/* JADX INFO: loaded from: classes4.dex */
public interface StorelessUnivariateStatistic extends org.apache.commons.math.stat.descriptive.UnivariateStatistic {
    void clear();

    org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic copy();

    long getN();

    double getResult();

    void increment(double d);

    void incrementAll(double[] dArr);

    void incrementAll(double[] dArr, int i, int i2);
}
