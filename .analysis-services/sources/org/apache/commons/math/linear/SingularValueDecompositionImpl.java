package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class SingularValueDecompositionImpl implements org.apache.commons.math.linear.SingularValueDecomposition {
    private org.apache.commons.math.linear.RealMatrix cachedU;
    private org.apache.commons.math.linear.RealMatrix cachedUt;
    private org.apache.commons.math.linear.RealMatrix cachedV;
    private org.apache.commons.math.linear.EigenDecomposition eigenDecomposition;
    private int m;
    private int n;
    private double[] singularValues;
    private org.apache.commons.math.linear.RealMatrix cachedS = null;
    private org.apache.commons.math.linear.RealMatrix cachedVt = null;

    public SingularValueDecompositionImpl(org.apache.commons.math.linear.RealMatrix matrix) throws org.apache.commons.math.linear.InvalidMatrixException {
        int p;
        this.m = matrix.getRowDimension();
        this.n = matrix.getColumnDimension();
        this.cachedU = null;
        this.cachedV = null;
        double[][] localcopy = matrix.getData();
        double[][] matATA = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, this.n, this.n);
        for (int i = 0; i < this.n; i++) {
            for (int j = i; j < this.n; j++) {
                matATA[i][j] = 0.0d;
                for (int k = 0; k < this.m; k++) {
                    double[] dArr = matATA[i];
                    dArr[j] = dArr[j] + (localcopy[k][i] * localcopy[k][j]);
                }
                matATA[j][i] = matATA[i][j];
            }
        }
        int i2 = this.m;
        double[][] matAAT = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, i2, this.m);
        for (int i3 = 0; i3 < this.m; i3++) {
            for (int j2 = i3; j2 < this.m; j2++) {
                matAAT[i3][j2] = 0.0d;
                for (int k2 = 0; k2 < this.n; k2++) {
                    double[] dArr2 = matAAT[i3];
                    dArr2[j2] = dArr2[j2] + (localcopy[i3][k2] * localcopy[j2][k2]);
                }
                matAAT[j2][i3] = matAAT[i3][j2];
            }
        }
        int i4 = this.m;
        if (i4 >= this.n) {
            p = this.n;
            this.eigenDecomposition = new org.apache.commons.math.linear.EigenDecompositionImpl(new org.apache.commons.math.linear.Array2DRowRealMatrix(matATA), 1.0d);
            this.singularValues = this.eigenDecomposition.getRealEigenvalues();
            this.cachedV = this.eigenDecomposition.getV();
            this.eigenDecomposition = new org.apache.commons.math.linear.EigenDecompositionImpl(new org.apache.commons.math.linear.Array2DRowRealMatrix(matAAT), 1.0d);
            this.cachedU = this.eigenDecomposition.getV().getSubMatrix(0, this.m - 1, 0, p - 1);
        } else {
            p = this.m;
            this.eigenDecomposition = new org.apache.commons.math.linear.EigenDecompositionImpl(new org.apache.commons.math.linear.Array2DRowRealMatrix(matAAT), 1.0d);
            this.singularValues = this.eigenDecomposition.getRealEigenvalues();
            this.cachedU = this.eigenDecomposition.getV();
            this.eigenDecomposition = new org.apache.commons.math.linear.EigenDecompositionImpl(new org.apache.commons.math.linear.Array2DRowRealMatrix(matATA), 1.0d);
            this.cachedV = this.eigenDecomposition.getV().getSubMatrix(0, this.n - 1, 0, p - 1);
        }
        for (int i5 = 0; i5 < p; i5++) {
            this.singularValues[i5] = org.apache.commons.math.util.FastMath.sqrt(org.apache.commons.math.util.FastMath.abs(this.singularValues[i5]));
        }
        for (int i6 = 0; i6 < p; i6++) {
            org.apache.commons.math.linear.RealVector tmp = this.cachedU.getColumnVector(i6);
            double product = matrix.operate(this.cachedV.getColumnVector(i6)).dotProduct(tmp);
            if (product < 0.0d) {
                this.cachedU.setColumnVector(i6, tmp.mapMultiply(-1.0d));
            }
        }
    }

    @Override // org.apache.commons.math.linear.SingularValueDecomposition
    public org.apache.commons.math.linear.RealMatrix getU() throws org.apache.commons.math.linear.InvalidMatrixException {
        return this.cachedU;
    }

    @Override // org.apache.commons.math.linear.SingularValueDecomposition
    public org.apache.commons.math.linear.RealMatrix getUT() throws org.apache.commons.math.linear.InvalidMatrixException {
        if (this.cachedUt == null) {
            this.cachedUt = getU().transpose();
        }
        return this.cachedUt;
    }

    @Override // org.apache.commons.math.linear.SingularValueDecomposition
    public org.apache.commons.math.linear.RealMatrix getS() throws org.apache.commons.math.linear.InvalidMatrixException {
        if (this.cachedS == null) {
            this.cachedS = org.apache.commons.math.linear.MatrixUtils.createRealDiagonalMatrix(this.singularValues);
        }
        return this.cachedS;
    }

    @Override // org.apache.commons.math.linear.SingularValueDecomposition
    public double[] getSingularValues() throws org.apache.commons.math.linear.InvalidMatrixException {
        return (double[]) this.singularValues.clone();
    }

    @Override // org.apache.commons.math.linear.SingularValueDecomposition
    public org.apache.commons.math.linear.RealMatrix getV() throws org.apache.commons.math.linear.InvalidMatrixException {
        return this.cachedV;
    }

    @Override // org.apache.commons.math.linear.SingularValueDecomposition
    public org.apache.commons.math.linear.RealMatrix getVT() throws org.apache.commons.math.linear.InvalidMatrixException {
        if (this.cachedVt == null) {
            this.cachedVt = getV().transpose();
        }
        return this.cachedVt;
    }

    @Override // org.apache.commons.math.linear.SingularValueDecomposition
    public org.apache.commons.math.linear.RealMatrix getCovariance(double minSingularValue) {
        int p = this.singularValues.length;
        int dimension = 0;
        while (dimension < p && this.singularValues[dimension] >= minSingularValue) {
            dimension++;
        }
        if (dimension == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.TOO_LARGE_CUTOFF_SINGULAR_VALUE, java.lang.Double.valueOf(minSingularValue), java.lang.Double.valueOf(this.singularValues[0]));
        }
        final double[][] data = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, dimension, p);
        getVT().walkInOptimizedOrder(new org.apache.commons.math.linear.DefaultRealMatrixPreservingVisitor() { // from class: org.apache.commons.math.linear.SingularValueDecompositionImpl.1
            @Override // org.apache.commons.math.linear.DefaultRealMatrixPreservingVisitor, org.apache.commons.math.linear.RealMatrixPreservingVisitor
            public void visit(int row, int column, double value) {
                data[row][column] = value / org.apache.commons.math.linear.SingularValueDecompositionImpl.this.singularValues[row];
            }
        }, 0, dimension - 1, 0, p - 1);
        org.apache.commons.math.linear.RealMatrix jv = new org.apache.commons.math.linear.Array2DRowRealMatrix(data, false);
        return jv.transpose().multiply(jv);
    }

    @Override // org.apache.commons.math.linear.SingularValueDecomposition
    public double getNorm() throws org.apache.commons.math.linear.InvalidMatrixException {
        return this.singularValues[0];
    }

    @Override // org.apache.commons.math.linear.SingularValueDecomposition
    public double getConditionNumber() throws org.apache.commons.math.linear.InvalidMatrixException {
        return this.singularValues[0] / this.singularValues[this.singularValues.length - 1];
    }

    @Override // org.apache.commons.math.linear.SingularValueDecomposition
    public int getRank() throws java.lang.IllegalStateException {
        double threshold = ((double) org.apache.commons.math.util.FastMath.max(this.m, this.n)) * org.apache.commons.math.util.FastMath.ulp(this.singularValues[0]);
        for (int i = this.singularValues.length - 1; i >= 0; i--) {
            if (this.singularValues[i] > threshold) {
                return i + 1;
            }
        }
        return 0;
    }

    @Override // org.apache.commons.math.linear.SingularValueDecomposition
    public org.apache.commons.math.linear.DecompositionSolver getSolver() {
        return new org.apache.commons.math.linear.SingularValueDecompositionImpl.Solver(this.singularValues, getUT(), getV(), getRank() == java.lang.Math.max(this.m, this.n));
    }

    private static class Solver implements org.apache.commons.math.linear.DecompositionSolver {
        private boolean nonSingular;
        private final org.apache.commons.math.linear.RealMatrix pseudoInverse;

        private Solver(double[] singularValues, org.apache.commons.math.linear.RealMatrix uT, org.apache.commons.math.linear.RealMatrix v, boolean nonSingular) {
            double a;
            double[][] suT = uT.getData();
            for (int i = 0; i < singularValues.length; i++) {
                if (singularValues[i] > 0.0d) {
                    a = 1.0d / singularValues[i];
                } else {
                    a = 0.0d;
                }
                double[] suTi = suT[i];
                for (int j = 0; j < suTi.length; j++) {
                    suTi[j] = suTi[j] * a;
                }
            }
            this.pseudoInverse = v.multiply(new org.apache.commons.math.linear.Array2DRowRealMatrix(suT, false));
            this.nonSingular = nonSingular;
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public double[] solve(double[] b) throws java.lang.IllegalArgumentException {
            return this.pseudoInverse.operate(b);
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealVector solve(org.apache.commons.math.linear.RealVector b) throws java.lang.IllegalArgumentException {
            return this.pseudoInverse.operate(b);
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealMatrix solve(org.apache.commons.math.linear.RealMatrix b) throws java.lang.IllegalArgumentException {
            return this.pseudoInverse.multiply(b);
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public boolean isNonSingular() {
            return this.nonSingular;
        }

        @Override // org.apache.commons.math.linear.DecompositionSolver
        public org.apache.commons.math.linear.RealMatrix getInverse() {
            return this.pseudoInverse;
        }
    }
}
