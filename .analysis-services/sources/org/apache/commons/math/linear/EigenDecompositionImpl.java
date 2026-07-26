package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class EigenDecompositionImpl implements org.apache.commons.math.linear.EigenDecomposition {
    private org.apache.commons.math.linear.RealMatrix cachedD;
    private org.apache.commons.math.linear.RealMatrix cachedV;
    private org.apache.commons.math.linear.RealMatrix cachedVt;
    private org.apache.commons.math.linear.ArrayRealVector[] eigenvectors;
    private double[] imagEigenvalues;
    private double[] main;
    private byte maxIter;
    private double[] realEigenvalues;
    private double[] secondary;
    private org.apache.commons.math.linear.TriDiagonalTransformer transformer;

    public EigenDecompositionImpl(org.apache.commons.math.linear.RealMatrix matrix, double splitTolerance) throws org.apache.commons.math.linear.InvalidMatrixException {
        this.maxIter = (byte) 30;
        if (isSymmetric(matrix)) {
            transformToTridiagonal(matrix);
            findEigenVectors(this.transformer.getQ().getData());
            return;
        }
        throw new org.apache.commons.math.linear.InvalidMatrixException(org.apache.commons.math.exception.util.LocalizedFormats.ASSYMETRIC_EIGEN_NOT_SUPPORTED, new java.lang.Object[0]);
    }

    public EigenDecompositionImpl(double[] main, double[] secondary, double splitTolerance) throws org.apache.commons.math.linear.InvalidMatrixException {
        this.maxIter = (byte) 30;
        this.main = (double[]) main.clone();
        this.secondary = (double[]) secondary.clone();
        this.transformer = null;
        int size = main.length;
        double[][] z = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, size, size);
        for (int i = 0; i < size; i++) {
            z[i][i] = 1.0d;
        }
        findEigenVectors(z);
    }

    private boolean isSymmetric(org.apache.commons.math.linear.RealMatrix matrix) {
        org.apache.commons.math.linear.RealMatrix realMatrix = matrix;
        int rows = matrix.getRowDimension();
        int columns = matrix.getColumnDimension();
        double eps = ((double) (rows * 10 * columns)) * 1.1102230246251565E-16d;
        int i = 0;
        while (i < rows) {
            int j = i + 1;
            while (j < columns) {
                double mij = realMatrix.getEntry(i, j);
                double mji = realMatrix.getEntry(j, i);
                int rows2 = rows;
                if (org.apache.commons.math.util.FastMath.abs(mij - mji) <= org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(mij), org.apache.commons.math.util.FastMath.abs(mji)) * eps) {
                    j++;
                    realMatrix = matrix;
                    rows = rows2;
                } else {
                    return false;
                }
            }
            i++;
            realMatrix = matrix;
        }
        return true;
    }

    @Override // org.apache.commons.math.linear.EigenDecomposition
    public org.apache.commons.math.linear.RealMatrix getV() throws org.apache.commons.math.linear.InvalidMatrixException {
        if (this.cachedV == null) {
            int m = this.eigenvectors.length;
            this.cachedV = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, m);
            for (int k = 0; k < m; k++) {
                this.cachedV.setColumnVector(k, this.eigenvectors[k]);
            }
        }
        return this.cachedV;
    }

    @Override // org.apache.commons.math.linear.EigenDecomposition
    public org.apache.commons.math.linear.RealMatrix getD() throws org.apache.commons.math.linear.InvalidMatrixException {
        if (this.cachedD == null) {
            this.cachedD = org.apache.commons.math.linear.MatrixUtils.createRealDiagonalMatrix(this.realEigenvalues);
        }
        return this.cachedD;
    }

    @Override // org.apache.commons.math.linear.EigenDecomposition
    public org.apache.commons.math.linear.RealMatrix getVT() throws org.apache.commons.math.linear.InvalidMatrixException {
        if (this.cachedVt == null) {
            int m = this.eigenvectors.length;
            this.cachedVt = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, m);
            for (int k = 0; k < m; k++) {
                this.cachedVt.setRowVector(k, this.eigenvectors[k]);
            }
        }
        return this.cachedVt;
    }

    @Override // org.apache.commons.math.linear.EigenDecomposition
    public double[] getRealEigenvalues() throws org.apache.commons.math.linear.InvalidMatrixException {
        return (double[]) this.realEigenvalues.clone();
    }

    @Override // org.apache.commons.math.linear.EigenDecomposition
    public double getRealEigenvalue(int i) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.ArrayIndexOutOfBoundsException {
        return this.realEigenvalues[i];
    }

    @Override // org.apache.commons.math.linear.EigenDecomposition
    public double[] getImagEigenvalues() throws org.apache.commons.math.linear.InvalidMatrixException {
        return (double[]) this.imagEigenvalues.clone();
    }

    @Override // org.apache.commons.math.linear.EigenDecomposition
    public double getImagEigenvalue(int i) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.ArrayIndexOutOfBoundsException {
        return this.imagEigenvalues[i];
    }

    @Override // org.apache.commons.math.linear.EigenDecomposition
    public org.apache.commons.math.linear.RealVector getEigenvector(int i) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.ArrayIndexOutOfBoundsException {
        return this.eigenvectors[i].copy();
    }

    @Override // org.apache.commons.math.linear.EigenDecomposition
    public double getDeterminant() {
        double determinant = 1.0d;
        for (double lambda : this.realEigenvalues) {
            determinant *= lambda;
        }
        return determinant;
    }

    @Override // org.apache.commons.math.linear.EigenDecomposition
    public org.apache.commons.math.linear.DecompositionSolver getSolver() {
        return new org.apache.commons.math.linear.EigenDecompositionImpl.Solver(this.realEigenvalues, this.imagEigenvalues, this.eigenvectors);
    }

    private static class Solver implements org.apache.commons.math.linear.DecompositionSolver {
        private final org.apache.commons.math.linear.ArrayRealVector[] eigenvectors;
        private double[] imagEigenvalues;
        private double[] realEigenvalues;

        private Solver(double[] realEigenvalues, double[] imagEigenvalues, org.apache.commons.math.linear.ArrayRealVector[] eigenvectors) {
            this.realEigenvalues = realEigenvalues;
            this.imagEigenvalues = imagEigenvalues;
            this.eigenvectors = eigenvectors;
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public double[] solve(double[] b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            if (!isNonSingular()) {
                throw new org.apache.commons.math.linear.SingularMatrixException();
            }
            int m = this.realEigenvalues.length;
            if (b.length != m) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(b.length), java.lang.Integer.valueOf(m));
            }
            double[] bp = new double[m];
            for (int i = 0; i < m; i++) {
                org.apache.commons.math.linear.ArrayRealVector v = this.eigenvectors[i];
                double[] vData = v.getDataRef();
                double s = v.dotProduct(b) / this.realEigenvalues[i];
                for (int j = 0; j < m; j++) {
                    bp[j] = bp[j] + (vData[j] * s);
                }
            }
            return bp;
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealVector solve(org.apache.commons.math.linear.RealVector b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            if (!isNonSingular()) {
                throw new org.apache.commons.math.linear.SingularMatrixException();
            }
            int m = this.realEigenvalues.length;
            if (b.getDimension() != m) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.VECTOR_LENGTH_MISMATCH, java.lang.Integer.valueOf(b.getDimension()), java.lang.Integer.valueOf(m));
            }
            double[] bp = new double[m];
            for (int i = 0; i < m; i++) {
                org.apache.commons.math.linear.ArrayRealVector v = this.eigenvectors[i];
                double[] vData = v.getDataRef();
                double s = v.dotProduct(b) / this.realEigenvalues[i];
                for (int j = 0; j < m; j++) {
                    bp[j] = bp[j] + (vData[j] * s);
                }
            }
            return new org.apache.commons.math.linear.ArrayRealVector(bp, false);
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealMatrix solve(org.apache.commons.math.linear.RealMatrix b) throws org.apache.commons.math.linear.InvalidMatrixException, java.lang.IllegalArgumentException {
            if (!isNonSingular()) {
                throw new org.apache.commons.math.linear.SingularMatrixException();
            }
            int m = this.realEigenvalues.length;
            if (b.getRowDimension() != m) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_2x2, java.lang.Integer.valueOf(b.getRowDimension()), java.lang.Integer.valueOf(b.getColumnDimension()), java.lang.Integer.valueOf(m), "n");
            }
            int nColB = b.getColumnDimension();
            double[][] bp = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, m, nColB);
            for (int k = 0; k < nColB; k++) {
                for (int i = 0; i < m; i++) {
                    org.apache.commons.math.linear.ArrayRealVector v = this.eigenvectors[i];
                    double[] vData = v.getDataRef();
                    double s = 0.0d;
                    for (int j = 0; j < m; j++) {
                        s += v.getEntry(j) * b.getEntry(j, k);
                    }
                    double s2 = s / this.realEigenvalues[i];
                    for (int j2 = 0; j2 < m; j2++) {
                        double[] dArr = bp[j2];
                        dArr[k] = dArr[k] + (vData[j2] * s2);
                    }
                }
            }
            return org.apache.commons.math.linear.MatrixUtils.createRealMatrix(bp);
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public boolean isNonSingular() {
            for (int i = 0; i < this.realEigenvalues.length; i++) {
                if (this.realEigenvalues[i] == 0.0d && this.imagEigenvalues[i] == 0.0d) {
                    return false;
                }
            }
            return true;
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealMatrix getInverse() throws org.apache.commons.math.linear.InvalidMatrixException {
            if (!isNonSingular()) {
                throw new org.apache.commons.math.linear.SingularMatrixException();
            }
            int m = this.realEigenvalues.length;
            double[][] invData = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, m, m);
            for (int i = 0; i < m; i++) {
                double[] invI = invData[i];
                for (int j = 0; j < m; j++) {
                    double invIJ = 0.0d;
                    for (int k = 0; k < m; k++) {
                        double[] vK = this.eigenvectors[k].getDataRef();
                        invIJ += (vK[i] * vK[j]) / this.realEigenvalues[k];
                    }
                    invI[j] = invIJ;
                }
            }
            return org.apache.commons.math.linear.MatrixUtils.createRealMatrix(invData);
        }
    }

    private void transformToTridiagonal(org.apache.commons.math.linear.RealMatrix matrix) {
        this.transformer = new org.apache.commons.math.linear.TriDiagonalTransformer(matrix);
        this.main = this.transformer.getMainDiagonalRef();
        this.secondary = this.transformer.getSecondaryDiagonalRef();
    }

    /* JADX WARN: Code restructure failed: missing block: B:65:0x01e9, code lost:
    
        r4 = r4 + 1;
        r7 = r26;
        r5 = 0.0d;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void findEigenVectors(double[][] r41) {
        /*
            Method dump skipped, instruction units count: 675
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math.linear.EigenDecompositionImpl.findEigenVectors(double[][]):void");
    }
}
