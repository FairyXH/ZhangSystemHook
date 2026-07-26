package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
public class GillIntegrator extends org.apache.commons.math.ode.nonstiff.RungeKuttaIntegrator {
    private static final double[] STATIC_C = {0.5d, 0.5d, 1.0d};
    private static final double[][] STATIC_A = {new double[]{0.5d}, new double[]{(org.apache.commons.math.util.FastMath.sqrt(2.0d) - 1.0d) / 2.0d, (2.0d - org.apache.commons.math.util.FastMath.sqrt(2.0d)) / 2.0d}, new double[]{0.0d, (-org.apache.commons.math.util.FastMath.sqrt(2.0d)) / 2.0d, (org.apache.commons.math.util.FastMath.sqrt(2.0d) + 2.0d) / 2.0d}};
    private static final double[] STATIC_B = {0.16666666666666666d, (2.0d - org.apache.commons.math.util.FastMath.sqrt(2.0d)) / 6.0d, (org.apache.commons.math.util.FastMath.sqrt(2.0d) + 2.0d) / 6.0d, 0.16666666666666666d};

    public GillIntegrator(double step) {
        super("Gill", STATIC_C, STATIC_A, STATIC_B, new org.apache.commons.math.ode.nonstiff.GillStepInterpolator(), step);
    }
}
