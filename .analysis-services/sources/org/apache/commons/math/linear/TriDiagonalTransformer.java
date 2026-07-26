package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
class TriDiagonalTransformer {
    private org.apache.commons.math.linear.RealMatrix cachedQ;
    private org.apache.commons.math.linear.RealMatrix cachedQt;
    private org.apache.commons.math.linear.RealMatrix cachedT;
    private final double[][] householderVectors;
    private final double[] main;
    private final double[] secondary;

    public TriDiagonalTransformer(org.apache.commons.math.linear.RealMatrix matrix) throws org.apache.commons.math.linear.InvalidMatrixException {
        if (!matrix.isSquare()) {
            throw new org.apache.commons.math.linear.NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
        }
        int m = matrix.getRowDimension();
        this.householderVectors = matrix.getData();
        this.main = new double[m];
        this.secondary = new double[m - 1];
        this.cachedQ = null;
        this.cachedQt = null;
        this.cachedT = null;
        transform();
    }

    public org.apache.commons.math.linear.RealMatrix getQ() {
        if (this.cachedQ == null) {
            this.cachedQ = getQT().transpose();
        }
        return this.cachedQ;
    }

    public org.apache.commons.math.linear.RealMatrix getQT() {
        if (this.cachedQt == null) {
            int m = this.householderVectors.length;
            this.cachedQt = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, m);
            for (int k = m - 1; k >= 1; k--) {
                double[] hK = this.householderVectors[k - 1];
                double inv = 1.0d / (this.secondary[k - 1] * hK[k]);
                this.cachedQt.setEntry(k, k, 1.0d);
                if (hK[k] != 0.0d) {
                    double beta = 1.0d / this.secondary[k - 1];
                    this.cachedQt.setEntry(k, k, (hK[k] * beta) + 1.0d);
                    for (int i = k + 1; i < m; i++) {
                        this.cachedQt.setEntry(k, i, hK[i] * beta);
                    }
                    for (int j = k + 1; j < m; j++) {
                        double beta2 = 0.0d;
                        for (int i2 = k + 1; i2 < m; i2++) {
                            beta2 += this.cachedQt.getEntry(j, i2) * hK[i2];
                        }
                        double beta3 = beta2 * inv;
                        this.cachedQt.setEntry(j, k, hK[k] * beta3);
                        for (int i3 = k + 1; i3 < m; i3++) {
                            this.cachedQt.addToEntry(j, i3, hK[i3] * beta3);
                        }
                    }
                }
            }
            this.cachedQt.setEntry(0, 0, 1.0d);
        }
        return this.cachedQt;
    }

    public org.apache.commons.math.linear.RealMatrix getT() {
        if (this.cachedT == null) {
            int m = this.main.length;
            this.cachedT = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, m);
            for (int i = 0; i < m; i++) {
                this.cachedT.setEntry(i, i, this.main[i]);
                if (i > 0) {
                    this.cachedT.setEntry(i, i - 1, this.secondary[i - 1]);
                }
                if (i < this.main.length - 1) {
                    this.cachedT.setEntry(i, i + 1, this.secondary[i]);
                }
            }
        }
        return this.cachedT;
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

    private void transform() {
        int m = this.householderVectors.length;
        double[] z = new double[m];
        for (int k = 0; k < m - 1; k++) {
            double[] hK = this.householderVectors[k];
            this.main[k] = hK[k];
            double xNormSqr = 0.0d;
            for (int j = k + 1; j < m; j++) {
                double c = hK[j];
                xNormSqr += c * c;
            }
            int j2 = k + 1;
            double a = hK[j2] > 0.0d ? -org.apache.commons.math.util.FastMath.sqrt(xNormSqr) : org.apache.commons.math.util.FastMath.sqrt(xNormSqr);
            this.secondary[k] = a;
            if (a != 0.0d) {
                int i = k + 1;
                hK[i] = hK[i] - a;
                double beta = (-1.0d) / (hK[k + 1] * a);
                java.util.Arrays.fill(z, k + 1, m, 0.0d);
                int i2 = k + 1;
                while (i2 < m) {
                    double[] hI = this.householderVectors[i2];
                    double hKI = hK[i2];
                    double zI = hI[i2] * hKI;
                    double xNormSqr2 = xNormSqr;
                    for (int j3 = i2 + 1; j3 < m; j3++) {
                        double hIJ = hI[j3];
                        zI += hK[j3] * hIJ;
                        z[j3] = z[j3] + (hIJ * hKI);
                    }
                    z[i2] = (z[i2] + zI) * beta;
                    i2++;
                    xNormSqr = xNormSqr2;
                }
                double gamma = 0.0d;
                for (int i3 = k + 1; i3 < m; i3++) {
                    gamma += z[i3] * hK[i3];
                }
                double gamma2 = gamma * (beta / 2.0d);
                for (int i4 = k + 1; i4 < m; i4++) {
                    z[i4] = z[i4] - (hK[i4] * gamma2);
                }
                for (int i5 = k + 1; i5 < m; i5++) {
                    double[] hI2 = this.householderVectors[i5];
                    for (int j4 = i5; j4 < m; j4++) {
                        hI2[j4] = hI2[j4] - ((hK[i5] * z[j4]) + (z[i5] * hK[j4]));
                    }
                }
            }
        }
        this.main[m - 1] = this.householderVectors[m - 1][m - 1];
    }
}
