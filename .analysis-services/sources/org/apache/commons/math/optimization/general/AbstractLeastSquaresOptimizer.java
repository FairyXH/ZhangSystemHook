package org.apache.commons.math.optimization.general;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractLeastSquaresOptimizer implements org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer {
    public static final int DEFAULT_MAX_ITERATIONS = 100;
    protected org.apache.commons.math.optimization.VectorialConvergenceChecker checker;
    protected int cols;
    protected double cost;
    private org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction function;
    private int iterations;
    private org.apache.commons.math.analysis.MultivariateMatrixFunction jF;
    protected double[][] jacobian;
    private int jacobianEvaluations;
    private int maxEvaluations;
    private int maxIterations;
    protected double[] objective;
    private int objectiveEvaluations;
    protected double[] point;
    protected double[] residuals;
    protected double[] residualsWeights;
    protected int rows;
    protected double[] targetValues;
    protected double[][] wjacobian;
    protected double[] wresiduals;

    protected abstract org.apache.commons.math.optimization.VectorialPointValuePair doOptimize() throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException, java.lang.IllegalArgumentException;

    protected AbstractLeastSquaresOptimizer() {
        setConvergenceChecker(new org.apache.commons.math.optimization.SimpleVectorialValueChecker());
        setMaxIterations(100);
        setMaxEvaluations(Integer.MAX_VALUE);
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer
    public int getMaxIterations() {
        return this.maxIterations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer
    public int getIterations() {
        return this.iterations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer
    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer
    public int getMaxEvaluations() {
        return this.maxEvaluations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer
    public int getEvaluations() {
        return this.objectiveEvaluations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer
    public int getJacobianEvaluations() {
        return this.jacobianEvaluations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer
    public void setConvergenceChecker(org.apache.commons.math.optimization.VectorialConvergenceChecker convergenceChecker) {
        this.checker = convergenceChecker;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer
    public org.apache.commons.math.optimization.VectorialConvergenceChecker getConvergenceChecker() {
        return this.checker;
    }

    protected void incrementIterationsCounter() throws org.apache.commons.math.optimization.OptimizationException {
        int i = this.iterations + 1;
        this.iterations = i;
        if (i > this.maxIterations) {
            throw new org.apache.commons.math.optimization.OptimizationException(new org.apache.commons.math.MaxIterationsExceededException(this.maxIterations));
        }
    }

    protected void updateJacobian() throws org.apache.commons.math.FunctionEvaluationException {
        this.jacobianEvaluations++;
        this.jacobian = this.jF.value(this.point);
        if (this.jacobian.length != this.rows) {
            throw new org.apache.commons.math.FunctionEvaluationException(this.point, org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(this.jacobian.length), java.lang.Integer.valueOf(this.rows));
        }
        for (int i = 0; i < this.rows; i++) {
            double[] ji = this.jacobian[i];
            double wi = org.apache.commons.math.util.FastMath.sqrt(this.residualsWeights[i]);
            for (int j = 0; j < this.cols; j++) {
                ji[j] = ji[j] * (-1.0d);
                this.wjacobian[i][j] = ji[j] * wi;
            }
        }
    }

    protected void updateResidualsAndCost() throws org.apache.commons.math.FunctionEvaluationException {
        int i = this.objectiveEvaluations + 1;
        this.objectiveEvaluations = i;
        if (i > this.maxEvaluations) {
            throw new org.apache.commons.math.FunctionEvaluationException(new org.apache.commons.math.MaxEvaluationsExceededException(this.maxEvaluations), this.point);
        }
        this.objective = this.function.value(this.point);
        if (this.objective.length != this.rows) {
            throw new org.apache.commons.math.FunctionEvaluationException(this.point, org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(this.objective.length), java.lang.Integer.valueOf(this.rows));
        }
        this.cost = 0.0d;
        int index = 0;
        for (int i2 = 0; i2 < this.rows; i2++) {
            double residual = this.targetValues[i2] - this.objective[i2];
            this.residuals[i2] = residual;
            this.wresiduals[i2] = org.apache.commons.math.util.FastMath.sqrt(this.residualsWeights[i2]) * residual;
            this.cost += this.residualsWeights[i2] * residual * residual;
            index += this.cols;
        }
        this.cost = org.apache.commons.math.util.FastMath.sqrt(this.cost);
    }

    public double getRMS() {
        return org.apache.commons.math.util.FastMath.sqrt(getChiSquare() / ((double) this.rows));
    }

    public double getChiSquare() {
        return this.cost * this.cost;
    }

    public double[][] getCovariances() throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException {
        updateJacobian();
        double[][] jTj = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, this.cols, this.cols);
        for (int i = 0; i < this.cols; i++) {
            for (int j = i; j < this.cols; j++) {
                double sum = 0.0d;
                for (int k = 0; k < this.rows; k++) {
                    sum += this.wjacobian[k][i] * this.wjacobian[k][j];
                }
                jTj[i][j] = sum;
                jTj[j][i] = sum;
            }
        }
        try {
            org.apache.commons.math.linear.RealMatrix inverse = new org.apache.commons.math.linear.LUDecompositionImpl(org.apache.commons.math.linear.MatrixUtils.createRealMatrix(jTj)).getSolver().getInverse();
            return inverse.getData();
        } catch (org.apache.commons.math.linear.InvalidMatrixException e) {
            throw new org.apache.commons.math.optimization.OptimizationException(org.apache.commons.math.exception.util.LocalizedFormats.UNABLE_TO_COMPUTE_COVARIANCE_SINGULAR_PROBLEM, new java.lang.Object[0]);
        }
    }

    public double[] guessParametersErrors() throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException {
        if (this.rows <= this.cols) {
            throw new org.apache.commons.math.optimization.OptimizationException(org.apache.commons.math.exception.util.LocalizedFormats.NO_DEGREES_OF_FREEDOM, java.lang.Integer.valueOf(this.rows), java.lang.Integer.valueOf(this.cols));
        }
        double[] errors = new double[this.cols];
        double c = org.apache.commons.math.util.FastMath.sqrt(getChiSquare() / ((double) (this.rows - this.cols)));
        double[][] covar = getCovariances();
        for (int i = 0; i < errors.length; i++) {
            errors[i] = org.apache.commons.math.util.FastMath.sqrt(covar[i][i]) * c;
        }
        return errors;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer
    public org.apache.commons.math.optimization.VectorialPointValuePair optimize(org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction f, double[] target, double[] weights, double[] startPoint) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException, java.lang.IllegalArgumentException {
        if (target.length != weights.length) {
            throw new org.apache.commons.math.optimization.OptimizationException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(target.length), java.lang.Integer.valueOf(weights.length));
        }
        this.iterations = 0;
        this.objectiveEvaluations = 0;
        this.jacobianEvaluations = 0;
        this.function = f;
        this.jF = f.jacobian();
        this.targetValues = (double[]) target.clone();
        this.residualsWeights = (double[]) weights.clone();
        this.point = (double[]) startPoint.clone();
        this.residuals = new double[target.length];
        this.rows = target.length;
        this.cols = this.point.length;
        this.jacobian = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, this.rows, this.cols);
        this.wjacobian = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, this.rows, this.cols);
        this.wresiduals = new double[this.rows];
        this.cost = Double.POSITIVE_INFINITY;
        return doOptimize();
    }
}
