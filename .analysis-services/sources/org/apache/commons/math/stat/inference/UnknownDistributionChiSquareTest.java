package org.apache.commons.math.stat.inference;

/* JADX INFO: loaded from: classes4.dex */
public interface UnknownDistributionChiSquareTest extends org.apache.commons.math.stat.inference.ChiSquareTest {
    double chiSquareDataSetsComparison(long[] jArr, long[] jArr2) throws java.lang.IllegalArgumentException;

    double chiSquareTestDataSetsComparison(long[] jArr, long[] jArr2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    boolean chiSquareTestDataSetsComparison(long[] jArr, long[] jArr2, double d) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;
}
