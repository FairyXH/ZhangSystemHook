package org.apache.commons.math.complex;

/* JADX INFO: loaded from: classes4.dex */
public class Complex implements org.apache.commons.math.FieldElement<org.apache.commons.math.complex.Complex>, java.io.Serializable {
    private static final long serialVersionUID = -6195664516687396620L;
    private final double imaginary;
    private final transient boolean isInfinite;
    private final transient boolean isNaN;
    private final double real;
    public static final org.apache.commons.math.complex.Complex I = new org.apache.commons.math.complex.Complex(0.0d, 1.0d);
    public static final org.apache.commons.math.complex.Complex NaN = new org.apache.commons.math.complex.Complex(Double.NaN, Double.NaN);
    public static final org.apache.commons.math.complex.Complex INF = new org.apache.commons.math.complex.Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    public static final org.apache.commons.math.complex.Complex ONE = new org.apache.commons.math.complex.Complex(1.0d, 0.0d);
    public static final org.apache.commons.math.complex.Complex ZERO = new org.apache.commons.math.complex.Complex(0.0d, 0.0d);

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
        boolean z = false;
        this.isNaN = java.lang.Double.isNaN(real) || java.lang.Double.isNaN(imaginary);
        if (!this.isNaN && (java.lang.Double.isInfinite(real) || java.lang.Double.isInfinite(imaginary))) {
            z = true;
        }
        this.isInfinite = z;
    }

    public double abs() {
        if (isNaN()) {
            return Double.NaN;
        }
        if (isInfinite()) {
            return Double.POSITIVE_INFINITY;
        }
        if (org.apache.commons.math.util.FastMath.abs(this.real) < org.apache.commons.math.util.FastMath.abs(this.imaginary)) {
            if (this.imaginary == 0.0d) {
                return org.apache.commons.math.util.FastMath.abs(this.real);
            }
            double q = this.real / this.imaginary;
            return org.apache.commons.math.util.FastMath.abs(this.imaginary) * org.apache.commons.math.util.FastMath.sqrt((q * q) + 1.0d);
        }
        if (this.real == 0.0d) {
            return org.apache.commons.math.util.FastMath.abs(this.imaginary);
        }
        double q2 = this.imaginary / this.real;
        return org.apache.commons.math.util.FastMath.abs(this.real) * org.apache.commons.math.util.FastMath.sqrt((q2 * q2) + 1.0d);
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.complex.Complex add(org.apache.commons.math.complex.Complex rhs) {
        return createComplex(this.real + rhs.getReal(), this.imaginary + rhs.getImaginary());
    }

    public org.apache.commons.math.complex.Complex conjugate() {
        if (isNaN()) {
            return NaN;
        }
        return createComplex(this.real, -this.imaginary);
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.complex.Complex divide(org.apache.commons.math.complex.Complex rhs) {
        if (isNaN() || rhs.isNaN()) {
            return NaN;
        }
        double c = rhs.getReal();
        double d = rhs.getImaginary();
        if (c == 0.0d && d == 0.0d) {
            return NaN;
        }
        if (rhs.isInfinite() && !isInfinite()) {
            return ZERO;
        }
        if (org.apache.commons.math.util.FastMath.abs(c) < org.apache.commons.math.util.FastMath.abs(d)) {
            double q = c / d;
            double denominator = (c * q) + d;
            return createComplex(((this.real * q) + this.imaginary) / denominator, ((this.imaginary * q) - this.real) / denominator);
        }
        double q2 = d / c;
        double denominator2 = (d * q2) + c;
        return createComplex(((this.imaginary * q2) + this.real) / denominator2, (this.imaginary - (this.real * q2)) / denominator2);
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof org.apache.commons.math.complex.Complex)) {
            return false;
        }
        org.apache.commons.math.complex.Complex rhs = (org.apache.commons.math.complex.Complex) other;
        if (rhs.isNaN()) {
            return isNaN();
        }
        return this.real == rhs.real && this.imaginary == rhs.imaginary;
    }

    public int hashCode() {
        if (isNaN()) {
            return 7;
        }
        return ((org.apache.commons.math.util.MathUtils.hash(this.imaginary) * 17) + org.apache.commons.math.util.MathUtils.hash(this.real)) * 37;
    }

    public double getImaginary() {
        return this.imaginary;
    }

    public double getReal() {
        return this.real;
    }

    public boolean isNaN() {
        return this.isNaN;
    }

    public boolean isInfinite() {
        return this.isInfinite;
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.complex.Complex multiply(org.apache.commons.math.complex.Complex rhs) {
        if (isNaN() || rhs.isNaN()) {
            return NaN;
        }
        if (java.lang.Double.isInfinite(this.real) || java.lang.Double.isInfinite(this.imaginary) || java.lang.Double.isInfinite(rhs.real) || java.lang.Double.isInfinite(rhs.imaginary)) {
            return INF;
        }
        return createComplex((this.real * rhs.real) - (this.imaginary * rhs.imaginary), (this.real * rhs.imaginary) + (this.imaginary * rhs.real));
    }

    public org.apache.commons.math.complex.Complex multiply(double rhs) {
        if (isNaN() || java.lang.Double.isNaN(rhs)) {
            return NaN;
        }
        if (java.lang.Double.isInfinite(this.real) || java.lang.Double.isInfinite(this.imaginary) || java.lang.Double.isInfinite(rhs)) {
            return INF;
        }
        return createComplex(this.real * rhs, this.imaginary * rhs);
    }

    public org.apache.commons.math.complex.Complex negate() {
        if (isNaN()) {
            return NaN;
        }
        return createComplex(-this.real, -this.imaginary);
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.complex.Complex subtract(org.apache.commons.math.complex.Complex rhs) {
        if (isNaN() || rhs.isNaN()) {
            return NaN;
        }
        return createComplex(this.real - rhs.getReal(), this.imaginary - rhs.getImaginary());
    }

    public org.apache.commons.math.complex.Complex acos() {
        if (isNaN()) {
            return NaN;
        }
        return add(sqrt1z().multiply(I)).log().multiply(I.negate());
    }

    public org.apache.commons.math.complex.Complex asin() {
        if (isNaN()) {
            return NaN;
        }
        return sqrt1z().add(multiply(I)).log().multiply(I.negate());
    }

    public org.apache.commons.math.complex.Complex atan() {
        if (isNaN()) {
            return NaN;
        }
        return add(I).divide(I.subtract(this)).log().multiply(I.divide(createComplex(2.0d, 0.0d)));
    }

    public org.apache.commons.math.complex.Complex cos() {
        if (isNaN()) {
            return NaN;
        }
        return createComplex(org.apache.commons.math.util.FastMath.cos(this.real) * org.apache.commons.math.util.MathUtils.cosh(this.imaginary), (-org.apache.commons.math.util.FastMath.sin(this.real)) * org.apache.commons.math.util.MathUtils.sinh(this.imaginary));
    }

    public org.apache.commons.math.complex.Complex cosh() {
        if (isNaN()) {
            return NaN;
        }
        return createComplex(org.apache.commons.math.util.MathUtils.cosh(this.real) * org.apache.commons.math.util.FastMath.cos(this.imaginary), org.apache.commons.math.util.MathUtils.sinh(this.real) * org.apache.commons.math.util.FastMath.sin(this.imaginary));
    }

    public org.apache.commons.math.complex.Complex exp() {
        if (isNaN()) {
            return NaN;
        }
        double expReal = org.apache.commons.math.util.FastMath.exp(this.real);
        return createComplex(org.apache.commons.math.util.FastMath.cos(this.imaginary) * expReal, org.apache.commons.math.util.FastMath.sin(this.imaginary) * expReal);
    }

    public org.apache.commons.math.complex.Complex log() {
        if (isNaN()) {
            return NaN;
        }
        return createComplex(org.apache.commons.math.util.FastMath.log(abs()), org.apache.commons.math.util.FastMath.atan2(this.imaginary, this.real));
    }

    public org.apache.commons.math.complex.Complex pow(org.apache.commons.math.complex.Complex x) {
        if (x == null) {
            throw new java.lang.NullPointerException();
        }
        return log().multiply(x).exp();
    }

    public org.apache.commons.math.complex.Complex sin() {
        if (isNaN()) {
            return NaN;
        }
        return createComplex(org.apache.commons.math.util.FastMath.sin(this.real) * org.apache.commons.math.util.MathUtils.cosh(this.imaginary), org.apache.commons.math.util.FastMath.cos(this.real) * org.apache.commons.math.util.MathUtils.sinh(this.imaginary));
    }

    public org.apache.commons.math.complex.Complex sinh() {
        if (isNaN()) {
            return NaN;
        }
        return createComplex(org.apache.commons.math.util.MathUtils.sinh(this.real) * org.apache.commons.math.util.FastMath.cos(this.imaginary), org.apache.commons.math.util.MathUtils.cosh(this.real) * org.apache.commons.math.util.FastMath.sin(this.imaginary));
    }

    public org.apache.commons.math.complex.Complex sqrt() {
        if (isNaN()) {
            return NaN;
        }
        if (this.real == 0.0d && this.imaginary == 0.0d) {
            return createComplex(0.0d, 0.0d);
        }
        double t = org.apache.commons.math.util.FastMath.sqrt((org.apache.commons.math.util.FastMath.abs(this.real) + abs()) / 2.0d);
        if (this.real >= 0.0d) {
            return createComplex(t, this.imaginary / (2.0d * t));
        }
        return createComplex(org.apache.commons.math.util.FastMath.abs(this.imaginary) / (2.0d * t), org.apache.commons.math.util.MathUtils.indicator(this.imaginary) * t);
    }

    public org.apache.commons.math.complex.Complex sqrt1z() {
        return createComplex(1.0d, 0.0d).subtract(multiply(this)).sqrt();
    }

    public org.apache.commons.math.complex.Complex tan() {
        if (isNaN()) {
            return NaN;
        }
        double real2 = this.real * 2.0d;
        double imaginary2 = this.imaginary * 2.0d;
        double d = org.apache.commons.math.util.FastMath.cos(real2) + org.apache.commons.math.util.MathUtils.cosh(imaginary2);
        return createComplex(org.apache.commons.math.util.FastMath.sin(real2) / d, org.apache.commons.math.util.MathUtils.sinh(imaginary2) / d);
    }

    public org.apache.commons.math.complex.Complex tanh() {
        if (isNaN()) {
            return NaN;
        }
        double real2 = this.real * 2.0d;
        double imaginary2 = this.imaginary * 2.0d;
        double d = org.apache.commons.math.util.MathUtils.cosh(real2) + org.apache.commons.math.util.FastMath.cos(imaginary2);
        return createComplex(org.apache.commons.math.util.MathUtils.sinh(real2) / d, org.apache.commons.math.util.FastMath.sin(imaginary2) / d);
    }

    public double getArgument() {
        return org.apache.commons.math.util.FastMath.atan2(getImaginary(), getReal());
    }

    public java.util.List<org.apache.commons.math.complex.Complex> nthRoot(int n) throws java.lang.IllegalArgumentException {
        int i = n;
        if (i <= 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_COMPUTE_NTH_ROOT_FOR_NEGATIVE_N, java.lang.Integer.valueOf(n));
        }
        java.util.List<org.apache.commons.math.complex.Complex> result = new java.util.ArrayList<>();
        if (isNaN()) {
            result.add(NaN);
            return result;
        }
        if (isInfinite()) {
            result.add(INF);
            return result;
        }
        double nthRootOfAbs = org.apache.commons.math.util.FastMath.pow(abs(), 1.0d / ((double) i));
        double nthPhi = getArgument() / ((double) i);
        double slice = 6.283185307179586d / ((double) i);
        double innerPart = nthPhi;
        int k = 0;
        while (k < i) {
            double realPart = org.apache.commons.math.util.FastMath.cos(innerPart) * nthRootOfAbs;
            double imaginaryPart = org.apache.commons.math.util.FastMath.sin(innerPart) * nthRootOfAbs;
            result.add(createComplex(realPart, imaginaryPart));
            innerPart += slice;
            k++;
            i = n;
        }
        return result;
    }

    protected org.apache.commons.math.complex.Complex createComplex(double realPart, double imaginaryPart) {
        return new org.apache.commons.math.complex.Complex(realPart, imaginaryPart);
    }

    protected final java.lang.Object readResolve() {
        return createComplex(this.real, this.imaginary);
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.Field<org.apache.commons.math.complex.Complex> getField() {
        return org.apache.commons.math.complex.ComplexField.getInstance();
    }
}
