package org.apache.commons.math.analysis.integration;

/* JADX INFO: loaded from: classes4.dex */
public class LegendreGaussIntegrator extends org.apache.commons.math.analysis.integration.UnivariateRealIntegratorImpl {
    private final double[] abscissas;
    private final double[] weights;
    private static final double[] ABSCISSAS_2 = {(-1.0d) / org.apache.commons.math.util.FastMath.sqrt(3.0d), 1.0d / org.apache.commons.math.util.FastMath.sqrt(3.0d)};
    private static final double[] WEIGHTS_2 = {1.0d, 1.0d};
    private static final double[] ABSCISSAS_3 = {-org.apache.commons.math.util.FastMath.sqrt(0.6d), 0.0d, org.apache.commons.math.util.FastMath.sqrt(0.6d)};
    private static final double[] WEIGHTS_3 = {0.5555555555555556d, 0.8888888888888888d, 0.5555555555555556d};
    private static final double[] ABSCISSAS_4 = {-org.apache.commons.math.util.FastMath.sqrt(((org.apache.commons.math.util.FastMath.sqrt(30.0d) * 2.0d) + 15.0d) / 35.0d), -org.apache.commons.math.util.FastMath.sqrt((15.0d - (org.apache.commons.math.util.FastMath.sqrt(30.0d) * 2.0d)) / 35.0d), org.apache.commons.math.util.FastMath.sqrt((15.0d - (org.apache.commons.math.util.FastMath.sqrt(30.0d) * 2.0d)) / 35.0d), org.apache.commons.math.util.FastMath.sqrt(((org.apache.commons.math.util.FastMath.sqrt(30.0d) * 2.0d) + 15.0d) / 35.0d)};
    private static final double[] WEIGHTS_4 = {(90.0d - (org.apache.commons.math.util.FastMath.sqrt(30.0d) * 5.0d)) / 180.0d, ((org.apache.commons.math.util.FastMath.sqrt(30.0d) * 5.0d) + 90.0d) / 180.0d, ((org.apache.commons.math.util.FastMath.sqrt(30.0d) * 5.0d) + 90.0d) / 180.0d, (90.0d - (org.apache.commons.math.util.FastMath.sqrt(30.0d) * 5.0d)) / 180.0d};
    private static final double[] ABSCISSAS_5 = {-org.apache.commons.math.util.FastMath.sqrt(((org.apache.commons.math.util.FastMath.sqrt(70.0d) * 2.0d) + 35.0d) / 63.0d), -org.apache.commons.math.util.FastMath.sqrt((35.0d - (org.apache.commons.math.util.FastMath.sqrt(70.0d) * 2.0d)) / 63.0d), 0.0d, org.apache.commons.math.util.FastMath.sqrt((35.0d - (org.apache.commons.math.util.FastMath.sqrt(70.0d) * 2.0d)) / 63.0d), org.apache.commons.math.util.FastMath.sqrt(((org.apache.commons.math.util.FastMath.sqrt(70.0d) * 2.0d) + 35.0d) / 63.0d)};
    private static final double[] WEIGHTS_5 = {(322.0d - (org.apache.commons.math.util.FastMath.sqrt(70.0d) * 13.0d)) / 900.0d, ((org.apache.commons.math.util.FastMath.sqrt(70.0d) * 13.0d) + 322.0d) / 900.0d, 0.5688888888888889d, ((org.apache.commons.math.util.FastMath.sqrt(70.0d) * 13.0d) + 322.0d) / 900.0d, (322.0d - (org.apache.commons.math.util.FastMath.sqrt(70.0d) * 13.0d)) / 900.0d};

    public LegendreGaussIntegrator(int n, int defaultMaximalIterationCount) throws java.lang.IllegalArgumentException {
        super(defaultMaximalIterationCount);
        switch (n) {
            case 2:
                this.abscissas = ABSCISSAS_2;
                this.weights = WEIGHTS_2;
                return;
            case 3:
                this.abscissas = ABSCISSAS_3;
                this.weights = WEIGHTS_3;
                return;
            case 4:
                this.abscissas = ABSCISSAS_4;
                this.weights = WEIGHTS_4;
                return;
            case 5:
                this.abscissas = ABSCISSAS_5;
                this.weights = WEIGHTS_5;
                return;
            default:
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.N_POINTS_GAUSS_LEGENDRE_INTEGRATOR_NOT_SUPPORTED, java.lang.Integer.valueOf(n), 2, 5);
        }
    }

    @Override // org.apache.commons.math.analysis.integration.UnivariateRealIntegrator
    @java.lang.Deprecated
    public double integrate(double min, double max) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException, org.apache.commons.math.ConvergenceException {
        return integrate(this.f, min, max);
    }

    @Override // org.apache.commons.math.analysis.integration.UnivariateRealIntegrator
    public double integrate(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException, org.apache.commons.math.ConvergenceException {
        clearResult();
        verifyInterval(min, max);
        verifyIterationCount();
        double oldt = stage(f, min, max, 1);
        double oldt2 = oldt;
        int n = 2;
        for (int i = 0; i < this.maximalIterationCount; i++) {
            double t = stage(f, min, max, n);
            double delta = org.apache.commons.math.util.FastMath.abs(t - oldt2);
            double limit = org.apache.commons.math.util.FastMath.max(this.absoluteAccuracy, this.relativeAccuracy * (org.apache.commons.math.util.FastMath.abs(oldt2) + org.apache.commons.math.util.FastMath.abs(t)) * 0.5d);
            if (i + 1 < this.minimalIterationCount || delta > limit) {
                double ratio = org.apache.commons.math.util.FastMath.min(4.0d, org.apache.commons.math.util.FastMath.pow(delta / limit, 0.5d / ((double) this.abscissas.length)));
                n = org.apache.commons.math.util.FastMath.max((int) (((double) n) * ratio), n + 1);
                oldt2 = t;
            } else {
                setResult(t, i);
                return this.result;
            }
        }
        throw new org.apache.commons.math.MaxIterationsExceededException(this.maximalIterationCount);
    }

    private double stage(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException {
        org.apache.commons.math.analysis.integration.LegendreGaussIntegrator legendreGaussIntegrator = this;
        double step = (max - min) / ((double) n);
        double halfStep = step / 2.0d;
        double midPoint = min + halfStep;
        double sum = 0.0d;
        int i = 0;
        while (i < n) {
            int j = 0;
            while (j < legendreGaussIntegrator.abscissas.length) {
                sum += legendreGaussIntegrator.weights[j] * f.value((legendreGaussIntegrator.abscissas[j] * halfStep) + midPoint);
                j++;
                legendreGaussIntegrator = this;
            }
            midPoint += step;
            i++;
            legendreGaussIntegrator = this;
        }
        return halfStep * sum;
    }
}
