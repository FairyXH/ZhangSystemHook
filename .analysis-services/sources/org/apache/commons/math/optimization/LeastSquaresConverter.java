package org.apache.commons.math.optimization;

/* JADX INFO: loaded from: classes4.dex */
public class LeastSquaresConverter implements org.apache.commons.math.analysis.MultivariateRealFunction {
    private final org.apache.commons.math.analysis.MultivariateVectorialFunction function;
    private final double[] observations;
    private final org.apache.commons.math.linear.RealMatrix scale;
    private final double[] weights;

    public LeastSquaresConverter(org.apache.commons.math.analysis.MultivariateVectorialFunction function, double[] observations) {
        this.function = function;
        this.observations = (double[]) observations.clone();
        this.weights = null;
        this.scale = null;
    }

    public LeastSquaresConverter(org.apache.commons.math.analysis.MultivariateVectorialFunction function, double[] observations, double[] weights) throws java.lang.IllegalArgumentException {
        if (observations.length != weights.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(observations.length), java.lang.Integer.valueOf(weights.length));
        }
        this.function = function;
        this.observations = (double[]) observations.clone();
        this.weights = (double[]) weights.clone();
        this.scale = null;
    }

    public LeastSquaresConverter(org.apache.commons.math.analysis.MultivariateVectorialFunction function, double[] observations, org.apache.commons.math.linear.RealMatrix scale) throws java.lang.IllegalArgumentException {
        if (observations.length != scale.getColumnDimension()) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(observations.length), java.lang.Integer.valueOf(scale.getColumnDimension()));
        }
        this.function = function;
        this.observations = (double[]) observations.clone();
        this.weights = null;
        this.scale = scale.copy();
    }

    @Override // org.apache.commons.math.analysis.MultivariateRealFunction
    public double value(double[] point) throws org.apache.commons.math.FunctionEvaluationException {
        double[] residuals = this.function.value(point);
        if (residuals.length != this.observations.length) {
            throw new org.apache.commons.math.FunctionEvaluationException(point, org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(residuals.length), java.lang.Integer.valueOf(this.observations.length));
        }
        for (int i = 0; i < residuals.length; i++) {
            residuals[i] = residuals[i] - this.observations[i];
        }
        double sumSquares = 0.0d;
        if (this.weights != null) {
            for (int i2 = 0; i2 < residuals.length; i2++) {
                double ri = residuals[i2];
                sumSquares += this.weights[i2] * ri * ri;
            }
        } else {
            int i3 = 0;
            if (this.scale != null) {
                double[] dArrOperate = this.scale.operate(residuals);
                int length = dArrOperate.length;
                while (i3 < length) {
                    double yi = dArrOperate[i3];
                    sumSquares += yi * yi;
                    i3++;
                }
            } else {
                int length2 = residuals.length;
                while (i3 < length2) {
                    double ri2 = residuals[i3];
                    sumSquares += ri2 * ri2;
                    i3++;
                }
            }
        }
        return sumSquares;
    }
}
