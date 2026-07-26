package org.apache.commons.math.fraction;

/* JADX INFO: loaded from: classes4.dex */
public class ProperFractionFormat extends org.apache.commons.math.fraction.FractionFormat {
    private static final long serialVersionUID = 760934726031766749L;
    private java.text.NumberFormat wholeFormat;

    public ProperFractionFormat() {
        this(getDefaultNumberFormat());
    }

    public ProperFractionFormat(java.text.NumberFormat format) {
        this(format, (java.text.NumberFormat) format.clone(), (java.text.NumberFormat) format.clone());
    }

    public ProperFractionFormat(java.text.NumberFormat wholeFormat, java.text.NumberFormat numeratorFormat, java.text.NumberFormat denominatorFormat) {
        super(numeratorFormat, denominatorFormat);
        setWholeFormat(wholeFormat);
    }

    @Override // org.apache.commons.math.fraction.FractionFormat
    public java.lang.StringBuffer format(org.apache.commons.math.fraction.Fraction fraction, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        int num = fraction.getNumerator();
        int den = fraction.getDenominator();
        int whole = num / den;
        int num2 = num % den;
        if (whole != 0) {
            getWholeFormat().format(whole, toAppendTo, pos);
            toAppendTo.append(' ');
            num2 = java.lang.Math.abs(num2);
        }
        getNumeratorFormat().format(num2, toAppendTo, pos);
        toAppendTo.append(" / ");
        getDenominatorFormat().format(den, toAppendTo, pos);
        return toAppendTo;
    }

    public java.text.NumberFormat getWholeFormat() {
        return this.wholeFormat;
    }

    @Override // org.apache.commons.math.fraction.FractionFormat, java.text.NumberFormat
    public org.apache.commons.math.fraction.Fraction parse(java.lang.String source, java.text.ParsePosition pos) {
        org.apache.commons.math.fraction.Fraction ret = super.parse(source, pos);
        if (ret != null) {
            return ret;
        }
        int initialIndex = pos.getIndex();
        parseAndIgnoreWhitespace(source, pos);
        java.lang.Number whole = getWholeFormat().parse(source, pos);
        if (whole == null) {
            pos.setIndex(initialIndex);
            return null;
        }
        parseAndIgnoreWhitespace(source, pos);
        java.lang.Number num = getNumeratorFormat().parse(source, pos);
        if (num == null) {
            pos.setIndex(initialIndex);
            return null;
        }
        if (num.intValue() < 0) {
            pos.setIndex(initialIndex);
            return null;
        }
        int startIndex = pos.getIndex();
        char c = parseNextCharacter(source, pos);
        switch (c) {
            case 0:
                return new org.apache.commons.math.fraction.Fraction(num.intValue(), 1);
            case '/':
                parseAndIgnoreWhitespace(source, pos);
                java.lang.Number den = getDenominatorFormat().parse(source, pos);
                if (den == null) {
                    pos.setIndex(initialIndex);
                    return null;
                }
                if (den.intValue() < 0) {
                    pos.setIndex(initialIndex);
                    return null;
                }
                int w = whole.intValue();
                int n = num.intValue();
                int d = den.intValue();
                return new org.apache.commons.math.fraction.Fraction(((java.lang.Math.abs(w) * d) + n) * org.apache.commons.math.util.MathUtils.sign(w), d);
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
