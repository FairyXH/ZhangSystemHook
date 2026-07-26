package org.apache.commons.math.transform;

/* JADX INFO: loaded from: classes4.dex */
public class FastSineTransformer implements org.apache.commons.math.transform.RealTransformer {
    @Override // org.apache.commons.math.transform.RealTransformer
    public double[] transform(double[] f) throws java.lang.IllegalArgumentException {
        return fst(f);
    }

    @Override // org.apache.commons.math.transform.RealTransformer
    public double[] transform(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        double[] data = org.apache.commons.math.transform.FastFourierTransformer.sample(f, min, max, n);
        data[0] = 0.0d;
        return fst(data);
    }

    public double[] transform2(double[] f) throws java.lang.IllegalArgumentException {
        double scaling_coefficient = org.apache.commons.math.util.FastMath.sqrt(2.0d / ((double) f.length));
        return org.apache.commons.math.transform.FastFourierTransformer.scaleArray(fst(f), scaling_coefficient);
    }

    public double[] transform2(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        double[] data = org.apache.commons.math.transform.FastFourierTransformer.sample(f, min, max, n);
        data[0] = 0.0d;
        double scaling_coefficient = org.apache.commons.math.util.FastMath.sqrt(2.0d / ((double) n));
        return org.apache.commons.math.transform.FastFourierTransformer.scaleArray(fst(data), scaling_coefficient);
    }

    @Override // org.apache.commons.math.transform.RealTransformer
    public double[] inversetransform(double[] f) throws java.lang.IllegalArgumentException {
        double scaling_coefficient = 2.0d / ((double) f.length);
        return org.apache.commons.math.transform.FastFourierTransformer.scaleArray(fst(f), scaling_coefficient);
    }

    @Override // org.apache.commons.math.transform.RealTransformer
    public double[] inversetransform(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        double[] data = org.apache.commons.math.transform.FastFourierTransformer.sample(f, min, max, n);
        data[0] = 0.0d;
        double scaling_coefficient = 2.0d / ((double) n);
        return org.apache.commons.math.transform.FastFourierTransformer.scaleArray(fst(data), scaling_coefficient);
    }

    public double[] inversetransform2(double[] f) throws java.lang.IllegalArgumentException {
        return transform2(f);
    }

    public double[] inversetransform2(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        return transform2(f, min, max, n);
    }

    protected double[] fst(double[] f) throws java.lang.IllegalArgumentException {
        double[] transformed = new double[f.length];
        org.apache.commons.math.transform.FastFourierTransformer.verifyDataSet(f);
        if (f[0] != 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FIRST_ELEMENT_NOT_ZERO, java.lang.Double.valueOf(f[0]));
        }
        int n = f.length;
        if (n == 1) {
            transformed[0] = 0.0d;
            return transformed;
        }
        double[] x = new double[n];
        x[0] = 0.0d;
        x[n >> 1] = f[n >> 1] * 2.0d;
        for (int i = 1; i < (n >> 1); i++) {
            double a = org.apache.commons.math.util.FastMath.sin((((double) i) * 3.141592653589793d) / ((double) n)) * (f[i] + f[n - i]);
            double b = (f[i] - f[n - i]) * 0.5d;
            x[i] = a + b;
            x[n - i] = a - b;
        }
        org.apache.commons.math.transform.FastFourierTransformer transformer = new org.apache.commons.math.transform.FastFourierTransformer();
        org.apache.commons.math.complex.Complex[] y = transformer.transform(x);
        transformed[0] = 0.0d;
        transformed[1] = y[0].getReal() * 0.5d;
        for (int i2 = 1; i2 < (n >> 1); i2++) {
            transformed[i2 * 2] = -y[i2].getImaginary();
            transformed[(i2 * 2) + 1] = y[i2].getReal() + transformed[(i2 * 2) - 1];
        }
        return transformed;
    }
}
