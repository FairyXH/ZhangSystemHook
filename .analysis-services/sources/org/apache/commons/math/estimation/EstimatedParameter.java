package org.apache.commons.math.estimation;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public class EstimatedParameter implements java.io.Serializable {
    private static final long serialVersionUID = -555440800213416949L;
    private boolean bound;
    protected double estimate;
    private final java.lang.String name;

    public EstimatedParameter(java.lang.String name, double firstEstimate) {
        this.name = name;
        this.estimate = firstEstimate;
        this.bound = false;
    }

    public EstimatedParameter(java.lang.String name, double firstEstimate, boolean bound) {
        this.name = name;
        this.estimate = firstEstimate;
        this.bound = bound;
    }

    public EstimatedParameter(org.apache.commons.math.estimation.EstimatedParameter parameter) {
        this.name = parameter.name;
        this.estimate = parameter.estimate;
        this.bound = parameter.bound;
    }

    public void setEstimate(double estimate) {
        this.estimate = estimate;
    }

    public double getEstimate() {
        return this.estimate;
    }

    public java.lang.String getName() {
        return this.name;
    }

    public void setBound(boolean bound) {
        this.bound = bound;
    }

    public boolean isBound() {
        return this.bound;
    }
}
