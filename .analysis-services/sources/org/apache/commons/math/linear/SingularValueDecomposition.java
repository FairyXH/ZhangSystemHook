package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public interface SingularValueDecomposition {
    double getConditionNumber();

    org.apache.commons.math.linear.RealMatrix getCovariance(double d) throws java.lang.IllegalArgumentException;

    double getNorm();

    int getRank();

    org.apache.commons.math.linear.RealMatrix getS();

    double[] getSingularValues();

    org.apache.commons.math.linear.DecompositionSolver getSolver();

    org.apache.commons.math.linear.RealMatrix getU();

    org.apache.commons.math.linear.RealMatrix getUT();

    org.apache.commons.math.linear.RealMatrix getV();

    org.apache.commons.math.linear.RealMatrix getVT();
}
