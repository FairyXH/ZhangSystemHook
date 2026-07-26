package org.apache.commons.math.optimization.general;

/* JADX INFO: loaded from: classes4.dex */
public class LevenbergMarquardtOptimizer extends org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer {
    private double[] beta;
    private double costRelativeTolerance;
    private double[] diagR;
    private double initialStepBoundFactor;
    private double[] jacNorm;
    private double[] lmDir;
    private double lmPar;
    private double orthoTolerance;
    private double parRelativeTolerance;
    private int[] permutation;
    private double qrRankingThreshold;
    private int rank;
    private int solvedCols;

    public LevenbergMarquardtOptimizer() {
        setMaxIterations(1000);
        setConvergenceChecker(null);
        setInitialStepBoundFactor(100.0d);
        setCostRelativeTolerance(1.0E-10d);
        setParRelativeTolerance(1.0E-10d);
        setOrthoTolerance(1.0E-10d);
        setQRRankingThreshold(Double.MIN_NORMAL);
    }

    public void setInitialStepBoundFactor(double initialStepBoundFactor) {
        this.initialStepBoundFactor = initialStepBoundFactor;
    }

    public void setCostRelativeTolerance(double costRelativeTolerance) {
        this.costRelativeTolerance = costRelativeTolerance;
    }

    public void setParRelativeTolerance(double parRelativeTolerance) {
        this.parRelativeTolerance = parRelativeTolerance;
    }

    public void setOrthoTolerance(double orthoTolerance) {
        this.orthoTolerance = orthoTolerance;
    }

    public void setQRRankingThreshold(double threshold) {
        this.qrRankingThreshold = threshold;
    }

    /* JADX WARN: Code restructure failed: missing block: B:126:0x0354, code lost:
    
        return r20;
     */
    @Override // org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected org.apache.commons.math.optimization.VectorialPointValuePair doOptimize() throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException, java.lang.IllegalArgumentException {
        /*
            Method dump skipped, instruction units count: 985
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math.optimization.general.LevenbergMarquardtOptimizer.doOptimize():org.apache.commons.math.optimization.VectorialPointValuePair");
    }

    private void determineLMParameter(double[] qy, double delta, double[] diag, double[] work1, double[] work2, double[] work3) {
        double paru;
        double gNorm;
        double dxNorm;
        double[] dArr = qy;
        for (int j = 0; j < this.rank; j++) {
            this.lmDir[this.permutation[j]] = dArr[j];
        }
        for (int j2 = this.rank; j2 < this.cols; j2++) {
            this.lmDir[this.permutation[j2]] = 0.0d;
        }
        int j3 = this.rank;
        for (int k = j3 - 1; k >= 0; k--) {
            int pk = this.permutation[k];
            double ypk = this.lmDir[pk] / this.diagR[pk];
            for (int i = 0; i < k; i++) {
                double[] dArr2 = this.lmDir;
                int i2 = this.permutation[i];
                dArr2[i2] = dArr2[i2] - (this.wjacobian[i][pk] * ypk);
            }
            this.lmDir[pk] = ypk;
        }
        double dxNorm2 = 0.0d;
        for (int j4 = 0; j4 < this.solvedCols; j4++) {
            int pj = this.permutation[j4];
            double s = diag[pj] * this.lmDir[pj];
            work1[pj] = s;
            dxNorm2 += s * s;
        }
        double dxNorm3 = org.apache.commons.math.util.FastMath.sqrt(dxNorm2);
        double fp = dxNorm3 - delta;
        if (fp <= delta * 0.1d) {
            this.lmPar = 0.0d;
            return;
        }
        double parl = 0.0d;
        if (this.rank == this.solvedCols) {
            for (int j5 = 0; j5 < this.solvedCols; j5++) {
                int pj2 = this.permutation[j5];
                work1[pj2] = work1[pj2] * (diag[pj2] / dxNorm3);
            }
            double sum2 = 0.0d;
            int j6 = 0;
            while (true) {
                double parl2 = parl;
                if (j6 >= this.solvedCols) {
                    break;
                }
                int pj3 = this.permutation[j6];
                double sum = 0.0d;
                for (int i3 = 0; i3 < j6; i3++) {
                    sum += this.wjacobian[i3][pj3] * work1[this.permutation[i3]];
                }
                double s2 = (work1[pj3] - sum) / this.diagR[pj3];
                work1[pj3] = s2;
                sum2 += s2 * s2;
                j6++;
                parl = parl2;
            }
            parl = fp / (delta * sum2);
        }
        double sum22 = 0.0d;
        int j7 = 0;
        while (j7 < this.solvedCols) {
            int pj4 = this.permutation[j7];
            double sum3 = 0.0d;
            double fp2 = fp;
            for (int i4 = 0; i4 <= j7; i4++) {
                sum3 += this.wjacobian[i4][pj4] * dArr[i4];
            }
            double sum4 = sum3 / diag[pj4];
            sum22 += sum4 * sum4;
            j7++;
            fp = fp2;
        }
        double fp3 = fp;
        double gNorm2 = org.apache.commons.math.util.FastMath.sqrt(sum22);
        double paru2 = gNorm2 / delta;
        if (paru2 != 0.0d) {
            paru = paru2;
        } else {
            paru = 2.2251E-308d / org.apache.commons.math.util.FastMath.min(delta, 0.1d);
        }
        this.lmPar = org.apache.commons.math.util.FastMath.min(paru, org.apache.commons.math.util.FastMath.max(this.lmPar, parl));
        if (this.lmPar == 0.0d) {
            this.lmPar = gNorm2 / dxNorm3;
        }
        int countdown = 10;
        while (countdown >= 0) {
            double dxNorm4 = this.lmPar;
            if (dxNorm4 == 0.0d) {
                gNorm = gNorm2;
                this.lmPar = org.apache.commons.math.util.FastMath.max(2.2251E-308d, 0.001d * paru);
            } else {
                gNorm = gNorm2;
            }
            double sPar = org.apache.commons.math.util.FastMath.sqrt(this.lmPar);
            for (int j8 = 0; j8 < this.solvedCols; j8++) {
                int pj5 = this.permutation[j8];
                work1[pj5] = diag[pj5] * sPar;
            }
            determineLMDirection(dArr, work1, work2, work3);
            double dxNorm5 = 0.0d;
            for (int j9 = 0; j9 < this.solvedCols; j9++) {
                int pj6 = this.permutation[j9];
                double s3 = diag[pj6] * this.lmDir[pj6];
                work3[pj6] = s3;
                dxNorm5 += s3 * s3;
            }
            double dxNorm6 = org.apache.commons.math.util.FastMath.sqrt(dxNorm5);
            double dxNorm7 = fp3;
            fp3 = dxNorm6 - delta;
            if (org.apache.commons.math.util.FastMath.abs(fp3) <= delta * 0.1d) {
                return;
            }
            if (parl == 0.0d && fp3 <= dxNorm7 && dxNorm7 < 0.0d) {
                return;
            }
            int j10 = 0;
            while (true) {
                double sPar2 = sPar;
                if (j10 >= this.solvedCols) {
                    break;
                }
                int pj7 = this.permutation[j10];
                work1[pj7] = (work3[pj7] * diag[pj7]) / dxNorm6;
                j10++;
                sPar = sPar2;
            }
            int j11 = 0;
            while (j11 < this.solvedCols) {
                int pj8 = this.permutation[j11];
                work1[pj8] = work1[pj8] / work2[j11];
                double tmp = work1[pj8];
                int i5 = j11 + 1;
                while (true) {
                    dxNorm = dxNorm6;
                    if (i5 < this.solvedCols) {
                        int i6 = this.permutation[i5];
                        work1[i6] = work1[i6] - (this.wjacobian[i5][pj8] * tmp);
                        i5++;
                        dxNorm6 = dxNorm;
                    }
                }
                j11++;
                dxNorm6 = dxNorm;
            }
            double dxNorm8 = dxNorm6;
            double sum23 = 0.0d;
            for (int j12 = 0; j12 < this.solvedCols; j12++) {
                double s4 = work1[this.permutation[j12]];
                sum23 += s4 * s4;
            }
            double correction = fp3 / (delta * sum23);
            if (fp3 > 0.0d) {
                parl = org.apache.commons.math.util.FastMath.max(parl, this.lmPar);
            } else if (fp3 < 0.0d) {
                paru = org.apache.commons.math.util.FastMath.min(paru, this.lmPar);
            }
            this.lmPar = org.apache.commons.math.util.FastMath.max(parl, this.lmPar + correction);
            countdown--;
            dArr = qy;
            gNorm2 = gNorm;
            dxNorm3 = dxNorm8;
        }
    }

    private void determineLMDirection(double[] qy, double[] diag, double[] lmDiag, double[] work) {
        int pj;
        double dpj;
        double cotan;
        double cos;
        for (int j = 0; j < this.solvedCols; j++) {
            int pj2 = this.permutation[j];
            for (int i = j + 1; i < this.solvedCols; i++) {
                this.wjacobian[i][pj2] = this.wjacobian[j][this.permutation[i]];
            }
            this.lmDir[j] = this.diagR[pj2];
            work[j] = qy[j];
        }
        int j2 = 0;
        while (true) {
            double d = 0.0d;
            if (j2 >= this.solvedCols) {
                break;
            }
            int pj3 = this.permutation[j2];
            double dpj2 = diag[pj3];
            if (dpj2 != 0.0d) {
                java.util.Arrays.fill(lmDiag, j2 + 1, lmDiag.length, 0.0d);
            }
            lmDiag[j2] = dpj2;
            double qtbpj = 0.0d;
            int k = j2;
            while (k < this.solvedCols) {
                int pk = this.permutation[k];
                if (lmDiag[k] == d) {
                    pj = pj3;
                    dpj = dpj2;
                } else {
                    double rkk = this.wjacobian[k][pk];
                    if (org.apache.commons.math.util.FastMath.abs(rkk) < org.apache.commons.math.util.FastMath.abs(lmDiag[k])) {
                        double cotan2 = rkk / lmDiag[k];
                        double sin = 1.0d / org.apache.commons.math.util.FastMath.sqrt((cotan2 * cotan2) + 1.0d);
                        cotan = cotan2 * sin;
                        cos = sin;
                    } else {
                        double tan = lmDiag[k] / rkk;
                        double cos2 = 1.0d / org.apache.commons.math.util.FastMath.sqrt((tan * tan) + 1.0d);
                        double d2 = cos2 * tan;
                        cotan = cos2;
                        cos = d2;
                    }
                    this.wjacobian[k][pk] = (cotan * rkk) + (lmDiag[k] * cos);
                    double temp = (work[k] * cotan) + (cos * qtbpj);
                    dpj = dpj2;
                    double qtbpj2 = ((-cos) * work[k]) + (cotan * qtbpj);
                    work[k] = temp;
                    int i2 = k + 1;
                    while (i2 < this.solvedCols) {
                        double rik = this.wjacobian[i2][pk];
                        double temp2 = (cotan * rik) + (lmDiag[i2] * cos);
                        lmDiag[i2] = ((-cos) * rik) + (lmDiag[i2] * cotan);
                        this.wjacobian[i2][pk] = temp2;
                        i2++;
                        pj3 = pj3;
                        temp = temp;
                    }
                    pj = pj3;
                    qtbpj = qtbpj2;
                }
                k++;
                pj3 = pj;
                dpj2 = dpj;
                d = 0.0d;
            }
            lmDiag[j2] = this.wjacobian[j2][this.permutation[j2]];
            this.wjacobian[j2][this.permutation[j2]] = this.lmDir[j2];
            j2++;
        }
        int nSing = this.solvedCols;
        for (int j3 = 0; j3 < this.solvedCols; j3++) {
            if (lmDiag[j3] == 0.0d && nSing == this.solvedCols) {
                nSing = j3;
            }
            if (nSing < this.solvedCols) {
                work[j3] = 0.0d;
            }
        }
        if (nSing > 0) {
            for (int j4 = nSing - 1; j4 >= 0; j4--) {
                int pj4 = this.permutation[j4];
                double sum = 0.0d;
                for (int i3 = j4 + 1; i3 < nSing; i3++) {
                    sum += this.wjacobian[i3][pj4] * work[i3];
                }
                work[j4] = (work[j4] - sum) / lmDiag[j4];
            }
        }
        for (int j5 = 0; j5 < this.lmDir.length; j5++) {
            this.lmDir[this.permutation[j5]] = work[j5];
        }
    }

    private void qrDecomposition() throws org.apache.commons.math.optimization.OptimizationException {
        int nextColumn;
        for (int k = 0; k < this.cols; k++) {
            this.permutation[k] = k;
            double norm2 = 0.0d;
            for (int i = 0; i < this.wjacobian.length; i++) {
                double akk = this.wjacobian[i][k];
                norm2 += akk * akk;
            }
            this.jacNorm[k] = org.apache.commons.math.util.FastMath.sqrt(norm2);
        }
        for (int k2 = 0; k2 < this.cols; k2++) {
            int nextColumn2 = -1;
            double ak2 = Double.NEGATIVE_INFINITY;
            for (int i2 = k2; i2 < this.cols; i2++) {
                double norm22 = 0.0d;
                for (int j = k2; j < this.wjacobian.length; j++) {
                    double aki = this.wjacobian[j][this.permutation[i2]];
                    norm22 += aki * aki;
                }
                if (!java.lang.Double.isInfinite(norm22) && !java.lang.Double.isNaN(norm22)) {
                    if (norm22 > ak2) {
                        nextColumn2 = i2;
                        ak2 = norm22;
                    }
                } else {
                    throw new org.apache.commons.math.optimization.OptimizationException(org.apache.commons.math.exception.util.LocalizedFormats.UNABLE_TO_PERFORM_QR_DECOMPOSITION_ON_JACOBIAN, java.lang.Integer.valueOf(this.rows), java.lang.Integer.valueOf(this.cols));
                }
            }
            if (ak2 <= this.qrRankingThreshold) {
                this.rank = k2;
                return;
            }
            int pk = this.permutation[nextColumn2];
            this.permutation[nextColumn2] = this.permutation[k2];
            this.permutation[k2] = pk;
            double akk2 = this.wjacobian[k2][pk];
            double alpha = akk2 > 0.0d ? -org.apache.commons.math.util.FastMath.sqrt(ak2) : org.apache.commons.math.util.FastMath.sqrt(ak2);
            double betak = 1.0d / (ak2 - (akk2 * alpha));
            this.beta[pk] = betak;
            this.diagR[pk] = alpha;
            double[] dArr = this.wjacobian[k2];
            dArr[pk] = dArr[pk] - alpha;
            int dk = (this.cols - 1) - k2;
            while (dk > 0) {
                double gamma = 0.0d;
                int j2 = k2;
                while (true) {
                    nextColumn = nextColumn2;
                    if (j2 >= this.wjacobian.length) {
                        break;
                    }
                    gamma += this.wjacobian[j2][pk] * this.wjacobian[j2][this.permutation[k2 + dk]];
                    j2++;
                    nextColumn2 = nextColumn;
                    ak2 = ak2;
                }
                double ak22 = ak2;
                double gamma2 = gamma * betak;
                for (int j3 = k2; j3 < this.wjacobian.length; j3++) {
                    double[] dArr2 = this.wjacobian[j3];
                    int i3 = this.permutation[k2 + dk];
                    dArr2[i3] = dArr2[i3] - (this.wjacobian[j3][pk] * gamma2);
                }
                dk--;
                nextColumn2 = nextColumn;
                ak2 = ak22;
            }
        }
        int k3 = this.solvedCols;
        this.rank = k3;
    }

    private void qTy(double[] y) {
        for (int k = 0; k < this.cols; k++) {
            int pk = this.permutation[k];
            double gamma = 0.0d;
            for (int i = k; i < this.rows; i++) {
                gamma += this.wjacobian[i][pk] * y[i];
            }
            double gamma2 = gamma * this.beta[pk];
            for (int i2 = k; i2 < this.rows; i2++) {
                y[i2] = y[i2] - (this.wjacobian[i2][pk] * gamma2);
            }
        }
    }
}
