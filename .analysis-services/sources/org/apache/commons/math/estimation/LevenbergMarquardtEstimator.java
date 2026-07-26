package org.apache.commons.math.estimation;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public class LevenbergMarquardtEstimator extends org.apache.commons.math.estimation.AbstractEstimator implements java.io.Serializable {
    private static final long serialVersionUID = -5705952631533171019L;
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
    private int rank;
    private int solvedCols;

    public LevenbergMarquardtEstimator() {
        setMaxCostEval(1000);
        setInitialStepBoundFactor(100.0d);
        setCostRelativeTolerance(1.0E-10d);
        setParRelativeTolerance(1.0E-10d);
        setOrthoTolerance(1.0E-10d);
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

    @Override // org.apache.commons.math.estimation.AbstractEstimator, org.apache.commons.math.estimation.Estimator
    public void estimate(org.apache.commons.math.estimation.EstimationProblem problem) throws org.apache.commons.math.estimation.EstimationException {
        double delta;
        double delta2;
        double xNorm;
        double[] oldRes;
        boolean firstIteration;
        double maxCosine;
        double[] diag;
        double[] tmpVec;
        double[] oldRes2;
        double xNorm2;
        double[] oldRes3;
        boolean firstIteration2;
        initializeEstimate(problem);
        this.solvedCols = org.apache.commons.math.util.FastMath.min(this.rows, this.cols);
        this.diagR = new double[this.cols];
        this.jacNorm = new double[this.cols];
        this.beta = new double[this.cols];
        this.permutation = new int[this.cols];
        this.lmDir = new double[this.cols];
        double delta3 = 0.0d;
        double xNorm3 = 0.0d;
        double[] diag2 = new double[this.cols];
        double[] oldX = new double[this.cols];
        double[] oldRes4 = new double[this.rows];
        double[] work1 = new double[this.cols];
        double[] work2 = new double[this.cols];
        double[] work3 = new double[this.cols];
        updateResidualsAndCost();
        this.lmPar = 0.0d;
        boolean firstIteration3 = true;
        while (true) {
            updateJacobian();
            qrDecomposition();
            qTy(this.residuals);
            int k = 0;
            while (k < this.solvedCols) {
                int pk = this.permutation[k];
                this.jacobian[(this.cols * k) + pk] = this.diagR[pk];
                k++;
                delta3 = delta3;
            }
            double delta4 = delta3;
            if (!firstIteration3) {
                delta = delta4;
            } else {
                double xNorm4 = 0.0d;
                for (int k2 = 0; k2 < this.cols; k2++) {
                    double dk = this.jacNorm[k2];
                    if (dk == 0.0d) {
                        dk = 1.0d;
                    }
                    double xk = this.parameters[k2].getEstimate() * dk;
                    xNorm4 += xk * xk;
                    diag2[k2] = dk;
                }
                xNorm3 = org.apache.commons.math.util.FastMath.sqrt(xNorm4);
                delta = this.initialStepBoundFactor;
                if (xNorm3 != 0.0d) {
                    delta *= xNorm3;
                }
            }
            double maxCosine2 = 0.0d;
            if (this.cost == 0.0d) {
                delta2 = delta;
                xNorm = xNorm3;
                oldRes = oldRes4;
                firstIteration = firstIteration3;
                maxCosine = 0.0d;
            } else {
                int j = 0;
                while (j < this.solvedCols) {
                    int pj = this.permutation[j];
                    double delta5 = delta;
                    double s = this.jacNorm[pj];
                    if (s == 0.0d) {
                        xNorm2 = xNorm3;
                        oldRes3 = oldRes4;
                        firstIteration2 = firstIteration3;
                    } else {
                        double sum = 0.0d;
                        int index = pj;
                        double d = xNorm3;
                        xNorm2 = d;
                        for (int i = 0; i <= j; i++) {
                            sum += this.jacobian[index] * this.residuals[i];
                            index += this.cols;
                        }
                        oldRes3 = oldRes4;
                        firstIteration2 = firstIteration3;
                        maxCosine2 = org.apache.commons.math.util.FastMath.max(maxCosine2, org.apache.commons.math.util.FastMath.abs(sum) / (this.cost * s));
                    }
                    j++;
                    delta = delta5;
                    xNorm3 = xNorm2;
                    oldRes4 = oldRes3;
                    firstIteration3 = firstIteration2;
                }
                delta2 = delta;
                xNorm = xNorm3;
                oldRes = oldRes4;
                firstIteration = firstIteration3;
                maxCosine = maxCosine2;
            }
            if (maxCosine <= this.orthoTolerance) {
                return;
            }
            for (int j2 = 0; j2 < this.cols; j2++) {
                diag2[j2] = org.apache.commons.math.util.FastMath.max(diag2[j2], this.jacNorm[j2]);
            }
            double delta6 = delta2;
            oldRes4 = oldRes;
            double ratio = 0.0d;
            while (ratio < 1.0E-4d) {
                for (int j3 = 0; j3 < this.solvedCols; j3++) {
                    int pj2 = this.permutation[j3];
                    oldX[pj2] = this.parameters[pj2].getEstimate();
                }
                double previousCost = this.cost;
                double[] tmpVec2 = this.residuals;
                this.residuals = oldRes4;
                double maxCosine3 = maxCosine;
                double[] oldRes5 = diag2;
                double previousCost2 = delta6;
                determineLMParameter(tmpVec2, delta6, oldRes5, work1, work2, work3);
                double lmNorm = 0.0d;
                for (int j4 = 0; j4 < this.solvedCols; j4++) {
                    int pj3 = this.permutation[j4];
                    this.lmDir[pj3] = -this.lmDir[pj3];
                    this.parameters[pj3].setEstimate(oldX[pj3] + this.lmDir[pj3]);
                    double s2 = diag2[pj3] * this.lmDir[pj3];
                    lmNorm += s2 * s2;
                }
                double lmNorm2 = org.apache.commons.math.util.FastMath.sqrt(lmNorm);
                if (!firstIteration) {
                    delta6 = previousCost2;
                } else {
                    delta6 = org.apache.commons.math.util.FastMath.min(previousCost2, lmNorm2);
                }
                updateResidualsAndCost();
                double actRed = -1.0d;
                if (this.cost * 0.1d < previousCost) {
                    double r = this.cost / previousCost;
                    actRed = 1.0d - (r * r);
                }
                for (int j5 = 0; j5 < this.solvedCols; j5++) {
                    int pj4 = this.permutation[j5];
                    double dirJ = this.lmDir[pj4];
                    work1[j5] = 0.0d;
                    int index2 = pj4;
                    int pj5 = 0;
                    while (pj5 <= j5) {
                        double d2 = work1[pj5];
                        double[] work22 = work2;
                        double[] work23 = this.jacobian;
                        work1[pj5] = d2 + (work23[index2] * dirJ);
                        index2 += this.cols;
                        pj5++;
                        work2 = work22;
                    }
                }
                double[] work24 = work2;
                double coeff1 = 0.0d;
                for (int j6 = 0; j6 < this.solvedCols; j6++) {
                    coeff1 += work1[j6] * work1[j6];
                }
                double pc2 = previousCost * previousCost;
                double coeff12 = coeff1 / pc2;
                double[] work12 = work1;
                double coeff2 = ((this.lmPar * lmNorm2) * lmNorm2) / pc2;
                double preRed = coeff12 + (coeff2 * 2.0d);
                double[] work32 = work3;
                double dirDer = -(coeff12 + coeff2);
                ratio = preRed == 0.0d ? 0.0d : actRed / preRed;
                if (ratio > 0.25d) {
                    double coeff13 = this.lmPar;
                    if (coeff13 == 0.0d || ratio >= 0.75d) {
                        double delta7 = lmNorm2 * 2.0d;
                        double delta8 = this.lmPar;
                        this.lmPar = delta8 * 0.5d;
                        delta6 = delta7;
                    }
                } else {
                    double tmp = actRed < 0.0d ? (dirDer * 0.5d) / (dirDer + (0.5d * actRed)) : 0.5d;
                    double coeff14 = this.cost;
                    if (coeff14 * 0.1d >= previousCost || tmp < 0.1d) {
                        tmp = 0.1d;
                    }
                    double delta9 = org.apache.commons.math.util.FastMath.min(delta6, 10.0d * lmNorm2) * tmp;
                    double delta10 = this.lmPar;
                    this.lmPar = delta10 / tmp;
                    delta6 = delta9;
                }
                if (ratio < 1.0E-4d) {
                    diag = diag2;
                    double previousCost3 = previousCost;
                    this.cost = previousCost3;
                    int j7 = 0;
                    while (j7 < this.solvedCols) {
                        int pj6 = this.permutation[j7];
                        this.parameters[pj6].setEstimate(oldX[pj6]);
                        j7++;
                        previousCost3 = previousCost3;
                    }
                    tmpVec = this.residuals;
                    this.residuals = tmpVec2;
                    oldRes2 = tmpVec;
                } else {
                    firstIteration = false;
                    double xNorm5 = 0.0d;
                    int k3 = 0;
                    while (true) {
                        double lmNorm3 = lmNorm2;
                        if (k3 >= this.cols) {
                            break;
                        }
                        double xK = diag2[k3] * this.parameters[k3].getEstimate();
                        xNorm5 += xK * xK;
                        k3++;
                        lmNorm2 = lmNorm3;
                        diag2 = diag2;
                    }
                    diag = diag2;
                    xNorm = org.apache.commons.math.util.FastMath.sqrt(xNorm5);
                    tmpVec = tmpVec2;
                    oldRes2 = tmpVec2;
                }
                double[] oldRes6 = oldRes2;
                if ((org.apache.commons.math.util.FastMath.abs(actRed) <= this.costRelativeTolerance && preRed <= this.costRelativeTolerance && ratio <= 2.0d) || delta6 <= this.parRelativeTolerance * xNorm) {
                    return;
                }
                if (org.apache.commons.math.util.FastMath.abs(actRed) <= 2.2204E-16d && preRed <= 2.2204E-16d && ratio <= 2.0d) {
                    throw new org.apache.commons.math.estimation.EstimationException("cost relative tolerance is too small ({0}), no further reduction in the sum of squares is possible", java.lang.Double.valueOf(this.costRelativeTolerance));
                }
                if (delta6 <= xNorm * 2.2204E-16d) {
                    throw new org.apache.commons.math.estimation.EstimationException("parameters relative tolerance is too small ({0}), no further improvement in the approximate solution is possible", java.lang.Double.valueOf(this.parRelativeTolerance));
                }
                if (maxCosine3 <= 2.2204E-16d) {
                    throw new org.apache.commons.math.estimation.EstimationException("orthogonality tolerance is too small ({0}), solution is orthogonal to the jacobian", java.lang.Double.valueOf(this.orthoTolerance));
                }
                oldRes4 = oldRes6;
                work1 = work12;
                maxCosine = maxCosine3;
                diag2 = diag;
                work3 = work32;
                work2 = work24;
            }
            double maxCosine4 = delta6;
            delta3 = maxCosine4;
            xNorm3 = xNorm;
            firstIteration3 = firstIteration;
        }
    }

    private void determineLMParameter(double[] qy, double delta, double[] diag, double[] work1, double[] work2, double[] work3) {
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
            int index = pk;
            for (int i = 0; i < k; i++) {
                double[] dArr2 = this.lmDir;
                int i2 = this.permutation[i];
                dArr2[i2] = dArr2[i2] - (this.jacobian[index] * ypk);
                index += this.cols;
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
                int index2 = pj3;
                for (int i3 = 0; i3 < j6; i3++) {
                    sum += this.jacobian[index2] * work1[this.permutation[i3]];
                    index2 += this.cols;
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
            int index3 = pj4;
            double fp2 = fp;
            for (int i4 = 0; i4 <= j7; i4++) {
                sum3 += this.jacobian[index3] * dArr[i4];
                index3 += this.cols;
            }
            double sum4 = sum3 / diag[pj4];
            sum22 += sum4 * sum4;
            j7++;
            fp = fp2;
        }
        double fp3 = fp;
        double gNorm2 = org.apache.commons.math.util.FastMath.sqrt(sum22);
        double paru = gNorm2 / delta;
        if (paru == 0.0d) {
            paru = 2.2251E-308d / org.apache.commons.math.util.FastMath.min(delta, 0.1d);
        }
        this.lmPar = org.apache.commons.math.util.FastMath.min(paru, org.apache.commons.math.util.FastMath.max(this.lmPar, parl));
        if (this.lmPar == 0.0d) {
            this.lmPar = gNorm2 / dxNorm3;
        }
        int countdown = 10;
        while (countdown >= 0) {
            if (this.lmPar == 0.0d) {
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
            double[] dArr3 = work2;
            determineLMDirection(dArr, work1, dArr3, work3);
            double dxNorm4 = 0.0d;
            for (int j9 = 0; j9 < this.solvedCols; j9++) {
                int pj6 = this.permutation[j9];
                double s3 = diag[pj6] * this.lmDir[pj6];
                work3[pj6] = s3;
                dxNorm4 += s3 * s3;
            }
            double dxNorm5 = org.apache.commons.math.util.FastMath.sqrt(dxNorm4);
            double dxNorm6 = fp3;
            fp3 = dxNorm5 - delta;
            if (org.apache.commons.math.util.FastMath.abs(fp3) <= delta * 0.1d) {
                return;
            }
            if (parl == 0.0d && fp3 <= dxNorm6 && dxNorm6 < 0.0d) {
                return;
            }
            int j10 = 0;
            while (true) {
                double sPar2 = sPar;
                if (j10 >= this.solvedCols) {
                    break;
                }
                int pj7 = this.permutation[j10];
                work1[pj7] = (work3[pj7] * diag[pj7]) / dxNorm5;
                j10++;
                sPar = sPar2;
            }
            int j11 = 0;
            while (j11 < this.solvedCols) {
                int pj8 = this.permutation[j11];
                work1[pj8] = work1[pj8] / dArr3[j11];
                double tmp = work1[pj8];
                int i5 = j11 + 1;
                while (true) {
                    dxNorm = dxNorm5;
                    if (i5 < this.solvedCols) {
                        int i6 = this.permutation[i5];
                        work1[i6] = work1[i6] - (this.jacobian[(this.cols * i5) + pj8] * tmp);
                        i5++;
                        dxNorm5 = dxNorm;
                    }
                }
                j11++;
                dArr3 = work2;
                dxNorm5 = dxNorm;
            }
            double dxNorm7 = dxNorm5;
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
            dxNorm3 = dxNorm7;
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
                this.jacobian[(this.cols * i) + pj2] = this.jacobian[(this.cols * j) + this.permutation[i]];
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
                if (lmDiag[k] != d) {
                    double rkk = this.jacobian[(this.cols * k) + pk];
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
                    this.jacobian[(this.cols * k) + pk] = (cotan * rkk) + (lmDiag[k] * cos);
                    double temp = (work[k] * cotan) + (cos * qtbpj);
                    dpj = dpj2;
                    double qtbpj2 = ((-cos) * work[k]) + (cotan * qtbpj);
                    work[k] = temp;
                    int i2 = k + 1;
                    while (i2 < this.solvedCols) {
                        double[] dArr = this.jacobian;
                        int pj4 = pj3;
                        int pj5 = this.cols;
                        double rik = dArr[(pj5 * i2) + pk];
                        double temp2 = (cotan * rik) + (lmDiag[i2] * cos);
                        lmDiag[i2] = ((-cos) * rik) + (lmDiag[i2] * cotan);
                        this.jacobian[(this.cols * i2) + pk] = temp2;
                        i2++;
                        pj3 = pj4;
                        temp = temp;
                    }
                    pj = pj3;
                    qtbpj = qtbpj2;
                } else {
                    pj = pj3;
                    dpj = dpj2;
                }
                k++;
                pj3 = pj;
                dpj2 = dpj;
                d = 0.0d;
            }
            int pj6 = this.cols;
            int index = (pj6 * j2) + this.permutation[j2];
            lmDiag[j2] = this.jacobian[index];
            this.jacobian[index] = this.lmDir[j2];
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
                int pj7 = this.permutation[j4];
                double sum = 0.0d;
                for (int i3 = j4 + 1; i3 < nSing; i3++) {
                    sum += this.jacobian[(this.cols * i3) + pj7] * work[i3];
                }
                work[j4] = (work[j4] - sum) / lmDiag[j4];
            }
        }
        for (int j5 = 0; j5 < this.lmDir.length; j5++) {
            this.lmDir[this.permutation[j5]] = work[j5];
        }
    }

    private void qrDecomposition() throws org.apache.commons.math.estimation.EstimationException {
        double ak2;
        for (int k = 0; k < this.cols; k++) {
            this.permutation[k] = k;
            double norm2 = 0.0d;
            int index = k;
            while (index < this.jacobian.length) {
                double akk = this.jacobian[index];
                norm2 += akk * akk;
                index += this.cols;
            }
            this.jacNorm[k] = org.apache.commons.math.util.FastMath.sqrt(norm2);
        }
        for (int k2 = 0; k2 < this.cols; k2++) {
            int nextColumn = -1;
            double ak22 = Double.NEGATIVE_INFINITY;
            for (int i = k2; i < this.cols; i++) {
                double norm22 = 0.0d;
                int iDiag = (this.cols * k2) + this.permutation[i];
                int index2 = iDiag;
                while (index2 < this.jacobian.length) {
                    double aki = this.jacobian[index2];
                    norm22 += aki * aki;
                    index2 += this.cols;
                }
                if (!java.lang.Double.isInfinite(norm22) && !java.lang.Double.isNaN(norm22)) {
                    if (norm22 > ak22) {
                        nextColumn = i;
                        ak22 = norm22;
                    }
                } else {
                    throw new org.apache.commons.math.estimation.EstimationException(org.apache.commons.math.exception.util.LocalizedFormats.UNABLE_TO_PERFORM_QR_DECOMPOSITION_ON_JACOBIAN, java.lang.Integer.valueOf(this.rows), java.lang.Integer.valueOf(this.cols));
                }
            }
            if (ak22 == 0.0d) {
                this.rank = k2;
                return;
            }
            int pk = this.permutation[nextColumn];
            this.permutation[nextColumn] = this.permutation[k2];
            this.permutation[k2] = pk;
            int kDiag = (this.cols * k2) + pk;
            double akk2 = this.jacobian[kDiag];
            double alpha = akk2 > 0.0d ? -org.apache.commons.math.util.FastMath.sqrt(ak22) : org.apache.commons.math.util.FastMath.sqrt(ak22);
            double betak = 1.0d / (ak22 - (akk2 * alpha));
            this.beta[pk] = betak;
            this.diagR[pk] = alpha;
            double[] dArr = this.jacobian;
            dArr[kDiag] = dArr[kDiag] - alpha;
            int dk = (this.cols - 1) - k2;
            while (dk > 0) {
                int dkp = this.permutation[k2 + dk] - pk;
                double gamma = 0.0d;
                int nextColumn2 = nextColumn;
                int nextColumn3 = kDiag;
                while (true) {
                    ak2 = ak22;
                    if (nextColumn3 >= this.jacobian.length) {
                        break;
                    }
                    gamma += this.jacobian[nextColumn3] * this.jacobian[nextColumn3 + dkp];
                    nextColumn3 += this.cols;
                    ak22 = ak2;
                    alpha = alpha;
                }
                double alpha2 = alpha;
                double gamma2 = gamma * betak;
                int index3 = kDiag;
                while (index3 < this.jacobian.length) {
                    double[] dArr2 = this.jacobian;
                    int i2 = index3 + dkp;
                    dArr2[i2] = dArr2[i2] - (this.jacobian[index3] * gamma2);
                    index3 += this.cols;
                    pk = pk;
                }
                dk--;
                nextColumn = nextColumn2;
                ak22 = ak2;
                alpha = alpha2;
            }
        }
        int k3 = this.solvedCols;
        this.rank = k3;
    }

    private void qTy(double[] y) {
        for (int k = 0; k < this.cols; k++) {
            int pk = this.permutation[k];
            int kDiag = (this.cols * k) + pk;
            double gamma = 0.0d;
            int index = kDiag;
            for (int i = k; i < this.rows; i++) {
                gamma += this.jacobian[index] * y[i];
                index += this.cols;
            }
            double gamma2 = gamma * this.beta[pk];
            int index2 = kDiag;
            for (int i2 = k; i2 < this.rows; i2++) {
                y[i2] = y[i2] - (this.jacobian[index2] * gamma2);
                index2 += this.cols;
            }
        }
    }
}
