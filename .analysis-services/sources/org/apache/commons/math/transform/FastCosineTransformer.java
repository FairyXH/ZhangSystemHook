package org.apache.commons.math.transform;

/* JADX INFO: loaded from: classes4.dex */
public class FastCosineTransformer implements org.apache.commons.math.transform.RealTransformer {
    @Override // org.apache.commons.math.transform.RealTransformer
    public double[] transform(double[] f) throws java.lang.IllegalArgumentException {
        return fct(f);
    }

    @Override // org.apache.commons.math.transform.RealTransformer
    public double[] transform(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        double[] data = org.apache.commons.math.transform.FastFourierTransformer.sample(f, min, max, n);
        return fct(data);
    }

    public double[] transform2(double[] f) throws java.lang.IllegalArgumentException {
        double scaling_coefficient = org.apache.commons.math.util.FastMath.sqrt(2.0d / ((double) (f.length - 1)));
        return org.apache.commons.math.transform.FastFourierTransformer.scaleArray(fct(f), scaling_coefficient);
    }

    public double[] transform2(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        double[] data = org.apache.commons.math.transform.FastFourierTransformer.sample(f, min, max, n);
        double scaling_coefficient = org.apache.commons.math.util.FastMath.sqrt(2.0d / ((double) (n - 1)));
        return org.apache.commons.math.transform.FastFourierTransformer.scaleArray(fct(data), scaling_coefficient);
    }

    @Override // org.apache.commons.math.transform.RealTransformer
    public double[] inversetransform(double[] f) throws java.lang.IllegalArgumentException {
        double scaling_coefficient = 2.0d / ((double) (f.length - 1));
        return org.apache.commons.math.transform.FastFourierTransformer.scaleArray(fct(f), scaling_coefficient);
    }

    @Override // org.apache.commons.math.transform.RealTransformer
    public double[] inversetransform(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        double[] data = org.apache.commons.math.transform.FastFourierTransformer.sample(f, min, max, n);
        double scaling_coefficient = 2.0d / ((double) (n - 1));
        return org.apache.commons.math.transform.FastFourierTransformer.scaleArray(fct(data), scaling_coefficient);
    }

    public double[] inversetransform2(double[] f) throws java.lang.IllegalArgumentException {
        return transform2(f);
    }

    public double[] inversetransform2(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        return transform2(f, min, max, n);
    }

    protected double[] fct(double[] f) throws java.lang.IllegalArgumentException {
        double[] transformed = new double[f.length];
        int n = f.length - 1;
        if (!org.apache.commons.math.transform.FastFourierTransformer.isPowerOf2(n)) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POWER_OF_TWO_PLUS_ONE, java.lang.Integer.valueOf(f.length));
        }
        double d = 0.5d;
        if (n == 1) {
            transformed[0] = (f[0] + f[1]) * 0.5d;
            transformed[1] = (f[0] - f[1]) * 0.5d;
            return transformed;
        }
        double[] x = new double[n];
        x[0] = (f[0] + f[n]) * 0.5d;
        x[n >> 1] = f[n >> 1];
        double t1 = (f[0] - f[n]) * 0.5d;
        int i = 1;
        while (i < (n >> 1)) {
            double a = (f[i] + f[n - i]) * d;
            double b = org.apache.commons.math.util.FastMath.sin((((double) i) * 3.141592653589793d) / ((double) n)) * (f[i] - f[n - i]);
            double[] x2 = x;
            double c = org.apache.commons.math.util.FastMath.cos((((double) i) * 3.141592653589793d) / ((double) n)) * (f[i] - f[n - i]);
            x2[i] = a - b;
            x2[n - i] = a + b;
            t1 += c;
            i++;
            x = x2;
            d = 0.5d;
        }
        org.apache.commons.math.transform.FastFourierTransformer transformer = new org.apache.commons.math.transform.FastFourierTransformer();
        org.apache.commons.math.complex.Complex[] y = transformer.transform(x);
        transformed[0] = y[0].getReal();
        transformed[1] = t1;
        for (int i2 = 1; i2 < (n >> 1); i2++) {
            transformed[i2 * 2] = y[i2].getReal();
            transformed[(i2 * 2) + 1] = transformed[(i2 * 2) - 1] - y[i2].getImaginary();
        }
        transformed[n] = y[n >> 1].getReal();
        return transformed;
    }
}
