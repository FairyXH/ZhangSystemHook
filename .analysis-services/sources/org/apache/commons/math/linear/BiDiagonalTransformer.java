package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
class BiDiagonalTransformer {
    private org.apache.commons.math.linear.RealMatrix cachedB;
    private org.apache.commons.math.linear.RealMatrix cachedU;
    private org.apache.commons.math.linear.RealMatrix cachedV;
    private final double[][] householderVectors;
    private final double[] main;
    private final double[] secondary;

    public BiDiagonalTransformer(org.apache.commons.math.linear.RealMatrix matrix) {
        int m = matrix.getRowDimension();
        int n = matrix.getColumnDimension();
        int p = org.apache.commons.math.util.FastMath.min(m, n);
        this.householderVectors = matrix.getData();
        this.main = new double[p];
        this.secondary = new double[p - 1];
        this.cachedU = null;
        this.cachedB = null;
        this.cachedV = null;
        if (m >= n) {
            transformToUpperBiDiagonal();
        } else {
            transformToLowerBiDiagonal();
        }
    }

    public org.apache.commons.math.linear.RealMatrix getU() {
        double d;
        if (this.cachedU == null) {
            int m = this.householderVectors.length;
            int n = this.householderVectors[0].length;
            int p = this.main.length;
            int diagOffset = m >= n ? 0 : 1;
            double[] diagonal = m >= n ? this.main : this.secondary;
            this.cachedU = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, m);
            int k = m - 1;
            while (true) {
                d = 1.0d;
                if (k < p) {
                    break;
                }
                this.cachedU.setEntry(k, k, 1.0d);
                k--;
            }
            int k2 = p - 1;
            while (k2 >= diagOffset) {
                double[] hK = this.householderVectors[k2];
                this.cachedU.setEntry(k2, k2, d);
                if (hK[k2 - diagOffset] != 0.0d) {
                    for (int j = k2; j < m; j++) {
                        double alpha = 0.0d;
                        for (int i = k2; i < m; i++) {
                            alpha -= this.cachedU.getEntry(i, j) * this.householderVectors[i][k2 - diagOffset];
                        }
                        double alpha2 = alpha / (diagonal[k2 - diagOffset] * hK[k2 - diagOffset]);
                        for (int i2 = k2; i2 < m; i2++) {
                            this.cachedU.addToEntry(i2, j, (-alpha2) * this.householderVectors[i2][k2 - diagOffset]);
                        }
                    }
                }
                k2--;
                d = 1.0d;
            }
            if (diagOffset > 0) {
                this.cachedU.setEntry(0, 0, 1.0d);
            }
        }
        return this.cachedU;
    }

    public org.apache.commons.math.linear.RealMatrix getB() {
        if (this.cachedB == null) {
            int m = this.householderVectors.length;
            int n = this.householderVectors[0].length;
            this.cachedB = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, n);
            for (int i = 0; i < this.main.length; i++) {
                this.cachedB.setEntry(i, i, this.main[i]);
                if (m < n) {
                    if (i > 0) {
                        this.cachedB.setEntry(i, i - 1, this.secondary[i - 1]);
                    }
                } else if (i < this.main.length - 1) {
                    this.cachedB.setEntry(i, i + 1, this.secondary[i]);
                }
            }
        }
        return this.cachedB;
    }

    public org.apache.commons.math.linear.RealMatrix getV() {
        if (this.cachedV == null) {
            int m = this.householderVectors.length;
            int n = this.householderVectors[0].length;
            int p = this.main.length;
            int diagOffset = m >= n ? 1 : 0;
            double[] diagonal = m >= n ? this.secondary : this.main;
            this.cachedV = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(n, n);
            for (int k = n - 1; k >= p; k--) {
                this.cachedV.setEntry(k, k, 1.0d);
            }
            int k2 = p - 1;
            while (k2 >= diagOffset) {
                double[] hK = this.householderVectors[k2 - diagOffset];
                this.cachedV.setEntry(k2, k2, 1.0d);
                if (hK[k2] != 0.0d) {
                    for (int j = k2; j < n; j++) {
                        double beta = 0.0d;
                        for (int i = k2; i < n; i++) {
                            beta -= this.cachedV.getEntry(i, j) * hK[i];
                        }
                        int i2 = k2 - diagOffset;
                        double beta2 = beta / (diagonal[i2] * hK[k2]);
                        int i3 = k2;
                        while (i3 < n) {
                            this.cachedV.addToEntry(i3, j, (-beta2) * hK[i3]);
                            i3++;
                            p = p;
                        }
                    }
                }
                k2--;
                p = p;
            }
            if (diagOffset > 0) {
                this.cachedV.setEntry(0, 0, 1.0d);
            }
        }
        return this.cachedV;
    }

    double[][] getHouseholderVectorsRef() {
        return this.householderVectors;
    }

    double[] getMainDiagonalRef() {
        return this.main;
    }

    double[] getSecondaryDiagonalRef() {
        return this.secondary;
    }

    boolean isUpperBiDiagonal() {
        return this.householderVectors.length >= this.householderVectors[0].length;
    }

    private void transformToUpperBiDiagonal() {
        int m = this.householderVectors.length;
        int n = this.householderVectors[0].length;
        for (int k = 0; k < n; k++) {
            double xNormSqr = 0.0d;
            for (int i = k; i < m; i++) {
                double c = this.householderVectors[i][k];
                xNormSqr += c * c;
            }
            double[] hK = this.householderVectors[k];
            double a = hK[k] > 0.0d ? -org.apache.commons.math.util.FastMath.sqrt(xNormSqr) : org.apache.commons.math.util.FastMath.sqrt(xNormSqr);
            this.main[k] = a;
            if (a != 0.0d) {
                hK[k] = hK[k] - a;
                for (int j = k + 1; j < n; j++) {
                    double alpha = 0.0d;
                    for (int i2 = k; i2 < m; i2++) {
                        double[] hI = this.householderVectors[i2];
                        alpha -= hI[j] * hI[k];
                    }
                    double alpha2 = alpha / (this.householderVectors[k][k] * a);
                    for (int i3 = k; i3 < m; i3++) {
                        double[] hI2 = this.householderVectors[i3];
                        hI2[j] = hI2[j] - (hI2[k] * alpha2);
                    }
                }
            }
            int j2 = n - 1;
            if (k < j2) {
                double xNormSqr2 = 0.0d;
                for (int j3 = k + 1; j3 < n; j3++) {
                    double c2 = hK[j3];
                    xNormSqr2 += c2 * c2;
                }
                int j4 = k + 1;
                double b = hK[j4] > 0.0d ? -org.apache.commons.math.util.FastMath.sqrt(xNormSqr2) : org.apache.commons.math.util.FastMath.sqrt(xNormSqr2);
                this.secondary[k] = b;
                if (b != 0.0d) {
                    int i4 = k + 1;
                    hK[i4] = hK[i4] - b;
                    for (int i5 = k + 1; i5 < m; i5++) {
                        double[] hI3 = this.householderVectors[i5];
                        double beta = 0.0d;
                        for (int j5 = k + 1; j5 < n; j5++) {
                            beta -= hI3[j5] * hK[j5];
                        }
                        int j6 = k + 1;
                        double beta2 = beta / (hK[j6] * b);
                        for (int j7 = k + 1; j7 < n; j7++) {
                            hI3[j7] = hI3[j7] - (hK[j7] * beta2);
                        }
                    }
                }
            }
        }
    }

    private void transformToLowerBiDiagonal() {
        int m = this.householderVectors.length;
        int n = this.householderVectors[0].length;
        int k = 0;
        while (k < m) {
            double[] hK = this.householderVectors[k];
            double xNormSqr = 0.0d;
            for (int j = k; j < n; j++) {
                double c = hK[j];
                xNormSqr += c * c;
            }
            double a = hK[k] > 0.0d ? -org.apache.commons.math.util.FastMath.sqrt(xNormSqr) : org.apache.commons.math.util.FastMath.sqrt(xNormSqr);
            this.main[k] = a;
            if (a != 0.0d) {
                hK[k] = hK[k] - a;
                for (int i = k + 1; i < m; i++) {
                    double[] hI = this.householderVectors[i];
                    double alpha = 0.0d;
                    for (int j2 = k; j2 < n; j2++) {
                        alpha -= hI[j2] * hK[j2];
                    }
                    double alpha2 = alpha / (this.householderVectors[k][k] * a);
                    for (int j3 = k; j3 < n; j3++) {
                        hI[j3] = hI[j3] - (hK[j3] * alpha2);
                    }
                }
            }
            int i2 = m - 1;
            if (k < i2) {
                double[] hKp1 = this.householderVectors[k + 1];
                double xNormSqr2 = 0.0d;
                for (int i3 = k + 1; i3 < m; i3++) {
                    double c2 = this.householderVectors[i3][k];
                    xNormSqr2 += c2 * c2;
                }
                double b = hKp1[k] > 0.0d ? -org.apache.commons.math.util.FastMath.sqrt(xNormSqr2) : org.apache.commons.math.util.FastMath.sqrt(xNormSqr2);
                this.secondary[k] = b;
                if (b != 0.0d) {
                    hKp1[k] = hKp1[k] - b;
                    int j4 = k + 1;
                    while (j4 < n) {
                        double beta = 0.0d;
                        int i4 = k + 1;
                        while (i4 < m) {
                            int n2 = n;
                            double[] hI2 = this.householderVectors[i4];
                            beta -= hI2[j4] * hI2[k];
                            i4++;
                            n = n2;
                        }
                        int n3 = n;
                        double beta2 = beta / (hKp1[k] * b);
                        for (int i5 = k + 1; i5 < m; i5++) {
                            double[] hI3 = this.householderVectors[i5];
                            hI3[j4] = hI3[j4] - (hI3[k] * beta2);
                        }
                        j4++;
                        n = n3;
                    }
                }
            }
            k++;
            n = n;
        }
    }
}
