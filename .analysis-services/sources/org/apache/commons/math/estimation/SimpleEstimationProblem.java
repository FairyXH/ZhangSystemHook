package org.apache.commons.math.estimation;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public class SimpleEstimationProblem implements org.apache.commons.math.estimation.EstimationProblem {
    private final java.util.List<org.apache.commons.math.estimation.EstimatedParameter> parameters = new java.util.ArrayList();
    private final java.util.List<org.apache.commons.math.estimation.WeightedMeasurement> measurements = new java.util.ArrayList();

    @Override // org.apache.commons.math.estimation.EstimationProblem
    public org.apache.commons.math.estimation.EstimatedParameter[] getAllParameters() {
        return (org.apache.commons.math.estimation.EstimatedParameter[]) this.parameters.toArray(new org.apache.commons.math.estimation.EstimatedParameter[this.parameters.size()]);
    }

    @Override // org.apache.commons.math.estimation.EstimationProblem
    public org.apache.commons.math.estimation.EstimatedParameter[] getUnboundParameters() {
        java.util.List<org.apache.commons.math.estimation.EstimatedParameter> unbound = new java.util.ArrayList<>(this.parameters.size());
        for (org.apache.commons.math.estimation.EstimatedParameter p : this.parameters) {
            if (!p.isBound()) {
                unbound.add(p);
            }
        }
        return (org.apache.commons.math.estimation.EstimatedParameter[]) unbound.toArray(new org.apache.commons.math.estimation.EstimatedParameter[unbound.size()]);
    }

    @Override // org.apache.commons.math.estimation.EstimationProblem
    public org.apache.commons.math.estimation.WeightedMeasurement[] getMeasurements() {
        return (org.apache.commons.math.estimation.WeightedMeasurement[]) this.measurements.toArray(new org.apache.commons.math.estimation.WeightedMeasurement[this.measurements.size()]);
    }

    protected void addParameter(org.apache.commons.math.estimation.EstimatedParameter p) {
        this.parameters.add(p);
    }

    protected void addMeasurement(org.apache.commons.math.estimation.WeightedMeasurement m) {
        this.measurements.add(m);
    }
}
