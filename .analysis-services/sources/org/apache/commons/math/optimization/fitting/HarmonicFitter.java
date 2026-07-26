package org.apache.commons.math.optimization.fitting;

/* JADX INFO: loaded from: classes4.dex */
public class HarmonicFitter {
    private final org.apache.commons.math.optimization.fitting.CurveFitter fitter;
    private double[] parameters;

    public HarmonicFitter(org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer optimizer) {
        this.fitter = new org.apache.commons.math.optimization.fitting.CurveFitter(optimizer);
        this.parameters = null;
    }

    public HarmonicFitter(org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer optimizer, double[] initialGuess) {
        this.fitter = new org.apache.commons.math.optimization.fitting.CurveFitter(optimizer);
        this.parameters = (double[]) initialGuess.clone();
    }

    public void addObservedPoint(double weight, double x, double y) {
        this.fitter.addObservedPoint(weight, x, y);
    }

    public org.apache.commons.math.optimization.fitting.HarmonicFunction fit() throws org.apache.commons.math.optimization.OptimizationException {
        if (this.parameters == null) {
            org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] observations = this.fitter.getObservations();
            if (observations.length < 4) {
                throw new org.apache.commons.math.optimization.OptimizationException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE, java.lang.Integer.valueOf(observations.length), 4);
            }
            org.apache.commons.math.optimization.fitting.HarmonicCoefficientsGuesser guesser = new org.apache.commons.math.optimization.fitting.HarmonicCoefficientsGuesser(observations);
            guesser.guess();
            this.parameters = new double[]{guesser.getGuessedAmplitude(), guesser.getGuessedPulsation(), guesser.getGuessedPhase()};
        }
        try {
            double[] fitted = this.fitter.fit(new org.apache.commons.math.optimization.fitting.HarmonicFitter.ParametricHarmonicFunction(), this.parameters);
            return new org.apache.commons.math.optimization.fitting.HarmonicFunction(fitted[0], fitted[1], fitted[2]);
        } catch (org.apache.commons.math.FunctionEvaluationException fee) {
            throw new java.lang.RuntimeException(fee);
        }
    }

    private static class ParametricHarmonicFunction implements org.apache.commons.math.optimization.fitting.ParametricRealFunction {
        private ParametricHarmonicFunction() {
        }

        @Override // org.apache.commons.math.optimization.fitting.ParametricRealFunction
        public double value(double x, double[] parameters) {
            double a = parameters[0];
            double omega = parameters[1];
            double phi = parameters[2];
            return org.apache.commons.math.util.FastMath.cos((omega * x) + phi) * a;
        }

        @Override // org.apache.commons.math.optimization.fitting.ParametricRealFunction
        public double[] gradient(double x, double[] parameters) {
            double a = parameters[0];
            double omega = parameters[1];
            double phi = parameters[2];
            double alpha = (omega * x) + phi;
            double cosAlpha = org.apache.commons.math.util.FastMath.cos(alpha);
            double sinAlpha = org.apache.commons.math.util.FastMath.sin(alpha);
            return new double[]{cosAlpha, (-a) * x * sinAlpha, (-a) * sinAlpha};
        }
    }
}
