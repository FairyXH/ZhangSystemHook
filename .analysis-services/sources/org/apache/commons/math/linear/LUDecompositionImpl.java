package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class LUDecompositionImpl implements org.apache.commons.math.linear.LUDecomposition {
    private static final double DEFAULT_TOO_SMALL = 1.0E-11d;
    private org.apache.commons.math.linear.RealMatrix cachedL;
    private org.apache.commons.math.linear.RealMatrix cachedP;
    private org.apache.commons.math.linear.RealMatrix cachedU;
    private boolean even;
    private double[][] lu;
    private int[] pivot;
    private boolean singular;

    public LUDecompositionImpl(org.apache.commons.math.linear.RealMatrix matrix) throws org.apache.commons.math.linear.InvalidMatrixException {
        this(matrix, 1.0E-11d);
    }

    public LUDecompositionImpl(org.apache.commons.math.linear.RealMatrix matrix, double singularityThreshold) throws org.apache.commons.math.linear.NonSquareMatrixException {
        if (!matrix.isSquare()) {
            throw new org.apache.commons.math.linear.NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
        }
        int m = matrix.getColumnDimension();
        this.lu = matrix.getData();
        this.pivot = new int[m];
        this.cachedL = null;
        this.cachedU = null;
        this.cachedP = null;
        for (int row = 0; row < m; row++) {
            this.pivot[row] = row;
        }
        this.even = true;
        this.singular = false;
        for (int col = 0; col < m; col++) {
            for (int row2 = 0; row2 < col; row2++) {
                double[] luRow = this.lu[row2];
                double sum = luRow[col];
                for (int i = 0; i < row2; i++) {
                    sum -= luRow[i] * this.lu[i][col];
                }
                luRow[col] = sum;
            }
            int max = col;
            double largest = Double.NEGATIVE_INFINITY;
            for (int row3 = col; row3 < m; row3++) {
                double[] luRow2 = this.lu[row3];
                double sum2 = luRow2[col];
                for (int i2 = 0; i2 < col; i2++) {
                    sum2 -= luRow2[i2] * this.lu[i2][col];
                }
                luRow2[col] = sum2;
                if (org.apache.commons.math.util.FastMath.abs(sum2) > largest) {
                    largest = org.apache.commons.math.util.FastMath.abs(sum2);
                    max = row3;
                }
            }
            if (org.apache.commons.math.util.FastMath.abs(this.lu[max][col]) < singularityThreshold) {
                this.singular = true;
                return;
            }
            if (max != col) {
                double[] luMax = this.lu[max];
                double[] luCol = this.lu[col];
                for (int i3 = 0; i3 < m; i3++) {
                    double tmp = luMax[i3];
                    luMax[i3] = luCol[i3];
                    luCol[i3] = tmp;
                }
                int temp = this.pivot[max];
                this.pivot[max] = this.pivot[col];
                this.pivot[col] = temp;
                this.even = !this.even;
            }
            double luDiag = this.lu[col][col];
            for (int row4 = col + 1; row4 < m; row4++) {
                double[] dArr = this.lu[row4];
                dArr[col] = dArr[col] / luDiag;
            }
        }
    }

    @Override // org.apache.commons.math.linear.LUDecomposition
    public org.apache.commons.math.linear.RealMatrix getL() {
        if (this.cachedL == null && !this.singular) {
            int m = this.pivot.length;
            this.cachedL = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, m);
            for (int i = 0; i < m; i++) {
                double[] luI = this.lu[i];
                for (int j = 0; j < i; j++) {
                    this.cachedL.setEntry(i, j, luI[j]);
                }
                this.cachedL.setEntry(i, i, 1.0d);
            }
        }
        return this.cachedL;
    }

    @Override // org.apache.commons.math.linear.LUDecomposition
    public org.apache.commons.math.linear.RealMatrix getU() {
        if (this.cachedU == null && !this.singular) {
            int m = this.pivot.length;
            this.cachedU = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, m);
            for (int i = 0; i < m; i++) {
                double[] luI = this.lu[i];
                for (int j = i; j < m; j++) {
                    this.cachedU.setEntry(i, j, luI[j]);
                }
            }
        }
        return this.cachedU;
    }

    @Override // org.apache.commons.math.linear.LUDecomposition
    public org.apache.commons.math.linear.RealMatrix getP() {
        if (this.cachedP == null && !this.singular) {
            int m = this.pivot.length;
            this.cachedP = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, m);
            for (int i = 0; i < m; i++) {
                this.cachedP.setEntry(i, this.pivot[i], 1.0d);
            }
        }
        return this.cachedP;
    }

    @Override // org.apache.commons.math.linear.LUDecomposition
    public int[] getPivot() {
        return (int[]) this.pivot.clone();
    }

    @Override // org.apache.commons.math.linear.LUDecomposition
    public double getDeterminant() {
        if (this.singular) {
            return 0.0d;
        }
        int m = this.pivot.length;
        double determinant = this.even ? 1.0d : -1.0d;
        for (int i = 0; i < m; i++) {
            determinant *= this.lu[i][i];
        }
        return determinant;
    }

    @Override // org.apache.commons.math.linear.LUDecomposition
    public org.apache.commons.math.linear.DecompositionSolver getSolver() {
        return new org.apache.commons.math.linear.LUDecompositionImpl.Solver(this.lu, this.pivot, this.singular);
    }

    private static class Solver implements org.apache.commons.math.linear.DecompositionSolver {
        private final double[][] lu;
        private final int[] pivot;
        private final boolean singular;

        private Solver(double[][] lu, int[] pivot, boolean singular) {
            this.lu = lu;
            this.pivot = pivot;
            this.singular = singular;
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public boolean isNonSingular() {
            return !this.singular;
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public double[] solve(double[] b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            int m = this.pivot.length;
            if (b.length != m) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(b.length), java.lang.Integer.valueOf(m));
            }
            if (this.singular) {
                throw new org.apache.commons.math.linear.SingularMatrixException();
            }
            double[] bp = new double[m];
            for (int row = 0; row < m; row++) {
                bp[row] = b[this.pivot[row]];
            }
            for (int col = 0; col < m; col++) {
                double bpCol = bp[col];
                for (int i = col + 1; i < m; i++) {
                    bp[i] = bp[i] - (this.lu[i][col] * bpCol);
                }
            }
            for (int col2 = m - 1; col2 >= 0; col2--) {
                bp[col2] = bp[col2] / this.lu[col2][col2];
                double bpCol2 = bp[col2];
                for (int i2 = 0; i2 < col2; i2++) {
                    bp[i2] = bp[i2] - (this.lu[i2][col2] * bpCol2);
                }
            }
            return bp;
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealVector solve(org.apache.commons.math.linear.RealVector b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            try {
                return solve((org.apache.commons.math.linear.ArrayRealVector) b);
            } catch (java.lang.ClassCastException e) {
                int m = this.pivot.length;
                if (b.getDimension() != m) {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(b.getDimension()), java.lang.Integer.valueOf(m));
                }
                if (this.singular) {
                    throw new org.apache.commons.math.linear.SingularMatrixException();
                }
                double[] bp = new double[m];
                for (int row = 0; row < m; row++) {
                    bp[row] = b.getEntry(this.pivot[row]);
                }
                for (int col = 0; col < m; col++) {
                    double bpCol = bp[col];
                    for (int i = col + 1; i < m; i++) {
                        bp[i] = bp[i] - (this.lu[i][col] * bpCol);
                    }
                }
                for (int col2 = m - 1; col2 >= 0; col2--) {
                    bp[col2] = bp[col2] / this.lu[col2][col2];
                    double bpCol2 = bp[col2];
                    for (int i2 = 0; i2 < col2; i2++) {
                        bp[i2] = bp[i2] - (this.lu[i2][col2] * bpCol2);
                    }
                }
                return new org.apache.commons.math.linear.ArrayRealVector(bp, false);
            }
        }

        public org.apache.commons.math.linear.ArrayRealVector solve(org.apache.commons.math.linear.ArrayRealVector b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            return new org.apache.commons.math.linear.ArrayRealVector(solve(b.getDataRef()), false);
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealMatrix solve(org.apache.commons.math.linear.RealMatrix b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            int m = this.pivot.length;
            if (b.getRowDimension() != m) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(b.getRowDimension()), java.lang.Integer.valueOf(b.getColumnDimension()), java.lang.Integer.valueOf(m), "n");
            }
            if (this.singular) {
                throw new org.apache.commons.math.linear.SingularMatrixException();
            }
            int nColB = b.getColumnDimension();
            double[][] bp = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, m, nColB);
            for (int row = 0; row < m; row++) {
                double[] bpRow = bp[row];
                int pRow = this.pivot[row];
                for (int col = 0; col < nColB; col++) {
                    bpRow[col] = b.getEntry(pRow, col);
                }
            }
            for (int col2 = 0; col2 < m; col2++) {
                double[] bpCol = bp[col2];
                for (int i = col2 + 1; i < m; i++) {
                    double[] bpI = bp[i];
                    double luICol = this.lu[i][col2];
                    for (int j = 0; j < nColB; j++) {
                        bpI[j] = bpI[j] - (bpCol[j] * luICol);
                    }
                }
            }
            for (int col3 = m - 1; col3 >= 0; col3--) {
                double[] bpCol2 = bp[col3];
                double luDiag = this.lu[col3][col3];
                for (int j2 = 0; j2 < nColB; j2++) {
                    bpCol2[j2] = bpCol2[j2] / luDiag;
                }
                for (int i2 = 0; i2 < col3; i2++) {
                    double[] bpI2 = bp[i2];
                    double luICol2 = this.lu[i2][col3];
                    for (int j3 = 0; j3 < nColB; j3++) {
                        bpI2[j3] = bpI2[j3] - (bpCol2[j3] * luICol2);
                    }
                }
            }
            return new org.apache.commons.math.linear.Array2DRowRealMatrix(bp, false);
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealMatrix getInverse() throws org.apache.commons.math.linear.InvalidMatrixException {
            return solve(org.apache.commons.math.linear.MatrixUtils.createRealIdentityMatrix(this.pivot.length));
        }
    }
}
