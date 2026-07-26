package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
public class GraggBulirschStoerIntegrator extends org.apache.commons.math.ode.nonstiff.AdaptiveStepsizeIntegrator {
    private static final java.lang.String METHOD_NAME = "Gragg-Bulirsch-Stoer";
    private double[][] coeff;
    private int[] costPerStep;
    private double[] costPerTimeUnit;
    private int maxChecks;
    private int maxIter;
    private int maxOrder;
    private int mudif;
    private double[] optimalStep;
    private double orderControl1;
    private double orderControl2;
    private boolean performTest;
    private int[] sequence;
    private double stabilityReduction;
    private double stepControl1;
    private double stepControl2;
    private double stepControl3;
    private double stepControl4;
    private boolean useInterpolationError;

    public GraggBulirschStoerIntegrator(double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(METHOD_NAME, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        setStabilityCheck(true, -1, -1, -1.0d);
        setStepsizeControl(-1.0d, -1.0d, -1.0d, -1.0d);
        setOrderControl(-1, -1.0d, -1.0d);
        setInterpolationControl(true, -1);
    }

    public GraggBulirschStoerIntegrator(double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(METHOD_NAME, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        setStabilityCheck(true, -1, -1, -1.0d);
        setStepsizeControl(-1.0d, -1.0d, -1.0d, -1.0d);
        setOrderControl(-1, -1.0d, -1.0d);
        setInterpolationControl(true, -1);
    }

    public void setStabilityCheck(boolean performStabilityCheck, int maxNumIter, int maxNumChecks, double stepsizeReductionFactor) {
        this.performTest = performStabilityCheck;
        this.maxIter = maxNumIter <= 0 ? 2 : maxNumIter;
        this.maxChecks = maxNumChecks <= 0 ? 1 : maxNumChecks;
        if (stepsizeReductionFactor < 1.0E-4d || stepsizeReductionFactor > 0.9999d) {
            this.stabilityReduction = 0.5d;
        } else {
            this.stabilityReduction = stepsizeReductionFactor;
        }
    }

    public void setStepsizeControl(double control1, double control2, double control3, double control4) {
        if (control1 < 1.0E-4d || control1 > 0.9999d) {
            this.stepControl1 = 0.65d;
        } else {
            this.stepControl1 = control1;
        }
        if (control2 < 1.0E-4d || control2 > 0.9999d) {
            this.stepControl2 = 0.94d;
        } else {
            this.stepControl2 = control2;
        }
        if (control3 < 1.0E-4d || control3 > 0.9999d) {
            this.stepControl3 = 0.02d;
        } else {
            this.stepControl3 = control3;
        }
        if (control4 < 1.0001d || control4 > 999.9d) {
            this.stepControl4 = 4.0d;
        } else {
            this.stepControl4 = control4;
        }
    }

    public void setOrderControl(int maximalOrder, double control1, double control2) {
        if (maximalOrder <= 6 || maximalOrder % 2 != 0) {
            this.maxOrder = 18;
        }
        if (control1 < 1.0E-4d || control1 > 0.9999d) {
            this.orderControl1 = 0.8d;
        } else {
            this.orderControl1 = control1;
        }
        if (control2 < 1.0E-4d || control2 > 0.9999d) {
            this.orderControl2 = 0.9d;
        } else {
            this.orderControl2 = control2;
        }
        initializeArrays();
    }

    @Override // org.apache.commons.math.ode.AbstractIntegrator, org.apache.commons.math.ode.ODEIntegrator
    public void addStepHandler(org.apache.commons.math.ode.sampling.StepHandler handler) {
        super.addStepHandler(handler);
        initializeArrays();
    }

    @Override // org.apache.commons.math.ode.AbstractIntegrator, org.apache.commons.math.ode.ODEIntegrator
    public void addEventHandler(org.apache.commons.math.ode.events.EventHandler function, double maxCheckInterval, double convergence, int maxIterationCount) {
        super.addEventHandler(function, maxCheckInterval, convergence, maxIterationCount);
        initializeArrays();
    }

    private void initializeArrays() {
        int size = this.maxOrder / 2;
        if (this.sequence == null || this.sequence.length != size) {
            this.sequence = new int[size];
            this.costPerStep = new int[size];
            this.coeff = new double[size][];
            this.costPerTimeUnit = new double[size];
            this.optimalStep = new double[size];
        }
        if (requiresDenseOutput()) {
            for (int k = 0; k < size; k++) {
                this.sequence[k] = (k * 4) + 2;
            }
        } else {
            for (int k2 = 0; k2 < size; k2++) {
                this.sequence[k2] = (k2 + 1) * 2;
            }
        }
        this.costPerStep[0] = this.sequence[0] + 1;
        for (int k3 = 1; k3 < size; k3++) {
            this.costPerStep[k3] = this.costPerStep[k3 - 1] + this.sequence[k3];
        }
        int k4 = 0;
        while (k4 < size) {
            this.coeff[k4] = k4 > 0 ? new double[k4] : null;
            for (int l = 0; l < k4; l++) {
                double ratio = ((double) this.sequence[k4]) / ((double) this.sequence[(k4 - l) - 1]);
                this.coeff[k4][l] = 1.0d / ((ratio * ratio) - 1.0d);
            }
            k4++;
        }
    }

    public void setInterpolationControl(boolean useInterpolationErrorForControl, int mudifControlParameter) {
        this.useInterpolationError = useInterpolationErrorForControl;
        if (mudifControlParameter <= 0 || mudifControlParameter >= 7) {
            this.mudif = 4;
        } else {
            this.mudif = mudifControlParameter;
        }
    }

    private void rescale(double[] y1, double[] y2, double[] scale) {
        if (this.vecAbsoluteTolerance == null) {
            for (int i = 0; i < scale.length; i++) {
                double yi = org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(y1[i]), org.apache.commons.math.util.FastMath.abs(y2[i]));
                scale[i] = this.scalAbsoluteTolerance + (this.scalRelativeTolerance * yi);
            }
            return;
        }
        for (int i2 = 0; i2 < scale.length; i2++) {
            double yi2 = org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(y1[i2]), org.apache.commons.math.util.FastMath.abs(y2[i2]));
            scale[i2] = this.vecAbsoluteTolerance[i2] + (this.vecRelativeTolerance[i2] * yi2);
        }
    }

    private boolean tryStep(double t0, double[] y0, double step, int k, double[] scale, double[][] f, double[] yMiddle, double[] yEnd, double[] yTmp) throws org.apache.commons.math.ode.DerivativeException {
        int i;
        org.apache.commons.math.ode.nonstiff.GraggBulirschStoerIntegrator graggBulirschStoerIntegrator = this;
        int i2 = k;
        double[] dArr = scale;
        int n = graggBulirschStoerIntegrator.sequence[i2];
        double subStep = step / ((double) n);
        double subStep2 = 2.0d * subStep;
        double t = t0 + subStep;
        int i3 = 0;
        while (true) {
            i = 0;
            if (i3 >= y0.length) {
                break;
            }
            yTmp[i3] = y0[i3];
            yEnd[i3] = y0[i3] + (f[0][i3] * subStep);
            i3++;
        }
        graggBulirschStoerIntegrator.computeDerivatives(t, yEnd, f[1]);
        int j = 1;
        while (j < n) {
            if (j * 2 == n) {
                java.lang.System.arraycopy(yEnd, i, yMiddle, i, y0.length);
            }
            t += subStep;
            for (int i4 = 0; i4 < y0.length; i4++) {
                double middle = yEnd[i4];
                yEnd[i4] = yTmp[i4] + (f[j][i4] * subStep2);
                yTmp[i4] = middle;
            }
            graggBulirschStoerIntegrator.computeDerivatives(t, yEnd, f[j + 1]);
            if (graggBulirschStoerIntegrator.performTest && j <= graggBulirschStoerIntegrator.maxChecks && i2 < graggBulirschStoerIntegrator.maxIter) {
                double initialNorm = 0.0d;
                for (int l = 0; l < dArr.length; l++) {
                    double ratio = f[0][l] / dArr[l];
                    initialNorm += ratio * ratio;
                }
                double deltaNorm = 0.0d;
                for (int l2 = 0; l2 < dArr.length; l2++) {
                    double ratio2 = (f[j + 1][l2] - f[0][l2]) / dArr[l2];
                    deltaNorm += ratio2 * ratio2;
                }
                if (deltaNorm > org.apache.commons.math.util.FastMath.max(1.0E-15d, initialNorm) * 4.0d) {
                    return false;
                }
            }
            j++;
            i2 = k;
            dArr = scale;
            i = 0;
            graggBulirschStoerIntegrator = this;
        }
        for (int i5 = 0; i5 < y0.length; i5++) {
            yEnd[i5] = (yTmp[i5] + yEnd[i5] + (f[n][i5] * subStep)) * 0.5d;
        }
        return true;
    }

    private void extrapolate(int offset, int k, double[][] diag, double[] last) {
        for (int j = 1; j < k; j++) {
            for (int i = 0; i < last.length; i++) {
                diag[(k - j) - 1][i] = diag[k - j][i] + (this.coeff[k + offset][j - 1] * (diag[k - j][i] - diag[(k - j) - 1][i]));
            }
        }
        for (int i2 = 0; i2 < last.length; i2++) {
            last[i2] = diag[0][i2] + (this.coeff[k + offset][k - 1] * (diag[0][i2] - last[i2]));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:204:0x059b  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x06e4  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x06f7  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x06fc  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0703  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x0711 A[LOOP:5: B:42:0x0172->B:252:0x0711, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:258:0x070b A[SYNTHETIC] */
    @Override // org.apache.commons.math.ode.nonstiff.AdaptiveStepsizeIntegrator, org.apache.commons.math.ode.FirstOrderIntegrator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public double integrate(org.apache.commons.math.ode.FirstOrderDifferentialEquations r58, double r59, double[] r61, double r62, double[] r64) throws org.apache.commons.math.ode.DerivativeException, org.apache.commons.math.ode.IntegratorException {
        /*
            Method dump skipped, instruction units count: 1846
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math.ode.nonstiff.GraggBulirschStoerIntegrator.integrate(org.apache.commons.math.ode.FirstOrderDifferentialEquations, double, double[], double, double[]):double");
    }
}
