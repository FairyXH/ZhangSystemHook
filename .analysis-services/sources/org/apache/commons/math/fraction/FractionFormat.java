package org.apache.commons.math.fraction;

/* JADX INFO: loaded from: classes4.dex */
public class FractionFormat extends org.apache.commons.math.fraction.AbstractFormat {
    private static final long serialVersionUID = 3008655719530972611L;

    public FractionFormat() {
    }

    public FractionFormat(java.text.NumberFormat format) {
        super(format);
    }

    public FractionFormat(java.text.NumberFormat numeratorFormat, java.text.NumberFormat denominatorFormat) {
        super(numeratorFormat, denominatorFormat);
    }

    public static java.util.Locale[] getAvailableLocales() {
        return java.text.NumberFormat.getAvailableLocales();
    }

    public static java.lang.String formatFraction(org.apache.commons.math.fraction.Fraction f) {
        return getImproperInstance().format(f);
    }

    public static org.apache.commons.math.fraction.FractionFormat getImproperInstance() {
        return getImproperInstance(java.util.Locale.getDefault());
    }

    public static org.apache.commons.math.fraction.FractionFormat getImproperInstance(java.util.Locale locale) {
        return new org.apache.commons.math.fraction.FractionFormat(getDefaultNumberFormat(locale));
    }

    public static org.apache.commons.math.fraction.FractionFormat getProperInstance() {
        return getProperInstance(java.util.Locale.getDefault());
    }

    public static org.apache.commons.math.fraction.FractionFormat getProperInstance(java.util.Locale locale) {
        return new org.apache.commons.math.fraction.ProperFractionFormat(getDefaultNumberFormat(locale));
    }

    protected static java.text.NumberFormat getDefaultNumberFormat() {
        return getDefaultNumberFormat(java.util.Locale.getDefault());
    }

    public java.lang.StringBuffer format(org.apache.commons.math.fraction.Fraction fraction, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        getNumeratorFormat().format(fraction.getNumerator(), toAppendTo, pos);
        toAppendTo.append(" / ");
        getDenominatorFormat().format(fraction.getDenominator(), toAppendTo, pos);
        return toAppendTo;
    }

    @Override // java.text.NumberFormat, java.text.Format
    public java.lang.StringBuffer format(java.lang.Object obj, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
        if (obj instanceof org.apache.commons.math.fraction.Fraction) {
            java.lang.StringBuffer ret = format((org.apache.commons.math.fraction.Fraction) obj, toAppendTo, pos);
            return ret;
        }
        if (obj instanceof java.lang.Number) {
            try {
                java.lang.StringBuffer ret2 = format(new org.apache.commons.math.fraction.Fraction(((java.lang.Number) obj).doubleValue()), toAppendTo, pos);
                return ret2;
            } catch (org.apache.commons.math.ConvergenceException ex) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_CONVERT_OBJECT_TO_FRACTION, ex.getLocalizedMessage());
            }
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_FORMAT_OBJECT_TO_FRACTION, new java.lang.Object[0]);
    }

    @Override // java.text.NumberFormat
    public org.apache.commons.math.fraction.Fraction parse(java.lang.String source) throws java.text.ParseException {
        java.text.ParsePosition parsePosition = new java.text.ParsePosition(0);
        org.apache.commons.math.fraction.Fraction result = parse(source, parsePosition);
        if (parsePosition.getIndex() == 0) {
            throw org.apache.commons.math.MathRuntimeException.createParseException(parsePosition.getErrorIndex(), org.apache.commons.math.exception.util.LocalizedFormats.UNPARSEABLE_FRACTION_NUMBER, source);
        }
        return result;
    }

    @Override // java.text.NumberFormat
    public org.apache.commons.math.fraction.Fraction parse(java.lang.String source, java.text.ParsePosition pos) {
        int initialIndex = pos.getIndex();
        parseAndIgnoreWhitespace(source, pos);
        java.lang.Number num = getNumeratorFormat().parse(source, pos);
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
                java.lang.Number den = getDenominatorFormat().parse(source, pos);
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
}
