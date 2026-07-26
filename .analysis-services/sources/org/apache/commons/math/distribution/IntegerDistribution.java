package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public interface IntegerDistribution extends org.apache.commons.math.distribution.DiscreteDistribution {
    double cumulativeProbability(int i) throws org.apache.commons.math.MathException;

    double cumulativeProbability(int i, int i2) throws org.apache.commons.math.MathException;

    int inverseCumulativeProbability(double d) throws org.apache.commons.math.MathException;

    double probability(int i);
}
