package org.apache.commons.math.random;

/* JADX INFO: loaded from: classes4.dex */
public class CorrelatedRandomVectorGenerator implements org.apache.commons.math.random.RandomVectorGenerator {
    private final org.apache.commons.math.random.NormalizedRandomGenerator generator;
    private final double[] mean;
    private final double[] normalized;
    private int rank;
    private org.apache.commons.math.linear.RealMatrix root;

    public CorrelatedRandomVectorGenerator(double[] mean, org.apache.commons.math.linear.RealMatrix covariance, double small, org.apache.commons.math.random.NormalizedRandomGenerator generator) throws org.apache.commons.math.DimensionMismatchException, org.apache.commons.math.linear.NotPositiveDefiniteMatrixException {
        int order = covariance.getRowDimension();
        if (mean.length != order) {
            throw new org.apache.commons.math.DimensionMismatchException(mean.length, order);
        }
        this.mean = (double[]) mean.clone();
        decompose(covariance, small);
        this.generator = generator;
        this.normalized = new double[this.rank];
    }

    public CorrelatedRandomVectorGenerator(org.apache.commons.math.linear.RealMatrix covariance, double small, org.apache.commons.math.random.NormalizedRandomGenerator generator) throws org.apache.commons.math.linear.NotPositiveDefiniteMatrixException {
        int order = covariance.getRowDimension();
        this.mean = new double[order];
        for (int i = 0; i < order; i++) {
            this.mean[i] = 0.0d;
        }
        decompose(covariance, small);
        this.generator = generator;
        this.normalized = new double[this.rank];
    }

    public org.apache.commons.math.random.NormalizedRandomGenerator getGenerator() {
        return this.generator;
    }

    public org.apache.commons.math.linear.RealMatrix getRootMatrix() {
        return this.root;
    }

    public int getRank() {
        return this.rank;
    }

    private void decompose(org.apache.commons.math.linear.RealMatrix covariance, double small) throws org.apache.commons.math.linear.NotPositiveDefiniteMatrixException {
        int[] swap;
        int i;
        int order = covariance.getRowDimension();
        double[][] c = covariance.getData();
        int i2 = 1;
        double[][] b = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, order, order);
        int[] swap2 = new int[order];
        int[] index = new int[order];
        for (int i3 = 0; i3 < order; i3++) {
            index[i3] = i3;
        }
        this.rank = 0;
        boolean loop = true;
        while (loop) {
            swap2[this.rank] = this.rank;
            for (int i4 = this.rank + i2; i4 < order; i4++) {
                int ii = index[i4];
                int isi = index[swap2[i4]];
                if (c[ii][ii] > c[isi][isi]) {
                    swap2[this.rank] = i4;
                }
            }
            int i5 = this.rank;
            if (swap2[i5] != this.rank) {
                int tmp = index[this.rank];
                index[this.rank] = index[swap2[this.rank]];
                index[swap2[this.rank]] = tmp;
            }
            int tmp2 = this.rank;
            int ir = index[tmp2];
            if (c[ir][ir] < small) {
                if (this.rank == 0) {
                    throw new org.apache.commons.math.linear.NotPositiveDefiniteMatrixException();
                }
                int i6 = this.rank;
                while (i6 < order) {
                    int[] swap3 = swap2;
                    if (c[index[i6]][index[i6]] >= (-small)) {
                        i6++;
                        swap2 = swap3;
                    } else {
                        throw new org.apache.commons.math.linear.NotPositiveDefiniteMatrixException();
                    }
                }
                swap = swap2;
                this.rank += i2;
                i = i2;
                loop = false;
            } else {
                swap = swap2;
                double sqrt = org.apache.commons.math.util.FastMath.sqrt(c[ir][ir]);
                b[this.rank][this.rank] = sqrt;
                double inverse = 1.0d / sqrt;
                int i7 = this.rank + i2;
                while (i7 < order) {
                    int ii2 = index[i7];
                    double e = c[ii2][ir] * inverse;
                    b[i7][this.rank] = e;
                    double[] dArr = c[ii2];
                    dArr[ii2] = dArr[ii2] - (e * e);
                    for (int j = this.rank + i2; j < i7; j++) {
                        int ij = index[j];
                        double f = c[ii2][ij] - (b[j][this.rank] * e);
                        c[ii2][ij] = f;
                        c[ij][ii2] = f;
                    }
                    i7++;
                    i2 = 1;
                }
                i = 1;
                int i8 = this.rank + 1;
                this.rank = i8;
                loop = i8 < order;
            }
            i2 = i;
            swap2 = swap;
        }
        this.root = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(order, this.rank);
        for (int i9 = 0; i9 < order; i9++) {
            for (int j2 = 0; j2 < this.rank; j2++) {
                this.root.setEntry(index[i9], j2, b[i9][j2]);
            }
        }
    }

    @Override // org.apache.commons.math.random.RandomVectorGenerator
    public double[] nextVector() {
        for (int i = 0; i < this.rank; i++) {
            this.normalized[i] = this.generator.nextNormalizedDouble();
        }
        double[] correlated = new double[this.mean.length];
        for (int i2 = 0; i2 < correlated.length; i2++) {
            correlated[i2] = this.mean[i2];
            for (int j = 0; j < this.rank; j++) {
                correlated[i2] = correlated[i2] + (this.root.getEntry(i2, j) * this.normalized[j]);
            }
        }
        return correlated;
    }
}
