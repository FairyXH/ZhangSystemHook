package org.apache.commons.math.transform;

/* JADX INFO: loaded from: classes4.dex */
public class FastFourierTransformer implements java.io.Serializable {
    static final long serialVersionUID = 5138259215438106000L;
    private org.apache.commons.math.transform.FastFourierTransformer.RootsOfUnity roots = new org.apache.commons.math.transform.FastFourierTransformer.RootsOfUnity();

    public org.apache.commons.math.complex.Complex[] transform(double[] f) throws java.lang.IllegalArgumentException {
        return fft(f, false);
    }

    public org.apache.commons.math.complex.Complex[] transform(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        double[] data = sample(f, min, max, n);
        return fft(data, false);
    }

    public org.apache.commons.math.complex.Complex[] transform(org.apache.commons.math.complex.Complex[] f) throws java.lang.IllegalArgumentException {
        this.roots.computeOmega(f.length);
        return fft(f);
    }

    public org.apache.commons.math.complex.Complex[] transform2(double[] f) throws java.lang.IllegalArgumentException {
        double scaling_coefficient = 1.0d / org.apache.commons.math.util.FastMath.sqrt(f.length);
        return scaleArray(fft(f, false), scaling_coefficient);
    }

    public org.apache.commons.math.complex.Complex[] transform2(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        double[] data = sample(f, min, max, n);
        double scaling_coefficient = 1.0d / org.apache.commons.math.util.FastMath.sqrt(n);
        return scaleArray(fft(data, false), scaling_coefficient);
    }

    public org.apache.commons.math.complex.Complex[] transform2(org.apache.commons.math.complex.Complex[] f) throws java.lang.IllegalArgumentException {
        this.roots.computeOmega(f.length);
        double scaling_coefficient = 1.0d / org.apache.commons.math.util.FastMath.sqrt(f.length);
        return scaleArray(fft(f), scaling_coefficient);
    }

    public org.apache.commons.math.complex.Complex[] inversetransform(double[] f) throws java.lang.IllegalArgumentException {
        double scaling_coefficient = 1.0d / ((double) f.length);
        return scaleArray(fft(f, true), scaling_coefficient);
    }

    public org.apache.commons.math.complex.Complex[] inversetransform(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        double[] data = sample(f, min, max, n);
        double scaling_coefficient = 1.0d / ((double) n);
        return scaleArray(fft(data, true), scaling_coefficient);
    }

    public org.apache.commons.math.complex.Complex[] inversetransform(org.apache.commons.math.complex.Complex[] f) throws java.lang.IllegalArgumentException {
        this.roots.computeOmega(-f.length);
        double scaling_coefficient = 1.0d / ((double) f.length);
        return scaleArray(fft(f), scaling_coefficient);
    }

    public org.apache.commons.math.complex.Complex[] inversetransform2(double[] f) throws java.lang.IllegalArgumentException {
        double scaling_coefficient = 1.0d / org.apache.commons.math.util.FastMath.sqrt(f.length);
        return scaleArray(fft(f, true), scaling_coefficient);
    }

    public org.apache.commons.math.complex.Complex[] inversetransform2(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        double[] data = sample(f, min, max, n);
        double scaling_coefficient = 1.0d / org.apache.commons.math.util.FastMath.sqrt(n);
        return scaleArray(fft(data, true), scaling_coefficient);
    }

    public org.apache.commons.math.complex.Complex[] inversetransform2(org.apache.commons.math.complex.Complex[] f) throws java.lang.IllegalArgumentException {
        this.roots.computeOmega(-f.length);
        double scaling_coefficient = 1.0d / org.apache.commons.math.util.FastMath.sqrt(f.length);
        return scaleArray(fft(f), scaling_coefficient);
    }

    protected org.apache.commons.math.complex.Complex[] fft(double[] f, boolean isInverse) throws java.lang.IllegalArgumentException {
        verifyDataSet(f);
        org.apache.commons.math.complex.Complex[] F = new org.apache.commons.math.complex.Complex[f.length];
        if (f.length == 1) {
            F[0] = new org.apache.commons.math.complex.Complex(f[0], 0.0d);
            return F;
        }
        int N = f.length >> 1;
        org.apache.commons.math.complex.Complex[] c = new org.apache.commons.math.complex.Complex[N];
        for (int i = 0; i < N; i++) {
            c[i] = new org.apache.commons.math.complex.Complex(f[i * 2], f[(i * 2) + 1]);
        }
        this.roots.computeOmega(isInverse ? -N : N);
        org.apache.commons.math.complex.Complex[] z = fft(c);
        this.roots.computeOmega(isInverse ? N * (-2) : N * 2);
        F[0] = new org.apache.commons.math.complex.Complex((z[0].getReal() + z[0].getImaginary()) * 2.0d, 0.0d);
        F[N] = new org.apache.commons.math.complex.Complex((z[0].getReal() - z[0].getImaginary()) * 2.0d, 0.0d);
        for (int i2 = 1; i2 < N; i2++) {
            org.apache.commons.math.complex.Complex A = z[N - i2].conjugate();
            org.apache.commons.math.complex.Complex B = z[i2].add(A);
            org.apache.commons.math.complex.Complex C = z[i2].subtract(A);
            org.apache.commons.math.complex.Complex D = new org.apache.commons.math.complex.Complex(-this.roots.getOmegaImaginary(i2), this.roots.getOmegaReal(i2));
            F[i2] = B.subtract(C.multiply(D));
            F[(N * 2) - i2] = F[i2].conjugate();
        }
        return scaleArray(F, 0.5d);
    }

    protected org.apache.commons.math.complex.Complex[] fft(org.apache.commons.math.complex.Complex[] data) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.transform.FastFourierTransformer fastFourierTransformer = this;
        int n = data.length;
        org.apache.commons.math.complex.Complex[] f = new org.apache.commons.math.complex.Complex[n];
        verifyDataSet(data);
        if (n == 1) {
            f[0] = data[0];
            return f;
        }
        if (n == 2) {
            f[0] = data[0].add(data[1]);
            f[1] = data[0].subtract(data[1]);
            return f;
        }
        int ii = 0;
        for (int i = 0; i < n; i++) {
            f[i] = data[ii];
            int k = n >> 1;
            while (ii >= k && k > 0) {
                ii -= k;
                k >>= 1;
            }
            ii += k;
        }
        for (int i2 = 0; i2 < n; i2 += 4) {
            org.apache.commons.math.complex.Complex a = f[i2].add(f[i2 + 1]);
            org.apache.commons.math.complex.Complex b = f[i2 + 2].add(f[i2 + 3]);
            org.apache.commons.math.complex.Complex c = f[i2].subtract(f[i2 + 1]);
            org.apache.commons.math.complex.Complex d = f[i2 + 2].subtract(f[i2 + 3]);
            org.apache.commons.math.complex.Complex e1 = c.add(d.multiply(org.apache.commons.math.complex.Complex.I));
            org.apache.commons.math.complex.Complex e2 = c.subtract(d.multiply(org.apache.commons.math.complex.Complex.I));
            f[i2] = a.add(b);
            f[i2 + 2] = a.subtract(b);
            f[i2 + 1] = fastFourierTransformer.roots.isForward() ? e2 : e1;
            f[i2 + 3] = fastFourierTransformer.roots.isForward() ? e1 : e2;
        }
        int i3 = 4;
        while (i3 < n) {
            int m = n / (i3 << 1);
            int j = 0;
            while (j < n) {
                int k2 = 0;
                while (k2 < i3) {
                    int k_times_m = k2 * m;
                    double omega_k_times_m_real = fastFourierTransformer.roots.getOmegaReal(k_times_m);
                    double omega_k_times_m_imaginary = fastFourierTransformer.roots.getOmegaImaginary(k_times_m);
                    org.apache.commons.math.complex.Complex z = new org.apache.commons.math.complex.Complex((f[(i3 + j) + k2].getReal() * omega_k_times_m_real) - (f[(i3 + j) + k2].getImaginary() * omega_k_times_m_imaginary), (f[i3 + j + k2].getReal() * omega_k_times_m_imaginary) + (f[i3 + j + k2].getImaginary() * omega_k_times_m_real));
                    f[i3 + j + k2] = f[j + k2].subtract(z);
                    f[j + k2] = f[j + k2].add(z);
                    k2++;
                    fastFourierTransformer = this;
                }
                j += i3 << 1;
                fastFourierTransformer = this;
            }
            i3 <<= 1;
            fastFourierTransformer = this;
        }
        return f;
    }

    public static double[] sample(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        if (n <= 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_NUMBER_OF_SAMPLES, java.lang.Integer.valueOf(n));
        }
        verifyInterval(min, max);
        double[] s = new double[n];
        double h = (max - min) / ((double) n);
        for (int i = 0; i < n; i++) {
            s[i] = f.value((((double) i) * h) + min);
        }
        return s;
    }

    public static double[] scaleArray(double[] f, double d) {
        for (int i = 0; i < f.length; i++) {
            f[i] = f[i] * d;
        }
        return f;
    }

    public static org.apache.commons.math.complex.Complex[] scaleArray(org.apache.commons.math.complex.Complex[] f, double d) {
        for (int i = 0; i < f.length; i++) {
            f[i] = new org.apache.commons.math.complex.Complex(f[i].getReal() * d, f[i].getImaginary() * d);
        }
        return f;
    }

    public static boolean isPowerOf2(long n) {
        return n > 0 && ((n - 1) & n) == 0;
    }

    public static void verifyDataSet(double[] d) throws java.lang.IllegalArgumentException {
        if (!isPowerOf2(d.length)) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POWER_OF_TWO_CONSIDER_PADDING, java.lang.Integer.valueOf(d.length));
        }
    }

    public static void verifyDataSet(java.lang.Object[] o) throws java.lang.IllegalArgumentException {
        if (!isPowerOf2(o.length)) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POWER_OF_TWO_CONSIDER_PADDING, java.lang.Integer.valueOf(o.length));
        }
    }

    public static void verifyInterval(double lower, double upper) throws java.lang.IllegalArgumentException {
        if (lower >= upper) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.ENDPOINTS_NOT_AN_INTERVAL, java.lang.Double.valueOf(lower), java.lang.Double.valueOf(upper));
        }
    }

    public java.lang.Object mdfft(java.lang.Object mdca, boolean forward) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.transform.FastFourierTransformer.MultiDimensionalComplexMatrix mdcm = (org.apache.commons.math.transform.FastFourierTransformer.MultiDimensionalComplexMatrix) new org.apache.commons.math.transform.FastFourierTransformer.MultiDimensionalComplexMatrix(mdca).clone();
        int[] dimensionSize = mdcm.getDimensionSizes();
        for (int i = 0; i < dimensionSize.length; i++) {
            mdfft(mdcm, forward, i, new int[0]);
        }
        return mdcm.getArray();
    }

    private void mdfft(org.apache.commons.math.transform.FastFourierTransformer.MultiDimensionalComplexMatrix mdcm, boolean forward, int d, int[] subVector) throws java.lang.IllegalArgumentException {
        org.apache.commons.math.complex.Complex[] temp;
        int[] dimensionSize = mdcm.getDimensionSizes();
        if (subVector.length == dimensionSize.length) {
            org.apache.commons.math.complex.Complex[] temp2 = new org.apache.commons.math.complex.Complex[dimensionSize[d]];
            for (int i = 0; i < dimensionSize[d]; i++) {
                subVector[d] = i;
                temp2[i] = mdcm.get(subVector);
            }
            if (forward) {
                temp = transform2(temp2);
            } else {
                temp = inversetransform2(temp2);
            }
            for (int i2 = 0; i2 < dimensionSize[d]; i2++) {
                subVector[d] = i2;
                mdcm.set(temp[i2], subVector);
            }
            return;
        }
        int[] vector = new int[subVector.length + 1];
        java.lang.System.arraycopy(subVector, 0, vector, 0, subVector.length);
        if (subVector.length == d) {
            vector[d] = 0;
            mdfft(mdcm, forward, d, vector);
            return;
        }
        for (int i3 = 0; i3 < dimensionSize[subVector.length]; i3++) {
            vector[subVector.length] = i3;
            mdfft(mdcm, forward, d, vector);
        }
    }

    private static class MultiDimensionalComplexMatrix implements java.lang.Cloneable {
        protected int[] dimensionSize;
        protected java.lang.Object multiDimensionalComplexArray;

        public MultiDimensionalComplexMatrix(java.lang.Object multiDimensionalComplexArray) {
            this.multiDimensionalComplexArray = multiDimensionalComplexArray;
            int numOfDimensions = 0;
            for (java.lang.Object lastDimension = multiDimensionalComplexArray; lastDimension instanceof java.lang.Object[]; lastDimension = ((java.lang.Object[]) lastDimension)[0]) {
                numOfDimensions++;
            }
            this.dimensionSize = new int[numOfDimensions];
            int numOfDimensions2 = 0;
            java.lang.Object lastDimension2 = multiDimensionalComplexArray;
            while (lastDimension2 instanceof java.lang.Object[]) {
                java.lang.Object[] array = (java.lang.Object[]) lastDimension2;
                this.dimensionSize[numOfDimensions2] = array.length;
                lastDimension2 = array[0];
                numOfDimensions2++;
            }
        }

        public org.apache.commons.math.complex.Complex get(int... vector) throws java.lang.IllegalArgumentException {
            if (vector == null) {
                if (this.dimensionSize.length > 0) {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, 0, java.lang.Integer.valueOf(this.dimensionSize.length));
                }
                return null;
            }
            if (vector.length != this.dimensionSize.length) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(vector.length), java.lang.Integer.valueOf(this.dimensionSize.length));
            }
            java.lang.Object lastDimension = this.multiDimensionalComplexArray;
            for (int i = 0; i < this.dimensionSize.length; i++) {
                lastDimension = ((java.lang.Object[]) lastDimension)[vector[i]];
            }
            return (org.apache.commons.math.complex.Complex) lastDimension;
        }

        public org.apache.commons.math.complex.Complex set(org.apache.commons.math.complex.Complex magnitude, int... vector) throws java.lang.IllegalArgumentException {
            if (vector == null) {
                if (this.dimensionSize.length > 0) {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, 0, java.lang.Integer.valueOf(this.dimensionSize.length));
                }
                return null;
            }
            if (vector.length != this.dimensionSize.length) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(vector.length), java.lang.Integer.valueOf(this.dimensionSize.length));
            }
            java.lang.Object[] lastDimension = (java.lang.Object[]) this.multiDimensionalComplexArray;
            for (int i = 0; i < this.dimensionSize.length - 1; i++) {
                lastDimension = (java.lang.Object[]) lastDimension[vector[i]];
            }
            org.apache.commons.math.complex.Complex lastValue = (org.apache.commons.math.complex.Complex) lastDimension[vector[this.dimensionSize.length - 1]];
            lastDimension[vector[this.dimensionSize.length - 1]] = magnitude;
            return lastValue;
        }

        public int[] getDimensionSizes() {
            return (int[]) this.dimensionSize.clone();
        }

        public java.lang.Object getArray() {
            return this.multiDimensionalComplexArray;
        }

        public java.lang.Object clone() {
            org.apache.commons.math.transform.FastFourierTransformer.MultiDimensionalComplexMatrix mdcm = new org.apache.commons.math.transform.FastFourierTransformer.MultiDimensionalComplexMatrix(java.lang.reflect.Array.newInstance((java.lang.Class<?>) org.apache.commons.math.complex.Complex.class, this.dimensionSize));
            clone(mdcm);
            return mdcm;
        }

        private void clone(org.apache.commons.math.transform.FastFourierTransformer.MultiDimensionalComplexMatrix mdcm) {
            int[] vector = new int[this.dimensionSize.length];
            int size = 1;
            for (int i = 0; i < this.dimensionSize.length; i++) {
                size *= this.dimensionSize[i];
            }
            int[][] vectorList = (int[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Integer.TYPE, size, this.dimensionSize.length);
            for (int[] iArr : vectorList) {
                java.lang.System.arraycopy(vector, 0, iArr, 0, this.dimensionSize.length);
                for (int i2 = 0; i2 < this.dimensionSize.length; i2++) {
                    vector[i2] = vector[i2] + 1;
                    if (vector[i2] < this.dimensionSize[i2]) {
                        break;
                    }
                    vector[i2] = 0;
                }
            }
            for (int[] nextVector : vectorList) {
                mdcm.set(get(nextVector), nextVector);
            }
        }
    }

    private static class RootsOfUnity implements java.io.Serializable {
        private static final long serialVersionUID = 6404784357747329667L;
        private int omegaCount = 0;
        private double[] omegaReal = null;
        private double[] omegaImaginaryForward = null;
        private double[] omegaImaginaryInverse = null;
        private boolean isForward = true;

        public synchronized boolean isForward() throws java.lang.IllegalStateException {
            if (this.omegaCount == 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.ROOTS_OF_UNITY_NOT_COMPUTED_YET, new java.lang.Object[0]);
            }
            return this.isForward;
        }

        public synchronized void computeOmega(int n) throws java.lang.IllegalArgumentException {
            if (n == 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_COMPUTE_0TH_ROOT_OF_UNITY, new java.lang.Object[0]);
            }
            this.isForward = n > 0;
            int absN = org.apache.commons.math.util.FastMath.abs(n);
            if (absN == this.omegaCount) {
                return;
            }
            double t = 6.283185307179586d / ((double) absN);
            double cosT = org.apache.commons.math.util.FastMath.cos(t);
            double sinT = org.apache.commons.math.util.FastMath.sin(t);
            this.omegaReal = new double[absN];
            this.omegaImaginaryForward = new double[absN];
            this.omegaImaginaryInverse = new double[absN];
            this.omegaReal[0] = 1.0d;
            this.omegaImaginaryForward[0] = 0.0d;
            this.omegaImaginaryInverse[0] = 0.0d;
            for (int i = 1; i < absN; i++) {
                this.omegaReal[i] = (this.omegaReal[i - 1] * cosT) + (this.omegaImaginaryForward[i - 1] * sinT);
                this.omegaImaginaryForward[i] = (this.omegaImaginaryForward[i - 1] * cosT) - (this.omegaReal[i - 1] * sinT);
                this.omegaImaginaryInverse[i] = -this.omegaImaginaryForward[i];
            }
            this.omegaCount = absN;
        }

        public synchronized double getOmegaReal(int k) throws java.lang.IllegalStateException, java.lang.IllegalArgumentException {
            if (this.omegaCount == 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.ROOTS_OF_UNITY_NOT_COMPUTED_YET, new java.lang.Object[0]);
            }
            if (k < 0 || k >= this.omegaCount) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_RANGE_ROOT_OF_UNITY_INDEX, java.lang.Integer.valueOf(k), 0, java.lang.Integer.valueOf(this.omegaCount - 1));
            }
            return this.omegaReal[k];
        }

        public synchronized double getOmegaImaginary(int k) throws java.lang.IllegalStateException, java.lang.IllegalArgumentException {
            if (this.omegaCount == 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.ROOTS_OF_UNITY_NOT_COMPUTED_YET, new java.lang.Object[0]);
            }
            if (k < 0 || k >= this.omegaCount) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_RANGE_ROOT_OF_UNITY_INDEX, java.lang.Integer.valueOf(k), 0, java.lang.Integer.valueOf(this.omegaCount - 1));
            }
            return this.isForward ? this.omegaImaginaryForward[k] : this.omegaImaginaryInverse[k];
        }
    }
}
