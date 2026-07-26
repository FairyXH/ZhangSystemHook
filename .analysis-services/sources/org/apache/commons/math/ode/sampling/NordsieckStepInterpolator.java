package org.apache.commons.math.ode.sampling;

/* JADX INFO: loaded from: classes4.dex */
public class NordsieckStepInterpolator extends org.apache.commons.math.ode.sampling.AbstractStepInterpolator {
    private static final long serialVersionUID = -7179861704951334960L;
    private org.apache.commons.math.linear.Array2DRowRealMatrix nordsieck;
    private double referenceTime;
    private double[] scaled;
    private double scalingH;
    protected double[] stateVariation;

    public NordsieckStepInterpolator() {
    }

    public NordsieckStepInterpolator(org.apache.commons.math.ode.sampling.NordsieckStepInterpolator interpolator) {
        super(interpolator);
        this.scalingH = interpolator.scalingH;
        this.referenceTime = interpolator.referenceTime;
        if (interpolator.scaled != null) {
            this.scaled = (double[]) interpolator.scaled.clone();
        }
        if (interpolator.nordsieck != null) {
            this.nordsieck = new org.apache.commons.math.linear.Array2DRowRealMatrix(interpolator.nordsieck.getDataRef(), true);
        }
        if (interpolator.stateVariation != null) {
            this.stateVariation = (double[]) interpolator.stateVariation.clone();
        }
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected org.apache.commons.math.ode.sampling.StepInterpolator doCopy() {
        return new org.apache.commons.math.ode.sampling.NordsieckStepInterpolator(this);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    public void reinitialize(double[] y, boolean forward) {
        super.reinitialize(y, forward);
        this.stateVariation = new double[y.length];
    }

    public void reinitialize(double time, double stepSize, double[] scaledDerivative, org.apache.commons.math.linear.Array2DRowRealMatrix nordsieckVector) {
        this.referenceTime = time;
        this.scalingH = stepSize;
        this.scaled = scaledDerivative;
        this.nordsieck = nordsieckVector;
        setInterpolatedTime(getInterpolatedTime());
    }

    public void rescale(double stepSize) {
        double ratio = stepSize / this.scalingH;
        for (int i = 0; i < this.scaled.length; i++) {
            double[] dArr = this.scaled;
            dArr[i] = dArr[i] * ratio;
        }
        double[][] nData = this.nordsieck.getDataRef();
        double power = ratio;
        for (double[] nDataI : nData) {
            power *= ratio;
            for (int j = 0; j < nDataI.length; j++) {
                nDataI[j] = nDataI[j] * power;
            }
        }
        this.scalingH = stepSize;
    }

    public double[] getInterpolatedStateVariation() throws org.apache.commons.math.ode.DerivativeException {
        getInterpolatedState();
        return this.stateVariation;
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        double x = this.interpolatedTime - this.referenceTime;
        double normalizedAbscissa = x / this.scalingH;
        java.util.Arrays.fill(this.stateVariation, 0.0d);
        java.util.Arrays.fill(this.interpolatedDerivatives, 0.0d);
        double[][] nData = this.nordsieck.getDataRef();
        for (int i = nData.length - 1; i >= 0; i--) {
            int order = i + 2;
            double[] nDataI = nData[i];
            double power = org.apache.commons.math.util.FastMath.pow(normalizedAbscissa, order);
            int j = 0;
            while (j < nDataI.length) {
                double d = nDataI[j] * power;
                double[] dArr = this.stateVariation;
                dArr[j] = dArr[j] + d;
                double[] dArr2 = this.interpolatedDerivatives;
                dArr2[j] = dArr2[j] + (((double) order) * d);
                j++;
                nDataI = nDataI;
                power = power;
            }
        }
        for (int j2 = 0; j2 < this.currentState.length; j2++) {
            double[] dArr3 = this.stateVariation;
            dArr3[j2] = dArr3[j2] + (this.scaled[j2] * normalizedAbscissa);
            this.interpolatedState[j2] = this.currentState[j2] + this.stateVariation[j2];
            this.interpolatedDerivatives[j2] = (this.interpolatedDerivatives[j2] + (this.scaled[j2] * normalizedAbscissa)) / x;
        }
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void writeExternal(java.io.ObjectOutput out) throws java.io.IOException {
        writeBaseExternal(out);
        out.writeDouble(this.scalingH);
        out.writeDouble(this.referenceTime);
        int n = this.currentState == null ? -1 : this.currentState.length;
        if (this.scaled == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            for (int j = 0; j < n; j++) {
                out.writeDouble(this.scaled[j]);
            }
        }
        if (this.nordsieck == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            out.writeObject(this.nordsieck);
        }
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void readExternal(java.io.ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
        double t = readBaseExternal(in);
        this.scalingH = in.readDouble();
        this.referenceTime = in.readDouble();
        int n = this.currentState == null ? -1 : this.currentState.length;
        boolean hasScaled = in.readBoolean();
        if (hasScaled) {
            this.scaled = new double[n];
            for (int j = 0; j < n; j++) {
                this.scaled[j] = in.readDouble();
            }
        } else {
            this.scaled = null;
        }
        boolean hasNordsieck = in.readBoolean();
        if (hasNordsieck) {
            this.nordsieck = (org.apache.commons.math.linear.Array2DRowRealMatrix) in.readObject();
        } else {
            this.nordsieck = null;
        }
        if (hasScaled && hasNordsieck) {
            this.stateVariation = new double[n];
            setInterpolatedTime(t);
        } else {
            this.stateVariation = null;
        }
    }
}
