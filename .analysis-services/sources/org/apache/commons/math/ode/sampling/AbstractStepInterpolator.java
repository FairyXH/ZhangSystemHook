package org.apache.commons.math.ode.sampling;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractStepInterpolator implements org.apache.commons.math.ode.sampling.StepInterpolator {
    protected double[] currentState;
    private boolean dirtyState;
    private boolean finalized;
    private boolean forward;
    private double globalCurrentTime;
    private double globalPreviousTime;
    protected double h;
    protected double[] interpolatedDerivatives;
    protected double[] interpolatedState;
    protected double interpolatedTime;
    private double softCurrentTime;
    private double softPreviousTime;

    protected abstract void computeInterpolatedStateAndDerivatives(double d, double d2) throws org.apache.commons.math.ode.DerivativeException;

    protected abstract org.apache.commons.math.ode.sampling.StepInterpolator doCopy();

    @Override // java.io.Externalizable
    public abstract void readExternal(java.io.ObjectInput objectInput) throws java.io.IOException, java.lang.ClassNotFoundException;

    @Override // java.io.Externalizable
    public abstract void writeExternal(java.io.ObjectOutput objectOutput) throws java.io.IOException;

    protected AbstractStepInterpolator() {
        this.globalPreviousTime = Double.NaN;
        this.globalCurrentTime = Double.NaN;
        this.softPreviousTime = Double.NaN;
        this.softCurrentTime = Double.NaN;
        this.h = Double.NaN;
        this.interpolatedTime = Double.NaN;
        this.currentState = null;
        this.interpolatedState = null;
        this.interpolatedDerivatives = null;
        this.finalized = false;
        this.forward = true;
        this.dirtyState = true;
    }

    protected AbstractStepInterpolator(double[] y, boolean forward) {
        this.globalPreviousTime = Double.NaN;
        this.globalCurrentTime = Double.NaN;
        this.softPreviousTime = Double.NaN;
        this.softCurrentTime = Double.NaN;
        this.h = Double.NaN;
        this.interpolatedTime = Double.NaN;
        this.currentState = y;
        this.interpolatedState = new double[y.length];
        this.interpolatedDerivatives = new double[y.length];
        this.finalized = false;
        this.forward = forward;
        this.dirtyState = true;
    }

    protected AbstractStepInterpolator(org.apache.commons.math.ode.sampling.AbstractStepInterpolator interpolator) {
        this.globalPreviousTime = interpolator.globalPreviousTime;
        this.globalCurrentTime = interpolator.globalCurrentTime;
        this.softPreviousTime = interpolator.softPreviousTime;
        this.softCurrentTime = interpolator.softCurrentTime;
        this.h = interpolator.h;
        this.interpolatedTime = interpolator.interpolatedTime;
        if (interpolator.currentState != null) {
            this.currentState = (double[]) interpolator.currentState.clone();
            this.interpolatedState = (double[]) interpolator.interpolatedState.clone();
            this.interpolatedDerivatives = (double[]) interpolator.interpolatedDerivatives.clone();
        } else {
            this.currentState = null;
            this.interpolatedState = null;
            this.interpolatedDerivatives = null;
        }
        this.finalized = interpolator.finalized;
        this.forward = interpolator.forward;
        this.dirtyState = interpolator.dirtyState;
    }

    protected void reinitialize(double[] y, boolean isForward) {
        this.globalPreviousTime = Double.NaN;
        this.globalCurrentTime = Double.NaN;
        this.softPreviousTime = Double.NaN;
        this.softCurrentTime = Double.NaN;
        this.h = Double.NaN;
        this.interpolatedTime = Double.NaN;
        this.currentState = y;
        this.interpolatedState = new double[y.length];
        this.interpolatedDerivatives = new double[y.length];
        this.finalized = false;
        this.forward = isForward;
        this.dirtyState = true;
    }

    @Override // org.apache.commons.math.ode.sampling.StepInterpolator
    public org.apache.commons.math.ode.sampling.StepInterpolator copy() throws org.apache.commons.math.ode.DerivativeException {
        finalizeStep();
        return doCopy();
    }

    public void shift() {
        this.globalPreviousTime = this.globalCurrentTime;
        this.softPreviousTime = this.globalPreviousTime;
        this.softCurrentTime = this.globalCurrentTime;
    }

    public void storeTime(double t) {
        this.globalCurrentTime = t;
        this.softCurrentTime = this.globalCurrentTime;
        this.h = this.globalCurrentTime - this.globalPreviousTime;
        setInterpolatedTime(t);
        this.finalized = false;
    }

    public void setSoftPreviousTime(double softPreviousTime) {
        this.softPreviousTime = softPreviousTime;
    }

    public void setSoftCurrentTime(double softCurrentTime) {
        this.softCurrentTime = softCurrentTime;
    }

    public double getGlobalPreviousTime() {
        return this.globalPreviousTime;
    }

    public double getGlobalCurrentTime() {
        return this.globalCurrentTime;
    }

    @Override // org.apache.commons.math.ode.sampling.StepInterpolator
    public double getPreviousTime() {
        return this.softPreviousTime;
    }

    @Override // org.apache.commons.math.ode.sampling.StepInterpolator
    public double getCurrentTime() {
        return this.softCurrentTime;
    }

    @Override // org.apache.commons.math.ode.sampling.StepInterpolator
    public double getInterpolatedTime() {
        return this.interpolatedTime;
    }

    @Override // org.apache.commons.math.ode.sampling.StepInterpolator
    public void setInterpolatedTime(double time) {
        this.interpolatedTime = time;
        this.dirtyState = true;
    }

    @Override // org.apache.commons.math.ode.sampling.StepInterpolator
    public boolean isForward() {
        return this.forward;
    }

    @Override // org.apache.commons.math.ode.sampling.StepInterpolator
    public double[] getInterpolatedState() throws org.apache.commons.math.ode.DerivativeException {
        if (this.dirtyState) {
            double oneMinusThetaH = this.globalCurrentTime - this.interpolatedTime;
            double theta = this.h != 0.0d ? (this.h - oneMinusThetaH) / this.h : 0.0d;
            computeInterpolatedStateAndDerivatives(theta, oneMinusThetaH);
            this.dirtyState = false;
        }
        return this.interpolatedState;
    }

    @Override // org.apache.commons.math.ode.sampling.StepInterpolator
    public double[] getInterpolatedDerivatives() throws org.apache.commons.math.ode.DerivativeException {
        if (this.dirtyState) {
            double oneMinusThetaH = this.globalCurrentTime - this.interpolatedTime;
            double theta = this.h != 0.0d ? (this.h - oneMinusThetaH) / this.h : 0.0d;
            computeInterpolatedStateAndDerivatives(theta, oneMinusThetaH);
            this.dirtyState = false;
        }
        return this.interpolatedDerivatives;
    }

    public final void finalizeStep() throws org.apache.commons.math.ode.DerivativeException {
        if (!this.finalized) {
            doFinalize();
            this.finalized = true;
        }
    }

    protected void doFinalize() throws org.apache.commons.math.ode.DerivativeException {
    }

    protected void writeBaseExternal(java.io.ObjectOutput out) throws java.io.IOException {
        if (this.currentState == null) {
            out.writeInt(-1);
        } else {
            out.writeInt(this.currentState.length);
        }
        out.writeDouble(this.globalPreviousTime);
        out.writeDouble(this.globalCurrentTime);
        out.writeDouble(this.softPreviousTime);
        out.writeDouble(this.softCurrentTime);
        out.writeDouble(this.h);
        out.writeBoolean(this.forward);
        if (this.currentState != null) {
            for (int i = 0; i < this.currentState.length; i++) {
                out.writeDouble(this.currentState[i]);
            }
        }
        out.writeDouble(this.interpolatedTime);
        try {
            finalizeStep();
        } catch (org.apache.commons.math.ode.DerivativeException e) {
            java.io.IOException ioe = new java.io.IOException(e.getLocalizedMessage());
            ioe.initCause(e);
            throw ioe;
        }
    }

    protected double readBaseExternal(java.io.ObjectInput in) throws java.io.IOException {
        int dimension = in.readInt();
        this.globalPreviousTime = in.readDouble();
        this.globalCurrentTime = in.readDouble();
        this.softPreviousTime = in.readDouble();
        this.softCurrentTime = in.readDouble();
        this.h = in.readDouble();
        this.forward = in.readBoolean();
        this.dirtyState = true;
        if (dimension < 0) {
            this.currentState = null;
        } else {
            this.currentState = new double[dimension];
            for (int i = 0; i < this.currentState.length; i++) {
                this.currentState[i] = in.readDouble();
            }
        }
        this.interpolatedTime = Double.NaN;
        this.interpolatedState = dimension < 0 ? null : new double[dimension];
        this.interpolatedDerivatives = dimension >= 0 ? new double[dimension] : null;
        this.finalized = true;
        return in.readDouble();
    }
}
