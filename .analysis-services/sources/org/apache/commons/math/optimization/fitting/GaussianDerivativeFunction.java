package org.apache.commons.math.optimization.fitting;

/* JADX INFO: loaded from: classes4.dex */
public class GaussianDerivativeFunction implements org.apache.commons.math.analysis.UnivariateRealFunction, java.io.Serializable {
    private static final long serialVersionUID = -6500229089670174766L;
    private final double b;
    private final double c;
    private final double d2;

    public GaussianDerivativeFunction(double b, double c, double d) {
        if (d == 0.0d) {
            throw new org.apache.commons.math.exception.ZeroException();
        }
        this.b = b;
        this.c = c;
        this.d2 = d * d;
    }

    public GaussianDerivativeFunction(double[] parameters) {
        if (parameters == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INPUT_ARRAY);
        }
        if (parameters.length != 3) {
            throw new org.apache.commons.math.exception.DimensionMismatchException(3, parameters.length);
        }
        if (parameters[2] == 0.0d) {
            throw new org.apache.commons.math.exception.ZeroException();
        }
        this.b = parameters[0];
        this.c = parameters[1];
        this.d2 = parameters[2] * parameters[2];
    }

    @Override // org.apache.commons.math.analysis.UnivariateRealFunction
    public double value(double x) {
        double xMc = x - this.c;
        return ((-this.b) / this.d2) * xMc * java.lang.Math.exp((-(xMc * xMc)) / (this.d2 * 2.0d));
    }
}
