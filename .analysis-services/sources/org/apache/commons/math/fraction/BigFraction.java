package org.apache.commons.math.fraction;

/* JADX INFO: loaded from: classes4.dex */
public class BigFraction extends java.lang.Number implements org.apache.commons.math.FieldElement<org.apache.commons.math.fraction.BigFraction>, java.lang.Comparable<org.apache.commons.math.fraction.BigFraction>, java.io.Serializable {
    private static final long serialVersionUID = -5630213147331578515L;
    private final java.math.BigInteger denominator;
    private final java.math.BigInteger numerator;
    public static final org.apache.commons.math.fraction.BigFraction TWO = new org.apache.commons.math.fraction.BigFraction(2);
    public static final org.apache.commons.math.fraction.BigFraction ONE = new org.apache.commons.math.fraction.BigFraction(1);
    public static final org.apache.commons.math.fraction.BigFraction ZERO = new org.apache.commons.math.fraction.BigFraction(0);
    public static final org.apache.commons.math.fraction.BigFraction MINUS_ONE = new org.apache.commons.math.fraction.BigFraction(-1);
    public static final org.apache.commons.math.fraction.BigFraction FOUR_FIFTHS = new org.apache.commons.math.fraction.BigFraction(4, 5);
    public static final org.apache.commons.math.fraction.BigFraction ONE_FIFTH = new org.apache.commons.math.fraction.BigFraction(1, 5);
    public static final org.apache.commons.math.fraction.BigFraction ONE_HALF = new org.apache.commons.math.fraction.BigFraction(1, 2);
    public static final org.apache.commons.math.fraction.BigFraction ONE_QUARTER = new org.apache.commons.math.fraction.BigFraction(1, 4);
    public static final org.apache.commons.math.fraction.BigFraction ONE_THIRD = new org.apache.commons.math.fraction.BigFraction(1, 3);
    public static final org.apache.commons.math.fraction.BigFraction THREE_FIFTHS = new org.apache.commons.math.fraction.BigFraction(3, 5);
    public static final org.apache.commons.math.fraction.BigFraction THREE_QUARTERS = new org.apache.commons.math.fraction.BigFraction(3, 4);
    public static final org.apache.commons.math.fraction.BigFraction TWO_FIFTHS = new org.apache.commons.math.fraction.BigFraction(2, 5);
    public static final org.apache.commons.math.fraction.BigFraction TWO_QUARTERS = new org.apache.commons.math.fraction.BigFraction(2, 4);
    public static final org.apache.commons.math.fraction.BigFraction TWO_THIRDS = new org.apache.commons.math.fraction.BigFraction(2, 3);
    private static final java.math.BigInteger ONE_HUNDRED_DOUBLE = java.math.BigInteger.valueOf(100);

    public BigFraction(java.math.BigInteger num) {
        this(num, java.math.BigInteger.ONE);
    }

    public BigFraction(java.math.BigInteger num, java.math.BigInteger den) {
        if (num == null) {
            throw new java.lang.NullPointerException(org.apache.commons.math.exception.util.LocalizedFormats.NUMERATOR.getSourceString());
        }
        if (den == null) {
            throw new java.lang.NullPointerException(org.apache.commons.math.exception.util.LocalizedFormats.DENOMINATOR.getSourceString());
        }
        if (java.math.BigInteger.ZERO.equals(den)) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_DENOMINATOR, new java.lang.Object[0]);
        }
        if (java.math.BigInteger.ZERO.equals(num)) {
            this.numerator = java.math.BigInteger.ZERO;
            this.denominator = java.math.BigInteger.ONE;
            return;
        }
        java.math.BigInteger gcd = num.gcd(den);
        if (java.math.BigInteger.ONE.compareTo(gcd) < 0) {
            num = num.divide(gcd);
            den = den.divide(gcd);
        }
        if (java.math.BigInteger.ZERO.compareTo(den) > 0) {
            num = num.negate();
            den = den.negate();
        }
        this.numerator = num;
        this.denominator = den;
    }

    public BigFraction(double value) throws java.lang.IllegalArgumentException {
        if (java.lang.Double.isNaN(value)) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NAN_VALUE_CONVERSION, new java.lang.Object[0]);
        }
        if (java.lang.Double.isInfinite(value)) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INFINITE_VALUE_CONVERSION, new java.lang.Object[0]);
        }
        long bits = java.lang.Double.doubleToLongBits(value);
        long sign = Long.MIN_VALUE & bits;
        long exponent = 9218868437227405312L & bits;
        long m = 4503599627370495L & bits;
        m = exponent != 0 ? m | 4503599627370496L : m;
        m = sign != 0 ? -m : m;
        int k = ((int) (exponent >> 52)) - 1075;
        while ((9007199254740990L & m) != 0 && (1 & m) == 0) {
            m >>= 1;
            k++;
        }
        if (k < 0) {
            this.numerator = java.math.BigInteger.valueOf(m);
            this.denominator = java.math.BigInteger.ZERO.flipBit(-k);
        } else {
            this.numerator = java.math.BigInteger.valueOf(m).multiply(java.math.BigInteger.ZERO.flipBit(k));
            this.denominator = java.math.BigInteger.ONE;
        }
    }

    public BigFraction(double value, double epsilon, int maxIterations) throws org.apache.commons.math.fraction.FractionConversionException {
        this(value, epsilon, Integer.MAX_VALUE, maxIterations);
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x00de, code lost:
    
        throw new org.apache.commons.math.fraction.FractionConversionException(r40, r6, r3);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private BigFraction(double r40, double r42, int r44, int r45) throws org.apache.commons.math.fraction.FractionConversionException {
        /*
            Method dump skipped, instruction units count: 241
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math.fraction.BigFraction.<init>(double, double, int, int):void");
    }

    public BigFraction(double value, int maxDenominator) throws org.apache.commons.math.fraction.FractionConversionException {
        this(value, 0.0d, maxDenominator, 100);
    }

    public BigFraction(int num) {
        this(java.math.BigInteger.valueOf(num), java.math.BigInteger.ONE);
    }

    public BigFraction(int num, int den) {
        this(java.math.BigInteger.valueOf(num), java.math.BigInteger.valueOf(den));
    }

    public BigFraction(long num) {
        this(java.math.BigInteger.valueOf(num), java.math.BigInteger.ONE);
    }

    public BigFraction(long num, long den) {
        this(java.math.BigInteger.valueOf(num), java.math.BigInteger.valueOf(den));
    }

    public static org.apache.commons.math.fraction.BigFraction getReducedFraction(int numerator, int denominator) {
        if (numerator == 0) {
            return ZERO;
        }
        return new org.apache.commons.math.fraction.BigFraction(numerator, denominator);
    }

    public org.apache.commons.math.fraction.BigFraction abs() {
        return java.math.BigInteger.ZERO.compareTo(this.numerator) <= 0 ? this : negate();
    }

    public org.apache.commons.math.fraction.BigFraction add(java.math.BigInteger bg) {
        return new org.apache.commons.math.fraction.BigFraction(this.numerator.add(this.denominator.multiply(bg)), this.denominator);
    }

    public org.apache.commons.math.fraction.BigFraction add(int i) {
        return add(java.math.BigInteger.valueOf(i));
    }

    public org.apache.commons.math.fraction.BigFraction add(long l) {
        return add(java.math.BigInteger.valueOf(l));
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.fraction.BigFraction add(org.apache.commons.math.fraction.BigFraction fraction) {
        java.math.BigInteger num;
        java.math.BigInteger den;
        if (fraction == null) {
            throw new java.lang.NullPointerException(org.apache.commons.math.exception.util.LocalizedFormats.FRACTION.getSourceString());
        }
        if (ZERO.equals(fraction)) {
            return this;
        }
        if (this.denominator.equals(fraction.denominator)) {
            num = this.numerator.add(fraction.numerator);
            den = this.denominator;
        } else {
            num = this.numerator.multiply(fraction.denominator).add(fraction.numerator.multiply(this.denominator));
            den = this.denominator.multiply(fraction.denominator);
        }
        return new org.apache.commons.math.fraction.BigFraction(num, den);
    }

    public java.math.BigDecimal bigDecimalValue() {
        return new java.math.BigDecimal(this.numerator).divide(new java.math.BigDecimal(this.denominator));
    }

    public java.math.BigDecimal bigDecimalValue(int roundingMode) {
        return new java.math.BigDecimal(this.numerator).divide(new java.math.BigDecimal(this.denominator), roundingMode);
    }

    public java.math.BigDecimal bigDecimalValue(int scale, int roundingMode) {
        return new java.math.BigDecimal(this.numerator).divide(new java.math.BigDecimal(this.denominator), scale, roundingMode);
    }

    @Override // java.lang.Comparable
    public int compareTo(org.apache.commons.math.fraction.BigFraction object) {
        java.math.BigInteger nOd = this.numerator.multiply(object.denominator);
        java.math.BigInteger dOn = this.denominator.multiply(object.numerator);
        return nOd.compareTo(dOn);
    }

    public org.apache.commons.math.fraction.BigFraction divide(java.math.BigInteger bg) {
        if (java.math.BigInteger.ZERO.equals(bg)) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_DENOMINATOR, new java.lang.Object[0]);
        }
        return new org.apache.commons.math.fraction.BigFraction(this.numerator, this.denominator.multiply(bg));
    }

    public org.apache.commons.math.fraction.BigFraction divide(int i) {
        return divide(java.math.BigInteger.valueOf(i));
    }

    public org.apache.commons.math.fraction.BigFraction divide(long l) {
        return divide(java.math.BigInteger.valueOf(l));
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.fraction.BigFraction divide(org.apache.commons.math.fraction.BigFraction fraction) {
        if (fraction == null) {
            throw new java.lang.NullPointerException(org.apache.commons.math.exception.util.LocalizedFormats.FRACTION.getSourceString());
        }
        if (java.math.BigInteger.ZERO.equals(fraction.numerator)) {
            throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_DENOMINATOR, new java.lang.Object[0]);
        }
        return multiply(fraction.reciprocal());
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return this.numerator.doubleValue() / this.denominator.doubleValue();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof org.apache.commons.math.fraction.BigFraction)) {
            return false;
        }
        org.apache.commons.math.fraction.BigFraction rhs = ((org.apache.commons.math.fraction.BigFraction) other).reduce();
        org.apache.commons.math.fraction.BigFraction thisOne = reduce();
        boolean ret = thisOne.numerator.equals(rhs.numerator) && thisOne.denominator.equals(rhs.denominator);
        return ret;
    }

    @Override // java.lang.Number
    public float floatValue() {
        return this.numerator.floatValue() / this.denominator.floatValue();
    }

    public java.math.BigInteger getDenominator() {
        return this.denominator;
    }

    public int getDenominatorAsInt() {
        return this.denominator.intValue();
    }

    public long getDenominatorAsLong() {
        return this.denominator.longValue();
    }

    public java.math.BigInteger getNumerator() {
        return this.numerator;
    }

    public int getNumeratorAsInt() {
        return this.numerator.intValue();
    }

    public long getNumeratorAsLong() {
        return this.numerator.longValue();
    }

    public int hashCode() {
        return ((this.numerator.hashCode() + 629) * 37) + this.denominator.hashCode();
    }

    @Override // java.lang.Number
    public int intValue() {
        return this.numerator.divide(this.denominator).intValue();
    }

    @Override // java.lang.Number
    public long longValue() {
        return this.numerator.divide(this.denominator).longValue();
    }

    public org.apache.commons.math.fraction.BigFraction multiply(java.math.BigInteger bg) {
        if (bg == null) {
            throw new java.lang.NullPointerException();
        }
        return new org.apache.commons.math.fraction.BigFraction(bg.multiply(this.numerator), this.denominator);
    }

    public org.apache.commons.math.fraction.BigFraction multiply(int i) {
        return multiply(java.math.BigInteger.valueOf(i));
    }

    public org.apache.commons.math.fraction.BigFraction multiply(long l) {
        return multiply(java.math.BigInteger.valueOf(l));
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.fraction.BigFraction multiply(org.apache.commons.math.fraction.BigFraction fraction) {
        if (fraction == null) {
            throw new java.lang.NullPointerException(org.apache.commons.math.exception.util.LocalizedFormats.FRACTION.getSourceString());
        }
        if (this.numerator.equals(java.math.BigInteger.ZERO) || fraction.numerator.equals(java.math.BigInteger.ZERO)) {
            return ZERO;
        }
        return new org.apache.commons.math.fraction.BigFraction(this.numerator.multiply(fraction.numerator), this.denominator.multiply(fraction.denominator));
    }

    public org.apache.commons.math.fraction.BigFraction negate() {
        return new org.apache.commons.math.fraction.BigFraction(this.numerator.negate(), this.denominator);
    }

    public double percentageValue() {
        return this.numerator.divide(this.denominator).multiply(ONE_HUNDRED_DOUBLE).doubleValue();
    }

    public org.apache.commons.math.fraction.BigFraction pow(int exponent) {
        if (exponent < 0) {
            return new org.apache.commons.math.fraction.BigFraction(this.denominator.pow(-exponent), this.numerator.pow(-exponent));
        }
        return new org.apache.commons.math.fraction.BigFraction(this.numerator.pow(exponent), this.denominator.pow(exponent));
    }

    public org.apache.commons.math.fraction.BigFraction pow(long exponent) {
        if (exponent < 0) {
            return new org.apache.commons.math.fraction.BigFraction(org.apache.commons.math.util.MathUtils.pow(this.denominator, -exponent), org.apache.commons.math.util.MathUtils.pow(this.numerator, -exponent));
        }
        return new org.apache.commons.math.fraction.BigFraction(org.apache.commons.math.util.MathUtils.pow(this.numerator, exponent), org.apache.commons.math.util.MathUtils.pow(this.denominator, exponent));
    }

    public org.apache.commons.math.fraction.BigFraction pow(java.math.BigInteger exponent) {
        if (exponent.compareTo(java.math.BigInteger.ZERO) < 0) {
            java.math.BigInteger eNeg = exponent.negate();
            return new org.apache.commons.math.fraction.BigFraction(org.apache.commons.math.util.MathUtils.pow(this.denominator, eNeg), org.apache.commons.math.util.MathUtils.pow(this.numerator, eNeg));
        }
        return new org.apache.commons.math.fraction.BigFraction(org.apache.commons.math.util.MathUtils.pow(this.numerator, exponent), org.apache.commons.math.util.MathUtils.pow(this.denominator, exponent));
    }

    public double pow(double exponent) {
        return org.apache.commons.math.util.FastMath.pow(this.numerator.doubleValue(), exponent) / org.apache.commons.math.util.FastMath.pow(this.denominator.doubleValue(), exponent);
    }

    public org.apache.commons.math.fraction.BigFraction reciprocal() {
        return new org.apache.commons.math.fraction.BigFraction(this.denominator, this.numerator);
    }

    public org.apache.commons.math.fraction.BigFraction reduce() {
        java.math.BigInteger gcd = this.numerator.gcd(this.denominator);
        return new org.apache.commons.math.fraction.BigFraction(this.numerator.divide(gcd), this.denominator.divide(gcd));
    }

    public org.apache.commons.math.fraction.BigFraction subtract(java.math.BigInteger bg) {
        if (bg == null) {
            throw new java.lang.NullPointerException();
        }
        return new org.apache.commons.math.fraction.BigFraction(this.numerator.subtract(this.denominator.multiply(bg)), this.denominator);
    }

    public org.apache.commons.math.fraction.BigFraction subtract(int i) {
        return subtract(java.math.BigInteger.valueOf(i));
    }

    public org.apache.commons.math.fraction.BigFraction subtract(long l) {
        return subtract(java.math.BigInteger.valueOf(l));
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.fraction.BigFraction subtract(org.apache.commons.math.fraction.BigFraction fraction) {
        java.math.BigInteger num;
        java.math.BigInteger den;
        if (fraction == null) {
            throw new java.lang.NullPointerException(org.apache.commons.math.exception.util.LocalizedFormats.FRACTION.getSourceString());
        }
        if (ZERO.equals(fraction)) {
            return this;
        }
        if (this.denominator.equals(fraction.denominator)) {
            num = this.numerator.subtract(fraction.numerator);
            den = this.denominator;
        } else {
            num = this.numerator.multiply(fraction.denominator).subtract(fraction.numerator.multiply(this.denominator));
            den = this.denominator.multiply(fraction.denominator);
        }
        return new org.apache.commons.math.fraction.BigFraction(num, den);
    }

    public java.lang.String toString() {
        if (java.math.BigInteger.ONE.equals(this.denominator)) {
            java.lang.String str = this.numerator.toString();
            return str;
        }
        if (java.math.BigInteger.ZERO.equals(this.numerator)) {
            return "0";
        }
        java.lang.String str2 = this.numerator + " / " + this.denominator;
        return str2;
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.Field<org.apache.commons.math.fraction.BigFraction> getField() {
        return org.apache.commons.math.fraction.BigFractionField.getInstance();
    }
}
