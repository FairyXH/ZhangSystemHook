package org.apache.commons.math.complex;

/* JADX INFO: loaded from: classes4.dex */
public class ComplexFormat extends org.apache.commons.math.util.CompositeFormat {
    private static final java.lang.String DEFAULT_IMAGINARY_CHARACTER = "i";
    private static final long serialVersionUID = -3343698360149467646L;
    private java.lang.String imaginaryCharacter;
    private java.text.NumberFormat imaginaryFormat;
    private java.text.NumberFormat realFormat;

    public ComplexFormat() {
        this(DEFAULT_IMAGINARY_CHARACTER, getDefaultNumberFormat());
    }

    public ComplexFormat(java.text.NumberFormat format) {
        this(DEFAULT_IMAGINARY_CHARACTER, format);
    }

    public ComplexFormat(java.text.NumberFormat realFormat, java.text.NumberFormat imaginaryFormat) {
        this(DEFAULT_IMAGINARY_CHARACTER, realFormat, imaginaryFormat);
    }

    public ComplexFormat(java.lang.String imaginaryCharacter) {
        this(imaginaryCharacter, getDefaultNumberFormat());
    }

    public ComplexFormat(java.lang.String imaginaryCharacter, java.text.NumberFormat format) {
        this(imaginaryCharacter, format, (java.text.NumberFormat) format.clone());
    }

    public ComplexFormat(java.lang.String imaginaryCharacter, java.text.NumberFormat realFormat, java.text.NumberFormat imaginaryFormat) {
        setImaginaryCharacter(imaginaryCharacter);
        setImaginaryFormat(imaginaryFormat);
        setRealFormat(realFormat);
    }

    public static java.util.Locale[] getAvailableLocales() {
        return java.text.NumberFormat.getAvailableLocales();
    }

    public static java.lang.String formatComplex(org.apache.commons.math.complex.Complex c) {
        return getInstance().format(c);
    }

    public java.lang.StringBuffer format(org.apache.commons.math.complex.Complex complex, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        double re = complex.getReal();
        formatDouble(re, getRealFormat(), toAppendTo, pos);
        double im = complex.getImaginary();
        if (im < 0.0d) {
            toAppendTo.append(" - ");
            formatDouble(-im, getImaginaryFormat(), toAppendTo, pos);
            toAppendTo.append(getImaginaryCharacter());
        } else if (im > 0.0d || java.lang.Double.isNaN(im)) {
            toAppendTo.append(" + ");
            formatDouble(im, getImaginaryFormat(), toAppendTo, pos);
            toAppendTo.append(getImaginaryCharacter());
        }
        return toAppendTo;
    }

    @Override // java.text.Format
    public java.lang.StringBuffer format(java.lang.Object obj, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
        if (obj instanceof org.apache.commons.math.complex.Complex) {
            java.lang.StringBuffer ret = format((org.apache.commons.math.complex.Complex) obj, toAppendTo, pos);
            return ret;
        }
        if (obj instanceof java.lang.Number) {
            java.lang.StringBuffer ret2 = format(new org.apache.commons.math.complex.Complex(((java.lang.Number) obj).doubleValue(), 0.0d), toAppendTo, pos);
            return ret2;
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_FORMAT_INSTANCE_AS_COMPLEX, obj.getClass().getName());
    }

    public java.lang.String getImaginaryCharacter() {
        return this.imaginaryCharacter;
    }

    public java.text.NumberFormat getImaginaryFormat() {
        return this.imaginaryFormat;
    }

    public static org.apache.commons.math.complex.ComplexFormat getInstance() {
        return getInstance(java.util.Locale.getDefault());
    }

    public static org.apache.commons.math.complex.ComplexFormat getInstance(java.util.Locale locale) {
        java.text.NumberFormat f = getDefaultNumberFormat(locale);
        return new org.apache.commons.math.complex.ComplexFormat(f);
    }

    public java.text.NumberFormat getRealFormat() {
        return this.realFormat;
    }

    public org.apache.commons.math.complex.Complex parse(java.lang.String source) throws java.text.ParseException {
        java.text.ParsePosition parsePosition = new java.text.ParsePosition(0);
        org.apache.commons.math.complex.Complex result = parse(source, parsePosition);
        if (parsePosition.getIndex() == 0) {
            throw org.apache.commons.math.MathRuntimeException.createParseException(parsePosition.getErrorIndex(), org.apache.commons.math.exception.util.LocalizedFormats.UNPARSEABLE_COMPLEX_NUMBER, source);
        }
        return result;
    }

    public org.apache.commons.math.complex.Complex parse(java.lang.String source, java.text.ParsePosition pos) {
        int sign;
        int initialIndex = pos.getIndex();
        parseAndIgnoreWhitespace(source, pos);
        java.lang.Number re = parseNumber(source, getRealFormat(), pos);
        if (re == null) {
            pos.setIndex(initialIndex);
            return null;
        }
        int startIndex = pos.getIndex();
        char c = parseNextCharacter(source, pos);
        switch (c) {
            case 0:
                return new org.apache.commons.math.complex.Complex(re.doubleValue(), 0.0d);
            case '+':
                sign = 1;
                break;
            case '-':
                sign = -1;
                break;
            default:
                pos.setIndex(initialIndex);
                pos.setErrorIndex(startIndex);
                return null;
        }
        parseAndIgnoreWhitespace(source, pos);
        java.lang.Number im = parseNumber(source, getRealFormat(), pos);
        if (im == null) {
            pos.setIndex(initialIndex);
            return null;
        }
        if (!parseFixedstring(source, getImaginaryCharacter(), pos)) {
            return null;
        }
        return new org.apache.commons.math.complex.Complex(re.doubleValue(), im.doubleValue() * ((double) sign));
    }

    @Override // java.text.Format
    public java.lang.Object parseObject(java.lang.String source, java.text.ParsePosition pos) {
        return parse(source, pos);
    }

    public void setImaginaryCharacter(java.lang.String imaginaryCharacter) {
        if (imaginaryCharacter == null || imaginaryCharacter.length() == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_STRING_FOR_IMAGINARY_CHARACTER, new java.lang.Object[0]);
        }
        this.imaginaryCharacter = imaginaryCharacter;
    }

    public void setImaginaryFormat(java.text.NumberFormat imaginaryFormat) {
        if (imaginaryFormat == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.IMAGINARY_FORMAT);
        }
        this.imaginaryFormat = imaginaryFormat;
    }

    public void setRealFormat(java.text.NumberFormat realFormat) {
        if (realFormat == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.REAL_FORMAT);
        }
        this.realFormat = realFormat;
    }
}
