package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public interface FieldMatrix<T extends org.apache.commons.math.FieldElement<T>> extends org.apache.commons.math.linear.AnyMatrix {
    org.apache.commons.math.linear.FieldMatrix<T> add(org.apache.commons.math.linear.FieldMatrix<T> fieldMatrix) throws java.lang.IllegalArgumentException;

    void addToEntry(int i, int i2, T t) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.FieldMatrix<T> copy();

    void copySubMatrix(int i, int i2, int i3, int i4, T[][] tArr) throws org.apache.commons.math.linear.MatrixIndexException, java.lang.IllegalArgumentException;

    void copySubMatrix(int[] iArr, int[] iArr2, T[][] tArr) throws org.apache.commons.math.linear.MatrixIndexException, java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldMatrix<T> createMatrix(int i, int i2);

    T[] getColumn(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.FieldMatrix<T> getColumnMatrix(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.FieldVector<T> getColumnVector(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    T[][] getData();

    T getEntry(int i, int i2) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.Field<T> getField();

    T[] getRow(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.FieldMatrix<T> getRowMatrix(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.FieldVector<T> getRowVector(int i) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.FieldMatrix<T> getSubMatrix(int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.FieldMatrix<T> getSubMatrix(int[] iArr, int[] iArr2) throws org.apache.commons.math.linear.MatrixIndexException;

    T getTrace() throws org.apache.commons.math.linear.NonSquareMatrixException;

    org.apache.commons.math.linear.FieldMatrix<T> multiply(org.apache.commons.math.linear.FieldMatrix<T> fieldMatrix) throws java.lang.IllegalArgumentException;

    void multiplyEntry(int i, int i2, T t) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.FieldVector<T> operate(org.apache.commons.math.linear.FieldVector<T> fieldVector) throws java.lang.IllegalArgumentException;

    T[] operate(T[] tArr) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldMatrix<T> preMultiply(org.apache.commons.math.linear.FieldMatrix<T> fieldMatrix) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldVector<T> preMultiply(org.apache.commons.math.linear.FieldVector<T> fieldVector) throws java.lang.IllegalArgumentException;

    T[] preMultiply(T[] tArr) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldMatrix<T> scalarAdd(T t);

    org.apache.commons.math.linear.FieldMatrix<T> scalarMultiply(T t);

    void setColumn(int i, T[] tArr) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException;

    void setColumnMatrix(int i, org.apache.commons.math.linear.FieldMatrix<T> fieldMatrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException;

    void setColumnVector(int i, org.apache.commons.math.linear.FieldVector<T> fieldVector) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException;

    void setEntry(int i, int i2, T t) throws org.apache.commons.math.linear.MatrixIndexException;

    void setRow(int i, T[] tArr) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException;

    void setRowMatrix(int i, org.apache.commons.math.linear.FieldMatrix<T> fieldMatrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException;

    void setRowVector(int i, org.apache.commons.math.linear.FieldVector<T> fieldVector) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException;

    void setSubMatrix(T[][] tArr, int i, int i2) throws org.apache.commons.math.linear.MatrixIndexException;

    org.apache.commons.math.linear.FieldMatrix<T> subtract(org.apache.commons.math.linear.FieldMatrix<T> fieldMatrix) throws java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldMatrix<T> transpose();

    T walkInColumnOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException;

    T walkInColumnOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException;

    T walkInColumnOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException;

    T walkInColumnOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException;

    T walkInOptimizedOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException;

    T walkInOptimizedOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException;

    T walkInOptimizedOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException;

    T walkInOptimizedOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException;

    T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException;

    T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException;

    T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) throws org.apache.commons.math.linear.MatrixVisitorException;

    T walkInRowOrder(org.apache.commons.math.linear.FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i, int i2, int i3, int i4) throws org.apache.commons.math.linear.MatrixVisitorException, org.apache.commons.math.linear.MatrixIndexException;
}
