package org.apache.commons.math.optimization.fitting;

/* JADX INFO: loaded from: classes4.dex */
public class GaussianFitter {
    private final org.apache.commons.math.optimization.fitting.CurveFitter fitter;

    public GaussianFitter(org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer optimizer) {
        this.fitter = new org.apache.commons.math.optimization.fitting.CurveFitter(optimizer);
    }

    public void addObservedPoint(double x, double y) {
        addObservedPoint(1.0d, x, y);
    }

    public void addObservedPoint(double weight, double x, double y) {
        this.fitter.addObservedPoint(weight, x, y);
    }

    public org.apache.commons.math.optimization.fitting.GaussianFunction fit() throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException {
        return new org.apache.commons.math.optimization.fitting.GaussianFunction(this.fitter.fit(new org.apache.commons.math.optimization.fitting.ParametricGaussianFunction(), createParametersGuesser(this.fitter.getObservations()).guess()));
    }

    protected org.apache.commons.math.optimization.fitting.GaussianParametersGuesser createParametersGuesser(org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] observations) {
        return new org.apache.commons.math.optimization.fitting.GaussianParametersGuesser(observations);
    }
}
