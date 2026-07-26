package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public interface CauchyDistribution extends org.apache.commons.math.distribution.ContinuousDistribution {
    double getMedian();

    double getScale();

    @java.lang.Deprecated
    void setMedian(double d);

    @java.lang.Deprecated
    void setScale(double d);
}
