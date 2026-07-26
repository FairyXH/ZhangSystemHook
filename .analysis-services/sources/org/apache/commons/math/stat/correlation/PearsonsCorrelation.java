package org.apache.commons.math.stat.correlation;

/* JADX INFO: loaded from: classes4.dex */
public class PearsonsCorrelation {
    private final org.apache.commons.math.linear.RealMatrix correlationMatrix;
    private final int nObs;

    public PearsonsCorrelation() {
        this.correlationMatrix = null;
        this.nObs = 0;
    }

    public PearsonsCorrelation(double[][] data) {
        this(new org.apache.commons.math.linear.BlockRealMatrix(data));
    }

    public PearsonsCorrelation(org.apache.commons.math.linear.RealMatrix matrix) {
        checkSufficientData(matrix);
        this.nObs = matrix.getRowDimension();
        this.correlationMatrix = computeCorrelationMatrix(matrix);
    }

    public PearsonsCorrelation(org.apache.commons.math.stat.correlation.Covariance covariance) {
        org.apache.commons.math.linear.RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        if (covarianceMatrix == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.COVARIANCE_MATRIX);
        }
        this.nObs = covariance.getN();
        this.correlationMatrix = covarianceToCorrelation(covarianceMatrix);
    }

    public PearsonsCorrelation(org.apache.commons.math.linear.RealMatrix covarianceMatrix, int numberOfObservations) {
        this.nObs = numberOfObservations;
        this.correlationMatrix = covarianceToCorrelation(covarianceMatrix);
    }

    public org.apache.commons.math.linear.RealMatrix getCorrelationMatrix() {
        return this.correlationMatrix;
    }

    public org.apache.commons.math.linear.RealMatrix getCorrelationStandardErrors() {
        int nVars = this.correlationMatrix.getColumnDimension();
        double[][] out = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, nVars, nVars);
        for (int i = 0; i < nVars; i++) {
            for (int j = 0; j < nVars; j++) {
                double r = this.correlationMatrix.getEntry(i, j);
                out[i][j] = org.apache.commons.math.util.FastMath.sqrt((1.0d - (r * r)) / ((double) (this.nObs - 2)));
            }
        }
        return new org.apache.commons.math.linear.BlockRealMatrix(out);
    }

    public org.apache.commons.math.linear.RealMatrix getCorrelationPValues() throws org.apache.commons.math.MathException {
        org.apache.commons.math.distribution.TDistribution tDistribution = new org.apache.commons.math.distribution.TDistributionImpl(this.nObs - 2);
        int nVars = this.correlationMatrix.getColumnDimension();
        double[][] out = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, nVars, nVars);
        for (int i = 0; i < nVars; i++) {
            for (int j = 0; j < nVars; j++) {
                if (i == j) {
                    out[i][j] = 0.0d;
                } else {
                    double r = this.correlationMatrix.getEntry(i, j);
                    double t = org.apache.commons.math.util.FastMath.abs(org.apache.commons.math.util.FastMath.sqrt(((double) (this.nObs - 2)) / (1.0d - (r * r))) * r);
                    out[i][j] = tDistribution.cumulativeProbability(-t) * 2.0d;
                }
            }
        }
        return new org.apache.commons.math.linear.BlockRealMatrix(out);
    }

    public org.apache.commons.math.linear.RealMatrix computeCorrelationMatrix(org.apache.commons.math.linear.RealMatrix matrix) {
        int nVars = matrix.getColumnDimension();
        org.apache.commons.math.linear.RealMatrix outMatrix = new org.apache.commons.math.linear.BlockRealMatrix(nVars, nVars);
        for (int i = 0; i < nVars; i++) {
            for (int j = 0; j < i; j++) {
                double corr = correlation(matrix.getColumn(i), matrix.getColumn(j));
                outMatrix.setEntry(i, j, corr);
                outMatrix.setEntry(j, i, corr);
            }
            outMatrix.setEntry(i, i, 1.0d);
        }
        return outMatrix;
    }

    public org.apache.commons.math.linear.RealMatrix computeCorrelationMatrix(double[][] data) {
        return computeCorrelationMatrix(new org.apache.commons.math.linear.BlockRealMatrix(data));
    }

    public double correlation(double[] xArray, double[] yArray) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.stat.regression.SimpleRegression regression = new org.apache.commons.math.stat.regression.SimpleRegression();
        if (xArray.length != yArray.length) {
            throw new org.apache.commons.math.exception.DimensionMismatchException(xArray.length, yArray.length);
        }
        if (xArray.length < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(xArray.length), 2);
        }
        for (int i = 0; i < xArray.length; i++) {
            regression.addData(xArray[i], yArray[i]);
        }
        return regression.getR();
    }

    public org.apache.commons.math.linear.RealMatrix covarianceToCorrelation(org.apache.commons.math.linear.RealMatrix covarianceMatrix) {
        int nVars = covarianceMatrix.getColumnDimension();
        org.apache.commons.math.linear.RealMatrix outMatrix = new org.apache.commons.math.linear.BlockRealMatrix(nVars, nVars);
        for (int i = 0; i < nVars; i++) {
            double sigma = org.apache.commons.math.util.FastMath.sqrt(covarianceMatrix.getEntry(i, i));
            outMatrix.setEntry(i, i, 1.0d);
            for (int j = 0; j < i; j++) {
                double entry = covarianceMatrix.getEntry(i, j) / (org.apache.commons.math.util.FastMath.sqrt(covarianceMatrix.getEntry(j, j)) * sigma);
                outMatrix.setEntry(i, j, entry);
                outMatrix.setEntry(j, i, entry);
            }
        }
        return outMatrix;
    }

    private void checkSufficientData(org.apache.commons.math.linear.RealMatrix matrix) {
        int nRows = matrix.getRowDimension();
        int nCols = matrix.getColumnDimension();
        if (nRows < 2 || nCols < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_ROWS_AND_COLUMNS, java.lang.Integer.valueOf(nRows), java.lang.Integer.valueOf(nCols));
        }
    }
}
