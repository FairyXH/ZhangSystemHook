package org.apache.commons.math.stat.descriptive.moment;

/* JADX INFO: loaded from: classes4.dex */
public class VectorialCovariance implements java.io.Serializable {
    private static final long serialVersionUID = 4118372414238930270L;
    private final boolean isBiasCorrected;
    private long n = 0;
    private final double[] productsSums;
    private final double[] sums;

    public VectorialCovariance(int dimension, boolean isBiasCorrected) {
        this.sums = new double[dimension];
        this.productsSums = new double[((dimension + 1) * dimension) / 2];
        this.isBiasCorrected = isBiasCorrected;
    }

    public void increment(double[] v) throws org.apache.commons.math.DimensionMismatchException {
        if (v.length != this.sums.length) {
            throw new org.apache.commons.math.DimensionMismatchException(v.length, this.sums.length);
        }
        int k = 0;
        for (int i = 0; i < v.length; i++) {
            double[] dArr = this.sums;
            dArr[i] = dArr[i] + v[i];
            int j = 0;
            while (j <= i) {
                double[] dArr2 = this.productsSums;
                dArr2[k] = dArr2[k] + (v[i] * v[j]);
                j++;
                k++;
            }
        }
        this.n++;
    }

    public org.apache.commons.math.linear.RealMatrix getResult() {
        int dimension = this.sums.length;
        org.apache.commons.math.linear.RealMatrix result = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(dimension, dimension);
        if (this.n > 1) {
            double c = 1.0d / (this.n * (this.isBiasCorrected ? this.n - 1 : this.n));
            int k = 0;
            for (int i = 0; i < dimension; i++) {
                int j = 0;
                while (j <= i) {
                    double e = ((this.n * this.productsSums[k]) - (this.sums[i] * this.sums[j])) * c;
                    result.setEntry(i, j, e);
                    result.setEntry(j, i, e);
                    j++;
                    k++;
                }
            }
        }
        return result;
    }

    public long getN() {
        return this.n;
    }

    public void clear() {
        this.n = 0L;
        java.util.Arrays.fill(this.sums, 0.0d);
        java.util.Arrays.fill(this.productsSums, 0.0d);
    }

    public int hashCode() {
        int result = (1 * 31) + (this.isBiasCorrected ? 1231 : 1237);
        return (((((result * 31) + ((int) (this.n ^ (this.n >>> 32)))) * 31) + java.util.Arrays.hashCode(this.productsSums)) * 31) + java.util.Arrays.hashCode(this.sums);
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof org.apache.commons.math.stat.descriptive.moment.VectorialCovariance)) {
            return false;
        }
        org.apache.commons.math.stat.descriptive.moment.VectorialCovariance other = (org.apache.commons.math.stat.descriptive.moment.VectorialCovariance) obj;
        return this.isBiasCorrected == other.isBiasCorrected && this.n == other.n && java.util.Arrays.equals(this.productsSums, other.productsSums) && java.util.Arrays.equals(this.sums, other.sums);
    }
}
