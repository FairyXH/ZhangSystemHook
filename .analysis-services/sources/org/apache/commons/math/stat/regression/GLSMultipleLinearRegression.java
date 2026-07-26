package org.apache.commons.math.stat.regression;

/* JADX INFO: loaded from: classes4.dex */
public class GLSMultipleLinearRegression extends org.apache.commons.math.stat.regression.AbstractMultipleLinearRegression {
    private org.apache.commons.math.linear.RealMatrix Omega;
    private org.apache.commons.math.linear.RealMatrix OmegaInverse;

    public void newSampleData(double[] y, double[][] x, double[][] covariance) {
        validateSampleData(x, y);
        newYSampleData(y);
        newXSampleData(x);
        validateCovarianceData(x, covariance);
        newCovarianceData(covariance);
    }

    protected void newCovarianceData(double[][] omega) {
        this.Omega = new org.apache.commons.math.linear.Array2DRowRealMatrix(omega);
        this.OmegaInverse = null;
    }

    protected org.apache.commons.math.linear.RealMatrix getOmegaInverse() {
        if (this.OmegaInverse == null) {
            this.OmegaInverse = new org.apache.commons.math.linear.LUDecompositionImpl(this.Omega).getSolver().getInverse();
        }
        return this.OmegaInverse;
    }

    @Override // org.apache.commons.math.stat.regression.AbstractMultipleLinearRegression
    protected org.apache.commons.math.linear.RealVector calculateBeta() {
        org.apache.commons.math.linear.RealMatrix OI = getOmegaInverse();
        org.apache.commons.math.linear.RealMatrix XT = this.X.transpose();
        org.apache.commons.math.linear.RealMatrix XTOIX = XT.multiply(OI).multiply(this.X);
        org.apache.commons.math.linear.RealMatrix inverse = new org.apache.commons.math.linear.LUDecompositionImpl(XTOIX).getSolver().getInverse();
        return inverse.multiply(XT).multiply(OI).operate(this.Y);
    }

    @Override // org.apache.commons.math.stat.regression.AbstractMultipleLinearRegression
    protected org.apache.commons.math.linear.RealMatrix calculateBetaVariance() {
        org.apache.commons.math.linear.RealMatrix OI = getOmegaInverse();
        org.apache.commons.math.linear.RealMatrix XTOIX = this.X.transpose().multiply(OI).multiply(this.X);
        return new org.apache.commons.math.linear.LUDecompositionImpl(XTOIX).getSolver().getInverse();
    }

    @Override // org.apache.commons.math.stat.regression.AbstractMultipleLinearRegression
    protected double calculateErrorVariance() {
        org.apache.commons.math.linear.RealVector residuals = calculateResiduals();
        double t = residuals.dotProduct(getOmegaInverse().operate(residuals));
        return t / ((double) (this.X.getRowDimension() - this.X.getColumnDimension()));
    }
}
