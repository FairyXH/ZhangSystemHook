package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public interface RandomData {
    double nextExponential(double d);

    double nextGaussian(double d, double d2);

    java.lang.String nextHexString(int i);

    int nextInt(int i, int i2);

    long nextLong(long j, long j2);

    int[] nextPermutation(int i, int i2);

    long nextPoisson(double d);

    java.lang.Object[] nextSample(java.util.Collection<?> collection, int i);

    java.lang.String nextSecureHexString(int i);

    int nextSecureInt(int i, int i2);

    long nextSecureLong(long j, long j2);

    double nextUniform(double d, double d2);
}
