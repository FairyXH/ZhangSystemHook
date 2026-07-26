package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class QRDecompositionImpl implements org.apache.commons.math.linear.QRDecomposition {
    private org.apache.commons.math.linear.RealMatrix cachedH;
    private org.apache.commons.math.linear.RealMatrix cachedQ;
    private org.apache.commons.math.linear.RealMatrix cachedQT;
    private org.apache.commons.math.linear.RealMatrix cachedR;
    private double[][] qrt;
    private double[] rDiag;

    public QRDecompositionImpl(org.apache.commons.math.linear.RealMatrix matrix) {
        int m = matrix.getRowDimension();
        int n = matrix.getColumnDimension();
        this.qrt = matrix.transpose().getData();
        this.rDiag = new double[org.apache.commons.math.util.FastMath.min(m, n)];
        this.cachedQ = null;
        this.cachedQT = null;
        this.cachedR = null;
        this.cachedH = null;
        for (int minor = 0; minor < org.apache.commons.math.util.FastMath.min(m, n); minor++) {
            double[] qrtMinor = this.qrt[minor];
            double xNormSqr = 0.0d;
            for (int row = minor; row < m; row++) {
                double c = qrtMinor[row];
                xNormSqr += c * c;
            }
            double a = qrtMinor[minor] > 0.0d ? -org.apache.commons.math.util.FastMath.sqrt(xNormSqr) : org.apache.commons.math.util.FastMath.sqrt(xNormSqr);
            this.rDiag[minor] = a;
            if (a != 0.0d) {
                qrtMinor[minor] = qrtMinor[minor] - a;
                for (int col = minor + 1; col < n; col++) {
                    double[] qrtCol = this.qrt[col];
                    double alpha = 0.0d;
                    for (int row2 = minor; row2 < m; row2++) {
                        alpha -= qrtCol[row2] * qrtMinor[row2];
                    }
                    double alpha2 = alpha / (qrtMinor[minor] * a);
                    for (int row3 = minor; row3 < m; row3++) {
                        qrtCol[row3] = qrtCol[row3] - (qrtMinor[row3] * alpha2);
                    }
                }
            }
        }
    }

    @Override // org.apache.commons.math.linear.QRDecomposition
    public org.apache.commons.math.linear.RealMatrix getR() {
        if (this.cachedR == null) {
            int n = this.qrt.length;
            int m = this.qrt[0].length;
            this.cachedR = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, n);
            for (int row = org.apache.commons.math.util.FastMath.min(m, n) - 1; row >= 0; row--) {
                this.cachedR.setEntry(row, row, this.rDiag[row]);
                for (int col = row + 1; col < n; col++) {
                    this.cachedR.setEntry(row, col, this.qrt[col][row]);
                }
            }
        }
        return this.cachedR;
    }

    @Override // org.apache.commons.math.linear.QRDecomposition
    public org.apache.commons.math.linear.RealMatrix getQ() {
        if (this.cachedQ == null) {
            this.cachedQ = getQT().transpose();
        }
        return this.cachedQ;
    }

    @Override // org.apache.commons.math.linear.QRDecomposition
    public org.apache.commons.math.linear.RealMatrix getQT() {
        if (this.cachedQT == null) {
            int n = this.qrt.length;
            int m = this.qrt[0].length;
            this.cachedQT = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, m);
            for (int minor = m - 1; minor >= org.apache.commons.math.util.FastMath.min(m, n); minor--) {
                this.cachedQT.setEntry(minor, minor, 1.0d);
            }
            int minor2 = org.apache.commons.math.util.FastMath.min(m, n);
            for (int minor3 = minor2 - 1; minor3 >= 0; minor3--) {
                double[] qrtMinor = this.qrt[minor3];
                this.cachedQT.setEntry(minor3, minor3, 1.0d);
                if (qrtMinor[minor3] != 0.0d) {
                    for (int col = minor3; col < m; col++) {
                        double alpha = 0.0d;
                        for (int row = minor3; row < m; row++) {
                            alpha -= this.cachedQT.getEntry(col, row) * qrtMinor[row];
                        }
                        double alpha2 = alpha / (this.rDiag[minor3] * qrtMinor[minor3]);
                        for (int row2 = minor3; row2 < m; row2++) {
                            this.cachedQT.addToEntry(col, row2, (-alpha2) * qrtMinor[row2]);
                        }
                    }
                }
            }
        }
        return this.cachedQT;
    }

    @Override // org.apache.commons.math.linear.QRDecomposition
    public org.apache.commons.math.linear.RealMatrix getH() {
        if (this.cachedH == null) {
            int n = this.qrt.length;
            int m = this.qrt[0].length;
            this.cachedH = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, n);
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < org.apache.commons.math.util.FastMath.min(i + 1, n); j++) {
                    this.cachedH.setEntry(i, j, this.qrt[j][i] / (-this.rDiag[j]));
                }
            }
        }
        return this.cachedH;
    }

    @Override // org.apache.commons.math.linear.QRDecomposition
    public org.apache.commons.math.linear.DecompositionSolver getSolver() {
        return new org.apache.commons.math.linear.QRDecompositionImpl.Solver(this.qrt, this.rDiag);
    }

    private static class Solver implements org.apache.commons.math.linear.DecompositionSolver {
        private final double[][] qrt;
        private final double[] rDiag;

        private Solver(double[][] qrt, double[] rDiag) {
            this.qrt = qrt;
            this.rDiag = rDiag;
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public boolean isNonSingular() {
            for (double diag : this.rDiag) {
                if (diag == 0.0d) {
                    return false;
                }
            }
            return true;
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public double[] solve(double[] b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            int n = this.qrt.length;
            int m = this.qrt[0].length;
            if (b.length != m) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(b.length), java.lang.Integer.valueOf(m));
            }
            if (!isNonSingular()) {
                throw new org.apache.commons.math.linear.SingularMatrixException();
            }
            double[] x = new double[n];
            double[] y = (double[]) b.clone();
            for (int minor = 0; minor < org.apache.commons.math.util.FastMath.min(m, n); minor++) {
                double[] qrtMinor = this.qrt[minor];
                double dotProduct = 0.0d;
                for (int row = minor; row < m; row++) {
                    dotProduct += y[row] * qrtMinor[row];
                }
                double dotProduct2 = dotProduct / (this.rDiag[minor] * qrtMinor[minor]);
                for (int row2 = minor; row2 < m; row2++) {
                    y[row2] = y[row2] + (qrtMinor[row2] * dotProduct2);
                }
            }
            for (int row3 = this.rDiag.length - 1; row3 >= 0; row3--) {
                y[row3] = y[row3] / this.rDiag[row3];
                double yRow = y[row3];
                double[] qrtRow = this.qrt[row3];
                x[row3] = yRow;
                for (int i = 0; i < row3; i++) {
                    y[i] = y[i] - (qrtRow[i] * yRow);
                }
            }
            return x;
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealVector solve(org.apache.commons.math.linear.RealVector b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            try {
                return solve((org.apache.commons.math.linear.ArrayRealVector) b);
            } catch (java.lang.ClassCastException e) {
                return new org.apache.commons.math.linear.ArrayRealVector(solve(b.getData()), false);
            }
        }

        public org.apache.commons.math.linear.ArrayRealVector solve(org.apache.commons.math.linear.ArrayRealVector b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            return new org.apache.commons.math.linear.ArrayRealVector(solve(b.getDataRef()), false);
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealMatrix solve(org.apache.commons.math.linear.RealMatrix b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            double d;
            org.apache.commons.math.linear.QRDecompositionImpl.Solver solver = this;
            int n = solver.qrt.length;
            int i = 0;
            int m = solver.qrt[0].length;
            if (b.getRowDimension() != m) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(b.getRowDimension()), java.lang.Integer.valueOf(b.getColumnDimension()), java.lang.Integer.valueOf(m), "n");
            }
            if (!isNonSingular()) {
                throw new org.apache.commons.math.linear.SingularMatrixException();
            }
            int columns = b.getColumnDimension();
            int blockSize = 52;
            int cBlocks = ((columns + 52) - 1) / 52;
            double[][] xBlocks = org.apache.commons.math.linear.BlockRealMatrix.createBlocksLayout(n, columns);
            double[][] y = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, b.getRowDimension(), 52);
            double[] alpha = new double[52];
            int columns2 = 0;
            while (columns2 < cBlocks) {
                int kStart = columns2 * 52;
                int kEnd = org.apache.commons.math.util.FastMath.min(kStart + 52, columns);
                int kWidth = kEnd - kStart;
                int kBlock = columns2;
                b.copySubMatrix(0, m - 1, kStart, kEnd - 1, y);
                int minor = 0;
                while (true) {
                    d = 1.0d;
                    if (minor >= org.apache.commons.math.util.FastMath.min(m, n)) {
                        break;
                    }
                    double[] qrtMinor = solver.qrt[minor];
                    double factor = 1.0d / (solver.rDiag[minor] * qrtMinor[minor]);
                    int columns3 = columns;
                    int blockSize2 = blockSize;
                    java.util.Arrays.fill(alpha, i, kWidth, 0.0d);
                    for (int row = minor; row < m; row++) {
                        double d2 = qrtMinor[row];
                        double[] yRow = y[row];
                        for (int k = 0; k < kWidth; k++) {
                            alpha[k] = alpha[k] + (yRow[k] * d2);
                        }
                    }
                    for (int k2 = 0; k2 < kWidth; k2++) {
                        alpha[k2] = alpha[k2] * factor;
                    }
                    int row2 = minor;
                    while (row2 < m) {
                        double d3 = qrtMinor[row2];
                        double[] yRow2 = y[row2];
                        double[] qrtMinor2 = qrtMinor;
                        for (int k3 = 0; k3 < kWidth; k3++) {
                            yRow2[k3] = yRow2[k3] + (alpha[k3] * d3);
                        }
                        row2++;
                        qrtMinor = qrtMinor2;
                    }
                    minor++;
                    columns = columns3;
                    blockSize = blockSize2;
                    i = 0;
                }
                int columns4 = columns;
                int blockSize3 = blockSize;
                int j = solver.rDiag.length - 1;
                while (j >= 0) {
                    int jBlock = j / 52;
                    int jStart = jBlock * 52;
                    double factor2 = d / solver.rDiag[j];
                    double[] yJ = y[j];
                    double[] xBlock = xBlocks[(jBlock * cBlocks) + kBlock];
                    int index = (j - jStart) * kWidth;
                    int k4 = 0;
                    while (k4 < kWidth) {
                        yJ[k4] = yJ[k4] * factor2;
                        xBlock[index] = yJ[k4];
                        k4++;
                        index++;
                    }
                    double[] qrtJ = solver.qrt[j];
                    for (int i2 = 0; i2 < j; i2++) {
                        double rIJ = qrtJ[i2];
                        double[] yI = y[i2];
                        for (int k5 = 0; k5 < kWidth; k5++) {
                            yI[k5] = yI[k5] - (yJ[k5] * rIJ);
                        }
                    }
                    j--;
                    d = 1.0d;
                    solver = this;
                }
                columns = columns4;
                blockSize = blockSize3;
                i = 0;
                columns2 = kBlock + 1;
                solver = this;
            }
            return new org.apache.commons.math.linear.BlockRealMatrix(n, columns, xBlocks, false);
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealMatrix getInverse() throws org.apache.commons.math.linear.InvalidMatrixException {
            return solve(org.apache.commons.math.linear.MatrixUtils.createRealIdentityMatrix(this.rDiag.length));
        }
    }
}
