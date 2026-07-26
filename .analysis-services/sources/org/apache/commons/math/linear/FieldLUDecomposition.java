package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public interface FieldLUDecomposition<T extends org.apache.commons.math.FieldElement<T>> {
    T getDeterminant();

    org.apache.commons.math.linear.FieldMatrix<T> getL();

    org.apache.commons.math.linear.FieldMatrix<T> getP();

    int[] getPivot();

    org.apache.commons.math.linear.FieldDecompositionSolver<T> getSolver();

    org.apache.commons.math.linear.FieldMatrix<T> getU();
}
