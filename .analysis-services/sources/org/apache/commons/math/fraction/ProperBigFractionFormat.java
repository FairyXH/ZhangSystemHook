package org.apache.commons.math.fraction;

/* JADX INFO: loaded from: classes4.dex */
public class ProperBigFractionFormat extends org.apache.commons.math.fraction.BigFractionFormat {
    private static final long serialVersionUID = -6337346779577272307L;
    private java.text.NumberFormat wholeFormat;

    public ProperBigFractionFormat() {
        this(getDefaultNumberFormat());
    }

    public ProperBigFractionFormat(java.text.NumberFormat format) {
        this(format, (java.text.NumberFormat) format.clone(), (java.text.NumberFormat) format.clone());
    }

    public ProperBigFractionFormat(java.text.NumberFormat wholeFormat, java.text.NumberFormat numeratorFormat, java.text.NumberFormat denominatorFormat) {
        super(numeratorFormat, denominatorFormat);
        setWholeFormat(wholeFormat);
    }

    @Override // org.apache.commons.math.fraction.BigFractionFormat
    public java.lang.StringBuffer format(org.apache.commons.math.fraction.BigFraction fraction, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        java.math.BigInteger num = fraction.getNumerator();
        java.math.BigInteger den = fraction.getDenominator();
        java.math.BigInteger whole = num.divide(den);
        java.math.BigInteger num2 = num.remainder(den);
        if (!java.math.BigInteger.ZERO.equals(whole)) {
            getWholeFormat().format(whole, toAppendTo, pos);
            toAppendTo.append(' ');
            if (num2.compareTo(java.math.BigInteger.ZERO) < 0) {
                num2 = num2.negate();
            }
        }
        getNumeratorFormat().format(num2, toAppendTo, pos);
        toAppendTo.append(" / ");
        getDenominatorFormat().format(den, toAppendTo, pos);
        return toAppendTo;
    }

    public java.text.NumberFormat getWholeFormat() {
        return this.wholeFormat;
    }

    @Override // org.apache.commons.math.fraction.BigFractionFormat, java.text.NumberFormat
    public org.apache.commons.math.fraction.BigFraction parse(java.lang.String source, java.text.ParsePosition pos) {
        org.apache.commons.math.fraction.BigFraction ret = super.parse(source, pos);
        if (ret != null) {
            return ret;
        }
        int initialIndex = pos.getIndex();
        parseAndIgnoreWhitespace(source, pos);
        java.math.BigInteger whole = parseNextBigInteger(source, pos);
        if (whole == null) {
            pos.setIndex(initialIndex);
            return null;
        }
        parseAndIgnoreWhitespace(source, pos);
        java.math.BigInteger num = parseNextBigInteger(source, pos);
        if (num == null) {
            pos.setIndex(initialIndex);
            return null;
        }
        if (num.compareTo(java.math.BigInteger.ZERO) < 0) {
            pos.setIndex(initialIndex);
            return null;
        }
        int startIndex = pos.getIndex();
        char c = parseNextCharacter(source, pos);
        switch (c) {
            case 0:
                return new org.apache.commons.math.fraction.BigFraction(num);
            case '/':
                parseAndIgnoreWhitespace(source, pos);
                java.math.BigInteger den = parseNextBigInteger(source, pos);
                if (den == null) {
                    pos.setIndex(initialIndex);
                    return null;
                }
                if (den.compareTo(java.math.BigInteger.ZERO) < 0) {
                    pos.setIndex(initialIndex);
                    return null;
                }
                boolean wholeIsNeg = whole.compareTo(java.math.BigInteger.ZERO) < 0;
                if (wholeIsNeg) {
                    whole = whole.negate();
                }
                java.math.BigInteger num2 = whole.multiply(den).add(num);
                if (wholeIsNeg) {
                    num2 = num2.negate();
                }
                return new org.apache.commons.math.fraction.BigFraction(num2, den);
            default:
                pos.setIndex(initialIndex);
                pos.setErrorIndex(startIndex);
                return null;
        }
    }

    public void setWholeFormat(java.text.NumberFormat format) {
        if (format == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.WHOLE_FORMAT);
        }
        this.wholeFormat = format;
    }
}
