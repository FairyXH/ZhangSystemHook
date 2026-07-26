package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public interface FieldDecompositionSolver<T extends org.apache.commons.math.FieldElement<T>> {
    org.apache.commons.math.linear.FieldMatrix<T> getInverse() throws org.apache.commons.math.linear.InvalidMatrixException;

    boolean isNonSingular();

    org.apache.commons.math.linear.FieldMatrix<T> solve(org.apache.commons.math.linear.FieldMatrix<T> fieldMatrix) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.FieldVector<T> solve(org.apache.commons.math.linear.FieldVector<T> fieldVector) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException;

    T[] solve(T[] tArr) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException;
}
