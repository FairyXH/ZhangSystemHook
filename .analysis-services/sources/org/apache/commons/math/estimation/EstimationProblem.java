package org.apache.commons.math.estimation;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public interface EstimationProblem {
    org.apache.commons.math.estimation.EstimatedParameter[] getAllParameters();

    org.apache.commons.math.estimation.WeightedMeasurement[] getMeasurements();

    org.apache.commons.math.estimation.EstimatedParameter[] getUnboundParameters();
}
