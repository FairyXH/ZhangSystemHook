package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class CholeskyDecompositionImpl implements org.apache.commons.math.linear.CholeskyDecomposition {
    public static final double DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD = 1.0E-10d;
    public static final double DEFAULT_RELATIVE_SYMMETRY_THRESHOLD = 1.0E-15d;
    private org.apache.commons.math.linear.RealMatrix cachedL;
    private org.apache.commons.math.linear.RealMatrix cachedLT;
    private double[][] lTData;

    public CholeskyDecompositionImpl(org.apache.commons.math.linear.RealMatrix matrix) throws org.apache.commons.math.linear.NonSquareMatrixException, org.apache.commons.math.linear.NotSymmetricMatrixException, org.apache.commons.math.linear.NotPositiveDefiniteMatrixException {
        this(matrix, 1.0E-15d, 1.0E-10d);
    }

    public CholeskyDecompositionImpl(org.apache.commons.math.linear.RealMatrix matrix, double relativeSymmetryThreshold, double absolutePositivityThreshold) throws org.apache.commons.math.linear.NonSquareMatrixException, org.apache.commons.math.linear.NotSymmetricMatrixException, org.apache.commons.math.linear.NotPositiveDefiniteMatrixException {
        if (!matrix.isSquare()) {
            throw new org.apache.commons.math.linear.NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
        }
        int order = matrix.getRowDimension();
        this.lTData = matrix.getData();
        this.cachedL = null;
        this.cachedLT = null;
        for (int i = 0; i < order; i++) {
            double[] lI = this.lTData[i];
            for (int j = i + 1; j < order; j++) {
                double[] lJ = this.lTData[j];
                double lIJ = lI[j];
                double lJI = lJ[i];
                double maxDelta = org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(lIJ), org.apache.commons.math.util.FastMath.abs(lJI)) * relativeSymmetryThreshold;
                if (org.apache.commons.math.util.FastMath.abs(lIJ - lJI) > maxDelta) {
                    throw new org.apache.commons.math.linear.NotSymmetricMatrixException();
                }
                lJ[i] = 0.0d;
            }
        }
        for (int i2 = 0; i2 < order; i2++) {
            double[] ltI = this.lTData[i2];
            if (ltI[i2] < absolutePositivityThreshold) {
                throw new org.apache.commons.math.linear.NotPositiveDefiniteMatrixException();
            }
            ltI[i2] = org.apache.commons.math.util.FastMath.sqrt(ltI[i2]);
            double inverse = 1.0d / ltI[i2];
            for (int q = order - 1; q > i2; q--) {
                ltI[q] = ltI[q] * inverse;
                double[] ltQ = this.lTData[q];
                for (int p = q; p < order; p++) {
                    ltQ[p] = ltQ[p] - (ltI[q] * ltI[p]);
                }
            }
        }
    }

    @Override // org.apache.commons.math.linear.CholeskyDecomposition
    public org.apache.commons.math.linear.RealMatrix getL() {
        if (this.cachedL == null) {
            this.cachedL = getLT().transpose();
        }
        return this.cachedL;
    }

    @Override // org.apache.commons.math.linear.CholeskyDecomposition
    public org.apache.commons.math.linear.RealMatrix getLT() {
        if (this.cachedLT == null) {
            this.cachedLT = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(this.lTData);
        }
        return this.cachedLT;
    }

    @Override // org.apache.commons.math.linear.CholeskyDecomposition
    public double getDeterminant() {
        double determinant = 1.0d;
        for (int i = 0; i < this.lTData.length; i++) {
            double lTii = this.lTData[i][i];
            determinant *= lTii * lTii;
        }
        return determinant;
    }

    @Override // org.apache.commons.math.linear.CholeskyDecomposition
    public org.apache.commons.math.linear.DecompositionSolver getSolver() {
        return new org.apache.commons.math.linear.CholeskyDecompositionImpl.Solver(this.lTData);
    }

    private static class Solver implements org.apache.commons.math.linear.DecompositionSolver {
        private final double[][] lTData;

        private Solver(double[][] lTData) {
            this.lTData = lTData;
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public boolean isNonSingular() {
            return true;
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public double[] solve(double[] b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            int m = this.lTData.length;
            if (b.length != m) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(b.length), java.lang.Integer.valueOf(m));
            }
            double[] x = (double[]) b.clone();
            for (int j = 0; j < m; j++) {
                double[] lJ = this.lTData[j];
                x[j] = x[j] / lJ[j];
                double xJ = x[j];
                for (int i = j + 1; i < m; i++) {
                    x[i] = x[i] - (lJ[i] * xJ);
                }
            }
            for (int j2 = m - 1; j2 >= 0; j2--) {
                x[j2] = x[j2] / this.lTData[j2][j2];
                double xJ2 = x[j2];
                for (int i2 = 0; i2 < j2; i2++) {
                    x[i2] = x[i2] - (this.lTData[i2][j2] * xJ2);
                }
            }
            return x;
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealVector solve(org.apache.commons.math.linear.RealVector b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            try {
                return solve((org.apache.commons.math.linear.ArrayRealVector) b);
            } catch (java.lang.ClassCastException e) {
                int m = this.lTData.length;
                if (b.getDimension() != m) {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(b.getDimension()), java.lang.Integer.valueOf(m));
                }
                double[] x = b.getData();
                for (int j = 0; j < m; j++) {
                    double[] lJ = this.lTData[j];
                    x[j] = x[j] / lJ[j];
                    double xJ = x[j];
                    for (int i = j + 1; i < m; i++) {
                        x[i] = x[i] - (lJ[i] * xJ);
                    }
                }
                for (int j2 = m - 1; j2 >= 0; j2--) {
                    x[j2] = x[j2] / this.lTData[j2][j2];
                    double xJ2 = x[j2];
                    for (int i2 = 0; i2 < j2; i2++) {
                        x[i2] = x[i2] - (this.lTData[i2][j2] * xJ2);
                    }
                }
                return new org.apache.commons.math.linear.ArrayRealVector(x, false);
            }
        }

        public org.apache.commons.math.linear.ArrayRealVector solve(org.apache.commons.math.linear.ArrayRealVector b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            return new org.apache.commons.math.linear.ArrayRealVector(solve(b.getDataRef()), false);
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealMatrix solve(org.apache.commons.math.linear.RealMatrix b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            int m = this.lTData.length;
            if (b.getRowDimension() != m) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(b.getRowDimension()), java.lang.Integer.valueOf(b.getColumnDimension()), java.lang.Integer.valueOf(m), "n");
            }
            int nColB = b.getColumnDimension();
            double[][] x = b.getData();
            for (int j = 0; j < m; j++) {
                double[] lJ = this.lTData[j];
                double lJJ = lJ[j];
                double[] xJ = x[j];
                for (int k = 0; k < nColB; k++) {
                    xJ[k] = xJ[k] / lJJ;
                }
                for (int i = j + 1; i < m; i++) {
                    double[] xI = x[i];
                    double lJI = lJ[i];
                    for (int k2 = 0; k2 < nColB; k2++) {
                        xI[k2] = xI[k2] - (xJ[k2] * lJI);
                    }
                }
            }
            for (int j2 = m - 1; j2 >= 0; j2--) {
                double lJJ2 = this.lTData[j2][j2];
                double[] xJ2 = x[j2];
                for (int k3 = 0; k3 < nColB; k3++) {
                    xJ2[k3] = xJ2[k3] / lJJ2;
                }
                for (int i2 = 0; i2 < j2; i2++) {
                    double[] xI2 = x[i2];
                    double lIJ = this.lTData[i2][j2];
                    for (int k4 = 0; k4 < nColB; k4++) {
                        xI2[k4] = xI2[k4] - (xJ2[k4] * lIJ);
                    }
                }
            }
            return new org.apache.commons.math.linear.Array2DRowRealMatrix(x, false);
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealMatrix getInverse() throws org.apache.commons.math.linear.InvalidMatrixException {
            return solve(org.apache.commons.math.linear.MatrixUtils.createRealIdentityMatrix(this.lTData.length));
        }
    }
}
