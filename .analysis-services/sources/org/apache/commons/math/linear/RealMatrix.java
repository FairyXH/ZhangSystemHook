package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public interface RealMatrix extends org.apache.commons.math.linear.AnyMatrix {
    org.apache.commons.math.linear.RealMatrix add(org.apache.commons.math.linear.RealMatrix realMatrix) throws java.lang.IllegalArgumentException;

    void addToEntry(int i, int i2, double d) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.RealMatrix copy();

    void copySubMatrix(int i, int i2, int i3, int i4, double[][] dArr) throws org.apache.commons.math.linear.MatrixIndexException, java.lang.IllegalArgumentException;

    void copySubMatrix(int[] iArr, int[] iArr2, double[][] dArr) throws org.apache.commons.math.linear.MatrixIndexException, java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.RealMatrix createMatrix(int i, int i2);

    double[] getColumn(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.RealMatrix getColumnMatrix(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.RealVector getColumnVector(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    double[][] getData();

    @java.lang.Deprecated
    double getDeterminant();

    double getEntry(int i, int i2) throws org.apache.commons.math.linear.MatrixIndexException;

    double getFrobeniusNorm();

    double getNorm();

    double[] getRow(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.RealMatrix getRowMatrix(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.RealVector getRowVector(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.RealMatrix getSubMatrix(int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.RealMatrix getSubMatrix(int[] iArr, int[] iArr2) throws org.apache.commons.math.linear.MatrixIndexException;

    double getTrace() throws org.apache.commons.math.linear.NonSquareMatrixException;

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealMatrix inverse() throws org.apache.commons.math.linear.InvalidMatrixException;

    @java.lang.Deprecated
    boolean isSingular();

    org.apache.commons.math.linear.RealMatrix multiply(org.apache.commons.math.linear.RealMatrix realMatrix) throws java.lang.IllegalArgumentException;

    void multiplyEntry(int i, int i2, double d) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.RealVector operate(org.apache.commons.math.linear.RealVector realVector) throws java.lang.IllegalArgumentException;

    double[] operate(double[] dArr) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.RealMatrix preMultiply(org.apache.commons.math.linear.RealMatrix realMatrix) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.RealVector preMultiply(org.apache.commons.math.linear.RealVector realVector) throws java.lang.IllegalArgumentException;

    double[] preMultiply(double[] dArr) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.RealMatrix scalarAdd(double d);

    org.apache.commons.math.linear.RealMatrix scalarMultiply(double d);

    void setColumn(int i, double[] dArr) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException;

    void setColumnMatrix(int i, org.apache.commons.math.linear.RealMatrix realMatrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException;

    void setColumnVector(int i, org.apache.commons.math.linear.RealVector realVector) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException;

    void setEntry(int i, int i2, double d) throws org.apache.commons.math.linear.MatrixIndexException;

    void setRow(int i, double[] dArr) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException;

    void setRowMatrix(int i, org.apache.commons.math.linear.RealMatrix realMatrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException;

    void setRowVector(int i, org.apache.commons.math.linear.RealVector realVector) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException;

    void setSubMatrix(double[][] dArr, int i, int i2) throws org.apache.commons.math.linear.MatrixIndexException;

    @java.lang.Deprecated
    org.apache.commons.math.linear.RealMatrix solve(org.apache.commons.math.linear.RealMatrix realMatrix) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException;

    @java.lang.Deprecated
    double[] solve(double[] dArr) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.RealMatrix subtract(org.apache.commons.math.linear.RealMatrix realMatrix) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.RealMatrix transpose();

    double walkInColumnOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor realMatrixChangingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException;

    double walkInColumnOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor realMatrixChangingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException;

    double walkInColumnOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor realMatrixPreservingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException;

    double walkInColumnOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor realMatrixPreservingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException;

    double walkInOptimizedOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor realMatrixChangingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException;

    double walkInOptimizedOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor realMatrixChangingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException;

    double walkInOptimizedOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor realMatrixPreservingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException;

    double walkInOptimizedOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor realMatrixPreservingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException;

    double walkInRowOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor realMatrixChangingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException;

    double walkInRowOrder(org.apache.commons.math.linear.RealMatrixChangingVisitor realMatrixChangingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException;

    double walkInRowOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor realMatrixPreservingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException;

    double walkInRowOrder(org.apache.commons.math.linear.RealMatrixPreservingVisitor realMatrixPreservingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException;
}
