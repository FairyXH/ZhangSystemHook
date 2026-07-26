package org.apache.commons.math.fraction;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractFormat extends java.text.NumberFormat implements java.io.Serializable {
    private static final long serialVersionUID = -6981118387974191891L;
    protected java.text.NumberFormat denominatorFormat;
    protected java.text.NumberFormat numeratorFormat;

    protected AbstractFormat() {
        this(getDefaultNumberFormat());
    }

    protected AbstractFormat(java.text.NumberFormat format) {
        this(format, (java.text.NumberFormat) format.clone());
    }

    protected AbstractFormat(java.text.NumberFormat numeratorFormat, java.text.NumberFormat denominatorFormat) {
        this.numeratorFormat = numeratorFormat;
        this.denominatorFormat = denominatorFormat;
    }

    protected static java.text.NumberFormat getDefaultNumberFormat() {
        return getDefaultNumberFormat(java.util.Locale.getDefault());
    }

    protected static java.text.NumberFormat getDefaultNumberFormat(java.util.Locale locale) {
        java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(locale);
        nf.setMaximumFractionDigits(0);
        nf.setParseIntegerOnly(true);
        return nf;
    }

    public java.text.NumberFormat getDenominatorFormat() {
        return this.denominatorFormat;
    }

    public java.text.NumberFormat getNumeratorFormat() {
        return this.numeratorFormat;
    }

    public void setDenominatorFormat(java.text.NumberFormat format) {
        if (format == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DENOMINATOR_FORMAT);
        }
        this.denominatorFormat = format;
    }

    public void setNumeratorFormat(java.text.NumberFormat format) {
        if (format == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NUMERATOR_FORMAT);
        }
        this.numeratorFormat = format;
    }

    protected static void parseAndIgnoreWhitespace(java.lang.String source, java.text.ParsePosition pos) {
        parseNextCharacter(source, pos);
        pos.setIndex(pos.getIndex() - 1);
    }

    protected static char parseNextCharacter(java.lang.String source, java.text.ParsePosition pos) {
        int index;
        char c;
        int index2 = pos.getIndex();
        int n = source.length();
        if (index2 >= n) {
            return (char) 0;
        }
        while (true) {
            index = index2 + 1;
            c = source.charAt(index2);
            if (!java.lang.Character.isWhitespace(c) || index >= n) {
                break;
            }
            index2 = index;
        }
        pos.setIndex(index);
        if (index >= n) {
            return (char) 0;
        }
        return c;
    }

    @Override // java.text.NumberFormat
    public java.lang.StringBuffer format(double value, java.lang.StringBuffer buffer, java.text.FieldPosition position) {
        return format(java.lang.Double.valueOf(value), buffer, position);
    }

    @Override // java.text.NumberFormat
    public java.lang.StringBuffer format(long value, java.lang.StringBuffer buffer, java.text.FieldPosition position) {
        return format(java.lang.Long.valueOf(value), buffer, position);
    }
}
