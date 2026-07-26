package org.apache.commons.math.ode;

/* JADX INFO: loaded from: classes4.dex */
public interface ODEIntegrator {
    void addEventHandler(org.apache.commons.math.ode.events.EventHandler eventHandler, double d, double d2, int i);

    void addStepHandler(org.apache.commons.math.ode.sampling.StepHandler stepHandler);

    void clearEventHandlers();

    void clearStepHandlers();

    double getCurrentSignedStepsize();

    double getCurrentStepStart();

    int getEvaluations();

    java.util.Collection<org.apache.commons.math.ode.events.EventHandler> getEventHandlers();

    int getMaxEvaluations();

    java.lang.String getName();

    java.util.Collection<org.apache.commons.math.ode.sampling.StepHandler> getStepHandlers();

    void setMaxEvaluations(int i);
}
