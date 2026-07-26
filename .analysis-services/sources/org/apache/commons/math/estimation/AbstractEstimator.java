package org.apache.commons.math.estimation;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public abstract class AbstractEstimator implements org.apache.commons.math.estimation.Estimator {
    public static final int DEFAULT_MAX_COST_EVALUATIONS = 100;
    protected int cols;
    protected double cost;
    private int costEvaluations;
    protected double[] jacobian;
    private int jacobianEvaluations;
    private int maxCostEval;
    protected org.apache.commons.math.estimation.WeightedMeasurement[] measurements;
    protected org.apache.commons.math.estimation.EstimatedParameter[] parameters;
    protected double[] residuals;
    protected int rows;

    @Override // org.apache.commons.math.estimation.Estimator
    public abstract void estimate(org.apache.commons.math.estimation.EstimationProblem estimationProblem) throws org.apache.commons.math.estimation.EstimationException;

    protected AbstractEstimator() {
        setMaxCostEval(100);
    }

    public final void setMaxCostEval(int maxCostEval) {
        this.maxCostEval = maxCostEval;
    }

    public final int getCostEvaluations() {
        return this.costEvaluations;
    }

    public final int getJacobianEvaluations() {
        return this.jacobianEvaluations;
    }

    protected void updateJacobian() {
        incrementJacobianEvaluationsCounter();
        java.util.Arrays.fill(this.jacobian, 0.0d);
        int index = 0;
        for (int i = 0; i < this.rows; i++) {
            org.apache.commons.math.estimation.WeightedMeasurement wm = this.measurements[i];
            double factor = -org.apache.commons.math.util.FastMath.sqrt(wm.getWeight());
            int j = 0;
            while (j < this.cols) {
                this.jacobian[index] = wm.getPartial(this.parameters[j]) * factor;
                j++;
                index++;
            }
        }
    }

    protected final void incrementJacobianEvaluationsCounter() {
        this.jacobianEvaluations++;
    }

    protected void updateResidualsAndCost() throws org.apache.commons.math.estimation.EstimationException {
        int i = this.costEvaluations + 1;
        this.costEvaluations = i;
        if (i > this.maxCostEval) {
            throw new org.apache.commons.math.estimation.EstimationException(org.apache.commons.math.exception.util.LocalizedFormats.MAX_EVALUATIONS_EXCEEDED, java.lang.Integer.valueOf(this.maxCostEval));
        }
        this.cost = 0.0d;
        int index = 0;
        int i2 = 0;
        while (i2 < this.rows) {
            org.apache.commons.math.estimation.WeightedMeasurement wm = this.measurements[i2];
            double residual = wm.getResidual();
            this.residuals[i2] = org.apache.commons.math.util.FastMath.sqrt(wm.getWeight()) * residual;
            this.cost += wm.getWeight() * residual * residual;
            i2++;
            index += this.cols;
        }
        this.cost = org.apache.commons.math.util.FastMath.sqrt(this.cost);
    }

    @Override // org.apache.commons.math.estimation.Estimator
    public double getRMS(org.apache.commons.math.estimation.EstimationProblem problem) {
        org.apache.commons.math.estimation.WeightedMeasurement[] wm = problem.getMeasurements();
        double criterion = 0.0d;
        for (int i = 0; i < wm.length; i++) {
            double residual = wm[i].getResidual();
            criterion += wm[i].getWeight() * residual * residual;
        }
        int i2 = wm.length;
        return org.apache.commons.math.util.FastMath.sqrt(criterion / ((double) i2));
    }

    public double getChiSquare(org.apache.commons.math.estimation.EstimationProblem problem) {
        org.apache.commons.math.estimation.WeightedMeasurement[] wm = problem.getMeasurements();
        double chiSquare = 0.0d;
        for (int i = 0; i < wm.length; i++) {
            double residual = wm[i].getResidual();
            chiSquare += (residual * residual) / wm[i].getWeight();
        }
        return chiSquare;
    }

    @Override // org.apache.commons.math.estimation.Estimator
    public double[][] getCovariances(org.apache.commons.math.estimation.EstimationProblem problem) throws org.apache.commons.math.estimation.EstimationException {
        updateJacobian();
        int n = problem.getMeasurements().length;
        int m = problem.getUnboundParameters().length;
        int max = m * n;
        double[][] jTj = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, m, m);
        for (int i = 0; i < m; i++) {
            for (int j = i; j < m; j++) {
                double sum = 0.0d;
                for (int k = 0; k < max; k += m) {
                    sum += this.jacobian[k + i] * this.jacobian[k + j];
                }
                jTj[i][j] = sum;
                jTj[j][i] = sum;
            }
        }
        try {
            org.apache.commons.math.linear.RealMatrix inverse = new org.apache.commons.math.linear.LUDecompositionImpl(org.apache.commons.math.linear.MatrixUtils.createRealMatrix(jTj)).getSolver().getInverse();
            return inverse.getData();
        } catch (org.apache.commons.math.linear.InvalidMatrixException e) {
            throw new org.apache.commons.math.estimation.EstimationException(org.apache.commons.math.exception.util.LocalizedFormats.UNABLE_TO_COMPUTE_COVARIANCE_SINGULAR_PROBLEM, new java.lang.Object[0]);
        }
    }

    @Override // org.apache.commons.math.estimation.Estimator
    public double[] guessParametersErrors(org.apache.commons.math.estimation.EstimationProblem problem) throws org.apache.commons.math.estimation.EstimationException {
        int m = problem.getMeasurements().length;
        int p = problem.getUnboundParameters().length;
        if (m <= p) {
            throw new org.apache.commons.math.estimation.EstimationException(org.apache.commons.math.exception.util.LocalizedFormats.NO_DEGREES_OF_FREEDOM, java.lang.Integer.valueOf(m), java.lang.Integer.valueOf(p));
        }
        double[] errors = new double[problem.getUnboundParameters().length];
        double c = org.apache.commons.math.util.FastMath.sqrt(getChiSquare(problem) / ((double) (m - p)));
        double[][] covar = getCovariances(problem);
        for (int i = 0; i < errors.length; i++) {
            errors[i] = org.apache.commons.math.util.FastMath.sqrt(covar[i][i]) * c;
        }
        return errors;
    }

    protected void initializeEstimate(org.apache.commons.math.estimation.EstimationProblem problem) {
        this.costEvaluations = 0;
        this.jacobianEvaluations = 0;
        this.measurements = problem.getMeasurements();
        this.parameters = problem.getUnboundParameters();
        this.rows = this.measurements.length;
        this.cols = this.parameters.length;
        this.jacobian = new double[this.rows * this.cols];
        this.residuals = new double[this.rows];
        this.cost = Double.POSITIVE_INFINITY;
    }
}
