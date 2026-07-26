package org.apache.commons.math.fraction;

/* JADX INFO: loaded from: classes4.dex */
public class BigFractionFormat extends org.apache.commons.math.fraction.AbstractFormat implements java.io.Serializable {
    private static final long serialVersionUID = -2932167925527338976L;

    public BigFractionFormat() {
    }

    public BigFractionFormat(java.text.NumberFormat format) {
        super(format);
    }

    public BigFractionFormat(java.text.NumberFormat numeratorFormat, java.text.NumberFormat denominatorFormat) {
        super(numeratorFormat, denominatorFormat);
    }

    public static java.util.Locale[] getAvailableLocales() {
        return java.text.NumberFormat.getAvailableLocales();
    }

    public static java.lang.String formatBigFraction(org.apache.commons.math.fraction.BigFraction f) {
        return getImproperInstance().format(f);
    }

    public static org.apache.commons.math.fraction.BigFractionFormat getImproperInstance() {
        return getImproperInstance(java.util.Locale.getDefault());
    }

    public static org.apache.commons.math.fraction.BigFractionFormat getImproperInstance(java.util.Locale locale) {
        return new org.apache.commons.math.fraction.BigFractionFormat(getDefaultNumberFormat(locale));
    }

    public static org.apache.commons.math.fraction.BigFractionFormat getProperInstance() {
        return getProperInstance(java.util.Locale.getDefault());
    }

    public static org.apache.commons.math.fraction.BigFractionFormat getProperInstance(java.util.Locale locale) {
        return new org.apache.commons.math.fraction.ProperBigFractionFormat(getDefaultNumberFormat(locale));
    }

    public java.lang.StringBuffer format(org.apache.commons.math.fraction.BigFraction BigFraction, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        getNumeratorFormat().format(BigFraction.getNumerator(), toAppendTo, pos);
        toAppendTo.append(" / ");
        getDenominatorFormat().format(BigFraction.getDenominator(), toAppendTo, pos);
        return toAppendTo;
    }

    @Override // java.text.NumberFormat, java.text.Format
    public java.lang.StringBuffer format(java.lang.Object obj, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
        if (obj instanceof org.apache.commons.math.fraction.BigFraction) {
            java.lang.StringBuffer ret = format((org.apache.commons.math.fraction.BigFraction) obj, toAppendTo, pos);
            return ret;
        }
        if (obj instanceof java.math.BigInteger) {
            java.lang.StringBuffer ret2 = format(new org.apache.commons.math.fraction.BigFraction((java.math.BigInteger) obj), toAppendTo, pos);
            return ret2;
        }
        if (obj instanceof java.lang.Number) {
            java.lang.StringBuffer ret3 = format(new org.apache.commons.math.fraction.BigFraction(((java.lang.Number) obj).doubleValue()), toAppendTo, pos);
            return ret3;
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_FORMAT_OBJECT_TO_FRACTION, new java.lang.Object[0]);
    }

    @Override // java.text.NumberFormat
    public org.apache.commons.math.fraction.BigFraction parse(java.lang.String source) throws java.text.ParseException {
        java.text.ParsePosition parsePosition = new java.text.ParsePosition(0);
        org.apache.commons.math.fraction.BigFraction result = parse(source, parsePosition);
        if (parsePosition.getIndex() == 0) {
            throw org.apache.commons.math.MathRuntimeException.createParseException(parsePosition.getErrorIndex(), org.apache.commons.math.exception.util.LocalizedFormats.UNPARSEABLE_FRACTION_NUMBER, source);
        }
        return result;
    }

    @Override // java.text.NumberFormat
    public org.apache.commons.math.fraction.BigFraction parse(java.lang.String source, java.text.ParsePosition pos) {
        int initialIndex = pos.getIndex();
        parseAndIgnoreWhitespace(source, pos);
        java.math.BigInteger num = parseNextBigInteger(source, pos);
        if (num == null) {
            pos.setIndex(initialIndex);
            return null;
        }
        int startIndex = pos.getIndex();
        char c = parseNextCharacter(source, pos);
        switch (c) {
            case 0:
                break;
            case '/':
                parseAndIgnoreWhitespace(source, pos);
                java.math.BigInteger den = parseNextBigInteger(source, pos);
                if (den == null) {
                    pos.setIndex(initialIndex);
                }
                break;
            default:
                pos.setIndex(initialIndex);
                pos.setErrorIndex(startIndex);
                break;
        }
        return null;
    }

    protected java.math.BigInteger parseNextBigInteger(java.lang.String source, java.text.ParsePosition pos) {
        int start = pos.getIndex();
        int end = source.charAt(start) == '-' ? start + 1 : start;
        while (end < source.length() && java.lang.Character.isDigit(source.charAt(end))) {
            end++;
        }
        try {
            java.math.BigInteger n = new java.math.BigInteger(source.substring(start, end));
            pos.setIndex(end);
            return n;
        } catch (java.lang.NumberFormatException e) {
            pos.setErrorIndex(start);
            return null;
        }
    }
}
