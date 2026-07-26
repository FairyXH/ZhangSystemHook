package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public interface NormalDistribution extends org.apache.commons.math.distribution.ContinuousDistribution, org.apache.commons.math.distribution.HasDensity<java.lang.Double> {
    @Override // org.apache.commons.math.distribution.HasDensity
    double density(java.lang.Double d);

    double getMean();

    double getStandardDeviation();

    @java.lang.Deprecated
    void setMean(double d);

    @java.lang.Deprecated
    void setStandardDeviation(double d);
}
