package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
public class DormandPrince54Integrator extends org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator {
    private static final double E1 = 0.0012326388888888888d;
    private static final double E3 = -0.0042527702905061394d;
    private static final double E4 = 0.03697916666666667d;
    private static final double E5 = -0.05086379716981132d;
    private static final double E6 = 0.0419047619047619d;
    private static final double E7 = -0.025d;
    private static final java.lang.String METHOD_NAME = "Dormand-Prince 5(4)";
    private static final double[] STATIC_C = {0.2d, 0.3d, 0.8d, 0.8888888888888888d, 1.0d, 1.0d};
    private static final double[][] STATIC_A = {new double[]{0.2d}, new double[]{0.075d, 0.225d}, new double[]{0.9777777777777777d, -3.7333333333333334d, 3.5555555555555554d}, new double[]{2.9525986892242035d, -11.595793324188385d, 9.822892851699436d, -0.2908093278463649d}, new double[]{2.8462752525252526d, -10.757575757575758d, 8.906422717743473d, 0.2784090909090909d, -0.2735313036020583d}, new double[]{0.09114583333333333d, 0.0d, 0.44923629829290207d, 0.6510416666666666d, -0.322376179245283d, 0.13095238095238096d}};
    private static final double[] STATIC_B = {0.09114583333333333d, 0.0d, 0.44923629829290207d, 0.6510416666666666d, -0.322376179245283d, 0.13095238095238096d, 0.0d};

    public DormandPrince54Integrator(double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(METHOD_NAME, true, STATIC_C, STATIC_A, STATIC_B, (org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator) new org.apache.commons.math.ode.nonstiff.DormandPrince54StepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public DormandPrince54Integrator(double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(METHOD_NAME, true, STATIC_C, STATIC_A, STATIC_B, (org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator) new org.apache.commons.math.ode.nonstiff.DormandPrince54StepInterpolator(), minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
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
            double errSum = (yDotK[0][j] * E1) + (yDotK[2][j] * E3) + (yDotK[3][j] * E4) + (yDotK[4][j] * E5) + (yDotK[5][j] * E6) + (yDotK[6][j] * E7);
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
