package org.apache.commons.math.optimization.fitting;

/* JADX INFO: loaded from: classes4.dex */
public class PolynomialFitter {
    private final int degree;
    private final org.apache.commons.math.optimization.fitting.CurveFitter fitter;

    public PolynomialFitter(int degree, org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer optimizer) {
        this.fitter = new org.apache.commons.math.optimization.fitting.CurveFitter(optimizer);
        this.degree = degree;
    }

    public void addObservedPoint(double weight, double x, double y) {
        this.fitter.addObservedPoint(weight, x, y);
    }

    public void clearObservations() {
        this.fitter.clearObservations();
    }

    public org.apache.commons.math.analysis.polynomials.PolynomialFunction fit() throws org.apache.commons.math.optimization.OptimizationException {
        try {
            return new org.apache.commons.math.analysis.polynomials.PolynomialFunction(this.fitter.fit(new org.apache.commons.math.optimization.fitting.PolynomialFitter.ParametricPolynomial(), new double[this.degree + 1]));
        } catch (org.apache.commons.math.FunctionEvaluationException fee) {
            throw new java.lang.RuntimeException(fee);
        }
    }

    private static class ParametricPolynomial implements org.apache.commons.math.optimization.fitting.ParametricRealFunction {
        private ParametricPolynomial() {
        }

        @Override // org.apache.commons.math.optimization.fitting.ParametricRealFunction
        public double[] gradient(double x, double[] parameters) {
            double[] gradient = new double[parameters.length];
            double xn = 1.0d;
            for (int i = 0; i < parameters.length; i++) {
                gradient[i] = xn;
                xn *= x;
            }
            return gradient;
        }

        @Override // org.apache.commons.math.optimization.fitting.ParametricRealFunction
        public double value(double x, double[] parameters) {
            double y = 0.0d;
            for (int i = parameters.length - 1; i >= 0; i--) {
                y = (y * x) + parameters[i];
            }
            return y;
        }
    }
}
