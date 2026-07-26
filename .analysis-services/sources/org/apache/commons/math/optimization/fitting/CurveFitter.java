package org.apache.commons.math.optimization.fitting;

/* JADX INFO: loaded from: classes4.dex */
public class CurveFitter {
    private final java.util.List<org.apache.commons.math.optimization.fitting.WeightedObservedPoint> observations = new java.util.ArrayList();
    private final org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer optimizer;

    public CurveFitter(org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer optimizer) {
        this.optimizer = optimizer;
    }

    public void addObservedPoint(double x, double y) {
        addObservedPoint(1.0d, x, y);
    }

    public void addObservedPoint(double weight, double x, double y) {
        this.observations.add(new org.apache.commons.math.optimization.fitting.WeightedObservedPoint(weight, x, y));
    }

    public void addObservedPoint(org.apache.commons.math.optimization.fitting.WeightedObservedPoint observed) {
        this.observations.add(observed);
    }

    public org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] getObservations() {
        return (org.apache.commons.math.optimization.fitting.WeightedObservedPoint[]) this.observations.toArray(new org.apache.commons.math.optimization.fitting.WeightedObservedPoint[this.observations.size()]);
    }

    public void clearObservations() {
        this.observations.clear();
    }

    public double[] fit(org.apache.commons.math.optimization.fitting.ParametricRealFunction f, double[] initialGuess) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException, java.lang.IllegalArgumentException {
        double[] target = new double[this.observations.size()];
        double[] weights = new double[this.observations.size()];
        int i = 0;
        for (org.apache.commons.math.optimization.fitting.WeightedObservedPoint point : this.observations) {
            target[i] = point.getY();
            weights[i] = point.getWeight();
            i++;
        }
        org.apache.commons.math.optimization.VectorialPointValuePair optimum = this.optimizer.optimize(new org.apache.commons.math.optimization.fitting.CurveFitter.TheoreticalValuesFunction(f), target, weights, initialGuess);
        return optimum.getPointRef();
    }

    private class TheoreticalValuesFunction implements org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction {
        private final org.apache.commons.math.optimization.fitting.ParametricRealFunction f;

        public TheoreticalValuesFunction(org.apache.commons.math.optimization.fitting.ParametricRealFunction f) {
            this.f = f;
        }

        @Override // org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction
        public org.apache.commons.math.analysis.MultivariateMatrixFunction jacobian() {
            return new org.apache.commons.math.analysis.MultivariateMatrixFunction() { // from class: org.apache.commons.math.optimization.fitting.CurveFitter.TheoreticalValuesFunction.1
                @Override // org.apache.commons.math.analysis.MultivariateMatrixFunction
                public double[][] value(double[] point) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
                    double[][] jacobian = new double[org.apache.commons.math.optimization.fitting.CurveFitter.this.observations.size()][];
                    int i = 0;
                    for (org.apache.commons.math.optimization.fitting.WeightedObservedPoint observed : org.apache.commons.math.optimization.fitting.CurveFitter.this.observations) {
                        jacobian[i] = org.apache.commons.math.optimization.fitting.CurveFitter.TheoreticalValuesFunction.this.f.gradient(observed.getX(), point);
                        i++;
                    }
                    return jacobian;
                }
            };
        }

        @Override // org.apache.commons.math.analysis.MultivariateVectorialFunction
        public double[] value(double[] point) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
            double[] values = new double[org.apache.commons.math.optimization.fitting.CurveFitter.this.observations.size()];
            int i = 0;
            for (org.apache.commons.math.optimization.fitting.WeightedObservedPoint observed : org.apache.commons.math.optimization.fitting.CurveFitter.this.observations) {
                values[i] = this.f.value(observed.getX(), point);
                i++;
            }
            return values;
        }
    }
}
