package org.apache.commons.math.optimization;

/* JADX INFO: loaded from: classes4.dex */
public class MultiStartDifferentiableMultivariateVectorialOptimizer implements org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer {
    private static final long serialVersionUID = 9206382258980561530L;
    private org.apache.commons.math.random.RandomVectorGenerator generator;
    private int maxEvaluations;
    private int maxIterations;
    private final org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer optimizer;
    private int starts;
    private int totalIterations = 0;
    private int totalEvaluations = 0;
    private int totalJacobianEvaluations = 0;
    private org.apache.commons.math.optimization.VectorialPointValuePair[] optima = null;

    public MultiStartDifferentiableMultivariateVectorialOptimizer(org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer optimizer, int starts, org.apache.commons.math.random.RandomVectorGenerator generator) {
        this.optimizer = optimizer;
        this.starts = starts;
        this.generator = generator;
        setMaxIterations(Integer.MAX_VALUE);
        setMaxEvaluations(Integer.MAX_VALUE);
    }

    public org.apache.commons.math.optimization.VectorialPointValuePair[] getOptima() throws java.lang.IllegalStateException {
        if (this.optima == null) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, new java.lang.Object[0]);
        }
        return (org.apache.commons.math.optimization.VectorialPointValuePair[]) this.optima.clone();
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
        return this.totalIterations;
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
        return this.totalEvaluations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer
    public int getJacobianEvaluations() {
        return this.totalJacobianEvaluations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer
    public void setConvergenceChecker(org.apache.commons.math.optimization.VectorialConvergenceChecker checker) {
        this.optimizer.setConvergenceChecker(checker);
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer
    public org.apache.commons.math.optimization.VectorialConvergenceChecker getConvergenceChecker() {
        return this.optimizer.getConvergenceChecker();
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer
    public org.apache.commons.math.optimization.VectorialPointValuePair optimize(org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction f, final double[] target, final double[] weights, double[] startPoint) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException, java.lang.IllegalArgumentException {
        this.optima = new org.apache.commons.math.optimization.VectorialPointValuePair[this.starts];
        this.totalIterations = 0;
        this.totalEvaluations = 0;
        this.totalJacobianEvaluations = 0;
        int i = 0;
        while (i < this.starts) {
            try {
                this.optimizer.setMaxIterations(this.maxIterations - this.totalIterations);
                this.optimizer.setMaxEvaluations(this.maxEvaluations - this.totalEvaluations);
                this.optima[i] = this.optimizer.optimize(f, target, weights, i == 0 ? startPoint : this.generator.nextVector());
            } catch (org.apache.commons.math.FunctionEvaluationException e) {
                this.optima[i] = null;
            } catch (org.apache.commons.math.optimization.OptimizationException e2) {
                this.optima[i] = null;
            }
            this.totalIterations += this.optimizer.getIterations();
            this.totalEvaluations += this.optimizer.getEvaluations();
            this.totalJacobianEvaluations += this.optimizer.getJacobianEvaluations();
            i++;
        }
        java.util.Arrays.sort(this.optima, new java.util.Comparator<org.apache.commons.math.optimization.VectorialPointValuePair>() { // from class: org.apache.commons.math.optimization.MultiStartDifferentiableMultivariateVectorialOptimizer.1
            @Override // java.util.Comparator
            public int compare(org.apache.commons.math.optimization.VectorialPointValuePair o1, org.apache.commons.math.optimization.VectorialPointValuePair o2) {
                if (o1 == null) {
                    return o2 == null ? 0 : 1;
                }
                if (o2 == null) {
                    return -1;
                }
                return java.lang.Double.compare(weightedResidual(o1), weightedResidual(o2));
            }

            private double weightedResidual(org.apache.commons.math.optimization.VectorialPointValuePair pv) {
                double[] value = pv.getValueRef();
                double sum = 0.0d;
                for (int i2 = 0; i2 < value.length; i2++) {
                    double ri = value[i2] - target[i2];
                    sum += weights[i2] * ri * ri;
                }
                return sum;
            }
        });
        if (this.optima[0] != null) {
            return this.optima[0];
        }
        throw new org.apache.commons.math.optimization.OptimizationException(org.apache.commons.math.exception.util.LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, java.lang.Integer.valueOf(this.starts));
    }
}
