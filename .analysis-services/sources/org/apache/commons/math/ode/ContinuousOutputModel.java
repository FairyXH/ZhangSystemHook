package org.apache.commons.math.ode;

/* JADX INFO: loaded from: classes4.dex */
public class ContinuousOutputModel implements org.apache.commons.math.ode.sampling.StepHandler, java.io.Serializable {
    private static final long serialVersionUID = -1417964919405031606L;
    private double finalTime;
    private boolean forward;
    private int index;
    private double initialTime;
    private java.util.List<org.apache.commons.math.ode.sampling.StepInterpolator> steps = new java.util.ArrayList();

    public ContinuousOutputModel() {
        reset();
    }

    public void append(org.apache.commons.math.ode.ContinuousOutputModel model) throws org.apache.commons.math.ode.DerivativeException {
        if (model.steps.size() == 0) {
            return;
        }
        if (this.steps.size() == 0) {
            this.initialTime = model.initialTime;
            this.forward = model.forward;
        } else {
            if (getInterpolatedState().length != model.getInterpolatedState().length) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(getInterpolatedState().length), java.lang.Integer.valueOf(model.getInterpolatedState().length));
            }
            if (!(this.forward ^ model.forward)) {
                org.apache.commons.math.ode.sampling.StepInterpolator lastInterpolator = this.steps.get(this.index);
                double current = lastInterpolator.getCurrentTime();
                double previous = lastInterpolator.getPreviousTime();
                double step = current - previous;
                double gap = model.getInitialTime() - current;
                if (org.apache.commons.math.util.FastMath.abs(gap) > org.apache.commons.math.util.FastMath.abs(step) * 0.001d) {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.HOLE_BETWEEN_MODELS_TIME_RANGES, java.lang.Double.valueOf(org.apache.commons.math.util.FastMath.abs(gap)));
                }
            } else {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.PROPAGATION_DIRECTION_MISMATCH, new java.lang.Object[0]);
            }
        }
        for (org.apache.commons.math.ode.sampling.StepInterpolator interpolator : model.steps) {
            this.steps.add(interpolator.copy());
        }
        this.index = this.steps.size() - 1;
        this.finalTime = this.steps.get(this.index).getCurrentTime();
    }

    @Override // org.apache.commons.math.ode.sampling.StepHandler
    public boolean requiresDenseOutput() {
        return true;
    }

    @Override // org.apache.commons.math.ode.sampling.StepHandler
    public void reset() {
        this.initialTime = Double.NaN;
        this.finalTime = Double.NaN;
        this.forward = true;
        this.index = 0;
        this.steps.clear();
    }

    @Override // org.apache.commons.math.ode.sampling.StepHandler
    public void handleStep(org.apache.commons.math.ode.sampling.StepInterpolator interpolator, boolean isLast) throws org.apache.commons.math.ode.DerivativeException {
        if (this.steps.size() == 0) {
            this.initialTime = interpolator.getPreviousTime();
            this.forward = interpolator.isForward();
        }
        this.steps.add(interpolator.copy());
        if (isLast) {
            this.finalTime = interpolator.getCurrentTime();
            this.index = this.steps.size() - 1;
        }
    }

    public double getInitialTime() {
        return this.initialTime;
    }

    public double getFinalTime() {
        return this.finalTime;
    }

    public double getInterpolatedTime() {
        return this.steps.get(this.index).getInterpolatedTime();
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0112  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void setInterpolatedTime(double r38) {
        /*
            Method dump skipped, instruction units count: 335
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math.ode.ContinuousOutputModel.setInterpolatedTime(double):void");
    }

    public double[] getInterpolatedState() throws org.apache.commons.math.ode.DerivativeException {
        return this.steps.get(this.index).getInterpolatedState();
    }

    private int locatePoint(double time, org.apache.commons.math.ode.sampling.StepInterpolator interval) {
        if (this.forward) {
            if (time < interval.getPreviousTime()) {
                return -1;
            }
            return time > interval.getCurrentTime() ? 1 : 0;
        }
        if (time > interval.getPreviousTime()) {
            return -1;
        }
        return time < interval.getCurrentTime() ? 1 : 0;
    }
}
