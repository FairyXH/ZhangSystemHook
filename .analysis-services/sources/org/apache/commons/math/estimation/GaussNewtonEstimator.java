package org.apache.commons.math.estimation;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public class GaussNewtonEstimator extends org.apache.commons.math.estimation.AbstractEstimator implements java.io.Serializable {
    private static final double DEFAULT_CONVERGENCE = 1.0E-6d;
    private static final double DEFAULT_STEADY_STATE_THRESHOLD = 1.0E-6d;
    private static final long serialVersionUID = 5485001826076289109L;
    private double convergence;
    private double steadyStateThreshold;

    public GaussNewtonEstimator() {
        this.steadyStateThreshold = 1.0E-6d;
        this.convergence = 1.0E-6d;
    }

    public GaussNewtonEstimator(int maxCostEval, double convergence, double steadyStateThreshold) {
        setMaxCostEval(maxCostEval);
        this.steadyStateThreshold = steadyStateThreshold;
        this.convergence = convergence;
    }

    public void setConvergence(double convergence) {
        this.convergence = convergence;
    }

    public void setSteadyStateThreshold(double steadyStateThreshold) {
        this.steadyStateThreshold = steadyStateThreshold;
    }

    @Override // org.apache.commons.math.estimation.AbstractEstimator, org.apache.commons.math.estimation.Estimator
    public void estimate(org.apache.commons.math.estimation.EstimationProblem problem) throws org.apache.commons.math.estimation.EstimationException {
        double[] bDecrementData;
        double previous;
        double[] bDecrementData2;
        initializeEstimate(problem);
        double[] grad = new double[this.parameters.length];
        org.apache.commons.math.linear.ArrayRealVector bDecrement = new org.apache.commons.math.linear.ArrayRealVector(this.parameters.length);
        double[] bDecrementData3 = bDecrement.getDataRef();
        org.apache.commons.math.linear.RealMatrix wGradGradT = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(this.parameters.length, this.parameters.length);
        double previous2 = Double.POSITIVE_INFINITY;
        while (true) {
            incrementJacobianEvaluationsCounter();
            org.apache.commons.math.linear.RealVector b = new org.apache.commons.math.linear.ArrayRealVector(this.parameters.length);
            org.apache.commons.math.linear.RealMatrix a = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(this.parameters.length, this.parameters.length);
            org.apache.commons.math.linear.RealVector b2 = b;
            int i = 0;
            org.apache.commons.math.linear.RealMatrix a2 = a;
            while (i < this.measurements.length) {
                if (this.measurements[i].isIgnored()) {
                    bDecrementData = bDecrementData3;
                    previous = previous2;
                } else {
                    double weight = this.measurements[i].getWeight();
                    double residual = this.measurements[i].getResidual();
                    int j = 0;
                    while (j < this.parameters.length) {
                        grad[j] = this.measurements[i].getPartial(this.parameters[j]);
                        bDecrementData3[j] = weight * residual * grad[j];
                        j++;
                        previous2 = previous2;
                    }
                    previous = previous2;
                    int k = 0;
                    while (k < this.parameters.length) {
                        double gk = grad[k];
                        int l = 0;
                        while (true) {
                            bDecrementData2 = bDecrementData3;
                            if (l < this.parameters.length) {
                                wGradGradT.setEntry(k, l, weight * gk * grad[l]);
                                l++;
                                bDecrementData3 = bDecrementData2;
                                weight = weight;
                            }
                        }
                        k++;
                        bDecrementData3 = bDecrementData2;
                    }
                    bDecrementData = bDecrementData3;
                    a2 = a2.add(wGradGradT);
                    b2 = b2.add(bDecrement);
                }
                i++;
                previous2 = previous;
                bDecrementData3 = bDecrementData;
            }
            double[] bDecrementData4 = bDecrementData3;
            try {
                org.apache.commons.math.linear.RealVector dX = new org.apache.commons.math.linear.LUDecompositionImpl(a2).getSolver().solve(b2);
                for (int i2 = 0; i2 < this.parameters.length; i2++) {
                    this.parameters[i2].setEstimate(this.parameters[i2].getEstimate() + dX.getEntry(i2));
                }
                previous2 = this.cost;
                updateResidualsAndCost();
                if (getCostEvaluations() < 2 || (org.apache.commons.math.util.FastMath.abs(previous2 - this.cost) > this.cost * this.steadyStateThreshold && org.apache.commons.math.util.FastMath.abs(this.cost) > this.convergence)) {
                    bDecrementData3 = bDecrementData4;
                } else {
                    return;
                }
            } catch (org.apache.commons.math.linear.InvalidMatrixException e) {
                throw new org.apache.commons.math.estimation.EstimationException(org.apache.commons.math.exception.util.LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM, new java.lang.Object[0]);
            }
        }
    }
}
