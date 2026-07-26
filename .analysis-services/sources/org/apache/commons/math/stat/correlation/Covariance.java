package org.apache.commons.math.stat.correlation;

/* JADX INFO: loaded from: classes4.dex */
public class Covariance {
    private final org.apache.commons.math.linear.RealMatrix covarianceMatrix;
    private final int n;

    public Covariance() {
        this.covarianceMatrix = null;
        this.n = 0;
    }

    public Covariance(double[][] data, boolean biasCorrected) {
        this(new org.apache.commons.math.linear.BlockRealMatrix(data), biasCorrected);
    }

    public Covariance(double[][] data) {
        this(data, true);
    }

    public Covariance(org.apache.commons.math.linear.RealMatrix matrix, boolean biasCorrected) {
        checkSufficientData(matrix);
        this.n = matrix.getRowDimension();
        this.covarianceMatrix = computeCovarianceMatrix(matrix, biasCorrected);
    }

    public Covariance(org.apache.commons.math.linear.RealMatrix matrix) {
        this(matrix, true);
    }

    public org.apache.commons.math.linear.RealMatrix getCovarianceMatrix() {
        return this.covarianceMatrix;
    }

    public int getN() {
        return this.n;
    }

    protected org.apache.commons.math.linear.RealMatrix computeCovarianceMatrix(org.apache.commons.math.linear.RealMatrix matrix, boolean biasCorrected) {
        int dimension = matrix.getColumnDimension();
        org.apache.commons.math.stat.descriptive.moment.Variance variance = new org.apache.commons.math.stat.descriptive.moment.Variance(biasCorrected);
        org.apache.commons.math.linear.RealMatrix outMatrix = new org.apache.commons.math.linear.BlockRealMatrix(dimension, dimension);
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < i; j++) {
                double cov = covariance(matrix.getColumn(i), matrix.getColumn(j), biasCorrected);
                outMatrix.setEntry(i, j, cov);
                outMatrix.setEntry(j, i, cov);
            }
            outMatrix.setEntry(i, i, variance.evaluate(matrix.getColumn(i)));
        }
        return outMatrix;
    }

    protected org.apache.commons.math.linear.RealMatrix computeCovarianceMatrix(org.apache.commons.math.linear.RealMatrix matrix) {
        return computeCovarianceMatrix(matrix, true);
    }

    protected org.apache.commons.math.linear.RealMatrix computeCovarianceMatrix(double[][] data, boolean biasCorrected) {
        return computeCovarianceMatrix(new org.apache.commons.math.linear.BlockRealMatrix(data), biasCorrected);
    }

    protected org.apache.commons.math.linear.RealMatrix computeCovarianceMatrix(double[][] data) {
        return computeCovarianceMatrix(data, true);
    }

    public double covariance(double[] xArray, double[] yArray, boolean biasCorrected) throws java.lang.IllegalArgumentException {
        double[] dArr = xArray;
        org.apache.commons.math.stat.descriptive.moment.Mean mean = new org.apache.commons.math.stat.descriptive.moment.Mean();
        double result = 0.0d;
        int length = dArr.length;
        if (length != yArray.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(length), java.lang.Integer.valueOf(yArray.length));
        }
        if (length < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(length), 2);
        }
        double xMean = mean.evaluate(dArr);
        double yMean = mean.evaluate(yArray);
        int i = 0;
        while (i < length) {
            double xDev = dArr[i] - xMean;
            double yDev = yArray[i] - yMean;
            double xMean2 = xMean;
            double xMean3 = i + 1;
            result += ((xDev * yDev) - result) / xMean3;
            i++;
            dArr = xArray;
            xMean = xMean2;
        }
        return biasCorrected ? (((double) length) / ((double) (length - 1))) * result : result;
    }

    public double covariance(double[] xArray, double[] yArray) throws java.lang.IllegalArgumentException {
        return covariance(xArray, yArray, true);
    }

    private void checkSufficientData(org.apache.commons.math.linear.RealMatrix matrix) {
        int nRows = matrix.getRowDimension();
        int nCols = matrix.getColumnDimension();
        if (nRows < 2 || nCols < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_ROWS_AND_COLUMNS, java.lang.Integer.valueOf(nRows), java.lang.Integer.valueOf(nCols));
        }
    }
}
