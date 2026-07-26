package org.apache.commons.math.ode.events;

/* JADX INFO: loaded from: classes4.dex */
public class EventState {
    private final double convergence;
    private boolean forward;
    private final org.apache.commons.math.ode.events.EventHandler handler;
    private final double maxCheckInterval;
    private final int maxIterationCount;
    private double t0 = Double.NaN;
    private double g0 = Double.NaN;
    private boolean g0Positive = true;
    private boolean pendingEvent = false;
    private double pendingEventTime = Double.NaN;
    private double previousEventTime = Double.NaN;
    private boolean increasing = true;
    private int nextAction = 3;

    public EventState(org.apache.commons.math.ode.events.EventHandler handler, double maxCheckInterval, double convergence, int maxIterationCount) {
        this.handler = handler;
        this.maxCheckInterval = maxCheckInterval;
        this.convergence = org.apache.commons.math.util.FastMath.abs(convergence);
        this.maxIterationCount = maxIterationCount;
    }

    public org.apache.commons.math.ode.events.EventHandler getEventHandler() {
        return this.handler;
    }

    public double getMaxCheckInterval() {
        return this.maxCheckInterval;
    }

    public double getConvergence() {
        return this.convergence;
    }

    public int getMaxIterationCount() {
        return this.maxIterationCount;
    }

    public void reinitializeBegin(org.apache.commons.math.ode.sampling.StepInterpolator interpolator) throws org.apache.commons.math.ode.events.EventException {
        try {
            double ignoreZone = interpolator.isForward() ? getConvergence() : -getConvergence();
            this.t0 = interpolator.getPreviousTime() + ignoreZone;
            interpolator.setInterpolatedTime(this.t0);
            this.g0 = this.handler.g(this.t0, interpolator.getInterpolatedState());
            boolean z = true;
            if (this.g0 == 0.0d) {
                double tStart = interpolator.getPreviousTime();
                interpolator.setInterpolatedTime(tStart);
                if (this.handler.g(tStart, interpolator.getInterpolatedState()) > 0.0d) {
                    z = false;
                }
                this.g0Positive = z;
                return;
            }
            if (this.g0 < 0.0d) {
                z = false;
            }
            this.g0Positive = z;
        } catch (org.apache.commons.math.ode.DerivativeException mue) {
            throw new org.apache.commons.math.ode.events.EventException(mue);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x0117 A[Catch: EmbeddedEventException -> 0x019d, EmbeddedDerivativeException -> 0x01a3, TryCatch #5 {EmbeddedDerivativeException -> 0x01a3, EmbeddedEventException -> 0x019d, blocks: (B:3:0x0004, B:6:0x001e, B:8:0x0022, B:10:0x002d, B:12:0x004d, B:16:0x0071, B:22:0x007c, B:24:0x0096, B:28:0x009f, B:35:0x00b2, B:39:0x00c3, B:40:0x00c8, B:41:0x00c9, B:49:0x00e4, B:55:0x010f, B:57:0x0117, B:59:0x0127, B:78:0x017c, B:64:0x0143, B:66:0x014b, B:71:0x015f, B:53:0x00fb, B:75:0x016b, B:76:0x0170, B:44:0x00d4, B:45:0x00d9, B:79:0x018b, B:9:0x0028), top: B:93:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x013d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean evaluateStep(org.apache.commons.math.ode.sampling.StepInterpolator r34) throws org.apache.commons.math.ode.DerivativeException, org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ode.events.EventException, org.apache.commons.math.ConvergenceException {
        /*
            Method dump skipped, instruction units count: 425
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math.ode.events.EventState.evaluateStep(org.apache.commons.math.ode.sampling.StepInterpolator):boolean");
    }

    public double getEventTime() {
        if (this.pendingEvent) {
            return this.pendingEventTime;
        }
        return Double.POSITIVE_INFINITY;
    }

    public void stepAccepted(double t, double[] y) throws org.apache.commons.math.ode.events.EventException {
        this.t0 = t;
        this.g0 = this.handler.g(t, y);
        if (this.pendingEvent && org.apache.commons.math.util.FastMath.abs(this.pendingEventTime - t) <= this.convergence) {
            this.previousEventTime = t;
            this.g0Positive = this.increasing;
            this.nextAction = this.handler.eventOccurred(t, y, true ^ (this.increasing ^ this.forward));
        } else {
            this.g0Positive = this.g0 >= 0.0d;
            this.nextAction = 3;
        }
    }

    public boolean stop() {
        return this.nextAction == 0;
    }

    public boolean reset(double t, double[] y) throws org.apache.commons.math.ode.events.EventException {
        if (!this.pendingEvent || org.apache.commons.math.util.FastMath.abs(this.pendingEventTime - t) > this.convergence) {
            return false;
        }
        if (this.nextAction == 1) {
            this.handler.resetState(t, y);
        }
        this.pendingEvent = false;
        this.pendingEventTime = Double.NaN;
        return this.nextAction == 1 || this.nextAction == 2;
    }

    private static class EmbeddedDerivativeException extends java.lang.RuntimeException {
        private static final long serialVersionUID = 3574188382434584610L;
        private final org.apache.commons.math.ode.DerivativeException derivativeException;

        public EmbeddedDerivativeException(org.apache.commons.math.ode.DerivativeException derivativeException) {
            this.derivativeException = derivativeException;
        }

        public org.apache.commons.math.ode.DerivativeException getDerivativeException() {
            return this.derivativeException;
        }
    }

    private static class EmbeddedEventException extends java.lang.RuntimeException {
        private static final long serialVersionUID = -1337749250090455474L;
        private final org.apache.commons.math.ode.events.EventException eventException;

        public EmbeddedEventException(org.apache.commons.math.ode.events.EventException eventException) {
            this.eventException = eventException;
        }

        public org.apache.commons.math.ode.events.EventException getEventException() {
            return this.eventException;
        }
    }
}
