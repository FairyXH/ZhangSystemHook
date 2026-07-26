package org.apache.commons.math.stat.inference;

/* JADX INFO: loaded from: classes4.dex */
public interface ChiSquareTest {
    double chiSquare(double[] dArr, long[] jArr) throws java.lang.IllegalArgumentException;

    double chiSquare(long[][] jArr) throws java.lang.IllegalArgumentException;

    double chiSquareTest(double[] dArr, long[] jArr) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    double chiSquareTest(long[][] jArr) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    boolean chiSquareTest(double[] dArr, long[] jArr, double d) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    boolean chiSquareTest(long[][] jArr, double d) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;
}
