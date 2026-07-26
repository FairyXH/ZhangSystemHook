package org.apache.commons.math.optimization.fitting;

/* JADX INFO: loaded from: classes4.dex */
public class ParametricGaussianFunction implements org.apache.commons.math.optimization.fitting.ParametricRealFunction, java.io.Serializable {
    private static final long serialVersionUID = -3875578602503903233L;

    @Override // org.apache.commons.math.optimization.fitting.ParametricRealFunction
    public double value(double x, double[] parameters) throws org.apache.commons.math.exception.ZeroException {
        validateParameters(parameters);
        double a = parameters[0];
        double b = parameters[1];
        double c = parameters[2];
        double d = parameters[3];
        double xMc = x - c;
        return (java.lang.Math.exp(((-xMc) * xMc) / ((d * d) * 2.0d)) * b) + a;
    }

    @Override // org.apache.commons.math.optimization.fitting.ParametricRealFunction
    public double[] gradient(double x, double[] parameters) throws org.apache.commons.math.exception.ZeroException {
        validateParameters(parameters);
        double b = parameters[1];
        double c = parameters[2];
        double d = parameters[3];
        double xMc = x - c;
        double d2 = d * d;
        double exp = java.lang.Math.exp(((-xMc) * xMc) / (2.0d * d2));
        double f = ((b * exp) * xMc) / d2;
        return new double[]{1.0d, exp, f, (f * xMc) / d};
    }

    private void validateParameters(double[] parameters) throws org.apache.commons.math.exception.ZeroException {
        if (parameters == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INPUT_ARRAY);
        }
        if (parameters.length != 4) {
            throw new org.apache.commons.math.exception.DimensionMismatchException(4, parameters.length);
        }
        if (parameters[3] == 0.0d) {
            throw new org.apache.commons.math.exception.ZeroException();
        }
    }
}
