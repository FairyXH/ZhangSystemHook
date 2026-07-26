package org.apache.commons.math.ode.events;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public class CombinedEventsManager {
    private final java.util.List<org.apache.commons.math.ode.events.EventState> states = new java.util.ArrayList();
    private org.apache.commons.math.ode.events.EventState first = null;
    private boolean initialized = false;

    public void addEventHandler(org.apache.commons.math.ode.events.EventHandler handler, double maxCheckInterval, double convergence, int maxIterationCount) {
        this.states.add(new org.apache.commons.math.ode.events.EventState(handler, maxCheckInterval, convergence, maxIterationCount));
    }

    public java.util.Collection<org.apache.commons.math.ode.events.EventHandler> getEventsHandlers() {
        java.util.List<org.apache.commons.math.ode.events.EventHandler> list = new java.util.ArrayList<>();
        for (org.apache.commons.math.ode.events.EventState state : this.states) {
            list.add(state.getEventHandler());
        }
        return java.util.Collections.unmodifiableCollection(list);
    }

    public void clearEventsHandlers() {
        this.states.clear();
    }

    public java.util.Collection<org.apache.commons.math.ode.events.EventState> getEventsStates() {
        return this.states;
    }

    public boolean isEmpty() {
        return this.states.isEmpty();
    }

    public boolean evaluateStep(org.apache.commons.math.ode.sampling.StepInterpolator interpolator) throws org.apache.commons.math.ode.DerivativeException, org.apache.commons.math.ode.IntegratorException {
        try {
            this.first = null;
            if (this.states.isEmpty()) {
                return false;
            }
            if (!this.initialized) {
                java.util.Iterator<org.apache.commons.math.ode.events.EventState> it = this.states.iterator();
                while (it.hasNext()) {
                    it.next().reinitializeBegin(interpolator);
                }
                this.initialized = true;
            }
            for (org.apache.commons.math.ode.events.EventState state : this.states) {
                if (state.evaluateStep(interpolator)) {
                    if (this.first == null) {
                        this.first = state;
                    } else if (interpolator.isForward()) {
                        if (state.getEventTime() < this.first.getEventTime()) {
                            this.first = state;
                        }
                    } else if (state.getEventTime() > this.first.getEventTime()) {
                        this.first = state;
                    }
                }
            }
            return this.first != null;
        } catch (org.apache.commons.math.ConvergenceException ce) {
            throw new org.apache.commons.math.ode.IntegratorException(ce);
        } catch (org.apache.commons.math.ode.events.EventException se) {
            java.lang.Throwable cause = se.getCause();
            if (cause != null && (cause instanceof org.apache.commons.math.ode.DerivativeException)) {
                throw ((org.apache.commons.math.ode.DerivativeException) cause);
            }
            throw new org.apache.commons.math.ode.IntegratorException(se);
        }
    }

    public double getEventTime() {
        if (this.first == null) {
            return Double.NaN;
        }
        return this.first.getEventTime();
    }

    public void stepAccepted(double t, double[] y) throws org.apache.commons.math.ode.IntegratorException {
        try {
            for (org.apache.commons.math.ode.events.EventState state : this.states) {
                state.stepAccepted(t, y);
            }
        } catch (org.apache.commons.math.ode.events.EventException se) {
            throw new org.apache.commons.math.ode.IntegratorException(se);
        }
    }

    public boolean stop() {
        for (org.apache.commons.math.ode.events.EventState state : this.states) {
            if (state.stop()) {
                return true;
            }
        }
        return false;
    }

    public boolean reset(double t, double[] y) throws org.apache.commons.math.ode.IntegratorException {
        boolean resetDerivatives = false;
        try {
            for (org.apache.commons.math.ode.events.EventState state : this.states) {
                if (state.reset(t, y)) {
                    resetDerivatives = true;
                }
            }
            return resetDerivatives;
        } catch (org.apache.commons.math.ode.events.EventException se) {
            throw new org.apache.commons.math.ode.IntegratorException(se);
        }
    }
}
