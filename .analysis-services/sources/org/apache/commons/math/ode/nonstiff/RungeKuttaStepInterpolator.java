package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
abstract class RungeKuttaStepInterpolator extends org.apache.commons.math.ode.sampling.AbstractStepInterpolator {
    protected org.apache.commons.math.ode.AbstractIntegrator integrator;
    protected double[][] yDotK;

    protected RungeKuttaStepInterpolator() {
        this.yDotK = null;
        this.integrator = null;
    }

    public RungeKuttaStepInterpolator(org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator interpolator) {
        super(interpolator);
        if (interpolator.currentState != null) {
            int dimension = this.currentState.length;
            this.yDotK = new double[interpolator.yDotK.length][];
            for (int k = 0; k < interpolator.yDotK.length; k++) {
                this.yDotK[k] = new double[dimension];
                java.lang.System.arraycopy(interpolator.yDotK[k], 0, this.yDotK[k], 0, dimension);
            }
        } else {
            this.yDotK = null;
        }
        this.integrator = null;
    }

    public void reinitialize(org.apache.commons.math.ode.AbstractIntegrator rkIntegrator, double[] y, double[][] yDotArray, boolean forward) {
        reinitialize(y, forward);
        this.yDotK = yDotArray;
        this.integrator = rkIntegrator;
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void writeExternal(java.io.ObjectOutput out) throws java.io.IOException {
        writeBaseExternal(out);
        int n = this.currentState == null ? -1 : this.currentState.length;
        int kMax = this.yDotK != null ? this.yDotK.length : -1;
        out.writeInt(kMax);
        for (int k = 0; k < kMax; k++) {
            for (int i = 0; i < n; i++) {
                out.writeDouble(this.yDotK[k][i]);
            }
        }
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void readExternal(java.io.ObjectInput in) throws java.io.IOException {
        double t = readBaseExternal(in);
        int n = this.currentState == null ? -1 : this.currentState.length;
        int kMax = in.readInt();
        this.yDotK = kMax < 0 ? null : new double[kMax][];
        for (int k = 0; k < kMax; k++) {
            this.yDotK[k] = n < 0 ? null : new double[n];
            for (int i = 0; i < n; i++) {
                this.yDotK[k][i] = in.readDouble();
            }
        }
        this.integrator = null;
        if (this.currentState != null) {
            setInterpolatedTime(t);
        } else {
            this.interpolatedTime = t;
        }
    }
}
