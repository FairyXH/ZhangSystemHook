package org.apache.commons.math.optimization.direct;

/* JADX INFO: loaded from: classes4.dex */
public class MultiDirectional extends org.apache.commons.math.optimization.direct.DirectSearchOptimizer {
    private final double gamma;
    private final double khi;

    public MultiDirectional() {
        this.khi = 2.0d;
        this.gamma = 0.5d;
    }

    public MultiDirectional(double khi, double gamma) {
        this.khi = khi;
        this.gamma = gamma;
    }

    @Override // org.apache.commons.math.optimization.direct.DirectSearchOptimizer
    protected void iterateSimplex(java.util.Comparator<org.apache.commons.math.optimization.RealPointValuePair> comparator) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException, java.lang.IllegalArgumentException {
        boolean converged;
        org.apache.commons.math.optimization.RealConvergenceChecker checker = getConvergenceChecker();
        do {
            incrementIterationsCounter();
            org.apache.commons.math.optimization.RealPointValuePair[] original = this.simplex;
            org.apache.commons.math.optimization.RealPointValuePair best = original[0];
            org.apache.commons.math.optimization.RealPointValuePair reflected = evaluateNewSimplex(original, 1.0d, comparator);
            if (comparator.compare(reflected, best) < 0) {
                org.apache.commons.math.optimization.RealPointValuePair[] reflectedSimplex = this.simplex;
                org.apache.commons.math.optimization.RealPointValuePair expanded = evaluateNewSimplex(original, this.khi, comparator);
                if (comparator.compare(reflected, expanded) <= 0) {
                    this.simplex = reflectedSimplex;
                    return;
                }
                return;
            }
            org.apache.commons.math.optimization.RealPointValuePair contracted = evaluateNewSimplex(original, this.gamma, comparator);
            if (comparator.compare(contracted, best) < 0) {
                return;
            }
            int iter = getIterations();
            converged = true;
            for (int i = 0; i < this.simplex.length; i++) {
                converged &= checker.converged(iter, original[i], this.simplex[i]);
            }
        } while (!converged);
    }

    private org.apache.commons.math.optimization.RealPointValuePair evaluateNewSimplex(org.apache.commons.math.optimization.RealPointValuePair[] original, double coeff, java.util.Comparator<org.apache.commons.math.optimization.RealPointValuePair> comparator) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException {
        double[] xSmallest = original[0].getPointRef();
        int n = xSmallest.length;
        this.simplex = new org.apache.commons.math.optimization.RealPointValuePair[n + 1];
        this.simplex[0] = original[0];
        for (int i = 1; i <= n; i++) {
            double[] xOriginal = original[i].getPointRef();
            double[] xTransformed = new double[n];
            for (int j = 0; j < n; j++) {
                xTransformed[j] = xSmallest[j] + ((xSmallest[j] - xOriginal[j]) * coeff);
            }
            this.simplex[i] = new org.apache.commons.math.optimization.RealPointValuePair(xTransformed, Double.NaN, false);
        }
        evaluateSimplex(comparator);
        return this.simplex[0];
    }
}
