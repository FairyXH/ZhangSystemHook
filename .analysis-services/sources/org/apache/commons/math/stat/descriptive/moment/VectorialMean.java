package org.apache.commons.math.stat.descriptive.moment;

/* JADX INFO: loaded from: classes4.dex */
public class VectorialMean implements java.io.Serializable {
    private static final long serialVersionUID = 8223009086481006892L;
    private final org.apache.commons.math.stat.descriptive.moment.Mean[] means;

    public VectorialMean(int dimension) {
        this.means = new org.apache.commons.math.stat.descriptive.moment.Mean[dimension];
        for (int i = 0; i < dimension; i++) {
            this.means[i] = new org.apache.commons.math.stat.descriptive.moment.Mean();
        }
    }

    public void increment(double[] v) throws org.apache.commons.math.DimensionMismatchException {
        if (v.length != this.means.length) {
            throw new org.apache.commons.math.DimensionMismatchException(v.length, this.means.length);
        }
        for (int i = 0; i < v.length; i++) {
            this.means[i].increment(v[i]);
        }
    }

    public double[] getResult() {
        double[] result = new double[this.means.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = this.means[i].getResult();
        }
        return result;
    }

    public long getN() {
        if (this.means.length == 0) {
            return 0L;
        }
        return this.means[0].getN();
    }

    public int hashCode() {
        int result = (1 * 31) + java.util.Arrays.hashCode(this.means);
        return result;
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof org.apache.commons.math.stat.descriptive.moment.VectorialMean)) {
            return false;
        }
        org.apache.commons.math.stat.descriptive.moment.VectorialMean other = (org.apache.commons.math.stat.descriptive.moment.VectorialMean) obj;
        return java.util.Arrays.equals(this.means, other.means);
    }
}
