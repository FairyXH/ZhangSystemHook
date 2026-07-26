package org.apache.commons.math.stat.regression;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractMultipleLinearRegression implements org.apache.commons.math.stat.regression.MultipleLinearRegression {
    protected org.apache.commons.math.linear.RealMatrix X;
    protected org.apache.commons.math.linear.RealVector Y;
    private boolean noIntercept = false;

    protected abstract org.apache.commons.math.linear.RealVector calculateBeta();

    protected abstract org.apache.commons.math.linear.RealMatrix calculateBetaVariance();

    public boolean isNoIntercept() {
        return this.noIntercept;
    }

    public void setNoIntercept(boolean noIntercept) {
        this.noIntercept = noIntercept;
    }

    public void newSampleData(double[] dArr, int i, int i2) {
        if (dArr == null) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NULL_NOT_ALLOWED, new java.lang.Object[0]);
        }
        if (dArr.length != (i2 + 1) * i) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INVALID_REGRESSION_ARRAY, java.lang.Integer.valueOf(dArr.length), java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(i2));
        }
        if (i <= i2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, new java.lang.Object[0]);
        }
        double[] dArr2 = new double[i];
        int i3 = this.noIntercept ? i2 : i2 + 1;
        double[][] dArr3 = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, i, i3);
        int i4 = 0;
        int i5 = 0;
        while (i5 < i) {
            int i6 = i4 + 1;
            dArr2[i5] = dArr[i4];
            if (!this.noIntercept) {
                dArr3[i5][0] = 1.0d;
            }
            int i7 = !this.noIntercept ? 1 : 0;
            while (i7 < i3) {
                dArr3[i5][i7] = dArr[i6];
                i7++;
                i6++;
            }
            i5++;
            i4 = i6;
        }
        this.X = new org.apache.commons.math.linear.Array2DRowRealMatrix(dArr3);
        this.Y = new org.apache.commons.math.linear.ArrayRealVector(dArr2);
    }

    protected void newYSampleData(double[] y) {
        if (y == null) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NULL_NOT_ALLOWED, new java.lang.Object[0]);
        }
        if (y.length == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NO_DATA, new java.lang.Object[0]);
        }
        this.Y = new org.apache.commons.math.linear.ArrayRealVector(y);
    }

    protected void newXSampleData(double[][] x) {
        if (x == null) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NULL_NOT_ALLOWED, new java.lang.Object[0]);
        }
        if (x.length == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NO_DATA, new java.lang.Object[0]);
        }
        if (this.noIntercept) {
            this.X = new org.apache.commons.math.linear.Array2DRowRealMatrix(x, true);
            return;
        }
        int nVars = x[0].length;
        double[][] xAug = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, x.length, nVars + 1);
        for (int i = 0; i < x.length; i++) {
            if (x[i].length == nVars) {
                xAug[i][0] = 1.0d;
                java.lang.System.arraycopy(x[i], 0, xAug[i], 1, nVars);
            } else {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIFFERENT_ROWS_LENGTHS, java.lang.Integer.valueOf(x[i].length), java.lang.Integer.valueOf(nVars));
            }
        }
        this.X = new org.apache.commons.math.linear.Array2DRowRealMatrix(xAug, false);
    }

    protected void validateSampleData(double[][] x, double[] y) {
        if (x == null || y == null || x.length != y.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(x == null ? 0 : x.length), java.lang.Integer.valueOf(y != null ? y.length : 0));
        }
        if (x.length == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NO_DATA, new java.lang.Object[0]);
        }
        if (x[0].length + 1 > x.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, java.lang.Integer.valueOf(x.length), java.lang.Integer.valueOf(x[0].length));
        }
    }

    protected void validateCovarianceData(double[][] x, double[][] covariance) {
        if (x.length != covariance.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(x.length), java.lang.Integer.valueOf(covariance.length));
        }
        if (covariance.length > 0 && covariance.length != covariance[0].length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NON_SQUARE_MATRIX, java.lang.Integer.valueOf(covariance.length), java.lang.Integer.valueOf(covariance[0].length));
        }
    }

    @Override // org.apache.commons.math.stat.regression.MultipleLinearRegression
    public double[] estimateRegressionParameters() {
        org.apache.commons.math.linear.RealVector b = calculateBeta();
        return b.getData();
    }

    @Override // org.apache.commons.math.stat.regression.MultipleLinearRegression
    public double[] estimateResiduals() {
        org.apache.commons.math.linear.RealVector b = calculateBeta();
        org.apache.commons.math.linear.RealVector e = this.Y.subtract(this.X.operate(b));
        return e.getData();
    }

    @Override // org.apache.commons.math.stat.regression.MultipleLinearRegression
    public double[][] estimateRegressionParametersVariance() {
        return calculateBetaVariance().getData();
    }

    @Override // org.apache.commons.math.stat.regression.MultipleLinearRegression
    public double[] estimateRegressionParametersStandardErrors() {
        double[][] betaVariance = estimateRegressionParametersVariance();
        double sigma = calculateErrorVariance();
        int length = betaVariance[0].length;
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = org.apache.commons.math.util.FastMath.sqrt(betaVariance[i][i] * sigma);
        }
        return result;
    }

    @Override // org.apache.commons.math.stat.regression.MultipleLinearRegression
    public double estimateRegressandVariance() {
        return calculateYVariance();
    }

    public double estimateErrorVariance() {
        return calculateErrorVariance();
    }

    public double estimateRegressionStandardError() {
        return java.lang.Math.sqrt(estimateErrorVariance());
    }

    protected double calculateYVariance() {
        return new org.apache.commons.math.stat.descriptive.moment.Variance().evaluate(this.Y.getData());
    }

    protected double calculateErrorVariance() {
        org.apache.commons.math.linear.RealVector residuals = calculateResiduals();
        return residuals.dotProduct(residuals) / ((double) (this.X.getRowDimension() - this.X.getColumnDimension()));
    }

    protected org.apache.commons.math.linear.RealVector calculateResiduals() {
        org.apache.commons.math.linear.RealVector b = calculateBeta();
        return this.Y.subtract(this.X.operate(b));
    }
}
