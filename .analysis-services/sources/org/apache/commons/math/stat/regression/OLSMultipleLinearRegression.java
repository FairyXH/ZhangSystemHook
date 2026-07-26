package org.apache.commons.math.stat.regression;

/* JADX INFO: loaded from: classes4.dex */
public class OLSMultipleLinearRegression extends org.apache.commons.math.stat.regression.AbstractMultipleLinearRegression {
    private org.apache.commons.math.linear.QRDecomposition qr = null;

    public void newSampleData(double[] y, double[][] x) {
        validateSampleData(x, y);
        newYSampleData(y);
        newXSampleData(x);
    }

    @Override // org.apache.commons.math.stat.regression.AbstractMultipleLinearRegression
    public void newSampleData(double[] data, int nobs, int nvars) {
        super.newSampleData(data, nobs, nvars);
        this.qr = new org.apache.commons.math.linear.QRDecompositionImpl(this.X);
    }

    public org.apache.commons.math.linear.RealMatrix calculateHat() {
        org.apache.commons.math.linear.RealMatrix Q = this.qr.getQ();
        int p = this.qr.getR().getColumnDimension();
        int n = Q.getColumnDimension();
        org.apache.commons.math.linear.Array2DRowRealMatrix augI = new org.apache.commons.math.linear.Array2DRowRealMatrix(n, n);
        double[][] augIData = augI.getDataRef();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j && i < p) {
                    augIData[i][j] = 1.0d;
                } else {
                    augIData[i][j] = 0.0d;
                }
            }
        }
        return Q.multiply(augI).multiply(Q.transpose());
    }

    public double calculateTotalSumOfSquares() {
        if (isNoIntercept()) {
            return org.apache.commons.math.stat.StatUtils.sumSq(this.Y.getData());
        }
        return new org.apache.commons.math.stat.descriptive.moment.SecondMoment().evaluate(this.Y.getData());
    }

    public double calculateResidualSumOfSquares() {
        org.apache.commons.math.linear.RealVector residuals = calculateResiduals();
        return residuals.dotProduct(residuals);
    }

    public double calculateRSquared() {
        return 1.0d - (calculateResidualSumOfSquares() / calculateTotalSumOfSquares());
    }

    public double calculateAdjustedRSquared() {
        double n = this.X.getRowDimension();
        if (isNoIntercept()) {
            return 1.0d - ((1.0d - calculateRSquared()) * (n / (n - ((double) this.X.getColumnDimension()))));
        }
        return 1.0d - ((calculateResidualSumOfSquares() * (n - 1.0d)) / (calculateTotalSumOfSquares() * (n - ((double) this.X.getColumnDimension()))));
    }

    @Override // org.apache.commons.math.stat.regression.AbstractMultipleLinearRegression
    protected void newXSampleData(double[][] x) {
        super.newXSampleData(x);
        this.qr = new org.apache.commons.math.linear.QRDecompositionImpl(this.X);
    }

    @Override // org.apache.commons.math.stat.regression.AbstractMultipleLinearRegression
    protected org.apache.commons.math.linear.RealVector calculateBeta() {
        return this.qr.getSolver().solve(this.Y);
    }

    @Override // org.apache.commons.math.stat.regression.AbstractMultipleLinearRegression
    protected org.apache.commons.math.linear.RealMatrix calculateBetaVariance() {
        int p = this.X.getColumnDimension();
        org.apache.commons.math.linear.RealMatrix Raug = this.qr.getR().getSubMatrix(0, p - 1, 0, p - 1);
        org.apache.commons.math.linear.RealMatrix Rinv = new org.apache.commons.math.linear.LUDecompositionImpl(Raug).getSolver().getInverse();
        return Rinv.multiply(Rinv.transpose());
    }
}
