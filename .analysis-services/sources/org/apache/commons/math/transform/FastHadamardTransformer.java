package org.apache.commons.math.transform;

/* JADX INFO: loaded from: classes4.dex */
public class FastHadamardTransformer implements org.apache.commons.math.transform.RealTransformer {
    @Override // org.apache.commons.math.transform.RealTransformer
    public double[] transform(double[] f) throws java.lang.IllegalArgumentException {
        return fht(f);
    }

    @Override // org.apache.commons.math.transform.RealTransformer
    public double[] transform(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        return fht(org.apache.commons.math.transform.FastFourierTransformer.sample(f, min, max, n));
    }

    @Override // org.apache.commons.math.transform.RealTransformer
    public double[] inversetransform(double[] f) throws java.lang.IllegalArgumentException {
        return org.apache.commons.math.transform.FastFourierTransformer.scaleArray(fht(f), 1.0d / ((double) f.length));
    }

    @Override // org.apache.commons.math.transform.RealTransformer
    public double[] inversetransform(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        double[] unscaled = fht(org.apache.commons.math.transform.FastFourierTransformer.sample(f, min, max, n));
        return org.apache.commons.math.transform.FastFourierTransformer.scaleArray(unscaled, 1.0d / ((double) n));
    }

    public int[] transform(int[] f) throws java.lang.IllegalArgumentException {
        return fht(f);
    }

    protected double[] fht(double[] x) throws java.lang.IllegalArgumentException {
        int n = x.length;
        int halfN = n / 2;
        if (!org.apache.commons.math.transform.FastFourierTransformer.isPowerOf2(n)) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POWER_OF_TWO, java.lang.Integer.valueOf(n));
        }
        double[] yPrevious = new double[n];
        double[] yCurrent = (double[]) x.clone();
        for (int j = 1; j < n; j <<= 1) {
            double[] yTmp = yCurrent;
            yCurrent = yPrevious;
            yPrevious = yTmp;
            for (int i = 0; i < halfN; i++) {
                int twoI = i * 2;
                yCurrent[i] = yPrevious[twoI] + yPrevious[twoI + 1];
            }
            for (int i2 = halfN; i2 < n; i2++) {
                int twoI2 = i2 * 2;
                yCurrent[i2] = yPrevious[twoI2 - n] - yPrevious[(twoI2 - n) + 1];
            }
        }
        return yCurrent;
    }

    protected int[] fht(int[] x) throws java.lang.IllegalArgumentException {
        int n = x.length;
        int halfN = n / 2;
        if (!org.apache.commons.math.transform.FastFourierTransformer.isPowerOf2(n)) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POWER_OF_TWO, java.lang.Integer.valueOf(n));
        }
        int[] yPrevious = new int[n];
        int[] yCurrent = (int[]) x.clone();
        for (int j = 1; j < n; j <<= 1) {
            int[] yTmp = yCurrent;
            yCurrent = yPrevious;
            yPrevious = yTmp;
            for (int i = 0; i < halfN; i++) {
                int twoI = i * 2;
                yCurrent[i] = yPrevious[twoI] + yPrevious[twoI + 1];
            }
            for (int i2 = halfN; i2 < n; i2++) {
                int twoI2 = i2 * 2;
                yCurrent[i2] = yPrevious[twoI2 - n] - yPrevious[(twoI2 - n) + 1];
            }
        }
        return yCurrent;
    }
}
