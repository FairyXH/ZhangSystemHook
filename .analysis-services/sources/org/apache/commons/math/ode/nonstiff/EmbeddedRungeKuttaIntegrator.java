package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
public abstract class EmbeddedRungeKuttaIntegrator extends org.apache.commons.math.ode.nonstiff.AdaptiveStepsizeIntegrator {
    private final double[][] a;
    private final double[] b;
    private final double[] c;
    private final double exp;
    private final boolean fsal;
    private double maxGrowth;
    private double minReduction;
    private final org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator prototype;
    private double safety;

    protected abstract double estimateError(double[][] dArr, double[] dArr2, double[] dArr3, double d);

    public abstract int getOrder();

    protected EmbeddedRungeKuttaIntegrator(java.lang.String name, boolean fsal, double[] c, double[][] a, double[] b, org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(name, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        this.fsal = fsal;
        this.c = c;
        this.a = a;
        this.b = b;
        this.prototype = prototype;
        this.exp = (-1.0d) / ((double) getOrder());
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(10.0d);
    }

    protected EmbeddedRungeKuttaIntegrator(java.lang.String name, boolean fsal, double[] c, double[][] a, double[] b, org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(name, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.fsal = fsal;
        this.c = c;
        this.a = a;
        this.b = b;
        this.prototype = prototype;
        this.exp = (-1.0d) / ((double) getOrder());
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(10.0d);
    }

    public double getSafety() {
        return this.safety;
    }

    public void setSafety(double safety) {
        this.safety = safety;
    }

    @Override // org.apache.commons.math.ode.nonstiff.AdaptiveStepsizeIntegrator, org.apache.commons.math.ode.FirstOrderIntegrator
    public double integrate(org.apache.commons.math.ode.FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y) throws org.apache.commons.math.ode.DerivativeException, org.apache.commons.math.ode.IntegratorException {
        org.apache.commons.math.ode.sampling.AbstractStepInterpolator interpolator;
        org.apache.commons.math.ode.sampling.AbstractStepInterpolator interpolator2;
        double error;
        org.apache.commons.math.ode.sampling.AbstractStepInterpolator interpolator3;
        double[] yDotTmp;
        double[][] yDotK;
        int stages;
        boolean forward;
        char c;
        double[] yTmp;
        double hNew;
        org.apache.commons.math.ode.sampling.AbstractStepInterpolator interpolator4;
        double[] yTmp2;
        double[] dArr = y0;
        sanityChecks(equations, t0, y0, t, y);
        setEquations(equations);
        resetEvaluations();
        char c2 = 0;
        boolean forward2 = t > t0;
        int stages2 = this.c.length + 1;
        if (y != dArr) {
            java.lang.System.arraycopy(dArr, 0, y, 0, dArr.length);
        }
        double[][] yDotK2 = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, stages2, dArr.length);
        double[] yTmp3 = new double[dArr.length];
        double[] yDotTmp2 = new double[dArr.length];
        if (requiresDenseOutput()) {
            org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator rki = (org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator) this.prototype.copy();
            rki.reinitialize(this, yTmp3, yDotK2, forward2);
            interpolator = rki;
        } else {
            interpolator = new org.apache.commons.math.ode.sampling.DummyStepInterpolator(yTmp3, yDotK2[stages2 - 1], forward2);
        }
        interpolator.storeTime(t0);
        this.stepStart = t0;
        double hNew2 = 0.0d;
        boolean firstTime = true;
        for (org.apache.commons.math.ode.sampling.StepHandler handler : this.stepHandlers) {
            handler.reset();
        }
        setStateInitialized(false);
        this.isLastStep = false;
        while (true) {
            interpolator.shift();
            boolean firstTime2 = firstTime;
            double error2 = 10.0d;
            double hNew3 = hNew2;
            while (error2 >= 1.0d) {
                if (firstTime2 || !this.fsal) {
                    error = error2;
                    computeDerivatives(this.stepStart, y, yDotK2[c2]);
                } else {
                    error = error2;
                }
                if (!firstTime2) {
                    interpolator3 = interpolator;
                    yDotTmp = yDotTmp2;
                    yDotK = yDotK2;
                    stages = stages2;
                    forward = forward2;
                    c = c2;
                    yTmp = yTmp3;
                    hNew = hNew3;
                } else {
                    double[] scale = new double[this.mainSetDimension];
                    if (this.vecAbsoluteTolerance == null) {
                        int i = 0;
                        while (i < scale.length) {
                            scale[i] = this.scalAbsoluteTolerance + (this.scalRelativeTolerance * org.apache.commons.math.util.FastMath.abs(y[i]));
                            i++;
                            yTmp3 = yTmp3;
                            interpolator = interpolator;
                        }
                        interpolator4 = interpolator;
                        yTmp2 = yTmp3;
                    } else {
                        interpolator4 = interpolator;
                        yTmp2 = yTmp3;
                        for (int i2 = 0; i2 < scale.length; i2++) {
                            scale[i2] = this.vecAbsoluteTolerance[i2] + (this.vecRelativeTolerance[i2] * org.apache.commons.math.util.FastMath.abs(y[i2]));
                        }
                    }
                    yTmp = yTmp2;
                    yDotK = yDotK2;
                    yDotTmp = yDotTmp2;
                    stages = stages2;
                    interpolator3 = interpolator4;
                    forward = forward2;
                    c = c2;
                    double hNew4 = initializeStep(equations, forward2, getOrder(), scale, this.stepStart, y, yDotK2[c2], yTmp, yDotK2[1]);
                    firstTime2 = false;
                    hNew = hNew4;
                }
                this.stepSize = hNew;
                for (int k = 1; k < stages; k++) {
                    for (int j = 0; j < dArr.length; j++) {
                        double sum = this.a[k - 1][c] * yDotK[c][j];
                        for (int l = 1; l < k; l++) {
                            sum += this.a[k - 1][l] * yDotK[l][j];
                        }
                        yTmp[j] = y[j] + (this.stepSize * sum);
                    }
                    computeDerivatives(this.stepStart + (this.c[k - 1] * this.stepSize), yTmp, yDotK[k]);
                }
                double[] yTmp4 = yTmp;
                for (int j2 = 0; j2 < dArr.length; j2++) {
                    double sum2 = this.b[c] * yDotK[c][j2];
                    for (int l2 = 1; l2 < stages; l2++) {
                        sum2 += this.b[l2] * yDotK[l2][j2];
                    }
                    yTmp4[j2] = y[j2] + (this.stepSize * sum2);
                }
                error2 = estimateError(yDotK, y, yTmp4, this.stepSize);
                if (error2 < 1.0d) {
                    forward2 = forward;
                    dArr = y0;
                    hNew3 = hNew;
                    yTmp3 = yTmp4;
                    stages2 = stages;
                    interpolator = interpolator3;
                    yDotK2 = yDotK;
                    yDotTmp2 = yDotTmp;
                    c2 = 0;
                } else {
                    double factor = org.apache.commons.math.util.FastMath.min(this.maxGrowth, org.apache.commons.math.util.FastMath.max(this.minReduction, this.safety * org.apache.commons.math.util.FastMath.pow(error2, this.exp)));
                    forward2 = forward;
                    hNew3 = filterStep(this.stepSize * factor, forward2, false);
                    dArr = y0;
                    yTmp3 = yTmp4;
                    stages2 = stages;
                    interpolator = interpolator3;
                    yDotK2 = yDotK;
                    yDotTmp2 = yDotTmp;
                    c2 = 0;
                }
            }
            double error3 = error2;
            org.apache.commons.math.ode.sampling.AbstractStepInterpolator interpolator5 = interpolator;
            double[] yDotTmp3 = yDotTmp2;
            double[][] yDotK3 = yDotK2;
            int stages3 = stages2;
            double[] yTmp5 = yTmp3;
            interpolator5.storeTime(this.stepStart + this.stepSize);
            java.lang.System.arraycopy(yTmp5, 0, y, 0, y0.length);
            java.lang.System.arraycopy(yDotK3[stages3 - 1], 0, yDotTmp3, 0, y0.length);
            this.stepStart = acceptStep(interpolator5, y, yDotTmp3, t);
            if (this.isLastStep) {
                interpolator2 = interpolator5;
                hNew2 = hNew3;
            } else {
                interpolator5.storeTime(this.stepStart);
                if (this.fsal) {
                    java.lang.System.arraycopy(yDotTmp3, 0, yDotK3[0], 0, y0.length);
                }
                interpolator2 = interpolator5;
                double factor2 = org.apache.commons.math.util.FastMath.min(this.maxGrowth, org.apache.commons.math.util.FastMath.max(this.minReduction, this.safety * org.apache.commons.math.util.FastMath.pow(error3, this.exp)));
                double scaledH = this.stepSize * factor2;
                double nextT = this.stepStart + scaledH;
                boolean nextIsLast = !forward2 ? nextT > t : nextT < t;
                double hNew5 = filterStep(scaledH, forward2, nextIsLast);
                double factor3 = this.stepStart;
                double filteredNextT = factor3 + hNew5;
                boolean filteredNextIsLast = !forward2 ? filteredNextT > t : filteredNextT < t;
                hNew2 = filteredNextIsLast ? t - this.stepStart : hNew5;
            }
            if (!this.isLastStep) {
                dArr = y0;
                yTmp3 = yTmp5;
                yDotTmp2 = yDotTmp3;
                stages2 = stages3;
                firstTime = firstTime2;
                interpolator = interpolator2;
                yDotK2 = yDotK3;
                c2 = 0;
            } else {
                double stopTime = this.stepStart;
                resetInternalState();
                return stopTime;
            }
        }
    }

    public double getMinReduction() {
        return this.minReduction;
    }

    public void setMinReduction(double minReduction) {
        this.minReduction = minReduction;
    }

    public double getMaxGrowth() {
        return this.maxGrowth;
    }

    public void setMaxGrowth(double maxGrowth) {
        this.maxGrowth = maxGrowth;
    }
}
