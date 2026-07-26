package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
public abstract class RungeKuttaIntegrator extends org.apache.commons.math.ode.AbstractIntegrator {
    private final double[][] a;
    private final double[] b;
    private final double[] c;
    private final org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator prototype;
    private final double step;

    protected RungeKuttaIntegrator(java.lang.String name, double[] c, double[][] a, double[] b, org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator prototype, double step) {
        super(name);
        this.c = c;
        this.a = a;
        this.b = b;
        this.prototype = prototype;
        this.step = org.apache.commons.math.util.FastMath.abs(step);
    }

    @Override // org.apache.commons.math.ode.FirstOrderIntegrator
    public double integrate(org.apache.commons.math.ode.FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y) throws org.apache.commons.math.ode.DerivativeException, org.apache.commons.math.ode.IntegratorException {
        org.apache.commons.math.ode.sampling.AbstractStepInterpolator interpolator;
        sanityChecks(equations, t0, y0, t, y);
        setEquations(equations);
        resetEvaluations();
        char c = 0;
        boolean forward = t > t0;
        int stages = this.c.length + 1;
        if (y != y0) {
            java.lang.System.arraycopy(y0, 0, y, 0, y0.length);
        }
        double[][] yDotK = new double[stages][];
        for (int i = 0; i < stages; i++) {
            yDotK[i] = new double[y0.length];
        }
        int i2 = y0.length;
        double[] yTmp = new double[i2];
        double[] yDotTmp = new double[y0.length];
        if (requiresDenseOutput()) {
            org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator rki = (org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator) this.prototype.copy();
            rki.reinitialize(this, yTmp, yDotK, forward);
            interpolator = rki;
        } else {
            interpolator = new org.apache.commons.math.ode.sampling.DummyStepInterpolator(yTmp, yDotK[stages - 1], forward);
        }
        interpolator.storeTime(t0);
        this.stepStart = t0;
        double d = this.step;
        if (!forward) {
            d = -d;
        }
        this.stepSize = d;
        for (org.apache.commons.math.ode.sampling.StepHandler handler : this.stepHandlers) {
            handler.reset();
        }
        setStateInitialized(false);
        this.isLastStep = false;
        while (true) {
            interpolator.shift();
            computeDerivatives(this.stepStart, y, yDotK[c]);
            int k = 1;
            while (k < stages) {
                int j = 0;
                while (j < y0.length) {
                    double sum = this.a[k - 1][c] * yDotK[c][j];
                    for (int l = 1; l < k; l++) {
                        sum += this.a[k - 1][l] * yDotK[l][j];
                    }
                    yTmp[j] = y[j] + (this.stepSize * sum);
                    j++;
                    forward = forward;
                    c = 0;
                }
                computeDerivatives(this.stepStart + (this.c[k - 1] * this.stepSize), yTmp, yDotK[k]);
                k++;
                forward = forward;
                c = 0;
            }
            boolean forward2 = forward;
            for (int j2 = 0; j2 < y0.length; j2++) {
                double sum2 = this.b[0] * yDotK[0][j2];
                for (int l2 = 1; l2 < stages; l2++) {
                    sum2 += this.b[l2] * yDotK[l2][j2];
                }
                yTmp[j2] = y[j2] + (this.stepSize * sum2);
            }
            interpolator.storeTime(this.stepStart + this.stepSize);
            java.lang.System.arraycopy(yTmp, 0, y, 0, y0.length);
            java.lang.System.arraycopy(yDotK[stages - 1], 0, yDotTmp, 0, y0.length);
            org.apache.commons.math.ode.sampling.AbstractStepInterpolator interpolator2 = interpolator;
            double[] yTmp2 = yTmp;
            double[] yDotTmp2 = yDotTmp;
            this.stepStart = acceptStep(interpolator, y, yDotTmp, t);
            if (!this.isLastStep) {
                interpolator2.storeTime(this.stepStart);
                double nextT = this.stepStart + this.stepSize;
                boolean nextIsLast = !forward2 ? nextT > t : nextT < t;
                if (nextIsLast) {
                    this.stepSize = t - this.stepStart;
                }
            }
            if (!this.isLastStep) {
                interpolator = interpolator2;
                yTmp = yTmp2;
                yDotTmp = yDotTmp2;
                forward = forward2;
                c = 0;
            } else {
                double stopTime = this.stepStart;
                this.stepStart = Double.NaN;
                this.stepSize = Double.NaN;
                return stopTime;
            }
        }
    }
}
