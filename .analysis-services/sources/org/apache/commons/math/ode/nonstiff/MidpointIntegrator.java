package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
public class MidpointIntegrator extends org.apache.commons.math.ode.nonstiff.RungeKuttaIntegrator {
    private static final double[] STATIC_C = {0.5d};
    private static final double[][] STATIC_A = {new double[]{0.5d}};
    private static final double[] STATIC_B = {0.0d, 1.0d};

    public MidpointIntegrator(double step) {
        super("midpoint", STATIC_C, STATIC_A, STATIC_B, new org.apache.commons.math.ode.nonstiff.MidpointStepInterpolator(), step);
    }
}
