package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public interface BinomialDistribution extends org.apache.commons.math.distribution.IntegerDistribution {
    int getNumberOfTrials();

    double getProbabilityOfSuccess();

    @java.lang.Deprecated
    void setNumberOfTrials(int i);

    @java.lang.Deprecated
    void setProbabilityOfSuccess(double d);
}
