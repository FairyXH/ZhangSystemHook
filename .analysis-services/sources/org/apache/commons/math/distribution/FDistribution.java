package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public interface FDistribution extends org.apache.commons.math.distribution.ContinuousDistribution {
    double getDenominatorDegreesOfFreedom();

    double getNumeratorDegreesOfFreedom();

    @java.lang.Deprecated
    void setDenominatorDegreesOfFreedom(double d);

    @java.lang.Deprecated
    void setNumeratorDegreesOfFreedom(double d);
}
