package org.apache.commons.math.stat.descriptive;

/* JADX INFO: loaded from: classes4.dex */
public interface UnivariateStatistic {
    org.apache.commons.math.stat.descriptive.UnivariateStatistic copy();

    double evaluate(double[] dArr);

    double evaluate(double[] dArr, int i, int i2);
}
