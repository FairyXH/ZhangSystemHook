package org.apache.commons.math.ode;

/* JADX INFO: loaded from: classes4.dex */
public class FirstOrderConverter implements org.apache.commons.math.ode.FirstOrderDifferentialEquations {
    private final int dimension;
    private final org.apache.commons.math.ode.SecondOrderDifferentialEquations equations;
    private final double[] z;
    private final double[] zDDot;
    private final double[] zDot;

    public FirstOrderConverter(org.apache.commons.math.ode.SecondOrderDifferentialEquations equations) {
        this.equations = equations;
        this.dimension = equations.getDimension();
        this.z = new double[this.dimension];
        this.zDot = new double[this.dimension];
        this.zDDot = new double[this.dimension];
    }

    @Override // org.apache.commons.math.ode.FirstOrderDifferentialEquations
    public int getDimension() {
        return this.dimension * 2;
    }

    @Override // org.apache.commons.math.ode.FirstOrderDifferentialEquations
    public void computeDerivatives(double t, double[] y, double[] yDot) throws org.apache.commons.math.ode.DerivativeException {
        java.lang.System.arraycopy(y, 0, this.z, 0, this.dimension);
        java.lang.System.arraycopy(y, this.dimension, this.zDot, 0, this.dimension);
        this.equations.computeSecondDerivatives(t, this.z, this.zDot, this.zDDot);
        java.lang.System.arraycopy(this.zDot, 0, yDot, 0, this.dimension);
        java.lang.System.arraycopy(this.zDDot, 0, yDot, this.dimension, this.dimension);
    }
}
