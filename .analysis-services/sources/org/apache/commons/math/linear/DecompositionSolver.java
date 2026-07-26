package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public interface DecompositionSolver {
    org.apache.commons.math.linear.RealMatrix getInverse() throws org.apache.commons.math.linear.InvalidMatrixException;

    boolean isNonSingular();

    org.apache.commons.math.linear.RealMatrix solve(org.apache.commons.math.linear.RealMatrix realMatrix) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException;

    org.apache.commons.math.linear.RealVector solve(org.apache.commons.math.linear.RealVector realVector) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException;

    double[] solve(double[] dArr) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException;
}
