package org.apache.commons.math.stat.inference;

/* JADX INFO: loaded from: classes4.dex */
public interface OneWayAnova {
    double anovaFValue(java.util.Collection<double[]> collection) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    double anovaPValue(java.util.Collection<double[]> collection) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    boolean anovaTest(java.util.Collection<double[]> collection, double d) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;
}
