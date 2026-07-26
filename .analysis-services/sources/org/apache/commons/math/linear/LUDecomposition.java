package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public interface LUDecomposition {
    double getDeterminant();

    org.apache.commons.math.linear.RealMatrix getL();

    org.apache.commons.math.linear.RealMatrix getP();

    int[] getPivot();

    org.apache.commons.math.linear.DecompositionSolver getSolver();

    org.apache.commons.math.linear.RealMatrix getU();
}
