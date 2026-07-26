package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public interface BigMatrix extends org.apache.commons.math.linear.AnyMatrix {
    org.apache.commons.math.linear.BigMatrix add(org.apache.commons.math.linear.BigMatrix bigMatrix) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.BigMatrix copy();

    java.math.BigDecimal[] getColumn(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    double[] getColumnAsDoubleArray(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.BigMatrix getColumnMatrix(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    java.math.BigDecimal[][] getData();

    double[][] getDataAsDoubleArray();

    java.math.BigDecimal getDeterminant() throws org.apache.commons.math.linear.InvalidMatrixException;

    java.math.BigDecimal getEntry(int i, int i2) throws org.apache.commons.math.linear.MatrixIndexException;

    double getEntryAsDouble(int i, int i2) throws org.apache.commons.math.linear.MatrixIndexException;

    java.math.BigDecimal getNorm();

    int getRoundingMode();

    java.math.BigDecimal[] getRow(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    double[] getRowAsDoubleArray(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.BigMatrix getRowMatrix(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.BigMatrix getSubMatrix(int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.BigMatrix getSubMatrix(int[] iArr, int[] iArr2) throws org.apache.commons.math.linear.MatrixIndexException;

    java.math.BigDecimal getTrace();

    org.apache.commons.math.linear.BigMatrix inverse() throws org.apache.commons.math.linear.InvalidMatrixException;

    org.apache.commons.math.linear.BigMatrix multiply(org.apache.commons.math.linear.BigMatrix bigMatrix) throws java.lang.IllegalArgumentException;

    java.math.BigDecimal[] operate(java.math.BigDecimal[] bigDecimalArr) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.BigMatrix preMultiply(org.apache.commons.math.linear.BigMatrix bigMatrix) throws java.lang.IllegalArgumentException;

    java.math.BigDecimal[] preMultiply(java.math.BigDecimal[] bigDecimalArr) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.BigMatrix scalarAdd(java.math.BigDecimal bigDecimal);

    org.apache.commons.math.linear.BigMatrix scalarMultiply(java.math.BigDecimal bigDecimal);

    org.apache.commons.math.linear.BigMatrix solve(org.apache.commons.math.linear.BigMatrix bigMatrix) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException;

    java.math.BigDecimal[] solve(java.math.BigDecimal[] bigDecimalArr) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.BigMatrix subtract(org.apache.commons.math.linear.BigMatrix bigMatrix) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.BigMatrix transpose();
}
