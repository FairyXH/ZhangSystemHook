package org.apache.commons.math.fraction;

/* JADX INFO: loaded from: classes4.dex */
public class Fraction extends java.lang.Number implements org.apache.commons.math.FieldElement<org.apache.commons.math.fraction.Fraction>, java.lang.Comparable<org.apache.commons.math.fraction.Fraction>, java.io.Serializable {
    private static final long serialVersionUID = 3698073679419233275L;
    private final int denominator;
    private final int numerator;
    public static final org.apache.commons.math.fraction.Fraction TWO = new org.apache.commons.math.fraction.Fraction(2, 1);
    public static final org.apache.commons.math.fraction.Fraction ONE = new org.apache.commons.math.fraction.Fraction(1, 1);
    public static final org.apache.commons.math.fraction.Fraction ZERO = new org.apache.commons.math.fraction.Fraction(0, 1);
    public static final org.apache.commons.math.fraction.Fraction FOUR_FIFTHS = new org.apache.commons.math.fraction.Fraction(4, 5);
    public static final org.apache.commons.math.fraction.Fraction ONE_FIFTH = new org.apache.commons.math.fraction.Fraction(1, 5);
    public static final org.apache.commons.math.fraction.Fraction ONE_HALF = new org.apache.commons.math.fraction.Fraction(1, 2);
    public static final org.apache.commons.math.fraction.Fraction ONE_QUARTER = new org.apache.commons.math.fraction.Fraction(1, 4);
    public static final org.apache.commons.math.fraction.Fraction ONE_THIRD = new org.apache.commons.math.fraction.Fraction(1, 3);
    public static final org.apache.commons.math.fraction.Fraction THREE_FIFTHS = new org.apache.commons.math.fraction.Fraction(3, 5);
    public static final org.apache.commons.math.fraction.Fraction THREE_QUARTERS = new org.apache.commons.math.fraction.Fraction(3, 4);
    public static final org.apache.commons.math.fraction.Fraction TWO_FIFTHS = new org.apache.commons.math.fraction.Fraction(2, 5);
    public static final org.apache.commons.math.fraction.Fraction TWO_QUARTERS = new org.apache.commons.math.fraction.Fraction(2, 4);
    public static final org.apache.commons.math.fraction.Fraction TWO_THIRDS = new org.apache.commons.math.fraction.Fraction(2, 3);
    public static final org.apache.commons.math.fraction.Fraction MINUS_ONE = new org.apache.commons.math.fraction.Fraction(-1, 1);

    public Fraction(double value) throws org.apache.commons.math.fraction.FractionConversionException {
        this(value, 1.0E-5d, 100);
    }

    public Fraction(double value, double epsilon, int maxIterations) throws org.apache.commons.math.fraction.FractionConversionException {
        this(value, epsilon, Integer.MAX_VALUE, maxIterations);
    }

    public Fraction(double value, int maxDenominator) throws org.apache.commons.math.fraction.FractionConversionException {
        this(value, 0.0d, maxDenominator, 100);
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x00fc, code lost:
    
        throw new org.apache.commons.math.fraction.FractionConversionException(r42, r6, r14);
     */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00d5 A[LOOP:0: B:9:0x004c->B:32:0x00d5, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00ac A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private Fraction(double r42, double r44, int r46, int r47) throws org.apache.commons.math.fraction.FractionConversionException {
        /*
            Method dump skipped, instruction units count: 270
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math.fraction.Fraction.<init>(double, double, int, int):void");
    }

    public Fraction(int num) {
        this(num, 1);
    }

    public Fraction(int num, int den) {
        if (den == 0) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION, java.lang.Integer.valueOf(num), java.lang.Integer.valueOf(den));
        }
        if (den < 0) {
            if (num == Integer.MIN_VALUE || den == Integer.MIN_VALUE) {
                throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.OVERFLOW_IN_FRACTION, java.lang.Integer.valueOf(num), java.lang.Integer.valueOf(den));
            }
            num = -num;
            den = -den;
        }
        int d = org.apache.commons.math.util.MathUtils.gcd(num, den);
        if (d > 1) {
            num /= d;
            den /= d;
        }
        if (den < 0) {
            num = -num;
            den = -den;
        }
        this.numerator = num;
        this.denominator = den;
    }

    public org.apache.commons.math.fraction.Fraction abs() {
        if (this.numerator >= 0) {
            return this;
        }
        return negate();
    }

    @Override // java.lang.Comparable
    public int compareTo(org.apache.commons.math.fraction.Fraction object) {
        long nOd = ((long) this.numerator) * ((long) object.denominator);
        long dOn = ((long) this.denominator) * ((long) object.numerator);
        if (nOd < dOn) {
            return -1;
        }
        return nOd > dOn ? 1 : 0;
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return ((double) this.numerator) / ((double) this.denominator);
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof org.apache.commons.math.fraction.Fraction)) {
            return false;
        }
        org.apache.commons.math.fraction.Fraction rhs = (org.apache.commons.math.fraction.Fraction) other;
        return this.numerator == rhs.numerator && this.denominator == rhs.denominator;
    }

    @Override // java.lang.Number
    public float floatValue() {
        return (float) doubleValue();
    }

    public int getDenominator() {
        return this.denominator;
    }

    public int getNumerator() {
        return this.numerator;
    }

    public int hashCode() {
        return ((this.numerator + 629) * 37) + this.denominator;
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) doubleValue();
    }

    @Override // java.lang.Number
    public long longValue() {
        return (long) doubleValue();
    }

    public org.apache.commons.math.fraction.Fraction negate() {
        if (this.numerator == Integer.MIN_VALUE) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.OVERFLOW_IN_FRACTION, java.lang.Integer.valueOf(this.numerator), java.lang.Integer.valueOf(this.denominator));
        }
        return new org.apache.commons.math.fraction.Fraction(-this.numerator, this.denominator);
    }

    public org.apache.commons.math.fraction.Fraction reciprocal() {
        return new org.apache.commons.math.fraction.Fraction(this.denominator, this.numerator);
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.fraction.Fraction add(org.apache.commons.math.fraction.Fraction fraction) {
        return addSub(fraction, true);
    }

    public org.apache.commons.math.fraction.Fraction add(int i) {
        return new org.apache.commons.math.fraction.Fraction(this.numerator + (this.denominator * i), this.denominator);
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.fraction.Fraction subtract(org.apache.commons.math.fraction.Fraction fraction) {
        return addSub(fraction, false);
    }

    public org.apache.commons.math.fraction.Fraction subtract(int i) {
        return new org.apache.commons.math.fraction.Fraction(this.numerator - (this.denominator * i), this.denominator);
    }

    private org.apache.commons.math.fraction.Fraction addSub(org.apache.commons.math.fraction.Fraction fraction, boolean isAdd) {
        if (fraction == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FRACTION);
        }
        if (this.numerator == 0) {
            return isAdd ? fraction : fraction.negate();
        }
        if (fraction.numerator == 0) {
            return this;
        }
        int d1 = org.apache.commons.math.util.MathUtils.gcd(this.denominator, fraction.denominator);
        if (d1 == 1) {
            int uvp = org.apache.commons.math.util.MathUtils.mulAndCheck(this.numerator, fraction.denominator);
            int upv = org.apache.commons.math.util.MathUtils.mulAndCheck(fraction.numerator, this.denominator);
            return new org.apache.commons.math.fraction.Fraction(isAdd ? org.apache.commons.math.util.MathUtils.addAndCheck(uvp, upv) : org.apache.commons.math.util.MathUtils.subAndCheck(uvp, upv), org.apache.commons.math.util.MathUtils.mulAndCheck(this.denominator, fraction.denominator));
        }
        java.math.BigInteger uvp2 = java.math.BigInteger.valueOf(this.numerator).multiply(java.math.BigInteger.valueOf(fraction.denominator / d1));
        java.math.BigInteger upv2 = java.math.BigInteger.valueOf(fraction.numerator).multiply(java.math.BigInteger.valueOf(this.denominator / d1));
        java.math.BigInteger t = isAdd ? uvp2.add(upv2) : uvp2.subtract(upv2);
        int tmodd1 = t.mod(java.math.BigInteger.valueOf(d1)).intValue();
        int d2 = tmodd1 == 0 ? d1 : org.apache.commons.math.util.MathUtils.gcd(tmodd1, d1);
        java.math.BigInteger w = t.divide(java.math.BigInteger.valueOf(d2));
        if (w.bitLength() > 31) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.NUMERATOR_OVERFLOW_AFTER_MULTIPLY, w);
        }
        return new org.apache.commons.math.fraction.Fraction(w.intValue(), org.apache.commons.math.util.MathUtils.mulAndCheck(this.denominator / d1, fraction.denominator / d2));
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.fraction.Fraction multiply(org.apache.commons.math.fraction.Fraction fraction) {
        if (fraction == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FRACTION);
        }
        if (this.numerator == 0 || fraction.numerator == 0) {
            return ZERO;
        }
        int d1 = org.apache.commons.math.util.MathUtils.gcd(this.numerator, fraction.denominator);
        int d2 = org.apache.commons.math.util.MathUtils.gcd(fraction.numerator, this.denominator);
        return getReducedFraction(org.apache.commons.math.util.MathUtils.mulAndCheck(this.numerator / d1, fraction.numerator / d2), org.apache.commons.math.util.MathUtils.mulAndCheck(this.denominator / d2, fraction.denominator / d1));
    }

    public org.apache.commons.math.fraction.Fraction multiply(int i) {
        return new org.apache.commons.math.fraction.Fraction(this.numerator * i, this.denominator);
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.fraction.Fraction divide(org.apache.commons.math.fraction.Fraction fraction) {
        if (fraction == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FRACTION);
        }
        if (fraction.numerator == 0) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_FRACTION_TO_DIVIDE_BY, java.lang.Integer.valueOf(fraction.numerator), java.lang.Integer.valueOf(fraction.denominator));
        }
        return multiply(fraction.reciprocal());
    }

    public org.apache.commons.math.fraction.Fraction divide(int i) {
        return new org.apache.commons.math.fraction.Fraction(this.numerator, this.denominator * i);
    }

    public static org.apache.commons.math.fraction.Fraction getReducedFraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION, java.lang.Integer.valueOf(numerator), java.lang.Integer.valueOf(denominator));
        }
        if (numerator == 0) {
            return ZERO;
        }
        if (denominator == Integer.MIN_VALUE && (numerator & 1) == 0) {
            numerator /= 2;
            denominator /= 2;
        }
        if (denominator < 0) {
            if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
                throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.OVERFLOW_IN_FRACTION, java.lang.Integer.valueOf(numerator), java.lang.Integer.valueOf(denominator));
            }
            numerator = -numerator;
            denominator = -denominator;
        }
        int gcd = org.apache.commons.math.util.MathUtils.gcd(numerator, denominator);
        return new org.apache.commons.math.fraction.Fraction(numerator / gcd, denominator / gcd);
    }

    public java.lang.String toString() {
        if (this.denominator == 1) {
            java.lang.String str = java.lang.Integer.toString(this.numerator);
            return str;
        }
        if (this.numerator == 0) {
            return "0";
        }
        java.lang.String str2 = this.numerator + " / " + this.denominator;
        return str2;
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.Field<org.apache.commons.math.fraction.Fraction> getField() {
        return org.apache.commons.math.fraction.FractionField.getInstance();
    }
}
