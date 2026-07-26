package org.apache.commons.math.util;

/* JADX INFO: loaded from: classes4.dex */
public abstract class CompositeFormat extends java.text.Format {
    private static final long serialVersionUID = 5358685519349262494L;

    protected static java.text.NumberFormat getDefaultNumberFormat() {
        return getDefaultNumberFormat(java.util.Locale.getDefault());
    }

    protected static java.text.NumberFormat getDefaultNumberFormat(java.util.Locale locale) {
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(locale);
        nf.setMaximumFractionDigits(2);
        return nf;
    }

    protected void parseAndIgnoreWhitespace(java.lang.String source, java.text.ParsePosition pos) {
        parseNextCharacter(source, pos);
        pos.setIndex(pos.getIndex() - 1);
    }

    protected char parseNextCharacter(java.lang.String source, java.text.ParsePosition pos) {
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

    private java.lang.Number parseNumber(java.lang.String source, double value, java.text.ParsePosition pos) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append('(');
        sb.append(value);
        sb.append(')');
        int n = sb.length();
        int startIndex = pos.getIndex();
        int endIndex = startIndex + n;
        if (endIndex >= source.length() || source.substring(startIndex, endIndex).compareTo(sb.toString()) != 0) {
            return null;
        }
        java.lang.Number ret = java.lang.Double.valueOf(value);
        pos.setIndex(endIndex);
        return ret;
    }

    protected java.lang.Number parseNumber(java.lang.String source, java.text.NumberFormat format, java.text.ParsePosition pos) {
        int startIndex = pos.getIndex();
        java.lang.Number number = format.parse(source, pos);
        int endIndex = pos.getIndex();
        if (startIndex == endIndex) {
            double[] special = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
            for (int i = 0; i < special.length && (number = parseNumber(source, special[i], pos)) == null; i++) {
            }
        }
        return number;
    }

    protected boolean parseFixedstring(java.lang.String source, java.lang.String expected, java.text.ParsePosition pos) {
        int startIndex = pos.getIndex();
        int endIndex = expected.length() + startIndex;
        if (startIndex >= source.length() || endIndex > source.length() || source.substring(startIndex, endIndex).compareTo(expected) != 0) {
            pos.setIndex(startIndex);
            pos.setErrorIndex(startIndex);
            return false;
        }
        pos.setIndex(endIndex);
        return true;
    }

    protected java.lang.StringBuffer formatDouble(double value, java.text.NumberFormat format, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
        if (java.lang.Double.isNaN(value) || java.lang.Double.isInfinite(value)) {
            toAppendTo.append('(');
            toAppendTo.append(value);
            toAppendTo.append(')');
        } else {
            format.format(value, toAppendTo, pos);
        }
        return toAppendTo;
    }
}
