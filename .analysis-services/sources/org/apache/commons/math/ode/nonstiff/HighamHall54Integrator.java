package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
public class HighamHall54Integrator extends org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator {
    private static final java.lang.String METHOD_NAME = "Higham-Hall 5(4)";
    private static final double[] STATIC_C = {0.2222222222222222d, 0.3333333333333333d, 0.5d, 0.6d, 1.0d, 1.0d};
    private static final double[][] STATIC_A = {new double[]{0.2222222222222222d}, new double[]{0.08333333333333333d, 0.25d}, new double[]{0.125d, 0.0d, 0.375d}, new double[]{0.182d, -0.27d, 0.624d, 0.064d}, new double[]{-0.55d, 1.35d, 2.4d, -7.2d, 5.0d}, new double[]{0.08333333333333333d, 0.0d, 0.84375d, -1.3333333333333333d, 1.3020833333333333d, 0.10416666666666667d}};
    private static final double[] STATIC_B = {0.08333333333333333d, 0.0d, 0.84375d, -1.3333333333333333d, 1.3020833333333333d, 0.10416666666666667d, 0.0d};
    private static final double[] STATIC_E = {-0.05d, 0.0d, 0.50625d, -1.2d, 0.78125d, 0.0625d, -0.1d};

    public HighamHall54Integrator(double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(METHOD_NAME, false, STATIC_C, STATIC_A, STATIC_B, (org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator) new org.apache.commons.math.ode.nonstiff.HighamHall54StepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public HighamHall54Integrator(double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(METHOD_NAME, false, STATIC_C, STATIC_A, STATIC_B, (org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator) new org.apache.commons.math.ode.nonstiff.HighamHall54StepInterpolator(), minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    @Override // org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator
    public int getOrder() {
        return 5;
    }

    @Override // org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        double d;
        double d2;
        double error = 0.0d;
        for (int j = 0; j < this.mainSetDimension; j++) {
            double errSum = STATIC_E[0] * yDotK[0][j];
            for (int l = 1; l < STATIC_E.length; l++) {
                errSum += STATIC_E[l] * yDotK[l][j];
            }
            double yScale = org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(y0[j]), org.apache.commons.math.util.FastMath.abs(y1[j]));
            if (this.vecAbsoluteTolerance == null) {
                d = this.scalAbsoluteTolerance;
                d2 = this.scalRelativeTolerance;
            } else {
                d = this.vecAbsoluteTolerance[j];
                d2 = this.vecRelativeTolerance[j];
            }
            double tol = d + (d2 * yScale);
            double ratio = (h * errSum) / tol;
            error += ratio * ratio;
        }
        int j2 = this.mainSetDimension;
        return org.apache.commons.math.util.FastMath.sqrt(error / ((double) j2));
    }
}
