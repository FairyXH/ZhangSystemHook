package org.apache.commons.math.stat.correlation;

/* JADX INFO: loaded from: classes4.dex */
public class SpearmansCorrelation {
    private final org.apache.commons.math.linear.RealMatrix data;
    private final org.apache.commons.math.stat.correlation.PearsonsCorrelation rankCorrelation;
    private final org.apache.commons.math.stat.ranking.RankingAlgorithm rankingAlgorithm;

    public SpearmansCorrelation(org.apache.commons.math.linear.RealMatrix dataMatrix, org.apache.commons.math.stat.ranking.RankingAlgorithm rankingAlgorithm) {
        this.data = dataMatrix.copy();
        this.rankingAlgorithm = rankingAlgorithm;
        rankTransform(this.data);
        this.rankCorrelation = new org.apache.commons.math.stat.correlation.PearsonsCorrelation(this.data);
    }

    public SpearmansCorrelation(org.apache.commons.math.linear.RealMatrix dataMatrix) {
        this(dataMatrix, new org.apache.commons.math.stat.ranking.NaturalRanking());
    }

    public SpearmansCorrelation() {
        this.data = null;
        this.rankingAlgorithm = new org.apache.commons.math.stat.ranking.NaturalRanking();
        this.rankCorrelation = null;
    }

    public org.apache.commons.math.linear.RealMatrix getCorrelationMatrix() {
        return this.rankCorrelation.getCorrelationMatrix();
    }

    public org.apache.commons.math.stat.correlation.PearsonsCorrelation getRankCorrelation() {
        return this.rankCorrelation;
    }

    public org.apache.commons.math.linear.RealMatrix computeCorrelationMatrix(org.apache.commons.math.linear.RealMatrix matrix) {
        org.apache.commons.math.linear.RealMatrix matrixCopy = matrix.copy();
        rankTransform(matrixCopy);
        return new org.apache.commons.math.stat.correlation.PearsonsCorrelation().computeCorrelationMatrix(matrixCopy);
    }

    public org.apache.commons.math.linear.RealMatrix computeCorrelationMatrix(double[][] matrix) {
        return computeCorrelationMatrix(new org.apache.commons.math.linear.BlockRealMatrix(matrix));
    }

    public double correlation(double[] xArray, double[] yArray) throws java.lang.IllegalArgumentException {
        if (xArray.length != yArray.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(xArray.length), java.lang.Integer.valueOf(yArray.length));
        }
        if (xArray.length < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(xArray.length), 2);
        }
        return new org.apache.commons.math.stat.correlation.PearsonsCorrelation().correlation(this.rankingAlgorithm.rank(xArray), this.rankingAlgorithm.rank(yArray));
    }

    private void rankTransform(org.apache.commons.math.linear.RealMatrix matrix) {
        for (int i = 0; i < matrix.getColumnDimension(); i++) {
            matrix.setColumn(i, this.rankingAlgorithm.rank(matrix.getColumn(i)));
        }
    }
}
