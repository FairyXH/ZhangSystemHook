package org.apache.commons.math.geometry;

/* JADX INFO: loaded from: classes4.dex */
public class Vector3DFormat extends org.apache.commons.math.util.CompositeFormat {
    private static final java.lang.String DEFAULT_PREFIX = "{";
    private static final java.lang.String DEFAULT_SEPARATOR = "; ";
    private static final java.lang.String DEFAULT_SUFFIX = "}";
    private static final long serialVersionUID = -5447606608652576301L;
    private final java.text.NumberFormat format;
    private final java.lang.String prefix;
    private final java.lang.String separator;
    private final java.lang.String suffix;
    private final java.lang.String trimmedPrefix;
    private final java.lang.String trimmedSeparator;
    private final java.lang.String trimmedSuffix;

    public Vector3DFormat() {
        this(DEFAULT_PREFIX, DEFAULT_SUFFIX, DEFAULT_SEPARATOR, getDefaultNumberFormat());
    }

    public Vector3DFormat(java.text.NumberFormat format) {
        this(DEFAULT_PREFIX, DEFAULT_SUFFIX, DEFAULT_SEPARATOR, format);
    }

    public Vector3DFormat(java.lang.String prefix, java.lang.String suffix, java.lang.String separator) {
        this(prefix, suffix, separator, getDefaultNumberFormat());
    }

    public Vector3DFormat(java.lang.String prefix, java.lang.String suffix, java.lang.String separator, java.text.NumberFormat format) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.separator = separator;
        this.trimmedPrefix = prefix.trim();
        this.trimmedSuffix = suffix.trim();
        this.trimmedSeparator = separator.trim();
        this.format = format;
    }

    public static java.util.Locale[] getAvailableLocales() {
        return java.text.NumberFormat.getAvailableLocales();
    }

    public java.lang.String getPrefix() {
        return this.prefix;
    }

    public java.lang.String getSuffix() {
        return this.suffix;
    }

    public java.lang.String getSeparator() {
        return this.separator;
    }

    public java.text.NumberFormat getFormat() {
        return this.format;
    }

    public static org.apache.commons.math.geometry.Vector3DFormat getInstance() {
        return getInstance(java.util.Locale.getDefault());
    }

    public static org.apache.commons.math.geometry.Vector3DFormat getInstance(java.util.Locale locale) {
        return new org.apache.commons.math.geometry.Vector3DFormat(getDefaultNumberFormat(locale));
    }

    public static java.lang.String formatVector3D(org.apache.commons.math.geometry.Vector3D v) {
        return getInstance().format(v);
    }

    public java.lang.StringBuffer format(org.apache.commons.math.geometry.Vector3D vector, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        toAppendTo.append(this.prefix);
        formatDouble(vector.getX(), this.format, toAppendTo, pos);
        toAppendTo.append(this.separator);
        formatDouble(vector.getY(), this.format, toAppendTo, pos);
        toAppendTo.append(this.separator);
        formatDouble(vector.getZ(), this.format, toAppendTo, pos);
        toAppendTo.append(this.suffix);
        return toAppendTo;
    }

    @Override // java.text.Format
    public java.lang.StringBuffer format(java.lang.Object obj, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
        if (obj instanceof org.apache.commons.math.geometry.Vector3D) {
            return format((org.apache.commons.math.geometry.Vector3D) obj, toAppendTo, pos);
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_FORMAT_INSTANCE_AS_3D_VECTOR, obj.getClass().getName());
    }

    public org.apache.commons.math.geometry.Vector3D parse(java.lang.String source) throws java.text.ParseException {
        java.text.ParsePosition parsePosition = new java.text.ParsePosition(0);
        org.apache.commons.math.geometry.Vector3D result = parse(source, parsePosition);
        if (parsePosition.getIndex() == 0) {
            throw org.apache.commons.math.MathRuntimeException.createParseException(parsePosition.getErrorIndex(), org.apache.commons.math.exception.util.LocalizedFormats.UNPARSEABLE_3D_VECTOR, source);
        }
        return result;
    }

    public org.apache.commons.math.geometry.Vector3D parse(java.lang.String source, java.text.ParsePosition pos) {
        int initialIndex = pos.getIndex();
        parseAndIgnoreWhitespace(source, pos);
        if (!parseFixedstring(source, this.trimmedPrefix, pos)) {
            return null;
        }
        parseAndIgnoreWhitespace(source, pos);
        java.lang.Number x = parseNumber(source, this.format, pos);
        if (x == null) {
            pos.setIndex(initialIndex);
            return null;
        }
        parseAndIgnoreWhitespace(source, pos);
        if (!parseFixedstring(source, this.trimmedSeparator, pos)) {
            return null;
        }
        parseAndIgnoreWhitespace(source, pos);
        java.lang.Number y = parseNumber(source, this.format, pos);
        if (y == null) {
            pos.setIndex(initialIndex);
            return null;
        }
        parseAndIgnoreWhitespace(source, pos);
        if (!parseFixedstring(source, this.trimmedSeparator, pos)) {
            return null;
        }
        parseAndIgnoreWhitespace(source, pos);
        java.lang.Number z = parseNumber(source, this.format, pos);
        if (z == null) {
            pos.setIndex(initialIndex);
            return null;
        }
        parseAndIgnoreWhitespace(source, pos);
        if (parseFixedstring(source, this.trimmedSuffix, pos)) {
            return new org.apache.commons.math.geometry.Vector3D(x.doubleValue(), y.doubleValue(), z.doubleValue());
        }
        return null;
    }

    @Override // java.text.Format
    public java.lang.Object parseObject(java.lang.String source, java.text.ParsePosition pos) {
        return parse(source, pos);
    }
}
