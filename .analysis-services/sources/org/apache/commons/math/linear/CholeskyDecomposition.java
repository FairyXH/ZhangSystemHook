package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public interface CholeskyDecomposition {
    double getDeterminant();

    org.apache.commons.math.linear.RealMatrix getL();

    org.apache.commons.math.linear.RealMatrix getLT();

    org.apache.commons.math.linear.DecompositionSolver getSolver();
}
