package org.apache.commons.math.linear;

/* JADX INFO: loaded from: classes4.dex */
public class RealVectorFormat extends org.apache.commons.math.util.CompositeFormat {
    private static final java.lang.String DEFAULT_PREFIX = "{";
    private static final java.lang.String DEFAULT_SEPARATOR = "; ";
    private static final java.lang.String DEFAULT_SUFFIX = "}";
    private static final long serialVersionUID = -708767813036157690L;
    private final java.text.NumberFormat format;
    private final java.lang.String prefix;
    private final java.lang.String separator;
    private final java.lang.String suffix;
    private final java.lang.String trimmedPrefix;
    private final java.lang.String trimmedSeparator;
    private final java.lang.String trimmedSuffix;

    public RealVectorFormat() {
        this(DEFAULT_PREFIX, DEFAULT_SUFFIX, DEFAULT_SEPARATOR, getDefaultNumberFormat());
    }

    public RealVectorFormat(java.text.NumberFormat format) {
        this(DEFAULT_PREFIX, DEFAULT_SUFFIX, DEFAULT_SEPARATOR, format);
    }

    public RealVectorFormat(java.lang.String prefix, java.lang.String suffix, java.lang.String separator) {
        this(prefix, suffix, separator, getDefaultNumberFormat());
    }

    public RealVectorFormat(java.lang.String prefix, java.lang.String suffix, java.lang.String separator, java.text.NumberFormat format) {
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

    public static org.apache.commons.math.linear.RealVectorFormat getInstance() {
        return getInstance(java.util.Locale.getDefault());
    }

    public static org.apache.commons.math.linear.RealVectorFormat getInstance(java.util.Locale locale) {
        return new org.apache.commons.math.linear.RealVectorFormat(getDefaultNumberFormat(locale));
    }

    public static java.lang.String formatRealVector(org.apache.commons.math.linear.RealVector v) {
        return getInstance().format(v);
    }

    public java.lang.StringBuffer format(org.apache.commons.math.linear.RealVector vector, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        toAppendTo.append(this.prefix);
        for (int i = 0; i < vector.getDimension(); i++) {
            if (i > 0) {
                toAppendTo.append(this.separator);
            }
            formatDouble(vector.getEntry(i), this.format, toAppendTo, pos);
        }
        toAppendTo.append(this.suffix);
        return toAppendTo;
    }

    @Override // java.text.Format
    public java.lang.StringBuffer format(java.lang.Object obj, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
        if (obj instanceof org.apache.commons.math.linear.RealVector) {
            return format((org.apache.commons.math.linear.RealVector) obj, toAppendTo, pos);
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_FORMAT_INSTANCE_AS_REAL_VECTOR, obj.getClass().getName());
    }

    public org.apache.commons.math.linear.ArrayRealVector parse(java.lang.String source) throws java.text.ParseException {
        java.text.ParsePosition parsePosition = new java.text.ParsePosition(0);
        org.apache.commons.math.linear.ArrayRealVector result = parse(source, parsePosition);
        if (parsePosition.getIndex() == 0) {
            throw org.apache.commons.math.MathRuntimeException.createParseException(parsePosition.getErrorIndex(), org.apache.commons.math.exception.util.LocalizedFormats.UNPARSEABLE_REAL_VECTOR, source);
        }
        return result;
    }

    public org.apache.commons.math.linear.ArrayRealVector parse(java.lang.String source, java.text.ParsePosition pos) {
        int initialIndex = pos.getIndex();
        parseAndIgnoreWhitespace(source, pos);
        if (!parseFixedstring(source, this.trimmedPrefix, pos)) {
            return null;
        }
        java.util.List<java.lang.Number> components = new java.util.ArrayList<>();
        boolean loop = true;
        while (loop) {
            if (!components.isEmpty()) {
                parseAndIgnoreWhitespace(source, pos);
                if (!parseFixedstring(source, this.trimmedSeparator, pos)) {
                    loop = false;
                }
            }
            if (loop) {
                parseAndIgnoreWhitespace(source, pos);
                java.lang.Number component = parseNumber(source, this.format, pos);
                if (component != null) {
                    components.add(component);
                } else {
                    pos.setIndex(initialIndex);
                    return null;
                }
            }
        }
        parseAndIgnoreWhitespace(source, pos);
        if (!parseFixedstring(source, this.trimmedSuffix, pos)) {
            return null;
        }
        double[] data = new double[components.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = components.get(i).doubleValue();
        }
        return new org.apache.commons.math.linear.ArrayRealVector(data, false);
    }

    @Override // java.text.Format
    public java.lang.Object parseObject(java.lang.String source, java.text.ParsePosition pos) {
        return parse(source, pos);
    }
}
