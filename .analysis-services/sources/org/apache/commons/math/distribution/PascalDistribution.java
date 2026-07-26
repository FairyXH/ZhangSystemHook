package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public interface PascalDistribution extends org.apache.commons.math.distribution.IntegerDistribution {
    int getNumberOfSuccesses();

    double getProbabilityOfSuccess();

    @java.lang.Deprecated
    void setNumberOfSuccesses(int i);

    @java.lang.Deprecated
    void setProbabilityOfSuccess(double d);
}
