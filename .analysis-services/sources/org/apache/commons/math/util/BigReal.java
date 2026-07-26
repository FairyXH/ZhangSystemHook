package org.apache.commons.math.util;

/* JADX INFO: loaded from: classes4.dex */
public class BigReal implements org.apache.commons.math.FieldElement<org.apache.commons.math.util.BigReal>, java.lang.Comparable<org.apache.commons.math.util.BigReal>, java.io.Serializable {
    private static final long serialVersionUID = 4984534880991310382L;
    private final java.math.BigDecimal d;
    private java.math.RoundingMode roundingMode = java.math.RoundingMode.HALF_UP;
    private int scale = 64;
    public static final org.apache.commons.math.util.BigReal ZERO = new org.apache.commons.math.util.BigReal(java.math.BigDecimal.ZERO);
    public static final org.apache.commons.math.util.BigReal ONE = new org.apache.commons.math.util.BigReal(java.math.BigDecimal.ONE);

    public BigReal(java.math.BigDecimal val) {
        this.d = val;
    }

    public BigReal(java.math.BigInteger val) {
        this.d = new java.math.BigDecimal(val);
    }

    public BigReal(java.math.BigInteger unscaledVal, int scale) {
        this.d = new java.math.BigDecimal(unscaledVal, scale);
    }

    public BigReal(java.math.BigInteger unscaledVal, int scale, java.math.MathContext mc) {
        this.d = new java.math.BigDecimal(unscaledVal, scale, mc);
    }

    public BigReal(java.math.BigInteger val, java.math.MathContext mc) {
        this.d = new java.math.BigDecimal(val, mc);
    }

    public BigReal(char[] in) {
        this.d = new java.math.BigDecimal(in);
    }

    public BigReal(char[] in, int offset, int len) {
        this.d = new java.math.BigDecimal(in, offset, len);
    }

    public BigReal(char[] in, int offset, int len, java.math.MathContext mc) {
        this.d = new java.math.BigDecimal(in, offset, len, mc);
    }

    public BigReal(char[] in, java.math.MathContext mc) {
        this.d = new java.math.BigDecimal(in, mc);
    }

    public BigReal(double val) {
        this.d = new java.math.BigDecimal(val);
    }

    public BigReal(double val, java.math.MathContext mc) {
        this.d = new java.math.BigDecimal(val, mc);
    }

    public BigReal(int val) {
        this.d = new java.math.BigDecimal(val);
    }

    public BigReal(int val, java.math.MathContext mc) {
        this.d = new java.math.BigDecimal(val, mc);
    }

    public BigReal(long val) {
        this.d = new java.math.BigDecimal(val);
    }

    public BigReal(long val, java.math.MathContext mc) {
        this.d = new java.math.BigDecimal(val, mc);
    }

    public BigReal(java.lang.String val) {
        this.d = new java.math.BigDecimal(val);
    }

    public BigReal(java.lang.String val, java.math.MathContext mc) {
        this.d = new java.math.BigDecimal(val, mc);
    }

    public java.math.RoundingMode getRoundingMode() {
        return this.roundingMode;
    }

    public void setRoundingMode(java.math.RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.util.BigReal add(org.apache.commons.math.util.BigReal a) {
        return new org.apache.commons.math.util.BigReal(this.d.add(a.d));
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.util.BigReal subtract(org.apache.commons.math.util.BigReal a) {
        return new org.apache.commons.math.util.BigReal(this.d.subtract(a.d));
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.util.BigReal divide(org.apache.commons.math.util.BigReal a) throws java.lang.ArithmeticException {
        return new org.apache.commons.math.util.BigReal(this.d.divide(a.d, this.scale, this.roundingMode));
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.util.BigReal multiply(org.apache.commons.math.util.BigReal a) {
        return new org.apache.commons.math.util.BigReal(this.d.multiply(a.d));
    }

    @Override // java.lang.Comparable
    public int compareTo(org.apache.commons.math.util.BigReal a) {
        return this.d.compareTo(a.d);
    }

    public double doubleValue() {
        return this.d.doubleValue();
    }

    public java.math.BigDecimal bigDecimalValue() {
        return this.d;
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof org.apache.commons.math.util.BigReal) {
            return this.d.equals(((org.apache.commons.math.util.BigReal) other).d);
        }
        return false;
    }

    public int hashCode() {
        return this.d.hashCode();
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.Field<org.apache.commons.math.util.BigReal> getField() {
        return org.apache.commons.math.util.BigRealField.getInstance();
    }
}
