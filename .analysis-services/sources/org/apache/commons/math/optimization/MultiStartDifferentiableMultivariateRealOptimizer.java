package org.apache.commons.math.optimization;

/* JADX INFO: loaded from: classes4.dex */
public class MultiStartDifferentiableMultivariateRealOptimizer implements org.apache.commons.math.optimization.DifferentiableMultivariateRealOptimizer {
    private org.apache.commons.math.random.RandomVectorGenerator generator;
    private int maxEvaluations;
    private int maxIterations;
    private final org.apache.commons.math.optimization.DifferentiableMultivariateRealOptimizer optimizer;
    private int starts;
    private int totalIterations = 0;
    private int totalEvaluations = 0;
    private int totalGradientEvaluations = 0;
    private org.apache.commons.math.optimization.RealPointValuePair[] optima = null;

    public MultiStartDifferentiableMultivariateRealOptimizer(org.apache.commons.math.optimization.DifferentiableMultivariateRealOptimizer optimizer, int starts, org.apache.commons.math.random.RandomVectorGenerator generator) {
        this.optimizer = optimizer;
        this.starts = starts;
        this.generator = generator;
        setMaxIterations(Integer.MAX_VALUE);
        setMaxEvaluations(Integer.MAX_VALUE);
    }

    public org.apache.commons.math.optimization.RealPointValuePair[] getOptima() throws java.lang.IllegalStateException {
        if (this.optima == null) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, new java.lang.Object[0]);
        }
        return (org.apache.commons.math.optimization.RealPointValuePair[]) this.optima.clone();
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateRealOptimizer
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateRealOptimizer
    public int getMaxIterations() {
        return this.maxIterations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateRealOptimizer
    public int getIterations() {
        return this.totalIterations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateRealOptimizer
    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateRealOptimizer
    public int getMaxEvaluations() {
        return this.maxEvaluations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateRealOptimizer
    public int getEvaluations() {
        return this.totalEvaluations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateRealOptimizer
    public int getGradientEvaluations() {
        return this.totalGradientEvaluations;
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateRealOptimizer
    public void setConvergenceChecker(org.apache.commons.math.optimization.RealConvergenceChecker checker) {
        this.optimizer.setConvergenceChecker(checker);
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateRealOptimizer
    public org.apache.commons.math.optimization.RealConvergenceChecker getConvergenceChecker() {
        return this.optimizer.getConvergenceChecker();
    }

    @Override // org.apache.commons.math.optimization.DifferentiableMultivariateRealOptimizer
    public org.apache.commons.math.optimization.RealPointValuePair optimize(org.apache.commons.math.analysis.DifferentiableMultivariateRealFunction f, final org.apache.commons.math.optimization.GoalType goalType, double[] startPoint) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException {
        this.optima = new org.apache.commons.math.optimization.RealPointValuePair[this.starts];
        this.totalIterations = 0;
        this.totalEvaluations = 0;
        this.totalGradientEvaluations = 0;
        int i = 0;
        while (i < this.starts) {
            try {
                this.optimizer.setMaxIterations(this.maxIterations - this.totalIterations);
                this.optimizer.setMaxEvaluations(this.maxEvaluations - this.totalEvaluations);
                this.optima[i] = this.optimizer.optimize(f, goalType, i == 0 ? startPoint : this.generator.nextVector());
            } catch (org.apache.commons.math.FunctionEvaluationException e) {
                this.optima[i] = null;
            } catch (org.apache.commons.math.optimization.OptimizationException e2) {
                this.optima[i] = null;
            }
            this.totalIterations += this.optimizer.getIterations();
            this.totalEvaluations += this.optimizer.getEvaluations();
            this.totalGradientEvaluations += this.optimizer.getGradientEvaluations();
            i++;
        }
        java.util.Arrays.sort(this.optima, new java.util.Comparator<org.apache.commons.math.optimization.RealPointValuePair>() { // from class: org.apache.commons.math.optimization.MultiStartDifferentiableMultivariateRealOptimizer.1
            @Override // java.util.Comparator
            public int compare(org.apache.commons.math.optimization.RealPointValuePair o1, org.apache.commons.math.optimization.RealPointValuePair o2) {
                if (o1 == null) {
                    return o2 == null ? 0 : 1;
                }
                if (o2 == null) {
                    return -1;
                }
                double v1 = o1.getValue();
                double v2 = o2.getValue();
                return goalType == org.apache.commons.math.optimization.GoalType.MINIMIZE ? java.lang.Double.compare(v1, v2) : java.lang.Double.compare(v2, v1);
            }
        });
        if (this.optima[0] != null) {
            return this.optima[0];
        }
        throw new org.apache.commons.math.optimization.OptimizationException(org.apache.commons.math.exception.util.LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, java.lang.Integer.valueOf(this.starts));
    }
}
