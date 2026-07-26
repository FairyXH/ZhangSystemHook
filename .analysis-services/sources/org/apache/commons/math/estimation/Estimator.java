package org.apache.commons.math.estimation;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public interface Estimator {
    void estimate(org.apache.commons.math.estimation.EstimationProblem estimationProblem) throws org.apache.commons.math.estimation.EstimationException;

    double[][] getCovariances(org.apache.commons.math.estimation.EstimationProblem estimationProblem) throws org.apache.commons.math.estimation.EstimationException;

    double getRMS(org.apache.commons.math.estimation.EstimationProblem estimationProblem);

    double[] guessParametersErrors(org.apache.commons.math.estimation.EstimationProblem estimationProblem) throws org.apache.commons.math.estimation.EstimationException;
}
