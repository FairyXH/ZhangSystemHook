package org.apache.commons.math.optimization.fitting;

/* JADX INFO: loaded from: classes4.dex */
public class GaussianFunction implements org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction, java.io.Serializable {
    private static final long serialVersionUID = -3195385616125629512L;
    private final double a;
    private final double b;
    private final double c;
    private final double d;

    public GaussianFunction(double a, double b, double c, double d) {
        if (d == 0.0d) {
            throw new org.apache.commons.math.exception.ZeroException();
        }
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public GaussianFunction(double[] parameters) {
        if (parameters == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INPUT_ARRAY);
        }
        if (parameters.length != 4) {
            throw new org.apache.commons.math.exception.DimensionMismatchException(4, parameters.length);
        }
        if (parameters[3] == 0.0d) {
            throw new org.apache.commons.math.exception.ZeroException();
        }
        this.a = parameters[0];
        this.b = parameters[1];
        this.c = parameters[2];
        this.d = parameters[3];
    }

    @Override // org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction
    public org.apache.commons.math.analysis.UnivariateRealFunction derivative() {
        return new org.apache.commons.math.optimization.fitting.GaussianDerivativeFunction(this.b, this.c, this.d);
    }

    @Override // org.apache.commons.math.analysis.UnivariateRealFunction
    public double value(double x) {
        double xMc = x - this.c;
        return this.a + (this.b * java.lang.Math.exp(((-xMc) * xMc) / ((this.d * this.d) * 2.0d)));
    }

    public double getA() {
        return this.a;
    }

    public double getB() {
        return this.b;
    }

    public double getC() {
        return this.c;
    }

    public double getD() {
        return this.d;
    }
}
