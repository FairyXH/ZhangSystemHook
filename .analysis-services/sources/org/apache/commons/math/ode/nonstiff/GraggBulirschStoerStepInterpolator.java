package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
class GraggBulirschStoerStepInterpolator extends org.apache.commons.math.ode.sampling.AbstractStepInterpolator {
    private static final long serialVersionUID = 7320613236731409847L;
    private int currentDegree;
    private double[] errfac;
    private double[][] polynoms;
    private double[] y0Dot;
    private double[] y1;
    private double[] y1Dot;
    private double[][] yMidDots;

    public GraggBulirschStoerStepInterpolator() {
        this.y0Dot = null;
        this.y1 = null;
        this.y1Dot = null;
        this.yMidDots = null;
        resetTables(-1);
    }

    public GraggBulirschStoerStepInterpolator(double[] y, double[] y0Dot, double[] y1, double[] y1Dot, double[][] yMidDots, boolean forward) {
        super(y, forward);
        this.y0Dot = y0Dot;
        this.y1 = y1;
        this.y1Dot = y1Dot;
        this.yMidDots = yMidDots;
        resetTables(yMidDots.length + 4);
    }

    public GraggBulirschStoerStepInterpolator(org.apache.commons.math.ode.nonstiff.GraggBulirschStoerStepInterpolator interpolator) {
        super(interpolator);
        int dimension = this.currentState.length;
        this.y0Dot = null;
        this.y1 = null;
        this.y1Dot = null;
        this.yMidDots = null;
        if (interpolator.polynoms == null) {
            this.polynoms = null;
            this.currentDegree = -1;
            return;
        }
        resetTables(interpolator.currentDegree);
        for (int i = 0; i < this.polynoms.length; i++) {
            this.polynoms[i] = new double[dimension];
            java.lang.System.arraycopy(interpolator.polynoms[i], 0, this.polynoms[i], 0, dimension);
        }
        int i2 = interpolator.currentDegree;
        this.currentDegree = i2;
    }

    private void resetTables(int maxDegree) {
        if (maxDegree < 0) {
            this.polynoms = null;
            this.errfac = null;
            this.currentDegree = -1;
            return;
        }
        double[][] newPols = new double[maxDegree + 1][];
        if (this.polynoms != null) {
            java.lang.System.arraycopy(this.polynoms, 0, newPols, 0, this.polynoms.length);
            for (int i = this.polynoms.length; i < newPols.length; i++) {
                newPols[i] = new double[this.currentState.length];
            }
        } else {
            for (int i2 = 0; i2 < newPols.length; i2++) {
                newPols[i2] = new double[this.currentState.length];
            }
        }
        this.polynoms = newPols;
        if (maxDegree <= 4) {
            this.errfac = null;
        } else {
            this.errfac = new double[maxDegree - 4];
            for (int i3 = 0; i3 < this.errfac.length; i3++) {
                int ip5 = i3 + 5;
                this.errfac[i3] = 1.0d / ((double) (ip5 * ip5));
                double e = org.apache.commons.math.util.FastMath.sqrt(((double) (i3 + 1)) / ((double) ip5)) * 0.5d;
                for (int j = 0; j <= i3; j++) {
                    double[] dArr = this.errfac;
                    dArr[i3] = dArr[i3] * (e / ((double) (j + 1)));
                }
            }
        }
        this.currentDegree = 0;
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected org.apache.commons.math.ode.sampling.StepInterpolator doCopy() {
        return new org.apache.commons.math.ode.nonstiff.GraggBulirschStoerStepInterpolator(this);
    }

    public void computeCoefficients(int mu, double h) {
        int i = mu;
        if (this.polynoms == null || this.polynoms.length <= i + 4) {
            resetTables(i + 4);
        }
        this.currentDegree = i + 4;
        int i2 = 0;
        while (i2 < this.currentState.length) {
            double yp0 = this.y0Dot[i2] * h;
            double yp1 = this.y1Dot[i2] * h;
            double ydiff = this.y1[i2] - this.currentState[i2];
            double aspl = ydiff - yp1;
            double bspl = yp0 - ydiff;
            this.polynoms[0][i2] = this.currentState[i2];
            this.polynoms[1][i2] = ydiff;
            this.polynoms[2][i2] = aspl;
            this.polynoms[3][i2] = bspl;
            if (i >= 0) {
                double ph0 = ((this.currentState[i2] + this.y1[i2]) * 0.5d) + ((aspl + bspl) * 0.125d);
                this.polynoms[4][i2] = (this.yMidDots[0][i2] - ph0) * 16.0d;
                if (i > 0) {
                    double ph1 = ydiff + ((aspl - bspl) * 0.25d);
                    this.polynoms[5][i2] = (this.yMidDots[1][i2] - ph1) * 16.0d;
                    if (i > 1) {
                        double ph2 = yp1 - yp0;
                        this.polynoms[6][i2] = ((this.yMidDots[2][i2] - ph2) + this.polynoms[4][i2]) * 16.0d;
                        if (i > 2) {
                            double ph3 = (bspl - aspl) * 6.0d;
                            this.polynoms[7][i2] = ((this.yMidDots[3][i2] - ph3) + (this.polynoms[5][i2] * 3.0d)) * 16.0d;
                            int j = 4;
                            while (j <= i) {
                                double fac1 = ((double) j) * 0.5d * ((double) (j - 1));
                                double fac2 = 2.0d * fac1 * ((double) (j - 2)) * ((double) (j - 3));
                                this.polynoms[j + 4][i2] = ((this.yMidDots[j][i2] + (this.polynoms[j + 2][i2] * fac1)) - (this.polynoms[j][i2] * fac2)) * 16.0d;
                                j++;
                                i = mu;
                                yp1 = yp1;
                                ydiff = ydiff;
                                aspl = aspl;
                            }
                        }
                    }
                }
                i2++;
                i = mu;
            } else {
                return;
            }
        }
    }

    public double estimateError(double[] scale) {
        double error = 0.0d;
        if (this.currentDegree < 5) {
            return 0.0d;
        }
        for (int i = 0; i < scale.length; i++) {
            double e = this.polynoms[this.currentDegree][i] / scale[i];
            error += e * e;
        }
        int i2 = scale.length;
        return org.apache.commons.math.util.FastMath.sqrt(error / ((double) i2)) * this.errfac[this.currentDegree - 5];
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        double dot3;
        double oneMinusTheta;
        int dimension = this.currentState.length;
        double oneMinusTheta2 = 1.0d - theta;
        double theta05 = theta - 0.5d;
        double tOmT = theta * oneMinusTheta2;
        double t4 = tOmT * tOmT;
        double t4Dot = tOmT * 2.0d * (1.0d - (theta * 2.0d));
        double dot1 = 1.0d / this.h;
        double dot2 = ((2.0d - (theta * 3.0d)) * theta) / this.h;
        double dot32 = ((((theta * 3.0d) - 4.0d) * theta) + 1.0d) / this.h;
        int i = 0;
        while (i < dimension) {
            int dimension2 = dimension;
            double p0 = this.polynoms[0][i];
            double p1 = this.polynoms[1][i];
            double p2 = this.polynoms[2][i];
            double p3 = this.polynoms[3][i];
            this.interpolatedState[i] = p0 + ((p1 + (((p2 * theta) + (p3 * oneMinusTheta2)) * oneMinusTheta2)) * theta);
            this.interpolatedDerivatives[i] = (dot1 * p1) + (dot2 * p2) + (dot32 * p3);
            if (this.currentDegree <= 3) {
                dot3 = dot32;
                oneMinusTheta = oneMinusTheta2;
            } else {
                double cDot = 0.0d;
                double c = this.polynoms[this.currentDegree][i];
                int j = this.currentDegree - 1;
                while (j > 3) {
                    double dot33 = dot32;
                    double dot34 = j - 3;
                    double d = 1.0d / dot34;
                    cDot = d * ((theta05 * cDot) + c);
                    c = this.polynoms[j][i] + (c * d * theta05);
                    j--;
                    dot32 = dot33;
                }
                dot3 = dot32;
                double[] dArr = this.interpolatedState;
                dArr[i] = dArr[i] + (t4 * c);
                double[] dArr2 = this.interpolatedDerivatives;
                oneMinusTheta = oneMinusTheta2;
                dArr2[i] = dArr2[i] + (((t4 * cDot) + (t4Dot * c)) / this.h);
            }
            i++;
            dimension = dimension2;
            dot32 = dot3;
            oneMinusTheta2 = oneMinusTheta;
        }
        int dimension3 = dimension;
        if (this.h == 0.0d) {
            java.lang.System.arraycopy(this.yMidDots[1], 0, this.interpolatedDerivatives, 0, dimension3);
        }
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void writeExternal(java.io.ObjectOutput out) throws java.io.IOException {
        int dimension = this.currentState == null ? -1 : this.currentState.length;
        writeBaseExternal(out);
        out.writeInt(this.currentDegree);
        for (int k = 0; k <= this.currentDegree; k++) {
            for (int l = 0; l < dimension; l++) {
                out.writeDouble(this.polynoms[k][l]);
            }
        }
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void readExternal(java.io.ObjectInput in) throws java.io.IOException {
        double t = readBaseExternal(in);
        int dimension = this.currentState == null ? -1 : this.currentState.length;
        int degree = in.readInt();
        resetTables(degree);
        this.currentDegree = degree;
        for (int k = 0; k <= this.currentDegree; k++) {
            for (int l = 0; l < dimension; l++) {
                this.polynoms[k][l] = in.readDouble();
            }
        }
        setInterpolatedTime(t);
    }
}
