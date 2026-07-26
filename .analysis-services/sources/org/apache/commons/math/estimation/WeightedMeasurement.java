package org.apache.commons.math.estimation;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public abstract class WeightedMeasurement implements java.io.Serializable {
    private static final long serialVersionUID = 4360046376796901941L;
    private boolean ignored;
    private final double measuredValue;
    private final double weight;

    public abstract double getPartial(org.apache.commons.math.estimation.EstimatedParameter estimatedParameter);

    public abstract double getTheoreticalValue();

    public WeightedMeasurement(double weight, double measuredValue) {
        this.weight = weight;
        this.measuredValue = measuredValue;
        this.ignored = false;
    }

    public WeightedMeasurement(double weight, double measuredValue, boolean ignored) {
        this.weight = weight;
        this.measuredValue = measuredValue;
        this.ignored = ignored;
    }

    public double getWeight() {
        return this.weight;
    }

    public double getMeasuredValue() {
        return this.measuredValue;
    }

    public double getResidual() {
        return this.measuredValue - getTheoreticalValue();
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public boolean isIgnored() {
        return this.ignored;
    }
}
