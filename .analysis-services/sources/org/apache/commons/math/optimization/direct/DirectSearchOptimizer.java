package org.apache.commons.math.optimization.direct;

/* JADX INFO: loaded from: classes4.dex */
public abstract class DirectSearchOptimizer implements org.apache.commons.math.optimization.MultivariateRealOptimizer {
    private org.apache.commons.math.optimization.RealConvergenceChecker checker;
    private int evaluations;
    private org.apache.commons.math.analysis.MultivariateRealFunction f;
    private int iterations;
    private int maxEvaluations;
    private int maxIterations;
    protected org.apache.commons.math.optimization.RealPointValuePair[] simplex;
    private double[][] startConfiguration;

    protected abstract void iterateSimplex(java.util.Comparator<org.apache.commons.math.optimization.RealPointValuePair> comparator) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException, java.lang.IllegalArgumentException;

    protected DirectSearchOptimizer() {
        setConvergenceChecker(new org.apache.commons.math.optimization.SimpleScalarValueChecker());
        setMaxIterations(Integer.MAX_VALUE);
        setMaxEvaluations(Integer.MAX_VALUE);
    }

    public void setStartConfiguration(double[] steps) throws java.lang.IllegalArgumentException {
        int n = steps.length;
        this.startConfiguration = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, n);
        for (int i = 0; i < n; i++) {
            double[] vertexI = this.startConfiguration[i];
            for (int j = 0; j < i + 1; j++) {
                if (steps[j] == 0.0d) {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.EQUAL_VERTICES_IN_SIMPLEX, java.lang.Integer.valueOf(j), java.lang.Integer.valueOf(j + 1));
                }
                java.lang.System.arraycopy(steps, 0, vertexI, 0, j + 1);
            }
        }
    }

    public void setStartConfiguration(double[][] referenceSimplex) throws java.lang.IllegalArgumentException {
        int n = referenceSimplex.length - 1;
        if (n < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.SIMPLEX_NEED_ONE_POINT, new java.lang.Object[0]);
        }
        this.startConfiguration = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, n);
        double[] ref0 = referenceSimplex[0];
        for (int i = 0; i < n + 1; i++) {
            double[] refI = referenceSimplex[i];
            if (refI.length != n) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(refI.length), java.lang.Integer.valueOf(n));
            }
            for (int j = 0; j < i; j++) {
                double[] refJ = referenceSimplex[j];
                boolean allEquals = true;
                int k = 0;
                while (true) {
                    if (k >= n) {
                        break;
                    }
                    if (refI[k] == refJ[k]) {
                        k++;
                    } else {
                        allEquals = false;
                        break;
                    }
                }
                if (allEquals) {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.EQUAL_VERTICES_IN_SIMPLEX, java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(j));
                }
            }
            if (i > 0) {
                double[] confI = this.startConfiguration[i - 1];
                for (int k2 = 0; k2 < n; k2++) {
                    confI[k2] = refI[k2] - ref0[k2];
                }
            }
        }
    }

    @Override // org.apache.commons.math.optimization.MultivariateRealOptimizer
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override // org.apache.commons.math.optimization.MultivariateRealOptimizer
    public int getMaxIterations() {
        return this.maxIterations;
    }

    @Override // org.apache.commons.math.optimization.MultivariateRealOptimizer
    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override // org.apache.commons.math.optimization.MultivariateRealOptimizer
    public int getMaxEvaluations() {
        return this.maxEvaluations;
    }

    @Override // org.apache.commons.math.optimization.MultivariateRealOptimizer
    public int getIterations() {
        return this.iterations;
    }

    @Override // org.apache.commons.math.optimization.MultivariateRealOptimizer
    public int getEvaluations() {
        return this.evaluations;
    }

    @Override // org.apache.commons.math.optimization.MultivariateRealOptimizer
    public void setConvergenceChecker(org.apache.commons.math.optimization.RealConvergenceChecker convergenceChecker) {
        this.checker = convergenceChecker;
    }

    @Override // org.apache.commons.math.optimization.MultivariateRealOptimizer
    public org.apache.commons.math.optimization.RealConvergenceChecker getConvergenceChecker() {
        return this.checker;
    }

    @Override // org.apache.commons.math.optimization.MultivariateRealOptimizer
    public org.apache.commons.math.optimization.RealPointValuePair optimize(org.apache.commons.math.analysis.MultivariateRealFunction function, final org.apache.commons.math.optimization.GoalType goalType, double[] startPoint) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException, java.lang.IllegalArgumentException {
        if (this.startConfiguration == null || this.startConfiguration.length != startPoint.length) {
            double[] unit = new double[startPoint.length];
            java.util.Arrays.fill(unit, 1.0d);
            setStartConfiguration(unit);
        }
        this.f = function;
        java.util.Comparator<org.apache.commons.math.optimization.RealPointValuePair> comparator = new java.util.Comparator<org.apache.commons.math.optimization.RealPointValuePair>() { // from class: org.apache.commons.math.optimization.direct.DirectSearchOptimizer.1
            @Override // java.util.Comparator
            public int compare(org.apache.commons.math.optimization.RealPointValuePair o1, org.apache.commons.math.optimization.RealPointValuePair o2) {
                double v1 = o1.getValue();
                double v2 = o2.getValue();
                return goalType == org.apache.commons.math.optimization.GoalType.MINIMIZE ? java.lang.Double.compare(v1, v2) : java.lang.Double.compare(v2, v1);
            }
        };
        this.iterations = 0;
        this.evaluations = 0;
        buildSimplex(startPoint);
        evaluateSimplex(comparator);
        org.apache.commons.math.optimization.RealPointValuePair[] previous = new org.apache.commons.math.optimization.RealPointValuePair[this.simplex.length];
        while (true) {
            if (this.iterations > 0) {
                boolean converged = true;
                for (int i = 0; i < this.simplex.length; i++) {
                    converged &= this.checker.converged(this.iterations, previous[i], this.simplex[i]);
                }
                if (converged) {
                    return this.simplex[0];
                }
            }
            java.lang.System.arraycopy(this.simplex, 0, previous, 0, this.simplex.length);
            iterateSimplex(comparator);
        }
    }

    protected void incrementIterationsCounter() throws org.apache.commons.math.optimization.OptimizationException {
        int i = this.iterations + 1;
        this.iterations = i;
        if (i > this.maxIterations) {
            throw new org.apache.commons.math.optimization.OptimizationException(new org.apache.commons.math.MaxIterationsExceededException(this.maxIterations));
        }
    }

    protected double evaluate(double[] x) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        int i = this.evaluations + 1;
        this.evaluations = i;
        if (i > this.maxEvaluations) {
            throw new org.apache.commons.math.FunctionEvaluationException(new org.apache.commons.math.MaxEvaluationsExceededException(this.maxEvaluations), x);
        }
        return this.f.value(x);
    }

    private void buildSimplex(double[] startPoint) throws java.lang.IllegalArgumentException {
        int n = startPoint.length;
        if (n != this.startConfiguration.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(n), java.lang.Integer.valueOf(this.startConfiguration.length));
        }
        this.simplex = new org.apache.commons.math.optimization.RealPointValuePair[n + 1];
        this.simplex[0] = new org.apache.commons.math.optimization.RealPointValuePair(startPoint, Double.NaN);
        for (int i = 0; i < n; i++) {
            double[] confI = this.startConfiguration[i];
            double[] vertexI = new double[n];
            for (int k = 0; k < n; k++) {
                vertexI[k] = startPoint[k] + confI[k];
            }
            this.simplex[i + 1] = new org.apache.commons.math.optimization.RealPointValuePair(vertexI, Double.NaN);
        }
    }

    protected void evaluateSimplex(java.util.Comparator<org.apache.commons.math.optimization.RealPointValuePair> comparator) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException {
        for (int i = 0; i < this.simplex.length; i++) {
            org.apache.commons.math.optimization.RealPointValuePair vertex = this.simplex[i];
            double[] point = vertex.getPointRef();
            if (java.lang.Double.isNaN(vertex.getValue())) {
                this.simplex[i] = new org.apache.commons.math.optimization.RealPointValuePair(point, evaluate(point), false);
            }
        }
        java.util.Arrays.sort(this.simplex, comparator);
    }

    protected void replaceWorstPoint(org.apache.commons.math.optimization.RealPointValuePair pointValuePair, java.util.Comparator<org.apache.commons.math.optimization.RealPointValuePair> comparator) {
        int n = this.simplex.length - 1;
        for (int i = 0; i < n; i++) {
            if (comparator.compare(this.simplex[i], pointValuePair) > 0) {
                org.apache.commons.math.optimization.RealPointValuePair tmp = this.simplex[i];
                this.simplex[i] = pointValuePair;
                pointValuePair = tmp;
            }
        }
        this.simplex[n] = pointValuePair;
    }
}
