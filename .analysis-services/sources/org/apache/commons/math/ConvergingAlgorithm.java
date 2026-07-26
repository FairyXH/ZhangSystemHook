package org.apache.commons.math;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public interface ConvergingAlgorithm {
    double getAbsoluteAccuracy();

    int getIterationCount();

    int getMaximalIterationCount();

    double getRelativeAccuracy();

    void resetAbsoluteAccuracy();

    void resetMaximalIterationCount();

    void resetRelativeAccuracy();

    void setAbsoluteAccuracy(double d);

    void setMaximalIterationCount(int i);

    void setRelativeAccuracy(double d);
}
