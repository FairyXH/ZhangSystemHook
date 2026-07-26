package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public interface ChiSquaredDistribution extends org.apache.commons.math.distribution.ContinuousDistribution, org.apache.commons.math.distribution.HasDensity<java.lang.Double> {
    @Override // org.apache.commons.math.distribution.HasDensity
    double density(java.lang.Double d);

    double getDegreesOfFreedom();

    @java.lang.Deprecated
    void setDegreesOfFreedom(double d);
}
